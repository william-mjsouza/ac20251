package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaMediator {
	private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
	private SeguradoPessoaDAO seguradoPessoaDAO = new SeguradoPessoaDAO();
	
	private static SeguradoPessoaMediator instancia = new SeguradoPessoaMediator();
	
	private SeguradoPessoaMediator() {}
	
	public static SeguradoPessoaMediator getInstancia() {
        return instancia;
    }
	
	public String validarCpf(String cpf) {
		return null;
	}
	public String validarRenda(double renda) {
		return null;
	}
	public String incluirSeguradoPessoa(SeguradoPessoa seg) {
		// Acho que tem que implementar essa lógica com base no teste
		validarSeguradoPessoa(seg);
		return null;
	}
	public String alterarSeguradoPessoa(SeguradoPessoa seg) {
		// Acho que tem que implementar essa lógica com base no teste
		validarSeguradoPessoa(seg);
		return null;
	}
	public String excluirSeguradoPessoa(String cpf) {
		return null;
	}
	public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
		return null;
	}
	public String validarSeguradoPessoa(SeguradoPessoa seg) {
		return null;
	}
}
