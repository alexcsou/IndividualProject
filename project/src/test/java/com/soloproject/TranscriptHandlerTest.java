package com.soloproject;

import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TranscriptHandlerTest {

    private TranscriptHandler handler;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void assertStreamButtonNotNull() {
        assertNotEquals(null, handler.getStreamFileSelectButton());
    }

}
