package com.example.demo.repository;

import com.example.demo.model.Donation;
import com.example.demo.model.Help;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpRepository extends JpaRepository<Help, Long> {
    List<Help> findAllByOrderByDateDesc();
}
