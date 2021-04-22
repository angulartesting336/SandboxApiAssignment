package com.validation.sandbox.api.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.validation.sandbox.api.exception.InvalidCertificateException;
import com.validation.sandbox.api.exception.InvalidRequestException;
import com.validation.sandbox.api.exception.InvalidSignatureException;
import com.validation.sandbox.api.exception.LimitExceededException;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice

public class SandboxValidationExceptionHandler extends ResponseEntityExceptionHandler {

	private HttpServletRequest httpServletRequest;

	@ExceptionHandler(InvalidCertificateException.class)
	public ResponseEntity<PaymentRejectedResponse> handleInvalidCertificateException(
			InvalidCertificateException exception, HttpHeaders headers1) {

		log.error(exception.toString());

		HttpHeaders headers = getHttpHeaders(exception.getSignature(), exception.getSignatureCertificate(),
				exception.getXRequestId());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers)
				.body(exception.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleInvalidSignatureException(
			InvalidSignatureException exception) {
		log.error(exception.toString());
		HttpHeaders headers = getHttpHeaders(exception.getSignature(), exception.getSignatureCertificate(),
				exception.getXRequestId());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers)
				.body(exception.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleInvalidRequestException(InvalidRequestException exception) {
		log.error(exception.toString());
		HttpHeaders headers = getHttpHeaders(exception.getSignature(), exception.getSignatureCertificate(),
				exception.getXRequestId());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers)
				.body(exception.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleLimitExceededException(LimitExceededException exception) {
		log.error(exception.toString());
		HttpHeaders headers = getHttpHeaders(exception.getSignature(), exception.getSignatureCertificate(),
				exception.getXRequestId());

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).headers(headers)
				.body(exception.getPaymentRejectedResponse());
	}

	@ExceptionHandler
	public ResponseEntity<PaymentRejectedResponse> handleGeneralException(Exception exception) {
		log.info(exception.toString());
		PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

		paymentRejectedResponse.setReason("Something went wrong on the application,please try again later");
		paymentRejectedResponse.setReasonCode("GENERAL_ERROR");
		paymentRejectedResponse.setStatus("Rejected");

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(paymentRejectedResponse);
	}

	private HttpHeaders getHttpHeaders(String signature, String signatureCertificate, String xRequestId) {

		HttpHeaders headers = new HttpHeaders();

		headers.add("Signature", signature);
		headers.add("Signature-Certificate", signatureCertificate);
		headers.add("X-Request-Id", xRequestId);

		return headers;

	}

}
