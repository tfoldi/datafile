package com.infomata.data.junit;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataFileFactory;
import com.infomata.data.DataRow;

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
