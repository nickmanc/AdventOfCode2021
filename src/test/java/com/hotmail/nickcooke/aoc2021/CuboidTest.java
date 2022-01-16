package com.hotmail.nickcooke.aoc2021;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class CuboidTest {
    @Test
    public void testOverlaps(){
        Cuboid cuboid1 = new Cuboid( new Vertex(1,1,1), new Vertex(2,2,2) );
        Cuboid cuboid2 = new Cuboid( new Vertex(3,3,3), new Vertex(4,4,4) );
        assertFalse( cuboid1.overlaps(cuboid2) );
        assertFalse( cuboid2.overlaps(cuboid1) );
        
        cuboid1 = new Cuboid( new Vertex(1,1,1), new Vertex(3,3, 3));
        cuboid2 = new Cuboid( new Vertex(2,2,2), new Vertex(3,3, 3));
        assertTrue( cuboid1.overlaps(cuboid2) );
        assertTrue( cuboid2.overlaps(cuboid1) );
    
        cuboid2 = new Cuboid( new Vertex(3,3,3), new Vertex(4,4, 4));
        assertFalse( cuboid1.overlaps(cuboid2) );
        assertFalse( cuboid2.overlaps(cuboid1) );
    
        cuboid1 = new Cuboid( new Vertex(1,1,1), new Vertex(2,2,2));
        cuboid2 = new Cuboid( new Vertex(0,0,0), new Vertex(3,3,3));
        assertTrue( cuboid1.overlaps(cuboid2) );
        assertTrue( cuboid2.overlaps(cuboid1) );
    
        cuboid1 = new Cuboid( new Vertex(3,2,3), new Vertex(5,3,5));
        cuboid2 = new Cuboid( new Vertex(1,1,1), new Vertex(4,4,4));
        assertTrue( cuboid1.overlaps(cuboid2) );
        assertTrue( cuboid2.overlaps(cuboid1) );
    
        cuboid1 = new Cuboid( new Vertex(1,0,1), new Vertex(3,1,3));
        cuboid2 = new Cuboid( new Vertex(-1,-1,-1), new Vertex(2,2,2));
        assertTrue( cuboid1.overlaps(cuboid2) );
        assertTrue( cuboid2.overlaps(cuboid1) );
    }
    @Test
    public void testContains(){
        Cuboid cuboid1 = new Cuboid( new Vertex(1,1,1), new Vertex(2,2,2) );
        Cuboid cuboid2 = new Cuboid( new Vertex(3,3,3), new Vertex(4,4,4) );
        assertFalse( cuboid1.contains(cuboid2) );
        assertFalse( cuboid2.contains(cuboid1) );

        cuboid2 = new Cuboid( new Vertex(1,1,1), new Vertex(2,2,2) );
        assertTrue( cuboid1.contains(cuboid2) );
        assertTrue( cuboid2.contains(cuboid1) );

        cuboid2 = new Cuboid( new Vertex(1,1,1), new Vertex(3,3,3) );
        assertFalse( cuboid1.contains(cuboid2) );
        assertTrue( cuboid2.contains(cuboid1) );

        cuboid2 = new Cuboid( new Vertex(2,2,2), new Vertex(3,3,3) );
        assertFalse( cuboid1.contains(cuboid2) );
        assertFalse( cuboid2.contains(cuboid1) );

        cuboid1 = new Cuboid( new Vertex(1,1,1), new Vertex(3,3,3) );
        cuboid2 = new Cuboid( new Vertex(2,2,2), new Vertex(3,3,3) );
        assertTrue( cuboid1.contains(cuboid2) );
        assertFalse( cuboid2.contains(cuboid1) );
    }
    
    @Test
    public void testVolume() {
        Cuboid cuboid = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 2, 2, 2 ) );
        assertEquals( 1, cuboid.volume() );
        cuboid = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 3, 3, 3 ) );
        assertEquals( 8, cuboid.volume() );
        cuboid = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 4,4,4) );
        assertEquals( 27, cuboid.volume() );


        cuboid = new Cuboid( new Vertex( -1, -1, -1 ), new Vertex( 0,0,0 ) );
        assertEquals( 1, cuboid.volume() );
        cuboid = new Cuboid( new Vertex( -2, -2, -2 ), new Vertex( 0,0,0 ) );
        assertEquals( 8, cuboid.volume() );
        cuboid = new Cuboid( new Vertex( -3, -3, -3 ), new Vertex( 0,0,0 ) );
        assertEquals( 27, cuboid.volume() );

        cuboid = new Cuboid( new Vertex( -1, -1, -1 ), new Vertex( 1,1,1 ) );
        assertEquals( 8, cuboid.volume() );
    }
    
    @Test
    public void testCombines() {
    
        Cuboid cuboid1 = new Cuboid( new Vertex( 2, 2, 2 ), new Vertex(4,4,4 ) );
        Cuboid cuboid2 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 5, 5, 5 ) );
        Set<Cuboid> combined = cuboid1.combine( cuboid2 );
        assertEquals( 0, combined.size() );
        long volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 0,volume );
        cuboid2.isOn=true;
         combined = cuboid1.combine( cuboid2 );
        assertEquals( 1, combined.size() );
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 64,volume );
    
        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 5, 5, 5 ) );
        cuboid2 = new Cuboid( new Vertex( 2, 2, 2 ), new Vertex(4,4,4 ) );
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 26, combined.size() );
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 56,volume );
    
        Set<Cuboid> combined2 = new HashSet<>();
        cuboid1.isOn=true;
        for (Cuboid subCuboid : combined){
            combined2.addAll( subCuboid.combine( cuboid1 ) );
        }
        volume= combined2.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 64,volume );
    
        combined2 = new HashSet<>();
        cuboid2.isOn=true;
        for (Cuboid subCuboid : combined){
            combined2.addAll( subCuboid.combine( cuboid2 ) );
        }
        volume= combined2.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 64,volume );
    
        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 2, 2, 2 ) );
        cuboid2 = new Cuboid( new Vertex( 2, 2, 2 ), new Vertex( 3, 3, 3 ) );
        cuboid2.isOn=true;
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 2, combined.size() );
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 2,volume );

        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 3, 3, 3 ) );
        cuboid2 = new Cuboid( new Vertex( 3, 3,3 ), new Vertex( 5,5,5 ) );
        cuboid2.isOn=true;
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 2, combined.size() );
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 16,volume );

        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 2, 2, 2 ) );
        cuboid2 = new Cuboid( new Vertex( 2, 2,2 ), new Vertex( 4, 4, 4 ) );
        cuboid2.isOn=true;
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 2, combined.size() );
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 9,volume );

        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 3, 3, 3 ) );
        cuboid2 = new Cuboid( new Vertex( 2,2,2 ), new Vertex( 3,4,3 ) );
        cuboid2.isOn=true;
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 8, combined.size() );
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 9,volume );
    
    
        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 5, 5, 5 ) );
        cuboid2 = new Cuboid( new Vertex( 4,4,4 ), new Vertex( 8,8,8 ) );
        cuboid2.isOn=true;
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 8, combined.size() );//????????????
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 127,volume );
        
        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 5, 5, 5 ) );
        cuboid2 = new Cuboid( new Vertex( 4,4,4 ), new Vertex( 8,8,8 ) );
        cuboid2.isOn=false;
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 7, combined.size() );//????????????
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 63,volume );
    
    
        cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 5, 5, 5 ) );
        cuboid2 = new Cuboid( new Vertex( 3,3,3 ), new Vertex( 7,7,7 ) );
        cuboid2.isOn=true;
        combined = cuboid1.combine( cuboid2 );
        assertEquals( 8, combined.size() );//????????????
        volume= combined.stream().collect( Collectors.summingLong(Cuboid::volume) );
        assertEquals( 120,volume );
    }
    
    @Test
    public void moreTests() {
    
        Cuboid cuboid1 = new Cuboid( new Vertex( 1, 1, 1 ), new Vertex( 3, 3, 3 ) );
        Cuboid cuboid2 = new Cuboid( new Vertex( 2, 2, 2 ), new Vertex( 5, 5, 5 ) );
        Cuboid cuboid3 = new Cuboid( new Vertex( 0,0,0 ), new Vertex( 4,4,4) );
        
        cuboid1.isOn=true;
        cuboid2.isOn=true;
        cuboid3.isOn=false;
        
        Set<Cuboid> firstCombine = cuboid1.combine( cuboid2 );
        Set<Cuboid> secondCombine = new HashSet<>();
        for (Cuboid cuboid : firstCombine) {
            secondCombine.addAll( cuboid.combine( cuboid3 ) );
        }
        assertEquals( 19, secondCombine.stream().collect( Collectors.summingLong(Cuboid::volume) ) );
    
        cuboid1 = new Cuboid( new Vertex( 0,0,0 ), new Vertex( 10,10,2) );
        cuboid2 = new Cuboid( new Vertex( 0,0,6 ), new Vertex( 10,10,8 ) );
        cuboid3 = new Cuboid( new Vertex( 0,0,0 ), new Vertex( 10,10,1) );
    
        cuboid1.isOn=true;
        cuboid2.isOn=true;
        cuboid3.isOn=true;
    
        firstCombine = cuboid1.combine( cuboid2 );
        secondCombine = new HashSet<>();
        for (Cuboid cuboid : firstCombine) {
            secondCombine.addAll( cuboid.combine( cuboid3 ) );
        }
        assertEquals( 400, secondCombine.stream().collect( Collectors.summingLong(Cuboid::volume) ) );
    }
}