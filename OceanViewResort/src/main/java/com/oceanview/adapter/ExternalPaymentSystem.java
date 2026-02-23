package com.oceanview.adapter;

/**
 * External Payment System Interface
 * 
 * This represents an external payment gateway with a different interface
 * 
 * DESIGN PATTERN: Adapter (Target Interface)
 * - Defines the interface that clients use
 * - Will be implemented by the adapter
 */
public interface ExternalPaymentSystem {
    
    /**
     * Process a payment
     * @param amount The amount to charge
     * @param currency The currency code
     * @param transactionId The transaction reference
     * @return true if successful
     */
    boolean processPayment(double amount, String currency, String transactionId);
    
    /**
     * Refund a payment
     * @param transactionId The transaction to refund
     * @return true if successful
     */
    boolean refundPayment(String transactionId);
}
