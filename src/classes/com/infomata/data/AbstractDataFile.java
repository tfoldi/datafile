package com.infomata.data;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractDataFile implements DataFile
{

    private String enc = null;

    protected Hashtable headerIndex = null;

    private ArrayList headerList = null;

    protected NumberFormat nf = null;

    protected DataFormat format = null;

    protected boolean containsCharacterEncoding()
    {
        return enc != null && enc.length() > 0;
    }

    protected String getCharacterEncoding()
    {
        return enc;
    }

    protected void setCharacterEncoding(String enc)
    {
        this.enc = enc;
    }

    public final void containsHeader(boolean header)
    {
        headerList = (header) ? new ArrayList() : null;
    }

    public final boolean containsHeader()
    {
        return headerList != null;
    }

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

    public final List getHeaderList()
    {
        return Collections.unmodifiableList(headerList);
    }

    /**
     * Sets the header lists (column labels) for the data file. If it already
     * exists, the new one replaces the old.
     * 
     * @param headers
     *            list of column labels in sequence of data in file.
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

    public void resetHeaders()
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

    /**
     * @see com.infomata.data.DataFile#setNumberFormat(java.text.NumberFormat)
     */
    public void setNumberFormat(NumberFormat numberFormat)
    {
        nf = numberFormat;
    }

}
