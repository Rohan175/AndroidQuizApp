package com.rsking175453.com.iaa;

public class qaModel {


    private String question;
    private String answer;
    private boolean read;

    qaModel(String question, String answer){
        this.question=question;
        this.answer=answer;
        read = false;
    }


    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

}
