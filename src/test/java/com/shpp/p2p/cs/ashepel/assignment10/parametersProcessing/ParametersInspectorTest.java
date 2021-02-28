package com.shpp.p2p.cs.ashepel.assignment10.parametersProcessing;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParametersInspectorTest {

    private void assertException(String[] input, String expected) {
        ParametersInspector parametersInspector = new ParametersInspector(input);

        try {
            parametersInspector.run();
            fail("Program must trows exception, but it doesn't.");
        }

        catch (IOException ioException) {
            assertEquals(expected, ioException.getMessage());
        }
    }

    @Test
    void runTest0() {
        String[] input = {"a=1", "b", "c=3"};
        String expected = "The argument \"" + input[1] + "\" contains less than 3 symbols.";

        assertException(input, expected);
    }

    @Test
    void runTest1() {
        String[] input = {"a=1", "bb2", "c=3"};
        String expected = "The argument \"" + input[1] + "\" doesn't contain equal sign \"=\".";

        assertException(input, expected);
    }

    @Test
    void runTest2() {
        String[] input = {"a=1", "2=2", "c=3"};
        String expected = "The argument \"" + input[1] + "\" doesn't contain any letter.";

        assertException(input, expected);
    }

    @Test
    void runTest3() {
        String[] input = {"a=1", "b=b", "c=3"};
        String expected = "The argument \"" + input[1] + "\" doesn't contain any digit.";

        assertException(input, expected);
    }

    @Test
    void runTest4() {
        String[] input = {"a=1", "2b=2", "c=3"};
        String expected = "The argument \"" + input[1] + "\" can't start with a digit.";

        assertException(input, expected);
    }

    @Test
    void runTest5() {
        String[] input = {"a=1", "b-b=2", "c=3"};
        String expected = "Name of the parameter \"" + input[1] + "\" contains unexpected symbol \"-\".";

        assertException(input, expected);
    }

    @Test
    void runTest6() {
        String[] input = {"a=1", "b=2-2", "c=3"};
        String expected = "Value of the parameter \"" + input[1] + "\" contains unexpected symbol \"-\".";

        assertException(input, expected);
    }

    @Test
    void runTest7() {
        String[] input = {"a=1", "b=.2", "c=3"};
        String expected = "Value of the parameter \"" + input[1] + "\" contains unexpected \".\".";

        assertException(input, expected);
    }

    @Test
    void runTest8() {
        String[] input = {"a=1", "b=-.2", "c=3"};
        String expected = "Value of the parameter \"" + input[1] + "\" contains unexpected \".\".";

        assertException(input, expected);
    }

    @Test
    void runTest9() {
        String[] input = {"a=1", "b=2.", "c=3"};
        String expected = "Value of the parameter \"" + input[1] + "\" contains unexpected \".\".";

        assertException(input, expected);
    }

}