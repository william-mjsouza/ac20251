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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;

public class TelaInclusaoApolice {

    protected Shell shell;
    private ApoliceMediator mediator;

    private Text txtCpfOuCnpj;
    private Text txtPlaca;
    private Text txtAno;
    private Text txtValorMaximoSegurado;
    private Combo cmbCategoria;

    private Button btnIncluir;
    private Button btnLimpar;

    private List<CategoriaVeiculo> categoriasOrdenadas;
    private Composite mainComposite;

    private Color colorThemeBackground;
    private Color colorButtonText;
    private Color colorTextDark;
    private Font customFont;
    private Font labelFontBold;
    
    private DecimalFormat numberInputParser;

    public static void main(String[] args) {
        try {
            TelaInclusaoApolice window = new TelaInclusaoApolice();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        Display display = Display.getDefault();
        setupFormatters();
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
    
    private void setupFormatters() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        numberInputParser = new DecimalFormat("#,##0.00", symbols);
        numberInputParser.setParseBigDecimal(true);
    }

    protected void createResources(Display display) {

        colorThemeBackground = new Color(display, 220, 220, 220); // #DCDCDC (Cinza Claro)
        colorButtonText = new Color(display, 255, 255, 255);  
        colorTextDark = new Color(display, 0, 0, 0); 

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
        colorButtonText.dispose();
        colorTextDark.dispose();
        customFont.dispose();
        labelFontBold.dispose();
    }

    protected void createContents() {
        mediator = ApoliceMediator.getInstancia();

        shell = new Shell(Display.getDefault(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX | SWT.RESIZE);
        shell.setSize(500, 360);
        shell.setText("Inclusão de Apólice");
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(colorThemeBackground);
        shell.setFont(customFont);

        shell.addDisposeListener(e -> disposeResources());

        mainComposite = new Composite(shell, SWT.NONE);
        mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mainComposite.setLayout(new GridLayout(2, false));
        mainComposite.setBackground(colorThemeBackground);
        mainComposite.setFont(customFont);

        createStyledLabel(mainComposite, "CPF/CNPJ Proprietário:");
        txtCpfOuCnpj = createStyledText(mainComposite);
        txtCpfOuCnpj.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) { 
                if (!e.text.matches("[0-9]*") && !e.text.isEmpty()) {
                    e.doit = false;
                }
            }
        });

        createStyledLabel(mainComposite, "Placa Veículo:");
        txtPlaca = createStyledText(mainComposite);

