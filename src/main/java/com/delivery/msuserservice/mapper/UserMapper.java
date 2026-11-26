package com.delivery.msuserservice.mapper;

import com.delivery.msuserservice.dto.request.UserRequest;
import com.delivery.msuserservice.dto.response.UserResponse;
import com.delivery.msuserservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
}
