package az.et.mspayment.service;

import az.et.mspayment.client.CountryClient;
import az.et.mspayment.entity.Payment;
import az.et.mspayment.exception.NotFoundException;
import az.et.mspayment.mapper.PaymentMapper;
import az.et.mspayment.model.request.PaymentRequest;
import az.et.mspayment.model.response.PaymentResponse;
import az.et.mspayment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.et.mspayment.mapper.PaymentMapper.mapEntityToResponse;
import static az.et.mspayment.mapper.PaymentMapper.mapRequestToEntity;
import static az.et.mspayment.model.constant.ExceptionConstant.COUNTRY_NOT_FOUND_CODE;
import static az.et.mspayment.model.constant.ExceptionConstant.COUNTRY_NOT_FOUND_MESSAGE;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CountryClient countryClient;

    public void savePayment(PaymentRequest request) {
        log.info("savePayment.started");
        countryClient.getAllAvailableCountries(request.getCurrency())
                .stream()
                .filter(country -> country.getRemainingLimit().compareTo(request.getAmount()) > 0)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(COUNTRY_NOT_FOUND_MESSAGE,
                        request.getAmount(),
                        request.getCurrency()),
                        COUNTRY_NOT_FOUND_CODE));

        paymentRepository.save(mapRequestToEntity(request));
        log.info("savePayment.success");
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::mapEntityToResponse).collect(Collectors.toList());

    }

    public PaymentResponse getPaymentById(Long id) {
        log.info("getPayment.start id: {}", id);
        Payment payment = fetchPaymentIfExist(id);
        log.info("getPayment.success id: {}", id);
        return mapEntityToResponse(payment);
    }

    public void updatePayment(Long id, PaymentRequest request) {
        log.info("updatePayment.start id: {}", id);
        Payment payment = fetchPaymentIfExist(id);
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        paymentRepository.save(payment);
        log.info("updatePayment.success id: {}", id);
    }

    public void deletePaymentById(Long id) {
        log.info("deletePayment.start id: {}", id);
        paymentRepository.deleteById(id);
        log.info("deletePayment.success id: {}", id);
    }

    private Payment fetchPaymentIfExist(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(RuntimeException::new);

    }
}
