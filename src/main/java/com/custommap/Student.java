package com.custommap;

public class Student {
    String name;
    int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 37 * h + name.hashCode();
        h = 37 * h + age;
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.name.equals(((Student) obj).name))
            if (this.age == ((Student) obj).age)
                return true;
        return false;
    }
}
