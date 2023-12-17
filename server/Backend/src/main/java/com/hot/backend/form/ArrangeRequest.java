package com.hot.backend.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArrangeRequest {
	@NotBlank
	@Size(max=128)
	private String selectCardType;
}
