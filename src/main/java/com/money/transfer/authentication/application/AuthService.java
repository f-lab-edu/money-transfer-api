package com.money.transfer.authentication.application;

import com.money.transfer.common.MessageResolver;
import com.money.transfer.exception.AuthException;
import com.money.transfer.user.domain.UserEntityRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JavaMailSender mailSender;

    private final CacheManager cacheManager;

    private final MessageResolver messageResolver;

    private final UserEntityRepository userEntityRepository;

    public void sendEmail(final String email) {
        if (userEntityRepository.findByEmail(email).isPresent()) {
            throw new AuthException(
                    HttpStatus.CONFLICT,
                    messageResolver.getExceptionMessage("auth.email.duplicate", email));
        }

        sendEmailWithCode(email, generateAuthCode(email));
    }

    @CachePut(value = "emailAuthCode", key = "'AUTH:EMAIL:' + #email")
    public String generateAuthCode(final String email) {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

    private void sendEmailWithCode(final String toEmail, final String authCode) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[Money-Transfer] 이메일 인증 코드");
            helper.setText("""
                    <div style="font-family:Arial,sans-serif; line-height:1.5;">
                        <h2>이메일 인증 코드</h2>
                        <p>아래 인증 코드를 입력해주세요:</p>
                        <h3>%s</h3>
                        <p>이 코드는 2분 동안 유효합니다.</p>
                    </div>
                    """.formatted(authCode), true);

            mailSender.send(message);
        } catch (final MessagingException e) {
            throw new AuthException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageResolver.getExceptionMessage("auth.email.sendFail"));
        }
    }

    public void verifyEmail(final String email, final String authCode) {
        final Cache cache = cacheManager.getCache("emailAuthCode");
        final String cachedAuthCode = cache.get("AUTH:EMAIL:" + email, String.class);

        if (cachedAuthCode == null) {
            throw new AuthException(
                    HttpStatus.GONE,
                    messageResolver.getExceptionMessage("auth.email.codeExpired"));
        }

        if (!cachedAuthCode.equals(authCode)) {
            throw new AuthException(
                    HttpStatus.BAD_REQUEST,
                    messageResolver.getExceptionMessage("auth.email.codeInvalid"));
        }

        cache.evict("AUTH:EMAIL:" + email);
    }
}
