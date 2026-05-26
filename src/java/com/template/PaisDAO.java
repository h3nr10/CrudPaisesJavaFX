package com.template;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaisDAO {

    private final Conexao conexao = new Conexao();

    public void salvar(PaisDTO pais) {
        String sql = """
            INSERT INTO paises (nome, sigla, capital, area_km2, pib_ppc_bilhoes, populacao, indice_poder_militar)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, pais);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar país: " + e.getMessage(), e);
        }
    }

    public void atualizar(PaisDTO pais) {
        String sql = """
            UPDATE paises SET nome=?, sigla=?, capital=?, area_km2=?, pib_ppc_bilhoes=?, populacao=?, indice_poder_militar=?
            WHERE id=?
        """;
        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, pais);
            stmt.setLong(8, pais.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar país: " + e.getMessage(), e);
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM paises WHERE id=?";
        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir país: " + e.getMessage(), e);
        }
    }

    public List<PaisDTO> listar() {
        List<PaisDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM paises ORDER BY nome ASC";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PaisDTO p = new PaisDTO(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("sigla"),
                        rs.getString("capital"),
                        rs.getDouble("area_km2"),
                        rs.getDouble("pib_ppc_bilhoes"),
                        rs.getInt("populacao"),
                        rs.getDouble("indice_poder_militar")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar países: " + e.getMessage(), e);
        }
        return lista;
    }

    private void preencherStatement(PreparedStatement stmt, PaisDTO pais) throws SQLException {
        stmt.setString(1, pais.getNome());
        stmt.setString(2, pais.getSigla());
        stmt.setString(3, pais.getCapital());
        stmt.setDouble(4, pais.getArea());
        stmt.setDouble(5, pais.getPib());
        stmt.setInt(6, pais.getPopulacao());
        stmt.setDouble(7, pais.getMilitar());
    }
}