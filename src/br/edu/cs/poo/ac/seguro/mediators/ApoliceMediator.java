package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class ApoliceMediator {
    private SeguradoPessoaDAO daoSegPes;
    private SeguradoEmpresaDAO daoSegEmp;
    private VeiculoDAO daoVel;
    private ApoliceDAO daoApo;
    private SinistroDAO daoSin;

    private static ApoliceMediator instancia;

    private ApoliceMediator() {
        this.daoSegPes = new SeguradoPessoaDAO();
        this.daoSegEmp = new SeguradoEmpresaDAO();
        this.daoVel = new VeiculoDAO();
        this.daoApo = new ApoliceDAO();
        this.daoSin = new SinistroDAO();
    }

    public static ApoliceMediator getInstancia() {
        if (instancia == null) {
            instancia = new ApoliceMediator();
        }
        return instancia;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        if (dados == null) {
            return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
        }

        String cpfOuCnpj = dados.getCpfOuCnpj();
        if (cpfOuCnpj == null || cpfOuCnpj.trim().isEmpty()) {
            return new RetornoInclusaoApolice(null, "CPF ou CNPJ deve ser informado");
        }

        boolean isCPF = cpfOuCnpj.length() == 11;
        boolean isCNPJ = cpfOuCnpj.length() == 14;

        if (isCPF && !ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) {
            return new RetornoInclusaoApolice(null, "CPF inválido");
        }
        if (isCNPJ && !ValidadorCpfCnpj.ehCnpjValido(cpfOuCnpj)) {
            return new RetornoInclusaoApolice(null, "CNPJ inválido");
        }

        if (dados.getPlaca() == null || dados.getPlaca().trim().isEmpty()) {
            return new RetornoInclusaoApolice(null, "Placa do veículo deve ser informada");
        }

        if (dados.getAno() < 2020 || dados.getAno() > 2025) {
            return new RetornoInclusaoApolice(null, "Ano tem que estar entre 2020 e 2025, incluindo estes");
        }

        if (dados.getValorMaximoSegurado() == null) {
            return new RetornoInclusaoApolice(null, "Valor máximo segurado deve ser informado");
        }

        CategoriaVeiculo categoria = CategoriaVeiculo.obter(dados.getCodigoCategoria());
        if (categoria == null) {
            return new RetornoInclusaoApolice(null, "Categoria inválida");
        }

        BigDecimal valorTabela = null;
        for (PrecoAno pa : categoria.getPrecosAnos()) {
            if (pa.getAno() == dados.getAno()) {
                valorTabela = BigDecimal.valueOf(pa.getPreco());
                break;
            }
        }

        if (valorTabela == null) {
            return new RetornoInclusaoApolice(null, "Ano não disponível para a categoria");
        }

        BigDecimal valorInformado = dados.getValorMaximoSegurado().setScale(2, RoundingMode.HALF_UP);
        BigDecimal limiteInferior = valorTabela.multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal limiteSuperior = valorTabela.setScale(2, RoundingMode.HALF_UP);

        if (valorInformado.compareTo(limiteInferior) < 0 || valorInformado.compareTo(limiteSuperior) > 0) {
            return new RetornoInclusaoApolice(null, "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria");
        }

        SeguradoPessoa pessoa = null;
        SeguradoEmpresa empresa = null;
        Segurado proprietario;

        if (isCPF) {
            pessoa = daoSegPes.buscar(cpfOuCnpj);
            if (pessoa == null) {
                return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
            }
            proprietario = pessoa;
        } else {
            empresa = daoSegEmp.buscar(cpfOuCnpj);
            if (empresa == null) {
                return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
            }
            proprietario = empresa;
        }

        Veiculo veiculo = daoVel.buscar(dados.getPlaca());
        LocalDate hoje = LocalDate.now();
        String numeroApolice = isCPF
                ? hoje.getYear() + "000" + cpfOuCnpj + dados.getPlaca()
                : hoje.getYear() + cpfOuCnpj + dados.getPlaca();

        Apolice apoliceExistente = daoApo.buscar(numeroApolice);
        if (apoliceExistente != null) {
            return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
        }

        Veiculo novoVeiculo = new Veiculo(dados.getPlaca(), dados.getAno(), proprietario ,categoria);
        if (veiculo == null) {
            daoVel.incluir(novoVeiculo);
        } else {
            veiculo.setProprietario(proprietario);
            daoVel.alterar(veiculo);
            novoVeiculo = veiculo;
        }

        BigDecimal vpa = dados.getValorMaximoSegurado().multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vpb = vpa;
        if (proprietario instanceof SeguradoEmpresa && ((SeguradoEmpresa) proprietario).isEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(new BigDecimal("1.2")).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal bonus = proprietario.getBonus();
        BigDecimal vpc = vpb.subtract(bonus.divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
        BigDecimal premio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO;
        BigDecimal franquia = vpb.multiply(new BigDecimal("1.3")).setScale(2, RoundingMode.HALF_UP);
        
        Apolice novaApolice = new Apolice(numeroApolice ,novoVeiculo, franquia, premio, dados.getValorMaximoSegurado(), hoje);
        daoApo.incluir(novaApolice);

        int anoAnterior = hoje.minusYears(1).getYear();
        boolean temSinistroAnoAnterior = false;

        for (Sinistro sin : daoSin.buscarTodosLista()) {
            Veiculo vSin = sin.getVeiculo();
            boolean mesmaPlaca = vSin != null &&
                    vSin.getPlaca().equalsIgnoreCase(novoVeiculo.getPlaca());
            boolean mesmoAno = sin.getDataHoraSinistro().getYear() == anoAnterior;

            if (mesmaPlaca && mesmoAno) {
                temSinistroAnoAnterior = true;
                break;
            }
        }

        if (!temSinistroAnoAnterior) {
            BigDecimal bonusAdicional = premio.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.HALF_UP);
           
            if (proprietario instanceof SeguradoPessoa) {
                SeguradoPessoa pessoaParaAtualizar = (SeguradoPessoa) proprietario;
                SeguradoPessoa pessoaAtualizada = new SeguradoPessoa(
                        pessoaParaAtualizar.getNome(),
                        pessoaParaAtualizar.getEndereco(),
                        pessoaParaAtualizar.getDataNascimento(), 
                        pessoaParaAtualizar.getBonus().add(bonusAdicional).setScale(2, RoundingMode.HALF_UP),
                        pessoaParaAtualizar.getCpf(),
                        pessoaParaAtualizar.getRenda()
                );
                daoSegPes.alterar(pessoaAtualizada);
            } else if (proprietario instanceof SeguradoEmpresa) { 
                SeguradoEmpresa empresaParaAtualizar = (SeguradoEmpresa) proprietario; 
                SeguradoEmpresa empresaAtualizada = new SeguradoEmpresa(
                        empresaParaAtualizar.getNome(),
                        empresaParaAtualizar.getEndereco(),
                        empresaParaAtualizar.getDataAbertura(),
                        empresaParaAtualizar.getBonus().add(bonusAdicional).setScale(2, RoundingMode.HALF_UP),
                        empresaParaAtualizar.getCnpj(),
                        empresaParaAtualizar.getFaturamento(),
                        empresaParaAtualizar.isEhLocadoraDeVeiculos()
                );
                daoSegEmp.alterar(empresaAtualizada);
            }
        }

        return new RetornoInclusaoApolice(numeroApolice, null);
    }

    public Apolice buscarApolice(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return null;
        }
        return daoApo.buscar(numero);
    }

    public String excluirApolice(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return "Número deve ser informado";
        }

        Apolice apolice = daoApo.buscar(numero);
        if (apolice == null) {
            return "Apólice inexistente";
        }

        List<Sinistro> sinistros = daoSin.buscarTodosLista();
        int anoApolice = apolice.getDataInicioVigencia().getYear();

        for (Sinistro sin : sinistros) {
            int anoSinistro = sin.getDataHoraSinistro().getYear();
            if (anoSinistro == anoApolice && 
                sin.getVeiculo() != null && 
                apolice.getVeiculo() != null &&
                sin.getVeiculo().getPlaca().equals(apolice.getVeiculo().getPlaca())) {
                return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
            }
        }

        daoApo.excluir(numero);
        return null;
    }
}