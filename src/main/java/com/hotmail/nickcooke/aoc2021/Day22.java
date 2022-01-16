package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22 extends AoCSolution {
    
    protected void solve() {
        Set<Cuboid> cuboids = new HashSet<>();
        Pattern reactorSwitchPattern = Pattern.compile( "(on|off) x=(-?\\d*)\\.\\.(-?\\d*),y=(-?\\d*)\\.\\.(-?\\d*),z=(-?\\d*)\\.\\.(-?\\d*)" );
        for ( String input : inputLines ) {
            Matcher reactorSwitchMatcher = reactorSwitchPattern.matcher( input );
            reactorSwitchMatcher.find();
            Vertex pointFrom = new Vertex( Integer.parseInt( reactorSwitchMatcher.group( 2 ) ), Integer.parseInt( reactorSwitchMatcher.group( 4 ) ), Integer.parseInt( reactorSwitchMatcher.group( 6 ) ) );
            Vertex pointTo = new Vertex( ( Integer.parseInt( reactorSwitchMatcher.group( 3 ) ) + 1 ), ( Integer.parseInt( reactorSwitchMatcher.group( 5 ) ) + 1 ), ( Integer.parseInt( reactorSwitchMatcher.group( 7 ) ) + 1 ) );
            Cuboid cuboid = new Cuboid( pointFrom, pointTo );
            
            if ( reactorSwitchMatcher.group( 1 ).equals( "on" ) ) {
                cuboid.isOn = true;
            }
            if ( cuboids.isEmpty() ) {
                cuboids.add( cuboid );
            }
            else {
                Set<Cuboid> combinedCuboids = new HashSet<>();
                for ( Cuboid existingCuboid : cuboids ) {
                    combinedCuboids.addAll( existingCuboid.combine( cuboid ) );
                }
                cuboids = combinedCuboids;
            }
        }
        long initializationOnCubes = 0;
        long totalOnCubes = 0;
        for ( Cuboid cuboid : cuboids ) {
            totalOnCubes += cuboid.volume();
            initializationOnCubes += cuboid.initializationVolume();
        }
        System.out.println( "Part 1 : " + initializationOnCubes );
        System.out.println( "Part 2 : " + totalOnCubes );
    }
    
    public static void main( String[] args ) {
        Day22 day22 = new Day22();
        day22.getInput();
        day22.solve();
    }
}

class Cuboid {
    
    Vertex from;
    
    Vertex to;
    
    boolean isOn;
    
    public String toString() {
        return "Volume: " + volume();
    }
    
    public Cuboid( Vertex from, Vertex to ) {
        this.from = from;
        this.to = to;
    }
    
    public long volume() {
        long xLength = to.x - from.x;
        long yLength = to.y - from.y;
        long zLength = to.z - from.z;
        return xLength * yLength * zLength;
    }
    
    public long initializationVolume() {
        if ( to.x < -50 || from.x > 51 || to.y < -50 || from.y > 51 || to.z < -50 || from.z > 51 ) {
            return 0;
        }
        long xLength = ( Math.min( 51, to.x ) - Math.max( -50, from.x ) );
        long yLength = ( Math.min( 51, to.y ) - Math.max( -50, from.y ) );
        long zLength = ( Math.min( 51, to.z ) - Math.max( -50, from.z ) );
        return xLength * yLength * zLength;
    }
    
    protected boolean overlaps( Cuboid other ) {
        return ( other.contains( this ) || ( ( ( other.from.x >= this.from.x && other.from.x < this.to.x ) || ( this.from.x >= other.from.x && this.from.x < other.to.x ) ) && ( ( other.from.y >= this.from.y && other.from.y < this.to.y ) || ( this.from.y >= other.from.y && this.from.y < other.to.y ) ) && ( ( other.from.z >= this.from.z && other.from.z < this.to.z ) || ( this.from.z >= other.from.z && this.from.z < other.to.z ) ) ) );
    }
    
    public boolean contains( Cuboid other ) {
        return ( other.from.x >= this.from.x && other.from.y >= this.from.y && other.from.z >= this.from.z ) && ( other.to.x <= this.to.x && other.to.y <= this.to.y && other.to.z <= this.to.z );
    }
    
    public Set<Cuboid> combine( Cuboid other ) {
        Set<Cuboid> combinedCuboids = new HashSet<>();
        if ( this.overlaps( other ) ) {
            List<Integer> xs = new ArrayList<>( new HashSet<>( Arrays.asList( this.from.x, this.to.x, other.from.x, other.to.x ) ) );
            List<Integer> ys = new ArrayList<>( new HashSet<>( Arrays.asList( this.from.y, this.to.y, other.from.y, other.to.y ) ) );
            List<Integer> zs = new ArrayList<>( new HashSet<>( Arrays.asList( this.from.z, this.to.z, other.from.z, other.to.z ) ) );
            Collections.sort( xs );
            Collections.sort( ys );
            Collections.sort( zs );
            Set<Cuboid> subCuboids = new HashSet<>();
            for ( int xIndex = 1; xIndex < xs.size(); xIndex++ ) {
                for ( int yIndex = 1; yIndex < ys.size(); yIndex++ ) {
                    for ( int zIndex = 1; zIndex < zs.size(); zIndex++ ) {
                        Cuboid newCuboid = new Cuboid( new Vertex( xs.get( xIndex - 1 ), ys.get( yIndex - 1 ), zs.get( zIndex - 1 ) ), new Vertex( xs.get( xIndex ), ys.get( yIndex ), zs.get( zIndex ) ) );
                        subCuboids.add( newCuboid );
                    }
                }
            }
            for ( Cuboid candidate : subCuboids ) {//add all the non-overlapping cubes from the first cube
                if ( this.contains( candidate ) && !other.contains( candidate ) ) {
                    combinedCuboids.add( candidate );
                }
            }
        }
        else {
            combinedCuboids.add( this );
        }
    
        if ( other.isOn ) {
            combinedCuboids.add( other );
        }
        return combinedCuboids;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Cuboid cuboid = (Cuboid) o;
        return from.equals( cuboid.from ) && to.equals( cuboid.to );
    }
    
    @Override
    public int hashCode() {
        return Objects.hash( from, to );
    }
}

class Vertex {
    
    int x;
    
    int y;
    
    int z;
    
    public Vertex( int x, int y, int z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Vertex cube = (Vertex) o;
        return x == cube.x && y == cube.y && z == cube.z;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash( x, y, z );
    }
    
    @Override
    public String toString() {
        return "Vertex{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}