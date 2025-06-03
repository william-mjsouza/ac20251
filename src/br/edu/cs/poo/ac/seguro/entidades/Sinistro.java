package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Sinistro implements Serializable, Registro {
	
	private static final long serialVersionUID = 1L;
	private String numero;
	private Veiculo veiculo;
	private LocalDateTime dataHoraSinistro;
	private LocalDateTime dataHoraRegistro;
	private String usuarioRegistro;
	private BigDecimal valorSinistro;
	private TipoSinistro tipo;
	private int sequencial;
	private String numeroApolice;
	
	public Sinistro(String numero, Veiculo veiculo, LocalDateTime dataHoraSinistro, LocalDateTime dataHoraRegistro,
			String usuarioRegistro, BigDecimal valorSinistro, TipoSinistro tipo) {
		super();
		this.numero = numero;
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
	
	public int getSequencial() {
		return sequencial;
	}
	public void setSequencial(int sequencial) {
		this.sequencial = sequencial;
	}
	
	public String getNumeroApolice() {
		return numeroApolice;
	}
	public void setNumeroApolice(String numeroApolice) {
		this.numeroApolice = numeroApolice;
	}
	
	@Override
    public String getIdUnico() {
        return this.getNumero(); 
    }
	
	
}