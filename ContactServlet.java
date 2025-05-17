import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form data
        String name = request.getParameter("name");
        String university = request.getParameter("university");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        
        try {
            // Email configuration
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            
            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("your-email@gmail.com", "your-app-password");
                }
            });
            
            // Create message
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress("your-email@gmail.com"));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("contact@hybeliving.com"));
            emailMessage.setSubject("New Contact Form Submission from " + name);
            
            // Create email content
            String emailContent = String.format(
                "Name: %s\n" +
                "University: %s\n" +
                "Email: %s\n" +
                "Message:\n%s",
                name, university, email, message
            );
            
            emailMessage.setText(emailContent);
            
            // Send message
            Transport.send(emailMessage);
            
            // Redirect to success page
            response.sendRedirect("thank-you.html");
            
        } catch (MessagingException e) {
            // Log the error and redirect to error page
            e.printStackTrace();
            response.sendRedirect("error.html");
        }
    }
} 