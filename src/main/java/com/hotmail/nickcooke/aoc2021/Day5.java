package com.hotmail.nickcooke.aoc2021;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Day5 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day5 day5 = new Day5();
        day5.getInput();
        day5.part1();
        day5.part2();
    }
    
    private void part1() {
        Set<Point> ventPositions = new HashSet<>();
        Set<Point> overlapPoints = new HashSet<>();
        for (String inputLine : inputLines){
            Point startPoint = new Point(Integer.parseInt( inputLine.split(" -> ")[0].split( "," )[0]),Integer.parseInt( inputLine.split(" -> ")[0].split( "," )[1]));
            Point endPoint = new Point(Integer.parseInt( inputLine.split(" -> ")[1].split( "," )[0]),Integer.parseInt( inputLine.split(" -> ")[1].split( "," )[1]));
            if (startPoint.x == endPoint.x ){
                if (startPoint.y < endPoint.y){
                    for (int i = startPoint.y; i <= endPoint.y; i++){
                        Point positionInVent =  new Point(startPoint.x,i );
                        if (ventPositions.contains( positionInVent ) )
                        {
                            overlapPoints.add( positionInVent );
                        }
                        ventPositions.add( positionInVent );
                    }
                }
                else {
                    for (int i = startPoint.y; i >= endPoint.y; i--){
                        Point positionInVent =  new Point(startPoint.x,i);
                        if (ventPositions.contains( positionInVent ) )
                        {
                            overlapPoints.add( positionInVent );
                        }
                        ventPositions.add( positionInVent );
                    }
                }
            }
            else if (startPoint.y == endPoint.y ) {
                if ( startPoint.x < endPoint.x ) {
                    for ( int i = startPoint.x; i <= endPoint.x; i++ ) {
                        Point positionInVent = new Point( i, startPoint.y );
                        if ( ventPositions.contains( positionInVent ) ) {
                            overlapPoints.add( positionInVent );
                        }
                        ventPositions.add( positionInVent );
                    }
                }
                else {
                    for ( int i = startPoint.x; i >= endPoint.x; i-- ) {
                        Point positionInVent = new Point( i, startPoint.y );
                        if ( ventPositions.contains( positionInVent ) ) {
                            overlapPoints.add( positionInVent );
                        }
                        ventPositions.add( positionInVent );
                    }
                }
            }
        }
        System.out.println("Part 1: " + overlapPoints.size());
    }
    
    private void part2() {
        Set<Point> ventPositions = new HashSet<>();
        Set<Point> overlapPoints = new HashSet<>();
        for (String inputLine : inputLines) {
            Point startPoint = new Point( Integer.parseInt( inputLine.split( " -> " )[0].split( "," )[0] ), Integer.parseInt( inputLine.split( " -> " )[0].split( "," )[1] ) );
            Point endPoint = new Point( Integer.parseInt( inputLine.split( " -> " )[1].split( "," )[0] ), Integer.parseInt( inputLine.split( " -> " )[1].split( "," )[1] ) );
            int xSignum = Integer.signum( startPoint.x - endPoint.x );
            int ySignum = Integer.signum( startPoint.y - endPoint.y );
            int lineLength = Math.max(Math.abs(startPoint.x - endPoint.x),Math.abs(startPoint.y - endPoint.y));
            for (int i = 0; i <= lineLength; i++) {
                Point positionInVent = new Point( startPoint.x - (i * xSignum), startPoint.y - (i * ySignum));
                if ( ventPositions.contains( positionInVent ) ) {
                    overlapPoints.add( positionInVent );
                }
                ventPositions.add( positionInVent );
            }
        }
        System.out.println("Part 2: " + overlapPoints.size());
    }
}
