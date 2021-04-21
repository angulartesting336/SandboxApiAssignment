package com.validation.sandbox.api.service;

import java.io.IOException;
import java.security.cert.CertificateException;

import com.validation.sandbox.api.model.PaymentInitiationRequest;
import com.validation.sandbox.api.model.ResponseDTO;

public interface SandboxValidationService {


	public ResponseDTO paymentValidationRequest(PaymentInitiationRequest paymentInitiationRequest, String signature,
			String signatureCertificate, String xRequestId) throws CertificateException,IOException;

}
