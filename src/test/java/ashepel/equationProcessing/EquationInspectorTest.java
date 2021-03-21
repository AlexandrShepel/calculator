package ashepel.equationProcessing;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class EquationInspectorTest {

    private void assertException(String input0, HashSet<String> input1, String expected) {
        EquationInspector equationInspector = new EquationInspector();

        try {
            equationInspector.inspect(input0, input1);
            fail("Program must throws exception, but it doesn't.");
        }

        catch (IOException ioException) {
            assertEquals(expected, ioException.getMessage());
        }
    }

    @Test
    void runTest0() {
        String input0 = "1";
        HashSet<String> input1 = new HashSet<>();
        String expected = "The equation doesn't contain any math logic.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest1() {
        String input0 = "abc";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        input1.add("c");
        String expected = "The equation doesn't contain any math logic.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest2() {
        String input0 = "a+1.0+@";
        HashSet<String> input1 = new HashSet<>();
        String expected = "The equation contains unexpected symbol \"@\"";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest3() {
        String input0 = "a+1b+c";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("1b");
        input1.add("c");
        String expected = "Parameter's name can't start with digit.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest4() {
        String input0 = "a+b+c";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("c");
        String expected = "The parameter \"b\" is not specified.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest5() {
        String input0 = "a+b";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        input1.add("c");
        String expected = "You specified unused parameter(-s)";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest6() {
        String input0 = "+a+b+c";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        input1.add("c");
        String expected = "The equation contains unexpected symbol \"+\" on the 0 position";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest7() {
        String input0 = "a+b+c+";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        input1.add("c");
        String expected = "The equation contains unexpected symbol \"+\" on the last position";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest8() {
        String input0 = "a+b+1..0";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        String expected = "The equation contains unexpected point.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest9() {
        String input0 = "a+b.b+c";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        input1.add("c");
        String expected = "The equation contains unexpected point.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest10() {
        String input0 = "a+b++c";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        input1.add("c");
        String expected = "Unexpected symbols sequence \"++\"";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest11() {
        String input0 = "-a*-b+-c";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        input1.add("c");
        String expected = "Unexpected symbols sequence \"+-\"";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest12() {
        String input0 = "a+b2.0";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b2");
        String expected = "Missed operator on the position 3.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest13() {
        String input0 = "a+b(a+b)";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        String expected = "Unknown function \"b\".";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest14() {
        String input0 = "a+b+()";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        String expected = "The equation contains incorrect brackets.";

        assertException(input0, input1, expected);
    }

    @Test
    void runTest15() {
        String input0 = "a+b+((a+b)";
        HashSet<String> input1 = new HashSet<>();
        input1.add("a");
        input1.add("b");
        String expected = "The equation contains incorrect brackets.";

        assertException(input0, input1, expected);
    }


}