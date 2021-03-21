package ashepel.utility;

/**
 * Stores math operation (notation).
 */
public class MathNotation {

    private final int priority;
    private final int index;
    private final String operatorOrFunction;
    private final String leftArgument;
    private final String rightArgument;

    /**
     * The class constructor.
     * Gets information about math operation.
     * Stores it for later use.
     *
     * @param priority The execution priority.
     * @param index The index of the math operator in the equation string.
     * @param operator The math operator.
     * @param leftArgument The member that stands on operator's left.
     * @param rightArgument The member that stands on operator's right.
     */
    public MathNotation(int priority, int index, String operator, String leftArgument, String rightArgument) {
        this.priority = priority;
        this.index = index;
        this.operatorOrFunction = operator;
        this.leftArgument = leftArgument;
        this.rightArgument = rightArgument;
    }

    public MathNotation(int priority, int index, String function, String argument) {
        this.priority = priority;
        this.index = index;
        this.operatorOrFunction = function;
        this.leftArgument = argument;
        this.rightArgument = "";
    }

    public int getPriority() {
        return priority;
    }

    public int getIndex() {
        return index;
    }

    public String getOperatorOrFunction() {
        return operatorOrFunction;
    }

    public String getLeftArgument() {
        return leftArgument;
    }

    public String getRightArgument() {
        return rightArgument;
    }

    public String getMask() {
        return "@" + priority;
    }

    /**
     * Represents stored notation as a usual input expression.
     */
    public String toString() {
        if (rightArgument.equals(""))
            return operatorOrFunction + "(" + leftArgument + ")";

        else return leftArgument + operatorOrFunction + rightArgument;
    }

}
