# ForkJoin Factorial

В этом проекте используется `ForkJoinPool` для вычисления факториала числа с помощью рекурсивных задач (`RecursiveTask`).

Каждая задача вычисляет часть факториала и создаёт подзадачу для оставшейся части.
Методы `fork()` и `join()` обеспечивают параллельное выполнение и объединение результатов.

## Пример

```java
ForkJoinPool forkJoinPool = new ForkJoinPool();
FactorialTask factorialTask = new FactorialTask(10);

long result = forkJoinPool.invoke(factorialTask);
System.out.println("Факториал 10! = " + result); // Факториал 10! = 3628800
