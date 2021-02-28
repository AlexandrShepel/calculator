package com.shpp.p2p.cs.ashepel.assignment10;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Assignment10SystemOutTest {

    private ByteArrayOutputStream customStream;
    private static final PrintStream originalStream = System.out;

    @Test
    void mainTest0() {
        String[] input = {"1+1"};
        String expected = "2.0";
        Assignment10.main(input);
        String actual = customStream.toString();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest1() {
        String[] input = {"1/0"};
        String expected = "Infinity";
        Assignment10.main(input);
        String actual = customStream.toString();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest2() {
        String[] input = {"10^0"};
        String expected = "1.0";
        Assignment10.main(input);
        String actual = customStream.toString();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest3() {
        String[] input = {"a+b", "a=1", "b=2"};
        String expected = "3.0";
        Assignment10.main(input);
        String actual = customStream.toString();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest4() {
        String[] input = {"-a+b", "a=2", "b=1"};
        String expected = "-1.0";
        Assignment10.main(input);
        String actual = customStream.toString();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest5() {
        String[] input = {"-a^-b", "a=2", "b=2"};
        String expected = "0.25";
        Assignment10.main(input);
        String actual = customStream.toString();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest6() {
        String[] input = {"-a^-b+98*a-6/100+300*-var+111/-var", "a=2", "b=2", "var=7"};
        String expected = "-1919.6671428571428";
        Assignment10.main(input);
        String actual = customStream.toString();

        assertEquals(expected, actual);
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