package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "marca")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idMarca;

    private String nombre;

    private Boolean certificadoVerde;

    @OneToMany(mappedBy = "marca")
    @ToString.Exclude
    private List<UsuarioMarca> usuarios;

    @OneToMany(mappedBy = "marca")
    @ToString.Exclude
    private List<Producto> productos;
}

