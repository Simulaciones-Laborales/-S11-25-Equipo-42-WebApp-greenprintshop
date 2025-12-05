package com.ecommerce.dto.regla;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ReglaEmisionResponse {
    private Long idRegla;
    private String valorIdentificador;
    private String tipoFactor;
    private BigDecimal valorEmision;
    private String unidadEmision;
    private String fuenteDatos;
}

