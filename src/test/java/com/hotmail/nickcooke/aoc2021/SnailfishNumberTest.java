package com.hotmail.nickcooke.aoc2021;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SnailfishNumberTest {
    @Test
    public void testConstructor(){
        SFN snailfishNumber = new SFN(new SFN(1),new SFN(2));
        assertEquals( "[1,2]", snailfishNumber.toString() );
    }
    
    
    @Test
    public void testAdd() {
        SFN sfn1 = new SFN( new SFN( 1 ), new SFN( 1 ) );
        SFN sfn2 = new SFN( new SFN( 2 ), new SFN( 2 ) );
        assertEquals( 0, sfn1.getParentCount() );
        assertEquals( 0, sfn2.getParentCount() );
        Day21 day18 = new Day21();
        SFN result = day18.add( sfn1, sfn2 );
        assertEquals( "[[1,1],[2,2]]", result.toString() );
        assertEquals( 0, result.getParentCount() );
        assertEquals( 1, sfn1.getParentCount() );
        assertEquals( 1, sfn2.getParentCount() );
        SFN sfn3 = new SFN( new SFN( 3 ), new SFN( 3 ) );
        result = day18.add( result, sfn3 );
        assertEquals( "[[[1,1],[2,2]],[3,3]]", result.toString() );
        assertEquals( 0, result.getParentCount() );
        assertEquals( 2, sfn1.getParentCount() );
        assertEquals( 2, sfn2.getParentCount() );
        assertEquals( 1, sfn3.getParentCount() );
    }
    
    @Test
    public void testAddWithReduce(){
        Day21 day18 = new Day21();
        SFN snailfishNumber = new SFN(new SFN(new SFN(new SFN(new SFN(new SFN(9),new SFN(8)),new SFN(1)),new SFN(2)),new SFN(3)),new SFN(4));
                System.out.println(snailfishNumber);
        snailfishNumber.reduce();
        assertEquals( "[[[[0,9],2],3],4]", snailfishNumber.toString() );
        
        snailfishNumber = new SFN(new SFN( new SFN( 6),
                new SFN( new SFN( 5 ),
                new SFN( new SFN( 4 ),
                new SFN( new SFN( 3 ), new SFN( 2) )))), new SFN( 1 ));
        snailfishNumber.reduce();
        assertEquals( "[[6,[5,[7,0]]],3]", snailfishNumber.toString() );
        System.out.println(snailfishNumber);
        
    
//        snailfishNumber = new SFN(new SFN (1),new SFN(new SFN(new SFN(new SFN(new SFN(new SFN(9),new SFN(8)),new SFN(1)),new SFN(2)),new SFN(3)),new SFN(4)));
//        snailfishNumber.reduce();
//        System.out.println(snailfishNumber);
//        assertEquals( "[10,[[[[0,9],2],3],4]]", snailfishNumber.toString() );

    }
}