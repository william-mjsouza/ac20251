package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

public class SinistroDAO extends DAOGenerico {

	public SinistroDAO() {
		cadastro = new CadastroObjetos(Sinistro.class);
	}

	public Sinistro buscar(String numero) {
		return (Sinistro) cadastro.buscar(numero);
	}

	public boolean incluir(Sinistro sinistro) {
		if (buscar(sinistro.getNumero()) != null) {
			return false;
		} else {
			cadastro.incluir(sinistro, sinistro.getNumero());
			return true;
		}
	}

	public boolean alterar(Sinistro sinistro) {
		if (buscar(sinistro.getNumero()) == null) {
			return false;
		} else {
			cadastro.alterar(sinistro, sinistro.getNumero());
			return true;
		}
	}

	public boolean excluir(String numero) {
		if (buscar(numero) == null) {
			return false;
		} else {
			cadastro.excluir(numero);
			return true;
		}
	}
}