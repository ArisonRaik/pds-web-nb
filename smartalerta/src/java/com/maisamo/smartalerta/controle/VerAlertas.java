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

import com.maisamo.smartalerta.modelo.fachada.AlertaFacede;
import com.maisamo.smartalerta.modelo.entidade.Alerta;
import com.maisamo.smartalerta.modelo.entidade.Usuario;

/**
 *
 * @author wagner
 */
@WebServlet(name = "VerAlertas", urlPatterns = {"/VerAlertas"})
public class VerAlertas extends HttpServlet {

    private HttpSession sessao = null;
    private final AlertaFacede af = new AlertaFacede();

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
            out.println("<title>Servlet VerAlertas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VerAlertas at " + request.getContextPath() + "</h1>");
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
        
        Usuario usuario = (Usuario) sessao.getAttribute("usuario");
        sessao.setAttribute("alertas", af.listar(usuario));
        
        String aid = request.getParameter("aid");
        if (aid != null) {
            Alerta alerta = af.procurarPorId(Long.parseLong(aid));
            if (request.getParameter("edt") != null) {
                request.setAttribute("editar_alerta", alerta);
                request.setAttribute("editar", true); // mostrar modal de edição
            } else {
                sessao.setAttribute("excluir_alerta", alerta);
                request.setAttribute("excluir", true); // mostrar modal de exclusão
            }
        } else if (request.getParameter("del") != null) {
            Alerta excluir_alerta = (Alerta) sessao.getAttribute("excluir_alerta");
            af.excluir(excluir_alerta);
            request.setAttribute("excluido", true); // mostrar modal de excluído
        }
        request.getServletContext().getRequestDispatcher("/visualizar_alertas.jsp").forward(request, response);
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
        
        Long id = Long.parseLong(request.getParameter("alertaId"));
        String categoria = request.getParameter("categoria");
        String titulo = request.getParameter("titulo");
        String mensagem = request.getParameter("mensagem");
        
        Alerta editar_alerta = new Alerta();
        editar_alerta.setId(id);
        editar_alerta.setCategoria(categoria);
        editar_alerta.setTitulo(titulo);
        editar_alerta.setMensagem(mensagem);
        
        af.atualizar(editar_alerta);
        
        request.setAttribute("atualizado", true); // mostrar modal de atualizado/editado
        request.getServletContext().getRequestDispatcher("/visualizar_alertas.jsp").forward(request, response);
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
