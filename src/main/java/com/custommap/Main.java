package com.custommap;

import java.util.*;
import java.util.Random;

public class Main {
    private static String getRandomString() {
        int a;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            a = r.nextInt(20);
            char c = (char) (a + 'a');
            sb.append(c);
        }
        return sb.toString();

    }

    private static Integer getRandomInt() {
        Random r = new Random();

        return new Integer(r.nextInt() + 1);

    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        Student student;
        LazyStudent lazyStudent;
        String name;
        int age;
        List<Student> list = new ArrayList<Student>();
        List<Student> list2 = new ArrayList<Student>();
        List<LazyStudent> list3 = new ArrayList<LazyStudent>();
        List<LazyStudent> list4 = new ArrayList<LazyStudent>();
        for (int i = 0; i < 2000; i++) {
            name = Main.getRandomString();
            age = Math.abs(Main.getRandomInt() % 30);
            student = new Student(name, age);
            lazyStudent = new LazyStudent(name, age);
            list.add(student);
            list3.add(lazyStudent);
        }
        list2.addAll(list);

        CustomMapInterface<Student, Integer> map = new CustomMapImpl<>(10);
        Iterator<Student> it = list.iterator();
        while (it.hasNext()) {
            student = it.next();
            map.put(student, Main.getRandomInt() % 10 + 1);
        }

        long time1 = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            student = list2.get(Math.abs(Main.getRandomInt() % 2000));
            map.get(student);
        }

        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time1);

        list4.addAll(list3);
        CustomMapInterface<LazyStudent, Integer> map2 = new CustomMapImpl<LazyStudent, Integer>(10);
        Iterator<LazyStudent> it2 = list3.iterator();
        while (it2.hasNext()) {
            lazyStudent = it2.next();
            map2.put(lazyStudent, Main.getRandomInt() % 10 + 1);
        }

        long time3 = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            lazyStudent = list4.get(Math.abs(Main.getRandomInt() % 2000));
            map2.get(lazyStudent);
        }

        long time4 = System.currentTimeMillis();
        System.out.println(time4 - time3);
    }
}