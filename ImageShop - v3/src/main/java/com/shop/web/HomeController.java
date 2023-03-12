package com.shop.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping(value = {"/", "/home"})
	public String home(Locale locale, Model model) {
		LocalDateTime currentTime = LocalDateTime.now();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분 s초");
		
		String formattedTime = currentTime.format(formatter);
		
		model.addAttribute("serverTime", formattedTime);
		
		return "home";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
}


/*
 *
 * 	CREATE TABLE `code_group` (
		`group_code` CHAR(3) NOT NULL,
		`group_name` VARCHAR(30) NOT NULL COLLATE 'utf8_general_ci',
		`use_yn` CHAR(1) DEFAULT 'Y' COLLATE 'utf8_general_ci',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`group_code`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	
	CREATE TABLE `code_detail` (
		`group_code` CHAR(3) NOT NULL,
		`code_value` VARCHAR(3) NOT NULL COLLATE 'utf8_general_ci',
		`code_name` VARCHAR(30) NOT NULL COLLATE 'utf8_general_ci',
		`sort_seq` BIGINT NOT NULL,
		`use_yn` CHAR(1) DEFAULT 'Y',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`group_code`, `code_value`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	
	CREATE TABLE `member` (
		`user_no` BIGINT NOT NULL AUTO_INCREMENT,
		`user_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`user_pw` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
		`user_name` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
		`job` VARCHAR(3) DEFAULT '00',
		`coin` BIGINT DEFAULT 0,
	`created_date` DATETIME NULL DEFAULT NULL,
	`modified_date` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`user_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	
	CREATE TABLE `member_auth` (
		`user_auth_no` BIGINT NOT NULL AUTO_INCREMENT,
		`user_no` BIGINT NOT NULL,
		`auth` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`user_auth_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;

	ALTER TABLE `member_auth` ADD CONSTRAINT `member_auth_fk` FOREIGN KEY(`user_no`) REFERENCES imageshop.member(user_no);


	CREATE TABLE `board` (
		`board_no` BIGINT NOT NULL AUTO_INCREMENT,
		`title` VARCHAR(200) NOT NULL COLLATE 'utf8_general_ci',
		`content` VARCHAR(2000) COLLATE 'utf8_general_ci',
		`writer` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`board_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	
	CREATE TABLE `notice` (
		`notice_no` BIGINT NOT NULL AUTO_INCREMENT,
		`title` VARCHAR(200) NOT NULL COLLATE 'utf8_general_ci',
		`content` VARCHAR(2000) COLLATE 'utf8_general_ci',
		`writer` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`notice_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	CREATE TABLE `item` (
		`item_id` BIGINT NOT NULL AUTO_INCREMENT,
		`item_name` VARCHAR(20) NOT NULL COLLATE 'utf8_general_ci',
	    `price` BIGINT,
	    `description` VARCHAR(50),
	    `picture_url` VARCHAR(200),
	    `preview_url` VARCHAR(200),
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`user_item_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	
	CREATE TABLE `user_item` (
		`user_item_no` BIGINT NOT NULL AUTO_INCREMENT,
		`user_no` BIGINT NOT NULL,
	   `item_id` BIGINT NOT NULL,
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`user_item_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	CREATE TABLE `pds` (
		`item_id` BIGINT NOT NULL AUTO_INCREMENT,
		`item_name` VARCHAR(20) NOT NULL COLLATE 'utf8_general_ci',
		`view_cnt` BIGINT DEFAULT 0,
		`description` VARCHAR(50),
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`item_id`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;


	CREATE TABLE `pds_file` (
		`pds_file_id` BIGINT NOT NULL AUTO_INCREMENT,
		`full_name` VARCHAR(150) NOT NULL COLLATE 'utf8_general_ci',
		`item_id` BIGINT NOT NULL,
		`down_cnt` BIGINT DEFAULT 0,
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`pds_file_id`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	CREATE TABLE `charge_coin_history` (
		`history_no` BIGINT NOT NULL AUTO_INCREMENT,
		`user_no` BIGINT NOT NULL,
		`amount` BIGINT DEFAULT 0,
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`history_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	
	CREATE TABLE `pay_coin_history` (
		`history_no` BIGINT NOT NULL AUTO_INCREMENT,
		`user_no` BIGINT NOT NULL,
		`item_id` BIGINT NOT NULL,
		`amount` BIGINT NOT NULL,
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`history_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;

	CREATE TABLE `access_log` (
		`log_no` BIGINT NOT NULL AUTO_INCREMENT,
		`request_uri` VARCHAR(200) NOT NULL COLLATE 'utf8_general_ci',
		`class_name` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
		`class_simple_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`method_name` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
		`remote_addr` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`log_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;
	
	CREATE TABLE `performance_log` (
		`log_no` BIGINT(20) NOT NULL AUTO_INCREMENT,
		`signature_name` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`signature_type_name` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
		`duration_time`  BIGINT(20) NULL DEFAULT '0',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		PRIMARY KEY (`log_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
	;

 * 
 */


/*
 *  
 *  CREATE TABLE `member` (
		`user_no` BIGINT(20) NOT NULL AUTO_INCREMENT,
		`user_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
		`user_pw` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
		`user_name` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
		`job` VARCHAR(3) NULL DEFAULT '00' COLLATE 'utf8_general_ci',
		`coin` BIGINT(20) NULL DEFAULT '0',
		`created_date` DATETIME NULL DEFAULT NULL,
		`modified_date` DATETIME NULL DEFAULT NULL,
		`enabled` TINYINT(1) NULL DEFAULT '0',
		PRIMARY KEY (`user_no`) USING BTREE
	)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB; 
 * 
 */
 