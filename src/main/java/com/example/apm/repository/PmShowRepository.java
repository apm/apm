package com.example.apm.repository;

import com.example.apm.entity.PmShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PmShowRepository extends JpaRepository<PmShow, String> {
    Optional<PmShow> findByPmShowId(String pmShowId);
    Page<PmShow> findAll(Pageable pageable);
}
