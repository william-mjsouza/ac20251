package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Segurado {
	private String nome;
	private Endereco endereco;
	private LocalDate dataCriacao;
	private BigDecimal bonus;
	
	public Segurado(String nome, Endereco endereco, LocalDate dataCriacao, 
					BigDecimal bonus) {
		this.nome = nome;
		this.endereco = endereco;
		this.dataCriacao = dataCriacao;
		this.bonus = bonus;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	protected LocalDate getDataCriacao() {
		return dataCriacao;
	}
	
	protected void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public BigDecimal getBonus() {
		return bonus;
	}
	
	
	public int getIdade() {
		return (int) ChronoUnit.YEARS.between(dataCriacao, LocalDate.now());
	}
	
	public void creditarBonus(BigDecimal valor) {
		bonus = bonus.add(valor);
	}
	
	public void debitarBonus(BigDecimal valor) {
		bonus = bonus.subtract(valor);
	}
}
