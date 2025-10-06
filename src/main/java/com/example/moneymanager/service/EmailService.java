package com.example.moneymanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.from.email}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            String url = "https://api.brevo.com/v3/smtp/email";

            // Build the email payload
            Map<String, Object> emailData = Map.of(
                    "sender", Map.of("email", fromEmail, "name", "Money Manager"),
                    "to", List.of(Map.of("email", to)),
                    "subject", subject,
                    "htmlContent", "<html><body>" + body + "</body></html>"
            );

            // Set the headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", brevoApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Send the email
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(url, new HttpEntity<>(emailData, headers), String.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via Brevo API: " + e.getMessage(), e);
        }
    }
}
