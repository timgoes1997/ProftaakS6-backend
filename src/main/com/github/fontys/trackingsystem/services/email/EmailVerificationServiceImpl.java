package com.github.fontys.trackingsystem.services.email;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.email.interfaces.EmailVerificationService;
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
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@Stateless
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private Logger logger;

    @Resource(name="mail/kwetter")
    private Session session;

    @Override
    public String generateVerificationLink(User user){
        String token = UUID.randomUUID().toString();
        if(userDAO.verificationLinkExists(token)){
            return generateVerificationLink(user);
        }else{
            return token;
        }
    }

    @Override
    public void sendVerificationMail(Account acc) {
        try {
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
                    + "http://192.168.24.110/verify.html?token=" + acc.getUser().getVerifyLink());

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
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }
}
