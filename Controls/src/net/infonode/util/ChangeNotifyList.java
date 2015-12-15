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

// $Id: ChangeNotifyList.java,v 1.4 2005/02/16 11:28:14 jesper Exp $
package net.infonode.util;

import java.util.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 */
abstract public class ChangeNotifyList implements List {
	private List list;

	abstract protected void changed();

	protected ChangeNotifyList() {
		this(new ArrayList(2));
	}

	protected ChangeNotifyList(List list) {
		this.list = list;
	}

	protected List getList() {
		return list;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public Object get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection c) {
		return list.containsAll(c);
	}

	@Override
	public Iterator iterator() {
		return listIterator();
	}

	@Override
	public ListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator listIterator(int index) {
		return new ChangeIterator(list.listIterator(index));
	}

	@Override
	public Object[] toArray(Object[] a) {
		return list.toArray(a);
	}

	@Override
	public void clear() {
		list.clear();
		changed();
	}

	@Override
	public Object remove(int index) {
		Object result = list.remove(index);
		changed();
		return result;
	}

	@Override
	public void add(int index, Object element) {
		list.add(index, element);
		changed();
	}

	@Override
	public boolean add(Object o) {
		if (list.add(o)) {
			changed();
			return true;
		} else
			return false;
	}

	@Override
	public boolean remove(Object o) {
		if (list.remove(o)) {
			changed();
			return true;
		} else
			return false;
	}

	@Override
	public boolean addAll(int index, Collection c) {
		if (list.addAll(index, c)) {
			changed();
			return true;
		} else
			return false;
	}

	@Override
	public boolean addAll(Collection c) {
		if (list.addAll(c)) {
			changed();
			return true;
		} else
			return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		if (list.removeAll(c)) {
			changed();
			return true;
		} else
			return false;
	}

	@Override
	public boolean retainAll(Collection c) {
		if (list.retainAll(c)) {
			changed();
			return true;
		} else
			return false;
	}

	@Override
	public List subList(int fromIndex, int toIndex) {
		return new ChangeNotifyList(list.subList(fromIndex, toIndex)) {
			@Override
			protected void changed() {
				ChangeNotifyList.this.changed();
			}
		};
	}

	@Override
	public Object set(int index, Object element) {
		Object result = list.set(index, element);
		changed();
		return result;
	}

	private class ChangeIterator implements ListIterator {
		private ListIterator iterator;

		ChangeIterator(ListIterator iterator) {
			this.iterator = iterator;
		}

		@Override
		public int nextIndex() {
			return iterator.nextIndex();
		}

		@Override
		public int previousIndex() {
			return iterator.previousIndex();
		}

		@Override
		public boolean hasPrevious() {
			return iterator.hasPrevious();
		}

		@Override
		public Object previous() {
			return iterator.previous();
		}

		@Override
		public void add(Object o) {
			iterator.add(o);
			changed();
		}

		@Override
		public void set(Object o) {
			iterator.set(o);
			changed();
		}

		@Override
		public void remove() {
			iterator.remove();
			changed();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Object next() {
			return iterator.next();
		}
	}
}
