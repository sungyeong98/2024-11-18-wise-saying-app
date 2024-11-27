package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    public static String getKeywordType(String query){
        Pattern pattern = Pattern.compile("keywordType=(\\w+)&keyword=([^&]+)");
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getKeyword(String query){
        Pattern pattern = Pattern.compile("keywordType=(\\w+)&keyword=([^&]+)");
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    public static int getId(String query){
        try{
            return Integer.parseInt(query.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getPage(String query){
        try{
            return Integer.parseInt(query.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
