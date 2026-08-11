package com.template.validator;

/**
 * Responsabilidade unica desta classe: validar os dados de um pais
 * antes de serem persistidos. Nao conhece componentes de UI nem
 * detalhes de acesso a banco de dados.
 */
public class PaisValidator {

    public void validarCampos(String nome, String sigla) {
        if (nome == null || nome.isBlank() || sigla == null || sigla.isBlank()) {
            throw new IllegalArgumentException("Preencha todos os campos obrigatórios (*).");
        }
    }
}
