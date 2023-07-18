package definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import pages.*;

import java.util.*;

public class JavaStepDefs {

    @Given("I say hello world")
    public void iSayHelloWorld() {
        System.out.println("Hello World!");
        String greeting = "Hello";
        String name = "I'm Slava.";
        String text = "I'm a student!";
        System.out.println(greeting.toUpperCase() + ", " + name);
        System.out.println(greeting + ", " + text);
    }

    @Given("^I perform action with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iPerformActionWithAnd(String str1, String str2) throws Throwable {
        System.out.println(str1);                                                     // this is from previous version
        System.out.println(str2);
        System.out.println(str1 + " " + str2);
        System.out.println(str1.toUpperCase() + " " + str2.toUpperCase());
        System.out.println(str1.equals(str2));
        System.out.println(str1.equalsIgnoreCase(str2));
        System.out.println(str1.contains(str2));

        System.out.println(str1.toUpperCase());
        System.out.println(str2.toLowerCase());
        System.out.println(str2.toUpperCase());

        System.out.println(str1.length());
        System.out.println(str2.length());

        System.out.println("Exact comparison ==: " + str1 + " != " + str2);
        System.out.println("Exact comparison equals: " + str1.equals(str2));

        System.out.println(str1.toLowerCase().equals(str2.toLowerCase()));
        System.out.println(str1.toUpperCase().equalsIgnoreCase((str2.toLowerCase())));

        System.out.println(str1.contains(str2));
    }

    @Given("^I compare \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iCompareAnd(String arg0, String arg1) throws Throwable {
        if (!arg0.equalsIgnoreCase(arg1)) {                                           // this is from previous
            System.out.println("Strings are not equal! " + arg0 + " and " + arg1);
        } else {
            System.out.println("Strings are not equal! " + arg0 + " and " + arg1);
        }

        if (arg0.equals(arg1)) {
            System.out.println("Strings are equal: " + arg0 + " " + arg1);
        } else {
            System.out.println("Strings are not equal: " + arg0 + " " + arg1);
        }
    }

