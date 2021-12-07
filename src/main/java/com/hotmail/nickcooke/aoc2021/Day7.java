package com.hotmail.nickcooke.aoc2021;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day7 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day7 day7 = new Day7();
        day7.getInput();
        day7.solve();
    }
    
    private void solve() {
        List<Integer> inputIntegers = Arrays.stream( inputLines.get( 0 ).split( "," ) ).map( Integer::parseInt ).collect( Collectors.toList() );
        Map<Integer, Integer> positionToCountMap = new HashMap<>();
        for ( Integer position : inputIntegers ) {
            positionToCountMap.merge( position, 1, Integer::sum );
        }
        int leastFuelPart1 = Integer.MAX_VALUE;
        int leastFuelPart2 = Integer.MAX_VALUE;
        for ( int horizontalPosition = Collections.min(positionToCountMap.keySet()); horizontalPosition <= Collections.max(positionToCountMap.keySet()); horizontalPosition++ ) {
            int part1Distance = 0;
            int part2Distance = 0;
            for ( Map.Entry<Integer, Integer> otherPosition : positionToCountMap.entrySet() ) {
                if ( otherPosition.getKey() != horizontalPosition ) {
                    part1Distance += Math.abs( horizontalPosition - otherPosition.getKey() ) * otherPosition.getValue();
                    part2Distance += triangleNumber( Math.abs( horizontalPosition - otherPosition.getKey() )) * otherPosition.getValue();
                }
            }
            leastFuelPart1 = Math.min( part1Distance, leastFuelPart1 );
            leastFuelPart2 = Math.min( part2Distance, leastFuelPart2 );
        }
        System.out.println( "Part 1: " + leastFuelPart1 );
        System.out.println( "Part 2: " + leastFuelPart2 );
    }
    
    private int triangleNumber( int number ) {
        return ( number * (number + 1) ) / 2;
    }
}