        createStyledLabel(mainComposite, "Ano Veículo (2020-2025):");
        txtAno = createStyledText(mainComposite);
        txtAno.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                String currentText = ((Text) e.widget).getText();
                if (e.text.matches("[0-9]*")) {
                    String newText = currentText.substring(0, e.start) + e.text + currentText.substring(e.end);
                    if (newText.length() > 4) { e.doit = false; } 
                } else if (!e.text.isEmpty()) { e.doit = false; }
            }
        });

        createStyledLabel(mainComposite, "Valor Máx. Segurado (R$):");
        txtValorMaximoSegurado = createStyledText(mainComposite);
        txtValorMaximoSegurado.setMessage("Ex: 50000,00");
        txtValorMaximoSegurado.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                String currentText = ((Text) e.widget).getText();
                String newChar = e.text;
                char decimalSeparator = DecimalFormatSymbols.getInstance(new Locale("pt", "BR")).getDecimalSeparator();
                if (newChar.matches("[0-9]*")) { e.doit = true; 
                } else if (newChar.equals(String.valueOf(decimalSeparator)) && !currentText.contains(String.valueOf(decimalSeparator))) { e.doit = true; 
                } else if (newChar.isEmpty()) { e.doit = true;
                } else { e.doit = false; }
            }
        });

        createStyledLabel(mainComposite, "Categoria Veículo:");
        cmbCategoria = new Combo(mainComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        cmbCategoria.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        cmbCategoria.setFont(customFont);
        cmbCategoria.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        cmbCategoria.setForeground(colorTextDark);
        populateComboCategorias();

        Composite buttonsComposite = new Composite(mainComposite, SWT.NONE);
        GridLayout gl_buttonsComposite = new GridLayout(2, true);
        gl_buttonsComposite.marginWidth = 0;
        buttonsComposite.setLayout(gl_buttonsComposite);
        GridData gd_buttonsComposite = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        buttonsComposite.setLayoutData(gd_buttonsComposite);
        buttonsComposite.setBackground(colorThemeBackground);

        btnIncluir = createStyledButton(buttonsComposite, "Incluir Apólice");
        btnLimpar = createStyledButton(buttonsComposite, "Limpar Campos");

        addEventListeners();
        configureTabOrder(buttonsComposite);
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
        Text text = new Text(parent, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        text.setFont(customFont);
        text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE)); 
        text.setForeground(colorTextDark); 
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
        btnIncluir.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                incluirNovaApolice();
            }
        });

        btnLimpar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                limparCampos();
            }
        });
    }
    
    private void configureTabOrder(Composite buttonsComposite) {
        mainComposite.setTabList(new Control[] {
            txtCpfOuCnpj, txtPlaca, txtAno, txtValorMaximoSegurado, cmbCategoria, buttonsComposite
        });
        buttonsComposite.setTabList(new Control[] {btnIncluir, btnLimpar});
        shell.setTabList(new Control[] {mainComposite});
    }

    private void populateComboCategorias() {
        if (cmbCategoria == null || cmbCategoria.isDisposed()) {
            return;
        }
        categoriasOrdenadas = Arrays.stream(CategoriaVeiculo.values())
                                     .sorted(Comparator.comparing(CategoriaVeiculo::getNome))
                                     .collect(Collectors.toList());
        cmbCategoria.removeAll();
        for (CategoriaVeiculo cat : categoriasOrdenadas) {
            cmbCategoria.add(cat.getNome());
        }
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.select(0);
        }
    }

    private void limparCampos() {
        if (txtCpfOuCnpj != null && !txtCpfOuCnpj.isDisposed()) txtCpfOuCnpj.setText("");
        if (txtPlaca != null && !txtPlaca.isDisposed()) txtPlaca.setText("");
        if (txtAno != null && !txtAno.isDisposed()) txtAno.setText("");
        if (txtValorMaximoSegurado != null && !txtValorMaximoSegurado.isDisposed()) txtValorMaximoSegurado.setText("");
        if (cmbCategoria != null && !cmbCategoria.isDisposed() && cmbCategoria.getItemCount() > 0) {
            cmbCategoria.select(0);
        }
        if (txtCpfOuCnpj != null && !txtCpfOuCnpj.isDisposed()) txtCpfOuCnpj.setFocus();
    }

    private void incluirNovaApolice() {
        if (shell == null || shell.isDisposed()){
            return;
        }

        String cpfOuCnpj = txtCpfOuCnpj.getText().trim();
        String placa = txtPlaca.getText().trim();
        String anoStr = txtAno.getText().trim();
        String valorMaxStr = txtValorMaximoSegurado.getText().trim();
        int categoriaIndex = cmbCategoria.getSelectionIndex();

        if (cpfOuCnpj.isEmpty() || placa.isEmpty() || anoStr.isEmpty() || valorMaxStr.isEmpty() || categoriaIndex == -1) {
            exibirMensagem("Campos Obrigatórios", "Todos os campos devem ser preenchidos.", SWT.ICON_WARNING);
            return;
        }

        int ano;
        try {
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException ex) {
            exibirMensagem("Erro de Formato", "Ano do veículo deve ser um número inteiro válido.", SWT.ICON_ERROR);
            txtAno.setFocus();
            return;
        }

        BigDecimal valorMaximoSegurado;
        try {
            Number parsedNumber = numberInputParser.parse(valorMaxStr);
            valorMaximoSegurado = ((BigDecimal) parsedNumber).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (ParseException ex) {
            exibirMensagem("Erro de Formato", "Valor máximo segurado deve ser um número decimal válido (Ex: 50000,00).", SWT.ICON_ERROR);
            txtValorMaximoSegurado.setFocus();
            return;
        }

        int codigoCategoria = -1;
        if (categoriaIndex >= 0 && categoriaIndex < categoriasOrdenadas.size()) {
            codigoCategoria = categoriasOrdenadas.get(categoriaIndex).getCodigo();
        } else {
             exibirMensagem("Seleção Inválida", "Selecione uma categoria de veículo válida.", SWT.ICON_ERROR);
             cmbCategoria.setFocus();
             return;
        }

        DadosVeiculo dados = new DadosVeiculo(cpfOuCnpj, placa, ano, valorMaximoSegurado, codigoCategoria);
        RetornoInclusaoApolice retorno = mediator.incluirApolice(dados);

        if (retorno.getMensagemErro() != null) {
            exibirMensagem("Erro na Inclusão", retorno.getMensagemErro(), SWT.ICON_ERROR);
        } else {
            exibirMensagem("Sucesso", "Apólice incluída com sucesso! Anote o número da apólice: " + retorno.getNumeroApolice(), SWT.ICON_INFORMATION);
            limparCampos();
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