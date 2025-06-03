package br.edu.cs.poo.ac.seguro.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Veiculo implements Serializable, Registro{

    private String placa;
    private int ano;
    private Segurado proprietario;
    private CategoriaVeiculo categoria;
    
    @Override
    public String getIdUnico() {
        return this.getPlaca();
    }
}