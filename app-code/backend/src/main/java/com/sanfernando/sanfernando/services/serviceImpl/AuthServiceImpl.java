package com.sanfernando.sanfernando.services.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sanfernando.sanfernando.dao.UserDao;
import com.sanfernando.sanfernando.dtos.requests.LoginRequest;
import com.sanfernando.sanfernando.dtos.responses.LoginResponse;
import com.sanfernando.sanfernando.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService{
  
  @Autowired
  private UserDao userDao;

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    return userDao.login(loginRequest);
  }

}
