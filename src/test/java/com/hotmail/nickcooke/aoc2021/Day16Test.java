package com.hotmail.nickcooke.aoc2021;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class Day16Test {
    
    @Test
    public void whenPacketIsLiteral() {
        Day16 day16 = new Day16();
        String input = "110100101111111000101000";
        List<Packet> packets = day16.parsePackets( input );
        LiteralPacket literalPacket = (LiteralPacket) packets.get( 0 );
        assertEquals( 1, packets.size() );
        assertEquals( 6, literalPacket.version );
        assertEquals( 2021, literalPacket.getValue() );
    }
    
    @Test
    public void whenPacketIsTwoLiterals() {
        Day16 day16 = new Day16();
        String input = "110100101111111000101110100101111111000110";
        List<Packet> packets = day16.parsePackets( input );
        assertEquals( 2, packets.size() );
        LiteralPacket literalPacket = (LiteralPacket) packets.get( 0 );
        assertEquals( 6, literalPacket.version );
        assertEquals( 2021, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packets.get( 1 );
        assertEquals( 6, literalPacket.version );
        assertEquals( 2022, literalPacket.getValue() );
    }
    
    @Test
    public void whenPacketIsThreeLiterals() {
        Day16 day16 = new Day16();
        String input = "110100101111111000101110100101111111000110110100101111111000111";
        List<Packet> packets = day16.parsePackets( input );
        assertEquals( 3, packets.size() );
        LiteralPacket literalPacket = (LiteralPacket) packets.get( 0 );
        assertEquals( 6, literalPacket.version );
        assertEquals( 2021, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packets.get( 1 );
        assertEquals( 6, literalPacket.version );
        assertEquals( 2022, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packets.get( 2 );
        assertEquals( 6, literalPacket.version );
        assertEquals( 2023, literalPacket.getValue() );
    }

    @Test
    public void whenPacketIsOperatorType1ContainingLiterals_thenVersionNumberReturned(){
        Day16 day16 = new Day16();
        String operatorContainingLiteralString = "11101110000000001101010000001100100000100011000001100000";
        List<Packet> packets = day16.parsePackets(operatorContainingLiteralString);
        assertEquals(4, packets.size());
        assertTrue(packets.get(0).isOperator());
        assertEquals(7, packets.get(0).version);
        LiteralPacket literalPacket = (LiteralPacket) packets.get( 1 );
        assertEquals( 2, literalPacket.version );
        assertEquals( 1, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packets.get( 2 );
        assertEquals( 4, literalPacket.version );
        assertEquals( 2, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packets.get( 3 );
        assertEquals( 1, literalPacket.version );
        assertEquals( 3, literalPacket.getValue() );
    }
    
    @Test
    public void whenPacketIsOperatorType0ContainingLiterals_thenVersionNumberReturned(){
        Day16 day16 = new Day16();
        String operatorContainingLiteralString = "00111000000000000110111101000101001010010001001000000000";
        List<Packet> packets = day16.parsePackets(operatorContainingLiteralString);
        assertEquals(3, packets.size());
        assertTrue(packets.get(0).isOperator());
        assertEquals(1, packets.get(0).version);
        LiteralPacket literalPacket = (LiteralPacket) packets.get( 1 );
        assertEquals( 6, literalPacket.version );
        assertEquals( 10, literalPacket.getValue() );
        literalPacket = (LiteralPacket) packets.get( 2 );
        assertEquals( 2, literalPacket.version );
        assertEquals( 20, literalPacket.getValue() );
    }
    
    @Test
    public void testExamples(){
        Day16 day16 = new Day16();
        String input = "D2FE28";
        List<Packet> packets = day16.parsePackets( Day16.getPacketInBits( input) );
        assertEquals( 1, packets.size() );
        Packet packet = packets.get(0);
        assertEquals( 6, packet.getVersion() );
        assertEquals( 4, packet.type );
        assertTrue( packet instanceof LiteralPacket );
        assertEquals( 2021, ((LiteralPacket)packet).value );
    
        input = "38006F45291200";
        packets = day16.parsePackets( Day16.getPacketInBits( input) );
        assertEquals( 3, packets.size() );
        packet = packets.get(0);
        assertEquals( 1, packet.getVersion() );
        assertEquals( 6, packet.type );
        packet = packets.get(1);
        assertEquals( 10, ((LiteralPacket)packet).value );
        packet = packets.get(2);
        assertEquals( 20, ((LiteralPacket)packet).value );
    
        input = "EE00D40C823060";
        packets = day16.parsePackets( Day16.getPacketInBits( input) );
        assertEquals( 4, packets.size() );
        packet = packets.get(0);
        assertEquals( 7, packet.getVersion() );
        assertEquals( 3, packet.type );
        packet = packets.get(1);
        assertEquals( 1, ((LiteralPacket)packet).value );
        packet = packets.get(2);
        assertEquals( 2, ((LiteralPacket)packet).value );
        packet = packets.get(3);
        assertEquals( 3, ((LiteralPacket)packet).value );
    }
    
    @Test
    public void testVersionSums(){
        Day16 day16 = new Day16();
        String input = "8A004A801A8002F478";
        long versionSum = day16.getVersionSum( Day16.getPacketInBits( input) );
        assertEquals( 16, versionSum );
    
        input = "620080001611562C8802118E34";
        versionSum = day16.getVersionSum( Day16.getPacketInBits( input) );
        assertEquals( 12, versionSum );
    
        input = "C0015000016115A2E0802F182340";
        versionSum = day16.getVersionSum( Day16.getPacketInBits( input) );
        assertEquals( 23, versionSum );
    
        input = "A0016C880162017C3686B18A3D4780";
        versionSum = day16.getVersionSum( Day16.getPacketInBits( input) );
        assertEquals( 31, versionSum );
    }
    
    
    @Test
    public void whenPacketIsLiteral_thenValueIsReturned() {
        Day16 day16 = new Day16();
        String input = "110100101111111000101000";
        List<Packet> packets = day16.parsePackets( input );
        Packet packet = packets.get( 0 );
        assertEquals( 2021, packet.getValue() );
    }
    
//    @Test
//    public void whenPacketIsSumOperator_thenValueIsReturned() {
//        Day16 day16 = new Day16();
//        String input = "C200B40A82";
//        List<Packet> packets = day16.parsePackets( Day16.getPacketInBits( input) );
//        Packet packet = packets.get( 0 );
//        assertEquals( 3, packet.getValue() );
//    }
}