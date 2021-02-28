package com.shpp.p2p.cs.ashepel.assignment10.parametersProcessing;

import java.io.IOException;

/**
 * Checks input parameters for incorrectness.
 */
public class ParametersInspector {

    private final String[] promptParameters;

    /**
     * The class constructor.
     *
     * @param promptParameters The input parameters
     *                         that must be checked.
     */
    public ParametersInspector(String[] promptParameters) {
        this.promptParameters = (promptParameters == null) ? null : removeSpacings(promptParameters);
    }

    /**
     * Runs parameters checking.
     *
     * @throws IOException An incorrect parameter exception.
     */
    public void run() throws IOException {
        if (promptParameters != null) {
            for (String parameter : promptParameters) {
                checkExpectedSymbols(parameter);

                checkUnexpectedNameSymbols(parameter);
                checkUnexpectedValueSymbols(parameter);
            }
        }
    }

    /**
     * Checks if parameter contains all expected attributes:
     * letter (name), digit (value), equal sign.
     *
     * @param parameter A parameter to be tested.
     * @throws IOException An incorrect parameter exception.
     */
    private void checkExpectedSymbols(String parameter) throws IOException {
        if (parameter.length() < 3)
            throw new IOException("The argument \"" + parameter + "\" contains less than 3 symbols.");

        if (!parameter.contains("="))
            throw new IOException("The argument \"" + parameter + "\" doesn't contain equal sign \"=\".");

        if (!containsLetter(parameter))
            throw new IOException("The argument \"" + parameter + "\" doesn't contain any letter.");

        if (!containsDigit(parameter))
            throw new IOException("The argument \"" + parameter + "\" doesn't contain any digit.");
    }

    /**
     * Checks unexpected symbols in the parameter's name.
     *
     * @param parameter A parameter to be tested.
     * @throws IOException An incorrect parameter exception.
     */
    private void checkUnexpectedNameSymbols(String parameter) throws IOException {
        String name = parameter.substring(0, parameter.indexOf('='));

        if (isDigit(parameter.charAt(0)))
            throw new IOException("The argument \"" + parameter + "\" can't start with a digit.");

        for (char ch: name.toCharArray())
            if (!isLetter(ch) && !isDigit(ch))
                throw new IOException(
                        "Name of the parameter \"" + parameter + "\" contains unexpected symbol \"" + ch + "\".");
    }

    /**
     * Checks unexpected symbols in the parameter's value.
     *
     * @param parameter A parameter to be tested.
     * @throws IOException An incorrect parameter exception.
     */
    private void checkUnexpectedValueSymbols(String parameter) throws IOException {
        String value = parameter.substring(parameter.indexOf('=') + 1);

        boolean isNegative = value.charAt(0) == '-';
        boolean isStartDotIncorrect = (isNegative) ? value.charAt(1) == '.' : value.charAt(0) == '.';
        boolean isEndDotIncorrect = value.charAt(value.length() - 1) == '.';

        if (isStartDotIncorrect || isEndDotIncorrect)
            throw new IOException("Value of the parameter \"" + parameter + "\" contains unexpected \".\".");

        int startIndex = (isNegative) ? 1 : 0;

        for (int index = startIndex; index < value.length(); index++) {
            char ch = value.charAt(index);

            if (!isDigit(ch))
                throw new IOException(
                        "Value of the parameter \"" + parameter + "\" contains unexpected symbol \"" + ch + "\".");
        }
    }

    /**
     * Removes spacing from the input parameters string values.
     *
     * @param promptParameters An input String[] array of parameters.
     * @return A String[] array of the parameters without spacings.
     */
    private String[] removeSpacings(String[] promptParameters) {
        String[] editedParameters = new String[promptParameters.length];

        for (int index = 0; index < promptParameters.length; index++) {
            String editedParameter = "";

            for (String element : promptParameters[index].split(" "))
                editedParameter = editedParameter.concat(element);

            editedParameters[index] = editedParameter;
        }

        return editedParameters;
    }

    /**
     * Checks if input String value contains any letter.
     *
     * @param string An input string.
     * @return A boolean "true" if input contains any letter.
     */
    private boolean containsLetter(String string) {
        for (char ch: string.toCharArray()) {
            if (isLetter(ch))
                return true;
        }

        return false;
    }

    /**
     * Checks if input String value contains any digit.
     *
     * @param string An input string.
     * @return A boolean "true" if input contains any digit.
     */
    private boolean containsDigit(String string) {
        for (char ch: string.toCharArray()) {
            if (isDigit(ch))
                return true;
        }

        return false;
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
