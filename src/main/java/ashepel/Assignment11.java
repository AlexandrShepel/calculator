package ashepel;

import ashepel.equationProcessing.EquationInspector;
import ashepel.equationProcessing.EquationParser;
import ashepel.parametersProcessing.ParametersInspector;
import ashepel.parametersProcessing.ParametersParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a calculator. Receives an equation
 * and its parameters in the form of String values.
 * Checks them for correctness and returns calculations
 * result to the console.
 */
public class Assignment11 {

    private static Calculator calculator;
    private static ParametersInspector parametersInspector;
    private static EquationInspector equationInspector;
    private static ParametersParser parametersParser;
    private static EquationParser equationParser;

    public static void init() {
        calculator = new Calculator();
        parametersInspector = new ParametersInspector();
        equationInspector = new EquationInspector();
        parametersParser = new ParametersParser();
        equationParser = new EquationParser();
    }

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

        final String promptEquation = args[0];

        if (args.length > 1) {
            String[] promptParameters = Arrays.copyOfRange(args, 1, args.length);
            checkParameters(promptParameters);
            parametersParser.parse(promptParameters);
        }

        checkEquation(promptEquation, parametersParser.getParametersHashMap().keySet());
        equationParser.parse(promptEquation);

        calculator.calculate(equationParser.getNotationsHashMap(), parametersParser.getParametersHashMap());
        System.out.print("Result = " + calculator.getResult());

//        while (areNewParameters()) {
//            updateParameterParser();
//            calculator.calculate(equationParser.getNotationsHashMap(), parametersParser.getParametersHashMap());
//            System.out.println("Result = " + calculator.getResult());
//        }
    }

    /**
     * Asks user if he wants to enter another parameters.
     *
     * @return Boolean "true" when user wants to enter new parameters.
     */
    private static boolean areNewParameters() {
        System.out.println("Do you want to calculate equation with another parameters? Y/N");
        Scanner scanner = new Scanner(System.in);
        String promptOption = scanner.nextLine();

        while (!promptOption.equalsIgnoreCase("Y") && !promptOption.equalsIgnoreCase("N")) {
            System.out.print("Seems like option is incorrect... \nEnter \"Y\" or \"N\": ");
            promptOption = scanner.nextLine();
        }

        return promptOption.equalsIgnoreCase("Y");
    }

    /**
     * Prompts user for the new parameters values. Checks them.
     */
    private static void updateParameterParser() {
        Scanner scanner = new Scanner(System.in);
        String[] promptParameters = new String[parametersParser.getParametersHashMap().size()];
        int parameterIndex = 0;

        for (String name: parametersParser.getParametersHashMap().keySet()) {
            System.out.print("\"" + name + "\" = ");
            String value = scanner.nextLine();
            promptParameters[parameterIndex++] = name + "=" + value;
            parametersParser.setParameter(name, Double.parseDouble(value));
        }

        checkParameters(promptParameters);
    }

    /**
     * Checks input parameters for correctness.
     *
     * @param promptParameters An input parameters.
     */
    private static void checkParameters(String[] promptParameters) {
        try {
            parametersInspector.inspect(promptParameters);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Checks input equation for correctness.
     *
     * @param promptEquation An input equation.
     * @param parameters Parsed input parameters.
     */
    private static void checkEquation(String promptEquation, Set<String> parameters) {
        try {
            equationInspector.inspect(promptEquation, parameters);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(0);
        }
    }

}
