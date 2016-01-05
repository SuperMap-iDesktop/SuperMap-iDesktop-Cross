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

// $Id: ParentMapRef.java,v 1.5 2005/02/16 11:28:15 jesper Exp $
package net.infonode.properties.propertymap.ref;

import net.infonode.properties.propertymap.PropertyMapImpl;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 */
public class ParentMapRef implements PropertyMapRef {
	public static final ParentMapRef INSTANCE = new ParentMapRef();

	private ParentMapRef() {
	}

	@Override
	public PropertyMapImpl getMap(PropertyMapImpl object) {
		return object == null ? null : object.getParent();
	}

	@Override
	public String toString() {
		return "parent";
	}

	@Override
	public void write(ObjectOutputStream out) throws IOException {
		out.writeInt(PropertyMapRefDecoder.PARENT);
	}

}
