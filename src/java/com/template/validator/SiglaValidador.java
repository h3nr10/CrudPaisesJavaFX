package com.template.validator;

import java.util.regex.Pattern;

public class SiglaValidador implements Validador<String> {

    private static final String SIGLA_REGEX = "^[A-Za-z]{2,3}$";
    private final Pattern pattern = Pattern.compile(SIGLA_REGEX);
    private final String sigla;

    public SiglaValidador(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public boolean validar(String valorAtual) {
        return this.sigla != null && pattern.matcher(this.sigla.trim()).matches();
    }

    @Override
    public String getMensagemErro() {
        return "Digite uma sigla válida, com 2 a 3 letras (exemplo: BRA)!";
    }

    @Override
    public String getValor() {
        return sigla;
    }
}
