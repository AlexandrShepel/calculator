package com.shpp.p2p.cs.ashepel.assignment10.equationProcessing;

import com.shpp.p2p.cs.ashepel.assignment10.utility.MathNotation;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquationParserTest {

    private void assertMapsEquals(
            HashMap<Integer, MathNotation> expected, HashMap<Integer, MathNotation> actual) {

        for (int key: expected.keySet()) {
            assertTrue(actual.containsKey(key));
            assertEquals(expected.get(key).toString(), actual.get(key).toString());
        }
    }

    @Test
    void getNotationsTest0() {
        String input = "1+1";
        HashMap<Integer, MathNotation> expected = new HashMap<>();
        MathNotation mathNotation;
        mathNotation =
                new MathNotation(0, 1, "+", "1", "1");
        expected.put(mathNotation.getPriority(), mathNotation);

        EquationParser equationParser = new EquationParser(input);
        assertMapsEquals(expected, equationParser.getNotations());
    }

    @Test
    void getNotationsTest1() {
        String input = "a^1";
        HashMap<Integer, MathNotation> expected = new HashMap<>();
        MathNotation mathNotation;
        mathNotation =
                new MathNotation(0, 1, "^", "a", "1");
        expected.put(mathNotation.getPriority(), mathNotation);

        EquationParser equationParser = new EquationParser(input);
        assertMapsEquals(expected, equationParser.getNotations());
    }

    @Test
    void getNotationsTest2() {
        String input = "a+b-c";
        HashMap<Integer, MathNotation> expected = new HashMap<>();
        MathNotation mathNotation;
        mathNotation =
                new MathNotation(0, 3, "-", "b", "c");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(1, 1, "+", "a", "@@0");
        expected.put(mathNotation.getPriority(), mathNotation);

        EquationParser equationParser = new EquationParser(input);
        assertMapsEquals(expected, equationParser.getNotations());
    }

    @Test
    void getNotationsTest3() {
        String input = "aa11+bb22-cc33*11.00/22.00^33.00";
//                     "aa11+bb22-cc33*11.00/@@@@@@@@@@0"
//                     "aa11+bb22-@@@@@@@@@1/@@@@@@@@@@0"
//                     "aa11+bb22-@@@@@@@@@@@@@@@@@@@@@2"
//                     "aa11+@@@@@@@@@@@@@@@@@@@@@@@@@@3"
//                     "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@4"
        int fooIndex = 0;
        HashMap<Integer, MathNotation> expected = new HashMap<>();
        MathNotation mathNotation;
        mathNotation =
                new MathNotation(0, fooIndex, "^", "22.00", "33.00");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(1, fooIndex, "*", "cc33", "11.00");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(2, fooIndex, "/", "@@@@@@@@@1", "@@@@@@@@@@0");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(3, fooIndex, "-", "bb22", "@@@@@@@@@@@@@@@@@@@@@2");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(4, fooIndex, "+", "aa11", "@@@@@@@@@@@@@@@@@@@@@@@@@@3");
        expected.put(mathNotation.getPriority(), mathNotation);

        EquationParser equationParser = new EquationParser(input);
        assertMapsEquals(expected, equationParser.getNotations());
    }

    @Test
    void getNotationsTest4() {
        String input = "-aa11+bb22-cc33*11.00/22.00^33.00";
//                     "-aa11+bb22-cc33*11.00/@@@@@@@@@@0"
//                     "-aa11+bb22-@@@@@@@@@1/@@@@@@@@@@0"
//                     "-aa11+bb22-@@@@@@@@@@@@@@@@@@@@@2"
//                     "-aa11+@@@@@@@@@@@@@@@@@@@@@@@@@@3"
//                     "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@4"
        int fooIndex = 0;
        HashMap<Integer, MathNotation> expected = new HashMap<>();
        MathNotation mathNotation;
        mathNotation =
                new MathNotation(0, fooIndex, "^", "22.00", "33.00");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(1, fooIndex, "*", "cc33", "11.00");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(2, fooIndex, "/", "@@@@@@@@@1", "@@@@@@@@@@0");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(3, fooIndex, "-", "bb22", "@@@@@@@@@@@@@@@@@@@@@2");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(4, fooIndex, "+", "-aa11", "@@@@@@@@@@@@@@@@@@@@@@@@@@3");
        expected.put(mathNotation.getPriority(), mathNotation);

        EquationParser equationParser = new EquationParser(input);
        assertMapsEquals(expected, equationParser.getNotations());
    }

    @Test
    void getNotationsTest5() {
        String input = "-aa11+bb22-cc33*-11.00/-22.00^-33.00";
//                     "-aa11+bb22-cc33*-11.00/@@@@@@@@@@@@0"
//                     "-aa11+bb22-@@@@@@@@@@1/@@@@@@@@@@@@0"
//                     "-aa11+bb22-@@@@@@@@@@@@@@@@@@@@@@@@2"
//                     "-aa11+@@@@@@@@@@@@@@@@@@@@@@@@@@@@@3"
//                     "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@4"
        int fooIndex = 0;
        HashMap<Integer, MathNotation> expected = new HashMap<>();
        MathNotation mathNotation;
        mathNotation =
                new MathNotation(0, fooIndex, "^", "-22.00", "-33.00");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(1, fooIndex, "*", "cc33", "-11.00");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(2, fooIndex, "/", "@@@@@@@@@@1", "@@@@@@@@@@@@0");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(3, fooIndex, "-", "bb22", "@@@@@@@@@@@@@@@@@@@@@@@@2");
        expected.put(mathNotation.getPriority(), mathNotation);
        mathNotation =
                new MathNotation(4, fooIndex, "+", "-aa11", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@3");
        expected.put(mathNotation.getPriority(), mathNotation);

        EquationParser equationParser = new EquationParser(input);
        assertMapsEquals(expected, equationParser.getNotations());
    }

}