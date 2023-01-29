package az.et.mspayment.mapper;

import az.et.mspayment.entity.Payment;
import az.et.mspayment.model.request.PaymentRequest;
import az.et.mspayment.model.response.PaymentResponse;

import java.time.LocalDateTime;

public class PaymentMapper {

    public static Payment mapRequestToEntity(PaymentRequest request){
        return Payment.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
    }
    public static PaymentResponse mapEntityToResponse(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .responseAt(LocalDateTime.now())
                .build();
    }
}
