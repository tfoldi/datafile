/*
 * $Id: AbstractDataFile.java,v 1.2 2005/12/19 12:31:29 oldman1004 Exp $
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract implementation of DataFile interface.  This class implements common methods
 * that do not differ in either reader or writer implementations.
 * 
 * @version $Revision: 1.2 $
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 *
 */
public abstract class AbstractDataFile implements DataFile
{

	/**
	 * Character encoding used for reading/writing a data file.
	 */
    private String enc = null;

    /**
     * Map of header (column label) to column index.
     */
    protected Hashtable headerIndex = null;

    /**
     * List of header names in column index sequence.
     */
    private ArrayList headerList = null;

    /**
     * Instance of NumberFormat used to parse or format numeric data.
     */
    protected NumberFormat nf = null;

    /**
     * Implementation of DataFormat for parsing/formatting data from/into
     * a data file in specified format.
     */
    protected DataFormat format = null;

    /**
     * Check if character encoding has been set.
     * @return <code>true</code> if set.  <code>false</code> otherwise.
     */
    protected boolean containsCharacterEncoding()
    {
        return enc != null && enc.length() > 0;
    }

    /**
     * Retrieve character encoding for current instance of DataFile.
     * @return character encoding.
     */
    protected String getCharacterEncoding()
    {
        return enc;
    }

    /**
     * Sets the character encoding for current instance of DataFile.
     * @param enc supported character encoding.
     */
    protected void setCharacterEncoding(String enc)
    {
        this.enc = enc;
    }

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#containsHeader(boolean)
     */
    public final void containsHeader(boolean header)
    {
        headerList = (header) ? new ArrayList() : null;
    }

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#containsHeader()
     */
    public final boolean containsHeader()
    {
        return headerList != null;
    }

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#setDataFormat(com.infomata.data.DataFormat)
     */
    public final void setDataFormat(DataFormat format)
    {
        this.format = format;
    }

    /**
     * Retrieves the list of header names found in the first row of the data
     * file. In order for this to work, the header row indicator must be set
     * prior to opening the data file. <b>Note: the header names are not
     * guaranteed to be the order they appear in the first row of the data file</b>.
     * 
     * @return header names (not guaranteed in the order in which they appear in
     *         file).
     * @deprecated use {@link #getHeaderList()} instead.
     */
    public final Iterator getHeaderNames()
    {
        ArrayList tmp = (headerIndex == null) ? new ArrayList()
                : new ArrayList(headerIndex.keySet());
        return tmp.iterator();
    }

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#getHeaderList()
     */
    public final List getHeaderList()
    {
        return Collections.unmodifiableList(headerList);
    }

    /*
     * (non-Javadoc)
     * @see com.infomata.data.DataFile#setHeaderList(java.util.List)
     */
    public void setHeaderList(List headers)
    {
        // for cases when header is manually set.
        if (containsHeader())
        {
            resetHeaders();
        }
        else
        {
            containsHeader(true);
        }

        headerIndex = new Hashtable(headers.size());
        for (int i = 0; i < headers.size(); i++)
        {
            headerList.add(headers.get(i).toString());
            headerIndex.put(headers.get(i).toString(), new Integer(i));
        }
    }

    /**
     * Removes header row (column labels) from the DataFile instance.
     */
    protected void resetHeaders()
    {
        if (containsHeader())
        {
            headerList.clear();
            if (headerIndex != null)
            {
                headerIndex.clear();
                headerIndex = null;
            }
        }
    }

    /* (non-Javadoc)
     * @see com.infomata.data.DataFile#setNumberFormat(java.text.NumberFormat)
     */
    public void setNumberFormat(NumberFormat numberFormat)
    {
        nf = numberFormat;
    }

}
