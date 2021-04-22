package com.validation.sandbox.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.validation.sandbox.api.exception.InvalidRequestException;
import com.validation.sandbox.api.model.PaymentInitiationRequest;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@ExtendWith(MockitoExtension.class)
public class PaymentRequestValidationServiceImplTests {

	@InjectMocks
	private PaymentRequestValidatonServiceImpl paymentRequestValidatonServiceImpl;

	private PaymentInitiationRequest paymentInitiationRequest = null;

	private String INVALID_REQUEST = "INVALID_REQUEST";

	private String REJECTED = "Rejected";

	private String REQUEST_VALIDATE = "Got rejected due to IBAN or Amount validation failed";

	@BeforeEach
	public void setUp() {

		paymentInitiationRequest = new PaymentInitiationRequest();

		paymentInitiationRequest.setAmount("1.00");
		paymentInitiationRequest.setCreditorIBAN("NL94ABNA1008270121");
		paymentInitiationRequest.setDebtorIBAN("NLNLRABO7134384551");
		INVALID_REQUEST = "INVALID_REQUEST";
		REJECTED = "Rejected";

		REQUEST_VALIDATE = "Got rejected due to IBAN or Amount validation failed";
	}

	@Test
	public void paymentValidationInvalidIBANRequestTest() {

		PaymentRejectedResponse expected = new PaymentRejectedResponse();

		expected.setReason(REQUEST_VALIDATE);
		expected.setReasonCode(INVALID_REQUEST);
		expected.setStatus(REJECTED);

		InvalidRequestException actual = assertThrows(InvalidRequestException.class,
				() -> paymentRequestValidatonServiceImpl.validatePaymentRequest(paymentInitiationRequest,
						"signatureCertificate"));

		assertEquals(expected, actual.getPaymentRejectedResponse());

	}

	@Test
	public void paymentValidationInvalidAmountRequestTest() {

		paymentInitiationRequest.setAmount("abc");

		paymentInitiationRequest.setDebtorIBAN("NL02RABO7134384551");

		PaymentRejectedResponse expected = new PaymentRejectedResponse();

		expected.setReason(REQUEST_VALIDATE);
		expected.setReasonCode(INVALID_REQUEST);
		expected.setStatus(REJECTED);

		InvalidRequestException actual = assertThrows(InvalidRequestException.class,
				() -> paymentRequestValidatonServiceImpl.validatePaymentRequest(paymentInitiationRequest,
						"signatureCertificate"));

		assertEquals(expected, actual.getPaymentRejectedResponse());

	}

	@Test
	public void paymentValidationInvalidAmountFormatTest() {

		paymentInitiationRequest.setAmount("100/000");

		paymentInitiationRequest.setDebtorIBAN("NL02RABO7134384551");

		PaymentRejectedResponse expected = new PaymentRejectedResponse();

		expected.setReason(REQUEST_VALIDATE);
		expected.setReasonCode(INVALID_REQUEST);
		expected.setStatus(REJECTED);

		InvalidRequestException actual = assertThrows(InvalidRequestException.class,
				() -> paymentRequestValidatonServiceImpl.validatePaymentRequest(paymentInitiationRequest,
						"signatureCertificate"));

		assertEquals(expected, actual.getPaymentRejectedResponse());

	}

	@Test
	public void paymentValidationInvalidIBANWithSpaceRequestTest() {

		paymentInitiationRequest.setDebtorIBAN("NL 02RABO71343 84551");

		PaymentRejectedResponse expected = new PaymentRejectedResponse();

		expected.setReason(REQUEST_VALIDATE);
		expected.setReasonCode(INVALID_REQUEST);
		expected.setStatus(REJECTED);

		InvalidRequestException actual = assertThrows(InvalidRequestException.class,
				() -> paymentRequestValidatonServiceImpl.validatePaymentRequest(paymentInitiationRequest,
						"signatureCertificate"));

		assertEquals(expected, actual.getPaymentRejectedResponse());

	}

	@Test
	public void validPaymentRequestTest() {

		PaymentRequestValidatonServiceImpl paymentRequestValidatonServiceImpl = mock(
				PaymentRequestValidatonServiceImpl.class);

		Mockito.lenient().doNothing().when(paymentRequestValidatonServiceImpl)
				.validatePaymentRequest(paymentInitiationRequest, "signatureCertificate");

	}

}
