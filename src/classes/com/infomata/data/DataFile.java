/*
 * $Id: DataFile.java,v 1.1 2004/04/24 04:19:36 oldman1004 Exp $
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

/**
 * <p>
 * Main class for generating or reading data files written in widely
 * accepted data format. Currently supported
 * data formats are tab separate ({@link com.infomata.data.TabFormat}) and
 * CSV ({@link com.infomata.data.CSVFormat}), but more data formats can
 * easily be added by implementing {@link com.infomata.data.DataFormat}
 * </p>
 * <p>
 * <b>USAGE:</b>
 * <pre class="example">
 * // Creating a reader for CSV file using ISO-8859-1
 *
 * DataFile read = DataFile.createReader("8859_1");
 * read.setDataFormat(new CSVFormat());
 *
 * try {
 *
 *     read.open(new File("/data/test.csv"));
 *
 *     for (DataRow row = read.next(); row != null; row = read.next()) {
 *         String text = row.getString(0);
 *         int number1 = row.getInt(1, 0);
 *         double number2 = row.getDouble(2);
 *         // use the retrieved data ...
 *     }
 * }
 *
 * finally {
 *  read.close();
 * }
 *
 * // Creating a writer for data file with European encoding
 * // ISO-8859-2 (European) using tab separated format.
 * // (rewrite existing file)
 *
 * DataFile write = DataFile.createWriter("8859_2", false);
 * write.setDataFormat(new TabFormat());
 *
 * try {
 *
 *     write.open(new File("/data/test.txt"));
 *
 *     for (DataRow row = write.next(); row != null; row = write.next()) {
 *         row.add("some German text");
 *         row.add(123);
 *         row.add(13323.23d);
 *     }
 * }
 *
 * finally {
 *     write.close();
 * }
 * </pre>
 *
 * @author <a href="mailto:skim@infomata.com">Sam Kim</a>
 * @version $Revision: 1.1 $
 */
public class DataFile {

    private final static int READER = 0x21;
    private final static int WRITER = 0x12;
    
    private int type = READER;
    private File file = null;
    private String enc = null;
    private DataRow row = null;
    private Vector rows = null;
    private boolean first = true;
    private NumberFormat nf = null;
    private Hashtable headerIndex = null;
  
    private BufferedReader in = null;
    private PrintWriter out = null;
    private DataFormat format = new CSVFormat();
    private boolean append = false;

    /**
     * Creates a new <code>DataFile</code> instance.
     *
     * @param enc a <code>String</code> value
     * @param type an <code>int</code> value
     */
    private DataFile(String enc, int type) {
        nf = NumberFormat.getInstance();
        this.enc = enc;
        this.type = type;
    }

    /**
     * Creates a reader instance of a data file.
     *
     * @param enc    Java character encoding in which the data
     *               file is written.
     * @return a reader instance of the DataFile.
     */
    public static final DataFile createReader(String enc) {
        DataFile data = new DataFile(enc, READER);
        return data;
    }


    /**
     * Creates a writer instance of DataFile used for writing
     * a data file.
     *
     * @param enc    Java encoding to use while writing
     *               the data file containing none-English characters.
     * @param append True if appending to existing data file.  False, if
     *               writing to a new file or overwriting current file.
     * @return a writer instance of DataFile.
     */
    public static final DataFile createWriter(String enc, boolean append) {
        DataFile data = new DataFile(enc, WRITER);
        data.append = append;
        return data;
    }


    /**
     * Sets the indicator for header row (must be first line
     * of data file).
     * @param header True if the file contains a header row.  False, otherwise.
     */    
    public final void containsHeader(boolean header) {
        headerIndex = (header) ? new Hashtable() : null;
    }

    /**
     * Checks if header indicator has been turned on using
     * {@link #containsHeader(boolean)}.  Data file reader
     * lacks the logic to determine if the first row is a
     * header row, automatically, so the existance of header
     * row must be indicated prior to reading the data file.
     * @return True, if header file indicator has been
     * set to <code>true</code>.  False, otherwise.
     */    
    public final boolean containsHeader() {
        return headerIndex != null;
    }

