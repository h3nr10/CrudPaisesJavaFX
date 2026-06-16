package com.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TextField txtNome;
    @FXML private TextField txtSigla;
    @FXML private TextField txtCapital;
    @FXML private TextField txtArea;
    @FXML private TextField txtPib;
    @FXML private TextField txtPopulacao;
    @FXML private TextField txtMilitar;
    @FXML private TextField txtPesquisa;

    @FXML private Label lblMensagem;
    @FXML private Label lblContador;

    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnExcluir;
    @FXML private Button btnLimpar;

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
    private FilteredList<PaisDTO> dadosFiltrados;
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

        configurarFiltrosNumericos();
        configurarPesquisa();

        listar();
        limpar();
    }

    @FXML
    public void salvar() {
        try {
            paisDAO.salvar(extrairDadosFormulario());
            exibirMensagem("Registro salvo com sucesso!", true);
            atualizarTela();
        } catch (Exception e) {
            exibirMensagem(e.getMessage(), false);
        }
    }

    @FXML
    public void atualizar() {
        if (idSelecionado == null) return;

        if (confirmarAcao("Confirmação de Edição", "Deseja salvar as alterações neste registro?")) {
            try {
                PaisDTO pais = extrairDadosFormulario();
                pais.setId(idSelecionado);
                paisDAO.atualizar(pais);
                exibirMensagem("Registro atualizado com sucesso!", true);
                atualizarTela();
            } catch (Exception e) {
                exibirMensagem(e.getMessage(), false);
            }
        }
    }

    @FXML
    public void excluir() {
        if (idSelecionado == null) return;

        if (confirmarAcao("Aviso de Exclusão", "Esta ação é permanente. Deseja remover este registro?")) {
            try {
                paisDAO.excluir(idSelecionado);
                exibirMensagem("Registro removido com sucesso!", true);
                atualizarTela();
            } catch (Exception e) {
                exibirMensagem(e.getMessage(), false);
            }
        }
    }

    public void listar() {
        try {
            lista.setAll(paisDAO.listar());
            lblContador.setText(lista.size() + " registros no banco");
        } catch (Exception e) {
            exibirMensagem("Falha ao carregar banco de dados.", false);
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
            txtArea.setText(p.getArea() != null ? String.valueOf(p.getArea()) : "");
            txtPib.setText(p.getPib() != null ? String.valueOf(p.getPib()) : "");
            txtPopulacao.setText(p.getPopulacao() != null ? String.valueOf(p.getPopulacao()) : "");
            txtMilitar.setText(p.getMilitar() != null ? String.valueOf(p.getMilitar()) : "");

            btnSalvar.setDisable(true);
            btnAtualizar.setDisable(false);
            btnExcluir.setDisable(false);
            lblMensagem.setText("");
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
        txtPesquisa.clear();

        idSelecionado = null;
        tablePaises.getSelectionModel().clearSelection();

        btnSalvar.setDisable(false);
        btnAtualizar.setDisable(true);
        btnExcluir.setDisable(true);

        txtNome.requestFocus();
    }

    private PaisDTO extrairDadosFormulario() {
        if (txtNome.getText().isBlank() || txtSigla.getText().isBlank()) {
            throw new IllegalArgumentException("Preencha todos os campos obrigatórios (*).");
        }

        PaisDTO p = new PaisDTO();
        p.setNome(txtNome.getText().trim());
        p.setSigla(txtSigla.getText().trim());
        p.setCapital(txtCapital.getText().trim());
        p.setArea(txtArea.getText().isBlank() ? 0.0 : Double.parseDouble(txtArea.getText().trim()));
        p.setPib(txtPib.getText().isBlank() ? 0.0 : Double.parseDouble(txtPib.getText().trim()));
        p.setPopulacao(txtPopulacao.getText().isBlank() ? 0 : Integer.parseInt(txtPopulacao.getText().trim()));
        p.setMilitar(txtMilitar.getText().isBlank() ? 0.0 : Double.parseDouble(txtMilitar.getText().trim()));

        return p;
    }

    private void configurarFiltrosNumericos() {
        txtPopulacao.textProperty().addListener((obs, antigo, novo) -> {
            if (!novo.matches("\\d*")) txtPopulacao.setText(novo.replaceAll("[^\\d]", ""));
        });

        configurarDecimal(txtArea);
        configurarDecimal(txtPib);
        configurarDecimal(txtMilitar);
    }

    private void configurarDecimal(TextField campo) {
        campo.textProperty().addListener((obs, antigo, novo) -> {
            if (!novo.matches("\\d*(\\.\\d*)?")) {
                campo.setText(antigo);
            }
        });
    }

    private void configurarPesquisa() {
        dadosFiltrados = new FilteredList<>(lista, p -> true);
        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> {
            dadosFiltrados.setPredicate(pais -> {
                if (novo == null || novo.isBlank()) return true;
                String filtro = novo.toLowerCase();
                return pais.getNome().toLowerCase().contains(filtro) ||
                        pais.getSigla().toLowerCase().contains(filtro) ||
                        (pais.getCapital() != null && pais.getCapital().toLowerCase().contains(filtro));
            });
        });
        tablePaises.setItems(dadosFiltrados);
    }

    private void atualizarTela() {
        listar();
        limpar();
    }

    private void exibirMensagem(String mensagem, boolean sucesso) {
        lblMensagem.setText(mensagem);
        lblMensagem.getStyleClass().removeAll("lbl-sucesso", "lbl-erro");
        lblMensagem.getStyleClass().add(sucesso ? "lbl-sucesso" : "lbl-erro");
    }

    private boolean confirmarAcao(String titulo, String cabecalho) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText("Escolha OK para prosseguir ou Cancelar para abortar.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}