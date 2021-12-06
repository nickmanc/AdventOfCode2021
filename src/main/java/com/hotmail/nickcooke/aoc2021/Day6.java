package com.hotmail.nickcooke.aoc2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day6 day6 = new Day6();
        day6.getInput();
        day6.part1();
        day6.part2();
    }
    
    private void part1() {
        List<LanternFish> lanternFishSchool =  Arrays.asList( inputLines.get( 0 ).split( "," )).stream().map( timer -> new LanternFish( Integer.parseInt( timer ))).collect( Collectors.toList());
        
        for (int i = 0; i <80;i++){
            int newFish=0;
            for (LanternFish lanternFish:lanternFishSchool){
                if (lanternFish.getTimer() == 0){
                    newFish++;
                }
                lanternFish.decrementTimer();
            }
            for (int j=0; j<newFish;j++){
                lanternFishSchool.add( new LanternFish(8  ) );
            }
        }
        System.out.println("Part 1: " + lanternFishSchool.size());
    }
    
    private void part2() {
    }
}

class LanternFish {
    int timer;
    
    public LanternFish (int startTimer) {
        timer = startTimer;
    }
    
    public int getTimer () {
        return timer;
    }
    public void decrementTimer(){
        if (timer == 0 ){
            timer = 7;
        }
        timer--;
    }
    
    @Override
    public String toString() {
        return "LanternFish{" + "timer=" + timer + '}';
    }
}
