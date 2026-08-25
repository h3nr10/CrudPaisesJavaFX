package com.template.validator;

import com.template.model.dto.PaisDTO;

import java.util.ArrayList;
import java.util.List;

public class PaisFormularioValidador {

    // Método principal de validação que combina todas as regras
    public void validarPais(PaisDTO pais) {
        // Validadores de texto (nome, sigla) - aplicados sequencialmente
        List<Validador<String>> validadoresTexto = new ArrayList<>();
        validadoresTexto.add(new CampoObrigatorioValidador("Nome", pais.getNome()));
        validadoresTexto.add(new CampoObrigatorioValidador("Sigla", pais.getSigla()));
        validadoresTexto.add(new SiglaValidador(pais.getSigla())); // formato específico da sigla

        for (Validador<String> validador : validadoresTexto) {
            aplicar(validador);
        }

        // Validadores numéricos - nenhum valor pode ser negativo
        List<Validador<Double>> validadoresNumericos = new ArrayList<>();
        validadoresNumericos.add(new ValorNaoNegativoValidador("Área", pais.getArea()));
        validadoresNumericos.add(new ValorNaoNegativoValidador("PIB", pais.getPib()));
        validadoresNumericos.add(new ValorNaoNegativoValidador("População",
                pais.getPopulacao() != null ? pais.getPopulacao().doubleValue() : null));
        validadoresNumericos.add(new ValorNaoNegativoValidador("Índice de Poder Militar", pais.getMilitar()));

        for (Validador<Double> validador : validadoresNumericos) {
            aplicar(validador);
        }
    }

    // Aplica um validador genérico, seja ele de String, Double ou qualquer outro tipo.
    // É essa assinatura genérica <T> que permite reaproveitar o mesmo método para
    // qualquer implementação de Validador<T>, sem precisar duplicar o loop.
    private <T> void aplicar(Validador<T> validador) {
        if (!validador.validar(validador.getValor())) {
            throw new IllegalArgumentException(validador.getMensagemErro());
        }
    }
}