    private boolean isPrime(int number) {
        if (number < 2) return false;
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Given("^I write java code$")
    public void iWriteJavaCode() throws Throwable {
        System.out.println("Hello World!");
        String var = "Say hello to";
        String firstName = "V-----y";
        String lastName = "D------n";
        String favoriteColor = "Blue";
        System.out.println(var + " " + firstName + " " + lastName + "!");

        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(favoriteColor);

        String greeting = "Hi";
        String text = "my name is ";
        //System.out.println(greeting + ", " + text);
        System.out.println(greeting + ", " + text + firstName + " " + lastName + ", " + "my favorite color is" + " " + favoriteColor + ".");

        String firstName1 = "Name";
        firstName1.getClass();
        firstName1.length();
        firstName1.toUpperCase();
        System.out.println(firstName1.toUpperCase());
        System.out.println(firstName1.getClass());
        System.out.println(firstName1.length());
    }

    @Given("^I print \"([^\"]*)\" argument$")
    public void iPrintArgument(String var) throws Throwable {
        System.out.println(var);
        System.out.println(var.toLowerCase());
        System.out.println(var.toUpperCase());
        System.out.println(var.length());
        System.out.println(var == "other string");
        System.out.println(var.equals("Java"));
        System.out.println(var.equalsIgnoreCase("java"));
        System.out.printf("%n");
        var = "other string";
        System.out.println(var == "other string");
        System.out.println(var.equals("other string"));
    }

    @Given("^I run operators with (\\d+) and (\\d+)$")
    public void iRunOperatorsWithAnd(int arg0, int arg1) {  // --> (int arg0, double arg1)
        System.out.println(arg0 / arg1);
        System.out.println(10 / 3.3);
        if (arg0 < arg1) {
            System.out.println("first number less than second!");
        } else {
            System.out.println("first number larger then second!");
        }
    }

    @Given("^I print url for \"([^\"]*)\" page$")
    public void iPrintUrlForPage(String site) throws Throwable {
        if (site.equalsIgnoreCase("yahoo")) {
            System.out.println("https://www.yahoo.com");
        } else if (site.equalsIgnoreCase("google")) {
            System.out.println("https://www.google.com");
        } else if (site.equalsIgnoreCase("quote")) {
            System.out.println("https://skryabin.com/market/quote.html");
        } else {
            System.out.println("Unsupported site: " + site);
        }
    }

    @Given("^I print arrays$")
    public void iPrintArrays() {
        int[] intArray = {3, 2, 55, 2};

        String[] groceryList = {"kiwi", "apples", "oranges", "plum"};
        System.out.println("------ forEach -----------");

        for (String item : groceryList) {
            System.out.println(item);
        }

        System.out.println("------- regular for ----------");

        for (int i = 0; i < groceryList.length; i++) {
            System.out.println(groceryList[i]);
        }

        System.out.println("-----------------");
        System.out.println(groceryList[1]);

        List<String> groceryArray = List.of("kiwi", "apples", "oranges", "plum");
        System.out.println("------ forEach -----------");

        for (String item : groceryArray) {
            System.out.println(item);
        }

        System.out.println("------ regular for -----------");
        for (int i = 0; i < groceryArray.size(); i++) {
            System.out.println(groceryArray.get(i));
        }
        System.out.println("-----------------");
        System.out.println(groceryArray.get(1));

        String str1 = new String("name");
        String str2 = "name";
        Integer i1 = Integer.valueOf(5);
        Integer i2 = 5;

        ArrayList<String> groceryArray2 = new ArrayList<>();
        groceryArray2.add("kiwi");
        groceryArray2.add("orange");

        Map<String, String> map = new HashMap<>();
        map.put("firstName", "John");
        map.put("lastName", "Doe");
    }

    @Given("I swap variables")
    public void iSwapVariables() {
        int x = 5;
        int y = 3;

        System.out.println("x: " + x);
        System.out.println("y: " + y);

        int temp = x;
        x = y;
        y = temp;
        System.out.println();
        System.out.println("x: " + x);
        System.out.println("y: " + y);
    }

    @And("I swap swaps two array elements {int}rd and {int}th")
    public void iSwapSwapsTwoArrayElementsRdAndTh(int arg0, int arg1) {
        System.out.println();
        int[] num = {5, 2, 9, 7, 3};

        for (int i = 0; i < num.length; i++) {
            int temp1 = num[2];
            int temp2 = num[4];
            num[4] = temp1;
            num[2] = temp2;
        }
        System.out.println(num[2] + " " + num[4]);
    }

    @And("I print numbers")
    public void iPrintNumbers() {
        int num = 10;
        for (int i = 1; i <= num; i++) {   // int i = 0; --> is wrong
            if (i % 3 == 0) {                 // for even num  i % 2
                System.out.println(i);
            }
        }
    }

    public boolean isDivisibleBy3(int num) {
        if (num % 3 == 0) {
            return true;

        } else {
            return false;
        }
    }

    @Given("I work with maps")
    public void iWorkWithMaps() {
        Map<String, String> user = Map.of("firstName", "Slava", "lastName", "Skryabin");
        System.out.println(user.get("lastName"));
        System.out.println(user.get("firstName"));

        Map<String, String> admin = Map.of("firstName", "John", "lastName", "Doe");
        System.out.println(admin.get("lastName"));
        System.out.println(admin.get("firstName"));
    }

    @Given("I want exchange first and last numbers in an array")
    public void iWantExchangeFirstAndLastNumbersInAnArray() {
        System.out.println();
        int[] num = {11, 22, 55, 44, 33};

        for (int i = 0; i < num.length; i++) {
            //int lastIndex = num.length - 1;
            //num[0] = lastIndex;
            //lastIndex = temp;
            int temp = num[0];
            num[0] = num[num.length - 1];
            num[num.length - 1] = temp;
            System.out.println(num[i]);
        }
        //return num[i];
    }

    @And("I write loop even numbers from {int}st to {int}th")
    public void iWriteLoopEvenNumbersFromStToTh(int arg0, int arg1) {
        System.out.println();
        int num = 10;
        for (int i = 1; i <= num; i++) {
//            if (i % 2 == 0) {
//                System.out.println(i);
//            }
            if (i % 3 == 0 && i % 2 != 0) {
                System.out.println(i);
            }
        }
    }

    @And("I print characters from {int}rd position to {int}th in a string")
    public void iPrintCharactersFromRdPositionToThInAString(int num, int num1) {
        System.out.println();
        //System.out.println("congratulations".substring(3, 8));
        String str = "congratulations";
        for (int i = 2; i < 7; i++) {
            System.out.print(str.charAt(i));
        }
    }

    @Given("I work with classes")
    public void iWorkWithClasses() {
//   Cat tom = new Cat("Tom");         // constructor (value "Tom")
        Animal tom = new Cat("Tom");
        tom.walk();
        tom.sleep();
        tom.eat("fish");
//		 tom.meow();
        tom.speak();
        System.out.println();

//      Dog jack = new Dog();
        Animal jack = new Dog();
        jack.walk();
        jack.sleep();
        jack.eat("bone");
//       jack.bark();
        jack.speak();
        System.out.println();

        Animal murka = new Cow("Murka");
        murka.walk();
        murka.sleep();
        murka.eat("grass");
        murka.speak();
    }

    @Given("I solve FizzBuzz challenge with {int} number")
    public void iSolveFizzBuzzChallengeWithNumber(int num) {
        for (int i = 1; i <= num; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.print("FizzBuzz ");
            } else if (i % 3 == 0) {
                System.out.print("Fuzz ");
            } else if (i % 4 == 0) {
                //System.out.print("");  //empty line print nothing
            } else if (i % 5 == 0) {
                System.out.print("Buzz ");
            } else {
                System.out.print(i + " ");
            }
        }
    }

