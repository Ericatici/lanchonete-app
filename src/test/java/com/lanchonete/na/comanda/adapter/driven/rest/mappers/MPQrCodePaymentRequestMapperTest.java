package com.lanchonete.na.comanda.adapter.driven.rest.mappers;

import static com.lanchonete.na.comanda.core.application.constants.ApiContants.DYNAMIC;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.ORDER_DESCRIPTION;
import static com.lanchonete.na.comanda.core.application.constants.ApiContants.QR;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.ORDER_ID;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.TOTAL_PRICE;
import static com.lanchonete.na.comanda.core.application.services.helper.TestContants.EXTERNAL_POS_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Config;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Config.Qr;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Transactions;
import com.lanchonete.na.comanda.adapter.driven.rest.request.MPQrCodePaymentRequest.Transactions.Payments;

public class MPQrCodePaymentRequestMapperTest {

    @Test
    void shouldCreateMPQrCodePaymentRequestSuccessfully() {
        Long orderId = ORDER_ID;
        BigDecimal totalPrice = TOTAL_PRICE;
        String externalPosId = EXTERNAL_POS_ID;

        MPQrCodePaymentRequest request = MPQrCodePaymentRequestMapper.createMPQrCodePaymentRequest(orderId, totalPrice, externalPosId);

        assertNotNull(request);
        assertEquals(QR, request.getType());
        assertEquals(totalPrice.toString(), request.getTotalAmount());
        assertEquals(ORDER_DESCRIPTION + orderId, request.getDescription());
        assertEquals(orderId.toString(), request.getExternalReference());

        Config config = request.getConfig();
        assertNotNull(config);
        Qr qr = config.getQr();
        assertNotNull(qr);
        assertEquals(externalPosId, qr.getExternalPosId());
        assertEquals(DYNAMIC, qr.getMode());

        Transactions transactions = request.getTransactions();
        assertNotNull(transactions);
        assertNotNull(transactions.getPayments());
        assertEquals(1, transactions.getPayments().size());
        Payments payment = transactions.getPayments().get(0);
        assertEquals(totalPrice.toString(), payment.getAmount());
    }
}
