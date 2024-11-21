package org.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WiseSayingService {
    private final WiseSayingRepository repo;

    public WiseSayingService(WiseSayingRepository repo) {
        this.repo = repo;
    }

    // 명언 추가(완료)
    public int addSaying(String author, String saying) {
        int nextId = repo.getNextId();
        repo.addSaying(nextId, author, saying);

        return nextId;
    }

    // 모든 명언 불러오기(완료)
    public List<String> getAllSayings() {
        Map<Integer, String[]> sayings = repo.getTemporarySayings();
        List<String> result = sayings.entrySet().stream()
                .map(entry -> String.format("%d / %s / %s (저장되지 않음)",
                        entry.getKey(), entry.getValue()[0], entry.getValue()[1]))
                .collect(Collectors.toList());

        List<String> storedSayings = repo.getStoredSayings();
        result.addAll(storedSayings);
        return result;
    }

    // 명언 수정(완료)
    public boolean updateSaying(int id, String newAuthor, String newSaying) {
        if(repo.updateTemporarySaying(id, newAuthor, newSaying)) {
            return true;
        }
        else{
            return repo.updateSavedSaying(id, newAuthor, newSaying);
        }
    }

    // 명언 삭제(완료)
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
