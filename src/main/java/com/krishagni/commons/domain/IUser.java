package com.krishagni.commons.domain;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUser extends UserDetails {
	Long getId();

	void setId(Long id);

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	String getLoginName();

	void setLoginName(String loginName);

	IInstitute getInstitute();

	void setInstitute(IInstitute institute);

	String getEmailAddress();

	void setEmailAddress(String emailAddress);

	String getDomainName();

	boolean isLocked();

	void lock();

	boolean isPasswordExpired();

	boolean isActive();
}
