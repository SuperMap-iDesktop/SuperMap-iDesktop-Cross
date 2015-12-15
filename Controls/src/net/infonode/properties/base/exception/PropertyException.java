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

// $Id: PropertyException.java,v 1.3 2004/09/22 14:32:50 jesper Exp $
package net.infonode.properties.base.exception;

import net.infonode.properties.base.Property;

/**
 * Base class for all property exceptions.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 */
public class PropertyException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final transient Property property;

	/**
	 * Constructor.
	 *
	 * @param property the property that triggered this exception
	 * @param text the exception text
	 */
	public PropertyException(Property property, String text) {
		super(text);
		this.property = property;
	}

	/**
	 * Returns the property that triggered this exception.
	 *
	 * @return the property that triggered this exception
	 */
	public Property getProperty() {
		return property;
	}
}
