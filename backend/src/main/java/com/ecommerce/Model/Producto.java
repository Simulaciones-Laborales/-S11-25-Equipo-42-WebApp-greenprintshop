package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idProducto;

    private String nombre;

    private BigDecimal precio;

    private Integer stock;

    private BigDecimal pesoBruto;

    private String badgeSostenibilidad;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @OneToOne
    @JoinColumn(name = "id_perfil_ambiental", unique = true)
    private PerfilAmbiental perfilAmbiental;
}

