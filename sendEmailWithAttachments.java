import java.util.Properties;
import java.util.Date;

import java.io.File;

import javax.mail.Authenticator ;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.BodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage ;

import javax.mail.* ;

public class sendEmailWithAttachments
{
    private String sender_email ;
    private String sender_password ;

    private String receiver_address ;
    private String mail_subject ;
    private String messageText ;

    private File[] files ;
    
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

    public void addFiles(File files[])
    {
        this.files = files ;
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

            Multipart multipart = new MimeMultipart() ;
            
            BodyPart msgBodyPart = new MimeBodyPart() ;
            msgBodyPart.setText(messageText);
   
            for(int i=0;i<files.length;i++)
            {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart() ; 
                attachmentBodyPart.attachFile(files[i]) ;
                multipart.addBodyPart(attachmentBodyPart);
            }                

            multipart.addBodyPart(msgBodyPart);
            
            msg.setContent(multipart);
            

            Transport.send(msg, sender_email, sender_password);

            System.out.println("Sent successfully to "+receiver_address) ;

        }catch (Exception ex) {
        System.out.println("Exception encountered : \n\n" + ex);
        } 
    }
}

class run2
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

        /*      Be careful while writing file paths .It is always a little tricky in java as compared to other languages . 
         *      A quick guide : 
         *          1. Add as many files you desire in the "files" variable (as long as the memory required is too much)
         *          2. Write complete paths . (../ doesn't work in java)
         *          3. use double '\' (not "/") 
         * 
         *      example path : "C:\\Users\\Binit\\Desktop\\mail-java\\temp.java"
         * 
         *      Follow them and don't fall in troube related to paths .
         */

        File files[] = {new File("C:\\Users\\Binit\\Desktop\\mail-java\\sendEmail.java"),
                        new File("C:\\Users\\Binit\\Desktop\\mail-java\\temp.java"),
                        new File("C:\\Users\\Binit\\Desktop\\mail-java\\sendEmailWithAttachments.java")
                        
        } ;

        sendEmailWithAttachments obj = new sendEmailWithAttachments() ;
        obj.createSender(senderGmailID, pass);
        obj.addReceiver(receiverEmail);
        obj.createMessage(subject,message);
        obj.addFiles(files);
        obj.send();
    }
}