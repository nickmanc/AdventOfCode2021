package com.hotmail.nickcooke.aoc2021;

public class Day2 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day2 day2 = new Day2();
        day2.getInput();
        day2.part1();
    }
    
    private void part1() {
        int horizontalPosition = 0;
        int part1Depth = 0;
        int part2Depth = 0;
        for (String line: inputLines){
            int x = Integer.parseInt( line.split( " " )[1]);
            if (line.contains( "forward" )){
                horizontalPosition += x;
                part2Depth += part1Depth * x;
            }
            else if (line.contains( "down" )){
                part1Depth += x;
            }
            else {
                part1Depth -= x;
            }
        }
        System.out.println("Part 1: " + horizontalPosition * part1Depth);
        System.out.println("Part 2: " + horizontalPosition * part2Depth);
    }
}
