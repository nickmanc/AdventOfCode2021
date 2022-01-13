package com.hotmail.nickcooke.aoc2021;

import java.util.List;

public class Day20 extends AoCSolution {
    public static void main( String[] args ) {
        Day20 day20 = new Day20();
        day20.getInput();
        day20.solve();
    }
    
    private void solve() {
        Image image = new Image( inputLines );
        int litPixels = image.enhance( 2 );
        System.out.println( "Part 1: " + litPixels );
        image = new Image( inputLines );
        litPixels = image.enhance( 50 );
        System.out.println( "Part 2: " + litPixels );
    }
    
}

class Image {
    char[][] imagePixels;
    
    private final String enhanceAlgorithm;
    
    private int timesEnhanced = 0;
    
    public Image( List<String> inputLines ) {
        enhanceAlgorithm = inputLines.get( 0 );
        List<String> initialImageInput = inputLines.subList( 2, inputLines.size() );
        imagePixels = new char[initialImageInput.get( 0 ).length()][initialImageInput.size()];
        for ( int i = 0; i < initialImageInput.size(); i++ ) {
            for ( int j = 0; j < initialImageInput.get( 0 ).length(); j++ ) {
                imagePixels[i][j] = initialImageInput.get( i ).charAt( j );
            }
        }
    }
    
    public void print() {
        for ( char [] row : imagePixels ) {
            for ( char pixel : row ) {
                System.out.print( pixel );
            }
            System.out.println();
        }
        System.out.println( "==========================" );
    }
    
    public int enhance( int iterations ) {
        int result = 0;
        for ( int i = 0; i < iterations; i++ ) {
            result = enhance();
        }
        return result;
    }
    
    private int enhance() {
        timesEnhanced++;
        char[][] enlargedImage = new char[imagePixels.length + 2][imagePixels[0].length + 2];
        int litPixelCount = 0;
        for ( int x = 0; x < enlargedImage.length; x++ ) {
            for ( int y = 0; y < enlargedImage[x].length; y++ ) {
                char enhancedPixel = getEnhancedPixel( x, y, imagePixels );
                enlargedImage[x][y] = enhancedPixel;
                if ( enhancedPixel == '#' ) {
                    litPixelCount++;
                }
            }
        }
        imagePixels = enlargedImage;
        return litPixelCount;
    }
    
    private char getEnhancedPixel( int x, int y, char[][] image ) {
        StringBuilder indexPixels = new StringBuilder();
        for ( int i = x - 1; i <= x + 1; i++ ) {
            for ( int j = y - 1; j <= y + 1; j++ ) {
                if ( i >= 1 && i <= image.length && j >= 1 && j <= image[0].length ) {
                    indexPixels.append( image[i - 1][j - 1]);
                }
                else {
                    if ( enhanceAlgorithm.charAt( 0 ) == '.' ) {
                        indexPixels.append('.');
                    }
                    else if ( enhanceAlgorithm.charAt( 0 ) == '#' && enhanceAlgorithm.charAt( enhanceAlgorithm.length() - 1 ) == '#' ) {
                        indexPixels.append('#');
                    }
                    else {
                        if ( timesEnhanced % 2 == 0 ) {
                            indexPixels.append('#');
                        }
                        else {
                            indexPixels.append('.');
                        }
                    }
                }
            }
        }
        int index = Integer.parseInt( indexPixels.toString().replace( '.', '0' ).replace( '#', '1' ), 2 );
        return enhanceAlgorithm.charAt( index );
    }
}