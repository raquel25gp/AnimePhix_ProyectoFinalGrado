package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActualizarPasswordDTO {
    private String email;
    private String nuevaPassword;
}
