package com.hotmail.nickcooke.aoc2021;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day23 extends AoCSolution {
    
    private int minEnergyToComplete = Integer.MAX_VALUE;
    
    private final Map<AmphipodBurrow, Integer> completedBurrowCache = new HashMap<>();
    
    public static void main( String[] args ) {
        Day23 day23 = new Day23();
        day23.getInput();
        day23.part1();
        day23.part2();
    }
    
    private void part1() {
        AmphipodBurrow burrow = getStartingPosition();
        int currentEnergy = 0;
        minEnergyToComplete = Integer.MAX_VALUE;
        organiseBurrow( burrow, currentEnergy );
        System.out.println( "Part 1: " + minEnergyToComplete );
    }
    
    private void part2() {
        AmphipodBurrow burrow = getStartingPosition( true );
        int energySoFar = 0;
        minEnergyToComplete = Integer.MAX_VALUE;
        organiseBurrow( burrow, energySoFar );
        System.out.println( "Part 2: " + minEnergyToComplete );
    }
    
    public void organiseBurrow( AmphipodBurrow burrow, int energySoFar ) {
        if ( isWorthContinuing( burrow, energySoFar ) ) {
            for ( Map.Entry<Amphipod, Set<Space>> move : burrow.getAllPossibleMoves().entrySet() ) {
                Amphipod amphipod = move.getKey();
                for ( Space space : move.getValue() ) {
                    AmphipodBurrow burrowClone = new AmphipodBurrow( burrow );
                    Space spaceClone = burrowClone.getSpace( space.y, space.x );
                    Amphipod amphipodClone = burrowClone.getSpace( amphipod.currentSpace.y, amphipod.currentSpace.x ).amphipod;
                    int nextMoveEnergy = energySoFar;
//                    burrowClone.print();
                    nextMoveEnergy += burrowClone.moveAmphipodToSpace( amphipodClone, spaceClone );
                    if ( nextMoveEnergy < minEnergyToComplete ) {
                        if ( burrowClone.gameCompleted() ) {
                            minEnergyToComplete = nextMoveEnergy;
                            continue;
                        }
                        organiseBurrow( burrowClone, nextMoveEnergy );
                    }
                }
            }
            completedBurrowCache.put( burrow, energySoFar );
        }
    }
    
    private boolean isWorthContinuing( AmphipodBurrow burrow, int energySoFar ) {
        return energySoFar < minEnergyToComplete && ( !completedBurrowCache.containsKey( burrow ) || energySoFar < completedBurrowCache.get( burrow ) );
    }
    
    private AmphipodBurrow getStartingPosition() {
        return getStartingPosition( false );
    }
    
    private AmphipodBurrow getStartingPosition( boolean unfoldPaper ) {
        StringBuilder sb = new StringBuilder();
        sb.append( inputLines.get( 1 ).replace( ".", "" ).replace( "#", "" ).replace( " ", "" ) );
        sb.append( inputLines.get( 2 ).replace( ".", "" ).replace( "#", "" ).replace( " ", "" ) );
        if ( unfoldPaper ) {
            sb.append( "DCBA" );
            sb.append( "DBAC" );
        }
        sb.append( inputLines.get( 3 ).replace( ".", "" ).replace( "#", "" ).replace( " ", "" ) );
        return new AmphipodBurrow( sb.toString() );
    }
}

class Amphipod {
    char type;
    char energy;
    Space currentSpace;
    
    public Amphipod( Amphipod amphipod ) {
        this.type = amphipod.type;
        this.energy = amphipod.energy;
    }
    
    public Amphipod( char type ) {
        this.type = type;
        if ( type == 'A' ) {
            energy = 1;
        }
        else if ( type == 'B' ) {
            energy = 10;
        }
        else if ( type == 'C' ) {
            energy = 100;
        }
        else if ( type == 'D' ) {
            energy = 1000;
        }
    }
    
    @Override
    public String toString() {
        return type + "";
    }
}

class Space {
    Amphipod amphipod;
    
    char sideRoomType;
    
    int x, y;
    
    public Space( Space space ) {
        this.sideRoomType = space.sideRoomType;
        this.x = space.x;
        this.y = space.y;
        if ( null != space.amphipod ) {
            this.amphipod = new Amphipod( space.amphipod );
            this.amphipod.currentSpace = this;
        }
    }
    
    public Space( int y, int x ) {
        this.y = y;
        this.x = x;
    }
    
    public boolean isInSideRoom() {
        return sideRoomType != '\u0000';
    }
    
    public boolean isOccupied() {
        return null != amphipod;
    }
    
