package com.libros.ms_usuarios.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
