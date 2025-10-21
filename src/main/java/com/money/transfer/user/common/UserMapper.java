package com.money.transfer.user.common;

import com.money.transfer.user.domain.User;
import com.money.transfer.user.domain.entity.UserEntity;
import com.money.transfer.user.presentation.request.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(final UserEntity userEntity);

    UserResponse toResponse(final User user);
}
