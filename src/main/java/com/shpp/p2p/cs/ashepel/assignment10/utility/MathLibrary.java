package com.shpp.p2p.cs.ashepel.assignment10.utility;

import java.util.HashMap;

/**
 * Keeps math operators library.
 */
public class MathLibrary {

    private static final HashMap<String, Integer> operatorsPriority = new HashMap<>();
    private static final String[] allowedSequences = {"*-", "/-", "^-"};

    /**
     * The class constructor.
     */
    public MathLibrary() {
        operatorsPriority.put("+", 0);
        operatorsPriority.put("-", 0);
        operatorsPriority.put("*", 1);
        operatorsPriority.put("/", 1);
        operatorsPriority.put("^", 2);
    }

    /**
     * Returns the most priority operator of the input array.
     * That operator will executes first.
     *
     * @param operators The array of the operators.
     * @return The most priority operator of the input array.
     */
    public String getPriorityOperator(String[] operators) {
        String mostPriorityOperator = operators[0];

        for (String operator : operators)
            if (operatorsPriority.get(operator) > operatorsPriority.get(mostPriorityOperator))
                mostPriorityOperator = operator;

        return mostPriorityOperator;
    }

    /**
     * Checks if input symbol is math operator.
     *
     * @param symbol Symbol to check.
     * @return A boolean "true" if symbol is math operator.
     */
    public boolean isMathOperator(String symbol) {
        return operatorsPriority.containsKey(symbol);
    }

    /**
     * Checks if input operators sequence is allowed.
     * For example, sequences "/*", "^/" are banned.
     * List of allowed sequences is represent
     * as class property (see above).
     *
     * @param sequence The input sequence of the operators
     *                 to be checked.
     * @return A boolean "true" if sequence is allowed.
     */
    public boolean isAllowedSequence(String sequence) {
        for (String allowedSequence: allowedSequences)
            if (sequence.equals(allowedSequence))
                return true;

        return false;
    }

    /**
     * @return Array of allowed math operators sequences.
     */
    public String[] getAllowedSequences() {
        return allowedSequences;
    }

    /**
     * @return Array of math operators
     *         that can be used in the app.
     */
    public String[] getOperatorsArray() {
        String[] operators = new String[operatorsPriority.size()];
        int index = 0;

        for (String operator : operatorsPriority.keySet())
            operators[index++] = operator;

        return operators;
    }

}
