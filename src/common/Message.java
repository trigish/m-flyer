package common;

/**
 * Created by Tim on 25.04.2016.
 */
public class Message {

    String text;
    User author;

    //TODO implement timestamp


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
