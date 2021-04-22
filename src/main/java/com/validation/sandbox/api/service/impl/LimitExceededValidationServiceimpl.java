package com.validation.sandbox.api.service.impl;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.validation.sandbox.api.exception.LimitExceededException;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@Service
public class LimitExceededValidationServiceimpl {

	String FORMAT_AMOUNT = "[^0-9]";

	String AMOUNT_EXCEEDED = "LIMIT_EXCEEDED";

	String LIMIT_EXCEEDED = "Got rejected due to Amount limit exceeded";

	String REJECTED = "Rejected";

	public void checkLimitExceeded(String amount, String debtorIban, String signatureCertificate) {

		if ((Double.parseDouble(amount) > 0)
				&& (Arrays.stream(debtorIban.replaceAll(FORMAT_AMOUNT, "").replaceAll("\\B", " ").split(" "))
						.mapToInt(Integer::parseInt).sum() % debtorIban.length() == 0)) {
			PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

			paymentRejectedResponse.setReason(LIMIT_EXCEEDED);
			paymentRejectedResponse.setReasonCode(AMOUNT_EXCEEDED);
			paymentRejectedResponse.setStatus(REJECTED);

			throw new LimitExceededException(paymentRejectedResponse, signatureCertificate);

		}

	}

}
