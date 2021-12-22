package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.List;

public class Day18 extends AoCSolution {
    
    List<SFN> snailfishNumberList = new ArrayList<>();
    
    public static void main( String[] args ) {
        Day21 day18 = new Day21();
        day18.getInput();
        day18.part1();
        day18.part2();
    }
    
    protected void part1() {
    }
    
    protected void part2() {
    }
    
    protected SFN add( SFN left, SFN right ) {
        SFN combined = new SFN( left, right );
        left.parent = combined;
        right.parent = combined;
        combined.reduce();
        return combined;
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
    
    private SFN getRoot(){
        SFN potentialRoot = this;
        while (null != potentialRoot.parent){
            potentialRoot = potentialRoot.parent;
        }
        return potentialRoot;
    }
    
    private List<SFN> getAllRegularNumbers() {
        if (isRegularNumber()){
            List<SFN> result = new ArrayList<>();
            result.add( this );
            return result ;
        }
        else {
            List<SFN> result = new ArrayList<>();
            result.addAll( left.getAllRegularNumbers() );
            result.addAll( right.getAllRegularNumbers() );
            return result;
        }
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
        explode();
    }
    
    
    private boolean split() {
        return false;
    }
    
    private boolean explode() {
        if ( value == -1 ) {
            int parentCount = getParentCount();
            if ( parentCount == 4 ) {
                SFN parentToReplace = this.parent;
                SFN pair = this.parent;
                System.out.println( "Exploding: " + wholeNumberToString() );
                SFN root = getRoot();
                List<SFN> allDigits = root.getAllRegularNumbers();
                if (allDigits.indexOf(  this.left ) > 0 ) {
                    allDigits.get( allDigits.indexOf( this.left ) - 1 ).value += left.value;
                }
                if (allDigits.indexOf(  this.right ) < allDigits.size() -1) {
                    allDigits.get( allDigits.indexOf( this.right ) + 1 ).value += right.value;
                }
                this.left = null;
                this.right = null;
                this.value=0;
                
//                if ( this.isLeft() ) {
//                    parentToReplace.left.value = 0;
//                    parentToReplace.right.value += right.value;
//
//                    while ( pair.isNested() && isLeft() ) {
//                        pair = pair.parent;
//                    }
//                    if ( pair.left.isRegularNumber() ) {
//                        pair.left.value += left.value;
//                    }
//                    else {
//                        while ( !pair.right.isRegularNumber() ) {
//                            pair = pair.right;
//                        }
//                        if ( isRight() ) {
//                            pair.left.value += left.value;
//                        }
//                    }
//                }
//                else {
//                    parentToReplace.right.value = 0;
//                    parentToReplace.left.value += left.value;
//
//                    while ( pair.isNested() && isRight() ) {
//                        pair = pair.parent;
//                    }
//                    if ( pair.right.isRegularNumber() ) {
//                        pair.right.value += right.value;
//                    }
//                    else {
//                        while ( !pair.left.isRegularNumber() ) {
//                            pair = pair.left;
//                        }
//                        if ( isLeft() ) {
//                            pair.right.value += right.value;
//                        }
//                    }
//                }
//                SFN replacement = new SFN( 0 );
//                this.left.parent = replacement;
//                this.right.parent = replacement;
//                replacement.parent = parentToReplace.parent;
                return true;
            }
            else {
                left.explode();
                right.explode();
            }
        }
        return false;
    }
    
    
    private boolean isRegularNumber() {
        return value != -1;
    }
    
    private String wholeNumberToString() {
        SFN parent = this.parent;
        while ( parent.isNested() ) {
            parent = parent.parent;
        }
        return parent.toString();
    }
    
    private boolean isLeft() {
        return this.equals( parent.left );
    }
    
    private boolean isRight() {
        return this.equals( parent.right );
    }
    
    private boolean isNested() {
        return !( null == parent );
    }
}