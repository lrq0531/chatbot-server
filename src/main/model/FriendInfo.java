package main.model;

public class FriendInfo {
    /**
     * Id of the friend
     */
    private String id;

    /**
     * Name
     */
    private String name;

    /**
     * Online
     */
    private boolean isOnline;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
