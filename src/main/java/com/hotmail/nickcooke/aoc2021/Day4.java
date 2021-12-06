package com.hotmail.nickcooke.aoc2021;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day4 extends AoCSolution {
    
    private int[] calledNumbers;
    
    public static void main( String[] args ) {
        Day4 day4 = new Day4();
        day4.getInput();
        day4.part1();
        day4.part2();
    }
    
    private void part1() {
        getCalledNumbers();
        Set<BingoBoard> bingoBoards = loadBingoBoards(inputLines);       
        System.out.println("Part 1:" + getWinningScore(bingoBoards));
    }
    
    private void part2() {
        Set<BingoBoard> bingoBoards = loadBingoBoards( inputLines );
        BingoBoard winningBoard = null;
        Set<BingoBoard> lastWinningBoards= new HashSet<>();
        int lastNumberCalled=-1;
        for (int numberCalled : calledNumbers){
            lastNumberCalled = numberCalled;
            for (BingoBoard board: bingoBoards){
                if (board.checkForBingo(numberCalled)){
                    lastWinningBoards.add(board);
                };
            }
            if (lastWinningBoards.size()>0){
                bingoBoards.removeAll( lastWinningBoards );
                if (bingoBoards.size()==0){
                    break;
                }
                else {
                    lastWinningBoards = new HashSet<>();
                }
            }
        }
        BingoBoard lastWinningBoard = lastWinningBoards.iterator().next();
        if (null != lastWinningBoard){
            System.out.println("Part 2:" + lastWinningBoard.getBoardScore() * lastNumberCalled);
        }
    }
    
    private void getCalledNumbers() {
        calledNumbers = Arrays.stream(inputLines.get( 0 ).split( "," )).mapToInt(Integer::parseInt).toArray();
        inputLines.remove( 0 );//don't need calledNumbers again
        inputLines.remove( 0 );//ignore first blank line
    }
    
    private int getWinningScore(Set<BingoBoard> bingoBoards) {
        int lastNumberCalled=-1;
        numberCalledLoop: for (int numberCalled : calledNumbers ){
            lastNumberCalled = numberCalled;
            for (BingoBoard board: bingoBoards){
                if (board.checkForBingo(numberCalled)){
                    return board.getBoardScore() * lastNumberCalled;
                };
            }
        }
        throw new RuntimeException("No winning board");
    }
    
    private Set<BingoBoard> loadBingoBoards( List<String> inputLines ) {
        Set<BingoBoard> bingoBoards = new HashSet<>();
        int[][] currentBoard = currentBoard = new int[5][5]; ;
        int boardRow = 0;
        for (String line : inputLines){
            if (line.trim().equals( "" )){
                bingoBoards.add( new BingoBoard (currentBoard) );
                boardRow = 0;
                currentBoard = new int[5][5];
            }
            else {
                currentBoard[boardRow] = Arrays.stream( line.trim().split( "\\s+" ) ).mapToInt( Integer::parseInt ).toArray();
                boardRow++;
            }
        }
        bingoBoards.add( new BingoBoard (currentBoard) );//add the final board
        return bingoBoards;
    }
    
    private void printBoards(Set<BingoBoard> boards){
        for(BingoBoard board:boards){
            board.printCurrentState();
        }
    }
}

class BingoBoard {
    int [][] positions;
    
    public BingoBoard(int[][] numbers){
        positions = numbers;
    }
    
    public boolean checkForBingo(int calledNumber){
        for (int i = 0; i <positions.length;i++) {
            for ( int j = 0; j < positions.length; j++ ) {
                if (positions[i][j] == calledNumber) {
                    positions[i][j] = -1;
                }
            }
        }
        return checkForBingo() ;
    }
    
    public int getBoardScore(){
        int total = 0;
        for (int i = 0; i <positions.length;i++) {
            for ( int j = 0; j < positions.length; j++ ) {
                if (positions[i][j] != -1) {
                    total += positions[i][j];
                }
            }
        }
        return total;
    }
    
    protected boolean checkForBingo() {
        for (int i=0; i < positions.length; i++){
            boolean foundBingo = true;
            for ( int j = 0; j < positions.length; j++ ) {
                if (positions[i][j] != -1) {
                    foundBingo = false;
                    break;
                }
            }
            if (foundBingo) {
                return true;
            }
        }
        for (int i=0; i < positions.length; i++){
            boolean foundBingo = true;
            for ( int j = 0; j < positions.length; j++ ) {
                if (positions[j][i] != -1) {
                    foundBingo = false;
                    break;
                }
            }
            if (foundBingo) {
                return true;
            }
        }
        return false;
    }
    
    public void printCurrentState() {
        for (int i=0; i < positions.length; i++) {
            for ( int j = 0; j < positions.length; j++ ) {
                System.out.print(positions[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}