package com.delivery.msuserservice.service;

import com.delivery.msuserservice.annotation.RedisCache;
import com.delivery.msuserservice.dto.common.UserCommonDto;
import com.delivery.msuserservice.dto.request.LoginRequest;
import com.delivery.msuserservice.dto.request.UserRequest;
import com.delivery.msuserservice.dto.response.UserResponse;
import com.delivery.msuserservice.entity.User;
import com.delivery.msuserservice.exception.InvalidPasswordException;
import com.delivery.msuserservice.exception.UserNotFoundException;
import com.delivery.msuserservice.mapper.UserMapper;
import com.delivery.msuserservice.repository.UserRepository;
import com.delivery.msuserservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse register(UserRequest request) {
        User user = userMapper.toEntity(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @RedisCache(key = "user_by_id_", ttl = 600)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }

    @Override
    @RedisCache(key = "common_user_", ttl = 600)
    public UserCommonDto getCommonUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserCommonDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername());
        }

        throw new InvalidPasswordException("Password is incorrect");
    }


}
