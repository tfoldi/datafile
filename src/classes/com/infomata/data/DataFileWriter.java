/*
 * $Id: DataFileWriter.java,v 1.3 2005/12/20 11:33:40 oldman1004 Exp $
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.NumberFormat;

// TODO: create separate test for DataFileWriter and test encoding
// TODO: test URL write (jar file?)

/**
 * <p>
 * Writer implementation of DataFile. As the name suggests, an instance of
 * DataFileWriter is used for writing data files in specified formats.
 * </p>
 * <b>USAGE:</b>
 * 
 * <pre class="example">
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
 * @version $Revision: 1.3 $
 */
public class DataFileWriter extends AbstractDataFile
{
    private DataRow row = null;

    private PrintWriter out = null;

    /**
     * Flag that indicates whether or not the new data is to appended to
     * file if it exists.
     */
    private boolean append = false;

    /**
     * Creates a new <code>DataFile</code> instance for writing data files.
     * 
     * @param enc character encoding supported by Java.
     */
    public DataFileWriter(String enc)
    {
        nf = NumberFormat.getInstance();
        setCharacterEncoding(enc);
    }

    /**
     * Interface for specifying if data should be added to the output file if it
     * exists.
     * 
     * @param append <code>true</code> if the data is to be appended to the file. <code>false</code>
     *               to overwrite existing content.
     */
    public void setAppendToFile(boolean append)
    {
        this.append = append;
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

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#close()
     */
    public final void close() throws IOException
    {
        if (containsHeader())
        {
            resetHeaders();
        }

        if (out != null)
        {
            if (row != null && row.size() > 0)
            {
                out.println(format.format(row));
                row = null;
            }
            out.close();
        }

    } // close()

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#open(java.io.File)
     */
    public final void open(File file) throws IOException
    {
        if (!file.exists() && !file.createNewFile())
        {
            throw new IOException("Can't create file: "
                    + file.getAbsolutePath());
        }

        if (out != null)
        {
            close();
        }

        open(file.toURL());
    }

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#open(java.net.URL)
     */
    public final void open(URL file) throws IOException
    {
        if (containsCharacterEncoding())
        {
            FileOutputStream fos = new FileOutputStream(file.getFile(), append);
            OutputStreamWriter ow = new OutputStreamWriter(fos,
                    getCharacterEncoding());
            out = new PrintWriter(ow);
        }
        else
        {
            FileWriter fw = new FileWriter(file.getFile(), append);
            out = new PrintWriter(new BufferedWriter(fw));
        }

        if (format == null)
        {
            format = new CSVFormat();
        }

    } // open()

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#next()
     */
    public final DataRow next() throws IOException
    {
        if (row == null)
        {
            row = new DataRow(nf);
        }
        else
        {
            if (row.size() == 0)
            {
                out.println("");
            }
            else
            {
                out.println(format.format(row));
            }
            row.clear();
        }
        return row;

    } // next()

}
