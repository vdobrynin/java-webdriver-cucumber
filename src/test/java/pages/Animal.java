package pages;

public abstract class Animal {

    private String name;

    public Animal() {
        name = " " ;
//        name = "nameless one" ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//                                            //    name or getName() it is no difference
    public void walk() {
//        System.out.println(getClass() + " " + name + " is walking!");
        System.out.println(/*getClass() + " " + */name + " is walking!");
    }

    public void sleep() {
//        System.out.println(getClass() + " " + name + " is sleeping!");
        System.out.println(/*getClass() + " " + */name + " is sleeping!");
    }

    public void eat(String what) {
//        System.out.println(getClass() + " " + name + " is eating " + what + "?");
        System.out.println(/*getClass() + " " + */name + " is eating " + what + "?");
    }

    public void speak() {
        System.out.println(" ");
//        System.out.println("Undefined");
    }
}
