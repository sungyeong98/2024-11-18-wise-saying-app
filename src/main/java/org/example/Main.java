package org.example;

/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
 */

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}

class App {
    static Scanner scanner = new Scanner(System.in);
    static HashMap<Integer, String[]> saying_list = new HashMap<Integer, String[]>();   //입력받은 정보 임시 저장공간
    static String path = "db/wiseSaying";

    //주 구동 함수
    public static void run() {
        System.out.println("== 명언 앱 ==");

        //HashMap<Integer, String[]> saying_list = new HashMap<Integer, String[]>();

        //String path = "db/wiseSaying";
        File folder = new File(path);

        int next_num = getNextId(path);

        while (true) {
            System.out.print("명령) ");
            String query = scanner.nextLine();

            //명언 등록
            if (query.equals("등록")) {
                writeSaying(next_num);

                next_num++;
            }

            //명언 목록
            else if (query.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                //저장된 목록들
                readFile(path);

                //저장 안 된 목록들
                readSaying();
            }

            //명언 삭제
            else if (query.contains("삭제?id=")) {
                int del_num = Integer.parseInt(query.substring(query.indexOf("=")+1));

                deleteSaying(del_num, path);
                next_num = getNextId(path);
            }

            //명언 수정
            else if (query.contains("수정?id=")) {
                int edit_num = Integer.parseInt(query.substring(query.indexOf("=")+1));

                if (saying_list.containsKey(edit_num)) {
                    String[] values = saying_list.get(edit_num);
                    System.out.println("명언(기존) : " + values[0]);
                    System.out.print("명언 : ");
                    String new_saying = scanner.nextLine();

                    System.out.println("이름(기존) : " + values[1]);
                    System.out.print("이름 : ");
                    String new_name = scanner.nextLine();

                    saying_list.put(edit_num, new String[]{new_name, new_saying});
                } else {
                    String file_path = path + "/" + edit_num + ".json";
                    File file = new File(file_path);
                    if (file.exists()) {
                        modifyFile(file_path);
                    } else {
                        System.out.println("파일이 존재하지 않습니다.");
                    }
                }
            }

            //명언 저장
            else if (query.equals("빌드")) {
                saveSaying(path);

                saying_list.clear();
                //최신 번호 갱신
                next_num = getNextId(path);
            }

            //종료
            else if (query.equals("종료")) {
                break;
            }
        }
    }

    //명언 등록 함수
    private static void writeSaying(int next_num) {
        System.out.print("명언 : ");
        String saying = scanner.nextLine();
        System.out.print("작가 : ");
        String name = scanner.nextLine();

        saying_list.put(next_num, new String[]{name, saying});
        System.out.println(next_num + "번 명언이 등록되었습니다.");
    }

    //명언 출력 함수
    private static void readSaying() {
        for (Map.Entry<Integer, String[]> i : saying_list.entrySet()) {
            int cur_num = i.getKey();
            String cur_saying = i.getValue()[0];
            String cur_name = i.getValue()[1];

            System.out.println(cur_num + " / " + cur_saying + " / " + cur_name + "    ->    (저장되지 않음)");
        }
    }

    //최신 번호를 가져오는 함수
    private static int getNextId(String path) {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("디렉토리가 존재하지 않습니다. " + path);
            return 1;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return 1;
        }

        List<Integer> ids = new ArrayList<>();
        for (File file : files) {
            String name = file.getName();

            if (name.endsWith(".json")) {
                try {
                    String number = name.replace(".json", "");
                    int id = Integer.parseInt(number);

                    ids.add(id);
                } catch (NumberFormatException e) {
                    System.out.println("숫자 변환 실패 : " + name);
                }
            }
        }

        Collections.sort(ids);
        for (int i = 1; i < ids.get(ids.size() - 1); i++) {
            if (!ids.contains(i)) {
                return i;
            }
        }

        return ids.get(ids.size() - 1) + 1;
    }

    //json파일 명언 출력 함수
    private static void readFile(String path) {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line.trim());
                }

                String json = jsonContent.toString();
                String id = extractValue(json, "id").replace("\"", "");
                String author = extractValue(json, "author").replace("\"", "");
                String content = extractValue(json, "content").replace("\"", "");

                System.out.println(id + " / " + author + " / " + content);

            } catch (IOException e) {
                return;
            }
        }
    }

    //json파일의 번호 추출 함수
    private static String extractValue(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) {
            return "값 없음";
        }

        start += pattern.length();
        int end;
        if (json.charAt(start) == '"') {
            start++;
            end = json.indexOf("\"", start);
            if (end == -1) return "값 없음";
        } else {
            end = json.indexOf(",", start);
            if (end == -1) {
                end = json.indexOf("}", start);
            }
        }

        return json.substring(start, end);
    }

    //json파일의 내용 수정 함수
    private static void modifyFile(String path) {
        File file = new File(path);

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder jsonContent = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                reader.close();

                String json = jsonContent.toString();
                String[] jsonParts = parseJson(json);

                System.out.println("명언(기존) : " + jsonParts[1]);
                System.out.print("명언 : ");
                String newSaying = scanner.nextLine();

                System.out.println("이름(기존) : " + jsonParts[2]);
                System.out.print("이름 : ");
                String newName = scanner.nextLine();

                String updatedJson = String.format("{\"id\": %s, \"content\": \"%s\", \"author\": \"%s\"}",
                        jsonParts[0], newSaying, newName);

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(updatedJson);
                writer.close();
            } catch (IOException e) {
                System.out.println("내용 수정 오류 : " + e.getMessage());
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

    //json파일의 내용 추출 함수
    private static String[] parseJson(String json) {
        String id = json.replaceAll(".*\"id\":\\s*(\\d+),.*", "$1");
        String content = json.replaceAll(".*\"content\":\\s*\"([^\"]+)\",.*", "$1");
        String author = json.replaceAll(".*\"author\":\\s*\"([^\"]+)\".*", "$1");
        return new String[]{id, content, author};
    }

    //명언 저장 함수
    private static void saveSaying(String path) {
        for (Map.Entry<Integer, String[]> i : saying_list.entrySet()) {
            int cur_num = i.getKey();
            String[] data = i.getValue();

            String file_name = path + "/" + cur_num + ".json";

            try (FileWriter writer = new FileWriter(file_name)) {
                writer.write("{\n");
                writer.write(String.format("  \"id\": %d,\n", cur_num));
                writer.write(String.format("  \"content\": \"%s\",\n", data[1]));
                writer.write(String.format("  \"author\": \"%s\"\n", data[0]));
                writer.write("}");
            } catch (IOException e) {
                System.out.println("오류 발생 : " + e.getMessage());
            }
        }
    }

    //명언 삭제 함수
    private static void deleteSaying(int id, String path) {
        if (saying_list.containsKey(id)) {
            saying_list.remove(id);
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            String file_path = path + "/" + id + ".json";

            File file = new File(file_path);

            if (file.exists()) {
                if (file.delete()) {
                    System.out.println(id + "번 명언이 삭제되었습니다.");
                } else {
                    System.out.println(id + "번 명언은 존재하지 않습니다.");
                }
            } else {
                System.out.println(id + "번 명언은 존재하지 않습니다.");
            }
        }
    }
}