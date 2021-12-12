package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day11 extends AoCSolution {
    
    public static final int MAX_ENERGY_LEVEL = 9;
    
    public static void main( String[] args ) {
        Day11 day11 = new Day11();
        day11.getInput();
        day11.solve();
    }
    
    private void solve() {
        Grid octopusGrid = new Grid( inputLines );
        boolean allFlashed=false;
        while ( !allFlashed || octopusGrid.currentStepCount<=100 ) {
            octopusGrid.runStep();
            if (octopusGrid.currentStepCount==100) {
                System.out.println( "Part 1: " + octopusGrid.getFlashCount() );
            }
            if (!allFlashed && octopusGrid.allJustFlashed()){
                allFlashed=true;
                System.out.println( "Part 2: " + octopusGrid.getCurrentStepCount() );
            }
        }
    }
    
    interface FlashCounter {
        void registerFlash();
        int getFlashCount();
    }
    
    class Grid implements Iterable<Octopus>, FlashCounter {
        private final int gridWidth;
        
        private final int gridDepth;
        
        List<Octopus> octopuses;
        
        int currentOctopusIndex;
        
        int currentFlashCount = 0;
        int currentStepCount = 0;
        
        public Grid( List<String> inputLines ) {
            octopuses = new ArrayList<>();
            gridWidth = inputLines.get( 0 ).length();
            gridDepth = inputLines.size();
            for ( int y = 0; y < gridDepth; y++ ) {
                for ( int x = 0; x < gridWidth; x++ ) {
                    octopuses.add( new Octopus( x, y, Integer.parseInt( inputLines.get( y ).charAt( x ) + "" ), this ) );
                }
            }
            for ( Octopus octopus : octopuses ) {
                octopus.setNeighbours( getAdjacentOctopuses( octopus ) );
            }
            currentOctopusIndex = 0;
        }
        
        public Set<Octopus> getAdjacentOctopuses( Octopus octopus ) {
            Set<Octopus> adjacentLocations = new HashSet<>();
            addIfExists( octopusAboveLeft( octopus ), adjacentLocations );
            addIfExists( octopusAbove( octopus ), adjacentLocations );
            addIfExists( octopusAboveRight( octopus ), adjacentLocations );
            addIfExists( octopusLeft( octopus ), adjacentLocations );
            addIfExists( octopusRight( octopus ), adjacentLocations );
            addIfExists( octopusBelowLeft( octopus ), adjacentLocations );
            addIfExists( octopusBelow( octopus ), adjacentLocations );
            addIfExists( octopusBelowRight( octopus ), adjacentLocations );
            return adjacentLocations;
        }
        
        private void addIfExists( Octopus octopus, Set<Octopus> octopuses ) {
            if ( null != octopus ) {
                octopuses.add( octopus );
            }
        }
        
        private Octopus octopusAboveLeft( Octopus octopus ) {
            if ( octopus.y > 0 && octopus.x > 0 ) {
                return octopuses.get( ( ( octopus.y - 1 ) * gridWidth ) + octopus.x - 1 );
            }
            return null;
        }
        
        private Octopus octopusAbove( Octopus octopus ) {
            if ( octopus.y > 0 ) {
                return octopuses.get( ( ( octopus.y - 1 ) * gridWidth ) + octopus.x );
            }
            return null;
        }
        
        private Octopus octopusAboveRight( Octopus octopus ) {
            if ( octopus.y > 0 && octopus.x < gridWidth - 1 ) {
                return octopuses.get( ( ( octopus.y - 1 ) * gridWidth ) + octopus.x + 1 );
            }
            return null;
        }
        
        private Octopus octopusLeft( Octopus octopus ) {
            if ( octopus.x > 0 ) {
                return octopuses.get( ( ( octopus.y ) * gridWidth ) + octopus.x - 1 );
            }
            return null;
        }
        
        private Octopus octopusRight( Octopus octopus ) {
            if ( octopus.x < gridWidth - 1 ) {
                return octopuses.get( ( ( octopus.y ) * gridWidth ) + octopus.x + 1 );
            }
            return null;
        }
        
        private Octopus octopusBelowLeft( Octopus octopus ) {
            if ( octopus.y < gridDepth - 1 && octopus.x > 0 ) {
                return octopuses.get( ( ( octopus.y + 1 ) * gridWidth ) + octopus.x - 1 );
            }
            return null;
        }
        
        private Octopus octopusBelow( Octopus octopus ) {
            if ( octopus.y < gridDepth - 1 ) {
                return octopuses.get( ( ( octopus.y + 1 ) * gridWidth ) + octopus.x );
            }
            return null;
        }
        
        private Octopus octopusBelowRight( Octopus octopus ) {
            if ( octopus.y < gridDepth - 1 && octopus.x < gridWidth - 1 ) {
                return octopuses.get( ( ( octopus.y + 1 ) * gridWidth ) + octopus.x + 1 );
            }
            
            return null;
        }
        
        @Override
        public Iterator<Octopus> iterator() {
            currentOctopusIndex = 0;
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return currentOctopusIndex < octopuses.size();
                }
                
                @Override
                public Octopus next() {
                    return ( octopuses.get( currentOctopusIndex++ ) );
                }
            };
        }
        
        void printGrid() {
            for ( int i = 0; i < octopuses.size(); i++ ) {
                Octopus octopus = octopuses.get( i );
                System.out.print( octopus.energy );
                if ( i > 0 && ( i + 1 ) % gridWidth == 0 ) {
                    System.out.println();
                }
            }
            System.out.println();
        }
        
        @Override
        public void registerFlash() {
            currentFlashCount++;
        }
        
        @Override
        public int getFlashCount() {
            return currentFlashCount;
        }
        
        public boolean allJustFlashed() {
            for ( Octopus octopus : octopuses ) {
                if ( octopus.energy != 0 ) {
                    return false;
                }
            }
            return true;
        }
        
        public void runStep() {
            for ( Octopus octopus : octopuses ) {
                octopus.incrementEnergyLevel();
            }
            for ( Octopus octopus : octopuses ) {
                octopus.resetFlashed();
            }
            currentStepCount++;
        }
    
        public int getCurrentStepCount() {
            return currentStepCount;
        }
    }
    
    class Octopus {
        int x;
        
        int y;
        
        int energy;
        
        FlashCounter flashCounter;
        
        public void setNeighbours( Set<Octopus> neighbours ) {
            this.neighbours = neighbours;
        }
        
        Set<Octopus> neighbours = new HashSet<>();
        
        public Octopus( int x, int y, int energy, FlashCounter flashCounter ) {
            this.x = x;
            this.y = y;
            this.energy = energy;
            this.flashCounter = flashCounter;
        }
        
        public void resetFlashed() {
            this.hasFlashed = false;
        }
        
        private boolean hasFlashed = false;
        
        public void incrementEnergyLevel() {
            if ( !hasFlashed && ++energy > MAX_ENERGY_LEVEL ) {
                flash();
            }
        }
        
        private void flash() {
            hasFlashed = true;
            energy = 0;
            flashCounter.registerFlash();
            for ( Octopus neighbour : neighbours ) {
                neighbour.incrementEnergyLevel();
            }
        }
        
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            Octopus octopus = (Octopus) o;
            return x == octopus.x && y == octopus.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash( x, y );
        }
    }
}