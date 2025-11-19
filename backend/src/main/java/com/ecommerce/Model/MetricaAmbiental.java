package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "metrica_ambiental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetricaAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idMetrica;

    private String tipo;

    private String valorIdentificador;

    private BigDecimal cantidadUsada;

    @ManyToOne
    @JoinColumn(name = "id_perfil")
    private PerfilAmbiental perfilAmbiental;

    @ManyToOne
    @JoinColumn(name = "id_regla")
    private ReglaEmision reglaEmision;
}

