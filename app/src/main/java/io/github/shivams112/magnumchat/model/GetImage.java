package io.github.shivams112.magnumchat.model;

public class GetImage {
    private String user,link;
    public GetImage(){}

    public GetImage(String user, String link) {
        this.user = user;
        this.link = link;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
