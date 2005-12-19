/*
* Copyright (c) 2004 Infomata.  All rights reserved.
* $Id: DataFormatUT.java,v 1.3 2005/12/19 12:31:28 oldman1004 Exp $
*/
package com.infomata.data.junit;

import com.infomata.data.DataFormat;
import com.infomata.data.DataRow;

import junit.framework.TestCase;

/**
 * DataFormatTest.java
 *
 *
 * Created: Thu Apr 22 12:02:50 2004
 *
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.3 $
 */
public abstract class DataFormatUT extends TestCase {

    protected DataFormat fmt = null;

    protected abstract void setUp();

    protected void applyNumericAssertions(DataRow row) {
        assertTrue(row.getDouble(0) == 1.2d);
        assertTrue(row.getInt(1) == 3);
        assertTrue(row.getDouble(2) == 5.7865d);
        assertTrue(row.getInt(3) == 123);
    }
    
    protected void out(Object o) {
    	System.out.print(o.toString());
    }
    protected void outln(Object o) {
     	System.out.println(o.toString());
    }

    protected void tearDown() {
        fmt = null;
    }
    
} // DataFormatTest
