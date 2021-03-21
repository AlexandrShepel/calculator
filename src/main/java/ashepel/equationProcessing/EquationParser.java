package ashepel.equationProcessing;

import ashepel.utility.MathLibrary;
import ashepel.utility.MathNotation;

import java.util.HashMap;

/**
 * Parses input equation.
 * Saves it in the form of math notations
 * (see utility.MathNotation class
 * for notations description).
 */
public class EquationParser {

    private final MathLibrary mathLibrary = new MathLibrary();
    private final HashMap<Integer, MathNotation> mathNotations = new HashMap<>();
    private String parsingEquation;

    public void parse(String promptEquation) {
        parsingEquation = removeSpacings(promptEquation);
        mathNotations.putAll(getNotations());
    }

    private HashMap<Integer, MathNotation> getNotations() {
        final HashMap<Integer, MathNotation> mathNotations = new HashMap<>();

        for (int index = 0; index < parsingEquation.length(); index++) {
            if (parsingEquation.charAt(index) == ')') {
                int oppositeIndex = index - 1;

                while (parsingEquation.charAt(oppositeIndex) != '(')
                    oppositeIndex--;

                int openIndex = oppositeIndex;
                String beforeBracketsEquation = parsingEquation.substring(0, openIndex);
                String insideBracketsEquation = parsingEquation.substring(openIndex + 1, index);
                String afterBracketsEquation = parsingEquation.substring(index + 1);
                String equationToCheck = insideBracketsEquation;

                if (insideBracketsEquation.charAt(0) == '-')
                    equationToCheck = insideBracketsEquation.substring(1);

                for (String operator : mathLibrary.getOperatorsArray()) {
                    if (equationToCheck.contains(operator)) {
                        mathNotations.putAll(getOperatorsNotations(insideBracketsEquation, mathNotations.size()));
                        insideBracketsEquation = replaceWithMaskWhole(
                                insideBracketsEquation, mathNotations.get(mathNotations.size() - 1));
                        parsingEquation =
                                beforeBracketsEquation + "(" + insideBracketsEquation + ")" + afterBracketsEquation;
                        break;
                    }
                }

                char checkingChar = (oppositeIndex > 0) ? parsingEquation.charAt(--oppositeIndex) : 0;
                boolean isFunction = oppositeIndex > 0 && (isDigit(checkingChar) || isLetter(checkingChar));

                if (isFunction)
                    mathNotations.put(mathNotations.size(),
                            getFunctionNotation(openIndex, index, mathNotations.size()));

                else if (insideBracketsEquation.contains("@"))
                    parsingEquation = beforeBracketsEquation + "@@" + insideBracketsEquation + afterBracketsEquation;
            }
        }

        mathNotations.putAll(getOperatorsNotations(parsingEquation, mathNotations.size()));

        return mathNotations;
    }

    private MathNotation getFunctionNotation(int openIndex, int closeIndex, int minPriority) {
        int oppositeIndex = openIndex - 1;

        while (isDigit(parsingEquation.charAt(oppositeIndex)) || isLetter(parsingEquation.charAt(oppositeIndex)))
            if (--oppositeIndex == -1)
                break;

        int functionBeginning = oppositeIndex + 1;
        String functionArgument = parsingEquation.substring(openIndex + 1, closeIndex);

        if (functionArgument.contains("@"))
            functionArgument = "@" + functionArgument.replace("@", "");

        MathNotation mathNotation = new MathNotation(
                mathNotations.size() + minPriority,
                functionBeginning,
                parsingEquation.substring(functionBeginning, openIndex),
                functionArgument
        );

        String beforeFunctionEquation = parsingEquation.substring(0, functionBeginning);
        String functionEquation = parsingEquation.substring(functionBeginning, closeIndex + 1);
        String afterFunctionEquation = parsingEquation.substring(closeIndex + 1);
        functionEquation = replaceWithMaskWhole(functionEquation, mathNotation);
        parsingEquation = beforeFunctionEquation + functionEquation + afterFunctionEquation;

        return mathNotation;
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
    private HashMap<Integer, MathNotation> getOperatorsNotations(String equation, int minPriority) {
        HashMap<Integer, String> operators = getEquationOperators(equation);
        HashMap<Integer, MathNotation> mathNotations = new HashMap<>();
        int operatorsCount = operators.size();

        for (int count = 0; count < operatorsCount; count++) {
            int priorityOperatorIndex = getPriorityOperatorIndex(operators, equation);
            String priorityOperator = operators.get(priorityOperatorIndex);
            String leftMember = getLeftMember(priorityOperatorIndex, equation);
            String rightMember = getRightMember(priorityOperatorIndex, priorityOperator.length(), equation);
            MathNotation mathNotation = new MathNotation(
                    count + minPriority,
                    priorityOperatorIndex - leftMember.length(),
                    priorityOperator,
                    (leftMember.contains("@")) ? "@" + leftMember.replace("@", "") : leftMember,
                    (rightMember.contains("@")) ? "@" + rightMember.replace("@", "") : rightMember
            );
            mathNotations.put(mathNotation.getPriority(), mathNotation);
            equation = replaceWithMaskByIndex(equation, leftMember, rightMember, mathNotation);
            operators.remove(priorityOperatorIndex);
        }

        return mathNotations;
    }

    private String replaceWithMaskWhole(String equation, MathNotation mathNotation) {
        int countToAdd = equation.length() - mathNotation.getMask().length();
        StringBuilder maskedEquation = new StringBuilder(mathNotation.getMask());

        for (int index = 0; index < countToAdd; index++)
            maskedEquation.insert(0, "@");

        return "" + maskedEquation;
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
    private int getPriorityOperatorIndex(HashMap<Integer, String> operators, String equation) {
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
     *
     * @param equation An input equation.
     * @param rightMember The right argument of the operation.
     * @param leftMember The left argument of the operation.
     * @param mathNotation A math notation that must be
     *                     found in the equation.
     * @return An equation which part is replaced with notation mask.
     */
    private String replaceWithMaskByIndex(
            String equation, String leftMember, String rightMember, MathNotation mathNotation) {

        int countToAdd = leftMember.length() + "+".length() + rightMember.length() - mathNotation.getMask().length();
        StringBuilder extendedMask = new StringBuilder(mathNotation.getMask());

        for (int index = 0; index < countToAdd; index++)
            extendedMask.insert(0, "@");

        String leftPart = equation.substring(0, mathNotation.getIndex());
        String rightPart = equation.substring(mathNotation.getIndex() + extendedMask.length());

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

        return leftPart + "@".repeat(target.length()) + rightPart;
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
    public HashMap<Integer, MathNotation> getNotationsHashMap() {
        return mathNotations;
    }

}
