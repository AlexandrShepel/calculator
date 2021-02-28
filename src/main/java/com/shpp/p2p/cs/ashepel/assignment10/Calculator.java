package com.shpp.p2p.cs.ashepel.assignment10;

import com.shpp.p2p.cs.ashepel.assignment10.utility.MathNotation;

import java.util.HashMap;

/**
 * Substitutes input parameters and calculates input notations.
 */
public class Calculator {

    private final HashMap<Integer, MathNotation> notations;
    private final HashMap<String, Double> parameters;
    private final HashMap<Integer, Double> maskedValues = new HashMap<>();
    private final Double result;

    /**
     * The class constructor.
     *
     * @param notations The notations of math operations
     *                  that must be executed.
     * @param parameters The parameters that must be substituted
     *                   to the notations.
     */
    public Calculator(HashMap<Integer, MathNotation> notations, HashMap<String, Double> parameters) {
        this.notations = notations;
        this.parameters = parameters;

        result = calculate();
    }

    /**
     * The main logic of the notations execution.
     *
     * @return An answer of the notations calculation.
     */
    private Double calculate() {
        for (int index = 0; index < notations.size(); index++) {
            double leftValue = getLeftValue(index);
            double rightValue = getRightValue(index);

            maskedValues.put(index, doOperation(leftValue, notations.get(index).getOperator(), rightValue));
        }

        return maskedValues.get(notations.size() - 1);
    }

    /**
     * @return The member that stands on operator's left.
     */
    private double getLeftValue(int key) {
        if (isParameter(notations.get(key).getLeftMember())) {
            String name = notations.get(key).getLeftMember();
            return (name.contains("-")) ?
                    (- 1) * parameters.get(name.replace("-", "")) : parameters.get(name);
        }

        else if (notations.get(key).getLeftMember().contains("@")) {
            String mask = notations.get(key).getLeftMember().replace("@", "");
            return maskedValues.get(Integer.parseInt(mask));
        }

        else
            return Double.parseDouble(notations.get(key).getLeftMember());
    }

    /**
     * @return The member that stands on operator's right.
     */
    private double getRightValue(int key) {
        if (isParameter(notations.get(key).getRightMember())) {
            String name = notations.get(key).getRightMember();
            return (name.contains("-")) ?
                    (- 1) * parameters.get(name.replace("-", "")) : parameters.get(name);
        }

        else if (notations.get(key).getRightMember().contains("@")) {
            String mask = notations.get(key).getRightMember().replace("@", "");
            return maskedValues.get(Integer.parseInt(mask));
        }

        else
            return Double.parseDouble(notations.get(key).getRightMember());
    }

    /**
     * Checks if argument is parameter that must be substituted.
     * Else it can be digit or another notation's result.
     */
    private boolean isParameter(String argument) {
        for (char ch: argument.toCharArray())
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                return true;

        return false;
    }

    /**
     * Executes math operation (notation).
     *
     * @param leftValue  The member that stands on math operator's left.
     * @param operator The math operator.
     * @param rightValue The member that stands on operator's right.
     * @return The result of the notation's calculation.
     */
    private double doOperation(double leftValue, String operator, double rightValue) {
        double result = 0;

        switch (operator) {
            case "+":
                result = leftValue + rightValue;
                break;
            case "-":
                result = leftValue - rightValue;
                break;
            case "*":
                result = leftValue * rightValue;
                break;
            case "/":
                result = leftValue / rightValue;
                break;
            case "^":
                result = Math.pow(leftValue, rightValue);
                break;
        }

        return result;
    }

    /**
     * @return The result of the input notations execution.
     */
    public Double getResult() {
        return result;
    }

}
