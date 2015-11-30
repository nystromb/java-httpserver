package scarvill.httpserver.request;

import java.util.HashMap;

public class QueryString {
    public HashMap<String, String> parse(String query) {
        HashMap<String, String> parameters = separateParameters(query);

        return parameters;
    }

    private HashMap<String, String> separateParameters(String query) {
        HashMap<String, String> parameters = new HashMap<>();

        for (String argument : query.split("&")) {
            String parameterName = translateSpecialCharacters(argument.split("=")[0]);
            String parameterValue = translateSpecialCharacters(argument.split("=")[1]);
            parameters.put(parameterName, parameterValue);
        }

        return parameters;
    }

    private String translateSpecialCharacters(String query) {
        String translatedString = "";

        for (int i = 0; i < query.length(); i++) {
            String currentCharacterSlice = query.substring(i, i + 1);

            if (i + 2 < query.length() && currentCharacterSlice.startsWith("%")) {
                translatedString += translateCharacterCode(query.substring(i + 1, i + 3));
                i += 2;
            } else {
                translatedString += currentCharacterSlice;
            }
        }

        return translatedString;
    }

    private String translateCharacterCode(String code) {
        switch (code) {
            case "20": return " ";
            case "22": return "\"";
            case "23": return "#";
            case "24": return "$";
            case "26": return "&";
            case "2B": return "+";
            case "2C": return ",";
            case "3A": return ":";
            case "3B": return ";";
            case "3C": return "<";
            case "3D": return "=";
            case "3E": return ">";
            case "3F": return "?";
            case "40": return "@";
            case "5B": return "[";
            case "5D": return "]";
            default: return "";
        }
    }
}
