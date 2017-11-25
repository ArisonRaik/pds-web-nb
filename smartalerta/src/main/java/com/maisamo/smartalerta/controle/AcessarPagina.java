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

import com.maisamo.smartalerta.modelo.fachada.AcessoPaginaFacede;
import com.maisamo.smartalerta.modelo.entidade.AcessoPagina;
import com.maisamo.smartalerta.modelo.servico.Seguranca;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wagner
 */
@WebServlet(name = "AcessarPagina", urlPatterns = {"/AcessarPagina"})
public class AcessarPagina extends HttpServlet {

    private HttpSession sessao = null;
    private AcessoPaginaFacede apf = null;

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
        apf = new AcessoPaginaFacede();
        AcessoPagina acesso_pagina = new AcessoPagina();
        acesso_pagina.setDataHoraAcesso(LocalDateTime.now());
        apf.inserir(acesso_pagina);

        sessao = request.getSession(true);
        try {
            sessao.setAttribute("from", Seguranca.deRSA(request.getParameter("frm")));
            sessao.setAttribute("to", Seguranca.deRSA(request.getParameter("par")));
            sessao.setAttribute("categoria", Seguranca.deRSA(request.getParameter("cat")));
            sessao.setAttribute("titulo", Seguranca.deRSA(request.getParameter("tit")));
            sessao.setAttribute("mensagem", Seguranca.deRSA(request.getParameter("msg")));

            response.sendRedirect("acesso_pagina.jsp");
        } catch (Exception ex) {
            Logger.getLogger(AcessarPagina.class.getName()).log(Level.SEVERE, null, ex);
        }
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
