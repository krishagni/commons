package com.krishagni.commons.service.impl;

import java.util.Locale;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.MessageSource;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.krishagni.commons.errors.AppException;
import com.krishagni.commons.service.TemplateService;

public class TemplateServiceImpl implements TemplateService {
	private VelocityEngine velocityEngine;

	private MessageSource messageSource;

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String render(String templateName, Map<String, Object> properties) {
		try {
			properties.put("locale", Locale.getDefault());
			properties.put("messageSource", messageSource);
			return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, properties);
		} catch (VelocityException ex) {
			throw AppException.serverError(ex);
		}
	}
}
