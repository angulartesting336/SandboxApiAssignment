package com.validation.sandbox.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;

import com.validation.sandbox.api.exception.InvalidCertificateException;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@Service
public class CertificateNameValidator {

	public void validateCertificateName(String signatureCertificate,String signature,String xRequestId) throws IOException, GeneralSecurityException {

		CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");

		X509Certificate certificate = (X509Certificate) certificateFactory
				.generateCertificate(new ByteArrayInputStream(signatureCertificate.getBytes()));
		

		if (!getCommonName(certificate).startsWith("Sandbox-TPP")) {

			PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

			paymentRejectedResponse.setReason("Got rejected due to Unknown Certificate CN");
			paymentRejectedResponse.setReasonCode("UNKNOWN_CERTIFICATE");
			paymentRejectedResponse.setStatus("Rejected");

			throw new InvalidCertificateException(paymentRejectedResponse, signatureCertificate,signature,xRequestId);
		}

	}

	private String getCommonName(X509Certificate certificate) {
		String name = certificate.getSubjectX500Principal().getName();
		int start = name.indexOf("CN=");
		int end = name.indexOf(",", start);
		if (end == -1) {
			end = name.length();
		}
		return name.substring(start + 3, end);
	}

}
