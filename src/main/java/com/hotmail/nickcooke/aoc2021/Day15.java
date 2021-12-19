package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Day15 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day15 day15 = new Day15();
        day15.getInput();
        day15.part1();
        day15.part2();
    }
    
    private void part1() {
        long startTime = System.nanoTime();
        System.out.println( "Part 1: " + solve() + ", in " + TimeUnit.MILLISECONDS.convert(System.nanoTime()-startTime, TimeUnit.NANOSECONDS) + " ms");
    }
    
    private void part2() {
        long startTime = System.nanoTime();
        System.out.println( "Part 2: " + solve(5) + ", in " + TimeUnit.MILLISECONDS.convert(System.nanoTime()-startTime, TimeUnit.NANOSECONDS) + " ms");
    }
    
    private int solve() {
        return solve(1);
    }
    
    private int solve(int gridScaleFactor) {
        Grid grid = new Grid( inputLines, gridScaleFactor );
        Position startPosition = grid.topLeft();
        Position endPosition = grid.bottomRight();
        startPosition.lowestTotalRiskLevel = 0;
        Queue<Position> uncheckedPositions = new PriorityQueue<>();
        uncheckedPositions.add( startPosition );
        while ( !uncheckedPositions.isEmpty()) {
            calculateRoute( uncheckedPositions.poll(), uncheckedPositions );
        }
        return endPosition.lowestTotalRiskLevel;
    }
    
    private void calculateRoute( Position position, Queue<Position> uncheckedPositions ) {
        for ( Position neighbour : position.neighbours ) {
            if ( neighbour.riskLevel + position.lowestTotalRiskLevel < neighbour.lowestTotalRiskLevel ) {
                neighbour.lowestTotalRiskLevel = position.lowestTotalRiskLevel + neighbour.riskLevel;
                uncheckedPositions.remove(neighbour);//have to remove and add to get in the correct position
                uncheckedPositions.add( neighbour );
            }
        }
    }
    
    class Grid implements Iterable<Position> {
        private final int gridWidth;
        
        private final int gridDepth;
        
        List<Position> positions;
        
        int currentPositionIndex;
        
        int[][] grid;
        
        public Grid( List<String> inputLines, int scaleFactor ) {
            positions = new ArrayList<>();
            int inputWidth = inputLines.get( 0 ).length();
            int inputDepth = inputLines.size();
            gridWidth = inputWidth * scaleFactor;
            gridDepth = inputDepth * scaleFactor;
            grid = new int[gridWidth][gridDepth];
                for ( int xScale = 0; xScale < scaleFactor; xScale++ ) {
                    for ( int gridY = 0; gridY < inputDepth; gridY++ ) {
                        for ( int yScale = 0; yScale < scaleFactor; yScale++ ) {
                            for ( int gridX = 0; gridX < inputWidth; gridX++ ) {
                            positions.add( new Position( gridX + ( inputWidth * xScale ),gridY + ( inputDepth * yScale ), applyScale( Integer.parseInt( inputLines.get( gridY ).charAt( gridX ) + "" ), xScale + yScale ) ) );
                         }
                    }
                }
            }
            for ( Position position : positions ) {
                position.setNeighbours( getAdjacentPositions( position ) );
            }
            currentPositionIndex = 0;
        }
        
        private int applyScale( int riskFactor, int scaleFactor ) {
            int newRiskFactor =  riskFactor +  scaleFactor;
            if (newRiskFactor >= 10) {
                newRiskFactor++;
            }
            return newRiskFactor%10;
        }
        
        public Set<Position> getAdjacentPositions( Position position ) {
            Set<Position> adjacentLocations = new HashSet<>();
            addIfExists( positionAbove( position ), adjacentLocations );
            addIfExists( positionLeft( position ), adjacentLocations );
            addIfExists( positionRight( position ), adjacentLocations );
            addIfExists( positionBelow( position ), adjacentLocations );
            return adjacentLocations;
        }
        
        private void addIfExists( Position position, Set<Position> positions ) {
            if ( null != position ) {
                positions.add( position );
            }
        }
        
        private Position positionAbove( Position position ) {
            if ( position.y > 0 ) {
                return positions.get( ( ( position.y - 1 ) * gridWidth ) + position.x );
            }
            return null;
        }
        
        private Position positionLeft( Position position ) {
            if ( position.x > 0 ) {
                return positions.get( ( ( position.y ) * gridWidth ) + position.x - 1 );
            }
            return null;
        }
        
        private Position positionRight( Position position ) {
            if ( position.x < gridWidth - 1 ) {
                return positions.get( ( ( position.y ) * gridWidth ) + position.x + 1 );
            }
            return null;
        }
        
        private Position positionBelow( Position position ) {
            if ( position.y < gridDepth - 1 ) {
                return positions.get( ( ( position.y + 1 ) * gridWidth ) + position.x );
            }
            return null;
        }
        
        @Override
        public Iterator<Position> iterator() {
            currentPositionIndex = 0;
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return currentPositionIndex < positions.size();
                }
                
                @Override
                public Position next() {
                    return ( positions.get( currentPositionIndex++ ) );
                }
            };
        }
        
        void printGrid() {
            for ( int i = 0; i < positions.size(); i++ ) {
                Position position = positions.get( i );
                System.out.print( position.riskLevel );
                if ( i > 0 && ( i + 1 ) % gridWidth == 0 ) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    
        public Position topLeft() {
            return positions.get( 0 );
        }
        
        public Position bottomRight() {
            return positions.get( positions.size() - 1 );
        }
    }
    
    class Position implements Comparable {
        int x;
        
        int y;
        
        int riskLevel;
        
        int lowestTotalRiskLevel = Integer.MAX_VALUE;
        
        Set<Position> neighbours;
        
        public Position( int x, int y, int riskLevel ) {
            this.x = x;
            this.y = y;
            this.riskLevel = riskLevel;
            neighbours = new HashSet<>();
        }
        
        @Override
        public String toString() {
            return "Position{" + "x=" + x + ", y=" + y + ", riskLevel=" + riskLevel + '}';
        }
        
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash( x, y );
        }
        
        public void setNeighbours( Set<Position> adjacentPositions ) {
            neighbours = adjacentPositions;
        }
    
        @Override
        public int compareTo( Object o ) {
            Position other = (Position) o;
            return  this.lowestTotalRiskLevel - other.lowestTotalRiskLevel ;
        }
    }
}