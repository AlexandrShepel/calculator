package ashepel.equationProcessing;

import ashepel.utility.MathLibrary;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Checks input equation for incorrectness.
 */
public class EquationInspector {

    private final MathLibrary mathLibrary = new MathLibrary();

    /*
     * Contains one boolean value per each char of the input equation.
     * It is initialized in the constructor with all "false" values.
     * When some char of the equation is checked,
     * we change the boolean value of the same index to "true".
     * In the end of the EquationInspector work
     * we must see entire array equals to "true".
     * So, this is a self-diagnostic data array.
     */
    private boolean[] isChecked;

    /**
     * Runs equation checking.
     *
     * @param equation An input equation.
     * @param parameters A HashSet of the parameters names.
     *                   Used to check that all of them
     *                   are represented in the equation.
     * @throws IOException An incorrect equation exception.
     */
    public void inspect(String equation, Set<String> parameters) throws IOException {
        HashSet<String> parametersHashSet = new HashSet<>(parameters);
        equation = removeSpacings(equation);
        isChecked = new boolean[equation.length()];
        Arrays.fill(isChecked, false);

        checkExpectedMathLogic(equation);
        checkUnexpectedSymbols(equation);
        checkUnexpectedPoints(equation);
        checkUnexpectedNumbers(equation);
        checkUnexpectedParameters(equation, parametersHashSet);
        checkUnexpectedZeroSymbol(equation);
        checkUnexpectedLastSymbol(equation);
        checkUnexpectedMathSymbols(equation);
        checkFunctions(equation);
        checkBrackets(equation);

        if (!isBooleanArrayTrue(isChecked))
            throw new IOException("Equation is incorrect. But we don't know why.");
    }

    /**
     * Checks expected math logic:
     *  -- at least 3 symbols (left member, right member, operator);
     *  -- at least one operator from the math library (see utility.MathLibrary class).
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkExpectedMathLogic(String equation) throws IOException {
        if (equation.length() < 3)
            throw new IOException("The equation doesn't contain any math logic.");

        for (String symbol: mathLibrary.getOperatorsArray())
            if (equation.substring(1).contains(symbol)) return;

        for (String function: mathLibrary.getFunctionsArray())
            if (equation.contains(function)) return;

        throw new IOException("The equation doesn't contain any math logic.");
    }

    /**
     * Checks unexpected symbols in the equation.
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkUnexpectedSymbols(String equation) throws IOException {
        for (int index = 0; index < equation.length(); index++) {
            if (isChecked[index]) continue;

            char ch = equation.charAt(index);

            if (!(isLetter(ch) || isDigit(ch) || ch == '.' || ch == '(' || ch == ')'
                    || mathLibrary.isMathOperator("" + ch)))
                throw new IOException("The equation contains unexpected symbol \"" + ch + "\"");
        }
    }

    /**
     * Checks if equation contains unexpected points.
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkUnexpectedPoints(String equation) throws IOException {
        for (int index = 1; index < equation.length() - 1; index++) {
            if (isChecked[index]) continue;

            if (equation.charAt(index) != '.') continue;

            char previousChar = equation.charAt(index - 1);
            char nextChar = equation.charAt(index + 1);

            if (!isDigit(previousChar) || !isDigit(nextChar))
                throw new IOException("The equation contains unexpected point.");
        }
    }

    /**
     * Checks if equation contains unexpected numbers.
     *
     * @param equation An input equation.
     */
    private void checkUnexpectedNumbers(String equation) throws IOException {
        for (int index = 0; index < equation.length(); index++) {
            if (isChecked[index]) continue;

            boolean isLastChar = (index >= equation.length() - 1);
            boolean isParameterOrFunction = index > 0 && isLetter(equation.charAt(index - 1));
            char ongoingChar = equation.charAt(index);

            if (isDigit(ongoingChar)) {
                if (isLastChar) {
                    isChecked[index] = true;
                    return;
                }

                int startIndex = index;

                while ((index + 1) < equation.length() &&
                        (isDigit(equation.charAt(index + 1)) || equation.charAt(index + 1) == '.'))
                    index++;

                if (equation.substring(startIndex, index + 1).contains(".") && isParameterOrFunction)
                    throw new IOException("Missed operator on the position " + startIndex + ".");

                Arrays.fill(isChecked, startIndex, index + 1, true);
            }
        }
    }

