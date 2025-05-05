package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
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

	private static ApoliceMediator instancia = new ApoliceMediator();

	private ApoliceMediator() {
		this.daoSegPes = new SeguradoPessoaDAO();
		this.daoSegEmp = new SeguradoEmpresaDAO();
		this.daoVel = new VeiculoDAO();
		this.daoApo = new ApoliceDAO();
		this.daoSin = new SinistroDAO();
	}

	public static ApoliceMediator getInstancia() {
		return instancia;
	}

	/*
	 * Algumas regras de validação e lógicas devem ser inferidas através da leitura
	 * e do entendimento dos testes automatizados. Seguem abaixo explicações
	 * pertinentes e necessárias ao entendimento completo de como este método deve
	 * funcionar.
	 * 
	 * Toda vez que um retorno contiver uma mensagem de erro não nula, significando
	 * que a apólice não foi incluída, o numero da apólice no retorno deve ser nulo.
	 * 
	 * Toda vez que um retorno contiver uma mensagem de erro não nula, significando
	 * que a apólice não foi incluída, o numero da apólice no retorno deve ser nulo.
	 * 
	 * Toda vez que um retorno contiver uma mensagem de erro nula, significando que
	 * a apólice foi incluída com sucesso, o numero da apólice no retorno deve ser o
	 * número da apólice incluída.
	 * 
	 * À clase Apolice, deve ser acrescentado (com seus respectivos get/set e
	 * presença dele no construtor) o atributo LocalDate dataInicioVigencia.
	 * 
	 * O número da apólice é igual a: Se cpfOuCnpj de dados for um CPF número da
	 * apólice = ano atual + "000" + cpfOuCnpj + placa Se cpfOuCnpj de dados for um
	 * CNPJ número da apólice = ano atual + cpfOuCnpj + placa
	 * 
	 * O valor do prêmio é calculado da seguinte forma (A) Calcula-se VPA = (3% do
	 * valor máximo segurado) (B) Calcula-se VPB = 1.2*VPA, se segurado for empresa
	 * e indicador de locadora for true; ou VPB = VPA, caso contrário. (C)
	 * Calcula-se VPC = VPB - bonus/10. (D) Calcula-se valor do prêmio = VPC, se VPC
	 * > 0; ou valor do prêmio = 0, se VPC <=0.
	 * 
	 * O valor da franquia é igual a 130% do VPB.
	 * 
	 * Após validar a nulidade de dados e da placa, fazer a busca do veículo por
	 * placa. Se o veículo não existir, realizar todas as valiações pertinentes Se o
	 * veículo existir, realizar as validações de cpf/cnpj e de valor máximo, e a
	 * verificação de apólice já existente (busca de apólice por número).
	 * 
	 * ASSOCIAÇÂO DE VEICULO COM SEGURADOS: buscar segurado empresa por CNPJ OU
	 * segura pessoa por CPF. Se não for encontrado, retornar mensagem de erro, Se
	 * for encontrado, associar ao veículo.
	 * 
	 * Se o veículo não existir (busca no dao de veículos por placa), ele deve ser
	 * incluído no dao de veículos com as informações recebidas em dados Se o
	 * veículo existir, (busca no dao de veículos por placa), ele deve ser alterado
	 * no dao de veículos, mudando-se apenas os segurado empresa e segurado pessoa,
	 * cujo cpf ou cnpj foi recebido em dados. Estes devem ser atualizados em
	 * veículo após validações de cpf/cnpj e busca deles a partir dos mediators de
	 * segurado empresa e de segurado pessoa.
	 * 
	 * Após todos os dados validados, e o veículo incluído ou alterado, deve-se
	 * instanciar um objeto do tipo Apolice, e incluí-lo no dao de apolice.
	 * 
	 * Após a inclusão bem sucedida da apólice, deve-se bonficar o segurado, se for
	 * o caso. O segurado, pessoa ou empresa, a depender do cpf ou do cnpj recebido
	 * em dados, vai ter direito a crédito no bônus se seu cpf ou cnpj não tiver
	 * tido sinistro cadastrado no ano anterior à data de início de vigência da
	 * apólice. Para inferir isto, deve-se usar um novo método, a ser criado no
	 * SinistroDAO, public Sinistro[] buscarTodos() e, com o resultado, verificar se
	 * existe pelos menos um sinistro cujo veículo está associado ao cpf ou ao cnpj
	 * do segurado da apólice, e o ano da data e hora do sinistro seja igual à data
	 * de início de vigência da apólice menos um. Se existir, não haverá bônus. Caso
	 * contrário, deve-se acrescer 30% do valor do prêmio da apólice ao bônus do
	 * segurado pessoa ou segurado empresa, e finalmente alterar o segurado pessoa
	 * ou segurado empresa no seu respectivo DAO.
	 * 
	 * OBS: TODOS os atributos do tipo BigDecimal devem ser gravados com 02 casas
	 * decimais (usar o método setScale). Se isto não ocorrer, alguns testes vão
	 * quebrar.
	 */
	public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
	    if (dados == null) {
	        return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
	    }
	    if (StringUtils.ehNuloOuBranco(dados.getPlaca())) {
	        return new RetornoInclusaoApolice(null, "Placa do veículo deve ser informada");
	    }

	    String placa = dados.getPlaca().trim();
	    String cpfOuCnpj = dados.getCpfOuCnpj();

	    if (StringUtils.ehNuloOuBranco(cpfOuCnpj)) {
	        return new RetornoInclusaoApolice(null, "CPF ou CNPJ deve ser informado");
	    }

	    boolean isCpf = cpfOuCnpj.length() == 11;
	    boolean isCnpj = cpfOuCnpj.length() == 14;

	    if (isCpf && !ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) {
	        return new RetornoInclusaoApolice(null, "CPF inválido");
	    }

	    if (isCnpj && !ValidadorCpfCnpj.ehCnpjValido(cpfOuCnpj)) {
	        return new RetornoInclusaoApolice(null, "CNPJ inválido");
	    }

	    if (dados.getAno() > 2025 || dados.getAno() < 2020) {
	        return new RetornoInclusaoApolice(null, "Ano tem que estar entre 2020 e 2025, incluindo estes");
	    }

	    BigDecimal valorMaximo = dados.getValorMaximoSegurado();
	    if (valorMaximo == null || valorMaximo.compareTo(BigDecimal.ZERO) <= 0) {
	        return new RetornoInclusaoApolice(null, "Valor máximo segurado deve ser informado");
	    }
	    
	    CategoriaVeiculo categoria = CategoriaVeiculo.buscarPorCodigo(dados.getCodigoCategoria());
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
	    
	    SeguradoPessoa segPes = null;
	    SeguradoEmpresa segEmp = null;

	    if (ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) {
	        segPes = daoSegPes.buscar(cpfOuCnpj);
	        if (segPes == null)
	            return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
	    } else {
	        segEmp = daoSegEmp.buscar(cpfOuCnpj);
	        if (segEmp == null)
	            return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
	    }
	    
	    Veiculo veiculo = daoVel.buscar(placa);
	    LocalDate agora = LocalDate.now();
        String numeroApolice = isCpf
                ? agora.getYear() + "000" + cpfOuCnpj + dados.getPlaca()
                : agora.getYear() + cpfOuCnpj + dados.getPlaca();

	    if (daoApo.buscar(numeroApolice) != null) {
	        return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
	    }
	    
	    if (veiculo == null) {
	        veiculo = new Veiculo(placa, dados.getAno(), segEmp, segPes, categoria);
	        daoVel.incluir(veiculo);
	    } else {
	        veiculo.setProprietarioPessoa(segPes);
	        veiculo.setProprietarioEmpresa(segEmp);
	        daoVel.alterar(veiculo);
	    }

	    BigDecimal vpa = dados.getValorMaximoSegurado().multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vpb = vpa;
        if (segEmp != null && segEmp.getEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(new BigDecimal("1.2")).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal bonus = segPes != null ? segPes.getBonus() : segEmp.getBonus();
        BigDecimal vpc = vpb.subtract(bonus.divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
        BigDecimal premio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO;
        BigDecimal franquia = vpb.multiply(new BigDecimal("1.3")).setScale(2, RoundingMode.HALF_UP);

	    Apolice apolice = new Apolice(numeroApolice, veiculo, franquia, premio, valorMaximo, LocalDate.now());
	    daoApo.incluir(apolice);

	    int anoAnterior = agora.minusYears(1).getYear();
	    boolean teveSinistro = false;

	    for (Sinistro sin : daoSin.buscarTodos()) {
	        Veiculo vSin = sin.getVeiculo();
	        boolean mesmaPlaca = vSin != null && 
	                vSin.getPlaca().equalsIgnoreCase(veiculo.getPlaca());
	        boolean mesmoAno = sin.getDataHoraSinistro().getYear() == anoAnterior;
	        
	        boolean mesmoProprietario =
	            (isCpf && vSin.getProprietarioPessoa() != null &&
	                cpfOuCnpj.equals(vSin.getProprietarioPessoa().getCpf())) ||
	            (isCnpj && vSin.getProprietarioEmpresa() != null &&
	                cpfOuCnpj.equals(vSin.getProprietarioEmpresa().getCnpj()));

	        if (mesmaPlaca && mesmoAno && mesmoProprietario) {
	            teveSinistro = true;
	            break;
	        }
	    }


	    if (!teveSinistro) {
	        BigDecimal acrescimo = premio.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.HALF_UP);
	        
	        if (segPes != null) {
	            segPes.creditarBonus(acrescimo);
	            daoSegPes.alterar(segPes);
	        } else {
	            segEmp.creditarBonus(acrescimo);
	            daoSegEmp.alterar(segEmp);
	        }
	    } else {
	        if (segPes != null) {
	            daoSegPes.alterar(segPes);
	        } else if (segEmp != null) {
	            daoSegEmp.alterar(segEmp);
	        }
	    }


	    return new RetornoInclusaoApolice(numeroApolice, null);
	}

	public Apolice buscarApolice(String numero) {
		return daoApo.buscar(numero);
	}

	/*
	 * A exclusão não é permitida quando: 1- O número for nulo ou branco. 2- Não
	 * existir apólice com o número recebido. 3- Existir sinistro cadastrado no
	 * mesmo ano da apólice (comparar ano da data e hora do sinistro com ano da data
	 * de início de vigência da apólice) para o mesmo veículo (comparar o veículo do
	 * sinistro com o veículo da apólice usando equals na classe veículo, que deve
	 * ser implementado). Para obter os sinistros cadastrados, usar o método
	 * buscarTodos do dao de sinistro, implementado para contempar a questão da
	 * bonificação no método de incluir apólice. É possível usar LOMBOK para
	 * implementação implicita do equals na classe Veiculo.
	 */
	public String excluirApolice(String numero) {
		if (StringUtils.ehNuloOuBranco(numero)) {
			return "Número deve ser informado";
		}
		
		else if (daoApo.buscar(numero) == null) {
			return "Apólice inexistente";
		}
		
		else {
			List<Sinistro> sinistros = daoSin.buscarTodos();
	        int anoApolice = daoApo.buscar(numero).getDataInicioVigencia().getYear();
	        
	        Veiculo veicApolice = daoApo.buscar(numero).getVeiculo();

	        for (Sinistro sin : sinistros) {
	            int anoSinistro = sin.getDataHoraSinistro().getYear();
	            Veiculo veicSinistro = sin.getVeiculo();

	            if (anoSinistro == anoApolice &&
	                veicSinistro != null &&
	                veicApolice != null &&
	                veicSinistro.getPlaca().equalsIgnoreCase(veicApolice.getPlaca())) {
	                return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
	            }
	        }

	        daoApo.excluir(numero);
	        return null;
		}
		
	}

	private String validarTodosDadosVeiculo(DadosVeiculo dados) {
		return null;
	}

	private String validarCpfCnpjValorMaximo(DadosVeiculo dados) {
		return null;
	}

	private BigDecimal obterValorMaximoPermitido(int ano, int codigoCat) {
		return null;
	}
}