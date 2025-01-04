package com.lyncas.lyncas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;



@Data
@Entity
@Table(name = "contas")
public class Conta {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Version
	private Long version;

    @Column(nullable = false)
    @NotNull(message = "A data de vencimento é obrigatória")
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Column(nullable = false)
    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private Double valor;

    @NotBlank(message = "A descrição não pode estar vazia")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;
}