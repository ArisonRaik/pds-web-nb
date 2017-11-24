/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maisamo.smartalerta.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.maisamo.smartalerta.modelo.conexao.ConexaoBanco;
import com.maisamo.smartalerta.modelo.entidade.Alerta;
import com.maisamo.smartalerta.modelo.entidade.Usuario;

/**
 *
 * @author wagner
 */
public class AlertaDAO {

    private Connection conexao;
    private ResultSet rs;
    private PreparedStatement preparador;
    private boolean resultado = false;

    public boolean inserir(Alerta alerta) {
        String sql = "INSERT INTO alerta (titulo,mensagem,categoria,usuario_id) VALUES (?,?,?,?)";

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setString(1, alerta.getTitulo());
            preparador.setString(2, alerta.getMensagem());
            preparador.setString(3, alerta.getCategoria());
            preparador.setLong(4, alerta.getUsuario().getId());
            preparador.execute();
            resultado = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharInstrucao(preparador);
            ConexaoBanco.fecharConexao(conexao);
        }
        return resultado;
    }

    public boolean atualizar(Alerta alerta) {
        String sql = "UPDATE alerta SET titulo = ?, mensagem = ?, categoria = ? WHERE id = ?";

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setString(1, alerta.getTitulo());
            preparador.setString(2, alerta.getMensagem());
            preparador.setString(3, alerta.getCategoria());
            preparador.setLong(4, alerta.getId());
            preparador.execute();
            resultado = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharInstrucao(preparador);
            ConexaoBanco.fecharConexao(conexao);
        }
        return resultado;
    }

    public boolean excluir(Alerta alerta) {
        String sql = "DELETE FROM alerta WHERE id = ?";

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setLong(1, alerta.getId());
            preparador.execute();
            resultado = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharInstrucao(preparador);
            ConexaoBanco.fecharConexao(conexao);
        }
        return resultado;
    }

    public List<Alerta> listar(Usuario usuario) {
        String sql = "SELECT * FROM alerta WHERE usuario_id = ? ORDER BY id DESC";

        List<Alerta> lista = new ArrayList();

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setLong(1, usuario.getId());

            rs = preparador.executeQuery();

            while (rs.next()) {
                Alerta alerta = new Alerta(usuario);
                alerta.setId(rs.getLong("id"));
                alerta.setTitulo(rs.getString("titulo"));
                alerta.setMensagem(rs.getString("mensagem"));
                alerta.setCategoria(rs.getString("categoria"));
                lista.add(alerta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharInstrucao(preparador);
            ConexaoBanco.fecharConexao(conexao);
        }
        return lista;
    }

    public List<Alerta> listarPorCategoria(String categoria) {
        String sql = "SELECT * FROM alerta WHERE categoria = ? ORDER BY titulo";

        List<Alerta> lista = new ArrayList<>();

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setString(1, categoria);

            rs = preparador.executeQuery();

            while (rs.next()) {
                Alerta alerta = new Alerta();
                alerta.setId(rs.getLong("id"));
                alerta.setTitulo(rs.getString("titulo"));
                alerta.setMensagem(rs.getString("mensagem"));
                alerta.setCategoria(rs.getString("categoria"));
                lista.add(alerta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharInstrucao(preparador);
            ConexaoBanco.fecharConexao(conexao);
        }
        return lista;
    }

    public boolean procurar(String valor, String coluna) {
        String sql = "SELECT * FROM alerta WHERE " + coluna + " = ?";

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setString(1, valor);

            rs = preparador.executeQuery();

            if (rs.next()) {
                resultado = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharInstrucao(preparador);
            ConexaoBanco.fecharConexao(conexao);
        }
        return resultado;
    }
}
