package com.hotmail.nickcooke.aoc2021;

import java.util.List;
import java.util.stream.Collectors;

public class Day1 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day1 day1 = new Day1();
        day1.getInput();
        day1.part1();
        day1.part2();
    }
    
    private void part1() {
        List<Integer> inputIntegers = inputLines.stream().map( Integer::parseInt ).collect( Collectors.toList() );
        int total = 0;
        for ( int i = 1; i < inputIntegers.size(); i++ ) {
            total += inputIntegers.get( i ) > inputIntegers.get( i - 1 ) ? 1 :0;
        }
        System.out.println("Part 1: " + total);
    }
    
    private void part2() {
        List<Integer> inputIntegers = inputLines.stream().map( Integer::parseInt ).collect( Collectors.toList() );
        int total = 0;
        for ( int i = 3; i < inputIntegers.size(); i++ ) {
            total += inputIntegers.get( i ) > inputIntegers.get( i - 3 ) ? 1 :0;
        }
        System.out.println("Part 2: " + total);
    }
}
