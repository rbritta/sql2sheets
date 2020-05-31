package br.eti.rbritta.sql2sheets.service;

import br.eti.rbritta.sql2sheets.model.BaseEntity;
import br.eti.rbritta.sql2sheets.repository.BaseEntityRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseEntityService <T extends BaseEntity, R extends BaseEntityRepository<T>> {

    public abstract R repo();

    public <N> List<N> getAllAs(Function<T, N> function) {
        return repo().findAll().stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public <N> N getOneAs(UUID id, Function<T, N> function) {
        return function.apply(repo().getOne(id));
    }

    public void delete(UUID id) {
        repo().deleteById(id);
    }
}
