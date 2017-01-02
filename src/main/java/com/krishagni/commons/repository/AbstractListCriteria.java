package com.krishagni.commons.repository;

public abstract class AbstractListCriteria<T> {
	private static final int MAX_RESULTS = 100;

	private String searchTerm;

	private int startAt;

	private int maxResults = MAX_RESULTS;

	private boolean includeStats;

	public String searchTerm() {
		return searchTerm;
	}

	public T searchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
		return self();
	}

	public int startAt() {
		return startAt < 0 ? 0 : startAt;
	}

	public T startAt(int startAt) {
		this.startAt = startAt;
		return self();
	}

	public int maxResults() {
		return maxResults <= 0 ? MAX_RESULTS : maxResults;
	}

	public T maxResults(int maxResults) {
		this.maxResults = maxResults;
		return self();
	}

	public boolean includeStats() {
		return includeStats;
	}

	public T includeStats(boolean includeStats) {
		this.includeStats = includeStats;
		return self();
	}


	public abstract T self();
}
