package com.supermap.desktop.process.core;

/**
 * @author XiaJT
 */
public interface IReadyChecker<E> {
	boolean isReady(E object);
}
