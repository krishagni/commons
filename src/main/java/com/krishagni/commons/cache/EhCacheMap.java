package com.krishagni.commons.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhCacheMap<K, V> implements Map<K, V> {
	private Ehcache cache;

	public EhCacheMap() {
		this(10000);
	}

	public EhCacheMap(int maxInMemoryElements) {
		this.cache = EhCacheManager.getInstance().newCache(maxInMemoryElements);
	}

	@Override
	public int size() {
		return cache.getSize();
	}

	@Override
	public boolean isEmpty() {
		return cache.getSize() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		Element element = cache.get(key);
		if (element != null) {
			return (V) element.getObjectValue();
		} else {
			return null;
		}
	}

	@Override
	public V put(K key, V value) {
		V prevVal = get(key);
		cache.put(new Element(key, value));
		return prevVal;
	}

	@Override
	public V remove(Object key) {
		V prevVal = get(key);
		cache.remove(key);
		return prevVal;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		cache.removeAll();
	}

	@Override
	public Set<K> keySet() {
		return new HashSet<K>(cache.getKeys());
	}

	@Override
	public Collection<V> values() {
		List<V> values = new ArrayList<V>();
		for (Object key : cache.getKeys()) {
			Element element = cache.get(key);
			values.add((V)element.getObjectValue());
		}

		return values;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		final Set<Entry<K, V>> values = new HashSet<Entry<K, V>>();
		for (Object key : cache.getKeys()) {
			Element element = cache.get(key);
			values.add(new EhCacheMapEntry((K) key));
		}

		return values;
	}

	public void destroy() {
		cache.removeAll();
		EhCacheManager.getInstance().removeCache(cache);
		cache = null;
	}

	private class EhCacheMapEntry implements Entry<K, V> {

		private final K key;

		public EhCacheMapEntry(K key) {
			this.key = key;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			Element element = cache.get(key);
			return element != null ? (V) element.getObjectValue() : null;
		}

		@Override
		public V setValue(V value) {
			Element element = cache.get(key);
			V prevVal = element != null ? (V) element.getObjectValue(): null;
			cache.put(new Element(key, value));
			return prevVal;
		}
	}
}
