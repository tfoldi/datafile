/*
 * $Id: DataRow.java,v 1.4 2005/12/19 12:31:29 oldman1004 Exp $
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Object representing collection of data items contained within one
 * line of the data file
 *
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.4 $
 */
public class DataRow {

    /**
     * column name to index reference table.
     */
    private Hashtable index = null;

    /**
     * container for data items.  (stored as string)
     */
    private ArrayList items = null;

    /**
     * <code>NumberFormat</code> instance used to parse
     * numeric values in specified format.
     */
    private NumberFormat nf = null;

    /**
     * SimpleDateFormat instance used to parse date items.
     */
    private SimpleDateFormat df = new SimpleDateFormat();

    /**
     * Creates a new <code>DataRow</code> instance.
     * Uses JVM's locale for formatting numbers.
     */
    public DataRow() {
        items = new ArrayList();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(50);
    }

    /**
     * Creates a new <code>DataRow</code> instance.
     * Uses specified NumberFormat object to read
     * or write numbers.
     *
     * @param format a <code>NumberFormat</code> object
     */
    public DataRow(NumberFormat format) {
        items = new ArrayList();
        nf = format;
    }
     

    /**
     * Adds a String datum to the next location
     *
     * @param datum String datum item
     */
    public void add(String datum) {
        items.add(datum);
    }


    /**
     * Adds an int datum to the next location.
     *
     * @param datum int datum item
     */
    public void add(int datum) {
        items.add(nf.format(datum));
    }


    /**
     * Adds a long datum to the next location.
     *
     * @param datum a <code>long</code> value
     */
    public void add(long datum) {
        items.add(nf.format(datum));
    }


    /**
     * Adds a double datum to the next location.
     *
     * @param datum a <code>double</code> value
     */
    public void add(double datum) {
        items.add(nf.format(datum));
    }


    /**
     * Adds an object with <code>toString()</code>
     * method defined to the next item.
     *
     * @param obj an <code>Object</code> with
     *            <code>toString()</code> defined.
     */
    public void add(Object obj) {
        String str = obj.toString();
        add(str);
    }


    /**
     * Adds an empty item (spacer)
     */
    public void addEmpty() {
        items.add("");
    }


    /**
     * Retrieves an iterator for items
     *
     * @return an <code>Iterator</code>
     */
    public Iterator iterator() {
        return items.iterator();
    }
    

    /**
     * Retrieves the date using the specified pattern
     * to parse the date. See <code>java.text.SimpleDateFormat</code>
     * documentation valid date format patterns.
     *
     * @param location locationg of the item
     * @param pattern  date format pattern (i.e. "yyyy.MM.dd" for 2002.10.20")
     * @return Date contained in specified location.  If parsing fails
     *         using the specified pattern, returns NULL.
     */
    public Date getDate(int location, String pattern) {
        Date d = null;
        String v = getString(location).trim();
        try {
            df.applyPattern(pattern);
            d = df.parse(v);
        }
        catch (ParseException e) {
            // do nothing and return null
        }
        return d;
    }

    /**
     * Retrieve the date value at the specified
     * <code>location</code> in specified <code>pattern</code>
     * as an instance of <code>java.sql.Date</code>
     * instead of <code>java.util.Date</code>.
     * @param location location of date value
     * @param pattern format of the date value in the data file
     * @return <code>java.sql.Date</code> instance representing
     * the date at <code>location</code>.
     */    
    public java.sql.Date getSqlDate(int location, String pattern) {
        java.sql.Date d = null;
        String v = getString(location).trim();
        try {
            df.applyPattern(pattern);
            d = new java.sql.Date(df.parse(v).getTime());
        }
        catch (ParseException e) {
            // do nothing
        }
        return d;
    }

    /**
     * Retrieves the date corresponding to the specified
     * column <code>label</code>.
     *
     * @param label column label
     * @param pattern see {@link java.text.SimpleDateFormat} for
     *                further description on pattern syntax.
     * @return <code>Date</code> value under specified column label.
     */
    public Date getDate(String label, String pattern) {
        Date d = null;
        int idx = getIndex(label);
        if (idx >= 0) {
            d = getDate(idx, pattern);
        }
        return d;
    }


    /** Retrieves the double value of the datum contained
     * in the specified location.
     * @return a <code>double</code> value
     * @param location location of the item
     * @throws NumberFormatException if the item cannot be parsed using normal
     * number parser.
     */
    public double getDouble(int location) throws NumberFormatException {

        double d = Double.NaN;
        String val = getString(location).trim();
        val = prepareNumberValue(val);
        
        try {
            d = nf.parse(val).doubleValue();
        }
        catch (ParseException e) {
            try {
                d = Double.valueOf(val).doubleValue();
            }
            catch (Exception l) {
                throw new NumberFormatException(l.getMessage());
            }
        }
        return d;
    }

