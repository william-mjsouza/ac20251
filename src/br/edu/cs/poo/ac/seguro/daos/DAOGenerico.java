package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Registro; 
import java.io.Serializable; 

public abstract class DAOGenerico<T extends Registro> {

    private CadastroObjetos cadastro;

    public DAOGenerico() {
        this.cadastro = new CadastroObjetos(getClasseEntidade());
    }

    public abstract Class<?> getClasseEntidade();

    public boolean incluir(T registro) {
        if (registro == null || registro.getIdUnico() == null) {
            return false;
        }
        if (buscar(registro.getIdUnico()) != null) {
            return false; 
        }
        cadastro.incluir(registro, registro.getIdUnico());
        return true;
    }

    @SuppressWarnings("unchecked") 
    public T buscar(String id) {
        if (id == null) {
            return null;
        }
        return (T) cadastro.buscar(id);
    }

    public boolean alterar(T registro) {
        if (registro == null || registro.getIdUnico() == null) {
            return false;
        }
        if (buscar(registro.getIdUnico()) == null) {
            return false; 
        }
        cadastro.alterar(registro, registro.getIdUnico());
        return true;
    }

    public boolean excluir(String id) {
        if (id == null || buscar(id) == null) {
            return false;
        }
        cadastro.excluir(id);
        return true;
    }

    public Registro[] buscarTodos() {
        Serializable[] objsSer = cadastro.buscarTodos();
        Registro[] registros = new Registro[objsSer.length];
        for (int i = 0; i < objsSer.length; i++) {
            registros[i] = (Registro) objsSer[i];
        }
        return registros;
    }
}