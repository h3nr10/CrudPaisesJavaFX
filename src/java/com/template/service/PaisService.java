package com.template.service;

import com.template.model.dao.PaisDAO;
import com.template.model.dto.PaisDTO;
import com.template.validator.PaisValidator;

import java.util.List;

/**
 * Responsabilidade unica desta classe: conter a logica de negocio
 * relacionada a Pais - construir o DTO a partir dos dados do
 * formulario, validar (delegando ao PaisValidator) e orquestrar a
 * persistencia atraves do PaisDAO. O Controller nao acessa mais o
 * DAO nem a validacao diretamente.
 */
public class PaisService {

    private final PaisDAO paisDAO = new PaisDAO();
    private final PaisValidator paisValidator = new PaisValidator();

    public List<PaisDTO> listar() {
        return paisDAO.listar();
    }

    public void salvar(PaisDTO pais) {
        paisValidator.validarCampos(pais.getNome(), pais.getSigla());
        paisDAO.salvar(pais);
    }

    public void atualizar(PaisDTO pais) {
        paisValidator.validarCampos(pais.getNome(), pais.getSigla());
        paisDAO.atualizar(pais);
    }

    public void excluir(Long id) {
        paisDAO.excluir(id);
    }

    public PaisDTO criarPaisDoFormulario(String nome, String sigla, String capital, String area,
                                          String pib, String populacao, String militar) {
        PaisDTO p = new PaisDTO();
        p.setNome(nome != null ? nome.trim() : "");
        p.setSigla(sigla != null ? sigla.trim() : "");
        p.setCapital(capital != null ? capital.trim() : "");
        p.setArea(area == null || area.isBlank() ? 0.0 : Double.parseDouble(area.trim()));
        p.setPib(pib == null || pib.isBlank() ? 0.0 : Double.parseDouble(pib.trim()));
        p.setPopulacao(populacao == null || populacao.isBlank() ? 0 : Integer.parseInt(populacao.trim()));
        p.setMilitar(militar == null || militar.isBlank() ? 0.0 : Double.parseDouble(militar.trim()));
        return p;
    }
}
