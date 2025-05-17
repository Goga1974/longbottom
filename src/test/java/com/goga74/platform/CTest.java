package com.goga74.platform;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CTest
{
    final String TEST = "Hello World!";

    @Test
    public void check1()
    {
        assertEquals(1, CharCounter.Test(TEST).get('H'));
        assertEquals(3, CharCounter.Test(TEST).get('l'));
    }

}
