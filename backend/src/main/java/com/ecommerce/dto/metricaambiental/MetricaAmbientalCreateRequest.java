package com.ecommerce.dto.metrica;

import com.ecommerce.model.TipoMetrica;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MetricaAmbientalCreateRequest {
    private TipoMetrica tipo;      // Tipo de la métrica
    private Long idRegla;           // FK a ReglaEmision
    private BigDecimal cantidadUsada;
    private String unidad;          // Opcional, para mostrar en frontend
}

