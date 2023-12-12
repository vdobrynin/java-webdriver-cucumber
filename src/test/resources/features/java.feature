@java
Feature: Java feature

    @java1
    Scenario: Java steps
        Given I say hello world

    @java2
    Scenario: Java steps with two arguments
        Given I perform action with "my var" and "my VAR"

    @java3
    Scenario: Comparison
        Given I compare "Los Altos" and "Los Altos"
        Given I compare "Los Altos" and "LOS ALTOS"

    @java4
    Scenario: Java steps 2
        Given I write java code

    @java5
    Scenario: Java steps with arguments
        Given I print "Java" argument

    @java6
    Scenario: Java steps with numbers
        Given I run operators with 10 and 3

    @java7
    Scenario: Java print url
        Given I print url for "sample" page
        Given I print url for "quote" page

    @java8
    Scenario: Coding challenge
        Given I swap variables
        And I print numbers

    @java9
    Scenario: Java arrays
        Given I print arrays
        And I swap swaps two array elements 3rd and 5th

    @java10
    Scenario: Map example
        Given I work with maps

    @java11
    Scenario: Coding challenge 2
        Given I want exchange first and last numbers in an array
        And I write loop even numbers from 1st to 10th
        And I print characters from 3rd position to 8th in a string

    @java12
    Scenario: Animal Classes Obj
        Given I work with classes

    @java13
    Scenario: FizzBuzz
        Given I solve FizzBuzz challenge with 20 number

    @java14
    Scenario: Challenges
        Given I solve second coding challenge

    @java15
    Scenario: Challenges II
        Given I solve coding challenge
        And I accepts integer number and returns divisible by 3 and 4
        And I accepts integer number and prints divisible by 2 by 5 by 10

