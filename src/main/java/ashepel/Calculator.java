package ashepel;

import ashepel.utility.MathNotation;

import java.util.HashMap;

/**
 * Substitutes input parameters and calculates input notations.
 */
public class Calculator {

    private Double result;

    /**
     * The main logic of the notations execution.
     *
     * @param notations The notations of math operations
     *                  that must be executed.
     * @param parameters The parameters that must be substituted
     *                   to the notations.
     */
    public void calculate(HashMap<Integer, MathNotation> notations, HashMap<String, Double> parameters) {
        final HashMap<String, Double> tempResults = new HashMap<>();

        for (int index = 0; index < notations.size(); index++) {
            double argument1 = getValue(notations.get(index).getLeftArgument(), parameters, tempResults);
            double argument2 = getValue(notations.get(index).getRightArgument(), parameters, tempResults);
            double tempResult = doOperation(argument1, notations.get(index).getOperatorOrFunction(), argument2);
            tempResults.put(notations.get(index).getMask(), tempResult);
        }

        result = tempResults.get(notations.get(notations.size() - 1).getMask());
    }

    /**
     * @return The member that stands on operator's right.
     */
    private double getValue(String name, HashMap<String, Double> parameters, HashMap<String, Double> tempResults) {
        if (isParameter(name))
            return (name.contains("-")) ?
                    (- 1) * parameters.get(name.replace("-", "")) : parameters.get(name);

        else if (name.contains("@"))
            return tempResults.get(name);

        else if (name.equals(""))
            return 0.0;

        else return Double.parseDouble(name);
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
     * @param argument1  The member that stands on math operatorOrFunction's left.
     * @param operatorOrFunction The math operatorOrFunction.
     * @param argument2 The member that stands on operatorOrFunction's right.
     * @return The result of the notation's calculation.
     */
    private double doOperation(double argument1, String operatorOrFunction, double argument2) {
        return switch (operatorOrFunction) {
            case "+" -> argument1 + argument2;
            case "-" -> argument1 - argument2;
            case "*" -> argument1 * argument2;
            case "/" -> argument1 / argument2;
            case "^" -> Math.pow(argument1, argument2);
            case "sin" -> Math.sin(argument1);
            case "cos" -> Math.cos(argument1);
            case "tan" -> Math.tan(argument1);
            case "atan" -> Math.atan(argument1);
            case "log10" -> Math.log10(argument1);
            case "log2" -> Math.log10(argument1) / Math.log10(2);
            case "sqrt" -> Math.sqrt(argument1);
            default -> 0;
        };
    }

    /**
     * @return The result of the input notations execution.
     */
    public Double getResult() {
        return result;
    }

}
