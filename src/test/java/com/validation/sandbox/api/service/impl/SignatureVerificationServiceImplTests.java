package com.validation.sandbox.api.service.impl;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import com.google.common.io.Files;
import com.validation.sandbox.api.model.PaymentInitiationRequest;

@ExtendWith(MockitoExtension.class)
public class SignatureVerificationServiceImplTests {

	private PaymentInitiationRequest paymentInitiationRequest = null;

	private String signature = null;

	private String xRequestId = null;

	@BeforeEach
	public void setUp() {

		paymentInitiationRequest = new PaymentInitiationRequest();

		paymentInitiationRequest.setAmount("1.00");
		paymentInitiationRequest.setCreditorIBAN("NL94ABNA1008270121");
		paymentInitiationRequest.setDebtorIBAN("NL02RABO7134384551");

		signature = "AlFr/WbYiekHmbB6XdEO/7ghKd0n6q/bapENAYsL86KoYHqa4eP34xfH9icpQRmTpH0qOkt1vfUPWnaqu+vHBWx/gJXiuVlhayxLZD2w41q8ITkoj4oRLn2U1q8cLbjUtjzFWX9TgiQw1iY0ezpFqyDLPU7+ZzO01JI+yspn2gtto0XUm5KuxUPK24+xHD6R1UZSCSJKXY1QsKQfJ+gjzEjrtGvmASx1SUrpmyzVmf4qLwFB1ViRZmDZFtHIuuUVBBb835dCs2W+d7a+icGOCtGQbFcHvW0FODibnY5qq8v5w/P9i9PSarDaGgYb+1pMSnF3p8FsHAjk3Wccg2a1GQ==";

		xRequestId = "29318e25-cebd-498c-888a-f77672f66449";

	}

	@Test
	public void verifiedSignatureTest() throws CertificateException, GeneralSecurityException, IOException {

		SignatureVerificationServiceImpl signatureVerificationServiceImpl = mock(
				SignatureVerificationServiceImpl.class);

		final ClassPathResource classPathResource = new ClassPathResource("signatureCertificate.txt");
		String signatureCertificate = Files.toString(classPathResource.getFile(), Charset.defaultCharset());

		Mockito.lenient().doNothing().when(signatureVerificationServiceImpl).validateSignature(paymentInitiationRequest,
				signature, signatureCertificate, xRequestId);
	}

}