    public boolean isHallway() {
        return !isInSideRoom();
    }
    
    public boolean isStoppingSpace() {
        return isInSideRoom() || ( x != 2 && x != 4 && x != 6 && x != 8 );
    }
    
    public String toStringCoordinates() {
        return "[" + y + "],[" + x + "]";
    }
    
    @Override
    public String toString() {
        String stringRepresentation = "";
        if ( null == amphipod ) {
            stringRepresentation += ".";
        }
        else {
            stringRepresentation += amphipod;
        }
        return stringRepresentation;
    }
}

class AmphipodBurrow {
    Space[][] burrow;
    Set<Amphipod> amphipods = new HashSet<>();
    final String NEWLINE = "\r\n";
    
    public AmphipodBurrow( String input ) {
        int sideRoomLength = input.length() / 4;
        burrow = new Space[sideRoomLength + 1][11];
        for ( int y = 0; y <= sideRoomLength; y++ ) {
            for ( int x = 0; x <= 10; x++ ) {
                if ( y == sideRoomLength || x == 2 || x == 4 || x == 6 || x == 8 ) {
                    burrow[y][x] = new Space( y, x );
                }
            }
            if (y<sideRoomLength) {
                burrow[y][2].sideRoomType = 'A';
                burrow[y][4].sideRoomType = 'B';
                burrow[y][6].sideRoomType = 'C';
                burrow[y][8].sideRoomType = 'D';
            }
        }
        
        for ( int y = sideRoomLength - 1; y >= 0; y-- ) {
            if ( input.charAt( input.length() - 4 * ( y + 1 ) + 0 ) != '.' ) {
                addAmphipod( y, 2, new Amphipod( input.charAt( input.length() - 4 * ( y + 1 ) + 0 ) ) );
            }
            if ( input.charAt( input.length() - 4 * ( y + 1 ) + 1 ) != '.' ) {
                addAmphipod( y, 4, new Amphipod( input.charAt( input.length() - 4 * ( y + 1 ) + 1 ) ) );
            }
            if ( input.charAt( input.length() - 4 * ( y + 1 ) + 2 ) != '.' ) {
                addAmphipod( y, 6, new Amphipod( input.charAt( input.length() - 4 * ( y + 1 ) + 2 ) ) );
            }
            if ( input.charAt( input.length() - 4 * ( y + 1 ) + 3 ) != '.' ) {
                addAmphipod( y, 8, new Amphipod( input.charAt( input.length() - 4 * ( y + 1 ) + 3 ) ) );
            }
        }
    }
    
    public AmphipodBurrow( AmphipodBurrow amphipodBurrow ) {
        this.burrow = new Space[amphipodBurrow.burrow.length][11];
        for ( int y = 0; y < amphipodBurrow.burrow.length; y++ ) {
            for ( int x = 0; x <= 10; x++ ) {
                if ( null != amphipodBurrow.getSpace( y, x ) ) {
                    this.burrow[y][x] = new Space( amphipodBurrow.getSpace( y, x ) );
                    if ( null != this.burrow[y][x].amphipod ) {
                        this.burrow[y][x].amphipod.currentSpace = this.burrow[y][x];
                        amphipods.add( this.burrow[y][x].amphipod );
                    }
                }
            }
        }
    }
    
    public int moveAmphipodToSpace( Amphipod amphipod, Space space ) {
        int movesMade = Math.abs( amphipod.currentSpace.x - space.x ) + Math.abs( amphipod.currentSpace.y - space.y );
        int energySpent = amphipod.energy * movesMade;
        amphipod.currentSpace.amphipod = null;
        space.amphipod = amphipod;
        amphipod.currentSpace = space;
        return energySpent;
    }
    boolean gameCompleted() {
        for ( Amphipod amphipod : amphipods ) {
            if ( notInFinalPosition( amphipod ) ) {
                return false;
            }
        }
        return true;
    }
    
