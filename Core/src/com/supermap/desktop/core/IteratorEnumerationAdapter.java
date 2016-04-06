package com.supermap.desktop.core;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author XiaJt
 */
public class IteratorEnumerationAdapter<E> implements Enumeration<E> {
	private Iterator<E> iterator;

	public IteratorEnumerationAdapter(Iterator<E> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasMoreElements() {
		return iterator != null && iterator.hasNext();
	}

	@Override
	public E nextElement() {
		if (iterator == null) {
			return null;
		}
		return iterator.next();
	}
}
