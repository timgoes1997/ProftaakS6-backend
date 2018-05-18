package com.github.fontys.trackingsystem.services.email;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.user.Account;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

public class EmailRecoveryService {

    @Inject
    private AccountDAO accountDAO;

    @Resource(name="mail/kwetter")
    private Session session;

    @Inject
    private Logger logger;

    public String generateRecoveryLink(Account account){
        String token = UUID.randomUUID().toString();
        if(accountDAO.recoveryLinkExists(token)){
            return generateRecoveryLink(account);
        }else{
            return token;
        }
    }

    public void sendRecoveryMail(Account acc) throws MessagingException {
        Message message = new MimeMessage(session);

        logger.info("Set subject");
        message.setSubject("Password recovery for RekeningRijden");

        logger.info("Set from");
        message.setFrom();

        logger.info("Set body");
        BodyPart messageGreeting = new MimeBodyPart();
        messageGreeting.setText("Hello " + acc.getUser().getName() + ",");

        BodyPart messageBody = new MimeBodyPart();
        messageBody.setText(System.lineSeparator()
                + "You have requested a password recovery, please click on the following link to enter your new password:"
                + System.lineSeparator()
                + "http://localhost:8080/Rekeningrijden/api/users/verify/" + acc.getRecoveryLink());

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