package com.money.transfer.user.application;

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
    public void join(UserJoinRequest request) {
        //TODO: 비밀번호 암호화, 이메일 검증
        User user = new User(request.name(), request.email(), request.password());

        userRepository.findByEmail(request.email())
                .ifPresent(nonUser -> {
                    throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
                });

        userRepository.save(user.toEntity());
    }
}