    /**
     * Sets the data format to be read or written.
     *
     * @param format an instance of a class that
     *               implements DataFormat interface.
     * @see com.infomata.data.DataFormat
     */
    public final void setDataFormat(DataFormat format) {
        this.format = format;
    }


    /**
     * Finalization method that closes the file descriptor.  This would
     * work only if JVM is current (1.3 or later?).
     */
    public void finalize() {
        close();
    } // finalize()


    /**
     * Closes a data file.
     */
    public final void close() {

        try {

            switch (type) {
        
            case READER:
                if (in != null) {
                    in.close();
                }
                if (headerIndex != null) {
                     headerIndex.clear();
                }
                break;
        
            case WRITER:
                if (out != null) {
                    if (row != null && row.size() > 0) {
                        out.println(format.format(row));
                    }
                    out.close();
                }
                break;
        
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    } // close()


    /**
     * Opens a data file for reading or writing
     *
     * @param file data file
     * @exception IOException if an error occurs
     */
    public final void open(File file) throws IOException {
    
        if (!file.exists()) {
            if (type == READER) {
                throw new IOException("No such file: "
                                      + file.getAbsolutePath());
            }
            else if (type == WRITER) {
                if (!file.createNewFile()) {
                    throw new IOException("Can't create file: "
                                          + file.getAbsolutePath());
                }
            }
        }
        
        if (in != null || out != null) {
            close();
        }
    
        switch (type) {

        case READER:
            if (enc != null && enc.length() > 0) {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, enc);
                in = new BufferedReader(isr);
            }
            else {
                FileReader fr = new FileReader(file);
                in = new BufferedReader(fr);
            }
            first = true;
            break;

        case WRITER:
            if (enc != null) {
                FileOutputStream fos =
                    new FileOutputStream(file.getAbsolutePath(), append);
                OutputStreamWriter ow = new OutputStreamWriter(fos, enc);
                out = new PrintWriter(ow);
            }
            else {
                FileWriter fw = new FileWriter(file.getAbsolutePath(), append);
                out = new PrintWriter(new BufferedWriter(fw));
            }
            break;

        default:
            break;

        }
               
    } // open()


    /**
     * Retrieves the next row for reading or
     * writing data
     *
     * @return a reference to next data row.
     * @exception IOException if an error occurs
     */
    public final DataRow next() throws IOException {

        switch (type) {
               
        case READER:

            String line = in.readLine();

            if (line != null) {

                row = format.parseLine(line);

                if (row == null) {
                    // cases where cell contains new line char without
                    // proper termination by delimiter (i.e. CSV with new line
                    // in data cell)
                    // DataFormat class should keep the previous row
                    // in memory and return only the line terminates
                    // properly.
                    row = next();
                }

                if (headerIndex != null) {
                    if (first) {
                        first = false;
                        for (int i = 0; i < row.size(); i++) {
                            headerIndex.put(row.getString(i), new Integer(i));
                        }
                        row = next();
                    }
                    row.setHeaderIndex(headerIndex);
                }
            }
            else {
                row = null;
            }
            break;

        case WRITER:
            if (row != null) {
                if (row.size() == 0) {
                    out.println("");
                }
                else {
                    out.println(format.format(row));
                }
            }
            row = new DataRow(nf);
            break;

        }

        return row;
          
    } // next()
    
    /**
     * Retrieves the list of header names found in the first
     * row of the data file.  In order for this to work,
     * the header row indicator must be set prior to opening
     * the data file.  <b>Note: the header names are not guaranteed
     * to be the order they appear in the first row of the
     * data file</b>.
     * @return header names (not guaranteed in the order in which
     * they appear in file).
     */    
    public final Iterator getHeaderNames() {
         ArrayList tmp = (headerIndex == null)
                         ? new ArrayList()
                         : new ArrayList(headerIndex.keySet());
         return tmp.iterator();
    }


}
