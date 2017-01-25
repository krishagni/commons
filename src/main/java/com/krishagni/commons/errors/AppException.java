package com.krishagni.commons.errors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.JDBCException;

import com.krishagni.commons.util.MessageUtil;

public class AppException extends RuntimeException {
	private static final long serialVersionUID = -1473557909717365251L;

	private ErrorType errorType = ErrorType.NONE;

	private List<ParameterizedError> errors = new ArrayList<>();

	private Throwable exception;

	private Long exceptionId;

	public AppException(ErrorType type, ErrorCode error, Object ... params) {
		this.errorType = type;
		errors.add(new ParameterizedError(error, params));
	}

	public AppException(ErrorType type) {
		this.errorType = type;
	}

	public AppException(Throwable t) {
		this(null, t);
	}

	public AppException(AppException other) {
		errorType = other.getErrorType();
		errors.addAll(other.getErrors());
		exception = other.getException();
		exceptionId = other.getExceptionId();
	}

	public AppException(Long exceptionId, Throwable t) {
		this.exceptionId = exceptionId;
		this.errorType = ErrorType.SYSTEM_ERROR;
		this.exception = t;

		if (t instanceof JDBCException) {
			JDBCException je = (JDBCException)t;
			String dbMsg = je.getCause().getMessage();
			if (StringUtils.isBlank(dbMsg)) {
				dbMsg = je.getSQLException().getMessage();
			}

			errors.add(new ParameterizedError(CommonErrorCode.SQL_EXCEPTION, dbMsg));
		}
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public List<ParameterizedError> getErrors() {
		return errors;
	}

	public Throwable getException() {
		return exception;
	}

	public void addError(ErrorCode error, Object ... params) {
		errors.add(new ParameterizedError(error, params));
	}

	public void addErrors(List<ParameterizedError> errors) {
		this.errors.addAll(errors);
	}

	public boolean hasAnyErrors() {
		return !this.errors.isEmpty() || exception != null;
	}

	public void checkAndThrow() {
		if (hasAnyErrors()) {
			throw this;
		}
	}

	public boolean containsError(ErrorCode error) {
		if (CollectionUtils.isEmpty(errors)) {
			return false;
		}

		return errors.stream().anyMatch(pe -> pe.error().equals(error));
	}

	public void rethrow(ErrorCode oldError, ErrorCode newError, Object ... params) {
		if (containsError(oldError)) {
			throw AppException.userError(newError, params);
		}
		throw this;
	}

	public String getMessage() {
		StringBuilder errorMsg = new StringBuilder();

		if (CollectionUtils.isNotEmpty(errors)) {
			for (ParameterizedError pe : errors) {
				errorMsg.append(getMessage(pe)).append(", ");
			}
			errorMsg.delete(errorMsg.length() - 2, errorMsg.length());
		} else if (exception != null) {
			errorMsg.append(exception.getMessage());
		} else {
			errorMsg.append(MessageUtil.getInstance().getMessage("internal_error"));
		}

		return errorMsg.toString();
	}

	public Long getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(Long exceptionId) {
		this.exceptionId = exceptionId;
	}

	public static AppException userError(ErrorCode error, Object ... params) {
		return new AppException(ErrorType.USER_ERROR, error, params);
	}

	public static AppException serverError(ErrorCode error, Object ... params) {
		return new AppException(ErrorType.SYSTEM_ERROR, error, params);
	}

	public static AppException serverError(Throwable e) {
		return new AppException(e);
	}

	public static AppException serverError(Long exceptionId, Throwable e) {
		return new AppException(exceptionId, e);
	}

	public static <T> T raiseError(Throwable e) {
		AppException ae = (e instanceof AppException) ? (AppException) e : serverError(e);
		ae.checkAndThrow();
		return null; // never comes here
	}

	private String getMessage(ParameterizedError error) {
		return MessageUtil.getInstance().getMessage(error.error().code().toLowerCase(), error.params());
	}
}
