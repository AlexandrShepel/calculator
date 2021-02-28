package alex.shepel.calculator.equationProcessing;

import alex.shepel.calculator.utility.MathLibrary;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Checks input equation for incorrectness.
 */
public class EquationInspector {

    private final String equation;
    private final HashSet<String> parameters;
    private final MathLibrary mathLibrary = new MathLibrary();
    private final HashMap<String, Boolean> isParameterUsed = new HashMap<>();

    /*
     * Contains one boolean value per each char of the input equation.
     * It is initialized in the constructor with all "false" values.
     * When some char of the equation is checked,
     * we change the boolean value of the same index to "true".
     * In the end of the EquationInspector work
     * we must see entire array equals to "true".
     * So, this is a self-diagnostic data array.
     */
    private final boolean[] isChecked;

    /**
     * The class constructor.
     *
     * @param equation An input equation.
     * @param parameters A HashSet of the parameters names.
     *                   Used to check that all of them
     *                   are represented in the equation.
     */
    public EquationInspector(String equation, HashSet<String> parameters) {
        this.equation = removeSpacings(equation);
        this.parameters = parameters;

        this.isChecked = new boolean[this.equation.length()];
        Arrays.fill(this.isChecked, false);

        for (String name: parameters)
            isParameterUsed.put(name, false);
    }

    /**
     * Runs equation checking.
     *
     * @throws IOException An incorrect equation exception.
     */
    public void run() throws IOException {
        checkExpectedMathLogic(equation);

        checkUnexpectedSymbols(equation);
        checkUnexpectedParameters(equation);
        checkUnexpectedZeroSymbol(equation);
        checkUnexpectedLastSymbol(equation);
        checkUnexpectedPoints(equation);
        checkUnexpectedNumbers(equation);
        checkUnexpectedMathSymbols(equation);

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

            if (!(isLetter(ch) || isDigit(ch) || mathLibrary.isMathOperator(String.valueOf(ch)) || ch == '.'))
                throw new IOException("The equation contains unexpected symbol \"" + ch + "\"");
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
    private void checkUnexpectedParameters(String equation) throws IOException {
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

                if (!parameters.contains("" + name))
                    throw new IOException("The parameter \"" + name + "\" is not specified.");

                isParameterUsed.replace("" + name, true);
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

        if (!(isDigit(firstChar) || isLetter(firstChar) || firstChar == '-'))
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

        if (!(isDigit(lastChar) || isLetter(lastChar)))
            throw new IOException("The equation contains unexpected symbol \"" + lastChar + "\" on the last position");
    }

    /**
     * Checks if equation contains unexpected points.
     *
     * @param equation An input equation.
     * @throws IOException An incorrect equation exception.
     */
    private void checkUnexpectedPoints(String equation) throws IOException {
        if (equation.charAt(equation.length() - 1) == '.')
            throw new IOException("The equation contains unexpected point.");

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
    private void checkUnexpectedNumbers(String equation) {
        for (int index = 0; index < equation.length(); index++) {
            if (isChecked[index]) continue;

            boolean isLastChar = (index >= equation.length() - 1);
            char ongoingChar = equation.charAt(index);

            if (isDigit(ongoingChar)) {
                if (isLastChar) {
                    isChecked[index] = true;
                    return;
                }

                char nextChar = equation.charAt(index + 1);
                int startIndex = index;

                while ((index + 1) < equation.length() && (isDigit(nextChar) || nextChar == '.'))
                    index++;

                Arrays.fill(isChecked, startIndex, index + 1, true);
            }
        }
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
