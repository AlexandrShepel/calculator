package com.shpp.p2p.cs.ashepel.assignment10.equationProcessing;

import com.shpp.p2p.cs.ashepel.assignment10.utility.MathNotation;
import com.shpp.p2p.cs.ashepel.assignment10.utility.MathLibrary;

import java.util.HashMap;

/**
 * Parses input equation.
 * Saves it in the form of math notations
 * (see utility.MathNotation class
 * for notations description).
 */
public class EquationParser {

    private final MathLibrary mathLibrary = new MathLibrary();
    private final HashMap<Integer, MathNotation> mathNotations;
    private final String equation;

    /**
     * The class constructor.
     *
     * @param promptEquation An input equation.
     */
    public EquationParser(String promptEquation) {
        equation = removeSpacings(promptEquation);
        mathNotations = getCalculationNotations(equation);
    }

    /**
     * Removes spacing from the input equation.
     *
     * @param equation An input equation.
     * @return An input equation without spacings.
     */
    private String removeSpacings(String equation) {
        String withoutSpacingEquation = "";

        for (String element: equation.split(" ")) {
            withoutSpacingEquation = withoutSpacingEquation.concat(element);
        }

        return withoutSpacingEquation;
    }

    /**
     * Converts string format equation to the math notation.
     *
     * @param equation An input equation.
     * @return A HashMap with math notations.
     *         Operation priority is a key.
     *         Math notation is a value.
     */
    private HashMap<Integer, MathNotation> getCalculationNotations(String equation) {
        HashMap<Integer, String> operators = getEquationOperators(equation);
        HashMap<Integer, MathNotation> mathNotations = new HashMap<>();
        int operatorsCount = operators.size();

        for (int index = 0; index < operatorsCount; index++) {
            int priorityOperatorIndex = getPriorityOperatorIndex(operators);
            String priorityOperator = operators.get(priorityOperatorIndex);
            String leftMember = getLeftMember(priorityOperatorIndex, equation);
            String rightMember = getRightMember(priorityOperatorIndex, priorityOperator.length(), equation);
            MathNotation mathNotation = new MathNotation(
                    index,
                    priorityOperatorIndex - leftMember.length(),
                    priorityOperator,
                    leftMember,
                    rightMember
            );
            mathNotations.put(mathNotation.getPriority(), mathNotation);
            equation = replaceWithMask(equation, mathNotation);
            operators.remove(priorityOperatorIndex);
        }

        return mathNotations;
    }

    /**
     * Returns all operators of the specified equation.
     *
     * @param equation An input equation.
     * @return The HashMap of operators.
     *         Operator priority is a key.
     */
    private HashMap<Integer, String> getEquationOperators(String equation) {
        HashMap<Integer, String> mathOperators = new HashMap<>();

        if (equation.charAt(0) == '-')
            equation = "@" + equation.substring(1);

        for (int index = 0; index < mathLibrary.getAllowedSequences().length;) {
            String operatorsSequence = mathLibrary.getAllowedSequences()[index];
            int sequenceIndex = equation.indexOf(operatorsSequence);

            if (sequenceIndex == -1)
                index++;

            else {
                mathOperators.put(sequenceIndex, operatorsSequence.substring(0, 1));
                equation = replaceWithAt(equation, operatorsSequence);
            }
        }

        for (int index = 0; index < mathLibrary.getOperatorsArray().length;) {
            String operator = mathLibrary.getOperatorsArray()[index];
            int operatorIndex = equation.indexOf(operator);

            if (operatorIndex == -1)
                index++;

            else {
                mathOperators.put(operatorIndex, operator);
                equation = replaceWithAt(equation, operator);
            }
        }

        return mathOperators;
    }

    /**
     * Returns a member that stands on specified operator's left.
     *
     * @param priorityOperatorIndex An index of the operator.
     * @param equation An input equation.
     * @return A member that stands on specified operator's left.
     */
    private String getLeftMember(int priorityOperatorIndex, String equation) {
        int index = priorityOperatorIndex - 1;
        char leftChar = equation.charAt(index);

        while (true) {
            if (index <= 0)
                break;

            if (!(isDigit(leftChar) || isLetter(leftChar) || leftChar == '@' || leftChar == '.')) {
                if (leftChar == '-' && mathLibrary.isMathOperator("" + equation.charAt(index - 1)))
                    break;

                index++;
                break;
            }

            leftChar = equation.charAt(--index);
        }

        return equation.substring(index, priorityOperatorIndex);
    }

