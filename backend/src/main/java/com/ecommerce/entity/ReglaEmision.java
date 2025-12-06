package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "regla_emision", indexes = {
        @Index(name = "idx_valor_identificador", columnList = "valor_identificador")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReglaEmision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegla;

    @Column(unique = true, nullable = false, length = 128)
    private String valorIdentificador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TipoMetrica tipoFactor;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal valorEmision; // kg CO2e / unidad

    @Column(length = 32)
    private String unidadEmision;    // kg, kWh, km, etc.

    @Column(length = 128)
    private String fuenteDatos;      // fuente del factor de emisión
}

