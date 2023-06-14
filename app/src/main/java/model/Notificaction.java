package model;

public class Notificaction {
    private int id;
    private String user;
    private String message;

    public Notificaction(int id, String user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public Notificaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
