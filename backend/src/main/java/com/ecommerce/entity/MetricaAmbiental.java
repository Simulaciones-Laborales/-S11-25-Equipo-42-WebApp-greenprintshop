package com.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "metrica_ambiental",
       indexes = {
           @Index(name = "idx_metrica_perfil", columnList = "id_perfil"),
           @Index(name = "idx_metrica_regla", columnList = "id_regla")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricaAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metrica")
    private Long id;

    // Perfil al que pertenece (perfil ambiental tiene muchas métricas)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil", nullable = false)
    private PerfilAmbiental perfilAmbiental;

    // Tipo categórico (enum)
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 32)
    @NotNull
    private TipoMetrica tipo;

    // Referencia a la regla que define el factor y validez del identificador
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_regla", nullable = false)
    @NotNull
    private ReglaEmision reglaEmision;

    // Cantidad usada (ej. 0.6 kg, 1200 km, 15 kWh)
    @Column(name = "cantidad_usada", nullable = false, precision = 19, scale = 6)
    @NotNull
    private BigDecimal cantidadUsada;

    // Unidad textual para reportes y frontend (ej. "kg", "kWh", "km")
    @Column(name = "unidad", length = 32)
    private String unidad;

    /**
     * Nota: el cálculo de emisiones se hace típicamente como:
     *   emision = cantidadUsada * reglaEmision.getValorEmision()
     */
}

