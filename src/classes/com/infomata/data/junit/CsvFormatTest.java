/*
 * $Id: CsvFormatTest.java,v 1.6 2005/12/26 01:54:55 oldman1004 Exp $
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
package com.infomata.data.junit;

import java.text.DecimalFormat;
import com.infomata.data.*;

/**
 * Test for CSVFormat
 *
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.6 $
 */
public class CsvFormatTest extends DataFormatUT {

    protected void setUp() {
        fmt = new CSVFormat();
    }

    public void testAddNullOrEmpty() {
        DataRow row = new DataRow();
        row.add(12.5d);
        row.add(null);
        row.add("");
        String val = fmt.format(row);
        assertEquals("12.5,null,", val);
    }

    public void testTextWithNewLine() {
        DataRow row = 
            fmt.parseLine("This is a test, \"Only a \"\" ");
        assertNull("must return null if quotes are not closed.", row);
        row = fmt.parseLine("test,,,\"");
        assertNotNull(row);
        assertEquals("Only a \" \ntest,,,", row.getString(1));
    }

    public void testTextConent() {
        DataRow row = fmt.parseLine("This is a test, \"Only a \"\" test,,,\"");
        assertNotNull("parsed row should not be null!", row);
        assertEquals("unescaped text", "This is a test", row.getString(0));
        assertEquals("escaped text", "Only a \" test,,,", row.getString(1));
    }

    public void testNumericParse() {
        DataRow row = fmt.parseLine("1.2,3,5.7865,123");
        applyNumericAssertions(row);
    }

    public void testNumericParseWithLeadingSpace() {
        DataRow row = fmt.parseLine(" 1.2,3,5.7865, 123");
        applyNumericAssertions(row);
    }

    public void testFormatting() {
        DataRow row = new DataRow(new DecimalFormat("####0.#######"));
        row.add(1.2d);
        row.add(3);
        row.add(5.7865d);
        row.add(123);
        row.add("This is a \"test\"");
        row.add("Test with an unexpected\nfor some reason.");
        String res = fmt.format(row);
        assertEquals("1.2,3,5.7865,123,\"This is a \"\"test\"\"\",\"Test with an unexpected\nfor some reason.\"", res);
    }

    public void testFormattingWithLeadingSpace() {
        DataRow row = new DataRow();
        row.add(" this is a test");
        row.add(" This is a \"test\"");
        String res = fmt.format(row);
        assertEquals("\" this is a test\",\" This is a \"\"test\"\"\"", res);
    }
    
    public void testLastEmptyElement()
    {
        CSVFormat fmt = new CSVFormat();
        DataRow row = new DataRow();
        row.add("item 1");
        row.add(2);
        row.add(3.123);
        row.addEmpty();
        String csvfmt = fmt.format(row);
        row = fmt.parseLine(csvfmt);
        assertEquals(4, row.size());
    }
    
    public void testFirstEmptyElement()
    {
        CSVFormat fmt = new CSVFormat();
        DataRow row = new DataRow();
        row.addEmpty();
        row.add("item 2");
        row.add(3);
        row.add("four?");
        String str = fmt.format(row);
        row = fmt.parseLine(str);
        assertEquals(4, row.size());
    }

    /*
    public static Test suite() {
        return new TestSuite(CsvFormatTest.class);
    }
    */

}
