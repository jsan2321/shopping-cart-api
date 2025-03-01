package com.ecoapi.goodshopping.service.user;

import com.ecoapi.goodshopping.dto.UserDto;
import com.ecoapi.goodshopping.model.User;
import com.ecoapi.goodshopping.request.CreateUserRequest;
import com.ecoapi.goodshopping.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
