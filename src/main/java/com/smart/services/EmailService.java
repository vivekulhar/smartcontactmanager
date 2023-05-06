package com.smart.services;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;





@Service
public class EmailService {

	public boolean sendEmail(String subject, String message, String to)
    {
        //rest of the code

        boolean f = false;

        String from = "learnbackendvivek@gmail.com";


        // Variable for gmail

        String host = "smtp.gmail.com";

        //get the system properties

        Properties properties = System.getProperties();
        System.out.println("PROPERTIES"+properties);

        //setting important information to properties object


        //host set--key value pair, mail.smtp.host is key that API will use
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        //Step 1: to get the session object..
        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //I have created an App Password here
                return new PasswordAuthentication("learnbackendvivek@gmail.com", "dpfjbtsfynqlvxgt");
            }
        });

        session.setDebug(true);

        //Step 2: Compose the message [text,multi-media]

        MimeMessage m = new MimeMessage(session);

        try {
            //from email
            m.setFrom(from);

            //adding recipient to message
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            //adding subject to message
            m.setSubject(subject);

            //adding text to message
            m.setText(message);

            //send

//			Step 3: send the message using Transport class
            Transport.send(m);

            System.out.println("Send success.......");

            f=true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}
