package com.lanchonete.na.comanda.core.domain.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentData {

    private String qrCode;
    private String paymentId;
    
}
