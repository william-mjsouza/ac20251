package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor

public class Sinistro {
	private String numero;
	private Veiculo veiculo;
	private LocalDateTime dataHoraSinistro;
	private LocalDateTime dataHoraRegistro;
	private String usuarioRegistro;
	private BigDecimal valorSinistro;
	private TipoSinistro tipo;
	
	public Sinistro(Veiculo veiculo, LocalDateTime dataHoraSinistro, LocalDateTime dataHoraRegistro,
			String usuarioRegistro, BigDecimal valorSinistro, TipoSinistro tipo) {
		super();
		this.veiculo = veiculo;
		this.dataHoraSinistro = dataHoraSinistro;
		this.dataHoraRegistro = dataHoraRegistro;
		this.usuarioRegistro = usuarioRegistro;
		this.valorSinistro = valorSinistro;
		this.tipo = tipo;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	
}
