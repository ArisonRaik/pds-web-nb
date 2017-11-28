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
import com.maisamo.smartalerta.modelo.fachada.ContatoFacede;
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
    private final AlertaFacede af = new AlertaFacede();
    private final ContatoFacede cf = new ContatoFacede();
    private final EnvioAlertaFacede eaf = new EnvioAlertaFacede();
    private final EnvioAlertaContatoFacede eacf = new EnvioAlertaContatoFacede();
    private final PaginaFacede pf = new PaginaFacede();
    
    private Pagina pag = null;
    private Enviador env = null;
    
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
        
        Usuario u = (Usuario) sessao.getAttribute("usuario");
        sessao.setAttribute("contatos", cf.listar(u));
        sessao.setAttribute("categorias", af.listarCategorias(u));
        
        if (request.getParameter("sel_categoria") != null) {
            String sel_categoria = request.getParameter("sel_categoria");
            request.setAttribute("sel_categoria", sel_categoria);
            request.setAttribute("titulos", af.listarTitulosPorCategoria(sel_categoria));
        }
        request.getServletContext().getRequestDispatcher("/enviar_alerta.jsp").forward(request, response);
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
        
        Usuario u = (Usuario) sessao.getAttribute("usuario");
        String[] enviarPara = request.getParameterValues("enviarPara");
        Alerta a = af.procurarPorTitulo(request.getParameter("sel_titulo"), u);
        if (a == null) System.out.println(request.getParameter("sel_titulo"));
        EnvioAlerta ea = new EnvioAlerta(a);
        ea.setDataHoraEnvio(LocalDateTime.now());
        if (eaf.inserir(ea)) System.out.println("chegou aki tbm");
        env = new Enviador();
        
        for (String id: enviarPara) {
            //Recupera um contato
            Contato c = cf.procurarPorId(Long.parseLong(id));
            System.out.println("chegou aki tbm²");
            //Registro de contatos que receberam este alerta 
            EnvioAlertaContato eac = new EnvioAlertaContato(c, ea);
            eacf.inserir(eac);
            
            //Cria uma página por contato
            pag = new Pagina(a, u, c);
            
            //De quem?
            env.setNomeFrom(u.getNome());
            env.setEmailFrom(u.getEmail());
            
            //Para quem?
            env.setNomeTo(c.getNome());
            env.setEmailTo(c.getEmail());
            //env.setFoneTo(c.getFone());
            
            //O que?
            env.setAssunto(a.getCategoria());
            env.setTitulo(a.getTitulo());
            env.setMensagem(a.getMensagem());
            
            //Link para acessar a página
            env.setUrl(pag.getUrl());
            System.out.println("chegou aki tbm³");
            //Envia o email
            env.enviarEmail();
        }
        
        request.setAttribute("valido", true);
        request.getServletContext().getRequestDispatcher("/enviar_alerta.jsp").forward(request, response);
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