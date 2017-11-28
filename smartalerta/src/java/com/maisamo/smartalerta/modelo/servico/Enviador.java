/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maisamo.smartalerta.modelo.servico;

import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.EmailException;

import com.rosaloves.bitlyj.BitlyException;
import com.rosaloves.bitlyj.Url;
import static com.rosaloves.bitlyj.Bitly.*;

/**
 *
 * @author wagner
 */
public class Enviador {

    private HtmlEmail email;
    private String emailFrom, emailTo, foneTo;
    private String nomeFrom, nomeTo;
    private String titulo, assunto, mensagem;
    private String url;

    public void setNomeFrom(String nomeFrom) {
        this.nomeFrom = nomeFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public void setNomeTo(String nomeTo) {
        this.nomeTo = nomeTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public void setFoneTo(String foneTo) {
        this.foneTo = foneTo;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String getShortenUrl(String stringUrl) {
        try {
            Url url = as("o_61grjdi3ec", "R_3fa7772aeccc4568bcadc58732341560").call(shorten(stringUrl));
            return url.getShortUrl();
        } catch (BitlyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enviarEmail() {
        email = new HtmlEmail();
        try {
            email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
            email.setAuthentication("thiagotierre.lima@gmail.com", "thiago.tierre.de.lima@141090");
            email.addTo(emailTo); // destinatario
            email.setFrom(emailFrom, nomeFrom); // remetente
            email.setSubject(titulo + " - " + assunto); // assunto do e-mail
            
            /* conteudo do e-mail */
            email.setHtmlMsg("<html>"
                    + "     <head>"
                    + "         <style>"
                    + "             h3{ margin-bottom: 25px }"
                    + "             h1{ text-align: center }"
                    + "             p, a{ font-size: 20px }"
                    + "             .body{"
                    + "                 width: 50%;"
                    + "                 margin: 0 25% 2.5% 25%;"
                    + "			border: 1px solid #c3c3c3;"
                    + "			background-color: #F4F7FA;"
                    + "			text-align: justify;"
                    + "			line-height: 25px"
                    + "             }"
                    + "             .header {"
                    + "			margin-bottom: 5px;"
                    + "			padding: 20px"
                    + "             }"
                    + "             hr{ color: 0 }"
                    + "             h1{ text-align: center }"
                    + "             .msgBody{"
                    + "			margin-top: 5px;"
                    + "			text-align: justify;"
                    + "                 border: 1px solid #c3c3c3;"
                    + "			padding: 15px;"
                    + "			margin: 20px"
                    + "             }"
                    + "             b{"
                    + "		        color: #C93756;"
                    + "			font-size: 22px"
                    + "             }"
                    + "         </style>"
                    + "         <meta charset=\"utf-8\"/>"
                    + "     </head>"
                    + "     <body>"
                    + "		<div class=\"body\">"
                    + "			<div class=\"header\">"
                    + "				<h1>Olá, " + nomeTo + ".</h1>"
                    + "				<hr>"
                    + "				<p>"
                    + "					Esta mensagem foi enviada por <b>" + nomeFrom + "</b> para avisá-lo de que você tem algo importante, sobre um(a) <b>" + assunto + "</b>, do(a) qual deve se lembrar."
                    + "				</p>"
                    + "			</div>"
                    + "			<div class=\"msgBody\">"
                    + "				<h1>Título do alerta</h1>"
                    + "				<hr>"
                    +                           mensagem
                    + "				<p>"
                    + "					Para mais detalhes, clique sobre o link a seguir. <a href=\"" + url + "\">http://bit.ly/2j3oS9G</a>"
                    + "				</p>"
                    + "			</div>"
                    + "		</div>"
                    + "     </body>"
                    + "</html>");
            
            /* caso o e-mail não suporte mensagens no formato html */
            email.setTextMsg("Olá, " + nomeTo + ".\n"
                    + "Esta mensagem foi enviada por " + nomeFrom
                    + "para avisá-lo de que você tem algo importante sobre um(a) "
                    + assunto + " da qual deve se lembrar.\n"
                    + mensagem
                    + "\nPara mais detalhes, acesse o link a seguir.\n" + url);
            
            email.setSSLOnConnect(true);
            email.send(); // envia o e-mail
            System.out.println("E-mail enviado!");
        } catch (EmailException e) {
            System.out.println(e);
        }
    }
}
