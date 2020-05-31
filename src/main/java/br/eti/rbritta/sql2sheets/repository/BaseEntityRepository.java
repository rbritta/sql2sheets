package br.eti.rbritta.sql2sheets.repository;

import br.eti.rbritta.sql2sheets.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BaseEntityRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
}