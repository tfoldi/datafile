package com.infomata.data.junit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataRow;

public class DataFileTest extends TestCase
{
    public void testFlushOnClose() throws IOException
    {
        DataFile df = null;

        File file1 = new File("datafile-test1.csv");
        file1.deleteOnExit();
        File file2 = new File("datafile-test2.csv");
        file2.deleteOnExit();

        try
        {
            df = DataFile.createWriter("8859_1", false);
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

            df = DataFile.createReader("8859_1");
            df.open(file2);
            row = df.next();
            assertNull(df.next());
        }
        finally
        {
        }
    }

    public void testHeaderList() throws IOException
    {
        DataFile df = null;
        File file = new File("headerlist-test.csv");
        file.deleteOnExit();

        df = DataFile.createWriter("8859_1", false);
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

        df = DataFile.createReader("8859_1");
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
