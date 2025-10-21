package com.money.transfer.user.application;

import com.money.transfer.authentication.domain.PasswordEncoder;
import com.money.transfer.exception.UserException;
import com.money.transfer.user.common.UserMapper;
import com.money.transfer.user.domain.User;
import com.money.transfer.user.domain.UserEntityRepository;
import com.money.transfer.user.domain.entity.UserEntity;
import com.money.transfer.user.presentation.request.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MessageSource messageSource;

    private final UserEntityRepository userRepository;

    @Transactional
    public User join(final UserJoinRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserException(
                    HttpStatus.BAD_REQUEST,
                    messageSource.getMessage(
                            "user.email.duplicate",new String[]{request.email()}, LocaleContextHolder.getLocale()));
        }

        final String encodedPassword = PasswordEncoder.encode(request.password());

        final UserEntity savedEntity = userRepository.save(new UserEntity(request.name(), request.email(), encodedPassword));

        return UserMapper.INSTANCE.toUser(savedEntity);
    }
}
