package com.example.demo.jpa;

import com.example.demo.DoctorVo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorResp extends JpaRepository<DoctorVo, Long> {
}
