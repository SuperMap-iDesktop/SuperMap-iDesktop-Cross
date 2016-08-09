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


// $Id: ContainerList.java,v 1.4 2005/02/16 11:28:13 jesper Exp $
package net.infonode.gui;

import net.infonode.util.ChangeNotifyList;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 */
public class ContainerList extends ChangeNotifyList {
  private Container container;

  public ContainerList(Container container) {
    super(new ArrayList());
    this.container = container;
  }

  protected void changed() {
    container.removeAll();
    List list = getList();

    for (int i = 0; i < list.size(); i++)
      container.add((Component) list.get(i));
  }
}
