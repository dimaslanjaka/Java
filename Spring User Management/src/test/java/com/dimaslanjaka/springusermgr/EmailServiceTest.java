package com.dimaslanjaka.springusermgr;

import com.dimaslanjaka.springusermgr.mailer.EmailService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@AutoConfigureMockMvc
@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    private GreenMail testSmtp;

    @Test
    void serviceNotNull() {
        Assertions.assertThat(emailService).isNotNull();
    }

    @Test
    public void testEmail() throws MessagingException, IOException {
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();
        emailService.sendEmail("sample@webmanajemen.com", "Test Subject", "Test mail sample@webmanajemen.com");

        testSmtp.waitForIncomingEmail(1);

        Message[] messages = testSmtp.getReceivedMessages();
        Assertions.assertThat(messages.length).isEqualTo(1);

        String message = messages[0].getSubject();

        Multipart part = (Multipart) messages[0].getContent();

        Assertions.assertThat(part.getCount()).isEqualTo(1);

        Assertions.assertThat(message).isEqualTo("Test Subject");
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        Assertions.assertThat(body.contains("Test mail")).isTrue();

        testSmtp.stop();
    }

    @Test
    public void testEmailWithFile() throws MessagingException, IOException {
        testSmtp = new GreenMail(ServerSetupTest.SMTP);
        testSmtp.start();


        File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("static/TestFile.txt")).getFile());
        emailService.sendEmail("sample@webmanajemen.com", "Test Subject", "Test mail with attachment sample@webmanajemen.com", file);

        testSmtp.waitForIncomingEmail(1);

        Message[] messages = testSmtp.getReceivedMessages();
        Assertions.assertThat(messages.length).isEqualTo(1);

        String message = messages[0].getSubject();
        Assertions.assertThat(message).isEqualTo("Test Subject");
        String body = GreenMailUtil.getBody(messages[0]).replaceAll("=\r?\n", "");
        Assertions.assertThat(body.contains("Test mail")).isTrue();

        Multipart part = (Multipart) messages[0].getContent();

        Assertions.assertThat(part.getCount()).isEqualTo(2);

        BodyPart bodyPart = part.getBodyPart(1);
        Assertions.assertThat(bodyPart.getFileName()).isEqualTo("TestFile.txt");

        Assertions.assertThat(bodyPart.getContent().toString()).isEqualTo("Testing file to be sent as email attachment");

        testSmtp.stop();

    }

}
