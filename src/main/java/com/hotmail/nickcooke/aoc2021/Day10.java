package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day10 extends AoCSolution {
    
    Map<Character, Character> closingToOpeningCharacterMap = new HashMap<>();
    Map<Character, Character> openingToClosingCharacterMap;
    Map<Character, Integer> closingCharacterSyntaxScoreMap = new HashMap<>();
    Map<Character, Integer> openingCharacterCompletionScoreMap = new HashMap<>();
    
    public static void main( String[] args ) {
        Day10 day10 = new Day10();
        day10.getInput();
        day10.solve();
    }
    public Day10(){
        closingToOpeningCharacterMap.put( ')', '(' );
        closingToOpeningCharacterMap.put( ']', '[' );
        closingToOpeningCharacterMap.put( '}', '{' );
        closingToOpeningCharacterMap.put( '>', '<' );
        closingCharacterSyntaxScoreMap.put( ')', 3 );
        closingCharacterSyntaxScoreMap.put( ']', 57 );
        closingCharacterSyntaxScoreMap.put( '}', 1197 );
        closingCharacterSyntaxScoreMap.put( '>', 25137 );
        openingCharacterCompletionScoreMap.put( '(', 1 );
        openingCharacterCompletionScoreMap.put( '[', 2 );
        openingCharacterCompletionScoreMap.put( '{', 3 );
        openingCharacterCompletionScoreMap.put( '<', 4 );
        openingToClosingCharacterMap = closingToOpeningCharacterMap.entrySet().stream().collect( Collectors.toMap( Map.Entry::getValue, Map.Entry::getKey ) );
    }
    
    private void solve() {
        int syntaxErrorScore = 0;
        List<Long> completionScores = new ArrayList<>();
        lineLoop: for ( Iterator<String> inputLinesIterator = inputLines.iterator(); inputLinesIterator.hasNext(); ) {
            Deque<Character> openingCharacterStack = new ArrayDeque<>();
            for ( char c : inputLinesIterator.next().toCharArray() ) {
                if ( closingToOpeningCharacterMap.containsKey( c ) ) {
                    if ( closingToOpeningCharacterMap.get( c ).equals( openingCharacterStack.peek() ) ) {
                        openingCharacterStack.pop();
                    }
                    else {
                        syntaxErrorScore += closingCharacterSyntaxScoreMap.get( c );
                        inputLinesIterator.remove();
                        continue lineLoop;
                    }
                }
                else {
                    openingCharacterStack.push( c );
                }
            }
            long completionScore = 0L;
            while ( !openingCharacterStack.isEmpty() ) {
                completionScore = (completionScore * 5) + openingCharacterCompletionScoreMap.get( openingCharacterStack.pop() );
            }
            completionScores.add(completionScore);
        }
        Collections.sort(completionScores);
        System.out.println( "Part 1: " + syntaxErrorScore );
        System.out.println( "Part 2: " + completionScores.get((completionScores.size()/2)) );
    }
}