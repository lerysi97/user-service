package com.example.userservice;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UserDaoImpl userDao = new UserDaoImpl();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:\n" +
                    "1 — Создать пользователя\n" +
                    "2 — Найти пользователя по id\n" +
                    "3 — Обновить пользователя\n" +
                    "4 — Удалить пользователя\n" +
                    "5 — Выйти");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Введите имя:");
                    String name = sc.nextLine();
                    System.out.println("Введите email:");
                    String email = sc.nextLine();
                    System.out.println("Введите возраст:");
                    int age = sc.nextInt();
                    sc.nextLine();
                    LocalDateTime createdAt = LocalDateTime.now();
                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setAge(age);
                    newUser.setCreatedAt(createdAt);
                    try {
                        userDao.save(newUser);
                        System.out.println("Пользователь создан:\n" + newUser);
                    }
                    catch (org.hibernate.exception.ConstraintViolationException e) {
                        System.out.println("Такой пользователь уже существует:\n" + newUser);
                    }
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Введите id: ");
                    Long id = sc.nextLong();
                    User foundUser = userDao.findById(id);
                    if (foundUser != null) {
                        System.out.println("Пользователь найден:\n" + foundUser);
                    } else {
                        System.out.printf("Пользователь с id = %d не найден", id);
                    }
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Введите id: ");
                    Long id1 = sc.nextLong();
                    sc.nextLine();
                    User targetUser  = userDao.findById(id1);
                    if (targetUser  != null) {
                        System.out.println("Введите новое имя:");
                        String name1 = sc.nextLine();
                        System.out.println("Введите новый email:");
                        String email1 = sc.nextLine();
                        System.out.println("Введите новый возраст:");
                        int age1 = sc.nextInt();
                        sc.nextLine();

                        targetUser .setName(name1);
                        targetUser .setEmail(email1);
                        targetUser .setAge(age1);
                        userDao.update(targetUser );
                        System.out.println("Пользователь обновлен:\n" + targetUser );
                    } else {
                        System.out.printf("Пользователь с id = %d не найден!", id1);
                    }
                    System.out.println();
                    break;
                case 4:
                    System.out.println("Введите id: ");
                    Long id2 = sc.nextLong();
                    User userToDelete = userDao.findById(id2);
                    if (userToDelete != null) {
                        userDao.deleteById(id2);
                        System.out.println("Пользователь удален:\n" + userToDelete);
                    } else {
                        System.out.printf("Пользователь с id = %d не найден!", id2);
                    }
                    System.out.println();
                    break;
                case 5:
                    System.out.println("Выход из программы");
                    return;
                default:
                    System.out.println("Вы ввели неправильное значение.");
            }
        }
    }
}
