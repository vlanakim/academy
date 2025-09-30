import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String[] words = {"a", "b", "a", "c", "b", "a"};
        Map<String, Integer> wordCount = Counter.countOccurrences(words);
        System.out.println(wordCount);
    }
}
