package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12 extends AoCSolution {
    Map<String, Cave> caves = new HashMap<>();
    
    Cave startCave;
    
    Cave endCave;
    
    public static void main( String[] args ) {
        Day12 day11 = new Day12();
        day11.getInput();
        day11.part1();
        day11.part2();
    }
    
    private void part1() {
        buildCaveMap();
        System.out.println( "Part 1: " + traverseCaves( startCave, new ArrayList<>(), new HashSet<>(), null ).size() );
    }
    
    private void part2() {
        buildCaveMap();
        Set<List<Cave>> uniquePaths = new HashSet<>();
        for ( Cave twoVisitCave : caves.values().stream().filter( c -> !( c.isBig || c.isStart || c.isEnd ) ).collect( Collectors.toList() ) ) {
            Set<List<Cave>> paths = new HashSet<>();
            uniquePaths.addAll( traverseCaves( startCave, new ArrayList<>(), new HashSet<>(), twoVisitCave ) );
        }
        System.out.println( "Part 2: " + uniquePaths.size() );
    }
    
    private Set<List<Cave>> traverseCaves( Cave currentCave, List<Cave> visitedCaves, Set<List<Cave>> discoveredPaths, Cave twoVisitCave ) {
        visitedCaves.add( currentCave );
        if ( currentCave.equals( endCave ) ) {
            //            System.out.println(visitedCaves.stream().map(c -> c.getCaveName()).collect( Collectors.joining("-")) );
            discoveredPaths.add( visitedCaves );
        }
        else {
            for ( Cave adjoiningCave : currentCave.adjoiningCaves ) {
                if ( adjoiningCave.isBig || !visitedCaves.contains( adjoiningCave ) || ( adjoiningCave.equals( twoVisitCave ) && visitedCaves.stream().filter( c -> c.equals( twoVisitCave ) ).count() < 2 ) ) {
                    traverseCaves( adjoiningCave, new ArrayList<>( visitedCaves ), discoveredPaths, twoVisitCave );
                }
            }
        }
        return discoveredPaths;
    }
    
    private void buildCaveMap() {
        for ( String connection : inputLines ) {
            String cave1Name = connection.split( "-" )[0];
            String cave2Name = connection.split( "-" )[1];
            caves.putIfAbsent( cave1Name, new Cave(cave1Name) );
            caves.putIfAbsent( cave2Name, new Cave(cave2Name) );
            caves.get( cave1Name ).addAdjoiningCave( caves.get( cave2Name ) );
            caves.get( cave2Name ).addAdjoiningCave( caves.get( cave1Name ) );
        }
        for ( Cave cave : caves.values() ) {
            if ( cave.isStart ) {
                startCave = cave;
            }
            else if ( cave.isEnd ) {
                endCave = cave;
            }
        }
    }
    
    class Cave {
        
        String caveName;
        
        boolean isBig;
        
        boolean isStart;
        
        boolean isEnd;
        
        Set<Cave> adjoiningCaves;
        
        public Cave( String caveName ) {
            this.caveName = caveName;
            isBig = caveName.equals( caveName.toUpperCase() );
            adjoiningCaves = new HashSet<>();
            isStart = caveName.equalsIgnoreCase( "start" );
            isEnd = caveName.equalsIgnoreCase( "end" );
        }
        
        public void addAdjoiningCave( Cave adjoiningCave ) {
            adjoiningCaves.add( adjoiningCave );
        }
        
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            Cave cave = (Cave) o;
            return caveName.equals( cave.caveName );
        }
        
        @Override
        public int hashCode() {
            return Objects.hash( caveName );
        }
        
        @Override
        public String toString() {
            String adjoiningCaveString = adjoiningCaves.stream().map( c -> c.caveName ).collect( Collectors.joining( "," ) );
            return "Cave{" + "caveName='" + caveName + '\'' + ", isBig=" + isBig + ", isStart=" + isStart + ", isEnd=" + isEnd + ", adjoiningCaves=" + adjoiningCaveString + '}';
        }
    }
}