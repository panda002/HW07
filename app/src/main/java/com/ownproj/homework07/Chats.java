package com.ownproj.homework07;

public class Chats {
    String DateTime;
    String Message;
    String Owner;

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    @Override
    public String toString() {
        return "Chats{" +
                "DateTime='" + DateTime + '\'' +
                ", Message='" + Message + '\'' +
                ", Owner='" + Owner + '\'' +
                '}';
    }
}
