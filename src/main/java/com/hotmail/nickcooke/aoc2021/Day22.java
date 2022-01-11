package com.hotmail.nickcooke.aoc2021;

import java.io.IOException;
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
    
    protected void part1() {
        Set<Cube> onCubes = new HashSet<>();
        Pattern reactorSwitchPattern = Pattern.compile( "(on|off) x=(-?\\d*)\\.\\.(-?\\d*),y=(-?\\d*)\\.\\.(-?\\d*),z=(-?\\d*)\\.\\.(-?\\d*)" );
        for ( String input : inputLines ) {
            Matcher reactorSwitchMatcher = reactorSwitchPattern.matcher( input );
            reactorSwitchMatcher.find();
            //            for ( int x =  Integer.parseInt( reactorSwitchMatcher.group( 2 )  ); x <=  Integer.parseInt( reactorSwitchMatcher.group( 3 ) ) ; x++ ) {
            //                for ( int y =  Integer.parseInt( reactorSwitchMatcher.group( 4 )  ); y <=  Integer.parseInt( reactorSwitchMatcher.group( 5 ) ) ; y++ ) {
            //                    for ( int z =  Integer.parseInt( reactorSwitchMatcher.group( 6 )  ); z <=  Integer.parseInt( reactorSwitchMatcher.group( 7 ) ) ; z++ ) {
            for ( int x = Math.max( -50, Integer.parseInt( reactorSwitchMatcher.group( 2 ) ) ); x <= Math.min( 50, Integer.parseInt( reactorSwitchMatcher.group( 3 ) ) ); x++ ) {
                for ( int y = Math.max( -50, Integer.parseInt( reactorSwitchMatcher.group( 4 ) ) ); y <= Math.min( 50, Integer.parseInt( reactorSwitchMatcher.group( 5 ) ) ); y++ ) {
                    for ( int z = Math.max( -50, Integer.parseInt( reactorSwitchMatcher.group( 6 ) ) ); z <= Math.min( 50, Integer.parseInt( reactorSwitchMatcher.group( 7 ) ) ); z++ ) {
                        Cube cube = new Cube( x, y, z );
                        if ( "on".equals( reactorSwitchMatcher.group( 1 ) ) ) {
                            onCubes.add( cube );
                        }
                        else {
                            onCubes.remove( cube );
                        }
                    }
                }
            }
        }
        System.out.println( "Part 1: " + onCubes.size() );
    }
    
    protected void part2() throws IOException {
        Set<Cuboid> cuboids = new HashSet<>();
        Pattern reactorSwitchPattern = Pattern.compile( "(on|off) x=(-?\\d*)\\.\\.(-?\\d*),y=(-?\\d*)\\.\\.(-?\\d*),z=(-?\\d*)\\.\\.(-?\\d*)" );
        for ( String input : inputLines ) {
//            System.out.println( "Processing new cuboid: " + input );
            Matcher reactorSwitchMatcher = reactorSwitchPattern.matcher( input );
            reactorSwitchMatcher.find();
            Vertex pointFrom = new Vertex( Integer.parseInt( reactorSwitchMatcher.group( 2 ) ), Integer.parseInt( reactorSwitchMatcher.group( 4 ) ), Integer.parseInt( reactorSwitchMatcher.group( 6 ) ) );
            Vertex pointTo = new Vertex( ( Integer.parseInt( reactorSwitchMatcher.group( 3 ) ) +1 ), ( Integer.parseInt( reactorSwitchMatcher.group( 5 ) ) +1), ( Integer.parseInt( reactorSwitchMatcher.group( 7 ) ) +1) );
            Cuboid cuboid = new Cuboid( pointFrom, pointTo );
            
            if (pointFrom.z == 73728){
                System.out.println("here!!!!!!!!!!!!!");
            }
            
            if ( reactorSwitchMatcher.group( 1 ).equals( "on" ) ) {
                cuboid.isOn = true;
            }
            if ( cuboids.isEmpty() ) {
                cuboids.add( cuboid );
            }
            else {
                Set<Cuboid> combinedCuboids = new HashSet<>();
                boolean addedNewCuboid = false;
                for ( Cuboid existingCuboid : cuboids ) {
//                    if ( existingCuboid.overlaps( cuboid ) ) {
                        combinedCuboids.addAll( existingCuboid.combine( cuboid ) );
//                        addedNewCuboid = true;
//                    }
//                    else {
//                        combinedCuboids.add( existingCuboid );
//                    }
                }
                cuboids = combinedCuboids;
            }
        }
        System.out.println( cuboids.size() );
        long initializationOnCubes = 0;
        long totalOnCubes = 0;
        for ( Cuboid cuboid : cuboids ) {
            totalOnCubes += cuboid.volume();
            if (totalOnCubes < 0){
                throw new RuntimeException();
            }
            initializationOnCubes += cuboid.initializationVolume();
        }
//        System.out.println( "initialization on: " + initializationOnCubes );
        System.out.println( "total : " + totalOnCubes );
    
//        System.out.println(Cuboid.maxResult);
//        System.out.println(Cuboid.minResult);
    
        Reactor reactor = new Reactor();
        reactor.reboot();
        
        
        //problem starts at line: 229
        //on x=-17242..-13743,y=19611..29304,z=73728..76397
        
    }
    
    public static void main( String[] args ) throws IOException {
        Day22 day22 = new Day22();
        day22.getInput();
        day22.part1();
        day22.part2();
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
        if (from.x >= to.x || from.y>=to.y||from.z>=to.z){
            throw new RuntimeException("All dimensions must be > 0, was " + from + "-> " + to);
        }
        this.from = from;
        this.to = to;
    }
    static long maxResult=Long.MIN_VALUE;
    static long minResult = Long.MAX_VALUE;
    public long volume() {
        long xLength = to.x - from.x;
        long yLength = to.y - from.y;
        long zLength = to.z - from.z;
        long result =  xLength * yLength * zLength;
        maxResult = Math.max(maxResult, result);
        minResult = Math.min(minResult, result);
        return result;
    }
    
    public long initializationVolume() {
        if (to.x < -50 || from.x > 51 ||to.y < -50 || from.y > 51||to.z < -50 || from.z > 51)
        {
            return 0;
        }
        long xLength =(Math.min( 51, to.x ) - Math.max( -50, from.x ));
        long yLength = (Math.min( 51, to.y ) - Math.max( -50, from.y ));
        long zLength = (Math.min( 51, to.z ) - Math.max( -50, from.z ));
        return xLength * yLength * zLength;
    }
    
    protected boolean overlaps( Cuboid other ) {
        return ( other.contains( this ) ||
                  (( ( other.from.x >= this.from.x && other.from.x < this.to.x ) || ( this.from.x >= other.from.x && this.from.x < other.to.x ) )
                && ( ( other.from.y >= this.from.y && other.from.y < this.to.y ) || ( this.from.y >= other.from.y && this.from.y < other.to.y ) )
                && ( ( other.from.z >= this.from.z && other.from.z < this.to.z ) || ( this.from.z >= other.from.z && this.from.z < other.to.z ) ) ));
    }
    
    public boolean contains( Cuboid other ) {
        return ( other.from.x>= this.from.x && other.from.y >= this.from.y && other.from.z >= this.from.z ) && ( other.to.x <= this.to.x && other.to.y <= this.to.y && other.to.z <= this.to.z );
    }
    
    public Set<Cuboid> combine( Cuboid other ) {
        Set<Cuboid> combinedCuboids = new HashSet<>();
        if (this.contains( other ) && other.isOn){
            combinedCuboids.add(this);
        }
        else if (this.overlaps( other )) {
            List<Integer> xs = new ArrayList<>( new HashSet<>( Arrays.asList( this.from.x, this.to.x , other.from.x, other.to.x ) ) );
            List<Integer> ys = new ArrayList<>( new HashSet<>( Arrays.asList( this.from.y, this.to.y, other.from.y, other.to.y ) ) );
            List<Integer> zs = new ArrayList<>( new HashSet<>( Arrays.asList( this.from.z, this.to.z, other.from.z, other.to.z ) ) );
            Collections.sort( xs );
            Collections.sort( ys );
            Collections.sort( zs );
            Set<Cuboid> subCuboids = new HashSet<>();
            for ( int xIndex = 1; xIndex < xs.size() ; xIndex++ ) {
                for ( int yIndex = 1; yIndex < ys.size() ; yIndex++ ) {
                    for ( int zIndex = 1; zIndex < zs.size() ; zIndex++ ) {
                        Cuboid newCuboid = new Cuboid( new Vertex( xs.get( xIndex -1 ), ys.get( yIndex -1), zs.get( zIndex-1)), new Vertex( xs.get( xIndex  ), ys.get( yIndex   ), zs.get( zIndex   ) ) );
                        subCuboids.add( newCuboid );
                    }
                }
            }
            for ( Cuboid candidate : subCuboids ) {//add all the non-overlapping cubes from the first cube
                if ( this.contains( candidate ) && !other.contains( candidate ) ) {
                    combinedCuboids.add( candidate );
                }
//                else if (other.isOn && other.contains( candidate )){
//                    combinedCuboids.add( candidate );
//
//                }
            }
            if ( other.isOn ) { //then add in the new cube if it's turned on
                combinedCuboids.add( other );
            }
            
        }
        else {
            combinedCuboids.add( this );
            if (other.isOn){
                combinedCuboids.add( other );
            }
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
class Cube {
    
    int x;
    
    int y;
    
    int z;
    
    public Cube( int x, int y, int z ) {
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
        Cube cube = (Cube) o;
        return x == cube.x && y == cube.y && z == cube.z;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash( x, y, z );
    }
    
    @Override
    public String toString() {
        return "Cube{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}