package com.sanfernando.sanfernando.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sanfernando.sanfernando.dtos.requests.LoginRequest;
import com.sanfernando.sanfernando.dtos.responses.LoginResponse;
import com.sanfernando.sanfernando.services.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  
  @Autowired
  private AuthService authService;

  @PostMapping(value = "login")
  public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }
}
