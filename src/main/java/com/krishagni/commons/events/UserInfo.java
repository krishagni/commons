package com.krishagni.commons.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.commons.domain.IInstitute;
import com.krishagni.commons.domain.IUser;

public class UserInfo {
	private Long id;

	private String firstName;

	private String lastName;

	private String loginName;

	private String emailAddress;

	private String instituteName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public static UserInfo from(IUser user) {
		UserInfo info = new UserInfo();
		info.setId(user.getId());
		info.setFirstName(user.getFirstName());
		info.setLastName(user.getLastName());
		info.setEmailAddress(user.getEmailAddress());
		info.setLoginName(user.getLoginName());
		info.setInstituteName(user.getInstitute() != null ? user.getInstitute().getName() : null);
		return info;
	}

	public static List<UserInfo> from(Collection<IUser> users) {
		return users.stream().map(UserInfo::from).collect(Collectors.toList());
	}
}
