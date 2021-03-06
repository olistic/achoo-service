package uy.achoo.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class EmailService {
    private static final String SMTP_HOST = "box.achoo.uy";
    private static final String SMTP_USER = "no-reply@achoo.uy";
    private static final String SMTP_PASSWORD = "mamadera";


    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private static final Logger logger = Logger.getLogger(EmailService.class
            .getName());

    public static void sendRegistrationMail(String email, String firstName)
            throws MessagingException {

        String subject = "Welcome " + firstName + " !";

        StringBuilder sb = new StringBuilder();
        sb.append("<p> Hello, ")
                .append(firstName)
                .append(":</p><p>You have a new account in <a href=\"http://localhost:8080/achoo-frontend/index.html\"> Achoo!</a>")
                .append("<br></br>");
        String body = sb.toString();
        sendMail(email, subject, body);

    }


    public static void sendResetPasswordMail(String email, String newPassword)
            throws MessagingException {

        String subject = "Password Reset";
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Greetings from <a href=\"http://localhost:8080/achoo-frontend/index.html\"> Achoo!</a><br/>")
                .append("<p> You have reset your password<p/>")
                .append("<p>Your username is : <b>")
                .append(email)
                .append("</b>")
                .append(" and your new password is : <b>")
                .append(newPassword)
                .append("</b></p>")
                .append("<br></br>");
        String body = sb.toString();
        sendMail(email, subject, body);
    }

    public static void sendSuccessfulOrderMail(String email) throws MessagingException {
        String subject = "Order Successful!";
        StringBuilder sb = new StringBuilder();
        sb.append("Congratulations!<br/> Your order was successfully submited, just wait a couple of minutes and your drugs will be delivered.");
        String body = sb.toString();
        sendMail(email, subject, body);
    }

    private static void sendMail(String email, String subject, String body) throws MessagingException {
        new Thread(() -> {
            if (validateEmail(email)) {
                try {
                    // Authentication properties
                    Properties props = System.getProperties();
                    props.put("mail.smtp.starttls.enable", true);
                    props.put("mail.smtp.host", SMTP_HOST);
                    props.put("mail.smtp.user", SMTP_USER);
                    props.put("mail.smtp.password", SMTP_PASSWORD);
                    props.put("mail.smtp.ssl.trust", SMTP_HOST);
                    props.put("mail.smtp.port", "25");
                    props.put("mail.smtp.auth", true);

                    System.out.println("Enviando email");

                    logger.log(Level.INFO, "Authenticating with SMTP Server");
                    Session session = Session.getInstance(props, null);
                    MimeMessage message = new MimeMessage(session);
                    logger.log(Level.INFO, "Crating email");

                    InternetAddress from = new InternetAddress(SMTP_USER);
                    message.setSubject(subject);
                    message.setFrom(from);
                    message.addRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(email));
                    message.setContent(body, "text/html");
                    // Send message
                    logger.log(Level.INFO, "Sending email ...");
                    Transport transport = session.getTransport("smtp");
                    transport.connect(SMTP_HOST, SMTP_USER,
                            SMTP_PASSWORD);
                    transport.sendMessage(message, message.getAllRecipients());
                    logger.log(Level.INFO, "Email send!");
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } else {
                logger.log(Level.WARN, "Invalid email!");
            }
        }).start();
    }


    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
