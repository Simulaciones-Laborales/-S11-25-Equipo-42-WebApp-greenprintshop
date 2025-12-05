package com.ecommerce.dto.perfil;

import com.ecommerce.dto.metrica.MetricaAmbientalResponse;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PerfilAmbientalResponse {
    private Long idPerfil;
    private BigDecimal huellaBaseCalculada;
    private LocalDateTime fechaUltimaCalculo;
    private Boolean validacionAdmin;
    private List<MetricaAmbientalResponse> metricas;
}

