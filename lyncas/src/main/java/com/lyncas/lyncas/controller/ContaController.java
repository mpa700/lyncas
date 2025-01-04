package com.lyncas.lyncas.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lyncas.lyncas.entity.Conta;
import com.lyncas.lyncas.service.ContaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova conta")
    @ApiResponse(responseCode = "200", description = "Conta cadastrada com sucesso")
    public ResponseEntity<Conta> cadastrar(@RequestBody @Valid Conta conta) {
        Conta novaConta = contaService.cadastrarConta(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    @Operation(summary = "Listar todas as contas")
    @ApiResponse(responseCode = "200", description = "Contas listadas com sucesso")
    public ResponseEntity<Page<Conta>> listar(Pageable pageable) {
        Page<Conta> contas = contaService.listarContas(pageable);
        return ResponseEntity.ok(contas);
    }
    
    
    @GetMapping("/listaro")
    @Operation(summary = "Listar todas as contas")
    @ApiResponse(responseCode = "200", description = "Contas listadas com sucesso")
    public ResponseEntity<Page<Conta>> listaro(Pageable pageable) {
        Page<Conta> contas = contaService.listarContas(pageable);
        return ResponseEntity.ok(contas);
    }

    @PutMapping("/{id}/pagar")
    @Operation(summary = "Paga uma conta")
    @ApiResponse(responseCode = "200", description = "Conta paga com sucesso")
    public ResponseEntity<Conta> pagar(@PathVariable Long id) {
        Conta contaPaga = contaService.pagarConta(id);
        return ResponseEntity.ok(contaPaga);
    }
}