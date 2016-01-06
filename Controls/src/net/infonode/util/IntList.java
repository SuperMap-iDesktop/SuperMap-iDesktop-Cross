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

// $Id: IntList.java,v 1.3 2004/09/22 14:35:05 jesper Exp $
package net.infonode.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A single linked list of positive int's.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 */
public class IntList {
	/**
	 * The empty list.
	 */
	public static final IntList EMPTY_LIST = new IntList(-1, null);

	private int value;
	private IntList next;

	/**
	 * Constructor.
	 *
	 * @param value
	 *            the int value
	 * @param next
	 *            the next list element
	 */
	public IntList(int value, IntList next) {
		this.value = value;
		this.next = next;
	}

	/**
	 * @return
	 */
	public int getValue() {
		return value;
	}

	public IntList getNext() {
		return next;
	}

	public boolean isEmpty() {
		return this == EMPTY_LIST;
	}

	public boolean equalsTemp(Object object) {
		return object instanceof IntList && equalsTemp((IntList) object);
	}

	public boolean equalsTemp(IntList list) {
		return value == list.value && (next == null ? list.next == null : next.equalsTemp(list.next));
	}

	@Override
	public boolean equals(Object obj) {
		return equalsTemp(obj);
	};

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + value;

		if (next != null)
			result = 37 * result + next.hashCode();

		return result;
	}

	public void write(ObjectOutputStream out) throws IOException {
		out.writeInt(value);

		if (next != null)
			next.write(out);
	}

	public static IntList decode(ObjectInputStream in) throws IOException {
		int i = in.readInt();
		return i == -1 ? EMPTY_LIST : new IntList(i, decode(in));
	}

	@Override
	public String toString() {
		return value + (next == null ? "" : ", " + next.toString());
	}

}
