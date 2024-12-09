package com.toel.service.Email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.toel.dto.MailSenderDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    // @Autowired
    // JavaMailSender mailSender;
    @Autowired
    JavaMailSender sender;

    // public void sendSimpleEmail(String toEmail, String subject, String body) {
    // SimpleMailMessage message = new SimpleMailMessage();
    // message.setFrom("khaduong28052004@gmail.com");
    // message.setTo(toEmail);
    // message.setSubject(subject);
    // message.setText(body);
    // mailSender.send(message);
    // System.out.println("Email sent successfully!");
    // }

    List<MimeMessage> queue = new ArrayList<>();

    public void push(String to, String subject, String content) {
        MailSenderDTO mail = new MailSenderDTO(to, subject, from(content));
        this.push(mail);
    }

    public void pushList(String subject, List<String> listmail, EmailTemplateType templateType,
            Map<String, String> emailToNameMap, String... staticData) {
        List<MailSenderDTO> personalizedEmails = listmail.stream().map(email -> {
            String name = emailToNameMap.getOrDefault(email, "Người dùng"); // Lấy tên, mặc định là "Người dùng"

            // Kết hợp dữ liệu động cho email cụ thể
            String[] dynamicData = new String[staticData.length + 1];
            dynamicData[0] = name; // Thêm tên vào dữ liệu động
            System.arraycopy(staticData, 0, dynamicData, 1, staticData.length);

            String content = TemplateBuilder.buildContent(templateType, dynamicData);

            MailSenderDTO mail = new MailSenderDTO();
            mail.setToEmail(email);
            mail.setSubject(subject);
            mail.setContent(content);

            return mail;
        }).toList();

        // Đẩy tất cả email trong một lần gửi
        personalizedEmails.forEach(this::push);
    }

    public void push(String to, String subject, EmailTemplateType templateType, String... dynamicData) {
        String content = TemplateBuilder.buildContent(templateType, dynamicData);
        MailSenderDTO mail = new MailSenderDTO(to, subject, content);
        this.push(mail);
    }

    public void push(MailSenderDTO mail) {
        // JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // mailSender.setHost("smtp.gmail.com");
        // mailSender.setPort(587);
        // mailSender.setUsername(email);
        // mailSender.setPassword(password);

        // Properties props = mailSender.getJavaMailProperties();
        // props.put("mail.transport.protocol", "smtp");
        // props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.starttls.enable", "true");
        // props.put("mail.debug", "true");

        MimeMessage mime = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "utf-8");
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getToEmail());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            try {
                for (String cc : mail.getCc()) {
                    helper.addCc(cc);
                }
                for (String bcc : mail.getBcc()) {
                    helper.addBcc(bcc);
                }
                for (File file : mail.getFiles()) {
                    helper.addAttachment(file.getName(), file);
                }
            } catch (Exception e) {
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        queue.add(mime);
    }

    public String from(String connect) {

        String lp = "<!DOCTYPE html>\r\n" +
                "<html lang=\"vi\">\r\n" +
                "<head>\r\n" +
                " <meta charset=\"UTF-8\">\r\n" +
                " <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" +
                " <title>Xác nhận yêu cầu quên mật khẩu</title>\r\n" +
                " <style>\r\n" +
                " body {\r\n" +
                " font-family: Arial, sans-serif;\r\n" +
                " background-color: #121212;\r\n" +
                " color: #e0e0e0;\r\n" +
                " margin: 0;\r\n" +
                " padding: 0;\r\n" +
                " }\r\n" +
                " .container {\r\n" +
                " max-width: 600px;\r\n" +
                " margin: 20px auto;\r\n" +
                " padding: 20px;\r\n" +
                " background-color: #1e1e1e;\r\n" +
                " border-radius: 8px;\r\n" +
                " box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);\r\n" +
                " }\r\n" +
                " .header {\r\n" +
                " text-align: center;\r\n" +
                " color: #ffffff;\r\n" +
                " padding-bottom: 20px;\r\n" +
                " border-bottom: 1px solid #333333;\r\n" +
                " }\r\n" +
                " .header h1 {\r\n" +
                " font-size: 24px;\r\n" +
                " margin: 0;\r\n" +
                " }\r\n" +
                " .content {\r\n" +
                " padding: 20px 0;\r\n" +
                " }\r\n" +
                " .content p {\r\n" +
                " font-size: 16px;\r\n" +
                " line-height: 1.6;\r\n" +
                " color: #e0e0e0;\r\n" +
                " }\r\n" +
                " .otp-box {\r\n" +
                " font-size: 20px;\r\n" +
                " font-weight: bold;\r\n" +
                " background-color: #333333;\r\n" +
                " color: #ffffff;\r\n" +
                " padding: 10px;\r\n" +
                " text-align: center;\r\n" +
                " border-radius: 4px;\r\n" +
                " margin: 10px 0;\r\n" +
                " }\r\n" +
                " .footer {\r\n" +
                " text-align: center;\r\n" +
                " font-size: 14px;\r\n" +
                " color: #777777;\r\n" +
                " padding-top: 20px;\r\n" +
                " border-top: 1px solid #333333;\r\n" +
                " }\r\n" +
                " .footer p {\r\n" +
                " margin: 5px 0;\r\n" +
                " }\r\n" +
                " </style>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                " <div class=\"container\">\r\n" +
                " <div class=\"header\">\r\n" +
                " <h1>Xác nhận yêu cầu quên mật khẩu</h1>\r\n" +
                " </div>\r\n" +
                " <div class=\"content\">\r\n" +
                " <p>Xin chào,</p>\r\n" +
                " <p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản của mình. Dưới đây là mã OTP để xác nhận quá trình này:</p>\r\n"
                +
                " <div class=\"otp-box\">Mã OTP của bạn là: [" + connect
                + "] <a href=\"https://www.w3schools.com/\">Đổi mật khẩu ngay!</a></div>\r\n" +
                " <p>Xin vui lòng nhập mã này vào trang đặt lại mật khẩu để tiếp tục quá trình đặt lại mật khẩu.</p>\r\n"
                +
                " <p>Lưu ý rằng mã OTP sẽ hết hạn sau 1 phút kể từ lúc nhận được mail.</p>\r\n" +
                " <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>\r\n" +
                " <p>Vui lòng giữ email này riêng tư và không chia sẻ với bất kỳ ai khác. Đây là thông tin quan trọng và được bảo vệ theo chính sách bảo mật của chúng tôi.</p>\r\n"
                +
                " </div>\r\n" +
                " <div class=\"footer\">\r\n" +
                " <p>Trân trọng,</p>\r\n" +
                " <p>Đội ngũ Hỗ trợ</p>\r\n" +
                " <p>Đây là email tự động, vui lòng không trả lời email này.</p>\r\n" +
                " </div>\r\n" +
                " </div>\r\n" +
                "</body>\r\n" +
                "</html>";
        return lp;
    }

    @Scheduled(fixedDelay = 100)
    public void run() {
        int error = 0;
        int success = 0;
        try {
            while (!queue.isEmpty()) {
                MimeMessage mime = queue.remove(0);
                sender.send(mime);
                success++;
            }
        } catch (Exception e) {
            error++;
        }
    }
}
