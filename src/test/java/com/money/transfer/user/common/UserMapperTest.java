package com.money.transfer.user.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.money.transfer.user.domain.User;
import com.money.transfer.user.domain.entity.UserEntity;
import com.money.transfer.user.presentation.request.UserResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void userEntity_toUser_success() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final UserEntity entity = new UserEntity("name", "email@test.com", "password");

        // when
        final User user = userMapper.toUser(entity);

        // then
        final User expected = new User(1L, "name", "email@test.com", "password", now, now);

        assertThat(user).usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(expected);
    }

    @Test
    void user_toResponse_success() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final User user = new User(1L, "name", "email@test.com", "password", now, now);

        // when
        final UserResponse response = userMapper.toResponse(user);

        // then
        final UserResponse expected = new UserResponse(1L, "name", "email@test.com", now, now);

        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }
}