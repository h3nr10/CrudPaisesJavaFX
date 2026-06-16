package com.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TextField txtNome;
    @FXML private TextField txtSigla;
    @FXML private TextField txtCapital;
    @FXML private TextField txtArea;
    @FXML private TextField txtPib;
    @FXML private TextField txtPopulacao;
    @FXML private TextField txtMilitar;

    @FXML private Button btnAtualizar;
    @FXML private Button btnExcluir;

    @FXML private TableView<PaisDTO> tablePaises;
    @FXML private TableColumn<PaisDTO, Long> colId;
    @FXML private TableColumn<PaisDTO, String> colNome;
    @FXML private TableColumn<PaisDTO, String> colSigla;
    @FXML private TableColumn<PaisDTO, String> colCapital;
    @FXML private TableColumn<PaisDTO, String> colArea;
    @FXML private TableColumn<PaisDTO, String> colPib;
    @FXML private TableColumn<PaisDTO, String> colPopulacao;
    @FXML private TableColumn<PaisDTO, String> colMilitar;

    private final ObservableList<PaisDTO> lista = FXCollections.observableArrayList();
    private final PaisDAO paisDAO = new PaisDAO();
    private Long idSelecionado = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSigla.setCellValueFactory(new PropertyValueFactory<>("sigla"));
        colCapital.setCellValueFactory(new PropertyValueFactory<>("capital"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("areaFormatada"));
        colPib.setCellValueFactory(new PropertyValueFactory<>("pibFormatado"));
        colPopulacao.setCellValueFactory(new PropertyValueFactory<>("populacaoFormatada"));
        colMilitar.setCellValueFactory(new PropertyValueFactory<>("militarFormatado"));

        listar();

        btnAtualizar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    @FXML
    public void salvar() {
        try {
            paisDAO.salvar(extrairDadosFormulario());
            atualizarTela();
        } catch (Exception e) {
            alerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void atualizar() {
        if (idSelecionado == null) {
            alerta("Selecione um país na tabela para atualizar.", Alert.AlertType.WARNING);
            return;
        }
        try {
            PaisDTO pais = extrairDadosFormulario();
            pais.setId(idSelecionado);
            paisDAO.atualizar(pais);
            atualizarTela();
        } catch (Exception e) {
            alerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void excluir() {
        if (idSelecionado == null) {
            alerta("Selecione um país na tabela para excluir.", Alert.AlertType.WARNING);
            return;
        }
        try {
            paisDAO.excluir(idSelecionado);
            atualizarTela();
        } catch (Exception e) {
            alerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void listar() {
        try {
            lista.setAll(paisDAO.listar());
            tablePaises.setItems(lista);
        } catch (Exception e) {
            alerta("Erro ao listar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void selecionarPais() {
        PaisDTO p = tablePaises.getSelectionModel().getSelectedItem();

        if (p != null) {
            idSelecionado = p.getId();
            txtNome.setText(p.getNome());
            txtSigla.setText(p.getSigla());
            txtCapital.setText(p.getCapital());
            txtArea.setText(String.valueOf(p.getArea()));
            txtPib.setText(String.valueOf(p.getPib()));
            txtPopulacao.setText(String.valueOf(p.getPopulacao()));
            txtMilitar.setText(String.valueOf(p.getMilitar()));

            btnAtualizar.setDisable(false);
            btnExcluir.setDisable(false);
        }
    }

    @FXML
    public void limpar() {
        txtNome.clear();
        txtSigla.clear();
        txtCapital.clear();
        txtArea.clear();
        txtPib.clear();
        txtPopulacao.clear();
        txtMilitar.clear();
        idSelecionado = null;
        tablePaises.getSelectionModel().clearSelection();

        btnAtualizar.setDisable(true);
        btnExcluir.setDisable(true);
    }

    private PaisDTO extrairDadosFormulario() {
        if (txtNome.getText().isBlank() || txtSigla.getText().isBlank()) {
            throw new IllegalArgumentException("Os campos Nome e Sigla são obrigatórios!");
        }

        try {
            PaisDTO p = new PaisDTO();
            p.setNome(txtNome.getText().trim());
            p.setSigla(txtSigla.getText().trim());
            p.setCapital(txtCapital.getText().trim());
            p.setArea(Double.parseDouble(txtArea.getText().trim()));
            p.setPib(Double.parseDouble(txtPib.getText().trim()));
            p.setPopulacao(Integer.parseInt(txtPopulacao.getText().trim()));
            p.setMilitar(Double.parseDouble(txtMilitar.getText().trim()));
            return p;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Verifique os campos numéricos. Use apenas números e '.' para decimais.");
        }
    }

    private void atualizarTela() {
        listar();
        limpar();
    }

    private void alerta(String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}