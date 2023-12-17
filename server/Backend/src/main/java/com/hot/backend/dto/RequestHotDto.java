package com.hot.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestHotDto {
	@NotBlank
	@Size(max=128)
	private String name;
		
	@NotBlank
	@Size(max=128)
	private String expansion;
	
	@NotBlank
	@Size(max=128)
	private String collection;
	
	@NotBlank
	@Size(max=4)
	private String rarity;
}
