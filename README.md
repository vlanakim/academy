# Counter Project

В этом проекте реализован метод, который считает количество вхождений каждого элемента в массиве.  
Результат работы метода — `Map`, где:
- ключ — элемент массива,
- значение — сколько раз он встретился.

## Пример

```java
String[] words = {"a", "b", "a", "c", "b", "a"};
Map<String, Integer> wordCount = Counter.countOccurrences(words);
System.out.println(wordCount); // {a=3, b=2, c=1}