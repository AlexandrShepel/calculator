package ashepel;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Assignment11SystemOutTest {

    private ByteArrayOutputStream customStream;
    private static final PrintStream originalStream = System.out;

    void assertMainSystemOut(String[] input, String expected) {
        Assignment11.init();
        Assignment11.main(input);
        String expectedOutput = "Result = " + expected;
        String actual = customStream.toString();

        assertEquals(expectedOutput, actual);
    }

    @Test
    void mainTest0() {
        String[] input = {"1+1"};
        String expected = "2.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest1() {
        String[] input = {"1/0"};
        String expected = "Infinity";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest2() {
        String[] input = {"10^0"};
        String expected = "1.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest3() {
        String[] input = {"a+b", "a=1", "b=2"};
        String expected = "3.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest4() {
        String[] input = {"-a+b", "a=2", "b=1"};
        String expected = "-1.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest5() {
        String[] input = {"-a^-b", "a=2", "b=2"};
        String expected = "0.25";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest6() {
        String[] input = {"-a^-b+98*a-6/100+300*-var+111/-var", "a=2", "b=2", "var=7"};
        String expected = "-1919.6671428571428";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest7() {
        String[] input = {"(3+3)"};
        String expected = "6.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest8() {
        String[] input = {"3+(3+3)"};
        String expected = "9.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest9() {
        String[] input = {"(1+3)^2"};
        String expected = "16.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest10() {
        String[] input = {"2+(3+5)+(1+3^2)^2"};
        String expected = "110.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest11() {
        String[] input = {"3-(8-(2+2+(3^2))^2)+(33+53)"};
        String expected = "250.0";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest12() {
        String[] input = {"sin(3)"};
        String expected = "0.1411200080598672";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest13() {
        String[] input = {"sin(3+3)"};
        String expected = "-0.27941549819892586";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest14() {
        String[] input = {"sin(3+3) + sin(4^2)"};
        String expected = "-0.5673188148639912";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest15() {
        String[] input = {"sin(a)", "a=0.5235987755982"};
        String expected = "0.4999999999999144";

        assertMainSystemOut(input, expected);
    }


    @Test
    void mainTest16() {
        String[] input = {"1+(2+3*(4+5-sin(45*cos(a))))/7", "a=55"};
        String expected = "4.78322481188113";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest17() {
        String[] input = {"-2^(-3)"};
        String expected = "-0.125";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest18() {
        String[] input = {"(-2)^-3"};
        String expected = "-0.125";

        assertMainSystemOut(input, expected);
    }

    @Test
    void mainTest19() {
        String[] input = {"-(1+23/4^5+(-67/(cos(8)^9+sin(tan(atan(log2(10)^11)/12)*13)+14-1516))^17-18+(-19^(-20))*" +
                "(-21)+22^23+tan(24)-sqrt(25)-26+27^28/29-30)/31^a+sqrt(sqrt(625))", "a = 32"};
        String expected = "-5.0";

        assertMainSystemOut(input, expected);
    }

    @BeforeEach
    void setCustomOutputStream() {
        System.setOut(new PrintStream(customStream = new ByteArrayOutputStream()));
    }

    @AfterAll
    static void restoreOriginalOutputStream() {
        System.setOut(originalStream);
    }

}