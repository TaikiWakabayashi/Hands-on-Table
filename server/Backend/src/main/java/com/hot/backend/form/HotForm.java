package com.hot.backend.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotForm {
	@NotBlank
	@Size(max=128)
	private String name;
	
	@NotBlank
	@Size(max=128)
	private String cardType;
	
	@Size(max=128)
	private String type;
	
	@NotBlank
	@Size(max=128)
	private String expansion;
	
	@NotBlank
	@Size(max=128)
	private String collection;
	
	@NotBlank
	@Size(max=4)
	private String rarity;
	
	@Size(max=2)
	private String promo;
	
	private Integer price;
}
