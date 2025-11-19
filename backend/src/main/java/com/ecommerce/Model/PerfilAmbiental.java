package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "perfil_ambiental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PerfilAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idPerfil;

    private BigDecimal huellaBaseCalculada;

    private LocalDateTime fechaUltimaCalculo;

    private Boolean validacionAdmin;

    @OneToMany(mappedBy = "perfilAmbiental")
    @ToString.Exclude
    private List<MetricaAmbiental> metricas;
}

