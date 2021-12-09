package com.hotmail.nickcooke.aoc2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day8 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day8 day8 = new Day8();
        day8.getInput();
        day8.part1();
        day8.part2();
    }
    
    private void part1() {
        int counter = 0;
        for ( String inputLine : inputLines ) {
            String[] outputValues = inputLine.split( " \\| " )[1].split( " " );
            for ( int i = 0; i < outputValues.length; i++ ) {
                if ( Arrays.asList( 2, 3, 4, 7 ).contains( outputValues[i].length() ) ) {
                    counter++;
                }
            }
        }
        System.out.println( "Part 1: " + counter );
    }
    
    private void part2() {
        int total = 0;
        for ( String inputLine : inputLines ) {
            String[] inputValues = inputLine.split( " \\| " )[0].split( " " );
            String[] outputValues = inputLine.split( " \\| " )[1].split( " " );
            Map<Integer, String> digitToInputMap = new HashMap<>();
            for ( int i = 0; i < inputValues.length; i++ ) {//1,7,4 & 8 have unique numbers of segments
                switch ( inputValues[i].length() ) {
                    case 2:
                        digitToInputMap.put( 1, inputValues[i] );
                        break;
                    case 3:
                        digitToInputMap.put( 7, inputValues[i] );
                        break;
                    case 4:
                        digitToInputMap.put( 4, inputValues[i] );
                        break;
                    case 7:
                        digitToInputMap.put( 8, inputValues[i] );
                        break;
                    default:
                }
            }
            
            for ( int i = 0; i < inputValues.length; i++ ) {
                if ( !digitToInputMap.containsValue( inputValues[i] ) ) {
                    if ( inputValues[i].length() == 5 ) {
                        if ( containsSegmentsFor( inputValues[i], digitToInputMap.get( 1 ) ) ) {
                            digitToInputMap.put( 3, inputValues[i] );//of numbers with 5 segments, only 3 containst those in 1
                        }
                    }
                }
            }
            
            for ( int i = 0; i < inputValues.length; i++ ) {
                if ( !digitToInputMap.containsValue( inputValues[i] ) ) {
                    if ( inputValues[i].length() == 6 ) {
                        if ( containsSegmentsFor( inputValues[i], digitToInputMap.get( 3 ) ) ) {
                            digitToInputMap.put( 9, inputValues[i] );//of numbers with 6 segments, only 9 containst those in 3
                        }
                    }
                }
            }
            for ( int i = 0; i < inputValues.length; i++ ) {
                if ( !digitToInputMap.containsValue( inputValues[i] ) ) {
                    if ( inputValues[i].length() == 6 ) {
                        if ( containsSegmentsFor( inputValues[i], digitToInputMap.get( 7 ) ) ) {
                            digitToInputMap.put( 0, inputValues[i] );//of remaining numbers with 6 segments, only 0 containst those in 6
                        }
                    }
                }
            }
            for ( int i = 0; i < inputValues.length; i++ ) {
                if ( !digitToInputMap.containsValue( inputValues[i] ) ) {
                    if ( inputValues[i].length() == 6 ) {
                        digitToInputMap.put( 6, inputValues[i] );//last 6 segment number is 6
                    }
                }
            }
            
            for ( int i = 0; i < inputValues.length; i++ ) {
                if ( !digitToInputMap.containsValue( inputValues[i] ) ) {
                    if ( inputValues[i].length() == 5 ) {
                        if ( containsSegmentsFor( digitToInputMap.get( 6 ), inputValues[i] ) ) {
                            digitToInputMap.put( 5, inputValues[i] );//of remaining numbers with 5 segments, 6 contains those in 5
                        }
                        else {
                            digitToInputMap.put( 2, inputValues[i] );//remaining number must be 2
                        }
                    }
                }
            }
            String output = "";
            for ( String outputDigit : outputValues ) {
                output += getMatchingDigit( outputDigit, digitToInputMap );
            }
            total += Integer.parseInt( output );
        }
        System.out.println( "Part 2: " + total );
    }
    
    private String getMatchingDigit( String outputDigit, Map<Integer, String> inputToDigitMap ) {
        for ( Map.Entry<Integer, String> mapEntry : inputToDigitMap.entrySet() ) {
            String inputValue = mapEntry.getValue();
            char[] outputDigitChars = outputDigit.toCharArray();
            char[] inputDigitChars = inputValue.toCharArray();
            Arrays.sort( outputDigitChars );
            Arrays.sort( inputDigitChars );
            if ( Arrays.equals( inputDigitChars, outputDigitChars ) ) {
                return mapEntry.getKey() + "";
            }
        }
        throw new RuntimeException( "Couldn't find value" );
    }
    
    private boolean containsSegmentsFor( String inputValue, String matchValue ) {
        for ( int i = 0; i < matchValue.length(); i++ ) {
            String sub = matchValue.substring( i, i + 1 );
            if ( !inputValue.contains( matchValue.substring( i, i + 1 ) ) ) {
                return false;
            }
        }
        return true;
    }
    
}
