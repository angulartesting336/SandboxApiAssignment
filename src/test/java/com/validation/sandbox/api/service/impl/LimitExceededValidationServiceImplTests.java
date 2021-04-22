package com.validation.sandbox.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.validation.sandbox.api.exception.LimitExceededException;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@ExtendWith(MockitoExtension.class)
public class LimitExceededValidationServiceImplTests {

	@InjectMocks
	private LimitExceededValidationServiceimpl limitExceededValidationServiceimpl;

	private String amount = null;

	private String debtorIban = null;

	private String AMOUNT_EXCEEDED = null;

	private String LIMIT_EXCEEDED = null;

	private String REJECTED = null;

	@BeforeEach
	public void setUp() {

		amount = "100.00";

		debtorIban = "NL02RABO7134384112";

		AMOUNT_EXCEEDED = "LIMIT_EXCEEDED";

		LIMIT_EXCEEDED = "Got rejected due to Amount limit exceeded";

		REJECTED = "Rejected";

	}

	@Test
	public void paymentValidationLimitExceededTest() {

		PaymentRejectedResponse expected = new PaymentRejectedResponse();

		expected.setReason(LIMIT_EXCEEDED);
		expected.setReasonCode(AMOUNT_EXCEEDED);
		expected.setStatus(REJECTED);

		LimitExceededException actual = assertThrows(LimitExceededException.class,
				() -> limitExceededValidationServiceimpl.checkLimitExceeded(amount, debtorIban,
						"signatureCertificate"));

		assertEquals(expected, actual.getPaymentRejectedResponse());
	}

	@Test
	public void paymentValidationLimitNotExceededTest() {

		debtorIban = "NL02RABO7134384113";

		LimitExceededValidationServiceimpl limitExceededValidationServiceimpl = mock(
				LimitExceededValidationServiceimpl.class);

		Mockito.lenient().doNothing().when(limitExceededValidationServiceimpl).checkLimitExceeded(amount, debtorIban,
				"signatureCertificate");

	}

}
