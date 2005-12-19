/*
 * $Id: DataFileFactory.java,v 1.2 2005/12/19 12:31:29 oldman1004 Exp $
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
 * <p>
 * Main class for generating or reading data files written in widely accepted
 * data format. Currently supported data formats are tab separate ({@link com.infomata.data.TabFormat})
 * and CSV ({@link com.infomata.data.CSVFormat}), but more data formats can
 * easily be added by implementing {@link com.infomata.data.DataFormat}
 * </p>
 * <p>
 * <b>USAGE:</b>
 * 
 * <pre class="example">
 * // Creating a reader for CSV file using ISO-8859-1
 * 
 * DataFile read = DataFile.createReader(&quot;8859_1&quot;);
 * read.setDataFormat(new CSVFormat());
 * 
 * try
 * {
 * 
 *     read.open(new File(&quot;/data/test.csv&quot;));
 * 
 *     for (DataRow row = read.next(); row != null; row = read.next())
 *     {
 *         String text = row.getString(0);
 *         int number1 = row.getInt(1, 0);
 *         double number2 = row.getDouble(2);
 *         // use the retrieved data ...
 *     }
 * }
 * 
 * finally
 * {
 *     read.close();
 * }
 * 
 * // Creating a writer for data file with European encoding
 * // ISO-8859-2 (European) using tab separated format.
 * // (rewrite existing file)
 * 
 * DataFile write = DataFile.createWriter(&quot;8859_2&quot;, false);
 * write.setDataFormat(new TabFormat());
 * 
 * try
 * {
 * 
 *     write.open(new File(&quot;/data/test.txt&quot;));
 * 
 *     for (DataRow row = write.next(); row != null; row = write.next())
 *     {
 *         row.add(&quot;some German text&quot;);
 *         row.add(123);
 *         row.add(13323.23d);
 *     }
 * }
 * 
 * finally
 * {
 *     write.close();
 * }
 * </pre>
 * 
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.2 $
 */
public class DataFileFactory
{

    /**
     * Creates a reader instance of a data file.
     * 
     * @param enc
     *            Java character encoding in which the data file is written.
     * @return a reader instance of the DataFile.
     */
    public static final DataFile createReader(String enc)
    {
        DataFile df = new DataFileReader(enc);
        return df;
    }

    /**
     * Creates a writer instance of DataFile used for writing a data file.
     * 
     * @param enc
     *            Java encoding to use while writing the data file containing
     *            none-English characters.
     * @param append
     *            True if appending to existing data file. False, if writing to
     *            a new file or overwriting current file.
     * @return a writer instance of DataFile.
     */
    public static final DataFile createWriter(String enc, boolean append)
    {
        DataFileWriter writer = new DataFileWriter(enc);
        writer.setAppendToFile(append);
        return writer;
    }

}
