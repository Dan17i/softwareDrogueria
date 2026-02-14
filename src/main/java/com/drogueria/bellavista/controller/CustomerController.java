package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.CustomerDTO;
import com.drogueria.bellavista.application.mapper.CustomerUseCaseMapper;
import com.drogueria.bellavista.domain.model.Customer;
import com.drogueria.bellavista.domain.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")  // ← CAMBIADO
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerUseCaseMapper mapper;

    @PostMapping
    public ResponseEntity<CustomerDTO.Response> createCustomer(
            @Valid @RequestBody CustomerDTO.CreateRequest request) {

        Customer customer = mapper.toDomain(request);
        Customer createdCustomer = customerService.createCustomer(customer);
        CustomerDTO.Response response = mapper.toResponse(createdCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO.Response> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO.UpdateRequest request) {

        Customer customerData = mapper.toDomain(request);
        Customer updatedCustomer = customerService.updateCustomer(id, customerData);
        CustomerDTO.Response response = mapper.toResponse(updatedCustomer);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO.Response> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        CustomerDTO.Response response = mapper.toResponse(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CustomerDTO.Response> getCustomerByCode(@PathVariable String code) {
        Customer customer = customerService.getCustomerByCode(code);
        CustomerDTO.Response response = mapper.toResponse(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO.Response> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        CustomerDTO.Response response = mapper.toResponse(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO.Response>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO.Response> responses = customers.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/active")
    public ResponseEntity<List<CustomerDTO.Response>> getActiveCustomers() {
        List<Customer> customers = customerService.getActiveCustomers();
        List<CustomerDTO.Response> responses = customers.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{customerType}")
    public ResponseEntity<List<CustomerDTO.Response>> getCustomersByType(
            @PathVariable String customerType) {

        List<Customer> customers = customerService.getCustomersByType(customerType);
        List<CustomerDTO.Response> responses = customers.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/morosos")
    public ResponseEntity<List<CustomerDTO.Response>> getMorosos() {
        List<Customer> customers = customerService.getMorosos();
        List<CustomerDTO.Response> responses = customers.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomerDTO.Response> deactivateCustomer(@PathVariable Long id) {
        Customer customer = customerService.deactivateCustomer(id);
        CustomerDTO.Response response = mapper.toResponse(customer);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomerDTO.Response> activateCustomer(@PathVariable Long id) {
        Customer customer = customerService.activateCustomer(id);
        CustomerDTO.Response response = mapper.toResponse(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/credit-available/{amount}")
    public ResponseEntity<Boolean> hasCreditAvailable(
            @PathVariable Long id,
            @PathVariable java.math.BigDecimal amount) {

        boolean hasCredit = customerService.hasCreditAvailable(id, amount);
        return ResponseEntity.ok(hasCredit);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceInfo> getCustomerBalance(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);

        java.math.BigDecimal availableCredit = customer.getCreditLimit()
                .subtract(customer.getPendingBalance() != null ? customer.getPendingBalance() : java.math.BigDecimal.ZERO);

        BalanceInfo balanceInfo = BalanceInfo.builder()
                .creditLimit(customer.getCreditLimit())
                .pendingBalance(customer.getPendingBalance())
                .availableCredit(availableCredit)
                .isMoroso(customer.isMoroso())
                .build();

        return ResponseEntity.ok(balanceInfo);
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class BalanceInfo {
        private java.math.BigDecimal creditLimit;
        private java.math.BigDecimal pendingBalance;
        private java.math.BigDecimal availableCredit;
        private Boolean isMoroso;
    }
}