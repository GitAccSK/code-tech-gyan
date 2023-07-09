import java.util.Properties;
import java.util.Date;

import javax.mail.Authenticator ;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage ;

public class sendEmail 
{
    private String sender_email ;
    private String sender_password ;

    private String receiver_address ;
    private String mail_subject ;
    private String messageText ;

    public void createSender(String sender_email,String sender_password)
    {
        this.sender_email = sender_email ;
        this.sender_password = sender_password ;
    }

    public void addReceiver(String receiver_address)
    {
        this.receiver_address = receiver_address ;
    }

    public void createMessage(String mail_subject,String messageText)
    {
        this.mail_subject = mail_subject ;
        this.messageText = messageText ;
    }
    public void send()
    {
        String smtpHost = "smtp.gmail.com" ;
        int smtpPort = 587 ;
        // 465 for SSL and 587 for TLS

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", true);  //enables authentication
        props.put("mail.smtp.starttls.enable", true); //enables TLS/SSL

        //Creating javax.mail.authentication object
        Authenticator auth = new javax.mail.Authenticator(){
            protected PasswordAuthentication getAuthentication()
            {
                return new PasswordAuthentication(sender_email, sender_password) ;
            }
        } ;

        Session session = Session.getInstance(props, auth);
        
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(sender_email);
            msg.setRecipients(Message.RecipientType.TO, receiver_address);
            msg.setSubject(mail_subject);
            msg.setSentDate(new Date());
            msg.setText(messageText);
            
            Transport.send(msg, sender_email, sender_password);

            System.out.println("Sent successfully to "+receiver_address) ;

        }catch (Exception ex) {
        System.out.println("Exception encountered : \n\n" + ex);
        } 
    }
}

class run
{
    public static void main(String[] args) 
    {
        String message = "Hey there !\n It's Binit" ;
        String subject = "Testing Gmail SMTP" ;

        String senderGmailID = "YOUR_EMAIL_HERE" ;
        String pass = "YOUR_GMAIL_APP_PASSWORD_HERE" ;
        // Enable 2FA on your gmail and use the APP PASSWORD here
        // if using regular password , AuthenticationFailedException may occur

        String receiverEmail = "RECEIVER_EMAIL_HERE" ;

        sendEmail obj = new sendEmail() ;
        obj.createSender(senderGmailID, pass);
        obj.addReceiver(receiverEmail);
        obj.createMessage(subject,message);
        obj.send();
    }
}