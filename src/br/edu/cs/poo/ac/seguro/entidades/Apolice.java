package br.edu.cs.poo.ac.seguro.entidades;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Apolice implements Serializable,Registro {

	private String numero;
    private Veiculo veiculo;
    private BigDecimal valorFranquia;
    private BigDecimal valorPremio;
    private BigDecimal valorMaximoSegurado;
    private LocalDate dataInicioVigencia;
    
    @Override
    public String getIdUnico() {
        return this.getNumero();
    }
}