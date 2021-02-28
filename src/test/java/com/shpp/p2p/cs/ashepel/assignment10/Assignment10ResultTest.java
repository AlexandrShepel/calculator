package com.shpp.p2p.cs.ashepel.assignment10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Assignment10ResultTest {

    @Test
    void mainTest0() {
        String[] input = {"1+1-1-1+1-1"};
        String expected = "0.0";
        Assignment10.main(input);
        String actual = "" + Assignment10.calculator.getResult();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest1() {
        String[] input = {"1/0"};
        String expected = "Infinity";
        Assignment10.main(input);
        String actual = "" + Assignment10.calculator.getResult();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest2() {
        String[] input = {"10^1^2"};
        String expected = "10.0";
        Assignment10.main(input);
        String actual = "" + Assignment10.calculator.getResult();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest3() {
        String[] input = {"a+b", "a=1", "b=2"};
        String expected = "3.0";
        Assignment10.main(input);
        String actual = "" + Assignment10.calculator.getResult();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest4() {
        String[] input = {"-a+b", "a=2", "b=1"};
        String expected = "-1.0";
        Assignment10.main(input);
        String actual = "" + Assignment10.calculator.getResult();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest5() {
        String[] input = {"-a^-b", "a=2", "b=2"};
        String expected = "0.25";
        Assignment10.main(input);
        String actual = "" + Assignment10.calculator.getResult();

        assertEquals(expected, actual);
    }

    @Test
    void mainTest6() {
        String[] input = {"-a^-b+98*a-6/100+300*-var+111/-var", "a=2", "b=2", "var=7"};
        String expected = "-1919.6671428571428";
        Assignment10.main(input);
        String actual = "" + Assignment10.calculator.getResult();

        assertEquals(expected, actual);
    }

}