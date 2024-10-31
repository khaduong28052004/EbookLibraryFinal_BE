package com.toel.service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toel.dto.MailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendMailService {
	@Autowired
	JavaMailSender sender;

	List<MimeMessage> queue = new ArrayList<MimeMessage>();

	public void push(String to, String subject, String content) {
		MailSender mail = new MailSender(to, subject, content);
		this.push(mail);
	}

	public void push(MailSender mail) {
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
				// TODO: handle exception
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		queue.add(mime);
	}

	// LocalDateTime localDateTime = LocalDateTime.now();
	// List<Bill> bills = billRepositoty.findAll();
	// for (Bill bill : bills) {
	// for (BillDetail billDetail : bill.getBillDetails()) {
	// LocalDateTime localDateTimeOld =
	// billDetail.getDateBuy().toInstant().atZone(ZoneId.systemDefault())
	// .toLocalDateTime();
	// Duration duration = Duration.between(localDateTimeOld, localDateTime);
	// if (duration.toHours() > 2) {
	// if (billDetail.isStatus() && billDetail.isActive() == false) {
	// MailSender mailSender = new MailSender();
	// mailSender.setToEmail(billDetail.getProduct().getAccount().getEmail());
	// mailSender.setSubject("");
	// mailSender.setContent("<!DOCTYPE html>\r\n" + "<html lang=\"vi\">\r\n" +
	// "<head>\r\n"
	// + " <meta charset=\"UTF-8\">\r\n"
	// + " <meta name=\"viewport\" content=\"width=device-width,
	// initial-scale=1.0\">\r\n"
	// + " <title>Order Processing Notification</title>\r\n" + " <style>\r\n"
	// + " body {\r\n" + " font-family: Arial, sans-serif;\r\n"
	// + " background-color: #f4f4f4;\r\n" + " margin: 0;\r\n"
	// + " padding: 0;\r\n" + " }\r\n" + " .container {\r\n"
	// + " width: 100%;\r\n" + " padding: 20px;\r\n"
	// + " background-color: #ffffff;\r\n"
	// + " box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n"
	// + " max-width: 600px;\r\n" + " margin: 20px auto;\r\n"
	// + " }\r\n" + " .header {\r\n" + " text-align: center;\r\n"
	// + " padding: 10px 0;\r\n" + " background-color: #4CAF50;\r\n"
	// + " color: white;\r\n" + " }\r\n" + " .header h1 {\r\n"
	// + " margin: 0;\r\n" + " font-size: 24px;\r\n" + " }\r\n"
	// + " .content {\r\n" + " padding: 20px;\r\n" + " }\r\n"
	// + " .content h2 {\r\n" + " color: #333;\r\n" + " }\r\n"
	// + " .content p {\r\n" + " font-size: 16px;\r\n"
	// + " line-height: 1.6;\r\n" + " color: #555;\r\n" + " }\r\n"
	// + " .content strong {\r\n" + " color: #000;\r\n" + " }\r\n"
	// + " .footer {\r\n" + " text-align: center;\r\n"
	// + " padding: 10px 0;\r\n" + " background-color: #f4f4f4;\r\n"
	// + " color: #777;\r\n" + " font-size: 14px;\r\n" + " }\r\n"
	// + " </style>\r\n" + "</head>\r\n" + "<body>\r\n"
	// + " <div class=\"container\">\r\n" + " <div class=\"header\">\r\n"
	// + " <h1>Thông báo từ EbookLibrary</h1>\r\n" + " </div>\r\n"
	// + " <div class=\"content\">\r\n"
	// + " <h2>Kính gửi Quý Người bán,</h2>\r\n"
	// + " <p>Qúy khách có đơn hàng được đặt hơn <strong>+" + duration.toHours()
	// + "+</strong> giờ nhưng hiện tại vẫn chưa được duyệt.</p>\r\n"
	// + " <p>Để đảm bảo thời gian giao hàng và nâng cao trải nghiệm khách hàng, vui
	// lòng xử lý đơn hàng này trong vòng <strong>2 giờ tới, trong vòng 2 giờ tới
	// đơn hàng của quý khách vẫn chưa duyệt chúng tôi sẽ duyệt tự động để đảm bảo
	// trải nghiệm của khách hàng.</strong>.</p>\r\n"
	// + " <p>Chúng tôi rất mong nhận được sự hợp tác từ Quý khách.</p>\r\n"
	// + " <p>Trân trọng,<br>Đội ngũ Hỗ trợ của EbookLibrary</p>\r\n"
	// + " </div>\r\n" + " <div class=\"footer\">\r\n"
	// + " <p>EbookLibrary, Cần Thơ, Việt Nam</p>\r\n"
	// + " <p>Đây là email tự động, vui lòng không trả lời email này.</p>\r\n"
	// + " </div>\r\n" + " </div>\r\n" + "</body>\r\n" + "</html>\r\n" + "");
	// push(mailSender);
	// }
	// }
	// }
	// }

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
		// System.err.println("chạy");
		// System.out.println("success : " + success + "/n error : " + error);

		// List<BillDetail> billDetails = billDetailRepository.findAll();
		// for (BillDetail billDetail : billDetails) {
		// 	LocalDateTime localDateTime = LocalDateTime.now();
		// 	LocalDateTime localDateTimeOld = billDetail.getDateBuy().toInstant().atZone(ZoneId.systemDefault())
		// 			.toLocalDateTime();
		// 	Duration duration = Duration.between(localDateTimeOld, localDateTime);
		// 	if (duration.toMillis() > 2) {
		// 		if (!billDetail.isStatus() && billDetail.isActive() == false) {
		// 			billDetail.setStatus(true);
		// 			billDetailRepository.save(billDetail);
		// 		}
		// 	}

		// }

	}

}
