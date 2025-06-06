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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;

public class TelaInclusaoSinistro {

    protected Shell shell;
    private SinistroMediator mediator;

    private Text txtPlaca;
    private Text txtDataHoraSinistro;
    private Text txtUsuarioRegistro;
    private Text txtValorSinistro;
    private Combo cmbTipoSinistro;

    private Button btnIncluir;
    private Button btnLimpar;

    private List<TipoSinistro> tiposSinistroOrdenados;
    private Composite mainComposite;

    private Color colorThemeBackground;
    private Color colorTextDark;
    private Color colorButtonText;
    private Font customFont;
    private Font labelFontBold;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String DATETIME_PATTERN_PLACEHOLDER = "AAAA-MM-DDTHH:MM:SS";
    private DecimalFormat numberInputParser;


    public static void main(String[] args) {
        try {
            TelaInclusaoSinistro window = new TelaInclusaoSinistro();
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
        mediator = SinistroMediator.getInstancia();

        shell = new Shell(Display.getDefault(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX | SWT.RESIZE);
        shell.setSize(520, 380);
        shell.setText("Inclusão de Sinistro");
        shell.setLayout(new GridLayout(1, false));
        shell.setBackground(colorThemeBackground);
        shell.setFont(customFont);

        shell.addDisposeListener(e -> disposeResources());

        mainComposite = new Composite(shell, SWT.NONE);
        mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mainComposite.setLayout(new GridLayout(2, false));
        mainComposite.setBackground(colorThemeBackground);
        mainComposite.setFont(customFont);

        createStyledLabel(mainComposite, "Placa Veículo:");
        txtPlaca = createStyledText(mainComposite);

        createStyledLabel(mainComposite, "Data/Hora Sinistro (" + DATETIME_PATTERN_PLACEHOLDER + "):");
        txtDataHoraSinistro = createStyledText(mainComposite);
        txtDataHoraSinistro.setMessage(DATETIME_PATTERN_PLACEHOLDER);

        createStyledLabel(mainComposite, "Usuário Registro:");
        txtUsuarioRegistro = createStyledText(mainComposite);

        createStyledLabel(mainComposite, "Valor Sinistro (R$):");
        txtValorSinistro = createStyledText(mainComposite);
        txtValorSinistro.setMessage("Ex: 1500,75");
        txtValorSinistro.addVerifyListener(new VerifyListener() {
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


        createStyledLabel(mainComposite, "Tipo de Sinistro:");
        cmbTipoSinistro = new Combo(mainComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        cmbTipoSinistro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        cmbTipoSinistro.setFont(customFont);
        cmbTipoSinistro.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        cmbTipoSinistro.setForeground(colorTextDark);
        populateComboTiposSinistro();

        Composite buttonsComposite = new Composite(mainComposite, SWT.NONE);
        GridLayout gl_buttonsComposite = new GridLayout(2, true);
        gl_buttonsComposite.marginWidth = 0;
        buttonsComposite.setLayout(gl_buttonsComposite);
        GridData gd_buttonsComposite = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        buttonsComposite.setLayoutData(gd_buttonsComposite);
        buttonsComposite.setBackground(colorThemeBackground);

        btnIncluir = createStyledButton(buttonsComposite, "Incluir Sinistro");
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
                incluirNovoSinistro();
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
            txtPlaca, txtDataHoraSinistro, txtUsuarioRegistro, txtValorSinistro, cmbTipoSinistro, buttonsComposite
        });
        buttonsComposite.setTabList(new Control[] {btnIncluir, btnLimpar});
        shell.setTabList(new Control[] {mainComposite});
    }

    private void populateComboTiposSinistro() {
        if (cmbTipoSinistro == null || cmbTipoSinistro.isDisposed()) {
            return;
        }
        tiposSinistroOrdenados = Arrays.stream(TipoSinistro.values())
                                     .sorted(Comparator.comparing(TipoSinistro::getDescricao, String.CASE_INSENSITIVE_ORDER))
                                     .collect(Collectors.toList());
        cmbTipoSinistro.removeAll();
        for (TipoSinistro tipo : tiposSinistroOrdenados) {
            cmbTipoSinistro.add(tipo.getDescricao());
        }
        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.select(0);
        }
    }

    private void limparCampos() {
        if (txtPlaca != null && !txtPlaca.isDisposed()) txtPlaca.setText("");
        if (txtDataHoraSinistro != null && !txtDataHoraSinistro.isDisposed()) txtDataHoraSinistro.setText("");
        if (txtUsuarioRegistro != null && !txtUsuarioRegistro.isDisposed()) txtUsuarioRegistro.setText("");
        if (txtValorSinistro != null && !txtValorSinistro.isDisposed()) txtValorSinistro.setText("");
        if (cmbTipoSinistro != null && !cmbTipoSinistro.isDisposed() && cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.select(0);
        }
        if (txtPlaca != null && !txtPlaca.isDisposed()) txtPlaca.setFocus();
    }

    private void incluirNovoSinistro() {
        if (shell == null || shell.isDisposed()) {
            return;
        }

        String placa = txtPlaca.getText().trim();
        String dataHoraStr = txtDataHoraSinistro.getText().trim();
        String usuarioRegistro = txtUsuarioRegistro.getText().trim();
        String valorSinistroStr = txtValorSinistro.getText().trim();
        int tipoSinistroIndex = cmbTipoSinistro.getSelectionIndex();

        if (placa.isEmpty() || dataHoraStr.isEmpty() || usuarioRegistro.isEmpty() || valorSinistroStr.isEmpty() || tipoSinistroIndex == -1) {
            exibirMensagem("Campos Obrigatórios", "Todos os campos devem ser preenchidos.", SWT.ICON_WARNING);
            return;
        }

        LocalDateTime dataHoraSinistro;
        try {
            dataHoraSinistro = LocalDateTime.parse(dataHoraStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            exibirMensagem("Erro de Formato", "Formato de Data/Hora inválido. Use " + DATETIME_PATTERN_PLACEHOLDER + " (Ex: 2025-06-05T23:30:00)", SWT.ICON_ERROR);
            txtDataHoraSinistro.setFocus();
            return;
        }

        double valorSinistroDouble;
        try {
            Number parsedNumber = numberInputParser.parse(valorSinistroStr);
            valorSinistroDouble = parsedNumber.doubleValue();
        } catch (ParseException ex) {
            exibirMensagem("Erro de Formato", "Valor do Sinistro inválido. Use um número (Ex: 1500,75).", SWT.ICON_ERROR);
            txtValorSinistro.setFocus();
            return;
        }

        int codigoTipoSinistro = -1;
        if (tipoSinistroIndex >= 0 && tipoSinistroIndex < tiposSinistroOrdenados.size()) {
            codigoTipoSinistro = tiposSinistroOrdenados.get(tipoSinistroIndex).getCodigo();
        } else {
             exibirMensagem("Seleção Inválida", "Selecione um tipo de sinistro válido.", SWT.ICON_ERROR);
             cmbTipoSinistro.setFocus();
             return;
        }

        DadosSinistro dadosParaMediator = new DadosSinistro(placa, dataHoraSinistro, usuarioRegistro, valorSinistroDouble, codigoTipoSinistro);

        try {
            if (mediator == null) {
                 exibirMensagem("Erro Interno", "Mediator de Sinistro não inicializado.", SWT.ICON_ERROR);
                 return;
            }
            String numeroGerado = mediator.incluirSinistro(dadosParaMediator, LocalDateTime.now());
            
            exibirMensagem("Inclusão Bem Sucedida", "Sinistro incluído com sucesso! Anote o número do sinistro: " + numeroGerado, SWT.ICON_INFORMATION);
            limparCampos();
        } catch (ExcecaoValidacaoDados ex) {
            List<String> mensagensErro = ex.getMensagens();
            StringBuilder errosFormatados = new StringBuilder("Erro de Validação:\n");
            if (mensagensErro != null && !mensagensErro.isEmpty()) {
                 for (String msg : mensagensErro) {
                    errosFormatados.append("- ").append(msg).append("\n");
                }
            } else if (ex.getMessage() != null) {
                errosFormatados.append(ex.getMessage());
            } else {
                errosFormatados.append("Ocorreu um erro de validação não especificado.");
            }
            exibirMensagem("Erro de Validação", errosFormatados.toString().trim(), SWT.ICON_ERROR);
        } catch (Exception exGenerica) {
            exGenerica.printStackTrace(); 
            exibirMensagem("Erro Geral", "Ocorreu um erro inesperado: " + exGenerica.getMessage(), SWT.ICON_ERROR);
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