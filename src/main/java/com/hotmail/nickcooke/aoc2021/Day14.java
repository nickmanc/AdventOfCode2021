package com.hotmail.nickcooke.aoc2021;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 extends AoCSolution {
    
    public static final String RULE_SEPARATOR = " -> ";
    
    public static void main( String[] args ) {
        Day14 day14 = new Day14();
        day14.getInput();
        day14.solve();
    }
    
    private void solve() {
        System.out.println( "Part 1: " + solve( 10 ) );
        System.out.println( "Part 2: " + solve( 40 ) );
    }
    
    private long solve( int steps ) {
        String startingPolymer = inputLines.get( 0 );
        Map<String, String> pairInsertionRules = parsePairInsertionRules();
        Map<String, Long> pairCounts = getStartingPairCounts( startingPolymer );
        for ( int i = 0; i < steps; i++ ) {
            Map<String, Long> updatedPairCounts = new HashMap<>();
            for ( Map.Entry<String, Long> entry : pairCounts.entrySet() ) {
                updatedPairCounts.merge( entry.getKey().charAt( 0 ) + pairInsertionRules.get( entry.getKey() ), entry.getValue(), Long::sum );
                updatedPairCounts.merge( pairInsertionRules.get( entry.getKey() ) + entry.getKey().charAt( 1 ), entry.getValue(), Long::sum );
            }
            pairCounts = updatedPairCounts;
        }
        return calculateDifferenceBetweenMostAndLeastCommonCharacters( pairCounts, startingPolymer );
    }
    
    private Map<String, Long> getStartingPairCounts( String polymer ) {
        Map<String, Long> pairCounts = new HashMap<>();
        for ( int i = 2; i <= polymer.length(); i++ ) {
            pairCounts.put( polymer.substring( i - 2, i ), 1L );
        }
        return pairCounts;
    }
    
    private long calculateDifferenceBetweenMostAndLeastCommonCharacters( Map<String, Long> pairCount, String startPolymer ) {
        Map<Character, Long> characterOccurrences = new HashMap<>();
        characterOccurrences.put( startPolymer.charAt( 0 ), 1L );
        characterOccurrences.put( startPolymer.charAt( startPolymer.length() - 1 ), 1L );
        for ( Map.Entry<String, Long> entry : pairCount.entrySet() ) {
            characterOccurrences.merge( entry.getKey().charAt( 0 ), entry.getValue(), Long::sum );
            characterOccurrences.merge( entry.getKey().charAt( 1 ), entry.getValue(), Long::sum );
        }
        long maxOccurrence = characterOccurrences.values().stream().mapToLong( v -> v/2 ).max().orElseThrow();
        long minOccurrence = characterOccurrences.values().stream().mapToLong( v -> v/2 ).min().orElseThrow();
        return maxOccurrence-minOccurrence;
    }
    
    private Map<String, String> parsePairInsertionRules() {
        return inputLines.stream()
                .filter( l -> l.contains( RULE_SEPARATOR ) )
               .collect( Collectors.toMap(s -> s.split( RULE_SEPARATOR )[0], s -> s.split( RULE_SEPARATOR )[1]) );
    }
}