/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */

// $Id: MapAdapter.java,v 1.6 2005/02/16 11:28:13 jesper Exp $
package net.infonode.util.collection.map;

import net.infonode.util.collection.map.base.ConstMapIterator;
import net.infonode.util.collection.map.base.Map;
import net.infonode.util.collection.map.base.MapIterator;

import java.util.HashMap;

public class MapAdapter implements Map {
	private static class Iterator implements MapIterator {
		private java.util.Iterator iteratorTemp;
		private java.util.Map.Entry nextEntry;

		Iterator(java.util.Iterator iterator) {
			this.iteratorTemp = iterator;
			next();
		}

		@Override
		public void remove() {
			iteratorTemp.remove();
		}

		@Override
		public boolean atEntry() {
			return nextEntry != null;
		}

		@Override
		public Object getKey() {
			return nextEntry.getKey();
		}

		@Override
		public Object getValue() {
			return nextEntry.getValue();
		}

		@Override
		public void next() {
			nextEntry = iteratorTemp.hasNext() ? (java.util.Map.Entry) iteratorTemp.next() : null;
		}
	}

	private HashMap map;

	public MapAdapter() {
		// do nothing
	}

	public MapAdapter(HashMap map) {
		this.map = map;
	}

	@Override
	public Object put(Object key, Object value) {
		if (map == null)
			map = new HashMap(4);

		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map == null ? null : map.remove(key);
	}

	@Override
	public void clear() {
		if (map != null)
			map.clear();
	}

	@Override
	public MapIterator iterator() {
		return map == null ? (MapIterator) EmptyIterator.INSTANCE : (MapIterator) new Iterator(map.entrySet().iterator());
	}

	@Override
	public Object get(Object key) {
		return map == null ? null : map.get(key);
	}

	@Override
	public boolean containsKey(Object key) {
		return map != null && map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map != null && map.containsValue(value);
	}

	@Override
	public boolean isEmpty() {
		return map == null || map.isEmpty();
	}

	@Override
	public ConstMapIterator constIterator() {
		return iterator();
	}

	public int size() {
		return map == null ? 0 : map.size();
	}
}
