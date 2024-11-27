package org.example;

import java.util.List;

public class WiseSayingService {
    //private static String storagePath = "db/wiseSaying";
    private WiseSayingRepository repo;

    public WiseSayingService(WiseSayingRepository repo) {
        this.repo = repo;
    }

    // 명언 추가
    public int addSaying(String author, String content) {
        int nextId = repo.getNextId();
        repo.addSaying(nextId, author, content);
        repo.updateIdFile(nextId + 1);

        return nextId;
    }

    /*
    // 임시 명언 불러오기
    public ArrayList<WiseSaying> getTempSayings() {
        return new ArrayList<>(repo.getTemporarySayings().values());
    }
     */

    // 저장된 명언 불러오기
    public List<String> getSavedSayings() {
        return repo.getSavedSayings();
    }

    /*
    // 임시 명언 불러오기(키워드 입력시)
    public ArrayList<WiseSaying> getTempSayings(String keywordType, String keyword){
        return new ArrayList<>(repo.getTemporarySayings(keywordType, keyword).values());
    }

    // 저장 명언 불러오기(키워드 입력시)
    public List<String> getSavedSayings(String keywordType, String keyword) {
        return repo.getSavedSayings(keywordType, keyword);
    }
     */

    // 명언 불러오기  ->  최종 사용
    public List<String> getSayings(int page){
        return repo.getSayings(page);
    }

    // 명언 불러오기  ->  최종 사용
    public List<String> getSayings(String keywordType, String keyword, int page){
        return repo.getSayings(keywordType, keyword, page);
    }

    // 명언 수정
    public boolean updateSaying(int id, String newAuthor, String newSaying) {
        if(repo.updateTemporarySaying(id, newAuthor, newSaying)) {
            return true;
        }
        else{
            return repo.updateSavedSaying(id, newAuthor, newSaying);
        }
    }

    // 명언 삭제
    public boolean deleteSaying(int id) {
        if (repo.deleteTemporarySaying(id)){
            return true;
        }
        else{
            return repo.deleteSavedSaying(id);
        }
    }

    // 명언 저장
    public void saveSaying() {
        repo.saveTemporarySaying();
    }

    // 데이터 크기 불러오기
    public int getSayingsSize(){
        return repo.getSayingsSize();
    }

    // 데이터 크기 불러오기(키워드)
    public int getSayingsSize(String keywordType, String keyword){
        return repo.getSayingsSize(keywordType, keyword);
    }
}
