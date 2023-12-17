package com.hot.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hot.backend.entity.HotEntity;

@Mapper
public interface HotMapper {
	// 検索結果取得
	HotEntity findPrice(HotEntity entity);
	
	// 同じExpansionを取得
	List<HotEntity> findExpansion(List<String> expansions);
	
	// リクエストのカードタイプのカードを取得
	List<HotEntity> selectCard(String cardType);
	
	// 登録
	boolean insert(HotEntity entity);
}
