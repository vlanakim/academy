# ComplexTask + CyclicBarrier Demo

Пример синхронизации выполнения нескольких задач с помощью `CyclicBarrier` и `ExecutorService`.

Классы:
- `ComplexTask` — симулирует сложную задачу.
- `ComplexTaskExecutor` — запускает несколько задач параллельно и синхронизирует их.
- `TestComplexTaskExecutor` — тест: два потока параллельно вызывают `executeTasks(5)`.