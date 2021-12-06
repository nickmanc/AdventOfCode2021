package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day3 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day3 day3 = new Day3();
        day3.getInput();
        day3.part1();
        day3.part2();
    }
    
    private void part1() {
        List<char []> binaryNumbers = inputLines.stream().map( s -> s.toCharArray() ).collect( Collectors.toList() );
        Map<Integer,Integer> sumOfDigits = new HashMap<>();
        for (char [] binaryNumber : binaryNumbers) {
            for (int i = 0; i < binaryNumber.length ; i++)
            {
                int binaryValue = Character.getNumericValue( binaryNumber[i] );
                if (sumOfDigits.containsKey( i )){
                    sumOfDigits.put( i, sumOfDigits.get(i) + binaryValue );
                }
                else {
                    sumOfDigits.put( i, binaryValue );
                }
            }
        }
        String gammaString = "";
        String epsilonString = "";
        for (int i = 0; i < sumOfDigits.size(); i++){
            if (sumOfDigits.get(i) >= binaryNumbers.size()/2){
                gammaString += "1";
                epsilonString += "0";
            }
            else {
                gammaString += "0";
                epsilonString += "1";
            }
        }
        System.out.println("Part 1: " + Integer.parseInt( gammaString,2 ) * Integer.parseInt( epsilonString,2 ));
    }
    
    private void part2() {
        List<char []> binaryNumbers = inputLines.stream().map( s -> s.toCharArray() ).collect( Collectors.toList() );
        int ogr = calculateOGR( new ArrayList<>(binaryNumbers) );
        int c02sr = calculateC02sr( new ArrayList<>(binaryNumbers) );
        System.out.println("Part 2: " + ogr * c02sr);
    }
    
    private int calculateOGR( List<char[]> binaryNumbers ) {
        int numberLength = binaryNumbers.get(0).length;
        for ( int index = 0; index < numberLength; index++ ) {
            int totalAtIndex = getTotalAtIndex( binaryNumbers, index );
            double is1 = binaryNumbers.size() / 2.0;
            if ( totalAtIndex >=is1 ) {
                final int j = index;
                binaryNumbers.removeIf( b -> b[j] == '0' );
            }
            else {
                final int j = index;
                binaryNumbers.removeIf( b -> b[j] == '1' );
            }
            if ( binaryNumbers.size() == 1 ) {
                break;
            }
        }
        return Integer.parseInt( String.valueOf( binaryNumbers.get( 0 ) ),2 );
    }
    
    private int calculateC02sr( List<char[]> binaryNumbers ) {
        int numberLength = binaryNumbers.get(0).length;
        for ( int index = 0; index < numberLength; index++ ) {
            int totalAtIndex = getTotalAtIndex( binaryNumbers, index );
            double is1 = binaryNumbers.size() / 2.0;
            if ( totalAtIndex < is1 ) {
                final int j = index;
                binaryNumbers.removeIf( b -> b[j] == '0' );
            }
            else {
                final int j = index;
                binaryNumbers.removeIf( b -> b[j] == '1' );
            }
            if ( binaryNumbers.size() == 1 ) {
                break;
            }
        }
        return Integer.parseInt( String.valueOf( binaryNumbers.get( 0 ) ),2 );
    }
    
    private int getTotalAtIndex( List<char[]> binaryNumbers, int index ) {
        int total=0;
        for (char [] binaryNumber : binaryNumbers) {
                total += Character.getNumericValue( binaryNumber[index] );
        }
        return total;
    }
}
