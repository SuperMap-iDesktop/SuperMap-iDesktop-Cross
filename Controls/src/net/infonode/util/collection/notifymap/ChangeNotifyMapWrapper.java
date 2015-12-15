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

// $Id: ChangeNotifyMapWrapper.java,v 1.3 2004/07/06 15:08:44 jesper Exp $
package net.infonode.util.collection.notifymap;

import net.infonode.util.ValueChange;
import net.infonode.util.collection.map.MapAdapter;
import net.infonode.util.collection.map.base.ConstMapIterator;
import net.infonode.util.collection.map.base.Map;
import net.infonode.util.collection.map.base.MapIterator;

public class ChangeNotifyMapWrapper extends AbstractChangeNotifyMap {
	private class Iterator implements MapIterator {
		private MapIterator iteratorTemp;

		public Iterator(MapIterator iterator) {
			this.iteratorTemp = iterator;
		}

		@Override
		public void remove() {
			iteratorTemp.remove();
			fireEntryRemoved(iteratorTemp.getKey(), iteratorTemp.getValue());
		}

		@Override
		public Object getKey() {
			return iteratorTemp.getKey();
		}

		@Override
		public Object getValue() {
			return iteratorTemp.getValue();
		}

		@Override
		public void next() {
			iteratorTemp.next();
		}

		@Override
		public boolean atEntry() {
			return iteratorTemp.atEntry();
		}
	}

	private Map map;

	public ChangeNotifyMapWrapper(Map map) {
		this.map = map;
	}

	public Map getMap() {
		return map;
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Object put(Object key, Object value) {
		Object oldValue = map.put(key, value);
		fireEntryChanged(key, oldValue, value);
		return oldValue;
	}

	@Override
	public Object remove(Object key) {
		Object oldValue = map.remove(key);
		fireEntryRemoved(key, oldValue);
		return oldValue;
	}

	@Override
	public void clear() {
		MapAdapter changeMap = new MapAdapter();

		for (ConstMapIterator iterator = map.constIterator(); iterator.atEntry(); iterator.next()) {
			changeMap.put(iterator.getKey(), new ValueChange(iterator.getValue(), null));
		}

		map.clear();
		fireEntriesChanged(changeMap);
	}

	@Override
	public MapIterator iterator() {
		return new Iterator(map.iterator());
	}
}
