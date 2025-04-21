package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class Sinistro {
	private String numero;
	private Veiculo veiculo;
	private LocalDateTime dataHoraSinistro;
	private LocalDateTime dataHoraRegistro;
	private String usuarioRegistro;
	private BigDecimal valorSinistro;
	private TipoSinistro tipo;
}
