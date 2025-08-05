package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    private String id;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    private double amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String status; // VERIFYING, SUCCEEDED, FAILED

}
