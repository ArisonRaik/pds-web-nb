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

import java.util.List;

import com.maisamo.smartalerta.modelo.fachada.AlertaFacede;
import com.maisamo.smartalerta.modelo.fachada.PaginaFacede;
import com.maisamo.smartalerta.modelo.fachada.EnvioAlertaFacede;
import com.maisamo.smartalerta.modelo.fachada.EnvioAlertaContatoFacede;

import com.maisamo.smartalerta.modelo.entidade.Usuario;
import com.maisamo.smartalerta.modelo.entidade.Alerta;
import com.maisamo.smartalerta.modelo.entidade.Contato;
import com.maisamo.smartalerta.modelo.entidade.Pagina;
import com.maisamo.smartalerta.modelo.entidade.EnvioAlerta;
import com.maisamo.smartalerta.modelo.entidade.EnvioAlertaContato;

import com.maisamo.smartalerta.modelo.servico.Enviador;

/**
 *
 * @author wagner
 */
@WebServlet(name = "EnviarAlerta", urlPatterns = {"/EnviarAlerta"})
public class EnviarAlerta extends HttpServlet {

    private HttpSession sessao = null;

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
            out.println("<title>Servlet EnviarAlerta</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EnviarAlerta at " + request.getContextPath() + "</h1>");
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
        verificarSessao(request, response);
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
        verificarSessao(request, response);

        Usuario usuario = (Usuario) sessao.getAttribute("usuario");
        String[] enviarPara = request.getParameterValues("enviarPara");
        String alerta = request.getParameter("alerta");
        //Pagina pagina = new Pagina();
        //pagina.addParam("nome=" + contato.getNome);
        //pagina.addParam("&titulo=" + alerta.getTitulo());
        //pagina.addParam("&categoria=" + alerta.getCategoria());
        //pagina.addParam("&mensagem=" + alerta.getMensagem());
        String nomeFrom = usuario.getNome();
        String emailFrom = usuario.getEmail();
        String nomeTo = request.getParameter("nome");

        String emailTo = request.getParameter("email");
        String foneTo = request.getParameter("telefone");
        String mensagem = request.getParameter("mensagem");
        String assunto = request.getParameter("tipo_alerta");
        String url = "https://www.youtube.com/";
        Enviador env = new Enviador();
        env.enviarEmail();
        response.sendRedirect("enviar_alerta.jsp");
    }

    private void verificarSessao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sessao = request.getSession(false);
        if (sessao == null) {
            response.sendRedirect("acesso_negado.jsp");
        }
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
