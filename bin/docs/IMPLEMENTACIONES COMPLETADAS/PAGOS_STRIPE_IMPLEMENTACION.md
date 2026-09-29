# 🚀 GUÍA IMPLEMENTACIÓN PASARELA PAGOS CON STRIPE

## 1. CREAR MAPPER PaymentMapper

Crear archivo: `src/main/java/com/drogueria/bellavista/infrastructure/mapper/PaymentMapper.java`

```java
package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    
    public Payment fromDomain(PaymentEntity entity) {
        if (entity == null) return null;
        
        return Payment.builder()
            .id(entity.getId())
            .orderId(entity.getOrderId())
            .customerId(entity.getCustomerId())
            .stripePaymentId(entity.getStripePaymentId())
            .stripeIntentId(entity.getStripeIntentId())
            .amount(entity.getAmount())
            .currency(entity.getCurrency())
            .status(Payment.PaymentStatus.valueOf(entity.getStatus().name()))
            .paymentMethod(entity.getPaymentMethod())
            .description(entity.getDescription())
            .errorMessage(entity.getErrorMessage())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .paidAt(entity.getPaidAt())
            .build();
    }
    
    public PaymentEntity toDomain(Payment domain) {
        if (domain == null) return null;
        
        return PaymentEntity.builder()
            .id(domain.getId())
            .orderId(domain.getOrderId())
            .customerId(domain.getCustomerId())
            .stripePaymentId(domain.getStripePaymentId())
            .stripeIntentId(domain.getStripeIntentId())
            .amount(domain.getAmount())
            .currency(domain.getCurrency())
            .status(PaymentEntity.PaymentStatus.valueOf(domain.getStatus().name()))
            .paymentMethod(domain.getPaymentMethod())
            .description(domain.getDescription())
            .errorMessage(domain.getErrorMessage())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .paidAt(domain.getPaidAt())
            .build();
    }
}
```

---

## 2. CREAR SERVICIO APLICACIÓN StripePaymentService

Crear archivo: `src/main/java/com/drogueria/bellavista/application/service/StripePaymentService.java`

```java
package com.drogueria.bellavista.application.service;

import com.drogueria.bellavista.application.dto.PaymentRequest;
import com.drogueria.bellavista.application.dto.PaymentResponse;
import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.domain.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StripePaymentService {
    
    private final PaymentService paymentService;
    
    @Value("${app.stripe.api.key}")
    private String stripeApiKey;
    
    /**
     * Procesar pago con Stripe.
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            Stripe.apiKey = stripeApiKey;
            
            // Crear payment intent en Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount().longValue() * 100) // Stripe usa centavos
                .setCurrency("usd")
                .setPaymentMethod(request.getPaymentMethodId())
                .setConfirm(true)
                .setDescription("Orden #" + request.getOrderId())
                .putMetadata("orderId", request.getOrderId().toString())
                .putMetadata("customerId", request.getCustomerId().toString())
                .build();
            
            PaymentIntent intent = PaymentIntent.create(params);
            
            // Crear registro en BD
            Payment payment = paymentService.createPayment(
                request.getOrderId(),
                request.getCustomerId(),
                request.getAmount(),
                "Pago por orden #" + request.getOrderId()
            );
            
            // Actualizar con datos de Stripe
            payment = paymentService.updatePaymentWithStripeData(
                payment.getId(),
                intent.getId(),
                intent.getId(),
                Payment.PaymentStatus.PROCESSING
            );
            
            // Verificar resultado
            if ("succeeded".equals(intent.getStatus())) {
                payment = paymentService.markAsSucceeded(payment.getId());
                log.info("✅ Pago exitoso: orderId={}, paymentId={}", request.getOrderId(), payment.getId());
                return buildSuccessResponse(payment, intent);
            } else if ("requires_action".equals(intent.getStatus())) {
                return buildPendingResponse(payment, intent);
            } else {
                payment = paymentService.markAsFailed(payment.getId(), intent.getLastPaymentError().getMessage());
                return buildFailureResponse(payment, intent);
            }
            
        } catch (StripeException e) {
            log.error("❌ Error procesando pago con Stripe: {}", e.getMessage());
            return PaymentResponse.builder()
                .success(false)
                .error(e.getMessage())
                .build();
        }
    }
    
    /**
     * Verificar estado de pago.
     */
    public PaymentResponse checkPaymentStatus(String stripeIntentId) {
        try {
            Stripe.apiKey = stripeApiKey;
            
            PaymentIntent intent = PaymentIntent.retrieve(stripeIntentId);
            Payment payment = paymentService.getByStripePaymentId(intent.getId());
            
            return PaymentResponse.builder()
                .success(true)
                .paymentId(payment.getId())
                .stripePaymentId(intent.getId())
                .status(intent.getStatus())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .build();
            
        } catch (StripeException e) {
            log.error("Error verificando pago: {}", e.getMessage());
            return PaymentResponse.builder()
                .success(false)
                .error(e.getMessage())
                .build();
        }
    }
    
    private PaymentResponse buildSuccessResponse(Payment payment, PaymentIntent intent) {
        return PaymentResponse.builder()
            .success(true)
            .paymentId(payment.getId())
            .stripePaymentId(intent.getId())
            .status("succeeded")
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .build();
    }
    
    private PaymentResponse buildPendingResponse(Payment payment, PaymentIntent intent) {
        return PaymentResponse.builder()
            .success(false)
            .paymentId(payment.getId())
            .stripePaymentId(intent.getId())
            .status("requires_action")
            .error("Pago requiere acción adicional")
            .build();
    }
    
    private PaymentResponse buildFailureResponse(Payment payment, PaymentIntent intent) {
        return PaymentResponse.builder()
            .success(false)
            .paymentId(payment.getId())
            .stripePaymentId(intent.getId())
            .status("failed")
            .error(intent.getLastPaymentError().getMessage())
            .build();
    }
}
```

