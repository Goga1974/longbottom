package com.goga74.platform.util;

import com.goga74.platform.CharCounter;
import io.swagger.v3.oas.annotations.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilTest
{
    private final String TEST = "Hello World!";

    @Test
    public void testConvertToJson()
    {

    }

    @Test
    public void check1()
    {
        assertEquals(1, CharCounter.Test(TEST).get('H'));
        assertEquals(3, CharCounter.Test(TEST).get('l'));
    }

    private static Stream<Arguments> stringProvider()
    {
        return Stream.of(
                Arguments.of("Test Data One", 'a', 2),
                Arguments.of("Test Data Two", 'T', 2),

                Arguments.of(null, 'a', null), // ?
                Arguments.of("", 'a', null)
        );
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    public void check2(final String input, final Character controlChar, final Integer controlAmount)
    {
        assertEquals(controlAmount, CharCounter.Test(input).get(controlChar));
    }
}