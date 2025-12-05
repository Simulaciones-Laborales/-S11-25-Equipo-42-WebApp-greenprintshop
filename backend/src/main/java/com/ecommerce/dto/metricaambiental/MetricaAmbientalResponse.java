package com.ecommerce.dto.metrica;

import com.ecommerce.model.TipoMetrica;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MetricaAmbientalResponse {
    private Long id;
    private TipoMetrica tipo;
    private Long idRegla;
    private String valorIdentificador;  // desde ReglaEmision
    private BigDecimal cantidadUsada;
    private String unidad;              // opcional, para frontend
    private BigDecimal factorEmision;   // regla.valorEmision
    private BigDecimal emisionCalculada; // cantidadUsada * factorEmision
}

