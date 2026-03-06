package com.oceanview.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Payment Processor Adapter
 * 
 * DESIGN PATTERN: Adapter
 * - Adapts ExternalPaymentSystem interface to PaymentProcessor interface
 * - Allows our application to use external payment systems
 * - Translates method calls between incompatible interfaces
 * 
 * PRINCIPLE: Single Responsibility Principle (SRP)
 * - Only responsible for adapting payment interfaces
 * 
 * PRINCIPLE: Open/Closed Principle (OCP)
 * - Can add new external payment systems without modifying existing code
 * 
 * PRINCIPLE: Dependency Inversion Principle (DIP)
 * - Depends on abstractions (interfaces), not concrete implementations
 */
public class PaymentProcessorAdapter implements PaymentProcessor {

    private ExternalPaymentSystem externalPaymentSystem;
    private Map<Integer, String> transactionMap; // reservationId -> transactionId
    
    /**
     * Constructor with dependency injection
     * @param externalPaymentSystem The external payment system to adapt
     */
    public PaymentProcessorAdapter(ExternalPaymentSystem externalPaymentSystem) {
        this.externalPaymentSystem = externalPaymentSystem;
        this.transactionMap = new HashMap<>();
    }

    @Override
    public String chargeReservation(int reservationId, double amount) {
        // Generate transaction ID
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Adapt the call to external payment system
        boolean success = externalPaymentSystem.processPayment(amount, "USD", transactionId);
        
        if (success) {
            transactionMap.put(reservationId, transactionId);
            return transactionId;
        }
        return null;
    }

    @Override
    public boolean cancelCharge(int reservationId) {
        String transactionId = transactionMap.get(reservationId);
        if (transactionId == null) {
            return false;
        }
        
        // Adapt the call to external refund system
        boolean success = externalPaymentSystem.refundPayment(transactionId);
        if (success) {
            transactionMap.remove(reservationId);
        }
        return success;
    }

    @Override
    public String getPaymentStatus(int reservationId) {
        String transactionId = transactionMap.get(reservationId);
        if (transactionId != null) {
            return "PAID - Transaction: " + transactionId;
        }
        return "NO PAYMENT";
    }
}
