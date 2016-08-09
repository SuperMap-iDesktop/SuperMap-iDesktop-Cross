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

// $Id: PropertyMapValueHandler.java,v 1.3 2004/07/06 15:07:17 jesper Exp $
package net.infonode.properties.propertymap;

import net.infonode.properties.base.Property;
import net.infonode.properties.base.exception.CantRemoveValueException;
import net.infonode.properties.propertymap.value.PropertyValue;
import net.infonode.properties.propertymap.value.SimplePropertyValue;
import net.infonode.properties.types.PropertyGroupProperty;
import net.infonode.properties.util.PropertyValueHandler;

/**
 * Property value handler for property maps.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 */
public class PropertyMapValueHandler implements PropertyValueHandler {
	/**
	 * The instance of this class.
	 */
	public static final PropertyMapValueHandler INSTANCE = new PropertyMapValueHandler();

	/**
	 * Constructor
	 */
	private PropertyMapValueHandler() {
	}

	@Override
	public Object getValue(Property property, Object object) {
		PropertyMapImpl propertyMap = (PropertyMapImpl) object;
		PropertyValue value = propertyMap.getValueWithDefault(property);
		return value == null ? null : value.getWithDefault(propertyMap);
	}

	@Override
	public void setValue(Property property, Object object, Object value) {
		((PropertyMapImpl) object).setValue(property, new SimplePropertyValue(value));
	}

	@Override
	public boolean getValueIsRemovable(Property property, Object object) {
		return !(property instanceof PropertyGroupProperty);
	}

	@Override
	public void removeValue(Property property, Object object) {
		if (property instanceof PropertyGroupProperty)
			throw new CantRemoveValueException(property);

		((PropertyMapImpl) object).removeValue(property);
	}

	@Override
	public boolean getValueIsSet(Property property, Object object) {
		return (property instanceof PropertyGroupProperty) || ((PropertyMapImpl) object).valueIsSet(property);
	}
}
