package com.sachin.blog.services;

import com.sachin.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
