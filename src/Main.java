import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Integer[] nums = {1, 2, 3, 4};
        Integer[] doubled = ArrayUtils.filter(nums, n -> n * 2);

        System.out.println(Arrays.toString(doubled));
    }
}
