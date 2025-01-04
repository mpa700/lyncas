package com.lyncas.lyncas.service;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ContaDTO {  
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private Double valor;
    private String descricao;
    private String situacao;
}