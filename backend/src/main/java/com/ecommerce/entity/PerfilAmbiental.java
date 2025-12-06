package com.ecommerce.entity;

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
@Builder
public class PerfilAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerfil;

    @Column(precision = 19, scale = 6)
    private BigDecimal huellaBaseCalculada;

    private LocalDateTime fechaUltimaCalculo;

    private Boolean validacionAdmin;

    @OneToMany(mappedBy = "perfilAmbiental", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MetricaAmbiental> metricas;
}

