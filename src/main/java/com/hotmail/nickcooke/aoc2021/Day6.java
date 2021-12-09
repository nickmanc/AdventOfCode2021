package com.hotmail.nickcooke.aoc2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day6 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day6 day6 = new Day6();
        day6.getInput();
        day6.part1();
        day6.part2();
    }
    
    private void part1() {
        List<LanternFish> lanternFishSchool = Arrays.stream( inputLines.get( 0 ).split( "," ) ).map( timer -> new LanternFish( Integer.parseInt( timer )) ).collect( Collectors.toList() );
        for ( int i = 0; i < 80; i++ ) {
            int newFish = 0;
            for ( LanternFish lanternFish : lanternFishSchool ) {
                if ( lanternFish.getTimer() == 0 ) {
                    newFish++;
                }
                lanternFish.decrementTimer();
            }
            for ( int j = 0; j < newFish; j++ ) {
                lanternFishSchool.add( new LanternFish( 8 ) );
            }
        }
        System.out.println( "Part 1: " + lanternFishSchool.size() );
    }
    
    class LanternFish {
        int timer;
        
        public LanternFish( int timer ) {
            this.timer = timer;
        }
        
        public int getTimer() {
            return timer;
        }
        
        public void decrementTimer() {
            if (timer-- == 0 ) {
                timer = 6;
            }
        }
    }
    
    private void part2() {
        List<LanternFish> lanternFishSchool = Arrays.stream( inputLines.get( 0 ).split( "," ) ).map( timer -> new LanternFish( Integer.parseInt( timer )) ).collect( Collectors.toList() );
        Map<Integer, Long> daysToFishCountMap = new HashMap<>();
        for ( LanternFish fish : lanternFishSchool ) {
            daysToFishCountMap.merge( fish.getTimer(), 1L, Long::sum );
        }
        for ( int day = 1; day <= 256; day++ ) {
            long fishWithCompletedTimerCount = null != daysToFishCountMap.get( 0 ) ? daysToFishCountMap.get( 0 ) : 0;
            for ( int daysLeftOnTimer = 0; daysLeftOnTimer < 8; daysLeftOnTimer++ ) {
                daysToFishCountMap.put( daysLeftOnTimer, daysToFishCountMap.get( daysLeftOnTimer + 1 ) );
            }
            daysToFishCountMap.merge( 6, fishWithCompletedTimerCount, Long::sum );//timer goes back to six for fish when timer copletes
            daysToFishCountMap.put( 8, fishWithCompletedTimerCount );//and as many new fish are created
        }
        System.out.println( "Part 2: " + daysToFishCountMap.values().stream().reduce( 0L,Long::sum ));
    }
}

