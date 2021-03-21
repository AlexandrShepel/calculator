package ashepel.parametersProcessing;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ParametersParserTest {

    @Test
    void getParametersTest0() {
        String[] input = {"a=1.0", "b=2.0", "c=3.0"};
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("a", 1.0);
        expected.put("b", 2.0);
        expected.put("c", 3.0);

        ParametersParser parametersParser = new ParametersParser();
        parametersParser.parse(input);
        assertEquals(expected, parametersParser.getParametersHashMap());
    }

    @Test
    void getParametersTest1() {
        String[] input = {"a=1", "b=2", "c=3"};
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("a", 1.0);
        expected.put("b", 2.0);
        expected.put("c", 3.0);

        ParametersParser parametersParser = new ParametersParser();
        parametersParser.parse(input);
        assertEquals(expected, parametersParser.getParametersHashMap());
    }

    @Test
    void getParametersTest2() {
        String[] input = {"a  =  1", "b   = 2", "c          = 3"};
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("a", 1.0);
        expected.put("b", 2.0);
        expected.put("c", 3.0);

        ParametersParser parametersParser = new ParametersParser();
        parametersParser.parse(input);
        assertEquals(expected, parametersParser.getParametersHashMap());
    }

    @Test
    void getParametersTest3() {
        String[] input = {"a = -1", "b = -2", "c = -3"};
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("a", -1.0);
        expected.put("b", -2.0);
        expected.put("c", -3.0);

        ParametersParser parametersParser = new ParametersParser();
        parametersParser.parse(input);
        assertEquals(expected, parametersParser.getParametersHashMap());
    }

    @Test
    void getParametersTest4() {
        String[] input = {"a = 1000000", "b = 2000000", "c = 3000000"};
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("a", 1000000.0);
        expected.put("b", 2000000.0);
        expected.put("c", 3000000.0);

        ParametersParser parametersParser = new ParametersParser();
        parametersParser.parse(input);
        assertEquals(expected, parametersParser.getParametersHashMap());
    }

    @Test
    void getParametersTest5() {
        String[] input = {"ant = 1", "board = 2", "caterpillar = 3"};
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("ant", 1.0);
        expected.put("board", 2.0);
        expected.put("caterpillar", 3.0);

        ParametersParser parametersParser = new ParametersParser();
        parametersParser.parse(input);
        assertEquals(expected, parametersParser.getParametersHashMap());
    }

    @Test
    void getParametersTest6() {
        String[] input = {"Ant = 1", "boArd = 2", "CaterPillAR = 3"};
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("Ant", 1.0);
        expected.put("boArd", 2.0);
        expected.put("CaterPillAR", 3.0);

        ParametersParser parametersParser = new ParametersParser();
        parametersParser.parse(input);
        assertEquals(expected, parametersParser.getParametersHashMap());
    }
}