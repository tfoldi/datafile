package com.infomata.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;

public interface DataFile {


    /**
     * Specifies whether the data file contains a header
     * row (a.k.a. column headers)
     * @param header <code>true</code> - if data file contains a header row.
     *               <code>false</code> - otherwise.
     */
	public void containsHeader(boolean header);

    /**
     * Checks if the data file contains a header row (a.k.a. column headers)
     * @return <code>true</code> if the file contains a header row (set by using {@link #containsHeader(boolean)})
     */
	public boolean containsHeader();

    /**
     * Sets the data format (ie. CSV format) used by the file.
     * @param format implementation of {@link com.infomata.data.DataFormat}.
     */
	public void setDataFormat(DataFormat format);

    /**
     * Closes the currently open data file.
     * @throws IOException
     */
	public void close() throws IOException; // close()

    /**
     * Opens the data file for reading or writing.
     * @param file file reference to data file.
     * @throws IOException if the file fails to open for read or write.
     */
	public void open(File file) throws IOException;

    /**
     * Opens the data file for reading/writing.
     * @param file URL representation of the data file.
     * @throws IOException if the file fails to open for read or write.
     */
	public void open(URL file) throws IOException; // open()

    /**
     * Retrieves the next row of data for reading or writing.
     * @return row of data.
     * @throws IOException when read or write fails.
     */
	public DataRow next() throws IOException; // next()

    /**
     * Retrieve the list of column headers in proper sequence.  In case
     * of reader, the list of column headers in the sequence they appear.
     * @return list of column headers
     */
	public abstract List getHeaderList();

    /**
     * Sets the list of headers to be used for reading/writing a data
     * file.  The sequence of the headers in the list determines
     * which label corresponds to which value.
     * @param headers list of header labels.  If one already exists, the
     *                new list overwrites the old.
     */
	public void setHeaderList(List headers);
    
    /**
     * Sets the instance of NumberFormat class used to parse or format
     * numeric data.  If not set, DataFile will use the appropriate
     * NumberFormat instance for current default Locale.  For example,
     * depending on default local, the following value can represent the
     * same value: <code>1,100</code> and <code>1.100</code> (the later is
     * used in countries using ISO 8859-2 encoding)
     * 
     * @param numberFormat formatting to use in place of default NumberFormat
     *                     for JVM's default Locale.
     */
    public void setNumberFormat(NumberFormat numberFormat);
}