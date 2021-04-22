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
public class SignatureVerificationServiceImpl {

	String INVALID_SIGNATURE = "INVALID_SIGNATURE";

	String SHA_CONSTANT = "SHA256withRSA";

	String REJECTED = "Rejected";

	String SHA_256 = "SHA-256";

	String SIGNATURE_VALIDATION = "Got rejected due to signature validation failed";

	String X509 = "X509";

	public void validateSignature(PaymentInitiationRequest paymentInitiationRequest, String signature,
			String signatureCertificate, String xRequestId) throws GeneralSecurityException {

		CertificateFactory cf1 = CertificateFactory.getInstance(X509);

		X509Certificate certificate = (X509Certificate) cf1
				.generateCertificate(new ByteArrayInputStream(signatureCertificate.getBytes()));

		Signature sign = Signature.getInstance(SHA_CONSTANT);

		sign.initVerify(certificate.getPublicKey());
		JSONObject json = new JSONObject(paymentInitiationRequest);

		MessageDigest md = MessageDigest.getInstance(SHA_256);

		md.update(json.toString().getBytes());

		byte[] data = md.digest();
		sign.update(xRequestId.getBytes());
		sign.update(data);

		if (sign.verify(Base64.decodeBase64(signature))) {
			PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();

			paymentRejectedResponse.setReason(SIGNATURE_VALIDATION);
			paymentRejectedResponse.setReasonCode(INVALID_SIGNATURE);
			paymentRejectedResponse.setStatus(REJECTED);

			throw new InvalidSignatureException(paymentRejectedResponse, signatureCertificate);

		}

	}

}
