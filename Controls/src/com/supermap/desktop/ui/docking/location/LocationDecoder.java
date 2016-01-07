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


// $Id: LocationDecoder.java,v 1.5 2005/02/16 11:28:14 jesper Exp $
package com.supermap.desktop.ui.docking.location;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.supermap.desktop.ui.docking.RootWindow;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 */
public class LocationDecoder {
  public static final int NULL = 0;
  public static final int ROOT = 1;
  public static final int SPLIT = 2;
  public static final int TAB = 3;

  private LocationDecoder() {
  }

  public static WindowLocation decode(ObjectInputStream in, RootWindow rootWindow) throws IOException {
    int type = in.readInt();

    switch (type) {
      case NULL:
        return NullLocation.INSTANCE;

      case ROOT:
        return WindowRootLocation.decode(in, rootWindow);

      case SPLIT:
        return WindowSplitLocation.decode(in, rootWindow);

      case TAB:
        return WindowTabLocation.decode(in, rootWindow);

      default:
        throw new IOException("Invalid location type!");
    }
  }
}
