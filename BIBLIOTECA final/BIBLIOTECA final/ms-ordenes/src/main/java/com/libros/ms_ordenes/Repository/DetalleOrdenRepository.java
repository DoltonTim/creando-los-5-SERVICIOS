package com.libros.ms_ordenes.Repository;

import com.libros.ms_ordenes.Entity.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
}
