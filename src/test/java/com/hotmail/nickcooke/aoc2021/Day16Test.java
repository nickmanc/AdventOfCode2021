package com.hotmail.nickcooke.aoc2021;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class Day16Test {
    
    @Test
    public void part1IsCorrect() {
        Day16 day16 = new Day16();
        day16.getInput();
        assertEquals( 991, day16.part1() );
    }
    
    @Test
    public void part2IsCorrect() {
        Day16 day16 = new Day16();
        day16.getInput();
        assertEquals( 1264485568252L, day16.part2() );
    }
    
    @Test
    public void whenPacketIsLiteral() {
        Day16 day16 = new Day16();
        String input = "110100101111111000101";
        Packet packet = day16.parsePacket( input );
        LiteralPacket literalPacket = (LiteralPacket) packet;
        assertEquals( 6, literalPacket.version );
        assertEquals( 2021, literalPacket.getValue() );
    }
    
    @Test
    public void whenPacketIsOperatorType1ContainingLiterals_thenVersionNumberReturned() {
        Day16 day16 = new Day16();
        String operatorContainingLiteralString = "11101110000000001101010000001100100000100011000001100000";
        Packet packet = day16.parsePacket( operatorContainingLiteralString );
        assertTrue( packet instanceof OperatorPacket );
        assertEquals( 7, packet.version );
        assertEquals( 3, packet.subPackets.size() );
        LiteralPacket literalPacket = (LiteralPacket) packet.subPackets.get( 0 );
        assertEquals( 2, literalPacket.version );
        assertEquals( 1, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packet.subPackets.get( 1 );
        assertEquals( 4, literalPacket.version );
        assertEquals( 2, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packet.subPackets.get( 2 );
        assertEquals( 1, literalPacket.version );
        assertEquals( 3, literalPacket.getValue() );
    }
    
    @Test
    public void whenPacketIsOperatorType0ContainingLiterals_thenVersionNumberReturned() {
        Day16 day16 = new Day16();
        String operatorContainingLiteralString = "00111000000000000110111101000101001010010001001000000000";
        Packet packet = day16.parsePacket( operatorContainingLiteralString );
        assertTrue( packet instanceof OperatorPacket );
        assertEquals( 2, packet.subPackets.size() );
        assertEquals( 1, packet.version );
        LiteralPacket literalPacket = (LiteralPacket) packet.subPackets.get( 0 );
        assertEquals( 6, literalPacket.version );
        assertEquals( 10, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packet.subPackets.get( 1 );
        assertEquals( 2, literalPacket.version );
        assertEquals( 20, literalPacket.getValue() );
    }
    
    @Test
    public void testExamples() {
        Day16 day16 = new Day16();
        String input = "D2FE28";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 6, packet.version );
        assertEquals( 4, packet.type );
        assertTrue( packet instanceof LiteralPacket );
        assertEquals( 2021, ( (LiteralPacket) packet ).value );
        
        day16 = new Day16();
        input = "38006F45291200";
        packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 2, packet.subPackets.size() );
        assertEquals( 1, packet.version );
        assertEquals( 6, packet.type );
        Packet subPacket = packet.subPackets.get( 0 );
        assertEquals( 10, ( (LiteralPacket) subPacket ).value );
        subPacket = packet.subPackets.get( 1 );
        assertEquals( 20, ( (LiteralPacket) subPacket ).value );
        
        day16 = new Day16();
        input = "EE00D40C823060";
        packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 3, packet.subPackets.size() );
        assertEquals( 7, packet.version );
        assertEquals( 3, packet.type );
        subPacket = packet.subPackets.get( 0 );
        assertEquals( 1, ( (LiteralPacket) subPacket ).value );
        subPacket = packet.subPackets.get( 1 );
        assertEquals( 2, ( (LiteralPacket) subPacket ).value );
        subPacket = packet.subPackets.get( 2 );
        assertEquals( 3, ( (LiteralPacket) subPacket ).value );
    }
    
    @Test
    public void testVersionSums() {
        Day16 day16 = new Day16();
        String input = "8A004A801A8002F478";
        long versionSum =  day16.parsePacket( day16.getPacketInBits(input) ).sumOfVersionNumbers();
        assertEquals( 16, versionSum );
        
        day16 = new Day16();
        input = "620080001611562C8802118E34";
        versionSum =  day16.parsePacket( day16.getPacketInBits(input) ).sumOfVersionNumbers();
        assertEquals( 12, versionSum );
        
        day16 = new Day16();
        input = "C0015000016115A2E0802F182340";
        versionSum =  day16.parsePacket( day16.getPacketInBits(input) ).sumOfVersionNumbers();
        assertEquals( 23, versionSum );
        
        day16 = new Day16();
        input = "A0016C880162017C3686B18A3D4780";
        versionSum =  day16.parsePacket( day16.getPacketInBits(input) ).sumOfVersionNumbers();
        assertEquals( 31, versionSum );
    }
    
    @Test
    public void whenPacketIsLiteral_thenValueIsReturned() {
        Day16 day16 = new Day16();
        String input = "110100101111111000101000";
        Packet packet = day16.parsePacket( input );
        assertEquals( 2021, packet.getValue() );
    }
    
    @Test
    public void whenPacketIsSumOperator() {
        Day16 day16 = new Day16();
        String input = "C200B40A82";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 3, packet.getValue() );
    }
    
    @Test
    public void whenPacketIsProductOperator() {
        Day16 day16 = new Day16();
        String input = "04005AC33890";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 54, packet.getValue() );
    }
    
    @Test
    public void whenPacketIsMinOperator() {
        Day16 day16 = new Day16();
        String input = "880086C3E88112";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 7, packet.getValue() );
    }
    
    @Test
    public void whenPacketIsMaxOperator() {
        Day16 day16 = new Day16();
        String input = "CE00C43D881120";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 9, packet.getValue() );
    }
    
    @Test
    public void whenPacketIsGreaterThanOperator() {
        Day16 day16 = new Day16();
        String input = "D8005AC2A8F0";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 1, packet.getValue() );
    }
    
    @Test
    public void whenPacketIsLessThanOperator() {
        Day16 day16 = new Day16();
        String input = "F600BC2D8F";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 0, packet.getValue() );
    }
    
    @Test
    public void whenPacketIsEqualsOperator() {
        Day16 day16 = new Day16();
        String input = "9C005AC2F8F0";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 0, packet.getValue() );
    }
    
    @Test
    public void whenNestedOperators() {
        Day16 day16 = new Day16();
        String input = "9C0141080250320F1802104A08";
        input = "9C01610001650320F10016104A08";
        input = "38006F45291200";
        Packet packet = day16.parsePacket( day16.getPacketInBits( input ) );
        assertEquals( 1, packet.getValue() );
    }
    
    @Test
    public void testAmbiguousNestingSubPacketCount() {
        Day16 day16 = new Day16();
        String inputBits = "00100010000000001000100110000000001100110000001001100000100011000001100110000100";
        Packet packet = day16.parsePacket( inputBits );
        assertEquals( 10, packet.getValue() );
    }
    
    @Test
    public void testAmbiguousNestingSubPacketLength() {
        Day16 day16 = new Day16();
        String inputBits = "0010000000000001000010001001000000000010000100110000001001100000100011000001100110000111";
        Packet packet = day16.parsePacket( inputBits );
        assertEquals( 13, packet.getValue() );
    }
    
    @Test
    public void testAmbiguousNestingSubPacketLength2() {
        Day16 day16 = new Day16();
        String inputBits = "0010000000000001000010001001000000000010000100110000001001100000100011000001100110000111";
        Packet packet = day16.parsePacket( inputBits );
        assertEquals( 13, packet.getValue() );
    }
}