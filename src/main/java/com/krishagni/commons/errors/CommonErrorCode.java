package com.krishagni.commons.errors;

public enum CommonErrorCode implements ErrorCode {
	INVALID_REQUEST,

	SQL_EXCEPTION,

	FILE_NOT_FOUND,

	EXCEPTION_NOT_FOUND,

	INVALID_STATUS;

	@Override
	public String code() {
		return "COMMON_" + this.name();
	}
}
