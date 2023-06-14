package pages;

public class Dog extends Animal {

  public Dog() {
  }

  public Dog(String name) { setName(name); }
  //    public void bark() { System.out.println(getName() + " is barking!"); }
  public void speak() {
    System.out.println(getName() + " is barking!");
  }
}
