package com.hotmail.nickcooke.aoc2021;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends AoCSolution {
    
    public static void main( String[] args ) {
        Day24 day24 = new Day24();
        day24.getInput();
        day24.part1();
    }
    
    public Map<Parameters, Long> parametersToZMap = new HashMap<>();
    public Set<AnswerIndex> answerCache = new HashSet<>();
    
    class AnswerIndex {
        public AnswerIndex( long z, long modelNumber ) {
            this.z = z;
            this.modelNumber = modelNumber;
        }
    
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            AnswerIndex that = (AnswerIndex) o;
            return z == that.z && modelNumber == that.modelNumber;
        }
    
        @Override
        public int hashCode() {
            return Objects.hash( z, modelNumber );
        }
    
        long z;
        long modelNumber;
    }
    
    static int runCalculation = 0;
    static Map<Integer, List<Integer>> specialValuesList = new HashMap<>();
    static {
        specialValuesList.put( 14, List.of( 1, 12, 7 ) );
        specialValuesList.put( 13, List.of( 1, 13, 8 ) );
        specialValuesList.put( 12, List.of( 1, 13, 10 ) );
        specialValuesList.put( 11, List.of( 26, -2, 4 ) );
        specialValuesList.put( 10, List.of( 26, -10, 4 ) );
        specialValuesList.put( 9, List.of( 1, 13, 6 ) );
        specialValuesList.put( 8, List.of( 26, -14, 11 ) );
        specialValuesList.put( 7, List.of( 26, -5, 13 ) );
        specialValuesList.put( 6, List.of( 1, 15, 1 ) );
        specialValuesList.put( 5, List.of( 1, 15, 8 ) );
        specialValuesList.put( 4, List.of( 26, -14, 4 ) );
        specialValuesList.put( 3, List.of( 1, 10, 13 ) );
        specialValuesList.put( 2, List.of( 26, -14, 4 ) );
        specialValuesList.put( 1, List.of( 26, -5, 14 ) );
    }
    
    class Result {
        public Result( int w, int zIn, int zOut ) {
            this.w = w;
            this.zIn = zIn;
            this.zOut = zOut;
        }
    
        @Override
        public String toString() {
            return "Result{" + "w=" + w + ", zIn=" + zIn + ", zOut=" + zOut+ '}';
        }
    
        int w;
        int zIn;
        int zOut;
    }
    
    private void manualTest() {
        Set<Integer> targets = new HashSet<>();
        
        targets.add( 0 );
        Map<Integer,Set<Result>> digitsToResults = new HashMap<>();
        for (int digit = 1; digit <=14;digit++) {
            Set<Result> results = getZs( targets, digit );
            digitsToResults.put(digit, results);
            System.out.println("********************");
            results.forEach( System.out::println );
            targets = new HashSet<>();
            for (Result result: results){
                targets.add(result.zIn);
            }
        }
        
        for (int digit = 14;digit>=1;digit--) {
            for ( int i = 9; i >= 1; i-- ) {
                Set<Result> results = digitsToResults.get( digit );
            }
        }
        //now I've got a set of all candidate z values for each digit.  Work from the start with all ones that have zIn = 0 and work out
        //the most expensive path through.  I just did it by hand :-(
        System.out.println("size: " + targets.size());
    }
    private Set<Result> getZs( Set<Integer> targets, int digit ){
        Set<Result> zCandidates = new HashSet<>();
        for (int zCandidate = -100000; zCandidate <10000; zCandidate++){
            for (int w = 1; w<=9;w++) {
                int z = getZ( zCandidate, w, digit );
                if ( targets.contains( z ) ) {
                    zCandidates.add( new Result(w, zCandidate, z ));
                }
            }
        }
        return zCandidates;
    }
    
    private void manualTest2() {
        for (int w = 1; w<=9;w++) {
            int z = getZ( -2, w, 13 );
            System.out.println(w + ":" +z);
        }
    }
    
    private int getZ( int zCandidate, int w, int digit ) {
        int z = zCandidate;
        int x = z;
        x = x%26;
        z = z/specialValuesList.get(digit).get(0);
        x = x + specialValuesList.get(digit).get(1);
        x = (x== w )?1:0;
        x = (x==0)?1:0;
        int y=25;
        y = y * x;
        y = y + 1;
        z = z * y;
        y = w;
        y = y + specialValuesList.get(digit).get(2);
        y = x * y;
        z = z + y;
        return z;
    }
    
    private void part1() {
        ALU alu = new ALU();
        //        for (long modelNumber=99L;modelNumber>0L;modelNumber--){
        //        for (long modelNumber=999999999999999999L;modelNumber>11111111111111L;modelNumber--){
        //            if (!(modelNumber+"").contains( "0" )) {
        //                State state = alu.run( inputLines, modelNumber + "" );
        //                if (state.z==1){
        //                    System.out.println("Part 1: " + modelNumber+1);
        //                    return;
        //                }
        ////                if ( modelNumber % 9999L == 0 ) {
        //                    System.out.println( modelNumber + ": z: " + state.z );
        ////                }
        //            }
        //        }
        
        for ( long modelNumber = 79197919999985L; modelNumber > 11111111111111L; modelNumber-- ) {
            if ( !( modelNumber + "" ).contains( "0" ) ) {
                long z = 0;
                for ( int i = 14; i > 0; i-- ) {
                    AnswerIndex answer = new AnswerIndex(z,modelNumber % (long) Math.pow( 10, ( i ) ) );
                    if (answerCache.contains( answer )){
//                        System.out.println("Already worked out from here and it isn't 1!!!");
                        break;
                    }
                    answerCache.add( answer );
                    long divisor = (long) Math.pow( 10, ( i - 1 ) );
                    long w = ( modelNumber % (long) Math.pow( 10, ( i ) ) ) / divisor;
                    List<Integer> specialValues = specialValuesList.get( i );
                    z = getZ( new Parameters( w, z, specialValues.get( 0 ), specialValues.get( 1 ), specialValues.get( 2 ) ) );
                }
                if ( z == 0 ) {
                    System.out.println( "Part 1: " + modelNumber );
                    return;
                }
                if ( modelNumber % 999L == 0 ) {
                    System.out.println( answerCache.size() + "@" + modelNumber + ": z: " + z  );
                }
            }
        }
    }
    
    public long getZ( Parameters parameters ) {
        if ( !parametersToZMap.containsKey( parameters ) ) {
            System.out.println("Running calc for: " + parameters.toString() + ", count: " + ++runCalculation);
                long z = parameters.z;
                long x = z;
                x = x%26;
                z = z/parameters.divZ;
                x = x + parameters.addX;
                x = (x== parameters.w )?1:0;
                x = (x==0)?1:0;
                long y=25;
                y = y * x;
                y = y + 1;
                z = z * y;
                y = parameters.w;
                y = y + parameters.addY;
                y = x * y;
                z = z + y;
//                return z;
            parametersToZMap.put( parameters, z );
        }
        return parametersToZMap.get( parameters );
    }
}
    
    class Parameters {
        @Override
        public String toString() {
            return "Parameters{" + "w=" + w + ", z=" + z + ", divZ=" + divZ + ", addX=" + addX + ", addY=" + addY + '}';
        }
    
        long w;
        
        long z;
        
        int divZ;
        
        int addX;
        
        int addY;
        
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            Parameters that = (Parameters) o;
            return w == that.w && z == that.z && divZ == that.divZ && addX == that.addX && addY == that.addY;
        }
        
        @Override
        public int hashCode() {
//            return Objects.hash( w, z, divZ, addX, addY );
            return Objects.hash( w, z, (100 *divZ + 10 * addX + addY ));
        }
        
        public Parameters( long w, long z, int divZ, int addX, int addY ) {
            this.w = w;
            this.z = z;
            this.divZ = divZ;
            this.addX = addX;
            this.addY = addY;
        }
    }
    
    class State {
        long w;
        
        long x;
        
        long y;
        
        long z;
        
        enum Variable {w, x, y, z}
        
        ;//todo move all functionality within enum??
        
        public void set( String variable, Long value ) {
            Variable variableToSet = Variable.valueOf( variable );
            if ( variableToSet == Variable.w ) {
                w = value;
            }
            if ( variableToSet == Variable.x ) {
                x = value;
            }
            if ( variableToSet == Variable.y ) {
                y = value;
            }
            if ( variableToSet == Variable.z ) {
                z = value;
            }
        }
        
        public long get( String variable ) {
            Variable variableToGet = Variable.valueOf( variable );
            if ( variableToGet == Variable.w ) {
                return w;
            }
            if ( variableToGet == Variable.x ) {
                return x;
            }
            if ( variableToGet == Variable.y ) {
                return y;
            }
            return z;
        }
        
        public void print() {
            System.out.println( "W: " + w );
            System.out.println( "X: " + x );
            System.out.println( "Y: " + y );
            System.out.println( "Z: " + z );
            System.out.println( "------------------" );
        }
    }
    
    class ALU {
        enum Operation {
            INP, ADD, MUL, DIV, MOD, EQL;
            
            public void apply( State state, String operand1, String operand2 ) {
                if ( this == Operation.INP ) {
                    state.set( "w", Long.parseLong( operand2 ) );
                    state.set( "y", 25L );
                    state.set( "x", state.get( "z" ) % 26 );
                }
                else if ( this == Operation.ADD ) {
                    state.set( operand1, state.get( operand1 ) + getValueOfOperand( operand2, state ) );
                }
                else if ( this == Operation.MUL ) {
                    state.set( operand1, state.get( operand1 ) * getValueOfOperand( operand2, state ) );
                }
                else if ( this == Operation.DIV ) {
                    state.set( operand1, state.get( operand1 ) / getValueOfOperand( operand2, state ) );
                }
                else if ( this == Operation.MOD ) {
                    state.set( operand1, state.get( operand1 ) % getValueOfOperand( operand2, state ) );
                }
                else if ( this == Operation.EQL ) {
                    long result = state.get( operand1 ) == getValueOfOperand( operand2, state ) ? 1 : 0;
                    state.set( operand1, result );
                }
            }
            
            private long getValueOfOperand( String operand2, State state ) {
                try {
                    return Long.parseLong( operand2 );
                }
                catch ( NumberFormatException nfe ) {
                    return state.get( operand2 );
                }
            }
        }
        
        State state;
        
        public State run( List<String> program, String input ) {
            //        Pattern operationPattern = Pattern.compile( "([a-z]{3}) (w||x||y||z) (\\d||w||x||y||z)?" );
            Pattern operationPattern = Pattern.compile( "([a-z]{3}) (w|x|y|z)( )?(\\-?\\d|w|x|y|z)?" );
            
            Matcher instructionMatcher;
            int inputIndex = 0;
            state = new State();
            for ( String instruction : program ) {
                instructionMatcher = operationPattern.matcher( instruction );
                instructionMatcher.find();
                Operation operation = Operation.valueOf( instructionMatcher.group( 1 ).toUpperCase() );
                String operand1 = instructionMatcher.group( 2 );
                String operand2 = null != instructionMatcher.group( 4 ) ? instructionMatcher.group( 4 ) : Long.parseLong( input.charAt( inputIndex++ ) + "" ) + "";
                operation.apply( state, operand1, operand2 );
            }
            return state;
        }
    }
