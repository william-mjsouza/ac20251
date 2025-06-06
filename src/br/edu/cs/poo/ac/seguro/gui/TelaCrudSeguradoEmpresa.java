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
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class TelaCrudSeguradoEmpresa {

    protected Shell shell;
    private Text txtCnpj;
    private Text txtNomeEmpresa;
    private Text txtDataAbertura;
    private Text txtFaturamento;
    private Button chkEhLocadora;
    private Text txtBonusEmpresa;

    private Text txtLogradouroEmpresa;
    private Text txtNumeroEnderecoEmpresa;
    private Text txtComplementoEmpresa;
    private Text txtCepEmpresa;
    private Text txtCidadeEmpresa;
    private Text txtEstadoEmpresa;
    private Text txtPaisEmpresa;

    private Button btnNovo;
    private Button btnIncluir;
    private Button btnBuscar;
    private Button btnAlterar;
    private Button btnExcluir;
    private Button btnCancelar;

    private SeguradoEmpresaMediator seguradoEmpresaMediator = SeguradoEmpresaMediator.getInstancia();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private Color colorThemeBackground;
    private Color colorTextDark;
    private Color colorButtonText;
    private Font customFont;
    private Font labelFontBold;


    public static void main(String[] args) {
        try {
            TelaCrudSeguradoEmpresa window = new TelaCrudSeguradoEmpresa();
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
        shell.setSize(600, 700); 
        shell.setText("CRUD de Segurado Empresa - Design Personalizado");
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(colorThemeBackground);
        shell.setFont(customFont);

        shell.addDisposeListener(e -> disposeResources());

        Composite topComposite = new Composite(shell, SWT.NONE);
        topComposite.setLayout(new GridLayout(4, false)); 
        topComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        topComposite.setBackground(colorThemeBackground);

        Label lblCnpj = new Label(topComposite, SWT.NONE);
        lblCnpj.setText("CNPJ (somente números):");
        lblCnpj.setFont(labelFontBold);
        lblCnpj.setForeground(colorTextDark);
        lblCnpj.setBackground(colorThemeBackground);

        txtCnpj = new Text(topComposite, SWT.BORDER);
        txtCnpj.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtCnpj.setFont(customFont);
        txtCnpj.addVerifyListener(e -> {
            if (!e.text.matches("[0-9]*")) {
                e.doit = false;
            }
        });

        btnNovo = new Button(topComposite, SWT.PUSH);
        btnNovo.setText("Novo");
        btnNovo.setFont(customFont);
        btnNovo.setBackground(colorTextDark); 
        btnNovo.setForeground(colorButtonText);
        btnNovo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));


        btnBuscar = new Button(topComposite, SWT.PUSH);
        btnBuscar.setText("Buscar");
        btnBuscar.setFont(customFont);
        btnBuscar.setBackground(colorTextDark);
        btnBuscar.setForeground(colorButtonText);
        btnBuscar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));


        Group grpEmpresa = new Group(shell, SWT.NONE);
        grpEmpresa.setText("Dados da Empresa");
        grpEmpresa.setLayout(new GridLayout(2, false));
        grpEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        grpEmpresa.setFont(labelFontBold);
        grpEmpresa.setForeground(colorTextDark);
        grpEmpresa.setBackground(colorThemeBackground);

        createStyledLabel(grpEmpresa, "Nome Empresa:");
        txtNomeEmpresa = createStyledText(grpEmpresa);

        createStyledLabel(grpEmpresa, "Data Abertura (AAAA-MM-DD):");
        txtDataAbertura = createStyledText(grpEmpresa);
        txtDataAbertura.addVerifyListener(e -> {
            String currentText = ((Text) e.widget).getText();
            if (e.text.matches("[0-9\\-]*")) {
                String newText = currentText.substring(0, e.start) + e.text + currentText.substring(e.end);
                if (newText.length() > 10) { e.doit = false; }
            } else if (!e.text.isEmpty()) { e.doit = false; }
        });

        createStyledLabel(grpEmpresa, "Faturamento (somente números):");
        txtFaturamento = createStyledText(grpEmpresa);
        txtFaturamento.addVerifyListener(e -> {
            if (!e.text.matches("[0-9]*")) {
                e.doit = false;
            }
        });
        
        createStyledLabel(grpEmpresa, "É Locadora de Veículos?");
        chkEhLocadora = new Button(grpEmpresa, SWT.CHECK);
        chkEhLocadora.setBackground(colorThemeBackground);
        chkEhLocadora.setFont(customFont);
        chkEhLocadora.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

        createStyledLabel(grpEmpresa, "Bônus (somente números):");
        txtBonusEmpresa = createStyledText(grpEmpresa, SWT.BORDER);
        txtBonusEmpresa.addVerifyListener(e -> {
            if (!e.text.matches("[0-9]*")) {
                e.doit = false;
            }
        });


        Group grpEnderecoEmpresa = new Group(shell, SWT.NONE);
        grpEnderecoEmpresa.setText("Endereço");
        grpEnderecoEmpresa.setLayout(new GridLayout(2, false));
        grpEnderecoEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        grpEnderecoEmpresa.setFont(labelFontBold);
        grpEnderecoEmpresa.setForeground(colorTextDark);
        grpEnderecoEmpresa.setBackground(colorThemeBackground);
        
        createStyledLabel(grpEnderecoEmpresa, "Logradouro:");
        txtLogradouroEmpresa = createStyledText(grpEnderecoEmpresa);

        createStyledLabel(grpEnderecoEmpresa, "Número:");
        txtNumeroEnderecoEmpresa = createStyledText(grpEnderecoEmpresa);
        
        createStyledLabel(grpEnderecoEmpresa, "Complemento:");
        txtComplementoEmpresa = createStyledText(grpEnderecoEmpresa);

        createStyledLabel(grpEnderecoEmpresa, "CEP (somente números):");
        txtCepEmpresa = createStyledText(grpEnderecoEmpresa);
        txtCepEmpresa.addVerifyListener(e -> {
            if (!e.text.matches("[0-9]*")) {
                e.doit = false;
            }
        });

        createStyledLabel(grpEnderecoEmpresa, "Cidade:");
        txtCidadeEmpresa = createStyledText(grpEnderecoEmpresa);

        createStyledLabel(grpEnderecoEmpresa, "Estado (UF):");
        txtEstadoEmpresa = createStyledText(grpEnderecoEmpresa);
        txtEstadoEmpresa.setTextLimit(2);

        createStyledLabel(grpEnderecoEmpresa, "País:");
        txtPaisEmpresa = createStyledText(grpEnderecoEmpresa);

        Composite bottomButtonsComposite = new Composite(shell, SWT.NONE);
        bottomButtonsComposite.setLayout(new GridLayout(4, true)); 
        bottomButtonsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        bottomButtonsComposite.setBackground(colorThemeBackground);
        
        btnIncluir = new Button(bottomButtonsComposite, SWT.PUSH);
        btnIncluir.setText("Incluir");
        btnIncluir.setFont(customFont);
        btnIncluir.setBackground(colorTextDark);
        btnIncluir.setForeground(colorButtonText);
        btnIncluir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        btnAlterar = new Button(bottomButtonsComposite, SWT.PUSH);
        btnAlterar.setText("Alterar");
        btnAlterar.setFont(customFont);
        btnAlterar.setBackground(colorTextDark);
        btnAlterar.setForeground(colorButtonText);
        btnAlterar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        btnCancelar = new Button(bottomButtonsComposite, SWT.PUSH);
        btnCancelar.setText("Cancelar");
        btnCancelar.setFont(customFont);
        btnCancelar.setBackground(colorTextDark);
        btnCancelar.setForeground(colorButtonText);
        btnCancelar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        btnExcluir = new Button(bottomButtonsComposite, SWT.PUSH);
        btnExcluir.setText("Excluir");
        btnExcluir.setFont(customFont);
        btnExcluir.setBackground(colorTextDark);
        btnExcluir.setForeground(colorButtonText);
        btnExcluir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        addEventListeners();
        configurarEstadoInicialCampos();
        configureTabOrder(topComposite, grpEmpresa, grpEnderecoEmpresa, bottomButtonsComposite);
    }

    private void createStyledLabel(Composite parent, String text) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        label.setFont(labelFontBold);
        label.setForeground(colorTextDark);
        label.setBackground(colorThemeBackground);
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
    
    private void addEventListeners() {
        btnNovo.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { processarCliqueNovo(); }
        });
        btnIncluir.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { incluir(); }
        });
        btnBuscar.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { buscar(); }
        });
        btnAlterar.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { alterar(); }
        });
        btnExcluir.addSelectionListener(new SelectionAdapter() {
            @Override public void widgetSelected(SelectionEvent e) { excluir(); }
        });
        btnCancelar.addSelectionListener(new SelectionAdapter() { 
            @Override public void widgetSelected(SelectionEvent e) { cancelarOperacao(); }
        });
    }

    private void configureTabOrder(Composite paramTopComposite, Group paramGrpEmpresa, Group paramGrpEndereco, Composite paramBottomButtonsComposite) {
        shell.setTabList(new Control[] { paramTopComposite, paramGrpEmpresa, paramGrpEndereco, paramBottomButtonsComposite });
        paramTopComposite.setTabList(new Control[] { txtCnpj, btnNovo, btnBuscar });
        paramGrpEmpresa.setTabList(new Control[] { 
            txtNomeEmpresa, txtDataAbertura, txtFaturamento, chkEhLocadora, txtBonusEmpresa 
        });
        paramGrpEndereco.setTabList(new Control[] { 
            txtLogradouroEmpresa, txtNumeroEnderecoEmpresa, txtComplementoEmpresa, 
            txtCepEmpresa, txtCidadeEmpresa, txtEstadoEmpresa, txtPaisEmpresa 
        });

        paramBottomButtonsComposite.setTabList(new Control[] {
            btnIncluir, btnAlterar, btnCancelar, btnExcluir
        });

    }
    
    private void configurarEstadoCampos(boolean cnpjEnabled, boolean outrosDadosEnabled, 
                                          boolean incluirEnabled, boolean buscarEnabledTop,
                                          boolean alterarEnabled, boolean excluirEnabled, 
                                          boolean novoEnabledTop, boolean cancelarEnabled) {
        txtCnpj.setEnabled(cnpjEnabled);
        txtNomeEmpresa.setEnabled(outrosDadosEnabled);
        txtDataAbertura.setEnabled(outrosDadosEnabled);
        txtFaturamento.setEnabled(outrosDadosEnabled);
        chkEhLocadora.setEnabled(outrosDadosEnabled);
        txtBonusEmpresa.setEnabled(outrosDadosEnabled); 

        txtLogradouroEmpresa.setEnabled(outrosDadosEnabled);
        txtNumeroEnderecoEmpresa.setEnabled(outrosDadosEnabled);
        txtComplementoEmpresa.setEnabled(outrosDadosEnabled);
        txtCepEmpresa.setEnabled(outrosDadosEnabled);
        txtCidadeEmpresa.setEnabled(outrosDadosEnabled);
        txtEstadoEmpresa.setEnabled(outrosDadosEnabled);
        txtPaisEmpresa.setEnabled(outrosDadosEnabled);

        btnNovo.setEnabled(novoEnabledTop);
        btnIncluir.setEnabled(incluirEnabled);
        btnBuscar.setEnabled(buscarEnabledTop);
        btnAlterar.setEnabled(alterarEnabled);
        btnExcluir.setEnabled(excluirEnabled);
        btnCancelar.setEnabled(cancelarEnabled);
    }

    private void configurarEstadoInicialCampos() {
        configurarEstadoCampos(true, false, false, true, false, false, true, false);
        limparTodosOsCamposDeTexto();
        txtCnpj.setFocus();
    }

    private void configurarModoInclusao() {
        configurarEstadoCampos(false, true, true, false, false, false, false, true);
        limparCamposDeDadosAposBuscaFalhaOuParaNovo(); 
        txtNomeEmpresa.setFocus();
    }
    
    private void configurarModoVisualizacaoOuAlteracao() {
        configurarEstadoCampos(false, true, false, false, true, true, false, true); 
        txtNomeEmpresa.setFocus();
    }
    
    private void configurarEstadoAposBuscaFalha() {
        configurarEstadoCampos(true, false, false, true, false, false, true, false);
        limparCamposDeDadosAposBuscaFalhaOuParaNovo(); 
        txtCnpj.setFocus();
    }

    private void limparTodosOsCamposDeTexto() {
        if(txtCnpj != null && !txtCnpj.isDisposed()) txtCnpj.setText(""); 
        limparCamposDeDadosAposBuscaFalhaOuParaNovo();
    }
    
    private void limparCamposDeDadosAposBuscaFalhaOuParaNovo() { 
        if(txtNomeEmpresa != null && !txtNomeEmpresa.isDisposed()) txtNomeEmpresa.setText("");
        if(txtDataAbertura != null && !txtDataAbertura.isDisposed()) txtDataAbertura.setText("");
        if(txtFaturamento != null && !txtFaturamento.isDisposed()) txtFaturamento.setText("");
        if(chkEhLocadora != null && !chkEhLocadora.isDisposed()) chkEhLocadora.setSelection(false);
        if(txtBonusEmpresa != null && !txtBonusEmpresa.isDisposed()) txtBonusEmpresa.setText("");
        if(txtLogradouroEmpresa != null && !txtLogradouroEmpresa.isDisposed()) txtLogradouroEmpresa.setText("");
        if(txtNumeroEnderecoEmpresa != null && !txtNumeroEnderecoEmpresa.isDisposed()) txtNumeroEnderecoEmpresa.setText("");
        if(txtComplementoEmpresa != null && !txtComplementoEmpresa.isDisposed()) txtComplementoEmpresa.setText("");
        if(txtCepEmpresa != null && !txtCepEmpresa.isDisposed()) txtCepEmpresa.setText("");
        if(txtCidadeEmpresa != null && !txtCidadeEmpresa.isDisposed()) txtCidadeEmpresa.setText("");
        if(txtEstadoEmpresa != null && !txtEstadoEmpresa.isDisposed()) txtEstadoEmpresa.setText("");
        if(txtPaisEmpresa != null && !txtPaisEmpresa.isDisposed()) txtPaisEmpresa.setText("");
    }
    
    private void processarCliqueNovo() {
        String cnpj = txtCnpj.getText().trim();
        if (cnpj.isEmpty()) {
            exibirMensagem("Atenção", "Por favor, digite um CNPJ antes de clicar em Novo.", SWT.ICON_WARNING);
            txtCnpj.setFocus();
            return;
        }
        String msgValidacaoCnpj = seguradoEmpresaMediator.validarCnpj(cnpj); 
        if (msgValidacaoCnpj != null) {
            exibirMensagem("CNPJ Inválido", msgValidacaoCnpj, SWT.ICON_ERROR);
            txtCnpj.selectAll();
            txtCnpj.setFocus();
            return;
        }
        SeguradoEmpresa seExistente = seguradoEmpresaMediator.buscarSeguradoEmpresa(cnpj); 
        if (seExistente != null) {
            exibirMensagem("CNPJ Existente", "CNPJ já cadastrado. Os dados foram carregados para visualização ou alteração.", SWT.ICON_INFORMATION);
            preencherCamposComSegurado(seExistente);
            configurarModoVisualizacaoOuAlteracao(); 
        } else {
            configurarModoInclusao(); 
        }
    }
    
    private void cancelarOperacao() {
        configurarEstadoInicialCampos(); 
    }

    private SeguradoEmpresa coletarDadosDaTela(boolean paraAlteracao) throws DateTimeParseException, NumberFormatException {
        String cnpj = txtCnpj.getText().trim(); 
        String nome = txtNomeEmpresa.getText().trim();
        LocalDate dataAbertura = null;
        if (!txtDataAbertura.getText().trim().isEmpty()){
            dataAbertura = LocalDate.parse(txtDataAbertura.getText().trim(), dateFormatter);
        }
        
        double faturamento = 0.0; 
        String faturamentoStr = txtFaturamento.getText().trim();
        if (!faturamentoStr.isEmpty()){
            faturamento = Double.parseDouble(faturamentoStr);
        }
        
        boolean ehLocadora = chkEhLocadora.getSelection();

        Endereco endereco = new Endereco( 
            txtLogradouroEmpresa.getText().trim(),
            txtCepEmpresa.getText().trim(),
            txtNumeroEnderecoEmpresa.getText().trim(),
            txtComplementoEmpresa.getText().trim(),
            txtPaisEmpresa.getText().trim(),
            txtEstadoEmpresa.getText().trim(),
            txtCidadeEmpresa.getText().trim()
        );

        BigDecimal bonus = BigDecimal.ZERO;
        String bonusStr = txtBonusEmpresa.getText().trim();
        if (!bonusStr.isEmpty()) {
            bonus = new BigDecimal(bonusStr);
        }
        
        return new SeguradoEmpresa(nome, endereco, dataAbertura, bonus, cnpj, faturamento, ehLocadora); 
    }
    
    private void preencherCamposComSegurado(SeguradoEmpresa se) {
        txtCnpj.setText(se.getCnpj() != null ? se.getCnpj() : "");
        txtNomeEmpresa.setText(se.getNome() != null ? se.getNome() : "");
        txtDataAbertura.setText(se.getDataAbertura() != null ? se.getDataAbertura().format(dateFormatter) : "");
        
        txtFaturamento.setText(String.valueOf((long) se.getFaturamento()));
        
        chkEhLocadora.setSelection(se.isEhLocadoraDeVeiculos()); 
        
        txtBonusEmpresa.setText(se.getBonus() != null ? String.valueOf(se.getBonus().longValue()) : "0");
        
        Endereco end = se.getEndereco(); 
        if (end != null) {
            txtLogradouroEmpresa.setText(end.getLogradouro() != null ? end.getLogradouro() : "");
            txtNumeroEnderecoEmpresa.setText(end.getNumero() != null ? end.getNumero() : "");
            txtComplementoEmpresa.setText(end.getComplemento() != null ? end.getComplemento() : "");
            txtCepEmpresa.setText(end.getCep() != null ? end.getCep() : "");
            txtCidadeEmpresa.setText(end.getCidade() != null ? end.getCidade() : "");
            txtEstadoEmpresa.setText(end.getEstado() != null ? end.getEstado() : "");
            txtPaisEmpresa.setText(end.getPais() != null ? end.getPais() : "");
        } else {
            limparCamposDeDadosAposBuscaFalhaOuParaNovo();
        }
    }

    private void incluir() {
        try {
            SeguradoEmpresa seColetado = coletarDadosDaTela(false); 
            String resultado = seguradoEmpresaMediator.incluirSeguradoEmpresa(seColetado); 
            if (resultado == null) {
                exibirMensagem("Sucesso", "Segurado empresa incluído com sucesso!", SWT.ICON_INFORMATION);
                configurarEstadoInicialCampos(); 
            } else {
                exibirMensagem("Erro de Validação", resultado, SWT.ICON_ERROR);
            }
        } catch (DateTimeParseException ex) {
            exibirMensagem("Erro de Formato", "Data de Abertura inválida. Use AAAA-MM-DD.", SWT.ICON_ERROR);
        } catch (NumberFormatException ex) { 
            exibirMensagem("Erro de Formato", "Valor do Faturamento ou Bônus inválido. Digite apenas números.", SWT.ICON_ERROR);
        } catch (Exception ex) {
            exibirMensagem("Erro Inesperado", "Ocorreu um erro: " + ex.getMessage(), SWT.ICON_ERROR);
            ex.printStackTrace();
        }
    }

    private void buscar() {
        String cnpj = txtCnpj.getText().trim();
        if (cnpj.isEmpty()) {
            exibirMensagem("Atenção", "CNPJ deve ser informado para busca.", SWT.ICON_WARNING);
            limparCamposDeDadosAposBuscaFalhaOuParaNovo();
            configurarEstadoCampos(true, false, false, true, false, false, true, false);
            return;
        }
        SeguradoEmpresa se = seguradoEmpresaMediator.buscarSeguradoEmpresa(cnpj); 
        if (se != null) {
            preencherCamposComSegurado(se);
            configurarModoVisualizacaoOuAlteracao();
        } else {
            exibirMensagem("Informação", "Segurado empresa com CNPJ '" + cnpj + "' não encontrado.", SWT.ICON_INFORMATION);
            configurarEstadoAposBuscaFalha();   
        }
    }
    
    private void alterar() {
        try {
            String cnpj = txtCnpj.getText().trim(); 
            if (cnpj.isEmpty() || !txtNomeEmpresa.isEnabled()) {
                 exibirMensagem("Atenção", "Busque um segurado antes de tentar alterar.", SWT.ICON_WARNING);
                 return;
            }
            SeguradoEmpresa seParaAlterar = coletarDadosDaTela(true); 
            String resultado = seguradoEmpresaMediator.alterarSeguradoEmpresa(seParaAlterar); 
            if (resultado == null) {
                exibirMensagem("Sucesso", "Segurado empresa alterado com sucesso!", SWT.ICON_INFORMATION);
                configurarEstadoInicialCampos();
            } else {
                exibirMensagem("Erro de Validação", resultado, SWT.ICON_ERROR);
            }
        } catch (DateTimeParseException ex) {
            exibirMensagem("Erro de Formato", "Data de Abertura inválida. Use AAAA-MM-DD.", SWT.ICON_ERROR);
        } catch (NumberFormatException ex) {
            exibirMensagem("Erro de Formato", "Valor do Faturamento ou Bônus inválido. Digite apenas números.", SWT.ICON_ERROR);
        } catch (Exception ex) {
            exibirMensagem("Erro Inesperado", "Ocorreu um erro: " + ex.getMessage(), SWT.ICON_ERROR);
            ex.printStackTrace();
        }
    }

    private void excluir() {
        String cnpj = txtCnpj.getText().trim();
         if (cnpj.isEmpty() || !btnExcluir.isEnabled()) {
            exibirMensagem("Atenção", "Nenhum segurado carregado para exclusão. Realize uma busca primeiro.", SWT.ICON_WARNING);
            return;
        }
        
        MessageBox messageBoxConfirmacao = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        messageBoxConfirmacao.setText("Confirmação de Exclusão");
        messageBoxConfirmacao.setMessage("Deseja realmente excluir o segurado empresa com CNPJ '" + cnpj + "'?");
        int response = messageBoxConfirmacao.open();

        if (response == SWT.YES) {
            String resultado = seguradoEmpresaMediator.excluirSeguradoEmpresa(cnpj); 
            if (resultado == null) {
                exibirMensagem("Sucesso", "Segurado empresa excluído com sucesso!", SWT.ICON_INFORMATION);
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