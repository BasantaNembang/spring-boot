package com.basanta.OrderService.mail;

import com.basanta.OrderService.event.OrderPlacedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class SendMail {

    private final JavaMailSender sender;

    public SendMail(JavaMailSender sender) {
        this.sender = sender;
    }

    @KafkaListener(topics = "order-place")
    public void listen(OrderPlacedEvent event){

        MimeMessagePreparator preparator = mimeMessage -> {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("nembangbasanta66@email.com");
            helper.setTo(event.getUser().toString());
            helper.setSubject(event.getSubject().toString());
            helper.setText("Your Order  ID  :"+ event.getOrderNumber().toString());
        };
        try{
            sender.send(preparator);
            System.out.println("Mail has been send  "+event);
        } catch (MailException e) {
            System.out.println("Error  is here");
            e.printStackTrace();
            //throw new RuntimeException(e);
        }

    }


}
