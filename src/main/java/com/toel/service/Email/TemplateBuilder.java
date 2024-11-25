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
            case PASSWORD_SUSSECC:
                return buildSuccessTemplate(dynamicData);
            case DANGKYV2:
                return buildSuccessTemplate(dynamicData);
            case HUYDON:
                return huyDon(dynamicData);
            case KHOATAIKHOAN:
                return khoaTaiKhoan(dynamicData);
            case MOTAIKHOAN:
                return moTaiKhoan(dynamicData);
            case DUYET:
                return duyet(dynamicData);
            case HUYDUYET:
                return huyDuyet(dynamicData);
            default:
                throw new IllegalArgumentException("Invalid template type");
        }
    }

    private static String DangkyV2Template(String... data) {
        // HTML for success email template
        String fullname = data.length > 0 ? data[0] : "User"; // Optional data handling for username
        String phone = data.length > 0 ? data[1] : "User"; // Optional data handling for username
        String email = data.length > 0 ? data[2] : "User"; // Optional data handling for username
        String username = data.length > 0 ? data[3] : "User"; // Optional data handling for username
        String password = data.length > 0 ? data[4] : "User"; // Optional data handling for username
        String link = data.length > 1 ? data[5] : "http://localhost:8080/api/v2/user/registerv_1"; // Optional data
                                                                                                   // handling
        String otp = data.length > 0 ? data[6] : "";
        String form = "<!DOCTYPE html>\r\n" +
                "<html lang=\"vi\">\r\n" +
                "<head>\r\n" +
                " <meta charset=\"UTF-8\">\r\n" +
                " <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" +
                " <title>Thông báo thay đổi mật khẩu thành công</title>\r\n" +
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
                " <h1>Thay đổi mật khẩu thành công </h1>\r\n" +
                " </div>\r\n" +
                " <div class=\"content\">\r\n" +
                " <p>Xin chào, " + username + "</p>\r\n" +
                " <p>Mật khẩu của bạn đã được thay đổi thành công. Bạn có thể đăng nhập lại và sử dụng tài khoản của mình bình thường.</p>\r\n"
                +

                "<form action=\'" + link + "' method=\"POST\">\r\n" + //
                "    <!-- OTP -->\r\n" + //
                // " <input type=\"hidden\" name=\"otp\" value='"+otp+"' />\r\n" + //
                "    <!-- Dữ liệu cứng -->\r\n" + //
                "    <input type=\"text\" name=\"email\" value='" + email + "' />\r\n" + //
                "    <input type=\"text\" name=\"phone\" value='" + phone + "' />\r\n" + //
                "    <input type=\"text\" name=\"fullname\" value='" + fullname + "'\" />\r\n" + //
                "    <input type=\"text\" name=\"username\" value='" + username + "' />\r\n" + //
                "    <input type=\"password\" name=\"password\" value='" + password + "' />\r\n" + //
                "    <!-- Nút xác nhận -->\r\n" + //
                "    <button type=\"submit\" style=\"background-color: #4CAF50; color: white;\">\r\n" + //
                "        Xác nhận đăng ký\r\n" + //
                "    </button>\r\n" + //
                "</form>\r\n" + //
                ""
                +
                " <p>Vui lòng đảm bảo bảo mật tài khoản của bạn. Nếu bạn không thực hiện yêu cầu này, hãy liên hệ với chúng tôi ngay lập tức.</p>\r\n"
                +
                " </div>\r\n" +
                " <div class=\"footer\">\r\n" +
                " <p>Trân trọng,</p>\r\n" +
                " <p>Đội ngũ Hỗ trợ</p>\r\n" +
                " <p>Đây là email tự động, vui lòng không trả lời email này.</p>\r\n" +
                "<a href='" + link + "'>Đổi mật khẩu ngay!</a>" +
                " </div>\r\n" +
                " </div>\r\n" +
                "</body>\r\n" +
                "</html>";
        return form;
    }

    private static String buildSuccessTemplate(String... data) {
        String username = data.length > 0 ? data[0] : "User"; // Optional data handling for username
        String link = data.length > 1 ? data[1] : "http://localhost:5173/login"; // Optional data handling
        // HTML for success email template
        return "<!DOCTYPE html>\r\n" +
                "<html lang=\"vi\">\r\n" +
                "<head>\r\n" +
                " <meta charset=\"UTF-8\">\r\n" +
                " <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" +
                " <title>Thông báo thay đổi mật khẩu thành công</title>\r\n" +
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
                " <h1>Thay đổi mật khẩu thành công </h1>\r\n" +
                " </div>\r\n" +
                " <div class=\"content\">\r\n" +
                " <p>Xin chào, " + username + "</p>\r\n" +
                " <p>Mật khẩu của bạn đã được thay đổi thành công. Bạn có thể đăng nhập lại và sử dụng tài khoản của mình bình thường.</p>\r\n"
                +
                " <p>Vui lòng đảm bảo bảo mật tài khoản của bạn. Nếu bạn không thực hiện yêu cầu này, hãy liên hệ với chúng tôi ngay lập tức.</p>\r\n"
                +
                " </div>\r\n" +
                " <div class=\"footer\">\r\n" +
                " <p>Trân trọng,</p>\r\n" +
                " <p>Đội ngũ Hỗ trợ</p>\r\n" +
                " <p>Đây là email tự động, vui lòng không trả lời email này.</p>\r\n" +
                "<a href='" + link + "'>Đổi mật khẩu ngay!</a>" +
                " </div>\r\n" +
                " </div>\r\n" +
                "</body>\r\n" +
                "</html>";
    }

    private static String buildOtpTemplate(String... data) {
        String otp = data[0];
        String link = data[1];
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
                + "] >>><a href='" + link + "'>Đổi mật khẩu ngay!</a></div>\r\n" +
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

    private static String huyDon(String... data) {
        String fullname = data.length > 0 ? data[0] : "";
        String id = data.length > 0 ? data[1] : "";
        String contents = data.length > 0 ? data[2] : "";
        String content = "<!DOCTYPE html>\r\n" + //
                "<html lang=\"en\">\r\n" + //
                "<head>\r\n" + //
                "    <meta charset=\"UTF-8\">\r\n" + //
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                "    <title>Thông Báo Hủy Đơn Hàng</title>\r\n" + //
                "    <style>\r\n" + //
                "        body {\r\n" + //
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            margin: 0;\r\n" + //
                "            padding: 0;\r\n" + //
                "            color: #333;\r\n" + //
                "        }\r\n" + //
                "        .email-container {\r\n" + //
                "            max-width: 600px;\r\n" + //
                "            margin: 30px auto;\r\n" + //
                "            background-color: #ffffff;\r\n" + //
                "            border-radius: 10px;\r\n" + //
                "            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\r\n" + //
                "            overflow: hidden;\r\n" + //
                "            border: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .email-header {\r\n" + //
                "            background-color: #2fbaf6; /* Tailwind sky-400 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            padding: 25px;\r\n" + //
                "            text-align: center;\r\n" + //
                "        }\r\n" + //
                "        .email-header h1 {\r\n" + //
                "            margin: 0;\r\n" + //
                "            font-size: 22px;\r\n" + //
                "            font-weight: bold;\r\n" + //
                "        }\r\n" + //
                "        .email-body {\r\n" + //
                "            padding: 20px 30px;\r\n" + //
                "        }\r\n" + //
                "        .email-body h2 {\r\n" + //
                "            font-size: 18px;\r\n" + //
                "            color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            margin-bottom: 15px;\r\n" + //
                "        }\r\n" + //
                "        .email-body p {\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            line-height: 1.8;\r\n" + //
                "            margin: 10px 0;\r\n" + //
                "        }\r\n" + //
                "        .email-body strong {\r\n" + //
                "            color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "        }\r\n" + //
                "        .email-footer {\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            padding: 15px;\r\n" + //
                "            font-size: 12px;\r\n" + //
                "            color: #64748b; /* Tailwind slate-500 */\r\n" + //
                "            text-align: center;\r\n" + //
                "            border-top: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .button {\r\n" + //
                "            display: inline-block;\r\n" + //
                "            background-color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            text-decoration: none;\r\n" + //
                "            padding: 10px 20px;\r\n" + //
                "            border-radius: 6px;\r\n" + //
                "            margin-top: 15px;\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            font-weight: 600;\r\n" + //
                "            transition: background-color 0.3s ease, transform 0.2s ease;\r\n" + //
                "        }\r\n" + //
                "        .button:hover {\r\n" + //
                "            background-color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "            transform: scale(1.05);\r\n" + //
                "        }\r\n" + //
                "        @media screen and (max-width: 600px) {\r\n" + //
                "            .email-body {\r\n" + //
                "                padding: 15px 20px;\r\n" + //
                "            }\r\n" + //
                "            .email-footer {\r\n" + //
                "                font-size: 10px;\r\n" + //
                "                padding: 10px;\r\n" + //
                "            }\r\n" + //
                "        }\r\n" + //
                "    </style>\r\n" + //
                "</head>\r\n" + //
                "<body>\r\n" + //
                "    <div class=\"email-container\">\r\n" + //
                "        <div class=\"email-header\">\r\n" + //
                "            <h1>TOEL - Thông Báo Hủy Đơn Hàng</h1>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"email-body\">\r\n" + //
                "            <h2>Xin chào " + fullname + ",</h2>\r\n" + //
                "            <p>Chúng tôi rất tiếc phải thông báo rằng đơn hàng của bạn với mã <strong>" + id
                + "</strong> đã được hủy. Chúng tôi xin lỗi vì sự bất tiện này.</p>\r\n"
                + //
                "            <p><strong>Lý do hủy:</strong>" + contents + "</p>\r\n" + //
                "            <p>Nếu bạn có bất kỳ thắc mắc nào hoặc cần thêm thông tin, vui lòng liên hệ với đội ngũ chăm sóc khách hàng của chúng tôi qua email dưới đây.</p>\r\n"
                + //
                "            <a href=\"mailto:khaduong28052004@gmail.com\" class=\"button\">Liên hệ hỗ trợ</a>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"email-footer\">\r\n" + //
                "            © 2024 TOEL. Tất cả các quyền được bảo lưu.  \r\n" + //
                "        </div>\r\n" + //
                "    </div>\r\n" + //
                "</body>\r\n" + //
                "</html>\r\n";
        return content;
    }

    private static String khoaTaiKhoan(String... data) {
        String fullname = data.length > 0 ? data[0] : "";
        String contents = (data.length > 1 && data[1] != null && !data[1].isEmpty())
                ? data[1].toString()
                : "Bạn đã vi phạm chính sách của sàn.";
        String entity = data.length > 0 ? data[2] : "";
        String content = "<!DOCTYPE html>\r\n" + //
                "<html lang=\"en\">\r\n" + //
                "<head>\r\n" + //
                "    <meta charset=\"UTF-8\">\r\n" + //
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                "    <title>Thông Báo Hủy Đơn Hàng</title>\r\n" + //
                "    <style>\r\n" + //
                "        body {\r\n" + //
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            margin: 0;\r\n" + //
                "            padding: 0;\r\n" + //
                "            color: #333;\r\n" + //
                "        }\r\n" + //
                "        .email-container {\r\n" + //
                "            max-width: 600px;\r\n" + //
                "            margin: 30px auto;\r\n" + //
                "            background-color: #ffffff;\r\n" + //
                "            border-radius: 10px;\r\n" + //
                "            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\r\n" + //
                "            overflow: hidden;\r\n" + //
                "            border: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .email-header {\r\n" + //
                "            background-color: #2fbaf6; /* Tailwind sky-400 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            padding: 25px;\r\n" + //
                "            text-align: center;\r\n" + //
                "        }\r\n" + //
                "        .email-header h1 {\r\n" + //
                "            margin: 0;\r\n" + //
                "            font-size: 22px;\r\n" + //
                "            font-weight: bold;\r\n" + //
                "        }\r\n" + //
                "        .email-body {\r\n" + //
                "            padding: 20px 30px;\r\n" + //
                "        }\r\n" + //
                "        .email-body h2 {\r\n" + //
                "            font-size: 18px;\r\n" + //
                "            color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            margin-bottom: 15px;\r\n" + //
                "        }\r\n" + //
                "        .email-body p {\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            line-height: 1.8;\r\n" + //
                "            margin: 10px 0;\r\n" + //
                "        }\r\n" + //
                "        .email-body strong {\r\n" + //
                "            color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "        }\r\n" + //
                "        .email-footer {\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            padding: 15px;\r\n" + //
                "            font-size: 12px;\r\n" + //
                "            color: #64748b; /* Tailwind slate-500 */\r\n" + //
                "            text-align: center;\r\n" + //
                "            border-top: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .button {\r\n" + //
                "            display: inline-block;\r\n" + //
                "            background-color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            text-decoration: none;\r\n" + //
                "            padding: 10px 20px;\r\n" + //
                "            border-radius: 6px;\r\n" + //
                "            margin-top: 15px;\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            font-weight: 600;\r\n" + //
                "            transition: background-color 0.3s ease, transform 0.2s ease;\r\n" + //
                "        }\r\n" + //
                "        .button:hover {\r\n" + //
                "            background-color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "            transform: scale(1.05);\r\n" + //
                "        }\r\n" + //
                "        @media screen and (max-width: 600px) {\r\n" + //
                "            .email-body {\r\n" + //
                "                padding: 15px 20px;\r\n" + //
                "            }\r\n" + //
                "            .email-footer {\r\n" + //
                "                font-size: 10px;\r\n" + //
                "                padding: 10px;\r\n" + //
                "            }\r\n" + //
                "        }\r\n" + //
                "    </style>\r\n" + //
                "</head>\r\n" + //
                "<body>\r\n" + //
                "    <div class=\"email-container\">\r\n" + //
                "        <div class=\"email-header\">\r\n" + //
                "            <h1>TOEL - Thông Báo Khóa " + entity + "</h1>" + //
                "        </div>\r\n" + //
                "        <div class=\"email-body\">\r\n" + //
                "            <h2>Xin chào " + fullname + ",</h2>\r\n" + //
                "            <p>Chúng tôi rất tiếc phải thông báo rằng " + entity + " của bạn"
                + " đã bị khóa.</p>\r\n"
                + //
                "            <p><strong>Lý do: </strong>" + contents + "</p>\r\n" + //
                "            <p>Nếu bạn có bất kỳ thắc mắc nào hoặc cần thêm thông tin, vui lòng liên hệ với đội ngũ chăm sóc khách hàng của chúng tôi qua email dưới đây.</p>\r\n"
                + //
                "            <a href=\"mailto:khaduong28052004@gmail.com\" class=\"button\">Liên hệ hỗ trợ</a>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"email-footer\">\r\n" + //
                "            © 2024 TOEL. Tất cả các quyền được bảo lưu.  \r\n" + //
                "        </div>\r\n" + //
                "    </div>\r\n" + //
                "</body>\r\n" + //
                "</html>\r\n";
        return content;
    }

    private static String moTaiKhoan(String... data) {
        String fullname = data.length > 0 ? data[0] : "";
        String contents = (data.length > 1 && data[1] != null && !data[1].isEmpty())
                ? data[1].toString()
                : "Mong bạn thực hiện đúng các chính sách của sàn.";
        String entity = data.length > 0 ? data[2] : "";
        String content = "<!DOCTYPE html>\r\n" + //
                "<html lang=\"en\">\r\n" + //
                "<head>\r\n" + //
                "    <meta charset=\"UTF-8\">\r\n" + //
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                "    <title>Thông Báo Hủy Đơn Hàng</title>\r\n" + //
                "    <style>\r\n" + //
                "        body {\r\n" + //
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            margin: 0;\r\n" + //
                "            padding: 0;\r\n" + //
                "            color: #333;\r\n" + //
                "        }\r\n" + //
                "        .email-container {\r\n" + //
                "            max-width: 600px;\r\n" + //
                "            margin: 30px auto;\r\n" + //
                "            background-color: #ffffff;\r\n" + //
                "            border-radius: 10px;\r\n" + //
                "            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\r\n" + //
                "            overflow: hidden;\r\n" + //
                "            border: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .email-header {\r\n" + //
                "            background-color: #2fbaf6; /* Tailwind sky-400 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            padding: 25px;\r\n" + //
                "            text-align: center;\r\n" + //
                "        }\r\n" + //
                "        .email-header h1 {\r\n" + //
                "            margin: 0;\r\n" + //
                "            font-size: 22px;\r\n" + //
                "            font-weight: bold;\r\n" + //
                "        }\r\n" + //
                "        .email-body {\r\n" + //
                "            padding: 20px 30px;\r\n" + //
                "        }\r\n" + //
                "        .email-body h2 {\r\n" + //
                "            font-size: 18px;\r\n" + //
                "            color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            margin-bottom: 15px;\r\n" + //
                "        }\r\n" + //
                "        .email-body p {\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            line-height: 1.8;\r\n" + //
                "            margin: 10px 0;\r\n" + //
                "        }\r\n" + //
                "        .email-body strong {\r\n" + //
                "            color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "        }\r\n" + //
                "        .email-footer {\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            padding: 15px;\r\n" + //
                "            font-size: 12px;\r\n" + //
                "            color: #64748b; /* Tailwind slate-500 */\r\n" + //
                "            text-align: center;\r\n" + //
                "            border-top: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .button {\r\n" + //
                "            display: inline-block;\r\n" + //
                "            background-color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            text-decoration: none;\r\n" + //
                "            padding: 10px 20px;\r\n" + //
                "            border-radius: 6px;\r\n" + //
                "            margin-top: 15px;\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            font-weight: 600;\r\n" + //
                "            transition: background-color 0.3s ease, transform 0.2s ease;\r\n" + //
                "        }\r\n" + //
                "        .button:hover {\r\n" + //
                "            background-color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "            transform: scale(1.05);\r\n" + //
                "        }\r\n" + //
                "        @media screen and (max-width: 600px) {\r\n" + //
                "            .email-body {\r\n" + //
                "                padding: 15px 20px;\r\n" + //
                "            }\r\n" + //
                "            .email-footer {\r\n" + //
                "                font-size: 10px;\r\n" + //
                "                padding: 10px;\r\n" + //
                "            }\r\n" + //
                "        }\r\n" + //
                "    </style>\r\n" + //
                "</head>\r\n" + //
                "<body>\r\n" + //
                "    <div class=\"email-container\">\r\n" + //
                "        <div class=\"email-header\">\r\n" + //
                "            <h1>TOEL - Thông Báo Mở " + entity + "</h1>" + //
                "        </div>\r\n" + //
                "        <div class=\"email-body\">\r\n" + //
                "            <h2>Xin chào " + fullname + ",</h2>\r\n" + //
                "            <p>Chúng tôi xin thông báo rằng " + entity + " của bạn đã được mở lại.</p>\r\n" + //
                "            <p>" + contents + "</p>\r\n" + //
                "            <p>Nếu bạn có bất kỳ thắc mắc nào hoặc cần thêm thông tin, vui lòng liên hệ với đội ngũ chăm sóc khách hàng của chúng tôi qua email dưới đây.</p>\r\n"
                + //
                "            <a href=\"mailto:khaduong28052004@gmail.com\" class=\"button\">Liên hệ hỗ trợ</a>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"email-footer\">\r\n" + //
                "            © 2024 TOEL. Tất cả các quyền được bảo lưu.  \r\n" + //
                "        </div>\r\n" + //
                "    </div>\r\n" + //
                "</body>\r\n" + //
                "</html>\r\n";
        return content;
    }

    private static String huyDuyet(String... data) {
        String fullname = data.length > 0 ? data[0] : "";
        String contents = data.length > 0 ? data[1].toString() : "";
        String entity = data.length > 0 ? data[2] : "";
        String ma = data.length > 0 ? data[3] : "";
        String ten = data.length > 0 ? data[4] : "";
        String loai = data.length > 0 ? data[5] : "";
        String danhmuc = entity.equalsIgnoreCase("sản phẩm") ? "loại" : "quyền";

        String content = "<!DOCTYPE html>\r\n" + //
                "<html lang=\"en\">\r\n" + //
                "<head>\r\n" + //
                "    <meta charset=\"UTF-8\">\r\n" + //
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                "    <title>Thông Báo Hủy Đơn Hàng</title>\r\n" + //
                "    <style>\r\n" + //
                "        body {\r\n" + //
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            margin: 0;\r\n" + //
                "            padding: 0;\r\n" + //
                "            color: #333;\r\n" + //
                "        }\r\n" + //
                "        .email-container {\r\n" + //
                "            max-width: 600px;\r\n" + //
                "            margin: 30px auto;\r\n" + //
                "            background-color: #ffffff;\r\n" + //
                "            border-radius: 10px;\r\n" + //
                "            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\r\n" + //
                "            overflow: hidden;\r\n" + //
                "            border: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .email-header {\r\n" + //
                "            background-color: #2fbaf6; /* Tailwind sky-400 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            padding: 25px;\r\n" + //
                "            text-align: center;\r\n" + //
                "        }\r\n" + //
                "        .email-header h1 {\r\n" + //
                "            margin: 0;\r\n" + //
                "            font-size: 22px;\r\n" + //
                "            font-weight: bold;\r\n" + //
                "        }\r\n" + //
                "        .email-body {\r\n" + //
                "            padding: 20px 30px;\r\n" + //
                "        }\r\n" + //
                "        .email-body h2 {\r\n" + //
                "            font-size: 18px;\r\n" + //
                "            color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            margin-bottom: 15px;\r\n" + //
                "        }\r\n" + //
                "        .email-body p {\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            line-height: 1.8;\r\n" + //
                "            margin: 10px 0;\r\n" + //
                "        }\r\n" + //
                "        .email-body strong {\r\n" + //
                "            color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "        }\r\n" + //
                "        .email-footer {\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            padding: 15px;\r\n" + //
                "            font-size: 12px;\r\n" + //
                "            color: #64748b; /* Tailwind slate-500 */\r\n" + //
                "            text-align: center;\r\n" + //
                "            border-top: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .button {\r\n" + //
                "            display: inline-block;\r\n" + //
                "            background-color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            text-decoration: none;\r\n" + //
                "            padding: 10px 20px;\r\n" + //
                "            border-radius: 6px;\r\n" + //
                "            margin-top: 15px;\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            font-weight: 600;\r\n" + //
                "            transition: background-color 0.3s ease, transform 0.2s ease;\r\n" + //
                "        }\r\n" + //
                "        .button:hover {\r\n" + //
                "            background-color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "            transform: scale(1.05);\r\n" + //
                "        }\r\n" + //
                "        @media screen and (max-width: 600px) {\r\n" + //
                "            .email-body {\r\n" + //
                "                padding: 15px 20px;\r\n" + //
                "            }\r\n" + //
                "            .email-footer {\r\n" + //
                "                font-size: 10px;\r\n" + //
                "                padding: 10px;\r\n" + //
                "            }\r\n" + //
                "        }\r\n" + //
                "    </style>\r\n" + //
                "</head>\r\n" + //
                "<body>\r\n" + //
                "    <div class=\"email-container\">\r\n" + //
                "        <div class=\"email-header\">\r\n" + //
                "            <h1>TOEL - Thông Báo Khóa " + entity + "</h1>" + //
                "        </div>\r\n" + //
                "        <div class=\"email-body\">\r\n" + //
                "            <h2>Xin chào " + fullname + ",</h2>\r\n" + //
                "            <p>Chúng tôi rất tiếc phải thông báo rằng " + entity + " của bạn"
                + " không được duyệt.</p>\r\n"
                + //
                "            <p>Chi tiết thông tin sản phẩm:  </p>\r\n" + //
                "            <p>- Mã " + entity + ": " + ma + " </p>\r\n" + //
                "            <p>- Tên " + entity + ":  " + ten + "</p>\r\n" + //
                "            <p>- " + danhmuc + ":  " + loai + "</p>\r\n" + //
                "            <p><strong>Lý do: </strong>" + contents + "</p>\r\n" + //
                "            <p>Nếu bạn có bất kỳ thắc mắc nào hoặc cần thêm thông tin, vui lòng liên hệ với đội ngũ chăm sóc khách hàng của chúng tôi qua email dưới đây.</p>\r\n"
                + //
                "            <a href=\"mailto:khaduong28052004@gmail.com\" class=\"button\">Liên hệ hỗ trợ</a>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"email-footer\">\r\n" + //
                "            © 2024 TOEL. Tất cả các quyền được bảo lưu.  \r\n" + //
                "        </div>\r\n" + //
                "    </div>\r\n" + //
                "</body>\r\n" + //
                "</html>\r\n";
        return content;
    }

    private static String duyet(String... data) {
        String fullname = data.length > 0 ? data[0] : "";
        String contents = data.length > 0 ? data[1].toString() : "";
        String entity = data.length > 0 ? data[2] : "";
        String ma = data.length > 0 ? data[3] : "";
        String ten = data.length > 0 ? data[4] : "";
        String loai = data.length > 0 ? data[5] : "";
        String danhmuc = entity.equalsIgnoreCase("sản phẩm") ? "loại" : "quyền";
        String content = "<!DOCTYPE html>\r\n" + //
                "<html lang=\"en\">\r\n" + //
                "<head>\r\n" + //
                "    <meta charset=\"UTF-8\">\r\n" + //
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                "    <title>Thông Báo Hủy Đơn Hàng</title>\r\n" + //
                "    <style>\r\n" + //
                "        body {\r\n" + //
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            margin: 0;\r\n" + //
                "            padding: 0;\r\n" + //
                "            color: #333;\r\n" + //
                "        }\r\n" + //
                "        .email-container {\r\n" + //
                "            max-width: 600px;\r\n" + //
                "            margin: 30px auto;\r\n" + //
                "            background-color: #ffffff;\r\n" + //
                "            border-radius: 10px;\r\n" + //
                "            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\r\n" + //
                "            overflow: hidden;\r\n" + //
                "            border: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .email-header {\r\n" + //
                "            background-color: #2fbaf6; /* Tailwind sky-400 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            padding: 25px;\r\n" + //
                "            text-align: center;\r\n" + //
                "        }\r\n" + //
                "        .email-header h1 {\r\n" + //
                "            margin: 0;\r\n" + //
                "            font-size: 22px;\r\n" + //
                "            font-weight: bold;\r\n" + //
                "        }\r\n" + //
                "        .email-body {\r\n" + //
                "            padding: 20px 30px;\r\n" + //
                "        }\r\n" + //
                "        .email-body h2 {\r\n" + //
                "            font-size: 18px;\r\n" + //
                "            color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            margin-bottom: 15px;\r\n" + //
                "        }\r\n" + //
                "        .email-body p {\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            line-height: 1.8;\r\n" + //
                "            margin: 10px 0;\r\n" + //
                "        }\r\n" + //
                "        .email-body strong {\r\n" + //
                "            color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "        }\r\n" + //
                "        .email-footer {\r\n" + //
                "            background-color: #f0f9ff; /* Tailwind sky-50 */\r\n" + //
                "            padding: 15px;\r\n" + //
                "            font-size: 12px;\r\n" + //
                "            color: #64748b; /* Tailwind slate-500 */\r\n" + //
                "            text-align: center;\r\n" + //
                "            border-top: 1px solid #bae6fd; /* Tailwind sky-200 */\r\n" + //
                "        }\r\n" + //
                "        .button {\r\n" + //
                "            display: inline-block;\r\n" + //
                "            background-color: #0ea5e9; /* Tailwind sky-500 */\r\n" + //
                "            color: #ffffff;\r\n" + //
                "            text-decoration: none;\r\n" + //
                "            padding: 10px 20px;\r\n" + //
                "            border-radius: 6px;\r\n" + //
                "            margin-top: 15px;\r\n" + //
                "            font-size: 14px;\r\n" + //
                "            font-weight: 600;\r\n" + //
                "            transition: background-color 0.3s ease, transform 0.2s ease;\r\n" + //
                "        }\r\n" + //
                "        .button:hover {\r\n" + //
                "            background-color: #0284c7; /* Tailwind sky-600 */\r\n" + //
                "            transform: scale(1.05);\r\n" + //
                "        }\r\n" + //
                "        @media screen and (max-width: 600px) {\r\n" + //
                "            .email-body {\r\n" + //
                "                padding: 15px 20px;\r\n" + //
                "            }\r\n" + //
                "            .email-footer {\r\n" + //
                "                font-size: 10px;\r\n" + //
                "                padding: 10px;\r\n" + //
                "            }\r\n" + //
                "        }\r\n" + //
                "    </style>\r\n" + //
                "</head>\r\n" + //
                "<body>\r\n" + //
                "    <div class=\"email-container\">\r\n" + //
                "        <div class=\"email-header\">\r\n" + //
                "            <h1>TOEL - Thông Báo Mở " + entity + "</h1>" + //
                "        </div>\r\n" + //
                "        <div class=\"email-body\">\r\n" + //
                "            <h2>Xin chào " + fullname + ",</h2>\r\n" + //
                "            <p>Chúng tôi xin thông báo rằng " + entity + " của bạn đã được duyệt.</p>\r\n" + //
                "            <p>Chi tiết thông tin sản phẩm:  </p>\r\n" + //
                "            <p>- Mã " + entity + ": " + ma + " </p>\r\n" + //
                "            <p>- Tên " + entity + ":  " + ten + "</p>\r\n" + //
                "            <p>- " + danhmuc + ":  " + loai + "</p>\r\n" + //

                "            <p>" + contents + "</p>\r\n" + //
                "            <p>Nếu bạn có bất kỳ thắc mắc nào hoặc cần thêm thông tin, vui lòng liên hệ với đội ngũ chăm sóc khách hàng của chúng tôi qua email dưới đây.</p>\r\n"
                + //
                "            <a href=\"mailto:khaduong28052004@gmail.com\" class=\"button\">Liên hệ hỗ trợ</a>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"email-footer\">\r\n" + //
                "            © 2024 TOEL. Tất cả các quyền được bảo lưu.  \r\n" + //
                "        </div>\r\n" + //
                "    </div>\r\n" + //
                "</body>\r\n" + //
                "</html>\r\n";
        return content;
    }

}
