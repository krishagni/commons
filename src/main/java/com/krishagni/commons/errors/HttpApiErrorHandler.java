package com.krishagni.commons.errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.krishagni.commons.util.MessageUtil;

@ControllerAdvice
public class HttpApiErrorHandler extends ResponseEntityExceptionHandler {

	private static final String INTERNAL_ERROR = "internal_error";

	public HttpApiErrorHandler() {
		super();
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleOtherException(Exception exception, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<Map<String, String>> errorMsgs = new ArrayList<>();

		if (exception instanceof AppException) {
			AppException ae = (AppException) exception;
			status = getHttpStatus(ae.getErrorType());

			if (ae.getException() != null) {
				logger.error("Error handling request", ae.getException());

				if (CollectionUtils.isEmpty(ae.getErrors())) {
					errorMsgs.add(getMessage(INTERNAL_ERROR, null));
				}
			}

			for (ParameterizedError error : ae.getErrors()) {
				errorMsgs.add(getMessage(error.error(), error.params()));
			}
		} else {
			logger.error("Error handling request", exception);
			errorMsgs.add(getMessage(INTERNAL_ERROR, null));
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(exception, errorMsgs, headers, status, request);
	}

	private HttpStatus getHttpStatus(ErrorType type) {
		switch (type) {
			case SYSTEM_ERROR:
				return HttpStatus.INTERNAL_SERVER_ERROR;

			case USER_ERROR:
				return HttpStatus.BAD_REQUEST;

			case UNKNOWN_ERROR:
				return HttpStatus.INTERNAL_SERVER_ERROR;

			case NONE:
				return HttpStatus.OK;

			default:
				throw new RuntimeException("Unknown error type: " + type);
		}
	}

	private Map<String, String> getMessage(ErrorCode error, Object[] params) {
		return getMessage(error.code(), params);
	}

	private Map<String, String> getMessage(String code, Object[] params) {
		Map<String, String> error = new HashMap<>();
		error.put("code", code);
		error.put("message", MessageUtil.getInstance().getMessage(code.toLowerCase(), params));
		return error;
	}
}
