package com.shpp.p2p.cs.ashepel.assignment10;

import com.shpp.p2p.cs.ashepel.assignment10.equationProcessing.EquationInspector;
import com.shpp.p2p.cs.ashepel.assignment10.equationProcessing.EquationParser;
import com.shpp.p2p.cs.ashepel.assignment10.parametersProcessing.ParametersInspector;
import com.shpp.p2p.cs.ashepel.assignment10.parametersProcessing.ParametersParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Represents a calculator. Receives an equation
 * and its parameters in the form of String values.
 * Checks them for correctness and returns calculations
 * result to the console.
 */
public class Assignment10 {

    public static Calculator calculator;

    /**
     * Receives input arguments. Starts an execution of the program.
     * Consists of 6 parts:
     *  -- input parameters correctness checking
     *  -- input parameters parsing
     *  -- input equation correctness checking
     *  -- input equation parsing
     *  -- calculation
     *  -- recalculation with another input parameters (if needed).
     *
     * @param args The input equation and its parameters:
     *             args[0] -- equation
     *             args[1]..args[N] -- parameters.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Equation is not specified.");
            System.exit(0);
        }

        String[] promptParameters = (args.length > 1) ? Arrays.copyOfRange(args, 1, args.length) : null;
        final String promptEquation = args[0];
        checkParameters(promptParameters);
        ParametersParser parametersParser = new ParametersParser(promptParameters);
        final HashSet<String> parametersHashSet = new HashSet<>(parametersParser.getParameters().keySet());
        checkEquation(promptEquation, parametersHashSet);
        final EquationParser equationParser = new EquationParser(promptEquation);

        calculator = new Calculator(equationParser.getNotations(), parametersParser.getParameters());
        System.out.println("Result = " + calculator.getResult());

        while (true) {
            if (promptOption().equals("1")) {
                calculator = new Calculator(
                        equationParser.getNotations(),
                        getPromptParameters(parametersParser).getParameters())
                ;
                System.out.println("Result = " + calculator.getResult());
            }

            else System.exit(0);
        }
    }

    /**
     * Prompts user for the option's number:
     *  "1" -- prompts a new parameters values
     *         and starts a new calculation cycle
     *  "2" -- exits an execution
     *
     * @return A prompt option's number.
     */
    private static String promptOption() {
        System.out.println("Option \"1\" -- calculate another parameters");
        System.out.println("Option \"2\" -- exit");
        System.out.print("Option: ");
        Scanner scanner = new Scanner(System.in);
        String promptOption = scanner.nextLine();

        while (!promptOption.equals("1") && !promptOption.equals("2")) {
            System.out.print("Seems like option is incorrect... \nOption: ");
            promptOption = scanner.nextLine();
        }

        return promptOption;
    }

    /**
     * Prompts user for the new parameters values.
     *
     * @param parametersParser The ParameterParser object that keeps old parameters values.
     * @return The ParametersParser object with a refreshed parameters values.
     */
    private static ParametersParser getPromptParameters(ParametersParser parametersParser) {
        Scanner scanner = new Scanner(System.in);
        String[] promptParameters = new String[parametersParser.getParameters().size()];
        int parameterIndex = 0;

        for (String name: parametersParser.getParameters().keySet()) {
            System.out.print("\"" + name + "\" = ");
            String value = scanner.nextLine();
            promptParameters[parameterIndex++] = name + "=" + value;
            parametersParser.setParameter(name, Double.parseDouble(value));
        }

        checkParameters(promptParameters);

        return parametersParser;
    }

    /**
     * Checks input parameters for correctness.
     *
     * @param promptParameters An input parameters.
     */
    private static void checkParameters(String[] promptParameters) {
        ParametersInspector parametersInspector = new ParametersInspector(promptParameters);

        try {
            parametersInspector.run();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Checks input equation for correctness.
     *
     * @param promptEquation An input equation.
     * @param parameters Parsed input parameters.
     */
    private static void checkEquation(String promptEquation, HashSet<String> parameters) {
        EquationInspector equationInspector = new EquationInspector(promptEquation, parameters);

        try {
            equationInspector.run();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
