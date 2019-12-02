package com.ownproj.homework07;

import android.os.Message;

public class Messages {

    String messages, name, time;

    public Messages(){}

    public Messages(String messages, String name, String time) {
        this.messages = messages;
        this.name = name;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messages='" + messages + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