---

## 3. CREAR DTOs

### PaymentRequest.java
`src/main/java/com/drogueria/bellavista/application/dto/PaymentRequest.java`

```java
package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Payment Method ID is required")
    private String paymentMethodId;
}
```

### PaymentResponse.java
`src/main/java/com/drogueria/bellavista/application/dto/PaymentResponse.java`

```java
package com.drogueria.bellavista.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    
    private Boolean success;
    private Long paymentId;
    private String stripePaymentId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String error;
}
```

---

## 4. CREAR CONTROLLER PaymentController

`src/main/java/com/drogueria/bellavista/controller/PaymentController.java`

```java
package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.PaymentRequest;
import com.drogueria.bellavista.application.dto.PaymentResponse;
import com.drogueria.bellavista.application.service.StripePaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final StripePaymentService stripePaymentService;
    
    /**
     * POST /api/payments/process
     * Procesar pago con Stripe.
     */
    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('SALES', 'MANAGER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = stripePaymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/payments/{intentId}
     * Verificar estado de pago.
     */
    @GetMapping("/{intentId}")
    @PreAuthorize("hasAnyRole('SALES', 'MANAGER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> checkPaymentStatus(@PathVariable String intentId) {
        PaymentResponse response = stripePaymentService.checkPaymentStatus(intentId);
        return ResponseEntity.ok(response);
    }
}
```

---

## 5. TESTS UNITARIOS

`src/test/java/com/drogueria/bellavista/domain/service/PaymentServiceTest.java`

```java
package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Payment;
import com.drogueria.bellavista.domain.repository.PaymentRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    
    private PaymentService paymentService;
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(paymentRepository);
    }
    
    @Test
    @DisplayName("Debe crear un pago exitosamente")
    void testCreatePaymentSuccess() {
        Long orderId = 1L;
        Long customerId = 1L;
        BigDecimal amount = new BigDecimal("99.99");
        
        Payment payment = Payment.builder()
            .orderId(orderId)
            .customerId(customerId)
            .amount(amount)
            .currency("USD")
            .status(Payment.PaymentStatus.PENDING)
            .build();
        
        when(paymentRepository.save(any())).thenReturn(payment);
        
        Payment result = paymentService.createPayment(orderId, customerId, amount, "Test");
        
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(amount, result.getAmount());
        assertEquals(Payment.PaymentStatus.PENDING, result.getStatus());
    }
    
    @Test
    @DisplayName("No debe crear pago con monto inválido")
    void testCreatePaymentInvalidAmount() {
        assertThrows(BusinessException.class, () -> 
            paymentService.createPayment(1L, 1L, BigDecimal.ZERO, "Test")
        );
    }
    
    @Test
    @DisplayName("No debe crear pago sin customer ID")
    void testCreatePaymentMissingCustomerId() {
        assertThrows(BusinessException.class, () -> 
            paymentService.createPayment(1L, null, new BigDecimal("100"), "Test")
        );
    }
    
    @Test
    @DisplayName("Debe marcar pago como exitoso")
    void testMarkAsSucceeded() {
        Long paymentId = 1L;
        Payment payment = Payment.builder()
            .id(paymentId)
            .status(Payment.PaymentStatus.PENDING)
            .build();
        
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenReturn(payment);
        
        Payment result = paymentService.markAsSucceeded(paymentId);
        
        assertEquals(Payment.PaymentStatus.SUCCEEDED, result.getStatus());
        assertNotNull(result.getPaidAt());
    }
    
    @Test
    @DisplayName("Debe obtener pago por ID")
    void testGetPaymentById() {
        Long paymentId = 1L;
        Payment payment = Payment.builder()
            .id(paymentId)
            .build();
        
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        
        Payment result = paymentService.getPaymentById(paymentId);
        
        assertNotNull(result);
        assertEquals(paymentId, result.getId());
    }
    
    @Test
    @DisplayName("Debe lanzar excepción si pago no existe")
    void testGetPaymentByIdNotFound() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> 
            paymentService.getPaymentById(999L)
        );
    }
}
```

---

## 6. CONFIGURACIÓN STRIPE EN application-dev.yml

```yaml
app:
  stripe:
    api:
      key: sk_test_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
      public-key: pk_test_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

**IMPORTANTE:** Reemplazar con tus llaves de test de Stripe.

---

## 7. TARJETAS DE PRUEBA STRIPE

```
ÉXITO:
4242 4242 4242 4242  | Exp: 12/25 | CVC: 123

DECLINAR:
4000 0000 0000 0002  | Exp: 12/25 | CVC: 123

CVC REQUERIDO:
4000 0025 0000 3155  | Exp: 12/25 | CVC: 123
```

---

## 8. OBTENER LLAVES STRIPE

1. Ir a: https://dashboard.stripe.com/register
2. Crear cuenta con Gmail
3. Dashboard → Developers → API Keys
4. Copiar `sk_test_xxx` (Secret Key - PRIVADA)
5. Copiar `pk_test_xxx` (Public Key - PÚBLICA)

---

## PRÓXIMOS PASOS

1. ✅ Crear todos los archivos según las instrucciones
2. ✅ Ejecutar tests: `mvn test`
3. ✅ Compilar: `mvn clean package`
4. ✅ Probar con frontend

¡Listo para producción! 🚀

