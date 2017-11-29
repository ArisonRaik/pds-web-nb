/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maisamo.smartalerta.controle;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;

import com.maisamo.smartalerta.modelo.fachada.AlertaFacede;
import com.maisamo.smartalerta.modelo.fachada.AcessoPaginaFacede;
import com.maisamo.smartalerta.modelo.entidade.AcessoPagina;
import com.maisamo.smartalerta.modelo.entidade.Alerta;
/**
 *
 * @author wagner
 */
@WebServlet(name = "AcessarPagina", urlPatterns = {"/AcessarPagina"})
public class AcessarPagina extends HttpServlet {

    private HttpSession sessao = null;
    private final AlertaFacede af = new AlertaFacede();
    private final AcessoPaginaFacede apf = new AcessoPaginaFacede();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AcessarPagina</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AcessarPagina at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AcessoPagina acesso_pagina = new AcessoPagina();
        acesso_pagina.setDataHoraAcesso(LocalDateTime.now());
        apf.inserir(acesso_pagina);

        sessao = request.getSession(true);
        sessao.setAttribute("from", request.getParameter("frm").replaceAll("+", " "));
        sessao.setAttribute("to", request.getParameter("par").replaceAll("+", " "));
        
        Long aid = Long.parseLong(request.getParameter("aid"));
        Alerta alerta = af.procurarPorId(aid);
        sessao.setAttribute("categoria", alerta.getCategoria());
        sessao.setAttribute("titulo", alerta.getTitulo());
        sessao.setAttribute("mensagem", alerta.getMensagem());
        
        response.sendRedirect("acesso_pagina.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
