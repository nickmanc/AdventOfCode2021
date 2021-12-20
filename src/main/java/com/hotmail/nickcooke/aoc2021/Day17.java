package com.hotmail.nickcooke.aoc2021;

import java.awt.Point;

public class Day17 extends AoCSolution {
    
    private int xFrom;
    private int xTo;
    private int yFrom;
    private int yTo;
    
    public static void main( String[] args ) {
        Day17 day17 = new Day17();
        day17.getInput();
        day17.part1();
        day17.part2();
    }
    
    protected void part1() {
        int maxY = yFrom;
        for ( int y = yFrom; y < yFrom * -1; y++ ) {
            for ( int x = 0; x <= xTo; x++ ) {
                Probe probe = new Probe( x, y );
                maxY = Math.max( maxY, peakY( probe ) );
            }
        }
        System.out.println("Part 1: " + maxY);
    }
    
    protected void part2() {
        int counter = 0;
        for ( int y = yFrom; y < yFrom * -1; y++ ) {
            for ( int x = 0; x <= xTo; x++ ) {
                Probe probe = new Probe( x, y );
                if (probeHitTarget( probe )) {
                    counter ++;
                }
            }
        }
        System.out.println("Part 2: " + counter);
    }
    
    private int peakY( Probe probe ) {
        int peakY = 0;
        while ( couldStillHitTarget( probe ) ) {
            probe.moveStep();
            peakY = Math.max( peakY, probe.position.y );
            if ( withinTarget( probe ) ) {
                return peakY;
            }
        }
        return -1;
    }
    
    private boolean probeHitTarget(Probe probe){
        while ( couldStillHitTarget( probe ) ) {
            probe.moveStep();
            if ( withinTarget( probe ) ) {
                return true;
            }
        }
        return false;
    }
    
    private boolean withinTarget( Probe probe ) {
        Point p = probe.position;
        return p.x >= xFrom && p.x <= xTo && p.y >= yFrom && p.y <= yTo;
    }
    
    private boolean couldStillHitTarget( Probe probe ) {
        Point p = probe.position;
        return p.x <= xTo && p.y >= yFrom;
    }
    
    @Override
    protected void getInput() {
        super.getInput();
        String input = inputLines.get( 0 );
        xFrom = Integer.parseInt( input.substring( input.indexOf( "x=" ) + 2, input.indexOf( ".." ) ) );
        xTo = Integer.parseInt( input.substring( input.indexOf( ".." ) + 2, input.indexOf( "," ) ) );
        yFrom = Integer.parseInt( input.substring( input.indexOf( "y=" ) + 2, input.indexOf( "..", input.indexOf( "y=" ) ) ) );
        yTo = Integer.parseInt( input.substring( input.indexOf( "..", input.indexOf( "y=" ) ) + 2 ) );
    }
}

class Probe {
    Point position = new Point( 0, 0 );
    int xVelocity;
    int yVelocity;
    
    public Probe( int xVelocity, int yVelocity ) {
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }
    
    public void moveStep() {
        position.x += xVelocity;
        position.y += yVelocity;
        if ( xVelocity > 0 ) {
            xVelocity--;
        }
        else if (xVelocity < 0) {
            xVelocity++;
        }
        yVelocity--;
    }
}