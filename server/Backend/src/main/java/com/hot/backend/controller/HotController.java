package com.hot.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hot.backend.dto.HotDto;
import com.hot.backend.form.ArrangeRequest;
import com.hot.backend.form.HotForm;
import com.hot.backend.service.HotService;
import com.hot.backend.status.Status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins="*")
public class HotController {
	
	@Autowired
	private final HotService hotService;
	
	@PostMapping("/hot")
	@ResponseBody
	public Map<String, List<HotDto>> findAll(@RequestBody(required = true)List<HotForm> request) {
		List<HotDto> dto = request.stream().map(HotDto::convertFormToDto).collect(Collectors.toList());
		return hotService.findPrice(dto);
	}
	
	@PostMapping("/hot/selectCard")
	@ResponseBody
	public List<HotDto> selectCard(@RequestBody(required = true)ArrangeRequest request) {
		return hotService.selectCard(request.getSelectCardType());
	}
	
	@PostMapping("/hot/add")
	@ResponseBody
	public Status insert(@RequestBody(required = true)List<HotForm> request) {
		List<HotDto> dto = request.stream().map(HotDto::convertFormToDto).collect(Collectors.toList());
		 return hotService.insert(dto);
	}
}
