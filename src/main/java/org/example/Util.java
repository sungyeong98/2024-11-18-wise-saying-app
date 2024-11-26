package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public void findType(String query){
        if(query.startsWith("목록")){

        }
    }

    public String[] listType(String query){
        Pattern pattern = Pattern.compile("\\?(\\w+)=([^&]+)");
        Matcher matcher = pattern.matcher(query);

        String key = "", value = "";

        while(matcher.find()){
            key = matcher.group(1);
            value = matcher.group(2);
        }

        return new String[]{key, value};
    }


}
