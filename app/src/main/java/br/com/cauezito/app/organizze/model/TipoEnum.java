package br.com.cauezito.app.organizze.model;

public enum TipoEnum {
    D("Despesa"),
    E("Entrada");

    private String tipo;

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) {this.tipo = tipo;
    }

    TipoEnum(String tipo) {
        this.tipo = tipo;
    }
}
