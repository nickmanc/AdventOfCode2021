package com.hotmail.nickcooke.aoc2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class AmphipodBurrowTest {
    @Test
    public void testBurrowCompleted() {
        String input = "BCBDADCA";
        AmphipodBurrow burrow = new AmphipodBurrow( input );
        burrow.print();
        assertFalse( burrow.gameCompleted() );
        input = "ABCDABCD";
        burrow = new AmphipodBurrow( input );
        burrow.print();
        assertTrue( burrow.gameCompleted() );
    }
    
    @Test
    public void testGetPossibleMovesOutOfSideRoom() {
        String input = "BCBDADCA";
        AmphipodBurrow burrow = new AmphipodBurrow( input );
        Amphipod amphipod = new Amphipod( 'X' );
        burrow.addAmphipod( 1, 2, amphipod );
        Set<Space> possibleMoves = new HashSet<>();
        Set<Space> visitedSpaces = new HashSet<>();
        Space currentSpace = burrow.getSpace( 1, 2 );
        possibleMoves = burrow.getPossibleMoves( amphipod, currentSpace, visitedSpaces );
        assertEquals( 7, possibleMoves.size() );
        
        possibleMoves = new HashSet<>();
        visitedSpaces = new HashSet<>();
        currentSpace = burrow.getSpace( 0, 2 );
        possibleMoves = burrow.getPossibleMoves( amphipod, currentSpace, visitedSpaces );
        assertEquals( 0, possibleMoves.size() );
        
        possibleMoves = new HashSet<>();
        visitedSpaces = new HashSet<>();
        burrow = new AmphipodBurrow( input );
        burrow.addAmphipod( 1, 4, amphipod );
        currentSpace = burrow.getSpace( 1, 4 );
        possibleMoves = burrow.getPossibleMoves( amphipod, currentSpace, visitedSpaces );
        assertEquals( 7, possibleMoves.size() );
        
        input = ".BCDABCD";
        burrow = new AmphipodBurrow( input );
        burrow.print();
        possibleMoves = new HashSet<>();
        visitedSpaces = new HashSet<>();
        burrow.addAmphipod( 0, 2, amphipod );
        currentSpace = burrow.getSpace( 0, 2 );
        possibleMoves = burrow.getPossibleMoves( amphipod, currentSpace, visitedSpaces );
        for ( Space space : possibleMoves ) {
            System.out.println( space.toStringCoordinates() );
        }
        assertEquals( 7, possibleMoves.size() );
    }
    
    @Test
    public void testGetPossibleMovesOutOfHallway() {
        String input = "........";
        AmphipodBurrow burrow = new AmphipodBurrow( input );
        Amphipod amphipod = new Amphipod( 'A' );
        Set<Space> possibleMoves = new HashSet<>();
        Set<Space> visitedSpaces = new HashSet<>();
        burrow.addAmphipod( 2, 0, amphipod );
        possibleMoves = burrow.getPossibleMoves( amphipod, amphipod.currentSpace, visitedSpaces );
        for ( Space space : possibleMoves ) {
            System.out.println( space.toStringCoordinates() );
        }
        assertEquals( 2, possibleMoves.size() );
    }
    
    @Test
    public void testGetPossibleMovesOutOfHallwayWithACorrectAmphipodAlreadyInSideroom() {
        String input = "....A...";
        AmphipodBurrow burrow = new AmphipodBurrow( input );
        Amphipod amphipod = new Amphipod( 'A' );
        Set<Space> possibleMoves = new HashSet<>();
        Set<Space> visitedSpaces = new HashSet<>();
        burrow.addAmphipod( 2, 0, amphipod );
        possibleMoves = burrow.getPossibleMoves( amphipod, amphipod.currentSpace, visitedSpaces );
        for ( Space space : possibleMoves ) {
            System.out.println( space.toStringCoordinates() );
        }
        assertEquals( 1, possibleMoves.size() );
    }
    
    @Test
    public void testGetPossibleMovesOutOfHallwayWithAnIncorrectAmphipodAlreadyInSideroom() {
        String input = "....B...";
        AmphipodBurrow burrow = new AmphipodBurrow( input );
        Amphipod amphipod = new Amphipod( 'A' );
        Set<Space> possibleMoves = new HashSet<>();
        Set<Space> visitedSpaces = new HashSet<>();
        burrow.addAmphipod( 2, 0, amphipod );
        possibleMoves = burrow.getPossibleMoves( amphipod, amphipod.currentSpace,  visitedSpaces );
        for ( Space space : possibleMoves ) {
            System.out.println( space.toStringCoordinates() );
        }
        assertEquals( 0, possibleMoves.size() );
    }
    
    
    @Test
    public void testGetPossibleMovesOutOfHallwayWithFullSideroom() {
        String input = "A...A...";
        AmphipodBurrow burrow = new AmphipodBurrow( input );
        Amphipod amphipod = new Amphipod( 'A' );
        Set<Space> possibleMoves = new HashSet<>();
        Set<Space> visitedSpaces = new HashSet<>();
        burrow.addAmphipod( 2, 0, amphipod );
        possibleMoves = burrow.getPossibleMoves( amphipod, amphipod.currentSpace, visitedSpaces );
        for ( Space space : possibleMoves ) {
            System.out.println( space.toStringCoordinates() );
        }
        assertEquals( 0, possibleMoves.size() );
    }
    
    @Test
    public void whenOtherSideroomsblocked_thenCannotMoveToSideroom() {
        String input = "....BBBB";
        AmphipodBurrow burrow = new AmphipodBurrow( input );
        burrow.print();
        Amphipod amphipod = new Amphipod( 'A' );
        Set<Space> possibleMoves = new HashSet<>();
        Set<Space> visitedSpaces = new HashSet<>();
        burrow.addAmphipod( 2,0,amphipod );
        possibleMoves = burrow.getPossibleMoves( amphipod, amphipod.currentSpace, visitedSpaces );
        for ( Space space : possibleMoves ) {
            System.out.println( space.toStringCoordinates() );
        }
        assertEquals( 0, possibleMoves.size() );
    }
    
    @Test
    public void testEquals() {
        String input = "ABCD....";
        AmphipodBurrow burrow1 = new AmphipodBurrow( input );
        AmphipodBurrow burrow2 = new AmphipodBurrow( input );
        assertTrue( burrow1.equals( burrow2 ) );
        assertFalse( burrow1 == burrow2 );
        
        input = "ABC....D";
        burrow2 = new AmphipodBurrow( input );
        assertFalse( burrow1.equals( burrow2 ) );
        
        Amphipod amphipod = burrow2.getSpace( 0, 8 ).amphipod;
        burrow2.moveAmphipodToSpace( amphipod, burrow2.getSpace( 1, 8 ));
        assertTrue( burrow1.equals( burrow2 ) );
    
        burrow2.moveAmphipodToSpace( amphipod, burrow2.getSpace(2, 8 ));
        assertFalse( burrow1.equals( burrow2 ) );
        
    }
}