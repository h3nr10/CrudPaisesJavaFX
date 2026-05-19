package com.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TextField txtNome;
    @FXML private TextField txtSigla;
    @FXML private TextField txtCapital;
    @FXML private TextField txtArea;
    @FXML private TextField txtPib;
    @FXML private TextField txtPopulacao;
    @FXML private TextField txtMilitar;

    @FXML private TableView<Pais> tablePaises;

    @FXML private TableColumn<Pais, Long> colId;
    @FXML private TableColumn<Pais, String> colNome;
    @FXML private TableColumn<Pais, String> colSigla;
    @FXML private TableColumn<Pais, String> colCapital;
    @FXML private TableColumn<Pais, Double> colArea;
    @FXML private TableColumn<Pais, Double> colPib;
    @FXML private TableColumn<Pais, Integer> colPopulacao;
    @FXML private TableColumn<Pais, Double> colMilitar;

    private final ObservableList<Pais> lista =
            FXCollections.observableArrayList();

    private final Conexao conexao = new Conexao();

    private Long idSelecionado = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colNome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));

        colSigla.setCellValueFactory(
                new PropertyValueFactory<>("sigla"));

        colCapital.setCellValueFactory(
                new PropertyValueFactory<>("capital"));

        colArea.setCellValueFactory(
                new PropertyValueFactory<>("area"));

        colPib.setCellValueFactory(
                new PropertyValueFactory<>("pib"));

        colPopulacao.setCellValueFactory(
                new PropertyValueFactory<>("populacao"));

        colMilitar.setCellValueFactory(
                new PropertyValueFactory<>("militar"));

        listar();
    }

    @FXML
    public void salvar() {

        String sql = """
            INSERT INTO paises
            (nome, sigla, capital,
             area_km2, pib_ppc_bilhoes,
             populacao, indice_poder_militar)

            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = conexao.conectar();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, txtNome.getText());
            stmt.setString(2, txtSigla.getText());
            stmt.setString(3, txtCapital.getText());

            stmt.setDouble(4,
                    Double.parseDouble(txtArea.getText()));

            stmt.setDouble(5,
                    Double.parseDouble(txtPib.getText()));

            stmt.setInt(6,
                    Integer.parseInt(txtPopulacao.getText()));

            stmt.setDouble(7,
                    Double.parseDouble(txtMilitar.getText()));

            stmt.execute();

            listar();

            limpar();

        } catch (Exception e) {

            alerta(e.getMessage());
        }
    }

    @FXML
    public void atualizar() {

        if (idSelecionado == null) {
            alerta("Selecione um país");
            return;
        }

        String sql = """
            UPDATE paises
            SET nome=?,
                sigla=?,
                capital=?,
                area_km2=?,
                pib_ppc_bilhoes=?,
                populacao=?,
                indice_poder_militar=?
            WHERE id=?
        """;

        try (
                Connection conn = conexao.conectar();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, txtNome.getText());
            stmt.setString(2, txtSigla.getText());
            stmt.setString(3, txtCapital.getText());

            stmt.setDouble(4,
                    Double.parseDouble(txtArea.getText()));

            stmt.setDouble(5,
                    Double.parseDouble(txtPib.getText()));

            stmt.setInt(6,
                    Integer.parseInt(txtPopulacao.getText()));

            stmt.setDouble(7,
                    Double.parseDouble(txtMilitar.getText()));

            stmt.setLong(8, idSelecionado);

            stmt.execute();

            listar();

            limpar();

        } catch (Exception e) {

            alerta(e.getMessage());
        }
    }

    @FXML
    public void excluir() {

        if (idSelecionado == null) {
            alerta("Selecione um país");
            return;
        }

        String sql =
                "DELETE FROM paises WHERE id=?";

        try (
                Connection conn = conexao.conectar();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, idSelecionado);

            stmt.execute();

            listar();

            limpar();

        } catch (Exception e) {

            alerta(e.getMessage());
        }
    }

    public void listar() {

        lista.clear();

        String sql = "SELECT * FROM paises";

        try (
                Connection conn = conexao.conectar();
                PreparedStatement stmt =
                        conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Pais p = new Pais();

                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setSigla(rs.getString("sigla"));
                p.setCapital(rs.getString("capital"));
                p.setArea(rs.getDouble("area_km2"));
                p.setPib(rs.getDouble("pib_ppc_bilhoes"));
                p.setPopulacao(rs.getInt("populacao"));

                p.setMilitar(
                        rs.getDouble(
                                "indice_poder_militar"
                        )
                );

                lista.add(p);
            }

            tablePaises.setItems(lista);

        } catch (Exception e) {

            alerta(e.getMessage());
        }
    }

    @FXML
    public void selecionarPais() {

        Pais p = tablePaises
                .getSelectionModel()
                .getSelectedItem();

        if (p != null) {

            idSelecionado = p.getId();

            txtNome.setText(p.getNome());
            txtSigla.setText(p.getSigla());
            txtCapital.setText(p.getCapital());

            txtArea.setText(
                    String.valueOf(p.getArea()));

            txtPib.setText(
                    String.valueOf(p.getPib()));

            txtPopulacao.setText(
                    String.valueOf(
                            p.getPopulacao()));

            txtMilitar.setText(
                    String.valueOf(
                            p.getMilitar()));
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
    }

    public void alerta(String msg) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setContentText(msg);

        alert.show();
    }
}