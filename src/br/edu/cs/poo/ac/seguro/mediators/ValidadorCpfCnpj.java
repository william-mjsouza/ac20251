package br.edu.cs.poo.ac.seguro.mediators;

public class ValidadorCpfCnpj {
	public static boolean ehCnpjValido(String cnpj) {
		if (cnpj == null) return false;

        String num = cnpj.replaceAll("\\D", "");
        if (num.length() != 14 || num.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (num.charAt(i) - '0') * peso1[i];
        }
        int resto = soma % 11;
        int dig1 = (resto < 2) ? 0 : 11 - resto;

        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (num.charAt(i) - '0') * peso2[i];
        }
        resto = soma % 11;
        int dig2 = (resto < 2) ? 0 : 11 - resto;

        return dig1 == (num.charAt(12) - '0') && dig2 == (num.charAt(13) - '0');
	}
	public static boolean ehCpfValido(String cpf) {
		if (cpf == null) return false;

        String num = cpf.replaceAll("\\D", "");
        if (num.length() != 11 || num.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (num.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int dig1 = (resto < 2) ? 0 : 11 - resto;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (num.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int dig2 = (resto < 2) ? 0 : 11 - resto;

        return dig1 == (num.charAt(9) - '0') && dig2 == (num.charAt(10) - '0');
	}
}
