package org.example;

import java.io.*;
import java.util.*;

public class WiseSayingRepository {
    private String storagePath;
    private final HashMap<Integer, WiseSaying> tempSayingList = new LinkedHashMap<>();

    public WiseSayingRepository() {
        storagePath = "db/wiseSaying";

        File folder = new File(storagePath);
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    public WiseSayingRepository(String path) {
        storagePath = path;

        File folder = new File(storagePath);
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    // 임시 명언 추가
    public void addSaying(int id, String author, String content){
        tempSayingList.put(id, new WiseSaying(id, author, content));
    }

    // 임시 명언 불러오기
    public HashMap<Integer, WiseSaying> getTemporarySayings(){
        return tempSayingList;
    }

    // 저장 명언 불러오기
    public List<String> getSavedSayings(){
        List<String> sayings = new ArrayList<>();
        File folder = new File(storagePath);
        if (!folder.exists()) return sayings;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return sayings;

        Arrays.sort(files, (f1,f2) -> {
            int id1 = Integer.parseInt(f1.getName().replace(".json", ""));
            int id2 = Integer.parseInt(f2.getName().replace(".json", ""));
            return Integer.compare(id2, id1);
        });

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

    // 임시 명언 불러오기(키워드 입력시)
    public HashMap<Integer, WiseSaying> getTemporarySaings(String keywordType, String keyword) {
        HashMap<Integer, WiseSaying> result = new HashMap<>();

        for(Map.Entry<Integer, WiseSaying> entry : tempSayingList.entrySet()){
            WiseSaying wiseSaying = entry.getValue();

            switch (keywordType){
                case "author":
                    if (wiseSaying.getAuthor().contains(keyword)) {
                        result.put(entry.getKey(), wiseSaying);
                    }
                    break;
                case "content":
                    if (wiseSaying.getContent().contains(keyword)) {
                        result.put(entry.getKey(), wiseSaying);
                    }
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    // 저장 명언 불러오기(키워드 입력시)
    public List<String> getSavedSayings(String keywordType, String keyword) {
        List<String> sayings = new ArrayList<>();
        File folder = new File(storagePath);
        if (!folder.exists()) return sayings;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return sayings;

        Arrays.sort(files, (f1, f2) -> {
            int id1 = Integer.parseInt(f1.getName().replace(".json", ""));
            int id2 = Integer.parseInt(f2.getName().replace(".json", ""));
            return Integer.compare(id2, id1);
        });

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String json = reader.lines().reduce("", String::concat);
                String id = extractValue(json, "id");
                String author = extractValue(json, "author").replace("\"", "");
                String content = extractValue(json, "content").replace("\"", "");

                // 키워드 필터링 적용
                boolean matches = false;
                switch (keywordType) {
                    case "author":
                        matches = author.contains(keyword);
                        break;
                    case "content":
                        matches = content.contains(keyword);
                        break;
                    default:
                        break;
                }

                if (matches) {
                    sayings.add(String.format("%s / %s / %s", id, author, content));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sayings;
    }

    // 임시 명언 수정
    public boolean updateTemporarySaying(int id, String newAuthor, String newSaying){
        WiseSaying saying = tempSayingList.get(id);
        if(saying != null){
            saying.setAuthor(newAuthor);
            saying.setContent(newSaying);
            return true;
        }
        return false;
    }

    // 저장 명언 수정
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

    // 임시 명언 삭제
    public boolean deleteTemporarySaying(int id){
        return tempSayingList.remove(id) != null;
    }

    // 저장 명언 삭제
    public boolean deleteSavedSaying(int id){
        File file = new File(storagePath + "/" + id + ".json");
        return file.exists() && file.delete();
    }

    // 임시 명언 저장
    public void saveTemporarySaying(){
        tempSayingList.forEach((id, wiseSaying) -> {

            File file = new File(storagePath + "/" + id + ".json");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(String.format("{\"id\": %d, \"author\": \"%s\", \"content\": \"%s\"}",
                        wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tempSayingList.clear();
    }

    // 최신 ID 불러오기
    public int getNextId(){
        /*
        Set<Integer> allIds = new HashSet<>(tempSayingList.keySet());

        File folder = new File(storagePath);

        if (folder.exists() && folder.isDirectory()){
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null){
                Arrays.stream(files)
                        .map(file -> file.getName().replace(".json",""))
                        .filter(name -> name.matches("\\d+"))
                        .mapToInt(Integer::parseInt)
                        .forEach(allIds::add);
            }
        }

        return allIds.stream().max(Integer::compareTo).orElse(0) + 1;

         */

        File idFile = new File(storagePath + "/lastId.txt");

        File folder = new File(storagePath);
        if(!folder.exists()) folder.mkdirs();

        if(!idFile.exists()) {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(idFile))){
                writer.write("1");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 1;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(idFile))){
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            return 1;
        }
    }

    // 최신 ID 갱신
    public void updateIdFile(int nextid){
        File idFile = new File(storagePath + "/lastId.txt");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(idFile))){
            writer.write(String.valueOf(nextid));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // json 값 추출
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