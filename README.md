# Parallel Stream

В этом проекте используется Parallel Stream API для вычисления средней оценки по каждому предмету среди студентов.
Каждый студент имеет набор предметов и соответствующих оценок.
Программа объединяет все данные и вычисляет среднее значение для каждого предмета с помощью многопоточной обработки.

## Пример

```java
List<Student> students = Arrays.asList(
                new Student("Student1", Map.of("Math", 90, "Physics", 85)),
                new Student("Student2", Map.of("Math", 95, "Physics", 88)),
                new Student("Student3", Map.of("Math", 88, "Chemistry", 92)),
                new Student("Student4", Map.of("Physics", 78, "Chemistry", 85))
);

Map<String, Double> averageGrades = students.parallelStream()
                .flatMap(student -> student.getGrades().entrySet().stream())
                .collect(Collectors.groupingByConcurrent(
                        Map.Entry::getKey,
                        Collectors.averagingDouble(Map.Entry::getValue)
                ));

averageGrades.forEach((subject, avg) ->
                System.out.println(subject + " -> " + avg)); //Chemistry -> 88.5
                                                             //Math -> 91.0
                                                             //Physics -> 83.66666666666667
