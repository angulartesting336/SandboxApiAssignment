package com.validation.sandbox.api.service.impl;

import java.io.ByteArrayInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.validation.sandbox.api.exception.InvalidSignatureException;
import com.validation.sandbox.api.model.PaymentInitiationRequest;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@Service
public class SignatureValidator {

	public void validateSignature(PaymentInitiationRequest paymentInitiationRequest, String signature,
			String signatureCertificate, String xRequestId) throws GeneralSecurityException {

		CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");

		X509Certificate certificate = (X509Certificate) certificateFactory
				.generateCertificate(new ByteArrayInputStream(signatureCertificate.getBytes()));

		Signature signatureObj = Signature.getInstance("SHA256withRSA");

		signatureObj.initVerify(certificate.getPublicKey());
		JSONObject json = new JSONObject(paymentInitiationRequest);

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(json.toString().getBytes());

		byte[] data = md.digest();
		signatureObj.update(xRequestId.getBytes());
		signatureObj.update(data);

		if (signatureObj.verify(Base64.decodeBase64(signature))) {
			PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

			paymentRejectedResponse.setReason("Got rejected due to signature  validation failure");
			paymentRejectedResponse.setReasonCode("INVALID_SIGNATURE");
			paymentRejectedResponse.setStatus("Rejected");

			throw new InvalidSignatureException(paymentRejectedResponse, signatureCertificate,signature,xRequestId);

		}

	}

}
