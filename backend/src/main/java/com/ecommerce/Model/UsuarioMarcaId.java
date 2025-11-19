package com.ecommerce.model;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioMarcaId implements Serializable {

    private Long usuario;
    private Long marca;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioMarcaId)) return false;
        UsuarioMarcaId that = (UsuarioMarcaId) o;
        return Objects.equals(usuario, that.usuario) &&
               Objects.equals(marca, that.marca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, marca);
    }
}

