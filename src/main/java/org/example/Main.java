package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String query;
        String saying;
        String name;
        int num=1;
        Scanner temp = new Scanner(System.in);

        HashMap<Integer, String[]> saying_list = new HashMap<Integer, String[]>();
        PriorityQueue<Integer> recycledNumbers = new PriorityQueue<>();

        while (true){
            System.out.print("명령) ");
            query = temp.nextLine();

            //명언 등록
            if(query.equals("등록")){
                System.out.print("명언 : ");
                saying = temp.nextLine();
                System.out.print("작가 : ");
                name = temp.nextLine();

                int col_num;
                if(!recycledNumbers.isEmpty()){
                    col_num = recycledNumbers.poll();
                }
                else{
                    col_num = num++;
                }

                saying_list.put(col_num,new String[]{name,saying});
                System.out.println(col_num + "번 명언이 등록되었습니다.");
            }

            //명언 목록
            else if(query.equals("목록")){
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                for(Map.Entry<Integer, String[]> i : saying_list.entrySet()){
                    int cur_num = i.getKey();
                    String cur_saying = i.getValue()[0];
                    String cur_name = i.getValue()[1];

                    System.out.println(cur_num + " / " + cur_saying + " / " + cur_name);
                }

            }

            else if(query.contains("삭제?id=")){
                int del_num = Integer.parseInt(query.substring(6, query.length()));

                if(!saying_list.containsKey(del_num)){
                    System.out.println(del_num + "번 명언은 존재하지 않습니다.");
                }
                else{
                    recycledNumbers.add(del_num);
                    saying_list.remove(del_num);
                    System.out.println(del_num + "번 명언이 삭제되었습니다.");
                }
            }

            else if(query.contains("수정?id=")){
                int edit_num = Integer.parseInt(query.substring(6, query.length()));

                if(!saying_list.containsKey(edit_num)){
                    continue;
                }
                else{
                    String[] values = saying_list.get(edit_num);
                    System.out.println("명언(기존) : "+values[0]);
                    System.out.print("명언 : ");
                    String new_saying = temp.nextLine();

                    System.out.println("이름(기존) : " + values[1]);
                    System.out.print("이름 : ");
                    String new_name = temp.nextLine();

                    saying_list.put(edit_num, new String[]{new_name, new_saying});
                }
            }

            else if(query.equals("빌드")){

            }

            //종료
            else if(query.equals("종료")){
                break;
            }
        }
    }
}