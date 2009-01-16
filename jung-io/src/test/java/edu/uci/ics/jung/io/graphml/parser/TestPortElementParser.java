/*
 * Copyright (c) 2008, the JUNG Project and the Regents of the University
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */

package edu.uci.ics.jung.io.graphml.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.PortMetadata;

public class TestPortElementParser extends AbstractParserTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test(expected= GraphIOException.class)
    public void testNoName() throws Exception {
        
        String xml = 
            "<port/>";
        
        readObject(xml);
    }

    @Test
    public void testName() throws Exception {
        
        String xml = 
            "<port name=\"p1\"/>";
        
        PortMetadata port = (PortMetadata) readObject(xml);
        Assert.assertNotNull(port);
        Assert.assertEquals("p1", port.getName());
        Assert.assertEquals(null, port.getDescription());
    }

    @Test
    public void testDesc() throws Exception {
        
        String xml = 
            "<port name=\"p1\">" +
                "<desc>this is my port</desc>" +
            "</port>";
        
        PortMetadata port = (PortMetadata) readObject(xml);
        Assert.assertNotNull(port);
        Assert.assertEquals("p1", port.getName());
        Assert.assertEquals("this is my port", port.getDescription());
    }

    @Test
    public void testUserAttributes() throws Exception {
        
        String xml = 
            "<port name=\"p1\" bob=\"abc123\"/>";
        
        PortMetadata port = (PortMetadata) readObject(xml);
        Assert.assertNotNull(port);
        Assert.assertEquals("p1", port.getName());
        Assert.assertEquals(1, port.getProperties().size());
        Assert.assertEquals("abc123", port.getProperty("bob"));
    }

    @Test
    public void testData() throws Exception {
        
        String xml =
            "<port name=\"p1\">" +
                "<data key=\"d1\">value1</data>" +
                "<data key=\"d2\">value2</data>" +
            "</port>";
        
        PortMetadata port = (PortMetadata) readObject(xml);
        Assert.assertNotNull(port);
        Assert.assertEquals("p1", port.getName());
        Assert.assertEquals(2, port.getProperties().size());
        Assert.assertEquals("value1", port.getProperty("d1"));
        Assert.assertEquals("value2", port.getProperty("d2"));
    }
}