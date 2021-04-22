package com.validation.sandbox.api.service.impl;

import static org.junit.Assert.assertThrows;
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

@ExtendWith(MockitoExtension.class)
public class CertificateNameValidatorTest {

	@InjectMocks
	private CertificateNameValidator certificateNameValidator;

	@BeforeEach
	public void setUp() {

	}

	@Test
	public void validCertificateNameTest() throws IOException, GeneralSecurityException {

		CertificateNameValidator certificateNameValidator = mock(CertificateNameValidator.class);
		final ClassPathResource classPathResource = new ClassPathResource("SignatureCertificate.txt");
		String signatureCertificate = Files.toString(classPathResource.getFile(), Charset.defaultCharset());

		Mockito.lenient().doNothing().when(certificateNameValidator).validateCertificateName(signatureCertificate,
				"signature", "xRequestId");
	}

	@Test
	public void invalidCertificateNameTest() throws CertificateException, IOException {

		final ClassPathResource classPathResource = new ClassPathResource("UnknownCertificate.txt");
		String unknownCertificate = Files.toString(classPathResource.getFile(), Charset.defaultCharset());

		assertThrows(InvalidCertificateException.class,
				() -> certificateNameValidator.validateCertificateName(unknownCertificate, "signature", "xRequestId"));

	}

}
