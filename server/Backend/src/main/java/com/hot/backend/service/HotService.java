package com.hot.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hot.backend.dto.HotDto;
import com.hot.backend.entity.HotEntity;
import com.hot.backend.mapper.HotMapper;
import com.hot.backend.status.Status;
import com.hot.backend.status.StatusType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class HotService {
	
	@Autowired
	private final HotMapper hotMapper;
	
	public Map<String, List<HotDto>> findPrice(List<HotDto> dto) {
		
		List<HotEntity> resultEntity = new ArrayList<>();
		List<String> expansionList = new ArrayList<>();
		// リクエストのDTOをEntityに変換
		List<HotEntity> entitysMap = dto.stream().map(HotDto::convertDtoToEntity).collect(Collectors.toList());
		
		entitysMap.forEach(entity -> {
			
			String expansion = entity.getExpansion();
			
			// expansionを抽出
			expansionList.add(expansion);
			// リクエスト値を元に金額を取得
			HotEntity result = this.hotMapper.findPrice(entity);
			if(result == null) {
				// 取得結果が0件の場合、リクエスト値をそのままセット
				resultEntity.add(entity);
			} else {
				resultEntity.add(result);
			}
		});
		
		// 抽出したexpansionを一意のリストに詰め替え
		List<String> primaryExpansionList = new ArrayList<String>(new LinkedHashSet<>(expansionList));
		// 一意リストを元にリクエスト値と同じexpansionを持つデータを取得
		List<HotEntity> resultExpansionList = this.hotMapper.findExpansion(primaryExpansionList);
		
		// 金額の取得結果をEntityからDTOに変換
		List<HotDto> resultPriceDto = this.changeDto(resultEntity);
		// 同じexpansionの取得結果をEntityからDTOに変換
		List<HotDto> resultExpansionDto = this.changeDto(resultExpansionList);
		
		Map<String, List<HotDto>> resultMap = new HashMap<>();
		resultMap.put("resultPrice", resultPriceDto);
		resultMap.put("resultExpansion", resultExpansionDto);
		
		return resultMap;
	}
	
	public List<HotDto> selectCard(String cardType) {
		
		List<HotEntity> resultEntity = new ArrayList<>();
		
		resultEntity = this.hotMapper.selectCard(cardType);
		
		List<HotDto> resultDto = this.changeDto(resultEntity);
		return resultDto;
	}
	
	public Status insert(List<HotDto> dto) {
		// リクエストのDTOをEntityに変換
		List<HotEntity> entitysMap = dto.stream().map(HotDto::convertDtoToEntity).collect(Collectors.toList());
		
		// レスポンスステータス
		Status status = new Status();
		
		entitysMap.forEach(entity -> {
			// リクエスト値を元に金額を取得
			boolean result = this.hotMapper.insert(entity);
			if (result) {
				status.setStatusType(StatusType.SUCCESS);
			} else {
				status.setStatusType(StatusType.ABNORMAL);
			}
		});
		return status;
	}
	
	private List<HotDto> changeDto (List<HotEntity> entityList) {
		return entityList.stream().map(HotDto::convertEntityToDto).collect(Collectors.toList());
	}
}
