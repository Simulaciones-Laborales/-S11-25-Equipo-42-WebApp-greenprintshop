package com.ecommerce.mapper;

import com.ecommerce.model.MetricaAmbiental;
import com.ecommerce.dto.metrica.MetricaAmbientalCreateRequest;
import com.ecommerce.dto.metrica.MetricaAmbientalResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface MetricaAmbientalMapper {

    // Request -> Entity
    @Mapping(source = "idRegla", target = "reglaEmision.idRegla")
    MetricaAmbiental toEntity(MetricaAmbientalCreateRequest request);

    // Entity -> Response
    @Mapping(source = "reglaEmision.idRegla", target = "idRegla")
    @Mapping(source = "reglaEmision.valorIdentificador", target = "valorIdentificador")
    @Mapping(source = "reglaEmision.valorEmision", target = "factorEmision")
    @Mapping(expression = "java(entity.getCantidadUsada().multiply(entity.getReglaEmision().getValorEmision()))", 
             target = "emisionCalculada")
    MetricaAmbientalResponse toResponse(MetricaAmbiental entity);
}

