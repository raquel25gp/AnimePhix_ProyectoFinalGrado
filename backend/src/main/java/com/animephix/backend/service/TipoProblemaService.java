package com.animephix.backend.service;

import com.animephix.backend.dto.TipoProblemaDTO;
import com.animephix.backend.model.TipoProblema;
import com.animephix.backend.repository.TipoProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TipoProblemaService {
    private TipoProblemaRepository tipoProblemaRepository;

    @Autowired
    public TipoProblemaService (TipoProblemaRepository tipoProblemaRepository) {
        this.tipoProblemaRepository = tipoProblemaRepository;
    }

    @Transactional(readOnly = true)
    public List<TipoProblemaDTO> listarNombresTipos() {
        List<TipoProblema> tipos = tipoProblemaRepository.findAll();
        List<TipoProblemaDTO> listadoNombres = new ArrayList<>();
        for (TipoProblema tipo: tipos) {
            listadoNombres.add(mapToNombreRequestDTO(tipo));
        }
        return listadoNombres;
    }

    // Método privado auxiliar
    private TipoProblemaDTO mapToNombreRequestDTO (TipoProblema tipo) {
        return TipoProblemaDTO.builder()
                .nombre(tipo.getNombre())
                .build();
    }
}
