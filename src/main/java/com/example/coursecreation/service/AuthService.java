package com.example.coursecreation.service;


import com.example.coursecreation.dto.UserDetailsDto;

public interface AuthService {

    public UserDetailsDto getUserDetailsFromAuthService(String serviceUrl, String token);

    public Boolean isAdmin(String role);
    public Boolean isStudent(String role);
    public Boolean isTeacher(String serviceUrl,String token);

}
