package com.oceanview.adapter;

/**
 * Internal Payment Processor Interface
 * 
 * This is our application's payment interface
 * 
 * DESIGN PATTERN: Adapter (Adaptee - what we have)
 * - Our internal interface for processing payments
 * - Different from external payment system interface
 */
public interface PaymentProcessor {
    
    /**
     * Charge a reservation
     * @param reservationId The reservation ID
     * @param amount The amount to charge
     * @return Transaction ID if successful, null otherwise
     */
    String chargeReservation(int reservationId, double amount);
    
    /**
     * Cancel a charge (refund)
     * @param reservationId The reservation ID
     * @return true if successful
     */
    boolean cancelCharge(int reservationId);
    
    /**
     * Get payment status
     * @param reservationId The reservation ID
     * @return Payment status string
     */
    String getPaymentStatus(int reservationId);
}
