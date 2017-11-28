/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maisamo.smartalerta.modelo.entidade;

import com.maisamo.smartalerta.controle.AcessarPagina;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import com.maisamo.smartalerta.modelo.servico.Seguranca;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wagner
 */
public class Pagina {

    private Long id;
    private StringBuffer url, params;
    private LocalDateTime datahora_expira;
    private final Alerta alerta;
    private final Usuario usuario;
    private final Contato contato;

    public Pagina(Alerta alerta, Usuario usuario, Contato contato) {
        this.alerta = alerta;
        this.usuario = usuario;
        this.contato = contato;
        datahora_expira = LocalDateTime.now().plusHours(24);
    }

    public String getUrl() {
        url.append("localhost:8084/smartalerta/AcessarPagina?");
        try {
            url.append(getParams());
        } catch (Exception ex) {
            Logger.getLogger(AcessarPagina.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url.toString();
    }

    private String getParams() throws Exception {
        params.append("frm=").append(Seguranca.paraRSA(usuario.getNome()));
        params.append("&par=").append(Seguranca.paraRSA(contato.getNome()));
        params.append("&cat=").append(Seguranca.paraRSA(alerta.getTitulo()));
        params.append("&tit=").append(Seguranca.paraRSA(alerta.getCategoria()));
        params.append("&msg=").append(Seguranca.paraRSA(alerta.getMensagem()));
        return params.toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataExpira() {
        return datahora_expira.toLocalDate();
    }

    public LocalTime getHoraExpira() {
        return datahora_expira.toLocalTime();
    }

    public void setDataHoraExpira(LocalDateTime datahora_expira) {
        this.datahora_expira = datahora_expira;
    }

    public boolean expirou() {
        return LocalDateTime.now().compareTo(datahora_expira) > 0;
    }

    public Alerta getAlerta() {
        return alerta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Contato getContato() {
        return contato;
    }
}
