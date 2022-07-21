package com.lira17.expensetracker.mapper;

import java.util.List;

public interface ModelDtoMapper<M, G, C> {

    G mapToDto(M model);

    List<G> mapToDtoList(List<M> modelList);

    M mapToModel(C createDto);
}
