package br.edu.cs.poo.ac.seguro.mediators;

public class StringUtils {
	private StringUtils() {}
	public static boolean ehNuloOuBranco(String str) {
		return (str == null) || str.trim().isEmpty();
	}
	
    public static boolean temSomenteNumeros(String input) {
    	if (ehNuloOuBranco(input)) {
            return false;
        }
    	
        for (int i = 0; i < input.length(); i++) {
        	if (!(input.charAt(i) >= '0'  && input.charAt(i) <= '9')) {
        		return false;
        	}
        }
        
    	return true; 
    }
}
