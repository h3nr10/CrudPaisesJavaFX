package com.template.validator;

public class ValorNaoNegativoValidador implements Validador<Double> {

    private final String nomeCampo;
    private final Double valor; // Armazena o valor a ser validado

    public ValorNaoNegativoValidador(String nomeCampo, Double valor) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
    }

    @Override
    public boolean validar(Double valorAtual) { // O valor do parâmetro é o que será validado neste ciclo
        return this.valor != null && this.valor >= 0;
    }

    @Override
    public String getMensagemErro() {
        return "O campo " + nomeCampo + " não pode ser negativo.";
    }

    @Override
    public Double getValor() {
        return valor;
    }
}
