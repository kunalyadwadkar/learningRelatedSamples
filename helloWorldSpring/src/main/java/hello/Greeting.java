package hello;

public class Greeting {
    private final int id;
    private final String content;

    public Greeting(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }
}
