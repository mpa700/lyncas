package com.lyncas.lyncas.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lyncas.lyncas.entity.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Page<Conta> findAll(Pageable pageable);
}