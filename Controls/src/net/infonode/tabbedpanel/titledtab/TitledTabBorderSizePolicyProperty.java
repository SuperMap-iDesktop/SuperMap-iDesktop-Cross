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


// $Id: TitledTabBorderSizePolicyProperty.java,v 1.7 2005/02/16 11:28:15 jesper Exp $
package net.infonode.tabbedpanel.titledtab;

import net.infonode.properties.base.PropertyGroup;
import net.infonode.properties.types.EnumProperty;
import net.infonode.properties.util.PropertyValueHandler;

/**
 * Property for TitledTabBorderSizePolicy
 *
 * @author $Author: jesper $
 * @version $Revision: 1.7 $
 * @see TitledTabBorderSizePolicy
 */
public class TitledTabBorderSizePolicyProperty extends EnumProperty {
  /**
   * Constructs a TitledTabBorderSizePolicyProperty object.
   *
   * @param group        property group
   * @param name         property name
   * @param description  property description
   * @param valueStorage storage for property
   */
  public TitledTabBorderSizePolicyProperty(PropertyGroup group,
                                           String name,
                                           String description,
                                           PropertyValueHandler valueStorage) {
    super(group,
          name,
          TitledTabBorderSizePolicy.class,
          description,
          valueStorage,
          TitledTabBorderSizePolicy.getSizePolicies());
  }

  /**
   * Gets the TitledTabBorderSizePolicy
   *
   * @param object storage object for property
   * @return the TitledTabBorderSizePolicy
   */
  public TitledTabBorderSizePolicy get(Object object) {
    return (TitledTabBorderSizePolicy) getValue(object);
  }

  /**
   * Sets the TitledTabBorderSizePolicy
   *
   * @param object storage object for property
   * @param policy the TitledTabBorderSizePolicy
   */
  public void set(Object object, TitledTabBorderSizePolicy policy) {
    setValue(object, policy);
  }
}
