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

// $Id: StreamUtil.java,v 1.7 2005/02/16 11:28:14 jesper Exp $

package net.infonode.util;

import java.io.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.7 $
 */
public class StreamUtil {
	private StreamUtil() {
		// 工具类不提供构造函数
	}

	public static final void readAll(InputStream in, byte[] data, int offset, int length) throws IOException {
		int offsetTemp = offset;
		int lengthTemp = length;
		while (lengthTemp > 0) {
			int len = in.read(data, offsetTemp, lengthTemp);

			if (len == -1)
				throw new IOException("End of stream reached!");

			offsetTemp += len;
			lengthTemp -= len;
		}
	}

	public static final byte[] readAll(InputStream is) throws IOException {
		byte[] data = new byte[is.available()];
		int pos = 0;

		while (pos < data.length)
			pos += is.read(data, pos, data.length - pos);

		is.close();
		return data;
	}

	public static final byte[] writeObject(Object object) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream o2 = new ObjectOutputStream(out);
		o2.writeObject(object);
		o2.close();
		return ArrayUtil.part(out.toByteArray(), 0, out.size());
	}

	public static final Object readObject(byte[] data) throws IOException, ClassNotFoundException {
		return new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
	}

	public static byte[] write(Writable writable) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream o2 = new ObjectOutputStream(out);
		writable.write(o2);
		o2.close();
		return out.toByteArray();
	}

	public static void read(byte[] data, Readable readable) throws IOException {
		readable.read(new ObjectInputStream(new ByteArrayInputStream(data)));
	}

	public static void readAll(InputStream in, byte[] data) throws IOException {
		readAll(in, data, 0, data.length);
	}

	public static void write(InputStream in, OutputStream out, int length) throws IOException {
		byte[] data = new byte[10000];
		int lengehTemp = length;
		while (lengehTemp > 0) {
			int read = in.read(data, 0, Math.min(data.length, lengehTemp));
			out.write(data, 0, read);
			lengehTemp -= read;
		}
	}
}
