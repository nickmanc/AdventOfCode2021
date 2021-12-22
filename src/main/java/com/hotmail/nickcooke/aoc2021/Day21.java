package com.hotmail.nickcooke.aoc2021;

public class Day21 extends AoCSolution {
    protected void part1() {
        DeterministicDie die = new DeterministicDie();
        int p1CurrentSpace = 6;
        int p2CurrentSpace = 10;
        int p1CurrentScore = 0;
        int p2CurrentScore = 0;
        while ( p2CurrentScore < 1000 ) {
            p1CurrentSpace = makeMove( p1CurrentSpace, die );
            p1CurrentScore += p1CurrentSpace;
            if ( p1CurrentScore >= 1000 ) {
                break;
            }
            p2CurrentSpace = makeMove( p2CurrentSpace, die );
            p2CurrentScore += p2CurrentSpace;
        }
        System.out.println( "Part 1: " + Math.min( p1CurrentScore, p2CurrentScore ) * die.getRollCount() );
    }
    
    private int makeMove( int p1CurrentSpace, DeterministicDie die ) {
        return ( ( p1CurrentSpace + die.rollThree() - 1 ) % 10 ) + 1;
    }
    
    public static void main( String[] args ) {
        Day21 day21 = new Day21();
        day21.getInput();
        day21.part1();
    }
}

class DeterministicDie {
    int rollCount = 0;
    
    public int roll() {
        return rollCount++ % 100 + 1;
    }
    
    public int rollThree() {
        int roll1 = roll();
        int roll2 = roll();
        int roll3 = roll();
        System.out.println( "Rolled: " + roll1 + "," + roll2 + "," + roll3 );
        return roll1 + roll2 + roll3;
    }
    
    public int getRollCount() {
        return rollCount;
    }
}

