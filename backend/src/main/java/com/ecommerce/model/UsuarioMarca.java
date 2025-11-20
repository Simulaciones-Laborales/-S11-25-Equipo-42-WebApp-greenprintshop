package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_marca")
@IdClass(UsuarioMarcaId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioMarca {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @EqualsAndHashCode.Include
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_marca")
    @EqualsAndHashCode.Include
    private Marca marca;

    private String rolMarca;
}

