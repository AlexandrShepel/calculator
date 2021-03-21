package ashepel.parametersProcessing;

import java.util.HashMap;

/**
 * Parses input parameters names and values.
 */
public class ParametersParser {

    private HashMap<String, Double> parametersHashMap;

    /**
     * Converts input String[] array to the HashMap.
     * Parameters names are keys.
     * Parameters values becomes map's values.
     *
     * @param promptParameters An input String[] array of the parameters.
     */
    public void parse(String[] promptParameters) {
        promptParameters = removeSpacings(promptParameters);
        HashMap<String, Double> parametersHashMap = new HashMap<>();

        for (String parameter : promptParameters) {
            String name = parameter.substring(0, parameter.indexOf('='));
            String value = parameter.substring(parameter.indexOf('=') + 1);
            parametersHashMap.put(name, Double.valueOf(value));
        }

        this.parametersHashMap = parametersHashMap;
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
     * @return Parameters in the HashMap format.
     */
    public HashMap<String, Double> getParametersHashMap() {
        return (parametersHashMap == null) ? new HashMap<>() : parametersHashMap;
    }

    /**
     * Sets new value to the specified parameter.
     *
     * @param name A name of the parameter that will be updated.
     * @param value A new value of the parameter.
     */
    public void setParameter(String name, double value) {
        parametersHashMap.put(name, value);
    }

}
