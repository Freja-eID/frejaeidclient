package com.verisec.frejaeid.client.beans.sign.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PushNotification {

    private final String title;
    private final String text;

    public static PushNotification create(String title, String text) {
        return new PushNotification(title, text);
    }

    @JsonCreator
    private PushNotification(@JsonProperty(value = "title") String title, @JsonProperty(value = "text") String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int hashCode() {
        return Objects.hash(title, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PushNotification other = (PushNotification) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PushNotification{" + "title=" + title + ", text=" + text + '}';
    }

}
