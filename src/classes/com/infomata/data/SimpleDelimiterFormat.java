/*
 * $Id: SimpleDelimiterFormat.java,v 1.2 2005/12/19 12:31:29 oldman1004 Exp $
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
 * SimpleDelimiterFormat is configurable data format that uses
 * simple delimiter and escape sequence scheme to encode data into a
 * text file.
 * <p style="font-weight:bold;">
 * Examples:
 * </p>
 * <table class="example">
 * <tr>
 *   <th class="example">Format</th>
 *   <th class="example">Instantiation</th>
 * </tr>
 * <tr>
 *   <td class="example">Tab separated</td>
 *   <td class="example"><code>new SimpleDelimiterFormat("\t", null)</code></td>
 * </tr>
 * <tr>
 *   <td class="example">Pipe separated</td>
 *   <td class="example"><code>new SimpleDelimiterFormat("|", "\")</code></td>
 * </tr>
 * </table>
 * 
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.2 $
 */
public class SimpleDelimiterFormat implements DataFormat {

    private char[] delimiter = null;
    private char[] escape = null;

    /**
     * Creates a new instance of SimpleDelimiterFormat with
     * specified delimiter and escape sequence.
     * 
     * @param delimiter - character sequence used to separate one element
     *                    from another.  <code>delimiter</code> 
     * 					  CANNOT be NULL or empty string.
     * @param escape    - character sequence used to signal that next
     *                    character sequence (either <code>delimiter</code>
     * 					  or <code>escape</code>) is part of data.
     */
    public SimpleDelimiterFormat(String delimiter, String escape) throws IllegalArgumentException {
        	
        if (delimiter == null || delimiter.length() == 0) {
            throw new IllegalArgumentException("delimter cannot be null or empty.");
        }

        this.delimiter = delimiter.toCharArray();

        if (escape == null) {
            this.escape = new char[0];
        }
        else {
            this.escape = escape.toCharArray();
        }
    }

    /* (non-Javadoc)
     * @see com.infomata.data.DataFormat#parseLine(java.lang.String)
     */
    public DataRow parseLine(String line) {

        DataRow row = new DataRow();

        if (line != null && line.length() > 0) {

            StringBuffer o = new StringBuffer();

            char[] ch = line.toCharArray();
            int start = 0;
            boolean ignore = false;

            for (int i = 0; i < ch.length; i++) {
                if (ch[i] == delimiter[0] && isDelimiter(ch, i)) {
                    if (ignore) {
                        ignore = false;
                    }
                    else {
                        row.add(decodeData(new String(ch, start, i - start)));
                        start = i + delimiter.length;
                    }
                    i += delimiter.length - 1;
                }
                else if (escape.length > 0 && ch[i] == escape[0] && isEscape(ch, i)) {
                    ignore = !ignore;
                    i += escape.length - 1;
                }
            }
            o.append(new String(ch, start, ch.length - start));
            row.add(o.toString());
        }
        return row;
    }

    /* (non-Javadoc)
     * @see com.infomata.data.DataFormat#format(com.infomata.data.DataRow)
     */
    public String format(DataRow row) {
        StringBuffer o = new StringBuffer();
        if (row != null && row.size() > 0) {
            o.append(encodeData(row.getString(0)));
            for (int i = 1; i < row.size(); i++) {
                o.append(delimiter);
                o.append(encodeData(row.getString(i)));
            }
        }
        return o.toString();
    }

    private String decodeData(String content) {
        StringBuffer o = new StringBuffer();
        if (content == null) {
            o.append("null");
        }
        else if (content.length() > 0) {
            char[] ch = content.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (escape.length > 0 && ch[i] == escape[0] && isEscape(ch, i)) {
                    i += escape.length;
                }
                o.append(ch[i]);
            }
        }
        return o.toString();
    }

    private String encodeData(String content) {
        StringBuffer o = new StringBuffer();
        if (content == null) {
            o.append("null");
        }
        else if (content.length() > 0) {
            char[] ch = content.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if ((ch[i] == delimiter[0] && isDelimiter(ch, i))
                    || (escape.length > 0 && ch[i] == escape[0] && isEscape(ch, i))) {
                    o.append(escape);
                }
                o.append(ch[i]);
            }
        }
        return o.toString();
    }

    private boolean isDelimiter(char[] ch, int offset) {
        return match(ch, delimiter, offset);
    }

    private boolean isEscape(char[] ch, int offset) {
        return match(ch, escape, offset);
    }
    
    
	private boolean match(char[] ch, char[] sp, int offset) {
		for (int i = 0; i < sp.length && i + offset < ch.length; i++) {
			if (ch[offset + i] != sp[i]) {
				return false;
			}
		}
		return true;
	}

    /*
    protected void out(Object o) {
        System.out.print(o.toString());
    }
    protected void outln(Object o) {
        System.out.println(o.toString());
    }
    */

}
