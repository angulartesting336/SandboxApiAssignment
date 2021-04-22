package com.validation.sandbox.api.exception.handler;

import java.security.GeneralSecurityException;

import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.validation.sandbox.api.controller.SandboxValidationController;
import com.validation.sandbox.api.exception.InvalidCertificateException;
import com.validation.sandbox.api.exception.InvalidRequestException;
import com.validation.sandbox.api.exception.InvalidSignatureException;
import com.validation.sandbox.api.exception.LimitExceededException;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class SandboxValidationExceptionHandler {

	@Autowired
	SandboxValidationController sandboxValidationController;

	String BAD_REQUEST = "BAD_REQUEST";

	String RSA_CONSTANT = "RSA";

	String SHA_CONSTANT = "SHA256withRSA";

	String REJECTED = "Rejected";

	String COMMON_ERROR = "Something went wrong on the application,please try again later";

	String GENERAL_ERROR = "GENERAL_ERROR";

	String UNKNOWN_CERTIFICATE = "Got rejected due to Unknown Certificate CN";

	String INVALID_SIGNATURE = "INVALID_SIGNATURE";

	String REQUEST_VALIDATE = "Got rejected due to IBAN or Amount validation failed";

	String LIMIT_EXCEEDED = "Got rejected due to Amount limit exceeded";

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleInvalidCertificateException(InvalidCertificateException ex)
			throws GeneralSecurityException {
		log.info(UNKNOWN_CERTIFICATE);

		HttpHeaders headers = new HttpHeaders();

		headers.add("Signature", new String(Base64
				.encodeBase64(sandboxValidationController.generateSignature(ex.getPaymentRejectedResponse()).sign())));
		headers.add("Signature-Certificate", ex.getSignatureCertificate());
		headers.add("X-Request-Id", "dsfv");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(ex.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleInvalidSignatureException(InvalidSignatureException ex)
			throws GeneralSecurityException {
		log.info(INVALID_SIGNATURE);
		HttpHeaders headers = new HttpHeaders();

		headers.add("Signature", new String(Base64
				.encodeBase64(sandboxValidationController.generateSignature(ex.getPaymentRejectedResponse()).sign())));
		headers.add("Signature-Certificate", ex.getSignatureCertificate());
		headers.add("X-Request-Id", "dsfv");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(ex.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleInvalidRequestException(InvalidRequestException ex)
			throws GeneralSecurityException {
		log.info(REQUEST_VALIDATE);
		HttpHeaders headers = new HttpHeaders();

		headers.add("Signature", new String(Base64
				.encodeBase64(sandboxValidationController.generateSignature(ex.getPaymentRejectedResponse()).sign())));
		headers.add("Signature-Certificate", ex.getSignatureCertificate());
		headers.add("X-Request-Id", "dsfv");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(ex.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleLimitExceededException(LimitExceededException ex)
			throws GeneralSecurityException {
		log.info(LIMIT_EXCEEDED);
		HttpHeaders headers = new HttpHeaders();

		headers.add("Signature", new String(Base64
				.encodeBase64(sandboxValidationController.generateSignature(ex.getPaymentRejectedResponse()).sign())));
		headers.add("Signature-Certificate", ex.getSignatureCertificate());
		headers.add("X-Request-Id", "dsfv");

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).headers(headers)
				.body(ex.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleGeneralException(Exception ex)
			throws GeneralSecurityException {
		log.info(COMMON_ERROR, ex);
		PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

		paymentRejectedResponse.setReason(COMMON_ERROR);
		paymentRejectedResponse.setReasonCode(GENERAL_ERROR);
		paymentRejectedResponse.setStatus(REJECTED);

		HttpHeaders headers = new HttpHeaders();

		headers.add("Signature", new String(
				Base64.encodeBase64(sandboxValidationController.generateSignature(paymentRejectedResponse).sign())));
		headers.add("X-Request-Id", "dsfv");

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(paymentRejectedResponse);
	}

}
