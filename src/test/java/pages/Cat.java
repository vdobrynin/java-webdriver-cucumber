package pages;

public class Cat extends Animal {

    public Cat() {
    }

    public Cat(String name) {
        setName(name);
    }

    //    public void meow() { System.out.println(getName() + " is meowing!"); }
    public void speak() {
        System.out.println(getName() + " is meowing!");
    }
}
