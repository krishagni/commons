package com.krishagni.commons.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.proxy.HibernateProxyHelper;

public class BaseEntity {
	private Long id;

	protected transient List<Runnable> onSaveProcs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Runnable> getOnSaveProcs() {
		return onSaveProcs;
	}

	public void setOnSaveProcs(List<Runnable> onSaveProcs) {
		this.onSaveProcs = onSaveProcs;
	}

	public void addOnSaveProc(Runnable proc) {
		onSaveProcs.add(proc);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != HibernateProxyHelper.getClassWithoutInitializingProxy(o)) {
			return false;
		}


		BaseEntity that = (BaseEntity) o;
		return getId() != null && getId().equals(that.getId());

	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
