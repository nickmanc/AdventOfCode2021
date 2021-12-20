package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 extends AoCSolution {
    
    public static final int LITERAL_TYPE = 4;
    public static final int LITERAL_CONTENT_BLOCK_SIZE = 5;
    public static final int VERSION_BIT_LENGTH = 3;
    public static final int TYPE_BIT_LENGTH = 3;
    public static final int LENGTH_TYPE_BIT_LENGTH = 1;
    public static final int SUBPACKET_COUNT_BIT_LENGTH = 11;
    public static final int SUBPACKET_LENGTH_BIT_LENGTH = 15;
    int pointer = 0;
    
    public static void main( String[] args ) {
        Day16 day16 = new Day16();
        day16.getInput();
        day16.part1();
        day16 = new Day16();
        day16.getInput();
        day16.part2();
    }
    
    protected int part1() {
        int result = parsePacket( getPacketInBits( inputLines.get( 0 ) ) ).sumOfVersionNumbers();
        System.out.println( "Part 1: " + result );
        return result;
    }
    
    protected long part2() {
        long result = parsePacket( getPacketInBits( inputLines.get( 0 ) ) ).getValue();
        System.out.println( "Part 2: " + result );
        return result;
    }
    
    protected String getPacketInBits( String packetString ) {
        return packetString.chars().mapToObj( c -> toPaddedBit( (char) c ) ).collect( Collectors.joining() );
    }
    
    private String toPaddedBit( char c ) {
        return String.format( "%4s", Long.toBinaryString( Long.parseLong( c + "", 16 ) ) ).replace( " ", "0" );
    }
    
    public Packet parsePacket( String bitString ) {
        Packet packet;
        int version = getInteger( bitString, VERSION_BIT_LENGTH );
        int type = getInteger( bitString, TYPE_BIT_LENGTH );
        if ( type == LITERAL_TYPE ) {
            packet = extractLiteral( version, bitString );
        }
        else {
            int operatorLengthTypeId = getInteger( bitString, LENGTH_TYPE_BIT_LENGTH );
            packet = new OperatorPacket( version, type );
            if ( operatorLengthTypeId == 1 ) {//has defined number of subpackets
                int numberOfSubPackets = getInteger( bitString, SUBPACKET_COUNT_BIT_LENGTH );
                int subPacketsAdded = 0;
                while ( subPacketsAdded++ < numberOfSubPackets ) {
                    packet.subPackets.add( parsePacket( bitString ) );
                }
            }
            else {//has defined subpacket length
                int subPacketLength = getInteger( bitString, SUBPACKET_LENGTH_BIT_LENGTH );
                int target = pointer + subPacketLength;
                while ( pointer < target ) {
                    packet.subPackets.add( parsePacket( bitString ) );
                }
            }
        }
        return packet;
    }
    
    private int getInteger( String bits, int intLength ) {
        return Integer.parseInt( bits.substring( pointer, pointer += intLength ), 2 );
    }
    
    private Packet extractLiteral( int version, String bitString ) {
        StringBuilder content = new StringBuilder();
        while ( bitString.charAt( pointer ) != '0' ) {
            content.append( bitString, pointer + 1, pointer + LITERAL_CONTENT_BLOCK_SIZE );
            pointer += LITERAL_CONTENT_BLOCK_SIZE;
        }
        content.append( bitString, pointer + 1, pointer + LITERAL_CONTENT_BLOCK_SIZE );
        pointer += LITERAL_CONTENT_BLOCK_SIZE;
        return new LiteralPacket( version, content.toString() );
    }
}

class Packet {
    int version;
    int type;
    long value;
    List<Packet> subPackets = new ArrayList<>();
    
    Packet( int version, int type ) {
        this.version = version;
        this.type = type;
    }
    
    public long getValue() {
        return this.value;
    }
    
    public int sumOfVersionNumbers() {
        return this.version + subPackets.stream().mapToInt( Packet::sumOfVersionNumbers ).sum();
    }
}

class LiteralPacket extends Packet {
    LiteralPacket( int version, String content ) {
        super( version, 4 );
        this.value = Long.parseLong( content, 2 );
    }
}

class OperatorPacket extends Packet {
    OperatorPacket( int version, int type ) {
        super( version, type );
    }
    
    @Override
    public long getValue() {
        switch ( type ) {
            case 0:
                return subPackets.stream().mapToLong( Packet::getValue ).sum();
            case 1:
                return subPackets.stream().mapToLong( Packet::getValue ).reduce( 1, ( a, b ) -> a * b );
            case 2:
                return subPackets.stream().mapToLong( Packet::getValue ).reduce( Long.MAX_VALUE, Math::min );
            case 3:
                return subPackets.stream().mapToLong( Packet::getValue ).reduce( Long.MIN_VALUE, Math::max );
            case 5:
                return subPackets.get( 0 ).getValue() > subPackets.get( 1 ).getValue() ? 1 : 0;
            case 6:
                return subPackets.get( 0 ).getValue() < subPackets.get( 1 ).getValue() ? 1 : 0;
            case 7:
                return subPackets.get( 0 ).getValue() == subPackets.get( 1 ).getValue() ? 1 : 0;
            default:
                throw new RuntimeException( "Invalid operation type: " + type );
        }
    }
}