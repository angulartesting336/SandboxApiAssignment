package com.validation.sandbox.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.validation.sandbox.api.exception.InvalidCertificateException;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@Service
public class CertificateNameValidatorServiceImpl {

	@Autowired
	SignatureVerificationServiceImpl signatureVerificationServiceImpl;

	String SANDBOX_CN = "Sandbox-TPP";

	String X509 = "X509";

	String UNKNOWN_CN = "UNKNOWN_CERTIFICATE";

	String CN = "CN=";

	String UNKNOWN_CERTIFICATE = "Got rejected due to Unknown Certificate CN";

	String REJECTED = "Rejected";

	public void validateCertificateName(String signatureCertificate) throws IOException, GeneralSecurityException {

		CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);

		X509Certificate certificate1 = (X509Certificate) certificateFactory
				.generateCertificate(new ByteArrayInputStream(signatureCertificate.getBytes()));

		if (!getCommonName(certificate1).startsWith(SANDBOX_CN)) {

			PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

			paymentRejectedResponse.setReason(UNKNOWN_CERTIFICATE);
			paymentRejectedResponse.setReasonCode(UNKNOWN_CN);
			paymentRejectedResponse.setStatus(REJECTED);

			throw new InvalidCertificateException(paymentRejectedResponse, signatureCertificate);
		}

	}

	private String getCommonName(X509Certificate certificate) {
		String name = certificate.getSubjectX500Principal().getName();
		int start = name.indexOf(CN);
		int end = name.indexOf(",", start);
		if (end == -1) {
			end = name.length();
		}
		return name.substring(start + 3, end);
	}

}
