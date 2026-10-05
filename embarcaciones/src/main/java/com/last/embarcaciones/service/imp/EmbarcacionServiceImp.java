package com.last.embarcaciones.service.imp;

import com.last.embarcaciones.dto.EmbarcacionDTO;
import com.last.embarcaciones.entity.Embarcacion;
import com.last.embarcaciones.exceptions.EntityNotFoundException;
import com.last.embarcaciones.repository.EmbarcacionRepository;
import com.last.embarcaciones.service.EmbarcacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbarcacionServiceImp implements EmbarcacionService {

    @Autowired
    private EmbarcacionRepository embarcacionRepository;


    @Override
    public EmbarcacionDTO crearEmbarcacion(EmbarcacionDTO embarcacionDTO) {
        Embarcacion embarcacion = mapearEntidad(embarcacionDTO);
        Embarcacion nuevaEmbarcacion = embarcacionRepository.save(embarcacion);

        return mapearDTO(nuevaEmbarcacion);
    }

    @Override
    public EmbarcacionDTO obtenerEmbarcacionPorId(int idEmbarcacion) {
        Embarcacion embarcacion = embarcacionRepository.findById(idEmbarcacion)
                .orElseThrow(()-> new EntityNotFoundException("Usuario", "ID: " + idEmbarcacion));

        return mapearDTO(embarcacion);
    }

    @Override
    public EmbarcacionDTO actualizarEmbarcacion(int idEmbarcacion, EmbarcacionDTO embarcacionDTO) {
        Embarcacion embarcacionExistente = embarcacionRepository.findById(idEmbarcacion)
                .orElseThrow(() -> new EntityNotFoundException("Embarcación", "ID: " + idEmbarcacion));

        embarcacionExistente.setNombre(embarcacionDTO.getNombre());
        embarcacionExistente.setCodigo(embarcacionDTO.getCodigo());
        embarcacionExistente.setTipo(embarcacionDTO.getTipo());
        embarcacionExistente.setEstado(embarcacionDTO.getEstado());
        embarcacionExistente.setMatricula(embarcacionDTO.getMatricula());
        embarcacionExistente.setUbicacion(embarcacionDTO.getUbicacion());
        embarcacionExistente.setDescripcion(embarcacionDTO.getDescripcion());
        embarcacionExistente.setObservaciones(embarcacionDTO.getObsevaciones());

        Embarcacion embarcacionActualizada = embarcacionRepository.save(embarcacionExistente);

        return mapearDTO(embarcacionActualizada);
    }

    @Override
    public List<EmbarcacionDTO> obtenerTodasLasEmbarcaciones() {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAll();
        return embarcaciones.stream().map(this::mapearDTO).toList();
    }

    @Override
    public void eliminarEmbarcacion(int idEmbarcacion) {
        Embarcacion embarcacion = embarcacionRepository.findById(idEmbarcacion)
                .orElseThrow(() -> new EntityNotFoundException("Embarcacion", "ID: " + idEmbarcacion));
        embarcacionRepository.delete(embarcacion);
    }

    private EmbarcacionDTO mapearDTO(Embarcacion embarcacion) {
        EmbarcacionDTO embarcacionDTO = new EmbarcacionDTO();
        embarcacionDTO.setIdEmbarcacion(embarcacion.getId_embarcacion());
        embarcacionDTO.setNombre(embarcacion.getNombre());
        embarcacionDTO.setTipo(embarcacion.getTipo());
        embarcacionDTO.setEstado(embarcacion.getEstado());
        embarcacionDTO.setMatricula(embarcacion.getMatricula());
        embarcacionDTO.setUbicacion(embarcacion.getUbicacion());
        embarcacionDTO.setDescripcion(embarcacion.getDescripcion());
        embarcacionDTO.setObsevaciones(embarcacion.getObservaciones());
        embarcacionDTO.setCodigo(embarcacion.getCodigo());

        return embarcacionDTO;

    }

    private Embarcacion mapearEntidad(EmbarcacionDTO embarcacionDTO) {
        Embarcacion embarcacion = new Embarcacion();
        embarcacion.setCodigo(embarcacionDTO.getCodigo());
        embarcacion.setNombre(embarcacionDTO.getNombre());
        embarcacion.setTipo(embarcacionDTO.getTipo());
        embarcacion.setEstado(embarcacionDTO.getEstado());
        embarcacion.setMatricula(embarcacionDTO.getMatricula());
        embarcacion.setUbicacion(embarcacionDTO.getUbicacion());
        embarcacion.setDescripcion(embarcacionDTO.getDescripcion());
        embarcacion.setObservaciones(embarcacionDTO.getObsevaciones());

        return embarcacion;
    }
}