    /**
     * Retrieve the <code>double</code> value contained
     * in data cell corresponding to the specified column
     * <code>label</code>
     *
     * @param label column label
     * @return a <code>double</code> value
     * @exception NumberFormatException if the value cannot be parsed
     *                                  correctly.
     */
    public double getDouble(String label) throws NumberFormatException {
        double d = 0d;
        int idx = getIndex(label);
        if (idx >= 0) {
            d = getDouble(idx);
        }
        else {
            throw new NumberFormatException("cannot parse null - check label");
        }
        return d;
    }


    /**
     * Retrieves the double value of the datum contained
     * in the specified location.  If the specified location does not
     * contain a double value or empty, default value is returned.
     *
     * @param location   location of the item.
     * @param defaultVal default value to use when value does not
     *                   exist or fails to parse correctly.
     * @return a <code>double</code> value
     */
    public double getDouble(int location, double defaultVal) {
        String val = getString(location).trim();
        try {
            defaultVal = nf.parse(val).doubleValue();
        }
        catch (ParseException e) {
            try {
                defaultVal = Double.valueOf(val).doubleValue();
            }
            catch (NumberFormatException l) {
                // do nothing and return defaultValue
            }
        }
        return defaultVal;
    }

    /** Retrieves the <code>double</code> value at specified
     * <code>location</code> parsed using <code>format</code>.
     * If the value does not exist or fails to parse into
     * <code>double</code>, <code>defaultVal</code> is returned.
     * @param location location of <code>double</code> value
     * @param format double value parser to be used in extracting
     * item.
     * @param defaultVal default value to use in case of missing data or
     * parse error.
     * @return a double value at specified <code>location</code>
     * or <code>defaultVal</code>
     */    
    public double getDouble(int location, DecimalFormat format, 
                            double defaultVal) {
        String val = getString(location).trim();
        val = prepareNumberValue(val);
        try {
            defaultVal = format.parse(val).doubleValue();
        }
        catch (ParseException e) {
            try {
                defaultVal = Double.valueOf(val).doubleValue();
            }
            catch (NumberFormatException nf) {
            }
        }
        return defaultVal;
    }

    /**
     * Retrieve the <code>double</code> value contained
     * in data cell corresponding to the specified column
     * <code>label</code>.  
     *
     * @param label      column label
     * @param defaultVal default value to use when value does not
     *                   exist or does not parse correctly.
     * @return <code>double</code> value corresponding to the
     *         specified column <code>label</code>. If the 
     *         specified column <code>label</code> does not 
     *         exist or the value cannot parsed into a
     *         <code>double</code>, <code>defaultValu</code>.
     */
    public double getDouble(String label, double defaultVal) {
        int idx = getIndex(label);
        if (idx >= 0) {
            defaultVal = getDouble(idx, defaultVal);
        }
        return defaultVal;
    }

     
    /**
     * Retrieves the int value of the datum contained
     * in the specified location.
     *
     * @param location location of the item.
     * @return an <code>int</code> at the specified <code>location</code>.
     * @throws NumberFormatException if the value at specified location
     *         cannot parsed into an <code>int</code>
     */
    public int getInt(int location) throws NumberFormatException {
        int i = 0;
        String val = getString(location).trim();
        val = prepareNumberValue(val);

        try {
            i = nf.parse(val).intValue();
        }
        catch (Exception e) {
            try {
                i = Integer.parseInt(val);
            }
            catch (Exception l) {
                new NumberFormatException(l.getMessage());
            }
        }
        return i;
    }

    private String prepareNumberValue(String val) {
    	// fix problem with last char is minus sign
      	if (val.endsWith("-")) {
    		val = "-" + val.substring(0, val.length() - 1);
    	}

      	// fix problem with * = 10
      	if (val.startsWith("*")) {
      		val = "10" + val.substring(1);
      	}
      	
    	return val;
	}

	/**
     * Retrieves the int value of the datum contained
     * under specified column <code>label</code>.
     *
     * @param label column label
     * @return a <code>int</code> value
     * @throws NumberFormatException if specified column <code>label</code>
     *         does not exist or the value cannot parsed into an 
     *         <code>int</code>
     */
    public int getInt(String label) throws NumberFormatException {
        int i = 0;
        int idx = getIndex(label);
        if (idx >= 0) {
            i = getInt(idx);
        }
        else {
            throw new NumberFormatException("cannot parse null - check label");
        }
        return i;
    }

    /**
     * Retrieves the int value of the datum contained
     * in the specified location.  If the specified location does not
     * contain a int value or empty, default value is returned.
     *
     * @param location   location of the item.
     * @param defaultVal default value to use when value does not exist.
     * @return a <code>int</code> value
     */
    public int getInt(int location, int defaultVal) {
        String val = getString(location).trim();
        val = prepareNumberValue(val);
        try {
            defaultVal = nf.parse(val).intValue();
        }
        catch(ParseException e) {
            try {
                defaultVal = Integer.parseInt(val);
            }
            catch (Exception l) {
                // do nothing
            }
        }
        return defaultVal;
    }
    
