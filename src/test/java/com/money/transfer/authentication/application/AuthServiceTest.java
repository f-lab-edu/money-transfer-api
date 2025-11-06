package com.money.transfer.authentication.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.money.transfer.common.MessageResolver;
import com.money.transfer.exception.AuthException;
import com.money.transfer.user.domain.UserEntityRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.javamail.JavaMailSender;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private MessageResolver messageResolver;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private Cache cache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache("emailAuthCode")).thenReturn(cache);
    }

    @Test
    void sendEmail_fail_duplicateEmail() {
        // given
        final String email = "test@test.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(mock()));
        when(messageResolver.getExceptionMessage("auth.email.duplicate", email))
                .thenReturn("이미 가입된 이메일입니다.");

        // when & then
        assertThatThrownBy(() -> authService.sendEmail(email))
                .isInstanceOf(AuthException.class)
                .hasMessage("이미 가입된 이메일입니다.");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendEmail_success() {
        // given
        final String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        final MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        authService.sendEmail(email);

        // then
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendEmail_fail_sendException() {
        // given
        final String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(mailSender.createMimeMessage()).thenAnswer(invocation -> {
            throw new MessagingException("메일 전송 실패");
        });
        when(messageResolver.getExceptionMessage("auth.email.sendFail"))
                .thenReturn("메일 전송 실패");

        // when & then
        assertThatThrownBy(() -> authService.sendEmail(email))
                .isInstanceOf(AuthException.class)
                .hasMessage("메일 전송 실패");

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void verifyEmail_fail_noCachedCode() {
        // given
        final String email = "test@test.com";
        final String inputCode = "123456";

        when(cache.get("AUTH:EMAIL:" + email, String.class)).thenReturn(null);
        when(messageResolver.getExceptionMessage("auth.email.codeExpired"))
                .thenReturn("인증 코드가 만료되었습니다.");

        // when & then
        assertThatThrownBy(() -> authService.verifyEmail(email, inputCode))
                .isInstanceOf(AuthException.class)
                .hasMessage("인증 코드가 만료되었습니다.");

        verify(cache, never()).evict(email);
    }

    @Test
    void verifyEmail_fail_invalidCode() {
        // given
        final String email = "test@test.com";
        final String inputCode = "123456";

        when(cache.get("AUTH:EMAIL:" + email, String.class)).thenReturn("999999");
        when(messageResolver.getExceptionMessage("auth.email.codeInvalid"))
                .thenReturn("인증 코드가 올바르지 않습니다.");

        // when & then
        assertThatThrownBy(() -> authService.verifyEmail(email, inputCode))
                .isInstanceOf(AuthException.class)
                .hasMessage("인증 코드가 올바르지 않습니다.");

        verify(cache, never()).evict(email);
    }

    @Test
    void verifyEmail_success() {
        // given
        final String email = "test@test.com";
        final String inputCode = "654321";

        when(cache.get("AUTH:EMAIL:" + email, String.class)).thenReturn("654321");

        // when
        authService.verifyEmail(email, inputCode);

        // then
        verify(cache, times(1)).evict("AUTH:EMAIL:" + email);
    }
}
