package com.shop.app;

import com.shop.contract.IDeliveryService;
import com.shop.contract.INotifier;
import com.shop.contract.IOrderService;
import com.shop.contract.IPayment;
import com.shop.core.OrderProcessor;
import com.shop.delivery.CourierDelivery;
import com.shop.domain.Cart;
import com.shop.domain.Order;
import com.shop.domain.OrderItem;
import com.shop.domain.Product;
import com.shop.domain.User;
import com.shop.notify.EmailNotifier;
import com.shop.payment.CreditCardPayment;
import com.shop.service.OrderServiceImpl;
import com.shop.storage.OrderStorage;
import com.shop.storage.Storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner in = new Scanner(System.in);

    private static final List<Product> catalog = new ArrayList<>();
    private static final Map<Integer, User> users = new LinkedHashMap<>();
    private static final Cart cart = new Cart();

    private static User current;
    private static Order order;

    private static IOrderService orderService;
    private static IPayment payment;
    private static IDeliveryService delivery;
    private static INotifier notifier;
    private static OrderProcessor processor;

    public static void main(String[] args) {
        init();
        menu();
    }

    private static void init() {
        catalog.add(new Product(1, "Пицца Маргарита", 24.50));
        catalog.add(new Product(2, "Суши-сет Филадельфия", 42.00));
        catalog.add(new Product(3, "Бургер Чизбургер", 18.90));
        catalog.add(new Product(4, "Паста Карбонара", 27.00));
        catalog.add(new Product(5, "Салат Цезарь", 15.50));
        catalog.add(new Product(6, "Кофе Латте", 8.00));
        catalog.add(new Product(7, "Чизкейк Нью-Йорк", 12.00));

        orderService = new OrderServiceImpl();
        payment      = new CreditCardPayment("4276-****-****-1234");
        delivery     = new CourierDelivery("Минск, ул. Ленина 1");
        notifier     = new EmailNotifier("smtp.food-delivery.by");

        processor = new OrderProcessor(orderService, payment, delivery, notifier);

        Storage.init();
        OrderStorage.init();
    }

    private static void menu() {
        while (true) {
            System.out.println();
            System.out.println("Активный пользователь: " + activeLabel());
            System.out.println("1.  Создать пользователя");
            System.out.println("2.  Выбрать пользователя");
            System.out.println("3.  Показать меню блюд");
            System.out.println("4.  Добавить блюдо в корзину");
            System.out.println("5.  Показать корзину");
            System.out.println("6.  Оформить заказ");
            System.out.println("7.  Показать заказ");
            System.out.println("8.  Оплатить и доставить");
            System.out.println("9.  Информация о системе");
            System.out.println("10. Показать всех пользователей (из файла)");
            System.out.println("11. Показать все заказы (из файла)");
            System.out.println("0.  Выход");
            System.out.print("Выбор: ");

            String s = in.nextLine().trim();

            switch (s) {
                case "1"  -> createUser();
                case "2"  -> selectUser();
                case "3"  -> showCatalog();
                case "4"  -> addToCart();
                case "5"  -> showCart();
                case "6"  -> makeOrder();
                case "7"  -> showOrder();
                case "8"  -> payAndDeliver();
                case "9"  -> showInfo();
                case "10" -> showAllUsers();
                case "11" -> showAllOrders();
                case "0"  -> {
                    System.out.println("Пока!");
                    return;
                }
                default -> System.out.println("Неверный пункт.");
            }
        }
    }

    private static String activeLabel() {
        return current == null
                ? "— (не выбран)"
                : current.getName() + " #" + current.getId();
    }

    private static void createUser() {
        System.out.print("Ваше имя: ");
        String name = in.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Имя не может быть пустым.");
            return;
        }
        int id = Storage.nextUserId();
        User u = new User(id, name);
        users.put(id, u);
        Storage.saveUser(u);
        current = u;
        System.out.println("Создан и выбран: " + activeLabel());
    }

    private static void selectUser() {
        if (users.isEmpty()) {
            System.out.println("Пользователей ещё нет. Создайте (п.1).");
            return;
        }
        System.out.println("--- Выбор пользователя ---");
        for (User u : users.values()) {
            String mark = (current != null && current.getId() == u.getId())
                    ? " ← текущий" : "";
            System.out.printf("  #%d %s%s%n", u.getId(), u.getName(), mark);
        }
        System.out.print("ID пользователя: ");
        int id = readInt();
        User u = users.get(id);
        if (u == null) {
            System.out.println("Пользователь не найден.");
            return;
        }
        current = u;
        System.out.println("Теперь активен: " + activeLabel());
    }

    private static void showCatalog() {
        System.out.println("--- Меню блюд ---");
        for (Product p : catalog) {
            System.out.println("  " + p.info());
        }
    }

    private static void addToCart() {
        if (current == null) {
            System.out.println("Сначала создайте или выберите пользователя (п.1 или п.2).");
            return;
        }
        showCatalog();
        System.out.print("ID блюда: ");
        int id = readInt();
        Product p = catalog.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if (p == null) {
            System.out.println("Блюдо не найдено.");
            return;
        }
        cart.add(p);
        System.out.printf("Добавлено для %s: %s%n", current.getName(), p.info());
    }

    private static void showCart() {
        if (current == null) {
            System.out.println("Сначала создайте или выберите пользователя.");
            return;
        }
        if (cart.getItems().isEmpty()) {
            System.out.println("Корзина пуста у " + activeLabel());
            return;
        }
        System.out.println("Корзина: " + activeLabel());
        double sum = 0;
        for (Product p : cart.getItems()) {
            System.out.println("  " + p.info());
            sum += p.getPrice();
        }
        System.out.printf("Итого: %.2f%n", sum);
    }

    private static void makeOrder() {
        if (current == null) {
            System.out.println("Сначала создайте или выберите пользователя.");
            return;
        }
        if (cart.getItems().isEmpty()) {
            System.out.println("Корзина пуста у " + activeLabel());
            return;
        }
        order = processor.process(current);
        for (Product p : cart.getItems()) {
            order.add(new OrderItem(p, 1));
        }
        current.place(order);
        cart.clear();
        OrderStorage.save(order);
        System.out.printf("Заказ #%d оформлен для %s. Сумма: %.2f%n", order.getId(), current.getName(), order.total());
    }

    private static void showOrder() {
        if (order == null) {
            System.out.println("Заказ ещё не создан.");
            return;
        }
        System.out.println("Заказ #" + order.getId());
        System.out.println("Клиент: " + order.getCustomer().getName() + " #" + order.getCustomer().getId());
        System.out.printf("Сумма: %.2f%n", order.total());
    }

    private static void payAndDeliver() {
        if (order == null) {
            System.out.println("Сначала оформите заказ");
            return;
        }
        System.out.println("Оплата и доставка");
        processor.checkout(order);
    }

    private static void showInfo() {
        System.out.println("Архитектура системы");
        System.out.println("IOrderService    " + orderService.getClass().getSimpleName());
        System.out.println("IPayment         " + payment.getClass().getSimpleName());
        System.out.println("IDeliveryService " + delivery.getClass().getSimpleName());
        System.out.println("INotifier        " + notifier.getClass().getSimpleName());
        System.out.println("OrderProcessor   DI через конструктор (Loose Coupling)");
    }

    private static void showAllUsers() {
        System.out.println("Пользователи");
        var rows = Storage.loadUsers();
        if (rows.isEmpty()) {
            System.out.println("Нет записей.");
            return;
        }
        for (String r : rows) {
            System.out.println("  " + r);
        }
    }

    private static void showAllOrders() {
        System.out.println("Заказы");
        var rows = OrderStorage.loadOrders();
        if (rows.isEmpty()) {
            System.out.println("Нет записей.");
            return;
        }
        for (String r : rows) {
            System.out.println("  " + r);
        }
    }

    private static int readInt() {
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}