package com.softwarecorporativo.monitoriaifpe.util.constantes;

public enum Situacao {
    APROVADO(1), ESPERA(2);

    public int situacao;

    Situacao(int valor) {
        situacao = valor;
    }
}