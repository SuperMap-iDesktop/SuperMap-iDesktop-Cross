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

// $Id: SimplePropertyValue.java,v 1.14 2005/03/17 16:15:32 jesper Exp $
package net.infonode.properties.propertymap.value;

import net.infonode.properties.propertymap.PropertyMapImpl;
import net.infonode.util.Printer;
import net.infonode.util.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.14 $
 */
public class SimplePropertyValue implements PropertyValue {
	private final Object value;

	public SimplePropertyValue(Object value) {
		this.value = value;
	}

	@Override
	public void updateListener(boolean enable) {
		// do nothing
	}

	@Override
	public PropertyValue getParent() {
		return null;
	}

	@Override
	public Object get(PropertyMapImpl object) {
		return value;
	}

	@Override
	public Object getWithDefault(PropertyMapImpl object) {
		return value;
	}

	@Override
	public PropertyValue getSubValue(PropertyMapImpl object) {
		return null;
	}

	@Override
	public void unset() {
		// do nothing
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public void dump(Printer printer) {
		printer.println(toString());
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof SimplePropertyValue && Utils.equalsMethod(((SimplePropertyValue) obj).value, value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public void write(ObjectOutputStream out) throws IOException {
		out.writeInt(ValueDecoder.SIMPLE);
		out.writeObject(value);
	}

	@Override
	public boolean isSerializable() {
		return value instanceof Serializable;
	}

	public static PropertyValue decode(ObjectInputStream in) throws IOException {
		try {
			return new SimplePropertyValue(in.readObject());
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	public static void skip(ObjectInputStream in) throws IOException {
		try {
			in.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public PropertyValue copyTo(PropertyMapImpl propertyMap) {
		return this;
	}

}
