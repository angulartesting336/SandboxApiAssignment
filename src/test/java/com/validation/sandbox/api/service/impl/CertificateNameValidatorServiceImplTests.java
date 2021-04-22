package com.validation.sandbox.api.service.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import com.google.common.io.Files;
import com.validation.sandbox.api.exception.InvalidCertificateException;
import com.validation.sandbox.api.model.PaymentRejectedResponse;

@ExtendWith(MockitoExtension.class)
public class CertificateNameValidatorServiceImplTests {

	@InjectMocks
	private CertificateNameValidatorServiceImpl certificateNameValidatorServiceImpl;

	private String UNKNOWN_CN = null;

	private String UNKNOWN_CERTIFICATE = null;

	private String REJECTED = null;

	@BeforeEach
	public void setUp() {
		UNKNOWN_CN = "UNKNOWN_CERTIFICATE";

		UNKNOWN_CERTIFICATE = "Got rejected due to Unknown Certificate CN";

		REJECTED = "Rejected";

	}

	@Test
	public void validCertificateNameTest() throws IOException, GeneralSecurityException {

		CertificateNameValidatorServiceImpl certificateNameValidatorServiceImpl = mock(
				CertificateNameValidatorServiceImpl.class);
		final ClassPathResource classPathResource = new ClassPathResource("SignatureCertificate.txt");
		String signatureCertificate = Files.toString(classPathResource.getFile(), Charset.defaultCharset());

		Mockito.lenient().doNothing().when(certificateNameValidatorServiceImpl)
				.validateCertificateName(signatureCertificate);
	}

	@Test
	public void invalidCertificateNameTest() throws CertificateException, IOException {

		PaymentRejectedResponse expected = new PaymentRejectedResponse();

		expected.setReason(UNKNOWN_CERTIFICATE);
		expected.setReasonCode(UNKNOWN_CN);
		expected.setStatus(REJECTED);

		final ClassPathResource classPathResource = new ClassPathResource("UnknownCertificate.txt");
		String unknownCertificate = Files.toString(classPathResource.getFile(), Charset.defaultCharset());

		InvalidCertificateException actual = assertThrows(InvalidCertificateException.class,
				() -> certificateNameValidatorServiceImpl.validateCertificateName(unknownCertificate));

		assertEquals(expected, actual.getPaymentRejectedResponse());
	}

}
