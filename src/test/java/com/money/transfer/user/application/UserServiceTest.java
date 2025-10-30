package com.money.transfer.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.money.transfer.common.MessageResolver;
import com.money.transfer.exception.UserException;
import com.money.transfer.user.common.UserMapper;
import com.money.transfer.user.domain.User;
import com.money.transfer.user.domain.UserEntityRepository;
import com.money.transfer.user.domain.entity.UserEntity;
import com.money.transfer.user.presentation.request.UserJoinRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private MessageResolver messageResolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userService = new UserService(messageResolver, userRepository);
    }

    @Test
    void join_success() {
        // given
        final UserJoinRequest request = new UserJoinRequest("abcd", "abcd@test.com", "Password!1");

        final UserEntity savedEntity = new UserEntity("abcd", "abcd@test.com", "hashedPassword");

        when(userRepository.findByEmail("abcd@test.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        // when
        final User user = userService.join(request);

        // then
        assertThat(user.getName()).isEqualTo("abcd");
        assertThat(user.getEmail()).isEqualTo("abcd@test.com");
        assertThat(user.getPassword()).isNotEmpty();
    }

    @Test
    void join_fail_duplicateEmail() {
        // given
        final UserJoinRequest request = new UserJoinRequest("abcd", "abcd@test.com", "Password!1");

        final UserEntity savedEntity = new UserEntity("abcd", "abcd@test.com", "hashedPassword");

        when(userRepository.findByEmail("abcd@test.com"))
                .thenReturn(Optional.of(savedEntity));

        // when & then
        assertThatThrownBy(() -> userService.join(request))
                .isInstanceOf(UserException.class)
                .hasMessage(
                        messageResolver.getExceptionMessage(
                                "user.email.duplicate",
                                request.email()
                        )
                );

        verify(userRepository, never()).save(any(UserEntity.class));
    }
}