    /**
     * Returns a member that stands on specified operator's right.
     *
     * @param priorityOperatorIndex An index of the operator.
     * @param equation An input equation.
     * @return A member that stands on specified operator's right.
     */
    private String getRightMember(int priorityOperatorIndex, int operatorLength, String equation) {
        int startIndex = priorityOperatorIndex + operatorLength;
        int index = (equation.charAt(startIndex) == '-') ? startIndex + 1 : startIndex;
        char nextChar = equation.charAt(index);

        while (true) {
            if (index + 1 >= equation.length())
                break;

            if (!(isDigit(nextChar) || isLetter(nextChar) || nextChar == '@' || nextChar == '.')) {
                index--;
                break;
            }

            nextChar = equation.charAt(++index);
        }

        return equation.substring(startIndex, index + 1);
    }

    /**
     * Returns index of the most priority operator
     * in the specified map.
     *
     * @param operators A HashMap with operators.
     * @return An index of the most priority operator.
     */
    private int getPriorityOperatorIndex(HashMap<Integer, String> operators) {
        int highestPriorityIndex = 0;
        String[] values = new String[operators.size()];
        int arrayIndex = 0;

        for (int index = 0; index < equation.length(); index++)
            if (operators.containsKey(index))
                values[arrayIndex++] = operators.get(index);

        String priorityOperator = mathLibrary.getPriorityOperator(values);

        if (priorityOperator.equals("^")) {
            for (int index = 0; index < equation.length(); index++)
                if (operators.containsKey(index) && operators.get(index).equals(priorityOperator))
                    highestPriorityIndex = index;

            return highestPriorityIndex;
        }

        else {
            for (int index = 0; index < equation.length(); index++)
                if (operators.containsKey(index) && operators.get(index).equals(priorityOperator))
                    return index;
        }

        return highestPriorityIndex;
    }

    /**
     * Looks for location of a notation in the specified equation.
     * Replaces a part of the specified equation
     * with its notation mask.
     *
     * @param equation An input equation.
     * @param mathNotation A math notation that must be
     *                     found in the equation.
     * @return An equation which part is replaced with notation mask.
     */
    private String replaceWithMask(String equation, MathNotation mathNotation) {
        int countToAdd = mathNotation.toString().length() - mathNotation.getMask().length();
        StringBuilder extendedMask = new StringBuilder(mathNotation.getMask());

        for (int index = 0; index < countToAdd; index++)
            extendedMask.insert(0, "@");

        String leftPart = equation.substring(0, mathNotation.getIndex());
        String rightPart = equation.substring(mathNotation.getIndex() + mathNotation.toString().length());

        return leftPart.concat("" + extendedMask).concat(rightPart);
    }

    /**
     * Extends notation mask to the length of replaced part.
     * It allows to avoid operators indexes shifting
     * when part of equation is replaced.
     *
     * @param string An input equation.
     * @param target A target part of equation
     *               that must be replaced by the mask.
     * @return An equation which part is replaced with notation mask.
     */
    private String replaceWithAt(String string, String target) {
        String leftPart = string.substring(0, string.indexOf(target));
        String rightPart = string.substring(string.indexOf(target) + target.length());
        StringBuilder targetPart = new StringBuilder();

        for (int index = 0; index < target.length(); index++)
            targetPart.append("@");

        return leftPart + targetPart + rightPart;
    }

    /**
     * Checks if input character is digit.
     *
     * @param ch An input character.
     * @return A boolean "true" if input is a digit.
     */
    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * Checks if input character is letter.
     *
     * @param ch An input character.
     * @return A boolean "true" if input is a letter.
     */
    private boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    /**
     * @return A HashMap of the equation's notations.
     *         Execution priority of the operator is a key.
     */
    public HashMap<Integer, MathNotation> getNotations() {
        return mathNotations;
    }

}
