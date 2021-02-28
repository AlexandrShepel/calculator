package alex.shepel.calculator.utility;

/**
 * Stores math operation (notation).
 */
public class MathNotation {

    private final int priority;
    private final int index;
    private final String mathSymbol;
    private final String leftMember;
    private final String rightMember;

    /**
     * The class constructor.
     * Gets information about math operation.
     * Stores it for later use.
     *
     * @param priority The execution priority.
     * @param index The index of the math operator in the equation string.
     * @param operator The math operator.
     * @param leftMember The member that stands on operator's left.
     * @param rightMember The member that stands on operator's right.
     */
    public MathNotation(int priority, int index, String operator, String leftMember, String rightMember) {
        this.priority = priority;
        this.index = index;
        this.mathSymbol = operator;
        this.leftMember = leftMember;
        this.rightMember = rightMember;
    }

    public int getPriority() {
        return priority;
    }

    public int getIndex() {
        return index;
    }

    public String getOperator() {
        return mathSymbol;
    }

    public String getLeftMember() {
        return leftMember;
    }

    public String getRightMember() {
        return rightMember;
    }

    public String getMask() {
        return "@" + priority;
    }

    /**
     * Represents stored notation as a usual input expression.
     */
    public String toString() {
        return leftMember + mathSymbol + rightMember;
    }

}
