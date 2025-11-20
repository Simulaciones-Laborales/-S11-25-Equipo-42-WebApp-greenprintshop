package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idUsuario;

    private String nombre;

    private String email;

    private String rol;

    @OneToMany(mappedBy = "usuario")
    @ToString.Exclude
    private List<UsuarioMarca> marcasGestionadas;

    @OneToMany(mappedBy = "cliente")
    @ToString.Exclude
    private List<Pedido> pedidos;
}

