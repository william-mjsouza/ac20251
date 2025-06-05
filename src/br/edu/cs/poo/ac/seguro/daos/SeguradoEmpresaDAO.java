package br.edu.cs.poo.ac.seguro.daos;


import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO extends DAOGenerico<SeguradoEmpresa> {

	@Override
    public Class<SeguradoEmpresa> getClasseEntidade() {
        return SeguradoEmpresa.class;
    }

	@Override
    public SeguradoEmpresa buscar(String id) {
        return (SeguradoEmpresa) super.buscar(id);
    }

}