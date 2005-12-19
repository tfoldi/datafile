/*
* Copyright (c) 2000 - 2004 Infomata.  All rights reserved.
* $Id: FixedWidthFormatTest.java,v 1.3 2005/12/19 12:31:28 oldman1004 Exp $
*/
package com.infomata.data.junit;

import java.text.DecimalFormat;

import com.infomata.data.DataRow;
import com.infomata.data.FixedWidthFormat;

/**
 * FixedWidthFormatTest.java
 *
 *
 * Created: Thu Apr 22 11:57:27 2004
 *
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.3 $
 */
public class FixedWidthFormatTest extends DataFormatUT {

    protected void setUp() {
        fmt = new FixedWidthFormat(new int[] {7, 7, 7, 7, 7});
    }
    
    public void testNumericParse() {
        //                           ^234567^234567^234567^234567
        DataRow row = fmt.parseLine("1.2    3      5.7865 123    ");
        applyNumericAssertions(row);
    }

    public void testLeadingSpace() {
        //                           ^234567^234567^234567^234567
        DataRow row = fmt.parseLine(" 1.2    3      5.7865 123   ");
        applyNumericAssertions(row);
    }

    public void testFormat() {
        DataRow row = new DataRow(new DecimalFormat("###0.#############"));
        row.add(1.2d);
        row.add(3);
        row.add(5.7865d);
        row.add(123);
        String res = fmt.format(row);
        // System.out.println("      :  ^234567^234567^234567^234567");
        // System.out.println("result: '" + res + "'");
        assertEquals("1.2    3      5.7865 123    ", res);
    }
    
} // FixedWidthFormatTest
