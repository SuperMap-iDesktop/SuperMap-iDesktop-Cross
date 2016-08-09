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

// $Id: OperationAbortedException.java,v 1.3 2004/09/28 15:07:29 jesper Exp $
package com.supermap.desktop.ui.docking;

/**
 * Exception thrown when an operation is aborted.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 */
public class OperationAbortedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public OperationAbortedException() {
		// do nothing
	}

	/**
	 * Constructor.
	 *
	 * @param message the exception message
	 */
	public OperationAbortedException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @param cause the exception cause
	 */
	public OperationAbortedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 *
	 * @param message the exception message
	 * @param cause the exception cause
	 */
	public OperationAbortedException(String message, Throwable cause) {
		super(message, cause);
	}
}
