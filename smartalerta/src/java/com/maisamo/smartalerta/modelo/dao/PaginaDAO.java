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
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.maisamo.smartalerta.modelo.conexao.ConexaoBanco;
import com.maisamo.smartalerta.modelo.entidade.Pagina;
import com.maisamo.smartalerta.modelo.entidade.Alerta;
import com.maisamo.smartalerta.modelo.entidade.Usuario;
import com.maisamo.smartalerta.modelo.entidade.Contato;

/**
 *
 * @author wagner
 */
public class PaginaDAO {

    private Connection conexao;
    private ResultSet rs;
    private PreparedStatement preparador;
    private boolean resultado;

    public boolean inserir(Pagina pagina) {
        String sql = "INSERT INTO pagina (url, data_expira, hora_expira, alerta_id, usuario_id, contato_id) values (?,?,?,?,?,?)";

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setString(1, pagina.getUrl());
            preparador.setDate(2, Date.valueOf(pagina.getDataExpira()));
            preparador.setTime(3, Time.valueOf(pagina.getHoraExpira()));
            preparador.setLong(4, pagina.getAlerta().getId());
            preparador.setLong(5, pagina.getUsuario().getId());
            preparador.setLong(6, pagina.getContato().getId());
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

    public boolean excluir(Pagina pagina) {
        String sql = "DELETE FROM pagina WHERE 1=?";

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);
            preparador.setBoolean(1, pagina.expirou());
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

    public List<Pagina> listar() {
        String sql = "SELECT * FROM pagina ORDER BY id";

        List<Pagina> lista = new ArrayList();

        try {
            conexao = ConexaoBanco.abrirConexao();
            preparador = conexao.prepareStatement(sql);

            rs = preparador.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                Alerta alerta = new Alerta(usuario);
                Contato contato = new Contato(usuario);
                Pagina pagina = new Pagina(alerta, usuario, contato);
                pagina.setDataHoraExpira(
                        LocalDateTime.of(
                                rs.getDate("data_expira").toLocalDate(),
                                rs.getTime("hora_expira").toLocalTime()
                        )
                );
                lista.add(pagina);
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
}
