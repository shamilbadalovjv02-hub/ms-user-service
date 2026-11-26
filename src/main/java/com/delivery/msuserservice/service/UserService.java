package com.delivery.msuserservice.service;

import com.delivery.msuserservice.dto.common.UserCommonDto;
import com.delivery.msuserservice.dto.request.LoginRequest;
import com.delivery.msuserservice.dto.request.UserRequest;
import com.delivery.msuserservice.dto.response.UserResponse;

public interface UserService {
    UserResponse register(UserRequest request);
    UserResponse getUserById(Long id);
    String login(LoginRequest request);
    UserCommonDto getCommonUser(Long id);
}
