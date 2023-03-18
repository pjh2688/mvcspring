package com.shop.service.pds;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.mapper.pds.IPdsMapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Transactional
@Service
public class PdsService {

	private final IPdsMapper pdsMapper;
}
