package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 extends AoCSolution {
    
    public static final int LITERAL_TYPE = 4;
    
    public static final int LITERAL_CONTENT_START_CHARACTER = 6;
    
    public static final int LITERAL_CONTENT_BLOCK_SIZE = 5;
    
    public static void main( String[] args ) {
        Day16 day16 = new Day16();
        day16.getInput();
        day16.part1();
        //        day15.part2();
    }
    
    private void part1() {
        String input = inputLines.get(0);
        String inputInBits = getPacketInBits( input );
        System.out.println("Part 1: " + getVersionSum( inputInBits));
    }
    
    protected static String getPacketInBits( String packetString ) {
        String result =  packetString.chars().mapToObj( c -> toPaddedBit( (char) c ) ).collect( Collectors.joining() );
        return result;
    }
    
    private static String toPaddedBit( char c ) {
        return String.format( "%4s", Long.toBinaryString( Long.parseLong( c + "", 16 ) ) ).replace( " ", "0" );
    }
    
    public long sumVersionNumbers( List<Packet> packets ) {
        return packets.stream().mapToInt( p -> p.getVersion() ).sum();
    }
    
    public long getVersionSum(String bitsString){
        List<Packet> packets = parsePackets( bitsString );
        return sumVersionNumbers(packets);
    }
    
    public List<Packet> parsePackets( String bitsString ) {
        List<Packet> packets = new ArrayList<>();
        StringBuilder bits  = new StringBuilder(bitsString);
        return parsePackets( bits,packets );
    }
    public List<Packet> parsePackets( StringBuilder bits, List<Packet> packets ) {
        while (bits.length() > 11) {//smallest value for a literal is length 11
            int version = Integer.parseInt( bits.substring( 0, 3 ), 2 );
            int type = Integer.parseInt( bits.substring( 3, 6 ), 2 );
            if ( type == LITERAL_TYPE ) {
                extractLiteral( bits, packets );
            }
            else {
                int operatorLengthTypeId = Integer.parseInt( bits.substring( 6, 7 ), 2 );
                OperatorPacket operatorPacket;
                if (operatorLengthTypeId == 1){//has defined number of subpackets
                    operatorPacket = new OperatorPacketLengthType1( bits.toString()  );
                    packets.add( operatorPacket );
                    bits.delete( 0,18 );
                }
                else {
                    operatorPacket = new OperatorPacketLengthType0( bits.toString()  );
                    packets.add( operatorPacket );
                    bits.delete( 0,22 );
                }
                parsePackets(bits, packets);
                operatorPacket.subPackets = new ArrayList<>(packets);
                
            }
        }
        return packets;
    }
    
    private void extractLiteral( StringBuilder bits, List<Packet> packets ) {
        String content = "";
        for ( int i = LITERAL_CONTENT_START_CHARACTER; i < bits.length(); i += LITERAL_CONTENT_BLOCK_SIZE ) {
            content += bits.substring( i + 1, i + LITERAL_CONTENT_BLOCK_SIZE );
            if ( bits.substring( i, i + 1 ).equals( "0" ) ) {
                packets.add( new LiteralPacket( bits.substring( 0, i + LITERAL_CONTENT_BLOCK_SIZE ) ) );
                bits.delete( 0, i + LITERAL_CONTENT_BLOCK_SIZE);
                break;
            }
        }
    }
}

class Packet {
    String packetInBits;
    
    int version;
    
    int type;
    
    public int getVersion() {
        return this.version;
    }
    
    Packet( String packetInBits ) {
        this.packetInBits = packetInBits;
        version = Integer.parseInt( packetInBits.substring( 0, 3 ), 2 );
        type = Integer.parseInt( packetInBits.substring( 3, 6 ), 2 );
    }
    
    @Override
    public String toString() {
        return "Packet{" + "version=" + version + ", type=" + type + '}';
    }
    
    public boolean isOperator() {
        return type != 4;
    }
    
    public long getValue(){return -1L;};
}

class LiteralPacket extends Packet {
    long value;
    
    LiteralPacket( String packetInBits ) {
        super( packetInBits );
        value = getValue( packetInBits.substring( 6 ) );
    }
    
    private long getValue( String valueString ) {
        String decodedValueString = "";
        for ( int i = 0; i < valueString.length(); i += 5 ) {
            decodedValueString += valueString.substring( i + 1, i + 5 );
            if ( valueString.substring( i, i + 1 ).equals( "0" ) ) {
                break;
            }
        }
        return Long.parseLong( decodedValueString, 2 );
    }
    
    public long getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return "LiteralPacket{" + "version=" + version + ", type=" + type + ", value=" + value + '}';
    }
}

class OperatorPacket extends Packet {
    int lengthTypeId;
    List<Packet> subPackets;
    
    OperatorPacket( String packetInBits ) {
        super( packetInBits );
        lengthTypeId = Integer.parseInt( packetInBits.substring( 6, 7 ), 2 );
    }
    
    @Override
    public String toString() {
        return "OperatorPacket{" + "version=" + version + ", type=" + type + ", lengthTypeId=" + lengthTypeId + '}';
    }
    
    public boolean isType0() {
        return lengthTypeId == 0;
    }
    
    @Override
    public long getValue() {
        return super.getValue();
    }
}

class OperatorPacketLengthType0 extends OperatorPacket {
    int lengthInBits;
    
    String subPackets;
    
    OperatorPacketLengthType0( String packetInBits ) {
        super( packetInBits );
        lengthInBits = Integer.parseInt( packetInBits.substring( 7, 22 ), 2 );
        subPackets = packetInBits.substring( 22 );
    }
    
    @Override
    public String toString() {
        return "OperatorPacketLengthType0{" + "version=" + version + ", type=" + type + ", lengthTypeId=" + lengthTypeId + ", lengthInBits=" + lengthInBits + ", subPackets='" + subPackets + '\'' + '}';
    }
}

class OperatorPacketLengthType1 extends OperatorPacket {
    int numberOfSubOperations;
    
    String subPackets;
    
    OperatorPacketLengthType1( String packetInBits ) {
        super( packetInBits );
        numberOfSubOperations = Integer.parseInt( packetInBits.substring( 7, 18 ), 2 );
        subPackets = packetInBits.substring( 18 );
    }
    
    @Override
    public String toString() {
        return "OperatorPacketLengthType1{" + "version=" + version + ", type=" + type + ", lengthTypeId=" + lengthTypeId + ", numberOfSubPackets=" + numberOfSubOperations + ", subPackets='" + subPackets + '\'' + '}';
    }
}