package com.sanfernando.sanfernando.services;

import com.sanfernando.sanfernando.dtos.requests.LoginRequest;
import com.sanfernando.sanfernando.dtos.responses.LoginResponse;

public interface AuthService {
  public LoginResponse login(LoginRequest loginRequest);
}
