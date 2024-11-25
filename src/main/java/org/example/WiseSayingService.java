package org.example;

import java.util.ArrayList;
import java.util.List;

public class WiseSayingService {
    //private static String storagePath = "db/wiseSaying";
    private WiseSayingRepository repo;

    public WiseSayingService(WiseSayingRepository repo) {
        this.repo = repo;
    }

    // 명언 추가
    public int addSaying(String author, String saying) {
        int nextId = repo.getNextId();
        repo.addSaying(nextId, author, saying);
        repo.updateIdFile(nextId + 1);

        return nextId;
    }

    // 임시 명언 불러오기
    public ArrayList<WiseSaying> getTempSayings() {
        return new ArrayList<>(repo.getTemporarySayings().values());
    }

    // 저장된 명언 불러오기
    public List<String> getSavedSayings() {
        return repo.getStoredSayings();
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
}
