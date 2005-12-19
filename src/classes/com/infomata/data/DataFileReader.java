/*
 * $Id: DataFileReader.java,v 1.2 2005/12/19 12:31:29 oldman1004 Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;


// TODO: update javadoc
// TODO: create separate unit test for DataFileReader and test encoding
/**
 * <p>
 * Reader implementation of DataFile.  As the name suggests, an instance of
 * DataFileReader is used for reading data files in a specific format.
 * </p>
 * <p>
 * <b>USAGE:</b>
 * 
 * <pre class="example">
 * // Creating a reader for CSV file using ISO-8859-1
 * 
 * DataFile reader = DataFileFactory.createReader(&quot;8859_1&quot;);
 * reader.setDataFormat(new CSVFormat());
 * 
 * try
 * {
 * 
 *     read.open(new File(&quot;/data/test.csv&quot;));
 * 
 *     for (DataRow row = reader.next(); row != null; row = read.next())
 *     {
 *         String text = row.getString(0);
 *         int number1 = row.getInt(1, 0);
 *         double number2 = row.getDouble(2);
 *     }
 * }
 * 
 * finally
 * {
 *     read.close();
 * }
 * </pre>
 * </p>
 * 
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.2 $
 */
public class DataFileReader extends AbstractDataFile
{

    private DataRow row = null;
    private BufferedReader in = null;

    /**
     * Creates a new <code>DataFile</code> instance for reading data files.
     * 
     * @param enc character encoding supported by Java.
     */
    public DataFileReader(String enc)
    {
        setCharacterEncoding(enc);
    }

    /**
     * Finalization method that closes the file descriptor. This would work only
     * if JVM is current (1.3 or later?).
     */
    public void finalize()
    {
        try
        {
            close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    } // finalize()

    /**
     * @see com.infomata.data.DataFile#close()
     */
    public void close() throws IOException
    {
        if (containsHeader())
        {
            resetHeaders();
        }

        if (in != null)
        {
            in.close();
        }
    } // close()

    /**
     * @see com.infomata.data.DataFile#open(java.io.File)
     */
    public final void open(File file) throws IOException
    {
        if (!file.exists())
        {
            throw new IOException("No such file: " + file.getAbsolutePath());
        }

        if (in != null)
        {
            close();
        }

        open(file.toURL());
    } // open(File)

    /**
     * @see com.infomata.data.DataFile#open(java.net.URL)
     */
    public final void open(URL file) throws IOException
    {
        InputStream is = file.openStream();
        InputStreamReader isr = (containsCharacterEncoding()) ? new InputStreamReader(
                is, getCharacterEncoding())
                : new InputStreamReader(is);

        in = new BufferedReader(isr);

        if (format == null)
        {
            format = new CSVFormat();
        }
        
        if (containsHeader())
        {
            DataRow row = next();
            setHeaderList(row.getValues());
        }
        
        if (nf == null)
        {
            nf = NumberFormat.getInstance();
        }

    } // open(URL)

    /**
     * @see com.infomata.data.DataFile#next()
     */
    public final DataRow next() throws IOException
    {

        String line = in.readLine();

        if (line != null)
        {
            row = format.parseLine(line);

            if (row == null)
            {
                // cases where cell contains new line char without
                // proper termination by delimiter (i.e. CSV with new line
                // in data cell)
                // DataFormat class should keep the previous row
                // in memory and return only the line terminates
                // properly.
                row = next();
            }

            if (containsHeader())
            {
                row.setHeaderIndex(headerIndex);
            }
        }
        else
        {
            row = null;
        }

        return row;

    } // next()
}