    public int charOccurrence(String word, Character c) {
        char ch = 'c';
        Character ch2 = 'c';
        List<Integer> l = List.of(4, 2, 5);

        int count = 0;
        for (int i = 0; i < word.length(); i++) {

            if (word.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public List<Object> uniqueElements(List<Object> list1, List<Object> list2) {
        List<Object> result = new LinkedList<>(); //call empty constructor
        boolean isFound;

        for (Object item1 : list1) {
            isFound = false;

            for (Object item2 : list2) {
                if (item1.equals(item2)) {

                    isFound = true;
                }
            }
            if (!isFound) {
                result.add(item1);
            }
        }

        for (Object item2 : list2) {
            isFound = false;

            for (Object item1 : list1) {
                if (item2.equals(item1)) {
                    isFound = true;
                }
            }
            if (!isFound) {
                result.add(item2);
            }
        }
        return result;
    }

    Map<Character, Integer> occurrences(String str) {
        Map<Character, Integer> map = new LinkedHashMap<>();   // to count occurrences in the string in the order
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int count = 0;
            if (map.get(c) != null) {
                continue;
            }
            for (int j = 0; j < str.length(); j++) {

                if (str.charAt(j) == c) {
                    count++;
                }
            }
            map.put(c, count);
        }
        return map;
    }

    @Given("I solve coding challenge")
    public void iSolveCodingChallenge() {
        // Return Map of characters occurrence in the string
        System.out.println(occurrences("WebDriver"));

        // find the number of characters occurrence in word
        System.out.println(charOccurrence("WebDriver", 'e'));

        //Build a new list out of two lists unique elements
        List<Object> list1 = List.of(5, 3, 2, 6);
        List<Object> list2 = List.of(8, 7, 6, 3);
        System.out.println(uniqueElements(list1, list2));

        // 1. write a function that would exchange first and last numbers in an array

        int arr[] = {25, 4, 6, 34, 12, 21};
        for (int i = 0; i < arr.length; i++) {
            int temp = arr[0];
            arr[0] = arr[arr.length - 1];
            arr[arr.length - 1] = temp;
            //System.out.println(arr[i]);
        }


//    3. Write a function that prints all even numbers from 0 up to n

        int n = 21;

        for (int i = 0; i < n; i++) {

            if (i % 2 == 0) {

                System.out.println(i);
            }
        }
        // 4. Write a function that finds the largest element of an array and test it

        int arr1[] = {3, 7, 11, 33, 21, 5};

        int largest = Integer.MIN_VALUE;
        for (int i = 0; i < arr1.length; i++) {

            if (largest < i) {

                largest = i;
                //System.out.println("Largest: " + largest);
            }
            //System.out.println("Largest: " + largest);
        }
        System.out.println("Largest: " + largest);
//    5. Reverse a string
//
//    6. Reverse words in a sentence
//
//    7. Write a function that sorts an array
//
//    8. Write a function that would find if any two elements in an array result in sum
//
//    9. Write a function that would create an array of duplicated numbers from original one
//
//    10. Write a function, accepts integer argument
//    It should print all the numbers up to the argument
//        BUT:
//    if number is multiple of 3, it should print Fizz instead of number
//    if number is multiple of 5, it should print Buzz instead of number
//    If it is multiple of both 3 and 5, it should print FizzBuzz instead of number
//        Result for 20:
//    1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz 16 17 Fizz 19 Buzz
    }

    @Given("I solve second coding challenge")
    public void iSolveSecondCodingChallenge() {
        //System.out.println(factorial(14));
        // write a function that would determine if the number is prime
        int number = 17;
        System.out.println("Is " + number + " prime? " + isPrime(number));

        // find factorial using recursive algorithm
        System.out.println(factorial(5));
        // array search
        int[] arr = {2, 4, 6, 8, 9, 10, 14, 15, 19};
        int num = 14;

        System.out.println("Contains num? " + search(arr, num));
        // array binary search
        System.out.println("Contains binary num? " + binarySearch(arr, num));
    }

    public int factorial(int num) {
        if (num == 0) {
            return 1;
        }
        return num * factorial(num - 1);
    }

    public boolean search(int arr[], int num) {
        for (int element : arr) {
            if (element == num) {
                return true;
            }
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }

    public boolean binarySearch(int arr[], int num) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int middle = (low + high) / 2;
            if (arr[middle] == num) {
                return true;
            } else if (arr[middle] < num) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        return false;
    }

    public boolean isPrime1(int num) {
        if (num < 2) {
            return false;
        }
        if (num % 2 == 0 && num != 2) {
            return false;
        }

        for (int i = 3; i <= Math.sqrt(num); i += 2) { // take 2nd
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }


    @Given("I print {string}, {string} and {string}")
    public static void iPrintAnd(String arg0, String arg1, String arg2) {
        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        double d = scan.nextDouble();
        scan.nextLine();
        String s = scan.nextLine();

        System.out.println("String: " + s);
        System.out.println("Double: " + d);
        System.out.println("Int: " + i);
    }

    @Given("^I define map data$")
    public void iDefineMapData() throws Throwable {
//    HashMap formData = new HashMap();
//    "empty initialization of " () "<--create object"
//    key "String", value "String" will be "<String, String>"
        HashMap<String, String> formData = new HashMap<String, String>(); // initialization map (in Java it's call HashMap)

        formData.put("username", "d------n");           // "put" (key, value) or "get" (Object key) those methods we'll use
        formData.put("email", "v---a@happy.com");

        System.out.println(formData.get("email"));          // to print it out
        System.out.println(formData.get("username"));
        System.out.println(formData.get("wrong"));       // put wrong key
        System.out.println(formData);                           // hold HashMap from data
    }

    @And("I accepts integer number and returns divisible by {int} and {int}")
    public void iAcceptsIntegerNumberAndReturnsDivisibleByAnd(int num1, int num2) {
//         Write a function that accepts integer number and prints
//        "divisible by 3" if number is divisible by 3
//        "divisible by 4" if element is divisible by 4
//        "divisible by 3 and 4" if divisible by 3 and 4
        int num = 20;

        if (num % 3 == 0 && num % 4 == 0) {
            System.out.println("The " + num + " divisible by 3 & 4");
        } else if (num % 3 == 0) {
            System.out.println("The " + num + " divisible by 3");
        } else if (num % 4 == 0) {
            System.out.println("The " + num + " divisible by 4");
        } else {
            System.out.println("The " + num + " not divisible by 3, or 4, or 3 & 4 ");
        }
    }

    @And("I accepts integer number and prints divisible by {int} by {int} by {int}")
    public void iAcceptsIntegerNumberAndPrintsDivisibleByByBy(int arg0, int arg1, int arg2) {
//        Write a function that accepts integer number and prints
//        "divisible by 2" if number is divisible by 2
//        "divisible by 5" if element is divisible by 5
//        "divisible by 10" if element is divisible by 2 and 5

        int num = 33;

        if (num % 2 == 0 && num % 5 == 0) {
            System.out.println("The " + num + " divisible by 10, by 5, by 2");
        } else if (num % 2 == 0) {
            System.out.println("The " + num + " divisible by 2");
        } else if (num % 5 == 0) {
            System.out.println("The " + num + " divisible by 5");
        } else {
            System.out.println("The " + num + " not divisible by 2, or 5, or 2 & 5, or 10");
        }
    }
}


