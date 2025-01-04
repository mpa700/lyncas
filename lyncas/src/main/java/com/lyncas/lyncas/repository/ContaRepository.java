package com.lyncas.lyncas.repository;


import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lyncas.lyncas.entity.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Page<Conta> findAll(Pageable pageable);
    
    List<Conta> findByDataVencimento(LocalDate dataVencimento);

    List<Conta> findByDescricaoContainingIgnoreCase(String descricao);

    List<Conta> findByDataVencimentoAndDescricao(LocalDate dataVencimento, String descricao);
    
    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :dataInicio AND :dataFim")
    Optional<BigDecimal> calcularTotalPagoPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}