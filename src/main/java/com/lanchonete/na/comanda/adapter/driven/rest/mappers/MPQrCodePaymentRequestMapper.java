package com.lanchonete.na.comanda.adapter.driven.rest.mappers;

import static com.lanchonete.na.comanda.core.application.constants.ApiContants.DYNAMIC;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.ORDER_DESCRIPTION;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.QR;

import java.math.BigDecimal;
import java.util.List;

import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Config;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Transactions;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Config.Qr;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Transactions.Payments;

public class MPQrCodePaymentRequestMapper {

    public static MPQrCodePaymentRequest createMPQrCodePaymentRequest(final Long orderId, final BigDecimal totalPrice, final String externalPosId){
         
        return MPQrCodePaymentRequest.builder()
            .type(QR)
            .totalAmount(totalPrice.toString())
            .description(ORDER_DESCRIPTION + orderId)
            .externalReference(orderId.toString())
            .config(getQrConfig(externalPosId))
            .transactions(getQrTransactions(totalPrice))
            .build();
        
    }

    private static Config getQrConfig(final String externalPosId){
        final Qr qr = Qr.builder()
            .externalPosId(externalPosId)
            .mode(DYNAMIC)
            .build();

        return Config.builder().qr(qr).build();
    }

    private static Transactions getQrTransactions(final BigDecimal totalPrice){
        final Payments payments = Payments.builder()
            .amount(totalPrice.toString())
            .build();

        return Transactions.builder().payments(List.of(payments)).build();
    }
    
}
