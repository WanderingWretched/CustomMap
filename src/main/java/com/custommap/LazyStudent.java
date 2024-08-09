package com.custommap;

public class LazyStudent extends Student {

    public LazyStudent(String name, int age) {
        super(name, age);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.name.equals(((LazyStudent) obj).name))
            if (this.age == ((LazyStudent) obj).age)
                return true;
        return false;
    }
}
