package com.extinguidor.repository;

import com.extinguidor.model.entity.Facturacion;
import com.extinguidor.model.entity.Route;
import com.extinguidor.model.entity.Parte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturacionRepository extends JpaRepository<Facturacion, Long> {
    
    List<Facturacion> findByRuta(Route ruta);
    
    List<Facturacion> findByParte(Parte parte);
}

