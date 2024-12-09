package com.toel.service;

import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsTextualMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfobipService {

    private final SmsApi smsApi;

    public InfobipService() {
        var client = ApiClient.forApiKey(
            // 965c1e2d4143ea26a8769941ea1c4fdd-f3161572-c297-4caf-8afa-ac990c021e51 4ejqrp.api.infobip.com
                ApiKey.from("965c1e2d4143ea26a8769941ea1c4fdd-f3161572-c297-4caf-8afa-ac990c021e51"))
                .withBaseUrl(BaseUrl.from("https://4ejqrp.api.infobip.com")) // Đảm bảo URL đúng
                .build();
        this.smsApi = new SmsApi(client); // Tạo instance SmsApi một lần
    }

    public void sendSMS(String toPhone, String content) {
        var message = new SmsTextualMessage()
                .from("InfoSMS") // ID đã được xác minh từ Infobip
                .destinations(List.of(new SmsDestination().to(toPhone))) // Số điện thoại đích
                .text(content); // Nội dung SMS

        var request = new SmsAdvancedTextualRequest()
                .messages(List.of(message));

        try {
            var response = smsApi.sendSmsMessage(request).execute();
            System.out.println("SMS sent successfully: " + response);
        } catch (ApiException e) {
            System.out.printf("Error: %d - %s%n", e.responseStatusCode(), e.details().getText());
        }
    }
}
