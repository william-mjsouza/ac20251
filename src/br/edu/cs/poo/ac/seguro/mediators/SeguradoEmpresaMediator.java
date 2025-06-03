package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaMediator {
    private static final SeguradoEmpresaMediator instancia = new SeguradoEmpresaMediator();
    private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
    private SeguradoEmpresaDAO dao = new SeguradoEmpresaDAO();

    private SeguradoEmpresaMediator() {}

    public static SeguradoEmpresaMediator getInstancia() {
        return instancia;
    }

    public String validarCnpj(String cnpj) {
        if (StringUtils.ehNuloOuBranco(cnpj)) return "CNPJ deve ser informado";
        if (cnpj.length() != 14) return "CNPJ deve ter 14 caracteres";
        if (!StringUtils.temSomenteNumeros(cnpj) || !ValidadorCpfCnpj.ehCnpjValido(cnpj)) return "CNPJ com dígito inválido";
        return null;
    }

    public String validarFaturamento(double faturamento) {
        return faturamento <= 0 ? "Faturamento deve ser maior que zero" : null;
    }

    public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
        String msg = validarSeguradoEmpresa(seg);
        if (msg != null) return msg;
        if (dao.buscar(seg.getCnpj()) != null) return "CNPJ do segurado empresa já existente";
        dao.incluir(seg);
        return null;
    }

    public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
        String msg = validarSeguradoEmpresa(seg);
        if (msg != null) return msg;
        if (dao.buscar(seg.getCnpj()) == null) return "CNPJ do segurado empresa não existente";
        dao.alterar(seg);
        return null;
    }

    public String excluirSeguradoEmpresa(String cnpj) {
        if (dao.buscar(cnpj) == null) return "CNPJ do segurado empresa não existente";
        dao.excluir(cnpj);
        return null;
    }

    public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
        return dao.buscar(cnpj);
    }

    public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
        if (seg == null) {
            return "Segurado inválido";
        }
        if (StringUtils.ehNuloOuBranco(seg.getNome())) {
            return "Nome deve ser informado";
        }
        if (seg.getEndereco() == null) {
            return "Endereço deve ser informado";
        }
        if (seg.getDataAbertura() == null) {
            return "Data da abertura deve ser informada";
        }

        String msg = validarCnpj(seg.getCnpj());
        if (msg != null) {
            return msg;
        }
        return validarFaturamento(seg.getFaturamento());
    }
}