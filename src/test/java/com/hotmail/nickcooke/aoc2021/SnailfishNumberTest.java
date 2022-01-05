package com.hotmail.nickcooke.aoc2021;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SnailfishNumberTest {
    @Test
    public void testConstructor() {
        SFN snailfishNumber = new SFN( "[1,2]" );
        assertEquals( "[1,2]", snailfishNumber.toString() );
    }
    
    @Test
    public void testAdd() {
        SFN sfn1 = new SFN( "[1,1]" );
        SFN sfn2 = new SFN( "[2,2]" );
        
        assertEquals( 0, sfn1.getParentCount() );
        assertEquals( 0, sfn2.getParentCount() );
        SFN result = sfn1.add( sfn2 );
        assertEquals( "[[1,1],[2,2]]", result.toString() );
        assertEquals( 0, result.getParentCount() );
        assertEquals( 1, sfn1.getParentCount() );
        assertEquals( 1, sfn2.getParentCount() );
        SFN sfn3 = new SFN( new SFN( 3 ), new SFN( 3 ) );
        result = result.add( sfn3 );
        assertEquals( "[[[1,1],[2,2]],[3,3]]", result.toString() );
        
        sfn1 = new SFN( "[[[[1,1],[2,2]],[3,3]],[4,4]]" );
        sfn2 = new SFN( "[5,5]" );
        assertEquals( "[[[[3,0],[5,3]],[4,4]],[5,5]]", sfn1.add( sfn2 ).toString() );
        
    }
    
    @Test
    public void testSplit() {
        SFN snailfishNumber = new SFN( 15 );
        snailfishNumber.split();
        assertEquals( "[7,8]", snailfishNumber.toString() );
        
        snailfishNumber = new SFN( 16 );
        snailfishNumber.split();
        assertEquals( "[8,8]", snailfishNumber.toString() );
    }
    
    @Test
    public void testExplode() {
        SFN snailfishNumber = new SFN( "[[[[[9,8],1],2],3],4]" );
        snailfishNumber.explode();
        assertEquals( "[[[[0,9],2],3],4]", snailfishNumber.toString() );
        
        snailfishNumber = new SFN( "[[6,[5,[4,[3,2]]]],1]" );
        snailfishNumber.explode();
        assertEquals( "[[6,[5,[7,0]]],3]", snailfishNumber.toString() );
        System.out.println( snailfishNumber );
        
        snailfishNumber = new SFN( "[7,[6,[5,[4,[3,2]]]]]" );
        snailfishNumber.explode();
        assertEquals( "[7,[6,[5,[7,0]]]]", snailfishNumber.toString() );
        System.out.println( snailfishNumber );
        
        snailfishNumber = new SFN( "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" );
        snailfishNumber.explode();
        assertEquals( "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", snailfishNumber.toString() );
        snailfishNumber.explode();
        assertEquals( "[[3,[2,[8,0]]],[9,[5,[7,0]]]]", snailfishNumber.toString() );
        System.out.println( snailfishNumber );
        
        snailfishNumber = new SFN( "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" );
        snailfishNumber.explode();
        assertEquals( "[[3,[2,[8,0]]],[9,[5,[7,0]]]]", snailfishNumber.toString() );
        System.out.println( snailfishNumber );
    }
    
    @Test
    public void testExplodeAndSplitAfterAdd() {
        
        SFN snailFishNumber1 = new SFN( "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]" );
        System.out.println( snailFishNumber1 );
        System.out.println( "*******" );
        
        snailFishNumber1.explode();
        assertEquals( "[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", snailFishNumber1.toString() );
        snailFishNumber1.explode();
        assertEquals( "[[[[0,7],4],[15,[0,13]]],[1,1]]", snailFishNumber1.toString() );
        snailFishNumber1.split();
        assertEquals( "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", snailFishNumber1.toString() );
        snailFishNumber1.split();
        assertEquals( "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", snailFishNumber1.toString() );
        snailFishNumber1.explode();
        assertEquals( "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", snailFishNumber1.toString() );
        
        SFN left = new SFN( "[[[[4,3],4],4],[7,[[8,4],9]]]" );
        SFN right = new SFN( "[1,1]" );
        SFN combined = new SFN( left, right );
        combined.explode();
        combined.explode();
        combined.split();
        combined.split();
        combined.explode();
        assertEquals( "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", combined.toString() );
        
        SFN snailFishNumber2 = new SFN( "[[[[4,3],4],4],[7,[[8,4],9]]]" );
        SFN snailFishNumber3 = new SFN( "[1,1]" );
        SFN result = snailFishNumber2.add( snailFishNumber3 );
        assertEquals( "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", result.toString() );
        
    }
    
    @Test
    public void testStringConstructor() {
        SFN sfn = new SFN( "7" );
        assertEquals( "7", sfn.toString() );
        
        sfn = new SFN( "[1,2]" );
        assertEquals( "[1,2]", sfn.toString() );
        
        sfn = new SFN( "[[1,2],3]" );
        assertEquals( "[[1,2],3]", sfn.toString() );
    }
    
    @Test
    public void randomTests1() {
        SFN sfn = new SFN( "[[[[[4,0],[5,4]],[[7,7],[6,0]]],[[[6,6],[5,0]],[[6,6],[7,0]]]],[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]]" );
        sfn.reduce();
    }
    
    @Test
    public void randomTests2() {
        
        SFN sfn1 = new SFN( "[[[[4,0],[5,4]],[[7,0],[15,5]]],[10,[[11,0],[[9,3],[8,8]]]]]" );
        sfn1.explode();
        assertEquals( "[[[[4,0],[5,4]],[[7,0],[15,5]]],[10,[[11,9],[0,[11,8]]]]]", sfn1.toString() );
        sfn1.explode();
        assertEquals( "[[[[4,0],[5,4]],[[7,0],[15,5]]],[10,[[11,9],[11,0]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,0],[[7,8],5]]],[10,[[11,9],[11,0]]]]", sfn1.toString() );
        sfn1.explode();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[0,13]]],[10,[[11,9],[11,0]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[0,[6,7]]]],[10,[[11,9],[11,0]]]]", sfn1.toString() );
        sfn1.explode();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[17,[[11,9],[11,0]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,9],[[11,9],[11,0]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,9],[[[5,6],9],[11,0]]]]", sfn1.toString() );
        sfn1.explode();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,14],[[0,15],[11,0]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[0,15],[11,0]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[0,[7,8]],[11,0]]]]", sfn1.toString() );
        sfn1.explode();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,0],[19,0]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,0],[[9,10],0]]]]", sfn1.toString() );
        sfn1.explode();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[0,10]]]]", sfn1.toString() );
        sfn1.split();
        assertEquals( "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[0,[5,5]]]]]", sfn1.toString() );
        sfn1.reduce();
        System.out.println( sfn1 );
    }
    
    @Test
    public void testMagnitude() {
        SFN sfn1 = new SFN( 5 );
        assertEquals( 5, sfn1.magnitude() );
        
        sfn1 = new SFN( "[9,1]" );
        assertEquals( 29, sfn1.magnitude() );
        
        sfn1 = new SFN( "[1,9]" );
        assertEquals( 21, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[9,1],[1,9]]" );
        assertEquals( 129, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[1,2],[[3,4],5]]" );
        assertEquals( 143, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" );
        assertEquals( 1384, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[[[1,1],[2,2]],[3,3]],[4,4]]" );
        assertEquals( 445, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[[[3,0],[5,3]],[4,4]],[5,5]]" );
        assertEquals( 791, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[[[5,0],[7,4]],[5,5]],[6,6]]" );
        assertEquals( 1137, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]" );
        assertEquals( 4140, sfn1.magnitude() );
        
        sfn1 = new SFN( "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" );
        assertEquals( 3488, sfn1.magnitude() );
    }
}