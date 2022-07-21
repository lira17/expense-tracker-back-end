package com.lira17.expensetracker.mapper;

public interface DtoMapper<M, D>{

    D mapToDto(M model);

    M mapToModel(D dto);
}
