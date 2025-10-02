package com.money.transfer.user.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.money.transfer.user.domain.UserEntityRepository;
import com.money.transfer.user.presentation.request.UserJoinRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userRepository;

    @Test
    @Transactional
    void join_success() {
        // given
        UserJoinRequest request = new UserJoinRequest("abcd", "abcd@test.com", "Password!1");

        // when
        userService.join(request);

        // then
        var savedUser = userRepository.findByEmail("abcd@test.com").orElseThrow();

        assertThat(savedUser.getName()).isEqualTo("abcd");
        assertThat(savedUser.getEmail()).isEqualTo("abcd@test.com");
        assertThat(savedUser.getPassword()).isNotBlank();
    }

}