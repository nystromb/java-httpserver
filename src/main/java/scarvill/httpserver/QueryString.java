package scarvill.httpserver;

import java.util.ArrayList;
import java.util.List;

public class QueryString {
    public List<String> parse(String query) {
        List<String> arguments = separateArguments(query);

        return arguments;
    }

    private List<String> separateArguments(String query) {
        List<String> arguments = new ArrayList<>();
        for (String argument : query.split("&")) {
            arguments.add(translateSpecialCharacters(argument));
        }

        return arguments;
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
