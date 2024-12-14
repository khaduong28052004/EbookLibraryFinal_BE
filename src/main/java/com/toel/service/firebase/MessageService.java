package com.toel.service.firebase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.toel.model.DeviceId;
import com.toel.repository.DeviceIdRepository;

@Service
public class MessageService {
    @Autowired
    DeviceIdRepository deviceIdRepository;

    public boolean pushMessage(Integer accountId, String content) {
        List<DeviceId> listToken = deviceIdRepository.getListDeviceId(accountId);
        boolean response = true;
        for (DeviceId deviceId : listToken) {
            Message message = Message.builder()
                    .putData("content", content)
                    .setToken(deviceId.getToken())
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                response = false;
                e.printStackTrace();
            }
        }
        return response;
    }
}
