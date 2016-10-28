package com.krishagni.commons.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MessageUtil {

	private static MessageUtil instance = null;

	@Autowired
	private MessageSource messageSource;

	public static MessageUtil getInstance() {
		if (instance == null) {
			instance = new MessageUtil();
		}

		return instance;
	}

	public String getBooleanMsg(Boolean value) {
		String bool = "common_no";
		if (value != null && value) {
			bool = "common_yes";
		}

		return messageSource.getMessage(bool, null, Locale.getDefault());
	}

	public String getMessage(String code) {
		return getMessage(code, null);
	}

	public String getMessage(String code, Object[] params) {
		return messageSource.getMessage(code, params, Locale.getDefault());
	}
}