    /**
     * Checks unexpected parameters in the equation.
     * They will not be represented in the HashSet
     * of the previously parsed parameters.
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkUnexpectedParameters(String equation, HashSet<String> parameters) throws IOException {
        HashMap<String, Boolean> isParameterUsed = new HashMap<>();

        for (String name: parameters)
            isParameterUsed.put(name, false);

        for (int index = 0; index < equation.length(); index++) {
            if (isChecked[index]) continue;

            char previousChar = (index > 0) ? equation.charAt(index - 1) : '+';
            char ongoingChar = equation.charAt(index);
            char nextChar = (index < equation.length() - 1) ? equation.charAt(index + 1) : '+';

            if (isLetter(ongoingChar)) {
                if (isDigit(previousChar))
                    throw new IOException("Parameter's name can't start with digit.");

                StringBuilder name = new StringBuilder("" + ongoingChar);
                int startIndex = index;

                if (isLetter(nextChar) || isDigit(nextChar))
                    index++;

                while (isLetter(nextChar) || isDigit(nextChar)) {
                    name.append(nextChar);
                    nextChar = (index + 1 < equation.length()) ? equation.charAt(++index) : '+';
                }

                if (!parameters.contains("" + name)) {
                    if (!mathLibrary.isFunction("" + name))
                        throw new IOException("The parameter \"" + name + "\" is not specified.");
                }

                else isParameterUsed.replace("" + name, true);

                Arrays.fill(isChecked, startIndex, index + 1, true);
            }
        }

        if (isParameterUsed.containsValue(false))
            throw new IOException("You specified unused parameter(-s)");
    }

    /**
     * Checks if first symbol of the equation is correct.
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkUnexpectedZeroSymbol(String equation) throws IOException {
        char firstChar = equation.charAt(0);

        if (!(isDigit(firstChar) || isLetter(firstChar) || firstChar == '-' || firstChar == '('))
            throw new IOException("The equation contains unexpected symbol \"" + firstChar + "\" on the 0 position");

        if (firstChar == '-' && !mathLibrary.isMathOperator("" + equation.charAt(1)))
            isChecked[0] = true;
    }

    /**
     * Checks if last symbol of the equation is correct.
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkUnexpectedLastSymbol(String equation) throws IOException {
        char lastChar = equation.charAt(equation.length() - 1);

        if (!(isDigit(lastChar) || isLetter(lastChar) || lastChar == ')'))
            throw new IOException("The equation contains unexpected symbol \"" + lastChar + "\" on the last position");
    }

    /**
     * Checks if equation contains unexpected math symbols.
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkUnexpectedMathSymbols(String equation) throws IOException {
        for (int index = 0; index < equation.length() - 1; index++) {
            if (isChecked[index]) continue;

            char ongoingChar = equation.charAt(index);
            char nextChar = equation.charAt(index + 1);
            String charSequence = "" + ongoingChar + nextChar;

            if (mathLibrary.isMathOperator("" + ongoingChar)) {
                if (mathLibrary.isMathOperator("" + nextChar)) {
                    char thirdChar = equation.charAt(index + 2);

                    if (mathLibrary.isMathOperator("" + nextChar) && mathLibrary.isMathOperator("" + thirdChar)) {
                        charSequence = "" + ongoingChar + nextChar + thirdChar;
                        throw new IOException("Unexpected symbols sequence \"" + charSequence + "\"");
                    }

                    if (!mathLibrary.isAllowedSequence(charSequence))
                        throw new IOException("Unexpected symbols sequence \"" + charSequence + "\"");

                    Arrays.fill(isChecked, index, index + 2, true);
                }

                else
                    isChecked[index] = true;
            }
        }
    }

    private void checkBrackets(String equation) throws IOException {
        int redundantCount = 0;
        int lastOpenIndex = 0;

        for (int index = 0; index < equation.length(); index++) {
            if (equation.charAt(index) == '(') {
                redundantCount++;
                lastOpenIndex = index;
                isChecked[index] = true;
            }

            else if (equation.charAt(index) == ')' && index != lastOpenIndex + 1) {
                redundantCount--;
                isChecked[index] = true;
            }
        }

        if (redundantCount != 0)
            throw new IOException("The equation contains incorrect brackets.");
    }

    private void checkFunctions(String equation) throws IOException {
        for (int index = 1; index < equation.length(); index++) {
            if (equation.charAt(index) == '(') {
                int oppositeIndex = index - 1;

                if (!isDigit(equation.charAt(oppositeIndex)) && !isLetter(equation.charAt(oppositeIndex)))
                    continue;

                while (isDigit(equation.charAt(oppositeIndex)) || isLetter(equation.charAt(oppositeIndex)))
                    if (--oppositeIndex == -1)
                        break;

                if (!mathLibrary.isFunction(equation.substring(++oppositeIndex, index)))
                    throw new IOException(
                            "Unknown function \"" + equation.substring(oppositeIndex, index) + "\".");

                else
                    Arrays.fill(isChecked, oppositeIndex, index + 1, true);
            }
        }
    }

    /**
     * Checks if boolean array contains only "true" values.
     *
     * @param isChecked A self-diagnostic array
     *                  (see description in the properties field of the class).
     * @return A boolean "true" if array contains only "true" values.
     */
    private boolean isBooleanArrayTrue(boolean[] isChecked) {
        for (boolean isTrue : isChecked)
            if (!isTrue) return false;

        return true;
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

}
