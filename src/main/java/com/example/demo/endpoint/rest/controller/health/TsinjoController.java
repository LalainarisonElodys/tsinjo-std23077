package com.example.demo.endpoint.rest.controller.health;

import com.example.demo.model.Donation;
import com.example.demo.model.Donor;
import com.example.demo.model.Payment;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.DonorRepository;
import com.example.demo.repository.HelpRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentVerificationService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;


public class TsinjoController {

    private final DonationRepository donationRepository;
    private final HelpRepository helpRepository;
    private final DonorRepository donorRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentVerificationService paymentVerificationService;

    public TsinjoController(DonationRepository donationRepository, HelpRepository helpRepository,
                            DonorRepository donorRepository, PaymentRepository paymentRepository,
                            PaymentVerificationService paymentVerificationService) {
        this.donationRepository = donationRepository;
        this.helpRepository = helpRepository;
        this.donorRepository = donorRepository;
        this.paymentRepository = paymentRepository;
        this.paymentVerificationService = paymentVerificationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("donations", donationRepository.findAll());
        model.addAttribute("helps", helpRepository.findAll());
        return "index";
    }

    @PostMapping("/donate")
    public String donate(@RequestParam String fullName,
                         @RequestParam String email,
                         @RequestParam double amount,
                         @RequestParam String paymentMethod) {

        Donor donor = donorRepository.findByEmail(email).orElseGet(() -> {
            Donor d = new Donor();
            d.setEmail(email);
            d.setFullName(fullName);
            return donorRepository.save(d);
        });
        Payment payment = new Payment();
        payment.setId(generatePaymentId()); // méthode pour générer un ID unique
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDate.now());
        paymentRepository.save(payment);

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setPayment(payment);
        donationRepository.save(donation);

        paymentVerificationService.submitPaymentVerification(payment);

        return "redirect:/";
    }

    private String generatePaymentId() {
        return "PAY-" + System.currentTimeMillis();
    }
}
