package com.example.coursecreation.service;


import com.example.coursecreation.dto.UserDetailsDto;

public interface AuthService {

    public UserDetailsDto getUserDetailsFromAuthService(String serviceUrl, String token);

    public Boolean isAdmin(String serviceUrl,String token);
    public Boolean isTeacher(String serviceUrl,String token);

}
