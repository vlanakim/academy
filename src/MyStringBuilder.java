import java.util.Stack;

public class MyStringBuilder {
    private StringBuilder builder;
    private Stack<String> history;

    public MyStringBuilder(String initial) {
        this.builder = new StringBuilder(initial);
        this.history = new Stack<>();
    }

    public MyStringBuilder append(String str) {
        saveState();
        builder.append(str);
        return this;
    }

    public MyStringBuilder delete(int start, int end) {
        saveState();
        builder.delete(start, end);
        return this;
    }

    public MyStringBuilder insert(int offset, String str) {
        saveState();
        builder.insert(offset, str);
        return this;
    }

    public MyStringBuilder replace(int start, int end, String str) {
        saveState();
        builder.replace(start, end, str);
        return this;
    }

    public void undo() {
        if (!history.isEmpty()) {
            builder = new StringBuilder(history.pop());
        }
    }

    private void saveState() {
        history.push(builder.toString());
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
