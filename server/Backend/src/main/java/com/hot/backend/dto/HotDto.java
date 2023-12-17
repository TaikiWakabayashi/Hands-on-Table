package com.hot.backend.dto;

import com.hot.backend.entity.HotEntity;
import com.hot.backend.form.HotForm;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotDto {

	@Size(max=128)
	private String name;
	
	@Size(max=128)
	private String cardType;
	
	@Size(max=128)
	private String type;
	

	@Size(max=128)
	private String expansion;
	

	@Size(max=128)
	private String collection;
	

	@Size(max=4)
	private String rarity;
	
	@Size(max=2)
	private String promo;
	
	private Integer price;
	
	public static HotDto convertFormToDto(HotForm form) {
		HotDto hotDto = new HotDto();
		hotDto.setName(form.getName());
		hotDto.setCardType(form.getCardType());
		hotDto.setType(form.getType());
		hotDto.setExpansion(form.getExpansion());
		hotDto.setCollection(form.getCollection());
		hotDto.setRarity(form.getRarity());
		hotDto.setPromo(form.getPromo());	
		if (form.getPrice() != null) {
			hotDto.setPrice(form.getPrice());
		}
		return hotDto;
	}
	
	public static HotDto convertEntityToDto(HotEntity entity) {
		HotDto hotDto = new HotDto();
		hotDto.setName(entity.getName());
		hotDto.setCardType(entity.getCardType());
		hotDto.setType(entity.getType());
		hotDto.setExpansion(entity.getExpansion());
		hotDto.setCollection(entity.getCollection());
		hotDto.setRarity(entity.getRarity());
		hotDto.setPromo(entity.getPromo());
		hotDto.setPrice(entity.getPrice());
		return hotDto;
	}
	
	public static HotEntity convertDtoToEntity(HotDto dto) {
		HotEntity entity = new HotEntity();
		entity.setName(dto.getName());
		entity.setCardType(dto.getCardType());
		entity.setType(dto.getType());
		entity.setExpansion(dto.getExpansion());
		entity.setCollection(dto.getCollection());
		entity.setRarity(dto.getRarity());
		entity.setPromo(dto.getPromo());
		entity.setPrice(dto.getPrice());
		return entity;
	}
}
