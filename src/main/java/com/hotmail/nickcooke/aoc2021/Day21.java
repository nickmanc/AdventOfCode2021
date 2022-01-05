package com.hotmail.nickcooke.aoc2021;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Day21 extends AoCSolution {
    
    public static final int P1_START_SPACE = 6;
    
    public static final int P2_START_SPACE = 10;
    
    protected void part1() {
        DeterministicDie die = new DeterministicDie();
        final int PART_1_WINNING_SCORE = 1_000;
        int p1CurrentSpace = P1_START_SPACE;
        int p2CurrentSpace = P2_START_SPACE;
        int p1CurrentScore = 0;
        int p2CurrentScore = 0;
        while ( p2CurrentScore < PART_1_WINNING_SCORE ) {
            p1CurrentSpace = makeMove( p1CurrentSpace, die );
            p1CurrentScore += p1CurrentSpace;
            if ( p1CurrentScore >= PART_1_WINNING_SCORE ) {
                break;
            }
            p2CurrentSpace = makeMove( p2CurrentSpace, die );
            p2CurrentScore += p2CurrentSpace;
        }
        System.out.println( "Part 1: " + Math.min( p1CurrentScore, p2CurrentScore ) * die.getRollCount() );
    }
    
    private int makeMove( int p1CurrentSpace, DeterministicDie die ) {
        return ( ( p1CurrentSpace + die.rollThree() - 1 ) % 10 ) + 1;
    }
    
    protected void part2() {
        Pawn player1 = new Pawn( ( P1_START_SPACE ) );
        Pawn player2 = new Pawn( ( P2_START_SPACE ) );
        GameState gs = new GameState( player1, player2, true );
        Game game = new Game( gs );
        Result result = game.playGame();
        System.out.println( "Part 2: " + Math.max( result.p1Wins, result.p2Wins ));
    }
    
    public static void main( String[] args ) {
        Day21 day21 = new Day21();
        day21.part1();
        day21.part2();
    }
}

class Game {
    public static final int WINNING_SCORE_PART_2 = 21;
    
    private static final Map<GameState, Result> gameStateCache = new HashMap<>();
    
    private static final Map<Integer, Integer> dicePermutations;
    
    static {
        dicePermutations = new HashMap<>();
        for ( int d1 = 1; d1 < 4; d1++ ) {
            for ( int d2 = 1; d2 < 4; d2++ ) {
                for ( int d3 = 1; d3 < 4; d3++ ) {
                    dicePermutations.merge( d1 + d2 + d3, 1, Integer::sum );
                }
            }
        }
    }
    
    GameState currentState;
    
    public Game( GameState gameState ) {
        this.currentState = gameState;
    }
    
    protected Result playGame() {
        if ( !gameStateCache.containsKey( currentState ) ) {
            if ( currentState.player1.score >= WINNING_SCORE_PART_2 ) {
                gameStateCache.put( currentState, new Result( 1, 0 ) );
            }
            else if ( currentState.player2.score >= WINNING_SCORE_PART_2 ) {
                gameStateCache.put( currentState, new Result( 0, 1 ) );
            }
            else {
                Result result = new Result( 0, 0 );
                for ( Map.Entry<Integer, Integer> permutation : dicePermutations.entrySet() ) {
                    Pawn newGamePlayer1 = new Pawn(currentState.player1);
                    Pawn newGamePlayer2 = new Pawn(currentState.player2);
                    Pawn currentPlayer = currentState.p1Turn?newGamePlayer1:newGamePlayer2;
                    currentPlayer.move( permutation.getKey() );
                    GameState newGameState = new GameState( newGamePlayer1,newGamePlayer2, !currentState.p1Turn );
                    Game newGame = new Game( newGameState );
                    for ( int i = 1; i <= permutation.getValue(); i++ ) {
                        result.add( newGame.playGame() );
                    }
                }
                gameStateCache.put( currentState, result );
                gameStateCache.put( new GameState( currentState.player2, currentState.player1, !currentState.p1Turn ), result.flip() );
            }
        }
        return gameStateCache.get( currentState );
    }
}

class Result {
    
    long p1Wins;
    
    long p2Wins;
    
    public Result( long p1Wins, long p2Wins ) {
        this.p1Wins = p1Wins;
        this.p2Wins = p2Wins;
    }
    
    public void add( Result other ) {
        this.p1Wins += other.p1Wins;
        this.p2Wins += other.p2Wins;
    }
    
    public Result flip() {
        return new Result( this.p2Wins, this.p1Wins );
    }
    
    @Override
    public String toString() {
        return "Result{" + "p1Wins=" + p1Wins + ", p2Wins=" + p2Wins + '}';
    }
}

class GameState {
    
    Pawn player1;
    
    Pawn player2;
    
    boolean p1Turn;
    
    public GameState( Pawn player1, Pawn player2, boolean p1Turn ) {
        this.player1 = player1;
        this.player2 = player2;
        this.p1Turn = p1Turn;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        GameState gameState = (GameState) o;
        return p1Turn == gameState.p1Turn && Objects.equals( player1, gameState.player1 ) && Objects.equals( player2, gameState.player2 );
    }
    
    @Override
    public int hashCode() {
        return Objects.hash( player1, player2, p1Turn );
    }
}

class Pawn {
    int score;
    
    int space;
    
    public Pawn( int startingSpace ) {
        this.space = startingSpace;
    }
    
    public void move( int roll ) {
        space = ( ( space + roll - 1 ) % 10 ) + 1;
        score += space;
    }
    
    public Pawn (Pawn pawnToCopy){
        this.space = pawnToCopy.space;
        this.score = pawnToCopy.score;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Pawn pawn = (Pawn) o;
        return score == pawn.score && space == pawn.space;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash( score, space );
    }
}

class DeterministicDie {
    int rollCount = 0;
    
    private int roll() {
        return rollCount++ % 100 + 1;
    }
    
    public int rollThree() {
        int roll1 = roll();
        int roll2 = roll();
        int roll3 = roll();
        return roll1 + roll2 + roll3;
    }
    
    public int getRollCount() {
        return rollCount;
    }
}

