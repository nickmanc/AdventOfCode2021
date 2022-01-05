package com.hotmail.nickcooke.aoc2021;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Day18 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day18 day18 = new Day18();
        day18.getInput();
        day18.part1();
        day18.part2();
    }
    
    protected void part1() {
        SFN result = null;
        for ( String input : inputLines ) {
            if ( null == result ) {
                result = new SFN( input );
            }
            else {
                result = result.add( new SFN( input ) );
            }
        }
        System.out.println( "Part 1: " + result.magnitude() );
    }
    
    protected void part2() {
        Set<SFN> snailFishNumbers = new HashSet<>();
        for ( String input : inputLines ) {
            snailFishNumbers.add( new SFN( input ) );
        }
        int maxMagnitude = Integer.MIN_VALUE;
        for ( SFN sfn : snailFishNumbers ) {
            for ( SFN otherSfn : snailFishNumbers ) {
                if ( sfn != otherSfn ) {
                    SFN copy = new SFN( sfn.toString() ).add( new SFN( otherSfn.toString() ) );
                    maxMagnitude = Math.max( maxMagnitude, copy.magnitude() );
                }
            }
        }
        System.out.println( "Part 2: " + maxMagnitude );
    }
}

class SFN {
    SFN left;
    SFN right;
    
    SFN parent;
    
    int value = -1;
    
    public SFN( int value ) {
        this.value = value;
    }
    
    public SFN( SFN left, SFN right ) {
        this.left = left;
        this.right = right;
        left.parent = this;
        right.parent = this;
    }
    
    public SFN( String sfnString ) {
        if ( isDigit( sfnString ) ) {
            this.value = Integer.parseInt( sfnString );
        }
        else {
            Stack<SFN> sfnStack = new Stack<>();
            for ( int i = 0; i < sfnString.length(); i++ ) {
                char currentChar = sfnString.charAt( i );
                if ( currentChar == '[' ) {
                    SFN sfn;
                    if ( sfnStack.isEmpty() ) {
                        sfn = this;
                        sfn.parent = null;
                    }
                    else {
                        sfn = new SFN();
                        sfn.parent = sfnStack.peek();
                    }
                    sfnStack.push( sfn );
                }
                else if ( isDigit( currentChar + "" ) ) {
                    String digit = currentChar + "";
                    while ( isDigit( sfnString.charAt( i + 1 ) + "" ) ) {
                        digit += sfnString.charAt( ++i );
                    }
                    SFN newSFN = sfnStack.peek();
                    if ( null == newSFN.left ) {
                        newSFN.left = new SFN( digit );
                        newSFN.left.parent = newSFN;
                    }
                    else {
                        newSFN.right = new SFN( digit );
                        newSFN.right.parent = newSFN;
                    }
                }
                else if ( currentChar == ']' ) {
                    SFN currentSFN = sfnStack.pop();
                    if ( !sfnStack.isEmpty() ) {
                        if ( null == currentSFN.parent.left ) {
                            currentSFN.parent.left = currentSFN;
                        }
                        else {
                            currentSFN.parent.right = currentSFN;
                        }
                    }
                }
            }
        }
    }
    
    public SFN() {
    
    }
    
    public SFN add( SFN right ) {
        SFN combined = new SFN( this, right );
        combined.reduce();
        return combined;
    }
    
    public int magnitude() {
        if ( value != -1 ) {
            return value;
        }
        else {
            return ( 3 * left.magnitude() + 2 * right.magnitude() );
        }
    }
    
    private boolean isDigit( String sfnString ) {
        try {
            Integer.parseInt( sfnString );
            return true;
        }
        catch ( NumberFormatException cfe ) {
        
        }
        return false;
    }
    
    public int getParentCount() {
        if ( null == this.parent ) {
            return 0;
        }
        return this.parent.getParentCount() + 1;
    }
    
    @Override
    public String toString() {
        if ( value > -1 ) {
            return value + "";
        }
        else {
            return "[" + left.toString() + "," + right.toString() + "]";
        }
    }
    
    public void reduce() {
        while ( explode() || split() );
    }
    
    protected boolean split() {
        if ( value != -1 ) {
            if ( value > 9 ) {
                left = new SFN( value / 2 );
                right = new SFN( ( value + 1 ) / 2 );
                left.parent = this;
                right.parent = this;
                value = -1;
                return true;
            }
            return false;
        }
        return left.split() || right.split();
    }
    
    protected boolean explode() {
        if ( value == -1 ) {
            int parentCount = getParentCount();
            if ( parentCount >= 4 ) {
                if ( isLeft() ) {
                    if ( parent.right.value == -1 ) {
                        parent.right.left.value += right.value;
                    }
                    else {
                        parent.right.value += right.value;
                    }
                    SFN leftSFN = getLeftSFN();
                    if ( null != leftSFN ) {
                        leftSFN.value += this.left.value;
                    }
                    this.value = 0;
                    this.left = null;
                    this.right = null;
                }
                else {
                    if ( parent.left.value == -1 ) {
                        parent.left.right.value += left.value;
                    }
                    else {
                        parent.left.value += left.value;
                    }
                    SFN rightSFN = getRightSFN();
                    if ( null != rightSFN ) {
                        rightSFN.value += this.right.value;
                    }
                    this.value = 0;
                    this.left = null;
                    this.right = null;
                }
                return true;
            }
            else {
                return left.explode() || right.explode();
            }
        }
        return false;
    }
    
    private SFN getRightSFN() {
        if ( null != parent ) {
            if ( isRight() ) {//traverse up the tree as long as it's a right
                return parent.getRightSFN();
            }
            SFN rightCandidate = parent.right;
            while ( rightCandidate.left != null ) {//traverse down the tree to get the rightmost left ?!
                rightCandidate = rightCandidate.left;
            }
            return rightCandidate;
        }
        return null;
    }
    
    private SFN getLeftSFN() {
        if ( null != parent ) {
            if ( isLeft() ) {//traverse up the tree as long as it's a left
                return parent.getLeftSFN();
            }
            SFN leftCandidate = parent.left;
            while ( leftCandidate.right != null ) {//traverse down the tree to get the rightmost left ?!
                leftCandidate = leftCandidate.right;
            }
            return leftCandidate;
        }
        return null;
    }
    
    private boolean isLeft() {
        return this.equals( parent.left );
    }
    
    private boolean isRight() {
        return this.equals( parent.right );
    }
}