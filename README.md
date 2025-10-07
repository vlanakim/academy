# Stream Collectors Example

В этом проекте с помощью Stream API и Collectors выполняется группировка заказов по продуктам и подсчёт их общей стоимости.
Результатом является список трёх самых дорогих продуктов и их суммарная стоимость.

## Пример

```java
List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0),
                new Order("Smartwatch", 600.0),
                new Order("Tablet", 700.0)
);

Map<String, Double> totalByProduct = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getProduct,
                        Collectors.summingDouble(Order::getCost)
                ));

List<Map.Entry<String, Double>> topProducts = totalByProduct.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .toList();

System.out.println("Top 3 most expensive products:");
topProducts.forEach(entry ->
                System.out.printf("%s -> total: %.2f%n", entry.getKey(), entry.getValue())
); // Top 3 most expensive products:
   // Laptop -> total: 2700,00
   // Smartphone -> total: 1700,00
   // Tablet -> total: 1200,00
