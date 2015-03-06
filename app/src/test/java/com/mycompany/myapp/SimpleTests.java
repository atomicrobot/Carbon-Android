package com.mycompany.myapp;

import android.os.Bundle;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class SimpleTests {
    @Test
    public void testTrueIsTrue() {
        Assert.assertTrue(true);
    }

    @Test
    public void testMocking() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        Mockito.when(mockBundle.getString("key")).thenReturn("value");

        String value = mockBundle.getString("key");
        Assert.assertEquals("value", value);
    }
}
