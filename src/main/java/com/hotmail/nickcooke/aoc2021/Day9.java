package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day9 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day9 day9 = new Day9();
        day9.getInput();
        day9.part1();
        day9.part2();
    }
    
    private void part1() {
        int riskLevel = 0;
        Grid heightmap = new Grid( inputLines );
        for ( Location location : heightmap ) {
            if ( heightmap.isLowPoint( location ) ) {
                riskLevel += location.height + 1;
            }
        }
        System.out.println( "Part 1: " + riskLevel );
    }
    
    private void part2() {
        Grid heightmap = new Grid( inputLines );
        Iterator<Location> heightmapIterator = heightmap.iterator();
        Set<Location> checkedLocations = new HashSet<>();
        List<Integer> basinSizes = new ArrayList<>();
        while ( heightmapIterator.hasNext() ) {
            Location currentLocation = heightmapIterator.next();
            if ( !checkedLocations.contains( currentLocation ) && currentLocation.height < 9 ) {
                Set<Location> basin = new HashSet<>();
                calculateBasin( currentLocation, heightmap, basin );
                checkedLocations.addAll( basin );
                basinSizes.add( basin.size() );
            }
        }
        basinSizes.sort( Collections.reverseOrder() );
        System.out.println( "Part 2: " + basinSizes.get( 0 ) * basinSizes.get( 1 ) * basinSizes.get( 2 ) );
    }
    
    private void calculateBasin( Location currentLocation, Grid heightmap, Set<Location> basin ) {
        for ( Location adjacentLocation : heightmap.getAdjacentLocations( currentLocation ) ) {
            if ( adjacentLocation.height < 9 && !basin.contains( adjacentLocation ) ) {
                basin.add( adjacentLocation );
                calculateBasin( adjacentLocation, heightmap, basin );
            }
        }
    }
}

class Grid implements Iterable<Location> {
    private final int gridWidth;
    
    private final int gridDepth;
    
    List<Location> gridLocations;
    
    int currentLocationIndex;
    
    public Grid( List<String> inputLines ) {
        gridLocations = new ArrayList<>();
        gridWidth = inputLines.get( 0 ).length();
        gridDepth = inputLines.size();
        for ( int y = 0; y < gridDepth; y++ ) {
            for ( int x = 0; x < gridWidth; x++ ) {
                gridLocations.add( new Location( x, y, Integer.parseInt( inputLines.get( y ).charAt( x ) + "" ) ) );
            }
        }
        currentLocationIndex = 0;
    }
    
    public boolean isLowPoint( Location location ) {
        for ( Location adjacentLocation : getAdjacentLocations( location ) ) {
            if ( adjacentLocation.height <= location.height ) {
                return false;
            }
        }
        return true;
    }
    
    Location locationAbove( Location location ) {
        if ( location.y > 0 ) {
            return gridLocations.get( ( ( location.y - 1 ) * gridWidth ) + location.x );
        }
        return null;
    }
    
    Location locationBelow( Location location ) {
        if ( location.y < gridDepth - 1 ) {
            return gridLocations.get( ( ( location.y + 1 ) * gridWidth ) + location.x );
        }
        return null;
    }
    
    Location locationLeft( Location location ) {
        if ( location.x > 0 ) {
            return gridLocations.get( ( ( location.y ) * gridWidth ) + location.x - 1 );
        }
        return null;
    }
    
    Location locationRight( Location location ) {
        if ( location.x < gridWidth - 1 ) {
            return gridLocations.get( ( ( location.y ) * gridWidth ) + location.x + 1 );
        }
        return null;
    }
    
    List<Location> getAdjacentLocations( Location location ) {
        List<Location> adjacentLocations = new ArrayList<>();
        if ( null != locationAbove( location ) ) {
            adjacentLocations.add( locationAbove( location ) );
        }
        if ( null != locationBelow( location ) ) {
            adjacentLocations.add( locationBelow( location ) );
        }
        if ( null != locationLeft( location ) ) {
            adjacentLocations.add( locationLeft( location ) );
        }
        if ( null != locationRight( location ) ) {
            adjacentLocations.add( locationRight( location ) );
        }
        return adjacentLocations;
    }
    
    @Override
    public Iterator<Location> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return currentLocationIndex < gridLocations.size();
            }
            
            @Override
            public Location next() {
                return ( gridLocations.get( currentLocationIndex++ ) );
            }
        };
    }
    
    private void printGrid() {
        for ( int i = 0; i < gridLocations.size(); i++ ) {
            Location location = gridLocations.get( i );
            System.out.print( location.height );
            if ( i > 0 && ( i + 1 ) % gridWidth == 0 ) {
                System.out.println();
            }
        }
    }
}

class Location {
    int x, y, height;
    
    public Location( int x, int y, int height ) {
        this.x = x;
        this.y = y;
        this.height = height;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash( x, y );
    }
}
