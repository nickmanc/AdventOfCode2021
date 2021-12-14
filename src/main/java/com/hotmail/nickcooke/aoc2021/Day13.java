package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.List;

public class Day13 extends AoCSolution {
    
    public static final String FOLD_ALONG = "fold along ";
    
    List<Fold> folds;
    private int maxX;
    private int maxY;
    
    public static void main( String[] args ) {
        Day13 day13 = new Day13();
        day13.getInput();
        day13.solve();
    }
    
    private void solve() {
        int[][] transparentPaper = parseInput();
        boolean printedPart1 = false;
        for ( Fold fold : folds ) {
            if ( fold.direction == 'y' ) {
                transparentPaper = foldY( transparentPaper, fold );
            }
            else if ( fold.direction == 'x' ) {
                transparentPaper = foldX( transparentPaper, fold );
            }
            if (!printedPart1){
                System.out.println( "Part 1: " + countDots( transparentPaper ) );
                printedPart1=true;
            }
        }
        printPaper( transparentPaper );
    }
    
    private int[][] foldX( int[][] transparentPaper, Fold fold ) {
        for ( int x = fold.position; x < transparentPaper.length; x++ ) {
            for ( int y = 0; y < transparentPaper[0].length; y++ ) {
                if ( transparentPaper[x][y] == 1 ) {
                    int newX = fold.position - ( x - fold.position );
                    transparentPaper[newX][y] = 1;
                    transparentPaper[x][y] = 0;
                }
            }
        }
        return trimArray( transparentPaper, transparentPaper[0].length, transparentPaper.length - fold.position - 1 );
    }
    
    private int[][] foldY( int[][] transparentPaper, Fold fold ) {
        for ( int x = 0; x < transparentPaper.length; x++ ) {
            for ( int y = fold.position; y < transparentPaper[0].length; y++ ) {
                if ( transparentPaper[x][y] == 1 ) {
                    int newY = fold.position - ( y - fold.position );
                    transparentPaper[x][newY] = 1;
                    transparentPaper[x][y] = 0;
                }
            }
        }
        return trimArray( transparentPaper, transparentPaper[0].length - fold.position - 1, transparentPaper.length );
    }
    
    private int[][] trimArray( int[][] oldArray, int newWidth, int newDepth ) {
        int[][] trimmedArray = new int[newDepth][newWidth];
        for ( int y = 0; y < newWidth; y++ ) {
            for ( int x = 0; x < newDepth; x++ ) {
                trimmedArray[x][y] = oldArray[x][y];
            }
        }
        return trimmedArray;
    }
    
    private int[][] parseInput() {
        for ( String inputLine : inputLines ) {
            if ( inputLine.trim().equals( "" ) ) {
                break;
            }
            maxX = Math.max( maxX, Integer.parseInt( inputLine.split( "," )[0] ) );
            maxY = Math.max( maxY, Integer.parseInt( inputLine.split( "," )[1] ) );
        }
        int[][] transparentPaper = new int[maxX + 1][maxY + 1];
        folds = new ArrayList<>();
        for ( String inputLine : inputLines ) {
            if ( !inputLine.equals( "" ) ) {
                if ( !inputLine.startsWith( FOLD_ALONG ) ) {
                    int x = Integer.parseInt( inputLine.split( "," )[0] );
                    int y = Integer.parseInt( inputLine.split( "," )[1] );
                    transparentPaper[x][y] = 1;
                }
                else {
                    Fold fold = new Fold( inputLine.replace( FOLD_ALONG, "" ).split( "=" )[0].toCharArray()[0], Integer.parseInt( inputLine.replace( FOLD_ALONG, "" ).split( "=" )[1] ) );
                    folds.add( fold );
                }
            }
        }
        return transparentPaper;
    }
    
    private void printPaper( int[][] transparentPaper ) {
        for ( int y = 0; y < transparentPaper[0].length; y++ ) {
            for ( int[] ints : transparentPaper ) {
                System.out.print( ints[y] == 1 ? '@' : ' ' );
            }
            System.out.println();
        }
    }
    
    private int countDots( int[][] transparentPaper ) {
        int dotCount = 0;
        for ( int y = 0; y < transparentPaper[0].length; y++ ) {
            for ( int[] ints : transparentPaper ) {
                dotCount += ints[y];
            }
        }
        return dotCount;
    }
    
    class Fold {
        char direction;
        int position;
        public Fold( char direction, int position ) {
            this.direction = direction;
            this.position = position;
        }
    }
}
