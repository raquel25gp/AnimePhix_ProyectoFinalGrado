package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegistroDTO {
    private String nombre;
    private String email;
    private String password;
}
