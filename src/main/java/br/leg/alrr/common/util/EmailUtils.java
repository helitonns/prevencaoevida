package br.leg.alrr.common.util;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class EmailUtils {
	
	 private static final String HOSTNAME = "smtp.interlegis.leg.br";
	 private static final String USERNAME = "gdsis@al.rr.leg.br";
	 private static final String PASSWORD = "OOkbty19";
	 private static final String EMAILORIGEM = "gdsis@al.rr.leg.br";
	 
	 public static Email conectaEmail() throws EmailException {
		 Email email = new SimpleEmail();
		 email.setHostName(HOSTNAME);
		 email.setSmtpPort(587);
		 email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
		 email.setSSL(false);
		 email.setTLS(true);
		 email.setFrom(EMAILORIGEM);
		 email.getMailSession().getProperties().put("mail.smtp.auth", "true");
	     email.getMailSession().getProperties().put("mail.debug", "true");
	     email.getMailSession().getProperties().put("mail.smtp.port", "587");
	     email.getMailSession().getProperties().put("mail.smtp.socketFactory.port", "587");
	     //email.getMailSession().getProperties().put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	     email.getMailSession().getProperties().put("mail.smtp.socketFactory.fallback", "false");
	     email.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");
		 return email;
	 }
	 
	 public static void enviaEmail(Mensagem mensagem) throws EmailException {
		 Email email = new SimpleEmail();
		 email = conectaEmail();
		 email.setSubject(mensagem.getTitulo());
		 email.setMsg(mensagem.getMensagem());
		 email.addTo(mensagem.getDestino());
		 String resposta = email.send();
	 }

}
