/*
 * $Id: DataRowTest.java,v 1.3 2005/12/19 12:31:28 oldman1004 Exp $
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

import junit.framework.TestCase;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataFileFactory;
import com.infomata.data.DataRow;

/**
 * 
 * @author <a href="mailto:oldman1004@gmail.com">Sam Kim</a>
 *
 */
public class DataRowTest extends TestCase
{
    File file = null;
    
    public void setUp() throws IOException
    {
        file = new File("data-row-test.csv");
        file.deleteOnExit();
        DataFile df = DataFileFactory.createWriter("8859_1", false);
        df.setDataFormat(new CSVFormat());
        try
        {
            df.open(file);
            DataRow row = df.next();
            row.add("item 1");
            row.add("item 2");
            row.add(1);
        }
        finally
        {
            df.close();
        }
        
    }
    
    public void testSize()
    {
        DataRow row = new DataRow();
        row.add("item 1");
        row.add("item 2");
        assertEquals(2, row.size());
    }
    
    public void testSizeOnRead() throws IOException
    {
        DataFile df = DataFileFactory.createReader("8859_1");
        df.open(file);
        DataRow row = df.next();
        assertEquals(3, row.size());
        df.close();
    }
    
    public void tearDown()
    {
        
    }
}
