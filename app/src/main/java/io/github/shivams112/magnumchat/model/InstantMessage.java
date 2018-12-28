package io.github.shivams112.magnumchat.model;

public class InstantMessage {
    private String inputMsg;
    private String user;

    public InstantMessage(){

    }

    public InstantMessage(String inputMsg, String user) {
        this.inputMsg = inputMsg;
        this.user = user;
    }

    public void setInputMsg(String inputMsg) {
        this.inputMsg = inputMsg;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getInputMsg() {

        return inputMsg;
    }

    public String getUser() {
        return user;
    }
}
