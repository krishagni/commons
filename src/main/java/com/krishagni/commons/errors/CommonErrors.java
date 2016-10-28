package com.krishagni.commons.errors;

public enum CommonErrors implements ErrorCode {
	INVALID_REQUEST,

	DB_CONSTRAINT_VIOLATION,

	INVALID_STATUS,

	FILE_NOT_FOUND;

	@Override
	public String code() {
		return "COMMON_" + this.name();
	}
}
