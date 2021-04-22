package com.validation.sandbox.api.model;

import lombok.Data;

@Data
public class PaymentRejectedResponse {

	private String status;

	private String reason;

	private String reasonCode;

}