    public boolean notInFinalPosition( Amphipod amphipod ) {
        //needs to be in correct sideroom, and not have any other type of amphipods below it
        if ( amphipod.currentSpace.isInSideRoom() && amphipod.currentSpace.sideRoomType == amphipod.type ) {
            if ( amphipod.currentSpace.y > 0 ) {
                for ( int y = 0; y < amphipod.currentSpace.y; y++ ) {
                    if ( null != burrow[y][amphipod.currentSpace.x].amphipod && burrow[y][amphipod.currentSpace.x].amphipod.type != amphipod.type ) {
                        return true; //a lower amphipod still needs to move out
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    public Space getSpace( int y, int x ) {
        return burrow[y][x];
    }
    
    public Set<Space> getAdjacentSpaces( Space space ) {
        Set<Space> adjacentSpaces = new HashSet<>();
        if ( space.x < 10 && burrow[space.y][space.x + 1] != null ) {
            adjacentSpaces.add( burrow[space.y][space.x + 1] );
        }
        if ( space.x > 0 && burrow[space.y][space.x - 1] != null ) {
            adjacentSpaces.add( burrow[space.y][space.x - 1] );
        }
        if ( space.y < burrow.length - 1 && burrow[space.y + 1][space.x] != null ) {
            adjacentSpaces.add( burrow[space.y + 1][space.x] );
        }
        if ( space.y > 0 && burrow[space.y - 1][space.x] != null ) {
            adjacentSpaces.add( burrow[space.y - 1][space.x] );
        }
        return adjacentSpaces;
    }
    
    public void addAmphipod( int y, int x, Amphipod amphipod ) {
        burrow[y][x].amphipod = amphipod;
        amphipod.currentSpace = burrow[y][x];
        amphipods.add( amphipod );
    }
    
    void print() {
        System.out.println( this );
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append( "#############" + NEWLINE );
        for ( int y = burrow.length - 1; y >= 0; y-- ) {
            for ( int x = -1; x <= 11; x++ ) {
                if ( y == burrow.length - 1 ) {
                    if ( x == -1 || x == 11 ) {
                        result.append( '#' );
                    }
                    else {
                        result.append( burrow[y][x] );
                    }
                }
                else if ( y == burrow.length - 2 ) {
                    if ( x == 2 || x == 4 || x == 6 || x == 8 ) {
                        result.append( burrow[y][x] );
                    }
                    else {
                        result.append( "#" );
                    }
                }
                else {
                    if ( x == 2 || x == 4 || x == 6 || x == 8 ) {
                        result.append( burrow[y][x] );
                    }
                    else if ( x == -1 || x == 0 || x == 10 || x == 11 ) {
                        result.append( " " );
                    }
                    else {
                        result.append( "#" );
                    }
                }
            }
            result.append( NEWLINE );
        }
        result.append( "  #########  " + NEWLINE );
        result.append( NEWLINE );
        return result.toString();
    }
    
    public Map<Amphipod, Set<Space>> getAllPossibleMoves() {
        Map<Amphipod, Set<Space>> allPossibleMoves = new HashMap<>();
        for ( Amphipod amphipod : amphipods ) {
            allPossibleMoves.put( amphipod, getPossibleMoves( amphipod ) );
        }
        return allPossibleMoves;
    }
    
    public Set<Space> getPossibleMoves( Amphipod amphipod ) {
        Set<Space> visitedSpaces = new HashSet<>();
        return getPossibleMoves( amphipod, amphipod.currentSpace, visitedSpaces );
    }
    
    public Set<Space> getPossibleMoves( Amphipod amphipod, Space space,Set<Space> visitedSpaces ) {
        Set<Space> possibleMoves = new HashSet<>();
        if ( notInFinalPosition( amphipod ) ) {
            for ( Space adjacentSpace : getAdjacentSpaces( space ) ) {
                if ( !adjacentSpace.isOccupied() && !visitedSpaces.contains( adjacentSpace )  ) {
                    if ( isValidMove( amphipod, adjacentSpace ) ) {
                        possibleMoves.add( adjacentSpace );
                    }
                    visitedSpaces.add( adjacentSpace );
                    possibleMoves.addAll( getPossibleMoves( amphipod, adjacentSpace, visitedSpaces ) );
                }
            }
        }
        return possibleMoves;
    }
    
    private boolean isValidMove( Amphipod amphipod, Space adjacentSpace ) {
        return ( ( amphipod.currentSpace.isHallway() && adjacentSpace.isInSideRoom() && okToEnterSideroom( adjacentSpace, amphipod ) )
                || (  amphipod.currentSpace.isInSideRoom() && adjacentSpace.isHallway() && adjacentSpace.isStoppingSpace()  ) );
    }
    
    private boolean okToEnterSideroom( Space space, Amphipod amphipod ) {
        if ( amphipod.type != space.sideRoomType ) {
            return false;
        }
        else {
            for ( int y = 0; y <= space.y; y++ ) {
                if ( null != burrow[y][space.x].amphipod && burrow[y][space.x].amphipod.type != amphipod.type ) {
                    return false;
                }
            }
            return true;
        }
    }
    
    @Override
    public boolean equals( Object o ) {
        return toString().equals( o.toString() );
    }
    
    @Override
    public int hashCode() {
        return Objects.hash( toString() );
    }
}
