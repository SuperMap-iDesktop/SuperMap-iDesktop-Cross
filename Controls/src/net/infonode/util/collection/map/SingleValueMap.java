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

// $Id: SingleValueMap.java,v 1.3 2005/02/16 10:21:02 johan Exp $
package net.infonode.util.collection.map;

import net.infonode.util.Utils;
import net.infonode.util.collection.map.base.ConstMap;
import net.infonode.util.collection.map.base.ConstMapIterator;

/**
 * @author $Author: johan $
 * @version $Revision: 1.3 $
 */
public class SingleValueMap implements ConstMap {
	private Object key;
	private Object value;

	public SingleValueMap(Object key, Object value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public Object get(Object key) {
		return Utils.equalsMethod(key, this.key) ? value : null;
	}

	@Override
	public boolean containsKey(Object key) {
		return Utils.equalsMethod(key, this.key);
	}

	@Override
	public boolean containsValue(Object value) {
		return Utils.equalsMethod(value, this.value);
	}

	@Override
	public ConstMapIterator constIterator() {
		return new ConstMapIterator() {
			private boolean done;

			@Override
			public Object getKey() {
				return key;
			}

			@Override
			public Object getValue() {
				return value;
			}

			@Override
			public void next() {
				done = true;
			}

			@Override
			public boolean atEntry() {
				return !done;
			}
		};
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