    /** Retrieves the <code>int</code> value at specified
     * <code>location</code> parsed using <code>format</code>.
     * If the value does not exist or fails to parse into
     * <code>int</code>, <code>defaultVal</code> is returned.
     * @param location location of <code>int</code> value
     * @param format <code>int</code> value parser to be used in extracting
     * item.
     * @param defaultVal default value to use in case of missing data or
     * parse error.
     * @return an <code>int</code> value at specified <code>location</code>
     * or the <code>defaultVal</code>
     */    
    public int getInt(int location, DecimalFormat format, int defaultVal) {
        String val = getString(location).trim();
        try {
            defaultVal = format.parse(val).intValue();
        }
        catch (ParseException e) {
            try {
                defaultVal = Integer.parseInt(val);
            }
            catch (NumberFormatException ne) {
            }
        }
        return defaultVal;
    }
        

    /**
     * Retrieves the <code>int</code> value under the specified
     * column <code>label</code>.
     *
     * @param label      column label (header)
     * @param defaultVal default value to use when value does not exist.
     * @return <code>int</code> value corresponding to the
     *         specified column <code>label</code>.
     *         If the specified column <code>label</code>
     *         does not exist or cannot be convert into an
     *         <code>int</code> default value is returned.
     */
    public int getInt(String label, int defaultVal) {
        int idx = getIndex(label);
        if (idx >= 0) {
            defaultVal = getInt(idx, defaultVal);
        }
        return defaultVal;
    }

    /**
     * Retrieves the String value contained in the 
     * specified location.  
     *
     * @param  location an <code>int</code> value
     * @return a <code>String</code> value or emty String
     *         if specified location does not exist. (never
     *         returns null)
     */
    public String getString(int location) {
        String s = "";
        try {
            s = (String)items.get(location);
        }
        catch (IndexOutOfBoundsException e) {
            // do nothing.
        }
        return s;
    }

    /**
     * Retrieves the <code>String</code> value located
     * under specified column <code>label</code>.
     *
     * @param label column label
     * @return a <code>String</code> value corresponding
     *         to specified column <code>label</code>.  If the column
     *         does not exist, empty <code>String</code> is returned.
     */
    public String getString(String label) {
        String s = "";
        int idx = getIndex(label);
        if (idx >= 0) {
            s = getString(idx);
        }
        return s;
    }
     

    /**
     * Retrieves the String value contained in the
     * specified location or <code>defaultVal</code> if it
     * does not.
     *
     * @param location location of the item (starts at zero)
     * @param defaultVal default value to use when the 
     *                   does not contain a value.
     * @return a <code>String</code> value at specified
     *         <code>location</code>.  If the value does not
     *         exist, <code>defaultVal</code> is returned.
     */
    public String getString(int location, String defaultVal) {
        try {
            String tmp = (String)items.get(location);
            if (tmp.length() != 0) {
                defaultVal = tmp;
            }
        }
        catch (IndexOutOfBoundsException e) {
            // do nothing
        }
        return defaultVal;
    }

    /**
     * Retrieves the <code>String</code> value contained
     * under the specified column <code>label</code>.
     *
     * @param label column label
     * @param defaultVal default value to return when value does not
     *                   exist or <code>label</code> fails to match
     *                   any of the column names in header row.
     * @return a <code>String</code> value corresponding to specified
     *         column <code>label</code>.  If no such column exists
     *         or the value is empty, <code>devaultVal</code> is
     *         returned.
     */
    public String getString(String label, String defaultVal) {
        int idx = getIndex(label);
        if (idx >= 0) {
            defaultVal = getString(idx, defaultVal);
        }
        return defaultVal;
    }

    /**
     * Method for <code>DataFile</code> to set the shared
     * instance of a <code>Hashtable</code> that contains
     * column label to index reference.
     *
     * @param index column label to index reference table.
     */
    void setHeaderIndex(Hashtable index) {
        this.index = index;
    }
  
    /**
     * Retrieves the number of location in row.
     *
     * @return number of items contined in <code>DataRow</code>
     */
    public int size() {
        return items.size();
    }

    /**
     * Helper method to retrieve column index for the specified
     * column label.
     *
     * @param label column label
     * @return column index corresponding to column label
     */
    private int getIndex(String label) {
        int loc = -1;
        if (index != null) {
            Integer idx = (Integer)index.get(label);
            if (idx != null) {
                loc = idx.intValue();
            }
        }
        return loc;
    }

    /**
     * <b>For debugging purpose</b>.
     * Converts the row of data into String
     * @return String representation of data row.
     */    
    public String toString() {
        StringBuffer o = new StringBuffer("[DataRow]");
        for (Iterator i = items.iterator(); i.hasNext();) {
            o.append("|'").append((String)i.next()).append("'");
        }
        return o.toString();
    }
    
    /**
     * Retrieves an immutable list of values contained within
     * the instance of DataRow.
     * @return immutable list of values in DataRow.
     */
    public List getValues()
    {
        return Collections.unmodifiableList(items);
    }

	/**
	 * Remove the content of DataRow.
	 */
	public void clear() {
		items.clear();
	}

} 
