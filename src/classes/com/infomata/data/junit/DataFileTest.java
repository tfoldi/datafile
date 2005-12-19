/*
 * $Id: DataFileTest.java,v 1.3 2005/12/19 12:31:28 oldman1004 Exp $
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataFileFactory;
import com.infomata.data.DataRow;

/**
 * 
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 * @version $Revision: 1.3 $
 */
public class DataFileTest extends TestCase
{
	/**
	 * Tests if all data within DataFile instance is flushed when closed.
	 * 
	 * @throws IOException
	 */
    public void testFlushOnClose() throws IOException
    {
        DataFile df = null;

        File file1 = new File("datafile-test1.csv");
        file1.deleteOnExit();
        File file2 = new File("datafile-test2.csv");
        file2.deleteOnExit();

        try
        {
            df = DataFileFactory.createWriter("8859_1", false);
            df.setDataFormat(new CSVFormat());
            df.open(file1);
            DataRow row = df.next();
            row.add("test 1");
            row.add("test 2");
            df.close();
            df.open(file2);
            row = df.next();
            row.add("test 3");
            row.add("test 4");
            df.close();

            df = DataFileFactory.createReader("8859_1");
            df.open(file2);
            row = df.next();
            assertNull(df.next());
        }
        finally
        {
        }
    }

    /**
     * Checks if the header list does not lose the sequence.
     * @throws IOException
     */
    public void testHeaderList() throws IOException
    {
        DataFile df = null;
        File file = new File("headerlist-test.csv");
        file.deleteOnExit();

        df = DataFileFactory.createWriter("8859_1", false);
        df.setDataFormat(new CSVFormat());
        df.open(file);

        // add headers
        DataRow row = df.next();
        row.add("col 1");
        row.add("col 2");
        row.add("abc");
        row = df.next();
        row.add(1);
        row.add(2);
        row.add(3);
        df.close();

        df = DataFileFactory.createReader("8859_1");
        df.containsHeader(true);
        df.open(file);
        List headers = df.getHeaderList();
        assertEquals("col 2", headers.get(1));
        assertEquals("col 1", headers.get(0));
        assertEquals("abc", headers.get(2));
        row = df.next();
        assertEquals(3, row.getInt("abc"));
        assertEquals(2, row.getInt("col 2"));
        assertEquals(1, row.getInt("col 1"));
        df.close();
    }
}
