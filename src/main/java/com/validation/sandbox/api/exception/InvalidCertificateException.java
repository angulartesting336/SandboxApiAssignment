package com.validation.sandbox.api.exception;

import com.validation.sandbox.api.model.PaymentRejectedResponse;

public class InvalidCertificateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private PaymentRejectedResponse paymentRejectedResponse;

	private String signatureCertificate;

	public InvalidCertificateException(PaymentRejectedResponse paymentRejectedResponse, String signatureCertificate) {
		super();
		this.paymentRejectedResponse = paymentRejectedResponse;
		this.signatureCertificate = signatureCertificate;
	}

	public PaymentRejectedResponse getPaymentRejectedResponse() {
		return paymentRejectedResponse;
	}

	public String getSignatureCertificate() {
		return signatureCertificate;
	}

}
