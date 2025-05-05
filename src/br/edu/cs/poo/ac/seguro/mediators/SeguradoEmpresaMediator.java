package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaMediator {
	private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
	private SeguradoEmpresaDAO seguradoEmpresaDAO = new SeguradoEmpresaDAO();
	
	private static SeguradoEmpresaMediator instancia = new SeguradoEmpresaMediator();
	
	private SeguradoEmpresaMediator() {}
	
	public static SeguradoEmpresaMediator getInstancia() {
        return instancia;
    }
	
	public String validarCnpj(String cnpj) {
		if (StringUtils.ehNuloOuBranco(cnpj)) {
            return "CNPJ deve ser informado";
        }
		
        String digitos = cnpj.replaceAll("\\D", "");
        if (digitos.length() != 14) {
            return "CNPJ deve ter 14 caracteres";
        }
        
        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            return "CNPJ com dígito inválido";
        }
        
        return null;
	}
	public String validarFaturamento(double faturamento) {
		if (faturamento <= 0.0) {
            return "Faturamento deve ser maior que zero";
        }
		
        return null;
	}
	public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
		String msg = validarSeguradoEmpresa(seg);
        if (msg != null) return msg;
        
        if (seguradoEmpresaDAO.buscar(seg.getCnpj()) != null) {
            return "CNPJ do segurado empresa já existente";
        }
        
        seguradoEmpresaDAO.incluir(seg);
        
        return null;
	}
	public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
		String msg = validarSeguradoEmpresa(seg);
		
        if (msg != null) return msg;
        
        if (seguradoEmpresaDAO.buscar(seg.getCnpj()) == null) {
            return "CNPJ do segurado empresa não existente";
        }
        
        seguradoEmpresaDAO.alterar(seg);
        
        return null;
	}
	public String excluirSeguradoEmpresa(String cnpj) {
		if (seguradoEmpresaDAO.buscar(cnpj) == null) {
            return "CNPJ do segurado empresa não existente";
        }
		
		seguradoEmpresaDAO.excluir(cnpj);
		
        return null;
	}
	public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
		return seguradoEmpresaDAO.buscar(cnpj);
	}
	public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
        String msg = seguradoMediator.validarNome(seg.getNome());
        if (msg != null) return msg;
        
        msg = seguradoMediator.validarEndereco(seg.getEndereco());
        if (msg != null) return msg;
 
        if (seg.getDataAbertura() == null) {
            return "Data da abertura deve ser informada";
        }
        
        msg = seguradoMediator.validarDataCriacao(seg.getDataAbertura());
        if (msg != null) {
            return msg.replace("criação", "abertura");
        }

        msg = validarCnpj(seg.getCnpj());
        if (msg != null) return msg;
        
        return validarFaturamento(seg.getFaturamento());
	}
}
