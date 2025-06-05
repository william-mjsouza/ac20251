package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaDAO extends SeguradoDAO<SeguradoPessoa> {

	@Override
    public Class<SeguradoPessoa> getClasseEntidade() {
        return SeguradoPessoa.class;
    }
	
	@Override
    public SeguradoPessoa buscar(String numero) {
        return (SeguradoPessoa) super.buscar(numero);
    }
}