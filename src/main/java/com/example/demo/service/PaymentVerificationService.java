package com.example.demo.service;

import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.Payment;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
@Service
public class PaymentVerificationService {
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${vola.api.key}")
    private String apiKey;

    @Value("${vola.api.url}")
    private String volaApiUrl;

    public PaymentVerificationService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Méthode pour lancer la vérification lors de la soumission du paiement
    public void submitPaymentVerification(Payment payment) {
        payment.setStatus("VERIFYING");
        paymentRepository.save(payment);
    }

    // Tâche planifiée qui tourne toutes les minutes pour vérifier les paiements en VERIFYING
    @Scheduled(fixedDelay = 60000)
    public void verifyPaymentsStatus() {
        List<Payment> pendingPayments = paymentRepository.findAll()
                .stream()
                .filter(p -> "VERIFYING".equals(p.getStatus()))
                .toList();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        for (Payment payment : pendingPayments) {
            String url = volaApiUrl + "/payments/" + payment.getId();

            try {
                ResponseEntity<PaymentResponse> response =
                        restTemplate.exchange(url, HttpMethod.GET, entity, PaymentResponse.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    String state = response.getBody().getState();

                    if ("SUCCEEDED".equals(state) || "FAILED".equals(state)) {
                        payment.setStatus(state);
                        paymentRepository.save(payment);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur récupération paiement Vola : " + e.getMessage());
            }
        }
    }

    private static class PaymentResponse {
        private String state;
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
    }
}
