package com.example.bolutho.Fragments.ModelClasses;

public class Chat {
    private String msg;
    private boolean isMine;

    public Chat(String msg, boolean isMine) {
        this.msg = msg;
        this.isMine = isMine;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
