package br.edu.cs.poo.ac.seguro.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class TelaCrudSeguradoPessoa {

    protected Shell shell;
    private Text txtCpf;
    private Text txtNome;
    private Text txtDataNascimento;
    private Text txtRenda;
    private Text txtBonus;

    private Text txtLogradouro;
    private Text txtNumeroEndereco;
    private Text txtComplemento;
    private Text txtCep;
    private Text txtCidade;
    private Text txtEstado;
    private Text txtPais;

    private Button btnNovo;
    private Button btnBuscar;
    private Button btnIncluirAlterar;
    private Button btnExcluir;
    private Button btnCancelar;
    private Button btnLimpar;

    private SeguradoPessoaMediator seguradoPessoaMediator = SeguradoPessoaMediator.getInstancia();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Recursos de cor e fonte
    private Color colorThemeBackground;
    private Color colorTextDark;
    private Color colorButtonText;
    private Font customFont;
    private Font labelFontBold;

    public static void main(String[] args) {
        try {
            TelaCrudSeguradoPessoa window = new TelaCrudSeguradoPessoa();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        Display display = Display.getDefault();
        createResources(display);
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
    
    protected void createResources(Display display) {

        colorThemeBackground = new Color(display, 220, 220, 220);

        colorTextDark = new Color(display, 0, 0, 0);
        colorButtonText = new Color(display, 255, 255, 255);

        FontData[] systemFontData = display.getSystemFont().getFontData();
        systemFontData[0].setHeight(10);
        customFont = new Font(display, systemFontData);

        FontData[] labelBoldFontData = display.getSystemFont().getFontData();
        labelBoldFontData[0].setHeight(10);
        labelBoldFontData[0].setStyle(SWT.BOLD);
        labelFontBold = new Font(display, labelBoldFontData);
    }

    protected void disposeResources() {
        colorThemeBackground.dispose();
        colorTextDark.dispose();
        colorButtonText.dispose();
        customFont.dispose();
        labelFontBold.dispose();
    }

    protected void createContents() {
        shell = new Shell();
        shell.setSize(620, 720);
        shell.setText("CRUD de Segurado Pessoa");
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(colorThemeBackground);
        shell.setFont(customFont);

        shell.addDisposeListener(e -> disposeResources());

        Composite topComposite = new Composite(shell, SWT.NONE);
        topComposite.setLayout(new GridLayout(4, false));
        topComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        topComposite.setBackground(colorThemeBackground);

        Label lblCpf = new Label(topComposite, SWT.NONE);
        lblCpf.setText("CPF (somente números):");
        lblCpf.setFont(labelFontBold);
        lblCpf.setForeground(colorTextDark);
        lblCpf.setBackground(colorThemeBackground);

        txtCpf = new Text(topComposite, SWT.BORDER);
        txtCpf.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtCpf.setFont(customFont);
        txtCpf.addVerifyListener(e -> {
            if (!e.text.matches("[0-9]*")) {
                e.doit = false;
            }
        });

        btnNovo = createStyledButton(topComposite, "Novo");
        btnBuscar = createStyledButton(topComposite, "Buscar");

        Group grpDadosPessoais = new Group(shell, SWT.NONE);
        grpDadosPessoais.setText("Dados Pessoais");
        grpDadosPessoais.setLayout(new GridLayout(2, false));
        grpDadosPessoais.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        grpDadosPessoais.setFont(labelFontBold);
        grpDadosPessoais.setForeground(colorTextDark);
        grpDadosPessoais.setBackground(colorThemeBackground);

        createStyledLabel(grpDadosPessoais, "Nome:");
        txtNome = createStyledText(grpDadosPessoais);

        createStyledLabel(grpDadosPessoais, "Data Nascimento (AAAA-MM-DD):");
        txtDataNascimento = createStyledText(grpDadosPessoais);
        txtDataNascimento.addVerifyListener(e -> {
            if (!e.text.matches("[0-9\\-]*")) {
                e.doit = false;
            }
        });

        VerifyListener integerListener = e -> {
            if (!e.text.matches("[0-9]*")) {
                e.doit = false;
            }
        };

        createStyledLabel(grpDadosPessoais, "Renda (somente números):");
        txtRenda = createStyledText(grpDadosPessoais);
        txtRenda.addVerifyListener(integerListener);

        createStyledLabel(grpDadosPessoais, "Bônus (somente números):");
        txtBonus = createStyledText(grpDadosPessoais, SWT.BORDER);
        txtBonus.addVerifyListener(integerListener);

        Group grpEndereco = new Group(shell, SWT.NONE);
        grpEndereco.setText("Endereço");
        grpEndereco.setLayout(new GridLayout(2, false));
        grpEndereco.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        grpEndereco.setFont(labelFontBold);
        grpEndereco.setForeground(colorTextDark);
        grpEndereco.setBackground(colorThemeBackground);
        
        createStyledLabel(grpEndereco, "Logradouro:");
        txtLogradouro = createStyledText(grpEndereco);
        createStyledLabel(grpEndereco, "Número:");
        txtNumeroEndereco = createStyledText(grpEndereco);
        createStyledLabel(grpEndereco, "Complemento:");
        txtComplemento = createStyledText(grpEndereco);
        createStyledLabel(grpEndereco, "CEP (somente números):");
        txtCep = createStyledText(grpEndereco);
        txtCep.addVerifyListener(e -> {
            if (!e.text.matches("[0-9]*")) {
                e.doit = false;
            }
        });
        createStyledLabel(grpEndereco, "Cidade:");
        txtCidade = createStyledText(grpEndereco);
        createStyledLabel(grpEndereco, "Estado (UF):");
        txtEstado = createStyledText(grpEndereco);
        txtEstado.setTextLimit(2);
        createStyledLabel(grpEndereco, "País:");
        txtPais = createStyledText(grpEndereco);

        Composite bottomButtonsComposite = new Composite(shell, SWT.NONE);
        bottomButtonsComposite.setLayout(new GridLayout(4, true));
        bottomButtonsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        bottomButtonsComposite.setBackground(colorThemeBackground);

        btnIncluirAlterar = createStyledButton(bottomButtonsComposite, "Incluir");
        btnCancelar = createStyledButton(bottomButtonsComposite, "Cancelar");
        btnExcluir = createStyledButton(bottomButtonsComposite, "Excluir");
        btnLimpar = createStyledButton(bottomButtonsComposite, "Limpar");

        addEventListeners();
        configurarEstadoInicialCampos();
        configureTabOrder(topComposite, grpDadosPessoais, grpEndereco, bottomButtonsComposite);
    }

    private Label createStyledLabel(Composite parent, String text) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        label.setFont(labelFontBold);
        label.setForeground(colorTextDark);
        label.setBackground(colorThemeBackground);
        return label;
    }

    private Text createStyledText(Composite parent) {
        return createStyledText(parent, SWT.BORDER);
    }

    private Text createStyledText(Composite parent, int style) {
        Text text = new Text(parent, style);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        text.setFont(customFont);
        return text;
    }
    
    private Button createStyledButton(Composite parent, String text) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(text);
        button.setFont(customFont);
        button.setBackground(colorTextDark);
        button.setForeground(colorButtonText);
        button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        return button;
    }

    private void addEventListeners() {
        btnNovo.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { processarCliqueNovo(); }
        });
        btnBuscar.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { buscar(); }
        });
        btnIncluirAlterar.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { processarCliqueIncluirAlterar(); }
        });
        btnExcluir.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { excluir(); }
        });
        btnCancelar.addSelectionListener(new SelectionAdapter() { 
            @Override public void widgetSelected(SelectionEvent e) { cancelarOperacao(); }
        });
        btnLimpar.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { limparCamposAcaoUsuario(); }
        });
    }
    
    private void configureTabOrder(Composite topComposite, Group grpDados, Group grpEndereco, Composite bottomButtons) {
        shell.setTabList(new Control[] { topComposite, grpDados, grpEndereco, bottomButtons });
        topComposite.setTabList(new Control[] { txtCpf, btnNovo, btnBuscar });
        grpDados.setTabList(new Control[] { txtNome, txtDataNascimento, txtRenda, txtBonus });
        grpEndereco.setTabList(new Control[] { txtLogradouro, txtNumeroEndereco, txtComplemento, txtCep, txtCidade, txtEstado, txtPais });
        
        bottomButtons.setTabList(new Control[] { btnIncluirAlterar, btnCancelar, btnExcluir, btnLimpar });

    }

    private void configurarEstadoCampos(boolean cpfEnabled, boolean dadosPessoaisEnabled, boolean enderecoEnabled, 
                                          boolean incluirAlterarEnabled, String incluirAlterarTexto, 
                                          boolean excluirEnabled, boolean cancelarEnabled, boolean limparEnabled,
                                          boolean novoEnabled, boolean buscarEnabled) {
        txtCpf.setEnabled(cpfEnabled);
        
        txtNome.setEnabled(dadosPessoaisEnabled);
        txtDataNascimento.setEnabled(dadosPessoaisEnabled);
        txtRenda.setEnabled(dadosPessoaisEnabled);
        txtBonus.setEnabled(dadosPessoaisEnabled);

        txtLogradouro.setEnabled(enderecoEnabled);
        txtNumeroEndereco.setEnabled(enderecoEnabled);
        txtComplemento.setEnabled(enderecoEnabled);
        txtCep.setEnabled(enderecoEnabled);
        txtCidade.setEnabled(enderecoEnabled);
        txtEstado.setEnabled(enderecoEnabled);
        txtPais.setEnabled(enderecoEnabled);

        btnNovo.setEnabled(novoEnabled);
        btnBuscar.setEnabled(buscarEnabled);
        btnIncluirAlterar.setEnabled(incluirAlterarEnabled);
        btnIncluirAlterar.setText(incluirAlterarTexto);
        btnExcluir.setEnabled(excluirEnabled);
        btnCancelar.setEnabled(cancelarEnabled);
        btnLimpar.setEnabled(limparEnabled);
    }

    private void configurarEstadoInicialCampos() {
        configurarEstadoCampos(true, false, false, false, "Incluir", false, false, true, true, true);
        limparTodosOsCamposDeTexto();
        txtCpf.setFocus();
    }
    
    private void limparCamposAcaoUsuario() {
        if (txtCpf.isEnabled()) {
            txtCpf.setText("");
        }
        limparCamposDeDados();
        if(txtNome.isEnabled()) txtNome.setFocus();
        else if(txtCpf.isEnabled()) txtCpf.setFocus();
    }

    private void limparTodosOsCamposDeTexto() {
        if(txtCpf != null && !txtCpf.isDisposed()) txtCpf.setText("");
        limparCamposDeDados();
    }

    private void limparCamposDeDados() {
        if(txtNome != null && !txtNome.isDisposed()) txtNome.setText("");
        if(txtDataNascimento != null && !txtDataNascimento.isDisposed()) txtDataNascimento.setText("");
        if(txtRenda != null && !txtRenda.isDisposed()) txtRenda.setText("");
        if(txtBonus != null && !txtBonus.isDisposed()) txtBonus.setText("");
        if(txtLogradouro != null && !txtLogradouro.isDisposed()) txtLogradouro.setText("");
        if(txtNumeroEndereco != null && !txtNumeroEndereco.isDisposed()) txtNumeroEndereco.setText("");
        if(txtComplemento != null && !txtComplemento.isDisposed()) txtComplemento.setText("");
        if(txtCep != null && !txtCep.isDisposed()) txtCep.setText("");
        if(txtCidade != null && !txtCidade.isDisposed()) txtCidade.setText("");
        if(txtEstado != null && !txtEstado.isDisposed()) txtEstado.setText("");
        if(txtPais != null && !txtPais.isDisposed()) txtPais.setText("");
    }

    private void processarCliqueNovo() {
        String cpf = txtCpf.getText().trim();
        if (cpf.isEmpty()) {
            exibirMensagem("Atenção", "CPF deve ser preenchido para iniciar um novo cadastro.", SWT.ICON_WARNING);
            txtCpf.setFocus();
            return;
        }
        String msgValidacaoCpf = seguradoPessoaMediator.validarCpf(cpf);
        if (msgValidacaoCpf != null) {
            exibirMensagem("CPF Inválido", msgValidacaoCpf, SWT.ICON_ERROR);
            txtCpf.selectAll();
            txtCpf.setFocus();
            return;
        }
        SeguradoPessoa spExistente = seguradoPessoaMediator.buscarSeguradoPessoa(cpf);
        if (spExistente != null) {
            exibirMensagem("CPF Existente", "CPF já cadastrado. Os dados foram carregados para visualização ou alteração.", SWT.ICON_INFORMATION);
            preencherCamposComSegurado(spExistente);
            configurarEstadoCampos(false, true, true, true, "Alterar", true, true, true, false, false);
        } else {
            limparCamposDeDados();
            txtBonus.setText("0"); 
            configurarEstadoCampos(false, true, true, true, "Incluir", false, true, true, false, false);
            txtNome.setFocus();
        }
    }
    
    private void cancelarOperacao() {
        configurarEstadoInicialCampos();
    }

    private SeguradoPessoa coletarDadosDaTela() throws DateTimeParseException, NumberFormatException {
        String cpf = txtCpf.getText().trim();
        String nome = txtNome.getText().trim();
        LocalDate dataNascimento = null;
        if (!txtDataNascimento.getText().trim().isEmpty()){
            dataNascimento = LocalDate.parse(txtDataNascimento.getText().trim(), dateFormatter);
        }
        
        double renda = 0.0;
        String rendaStr = txtRenda.getText().trim();
        if(!rendaStr.isEmpty()){
            renda = Double.parseDouble(rendaStr);
        }

        Endereco endereco = new Endereco(
            txtLogradouro.getText().trim(),
            txtCep.getText().trim(),
            txtNumeroEndereco.getText().trim(),
            txtComplemento.getText().trim(),
            txtPais.getText().trim(),
            txtEstado.getText().trim(),
            txtCidade.getText().trim()
        );

        BigDecimal bonus = BigDecimal.ZERO;
        String bonusStr = txtBonus.getText().trim();
        if (!bonusStr.isEmpty()) {
            bonus = new BigDecimal(bonusStr);
        }
        
        return new SeguradoPessoa(nome, endereco, dataNascimento, bonus, cpf, renda);
    }
    
    private void preencherCamposComSegurado(SeguradoPessoa sp) {
        txtCpf.setText(sp.getCpf() != null ? sp.getCpf() : "");
        txtNome.setText(sp.getNome() != null ? sp.getNome() : "");
        txtDataNascimento.setText(sp.getDataNascimento() != null ? sp.getDataNascimento().format(dateFormatter) : "");
        
        txtRenda.setText(String.valueOf((long) sp.getRenda()));
        txtBonus.setText(sp.getBonus() != null ? String.valueOf(sp.getBonus().longValue()) : "0");
        
        Endereco end = sp.getEndereco();
        if (end != null) {
            txtLogradouro.setText(end.getLogradouro() != null ? end.getLogradouro() : "");
            txtNumeroEndereco.setText(end.getNumero() != null ? end.getNumero() : "");
            txtComplemento.setText(end.getComplemento() != null ? end.getComplemento() : "");
            txtCep.setText(end.getCep() != null ? end.getCep() : "");
            txtCidade.setText(end.getCidade() != null ? end.getCidade() : "");
            txtEstado.setText(end.getEstado() != null ? end.getEstado() : "");
            txtPais.setText(end.getPais() != null ? end.getPais() : "");
        } else {
            limparCamposDeDados(); 
        }
    }

    private void processarCliqueIncluirAlterar() {
        try {
            SeguradoPessoa spColetado = coletarDadosDaTela();
            String resultado;
            String operacao = btnIncluirAlterar.getText();

            if (operacao.equals("Incluir")) {
                resultado = seguradoPessoaMediator.incluirSeguradoPessoa(spColetado);
                if (resultado == null) {
                    exibirMensagem("Sucesso", "Segurado pessoa incluído com sucesso!", SWT.ICON_INFORMATION);
                    configurarEstadoInicialCampos();
                } else {
                    exibirMensagem("Erro de Validação", resultado, SWT.ICON_ERROR);
                }
            } else { // Alterar
                resultado = seguradoPessoaMediator.alterarSeguradoPessoa(spColetado);
                if (resultado == null) {
                    exibirMensagem("Sucesso", "Segurado pessoa alterado com sucesso!", SWT.ICON_INFORMATION);
                    configurarEstadoInicialCampos();
                } else {
                    exibirMensagem("Erro de Validação", resultado, SWT.ICON_ERROR);
                }
            }
        } catch (DateTimeParseException ex) {
            exibirMensagem("Erro de Formato", "Data de Nascimento inválida. Use AAAA-MM-DD.", SWT.ICON_ERROR);
        } catch (NumberFormatException ex) {
            exibirMensagem("Erro de Formato", "Valor da Renda ou Bônus inválido. Digite apenas números.", SWT.ICON_ERROR);
        } catch (Exception ex) {
            exibirMensagem("Erro Inesperado", "Ocorreu um erro: " + ex.getMessage(), SWT.ICON_ERROR);
            ex.printStackTrace();
        }
    }

    private void buscar() {
        String cpf = txtCpf.getText().trim();
        if (cpf.isEmpty()) {
            exibirMensagem("Atenção", "CPF deve ser informado para busca.", SWT.ICON_WARNING);
            return;
        }
        SeguradoPessoa sp = seguradoPessoaMediator.buscarSeguradoPessoa(cpf);
        if (sp != null) {
            preencherCamposComSegurado(sp);
            configurarEstadoCampos(false, true, true, true, "Alterar", true, true, true, false, false);
        } else {
            exibirMensagem("Informação", "Segurado pessoa com CPF '" + cpf + "' não encontrado.", SWT.ICON_INFORMATION);
            limparCamposDeDados();
            configurarEstadoCampos(true, false, false, false, "Incluir", false, false, true, true, true);
            txtCpf.setFocus();
        }
    }
    
    private void excluir() {
        String cpf = txtCpf.getText().trim();
        if (cpf.isEmpty() || !btnExcluir.isEnabled()) {
            exibirMensagem("Atenção", "Nenhum segurado carregado para exclusão. Realize uma busca primeiro.", SWT.ICON_WARNING);
            return;
        }
        
        MessageBox messageBoxConfirmacao = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        messageBoxConfirmacao.setText("Confirmação de Exclusão");
        messageBoxConfirmacao.setMessage("Deseja realmente excluir o segurado pessoa com CPF '" + cpf + "'?");
        int response = messageBoxConfirmacao.open();

        if (response == SWT.YES) {
            String resultado = seguradoPessoaMediator.excluirSeguradoPessoa(cpf);
            if (resultado == null) {
                exibirMensagem("Sucesso", "Segurado pessoa excluído com sucesso!", SWT.ICON_INFORMATION);
                configurarEstadoInicialCampos();
            } else {
                exibirMensagem("Erro", "Falha ao excluir: " + resultado, SWT.ICON_ERROR); 
            }
        }
    }
    
    private void exibirMensagem(String titulo, String mensagem, int tipoIcone) {
        if (shell == null || shell.isDisposed()) return;
        MessageBox messageBox = new MessageBox(shell, tipoIcone | SWT.OK);
        messageBox.setText(titulo);
        messageBox.setMessage(mensagem);
        messageBox.open();
    }
}