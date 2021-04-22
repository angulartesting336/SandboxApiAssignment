package com.validation.sandbox.api.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.validation.sandbox.api.model.PaymentAcceptedResponse;
import com.validation.sandbox.api.model.PaymentInitiationRequest;
import com.validation.sandbox.api.service.SandboxValidationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SandboxValidationServiceImpl implements SandboxValidationService {

	@Autowired
	CertificateNameValidatorServiceImpl certificateNameValidatorServiceImpl;

	@Autowired
	SignatureVerificationServiceImpl signatureVerificationServiceImpl;

	@Autowired
	PaymentRequestValidatonServiceImpl paymentRequestValidatonServiceImpl;

	@Autowired
	LimitExceededValidationServiceimpl limitExceededValidationServiceimpl;

	String ACCEPTED = "Accepted";


	@Override
	public PaymentAcceptedResponse paymentValidationRequest(PaymentInitiationRequest paymentInitiationRequest,
			String signature, String signatureCertificate, String xRequestId) throws IOException, GeneralSecurityException
			 {

		log.info("Certificate Name Validation begins");
		certificateNameValidatorServiceImpl.validateCertificateName(signatureCertificate);
		log.info("Successfully validated the certificate Name");
		
		log.info("Signature verification begins");
		signatureVerificationServiceImpl.validateSignature(paymentInitiationRequest, signature, signatureCertificate,
				xRequestId);
		log.info("Successfully verified the Signature ");
		
		log.info("Payment Request validation begins");
		paymentRequestValidatonServiceImpl.validatePaymentRequest(paymentInitiationRequest, signatureCertificate);
		log.info("Successfully validated the incoming payment request ");
		
		log.info("Checking whether provided amount limit is exceeded or not");
		limitExceededValidationServiceimpl.checkLimitExceeded(paymentInitiationRequest.getAmount(),
				paymentInitiationRequest.getDebtorIBAN(), signatureCertificate);
		log.info("Provided amount limit is not exceeded");
		
		PaymentAcceptedResponse paymentAcceptedResponse = new PaymentAcceptedResponse();
		paymentAcceptedResponse.setPaymentId((UUID.randomUUID()).toString());
		paymentAcceptedResponse.setStatus(ACCEPTED);

		return paymentAcceptedResponse;

	}

}
