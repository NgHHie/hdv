package com.example.security_service.dto;

import lombok.Data;
import java.util.List;
import com.example.security_service.models.ERole;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private ERole role;
    private List<String> permissions;

    public JwtResponse(String token, Long id, String username, String email, 
                      String firstName, String lastName, ERole role, List<String> permissions) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.permissions = permissions;
    }
}
