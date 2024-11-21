package org.example;

import java.io.*;
import java.util.*;

public class WiseSayingRepository {
    private final String storagePath;
    private final HashMap<Integer, String[]> tempSayingList = new HashMap<>();

    public WiseSayingRepository(String storagePath) {
        this.storagePath = storagePath;
    }

    // 임시 명언 추가(완료)
    public void addSaying(int id, String author, String saying){
        tempSayingList.put(id, new String[]{author, saying});
    }

    // 임시 명언 불러오기(완료)
    public Map<Integer, String[]> getTemporarySayings(){
        return tempSayingList;
    }

    // 저장 명언 불러오기(완료)
    public List<String> getStoredSayings(){
        List<String> sayings = new ArrayList<>();
        File folder = new File(storagePath);
        if (!folder.exists()) return sayings;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return sayings;

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String json = reader.lines().reduce("", String::concat);
                String id = extractValue(json, "id");
                String author = extractValue(json, "author").replace("\"", "");
                String content = extractValue(json, "content").replace("\"", "");
                sayings.add(String.format("%s / %s / %s", id, author, content));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sayings;
    }

    // 임시 명언 수정(완료)
    public boolean updateTemporarySaying(int id, String newAuthor, String newSaying){
        if(tempSayingList.containsKey(id)){
            tempSayingList.put(id, new String[]{newAuthor, newSaying});
            return true;
        }
        return false;
    }

    // 저장 명언 수정(완료)
    public boolean updateSavedSaying(int id, String newAuthor, String newSaying){
        File file = new File(storagePath + "/" + id + ".json");

        if(!file.exists()){
            return false;
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            String updatedJson = String.format("{\"id\": %d, \"author\": \"%s\", \"content\": \"%s\"}",
                    id, newAuthor, newSaying);
            writer.write(updatedJson);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // 임시 명언 삭제(완료)
    public boolean deleteTemporarySaying(int id){
        return tempSayingList.remove(id) != null;
    }

    // 저장 명언 삭제(완료)
    public boolean deleteSavedSaying(int id){
        File file = new File(storagePath + "/" + id + ".json");
        return file.exists() && file.delete();
    }

    // 임시 명언 저장(완료)
    public void saveTemporarySaying(){
        for (Map.Entry<Integer, String[]> entry : tempSayingList.entrySet()) {
            int id = entry.getKey();
            String[] data = entry.getValue();

            File file = new File(storagePath + "/" + id + ".json");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(String.format("{\"id\": %d, \"author\": \"%s\", \"content\": \"%s\"}",
                        id, data[0], data[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tempSayingList.clear();
    }

    // 최신 ID 불러오기(완료)
    public int getNextId(){
        File folder = new File(storagePath);
        if(!folder.exists() || !folder.isDirectory()) return 1;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        Set<Integer> allIds = new HashSet<>();

        if(files != null){
            for (File file : files) {
                try{
                    String fileName = file.getName().replace(".json", "");
                    int id = Integer.parseInt(fileName);
                    allIds.add(id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        allIds.addAll(tempSayingList.keySet());

        for(int i=1; i<=allIds.size(); i++){
            if(!allIds.contains(i)) return i;
        }

        return allIds.stream().max(Integer::compareTo).orElse(0)+1;
    }

    // json 값 추출(완료)
    private String extractValue(String json, String key){
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return "";
        start += pattern.length();

        if (json.charAt(start) == '"') {
            int end = json.indexOf("\"", start + 1);
            return json.substring(start + 1, end);
        } else {
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return json.substring(start, end).trim();
        }
    }
}