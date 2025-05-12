package com.animephix.backend.model.compositePK;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodioId implements Serializable {
    private Long anime;
    private Long numEpisodio;
}
