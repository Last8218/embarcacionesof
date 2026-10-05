package com.last.embarcaciones.repository;


import com.last.embarcaciones.entity.Embarcacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbarcacionRepository extends JpaRepository<Embarcacion, Integer> {


}
