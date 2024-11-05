package com.toel.service.Email;

public class TemplateBuilder {
    public static String buildContent(EmailTemplateType type, String... dynamicData) {
        switch (type) {
            case OTP:
                return buildOtpTemplate(dynamicData); // Pass the OTP code as dynamic data
            case PASSWORD_RESET:
                return buildPasswordResetTemplate(dynamicData);
            case WELCOME:
                return buildWelcomeTemplate(dynamicData);
            default:
                throw new IllegalArgumentException("Invalid template type");
        }
    }

    private static String buildOtpTemplate(String... data) {
        String otp = data[0];
        String username = data.length > 1 ? data[1] : "User"; // Optional data handling
        // HTML for OTP email template
        return "<!DOCTYPE html>\r\n" +
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
                " <div class=\"otp-box\">Mã OTP của bạn là: [" + otp
                + "] >>><a href='https://www.w3schools.com'>Đổi mật khẩu ngay!</a></div>\r\n" +
                " <p>Xin vui lòng nhập mã này vào trang đặt lại mật khẩu để tiếp tục quá trình đặt lại mật khẩu.</p>\r\n"
                +
                "<a href='https://www.w3schools.com'>Đổi mật khẩu ngay!</a>"
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
    }

    private static String buildPasswordResetTemplate(String... data) {
        // HTML for Password Reset template with placeholder for the reset link
        return "<html>" +
                "<body>" +
                "<div style=\"max-width:600px;margin:20px auto;padding:20px;background-color:#f4f4f9;border-radius:8px;\">"
                +
                "<h2>Password Reset Request</h2>" +
                "<p>To reset your password, please click the link below:</p>" +
                "<a href=\"" + data[0] + "\" style=\"color:#007bff;text-decoration:none;\">Reset Password</a>" +
                "<p>If you did not request a password reset, please ignore this email.</p>" +
                "<p>Thank you,<br>Your Support Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private static String buildWelcomeTemplate(String... data) {
        // HTML for Welcome email template with a placeholder for a personalized message
        return "<html>" +
                "<body>" +
                "<div style=\"max-width:600px;margin:20px auto;padding:20px;background-color:#f4f4f9;border-radius:8px;\">"
                +
                "<h2>Welcome!</h2>" +
                "<p>" + data[0] + "</p>" +
                "<p>Thank you for joining us! We’re excited to have you on board.</p>" +
                "<p>Best Regards,<br>Your Support Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}