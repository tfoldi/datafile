/*
* Copyright (c) 2000 - 2004 Infomata.  All rights reserved.
* $Id: TabFormatTest.java,v 1.3 2005/12/19 12:31:28 oldman1004 Exp $
*/
package com.infomata.data.junit;

import java.text.DecimalFormat;

import com.infomata.data.DataFormat;
import com.infomata.data.DataRow;
import com.infomata.data.TabFormat;

/**
 * TabFormatTest.java
 *
 *
 * Created: Thu Apr 22 11:57:27 2004
 *
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.3 $
 */
public class TabFormatTest extends DataFormatUT {

    private DataFormat fmt = null;

    protected void setUp() {
        fmt = new TabFormat();
    }
    
    public void testNumericParse() {
        DataRow row = fmt.parseLine("1.2\t3\t5.7865\t123");
        applyNumericAssertions(row);
    }

    public void testLeadingSpace() {
        DataRow row = fmt.parseLine("1.2\t 3\t 5.7865\t123");
        applyNumericAssertions(row);
    }

    public void testFormatting() {
        DataRow row = new DataRow(new DecimalFormat("###0.########"));
        row.add(1.2d);
        row.add(3);
        row.add(5.7865d);
        row.add(123);
        row.add("Testing... one, two, three");
        String res = fmt.format(row);
        assertEquals("1.2\t3\t5.7865\t123\tTesting... one, two, three", res);
    }
    
} // TabFormatTest
