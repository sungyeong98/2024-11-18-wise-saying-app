package org.example;

public class WiseSaying {
    private final int id;
    private String author;
    private String content;

    public WiseSaying(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public int getId(){
        return id;
    }

    public String getAuthor(){
        return author;
    }

    public String getContent(){
        return content;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setContent(String content){
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("%d / %s / %s", id, author, content);
    }
}
