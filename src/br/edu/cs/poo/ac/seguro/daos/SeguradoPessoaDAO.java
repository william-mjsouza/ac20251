package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaDAO extends DAOGenerico<SeguradoPessoa> {

	@Override
    public Class<SeguradoPessoa> getClasseEntidade() {
        return SeguradoPessoa.class;
    }
	
	@Override
    public SeguradoPessoa buscar(String id) {
        return (SeguradoPessoa) super.buscar(id);
    }
}