package com.toel.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

public class GoogleTokenVerifier {
    private static final String CLIENT_ID = "802515130057-2djim3amjrd5pinc6rmspgid56l1rkdl.apps.googleusercontent.com"; 
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static GoogleIdToken.Payload verifyToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                System.out.println("Invalid ID token.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    // }
    // private static final String CLIENT_ID = "802515130057-2djim3amjrd5pinc6rmspgid56l1rkdl.apps.googleusercontent.com"; // Thay YOUR_CLIENT_ID bằng Client ID của bạn
    // private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // public static GoogleIdToken.Payload verifyToken(String idTokenString) {
    //     try {
    //         GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
    //                 .setAudience(Collections.singletonList(CLIENT_ID))
    //                 .build();

    //         GoogleIdToken idToken = verifier.verify(idTokenString);
    //         if (idToken != null) {
    //             return idToken.getPayload();
    //         } else {
    //             System.out.println("Invalid ID token.");
    //         }
    //     } catch (Exception e) {
    //         System.out.println("lỗi");
    //         e.printStackTrace();
    //     }
        // return null;
    }
}
