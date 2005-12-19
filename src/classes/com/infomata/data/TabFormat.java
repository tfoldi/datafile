/*
 * $Id: TabFormat.java,v 1.3 2005/12/19 12:31:29 oldman1004 Exp $
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

import java.util.Iterator;

/**
 * Implementation of DataFormat interface for tab delimited
 * data file.
 *
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.3 $
 */
public class TabFormat implements DataFormat {

    /**
     * parses a line of data separated by tab
     * into <code>DataRow</code> object.
     *
     * @param line <code>String</code> containing
     *             data separated by tab character.
     * @return <code>DataRow</code> object containing
     *         data parsed from <code>line</code>
     */
    public DataRow parseLine(String line) {

        DataRow row = null;

        if (line != null) {
      
            row = new DataRow();

            int last = 0;
            int idx = line.indexOf(TAB, last);
      
            while (idx >= 0) {
                row.add(line.substring(last, idx));
                last = idx + 1;
                idx = line.indexOf(TAB, last);
            }

            row.add(line.substring(last));

        }

        return row;

    } // parseLine(String)

    /**
     * Converts DataRow instance into a tab-separated list of data.
     * @param row DataRow instance containing data.
     */
    public String format(DataRow row) {
    
        StringBuffer o = new StringBuffer();
        Iterator i = row.iterator();
        while (i.hasNext()) {
            if (o.length() > 0) {
                o.append(TAB);
            }
            o.append((String)i.next());
        }

        return o.toString();

    } // format(DataRow)
    
    /**
     * constant for tab character in <code>String</code> format.
     */
    private static final String TAB = "\t";

}
