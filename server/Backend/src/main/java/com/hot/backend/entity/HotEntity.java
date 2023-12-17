package com.hot.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class HotEntity {

	// カード名称
	private String name;

	// カード属性
	private String cardType;

	// タイプ
	private String type;

	// エクスパンション
	private String expansion;

	// コレクション
	@Id
	private String collection;	

	// レアリティ
	private String rarity;

	// プロモ有無
	private String promo;
	
	// 金額
	private Integer price;
}
