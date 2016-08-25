package net.maiatoday.minotaur.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by maia on 2016/08/25.
 */
public class SomeUtilTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void justFunctionTest() throws Exception {
        int i = 2;
        int j = 3;
        int expectedResult = i+j;
        assertEquals("test", expectedResult, SomeUtil.justFunction(i, j));

    }

    @Test
    public void justFunctionNotTest() throws Exception {
        int i = 2;
        int j = 3;
        int expectedResult = i+j;
        assertNotEquals("test", expectedResult, SomeUtil.justFunction(i, j+1));

    }



}