/*
 * $Id: ItemPerLineFormat.java,v 1.2 2005/12/19 12:31:29 oldman1004 Exp $
 *
 * Copyright(c) 2002 Infomata
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.infomata.data;

/**
 * Sample implementation of {@link com.infomata.data.DataFormat}
 * interface for data file with one item per line.
 * <pre>
 *  public class ItemPerLineFormat implements DataFormat {
 *
 *    public DataRow parseLine(String line) {
 *        DataRow row = null;
 *        if (line != null) {
 *            row = new DataRow();
 *            row.add(line);
 *        }
 *        return row;
 *    }
 *
 *    public String format(DataRow row) {
 *        return row.getString(0);
 *    }
 * }
 * </pre>
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.2 $
 */
public class ItemPerLineFormat implements DataFormat {

    public DataRow parseLine(String line) {
        DataRow row = null;
        if (line != null) {
            row = new DataRow();
            row.add(line);
        }
        return row;
    }

    public String format(DataRow row) {
        return row.getString(0);
    }

}
