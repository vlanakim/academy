# Filter Project

Пример реализации метода `filter`, который принимает массив и интерфейс `Filter<T>`, применяющий функцию `apply` к каждому элементу массива.

## Пример

```java
Integer[] nums = {1, 2, 3, 4};
Integer[] doubled = ArrayUtils.filter(nums, n -> n * 2);
System.out.println(Arrays.toString(doubled)); // [2, 4, 6, 8]
