package com.template.validator;

/**
 * Interface para definir um contrato de validação (OCP).
 * Certifique-se de que todas as classes estejam no mesmo pacote 'validator'.
 */
public interface Validador<T> {

    boolean validar(T valorAtual);

    String getMensagemErro();

    T getValor();
}
