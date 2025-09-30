import java.lang.reflect.Array;

public class ArrayUtils {
    public static <T> T[] filter(T[] input, Filter<T> filter) {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(input.getClass().getComponentType(), input.length);

        for (int i = 0; i < input.length; i++) {
            result[i] = filter.apply(input[i]);
        }

        return result;
    }
}
