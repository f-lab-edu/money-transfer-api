package com.money.transfer.user.application;

import com.money.transfer.user.common.UserMapper;
import com.money.transfer.user.domain.User;
import com.money.transfer.user.domain.UserEntityRepository;
import com.money.transfer.user.presentation.request.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;

    @Transactional
    public void join(final UserJoinRequest request) {
        final User user = new User(request.name(), request.email(), request.password());

        userRepository.save(UserMapper.INSTANCE.toEntity(user));
    }
}
