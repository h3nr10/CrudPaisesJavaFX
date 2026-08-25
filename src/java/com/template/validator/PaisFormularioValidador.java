package com.template.validator;

import java.util.ArrayList;
import java.util.List;

public class PaisFormularioValidador {

    public void validarPais(String nome, String sigla) {
        List<Validador<String>> validadores = new ArrayList<>();

        validadores.add(new CampoObrigatorioValidador("Nome", nome));
        validadores.add(new CampoObrigatorioValidador("Sigla", sigla));

        validadores.add(new SiglaValidador(sigla));

        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                throw new IllegalArgumentException(validador.getMensagemErro());
            }
        }
    }
}
