package br.edu.cs.poo.ac.seguro.entidades;

public enum TipoSinistro {

    COLISAO(1,"Colisão"),
    INCENDIO(2,"Incêndio"),
    FURTO(3, "Furto"),
    ENCHENTE(4, "Enchente"),
    DEPREDACAO(5, "Depredação");

    private int codigo;
    private String descricao;

    private TipoSinistro(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoSinistro getTipoSinistro(int codigo){
        for (TipoSinistro tipo : TipoSinistro.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        return null;
    }
}