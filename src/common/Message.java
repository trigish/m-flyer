package common;

import java.io.Serializable;

/**
 * An object of this class represents a single blog post.
 * It is not used to represent "special" messages needed for communications between servers (and/or clients).
 */
public class Message implements Serializable {

    String text;
    User author;

    public Message(String text, User author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }
}
