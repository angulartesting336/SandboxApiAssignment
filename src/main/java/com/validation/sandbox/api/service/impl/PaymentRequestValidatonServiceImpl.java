package com.validation.sandbox.api.service.impl;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.validation.sandbox.api.exception.InvalidRequestException;
import com.validation.sandbox.api.model.PaymentInitiationRequest;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@Service
public class PaymentRequestValidatonServiceImpl {

	String INVALID_REQUEST = "INVALID_REQUEST";

	String IBAN_FORMAT = "[A-Z]{2}[0-9]{2}[a-zA-Z0-9]{1,30}";

	String AMOUNT_FORMAT = "-?[0-9]+(.[0-9]{1,3})?";

	String REJECTED = "Rejected";

	String REQUEST_VALIDATE = "Got rejected due to IBAN or Amount validation failed";

	public void validatePaymentRequest(PaymentInitiationRequest paymentInitiationRequest, String signatureCertificate) {

		Stream<String> initialStream = Stream.of(paymentInitiationRequest.getCreditorIBAN(),
				paymentInitiationRequest.getDebtorIBAN(), paymentInitiationRequest.getAmount());

		if (!formatValidator(initialStream).isEmpty()) {
			PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

			paymentRejectedResponse.setReason(REQUEST_VALIDATE);
			paymentRejectedResponse.setReasonCode(INVALID_REQUEST);
			paymentRejectedResponse.setStatus(REJECTED);

			throw new InvalidRequestException(paymentRejectedResponse, signatureCertificate);

		}

	}

	private List<String> formatValidator(Stream<String> requestObjects) {

		return requestObjects
				.filter(requestObject -> (!requestObject.contains(".") && !Pattern.matches(IBAN_FORMAT, requestObject))
						|| (requestObject.contains(".") && !Pattern.matches(AMOUNT_FORMAT, requestObject)))
				.collect(Collectors.toList());

	}

}
