package pages;

public class Cow extends Animal {

	public Cow() {
	}

	public Cow(String name) {
		setName(name);
	}

//	public void moo() {
//		System.out.println(getName() + " is mooing!");
//	}

	public void speak() {
		System.out.println(getName() + " is mooing!");
	}
}
