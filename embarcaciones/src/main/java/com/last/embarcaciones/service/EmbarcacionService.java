package com.last.embarcaciones.service;

import com.last.embarcaciones.dto.EmbarcacionDTO;

import java.util.List;

public interface EmbarcacionService {

    EmbarcacionDTO crearEmbarcacion(EmbarcacionDTO embarcacionDTO) ;

    EmbarcacionDTO obtenerEmbarcacionPorId(int idEmbarcacion) ;

    EmbarcacionDTO actualizarEmbarcacion(int idEmbarcacion, EmbarcacionDTO embarcacionDTO) ;

    List<EmbarcacionDTO> obtenerTodasLasEmbarcaciones() ;

    void eliminarEmbarcacion(int idEmbarcacion);



}
