package com.hotmail.nickcooke.aoc2021;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Day25 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day25 day25 = new Day25();
        day25.getInput();
        day25.part1();
    }
    
    private void part1() {
        char[][] grid = getStartingGrid();
        int iterations = 1;
        while ( eastHerdMove( grid ) | southHerdMove( grid ) ) {
            iterations++;
        }
        System.out.println( "Part 1: " + iterations );
    }
    
    private char[][] getStartingGrid() {
        char[][] grid = new char[inputLines.get( 0 ).length()][inputLines.size()];
        for ( int y = 0; y < inputLines.size(); y++ ) {
            for ( int x = 0; x < inputLines.get( y ).length(); x++ ) {
                grid[x][y] = inputLines.get( y ).charAt( x );
            }
        }
        return grid;
    }
    
    private boolean eastHerdMove( char[][] grid ) {
        Set<Point> newCucumbers = new HashSet<>();
        Set<Point> newDots = new HashSet<>();
        for ( int y = 0; y < inputLines.size(); y++ ) {
            for ( int x = 0; x < inputLines.get( y ).length(); x++ ) {
                int nextX = ( x + 1 ) % grid.length;
                if ( grid[x][y] == '>' && grid[nextX][y] == '.' ) {
                    newDots.add( new Point( x, y ) );
                    newCucumbers.add( new Point( nextX, y ) );
                    x++;
                }
            }
        }
        for ( Point point : newCucumbers ) {
            grid[point.x][point.y] = '>';
        }
        for ( Point point : newDots ) {
            grid[point.x][point.y] = '.';
        }
        return !newCucumbers.isEmpty();
    }
    
    private boolean southHerdMove( char[][] grid ) {
        Set<Point> newCucumbers = new HashSet<>();
        Set<Point> newDots = new HashSet<>();
        for ( int x = 0; x < inputLines.get( 0 ).length(); x++ ) {
            for ( int y = 0; y < inputLines.size(); y++ ) {
                int nextY = ( y + 1 ) % grid[x].length;
                if ( grid[x][y] == 'v' && grid[x][nextY] == '.' ) {
                    newDots.add( new Point( x, y ) );
                    newCucumbers.add( new Point( x, nextY ) );
                    y++;
                }
            }
        }
        for ( Point point : newCucumbers ) {
            grid[point.x][point.y] = 'v';
        }
        for ( Point point : newDots ) {
            grid[point.x][point.y] = '.';
        }
        return !newCucumbers.isEmpty();
    }
    
    private void printGrid( char[][] grid ) {
        for ( int y = 0; y < grid[0].length; y++ ) {
            for ( int x = 0; x < grid.length; x++ ) {
                System.out.print( grid[x][y] );
            }
            System.out.println();
        }
        System.out.println( "====================================================" );
    }
}
