package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

//import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor

public class Apolice {
	private String numero;
	private Veiculo veiculo;
	private BigDecimal valorFranquia;
	private BigDecimal valorPremio;
	private BigDecimal valorMaximoSegurado;
	
	 private LocalDate dataInicioVigencia;
	
	public Apolice(String numero, Veiculo veiculo, BigDecimal valorFranquia, 
				   BigDecimal valorPremio, BigDecimal valorMaximoSegurado, 
				   LocalDate dataInicioVigencia) {
		super();
		this.numero = numero;
		
		this.veiculo = veiculo;
		this.valorFranquia = valorFranquia;
		this.valorPremio = valorPremio;
		this.valorMaximoSegurado = valorMaximoSegurado;
		
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
}
