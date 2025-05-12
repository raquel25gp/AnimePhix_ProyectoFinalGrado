package com.animephix.backend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnimeFavoritoDTO {
    private String email;
    private String nombreAnime;
}
