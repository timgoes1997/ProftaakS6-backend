package com.github.fontys.trackingsystem.services.email;

import com.github.fontys.trackingsystem.services.email.interfaces.EmailTradeService;
import com.github.fontys.trackingsystem.transfer.Transfer;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.NotFoundException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class EmailTradeServiceImpl implements EmailTradeService{

    @Inject
    private Logger logger;

    @Resource(name = "mail/kwetter")
    private Session session;

    @Override
    public void sendTransferMail(Transfer transfer, String email) {
        try {
            Message message = new MimeMessage(session);

            message.setSubject("Accept car transfer");

            message.setFrom();

            BodyPart messageGreeting = new MimeBodyPart();
            messageGreeting.setText("Hello " + email + ",");

            BodyPart messageBody = new MimeBodyPart();
            messageBody.setText(System.lineSeparator()
                    + "Someone has requested a car transfer to give you it's proof of ownership."
                    + System.lineSeparator()
                    + "If that was you please click on the following link:"
                    + System.lineSeparator()
                    + "http://" + InetAddress.getLocalHost().getHostAddress() + "/trade.html?token=" + transfer.getTransferToken());

            BodyPart signatureBody = new MimeBodyPart();
            signatureBody.setText(System.lineSeparator()
                    + "Rekeningrijden Duitsland");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageGreeting, 0);
            multipart.addBodyPart(messageBody, 1);
            multipart.addBodyPart(signatureBody, 2);

            message.setContent(multipart);
            message.setHeader("X-mailer", "Java Mail API");
            message.setSentDate(new Date());
            message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(email, false)[0]);
            Transport.send(message);
        }catch (Exception e){
            logger.warning(e.getMessage());
            throw new NotFoundException("Couldn't send given email");
        }
    }

    @Override
    public void sendStatusUpdate(Transfer transfer){
        if(transfer.getCurrentOwner() == null || transfer.getOwnerToTransferTo() == null){
            throw new NotFoundException("No owners found");
        }

        try {
            Message message = new MimeMessage(session);

            message.setSubject("Car transfer status update");

            message.setFrom();

            BodyPart messageGreeting = new MimeBodyPart();
            messageGreeting.setText("Hello transferers,");

            BodyPart messageBody = new MimeBodyPart();
            messageBody.setText(System.lineSeparator()
                    + "Someone has updated the status of your transfer to:"
                    + System.lineSeparator()
                    + transfer.getStatus().toString());

            BodyPart transferBody = new MimeBodyPart();

            transferBody.setText(System.lineSeparator()
                    + "To view the update go to your transfer tab in the RekeningRijden app or click on the following link:"
                    + System.lineSeparator()
                    + "http://" + Localhost.getLocalHostLANAddress().getHostAddress() + "/trade.html?token=" + transfer.getTransferToken());

            BodyPart signatureBody = new MimeBodyPart();
            signatureBody.setText(System.lineSeparator()
                    + "Rekeningrijden Duitsland");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageGreeting, 0);
            multipart.addBodyPart(messageBody, 1);
            multipart.addBodyPart(signatureBody, 2);

            message.setContent(multipart);
            message.setHeader("X-mailer", "Java Mail API");
            message.setSentDate(new Date());

            List<InternetAddress> addressList = new ArrayList<>();
            message.addRecipient(Message.RecipientType.TO, InternetAddress.parse(transfer.getOwnerToTransferTo().getAccount().getEmail(), false)[0]);
            message.addRecipient(Message.RecipientType.TO, InternetAddress.parse(transfer.getCurrentOwner().getAccount().getEmail(), false)[0]);
            Transport.send(message);
        }catch (Exception e){
            logger.warning(e.getMessage());
            throw new NotFoundException("Couldn't send given email");
        }
    }
}
