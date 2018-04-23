package com.github.fontys.services;

import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

@Stateless
public class EmailService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private Logger logger;

    @Resource(name="mail/kwetter")
    private Session session;

    public String generateVerificationLink(User user){
        Random random = new SecureRandom();
        random.setSeed(user.getId() + new Date().getTime());
        String token = new BigInteger(130, random).toString(32);
        if(userDAO.verificationLinkExists(token)){
            return generateVerificationLink(user);
        }else{
            return token;
        }
    }

    public void sendVerificationMail(Account acc) throws MessagingException {
        Message message = new MimeMessage(session);

        logger.info("Set subject");
        message.setSubject("Verify your user for RekeningRijden");

        logger.info("Set from");
        message.setFrom();

        logger.info("Set body");
        BodyPart messageGreeting = new MimeBodyPart();
        messageGreeting.setText("Hello " + acc.getUser().getName() + ",");

        BodyPart messageBody = new MimeBodyPart();
        messageBody.setText(System.lineSeparator()
                + "Please use this verification link to verify your account:"
                + System.lineSeparator()
                + "http://localhost:8080/Rekeningrijden/api/users/verify/" + acc.getUser().getVerifyLink());

        BodyPart signatureBody = new MimeBodyPart();
        signatureBody.setText(System.lineSeparator()
                + "Rekeningrijden Duitsland");

        logger.info("Set multipart");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageGreeting, 0);
        multipart.addBodyPart(messageBody, 1);
        multipart.addBodyPart(signatureBody, 2);

        logger.info("Set content message");
        message.setContent(multipart);

        logger.info("Set header mail");
        message.setHeader("X-mailer", "Java Mail API");

        logger.info("Set send date");
        message.setSentDate(new Date());

        logger.info("Set recipient");
        message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(acc.getEmail(), false)[0]);

        logger.info("Send mail");
        Transport.send(message);
    }
}
