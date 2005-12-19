/*
 * $Id: DataFormat.java,v 1.2 2005/12/19 12:31:29 oldman1004 Exp $
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
 * Interface for all data format classes used in reading
 * and writing a data file.
 *
 * @see com.infomata.data.DataFile
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.2 $
 */
public interface DataFormat {

    /**
     * <p>
     * Parses a line of data into a DataRow object
     * which contains each data item in individual cells.
     * If the line of data does not terminate properly,
     * the method must return null and keep the currently parsed
     * row in memory for further processing.  Only when termination
     * requirements have been met, the method should return a
     * <code>DataRow</code> object.
     * </p>
     * <p>
     * For example, a CSV formated cell may contain a new line character.
     * Since the each line is delimited using new line character,
     * the line may contain only a part of the CSV data cell content.
     * The <code>CSVFormat</code> implementation does not return a
     * non-null reference to the <code>DataRow</code> object until
     * that cell has been terminated properly.
     * </p>
     * @param line line containing data items.
     * @return a <code>DataRow</code> object containing
     *         data items in individual cells.
     */
    public DataRow parseLine(String line);

  
    /**
     * Converts the data contained in the DataRow
     * object into a line of data.
     *
     * @param row a <code>DataRow</code> object containing data
     * @return String representing data items contained in
     *         DataRow object.
     */
    public String format(DataRow row);

}
