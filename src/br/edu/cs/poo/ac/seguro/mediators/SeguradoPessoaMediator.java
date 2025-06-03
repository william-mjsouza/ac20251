package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaMediator {
    private SeguradoPessoaDAO dao = new SeguradoPessoaDAO();
    private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
    private static final SeguradoPessoaMediator instancia = new SeguradoPessoaMediator();

    private SeguradoPessoaMediator() {}

    public static SeguradoPessoaMediator getInstancia() {
        return instancia;
    }

    public String validarCpf(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) {
            return "CPF deve ser informado";  
        }
        
        if (cpf.length() != 11) {
            return "CPF deve ter 11 caracteres";
        }
        
        if (!StringUtils.temSomenteNumeros(cpf) || !ValidadorCpfCnpj.ehCpfValido(cpf)) {
            return "CPF com dígito inválido";
        }
        
        return null;
    }

    public String validarRenda(double renda) {
        if (renda < 0) {
            return "Renda deve ser maior ou igual à zero";
        }
        return null;
    }

    public String validarSeguradoPessoa(SeguradoPessoa seg) {
        if (seg == null) {
            return "Segurado inválido";
        }
        
        if (StringUtils.ehNuloOuBranco(seg.getNome())) {
            return "Nome deve ser informado";
        }
        
        
        if (seg.getEndereco() == null) {
            return "Endereço deve ser informado";
        }
        
        
        if (seg.getDataNascimento() == null) {
            return "Data do nascimento deve ser informada";
        }
        
      
        String msg = validarCpf(seg.getCpf());
        if (msg != null) return msg;
        
        
        msg = validarRenda(seg.getRenda());
        if (msg != null) return msg;
        
        return null;
    }

    public String incluirSeguradoPessoa(SeguradoPessoa seg) {
        String msg = validarSeguradoPessoa(seg);
        if (msg != null) return msg;
        if (dao.buscar(seg.getCpf()) != null) {
            return "CPF do segurado pessoa já existente";
        }
        dao.incluir(seg);
        return null;
    }

    public String alterarSeguradoPessoa(SeguradoPessoa seg) {
        String msg = validarSeguradoPessoa(seg);
        if (msg != null) return msg;
        if (dao.buscar(seg.getCpf()) == null) {
            return "CPF do segurado pessoa não existente";  
        }
        dao.alterar(seg);
        return null;
    }

    public String excluirSeguradoPessoa(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) {
            return "CPF deve ser informado";
        }
        
        if (dao.buscar(cpf) == null) {
            return "CPF do segurado pessoa não existente";
        }
        
        dao.excluir(cpf);
        return null;
    }

    public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) {
            return null;
        }
        return dao.buscar(cpf);
    }
}