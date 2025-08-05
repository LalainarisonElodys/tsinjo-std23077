package com.example.demo.repository;

import com.example.demo.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Integer> {
    Optional<Donor> findByEmail(String email);
}
