package com.hotmail.nickcooke.aoc2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day19 extends AoCSolution {
    
    public static final int MINIMUM_MATCHING_DISTANCES = 66;
    
    public static void main( String[] args ) {
        Day19 day19 = new Day19();
        day19.getInput();
        day19.solve();
    }
    
    private void solve() {
        Map<Integer, Scanner> scanners = getScannerFromInput();
        while ( scanners.size() > 1 ) {
            scanners = combineScanners( scanners );
        }
        Scanner fullMap = scanners.get( 0 );
        System.out.println( "Part 1: " + fullMap.currentBeacons.size() );
        int maxManhattanDistance = getMaxManhattanDistance( fullMap.subScanners );
        System.out.println( "Part 2: " + maxManhattanDistance );
    }
    
    private Map<Integer, Scanner> combineScanners( Map<Integer, Scanner> scanners ) {
        List<Scanner> processedScanners = new ArrayList<>();
        for ( int i = 0; i < scanners.size(); i++ ) {
            for ( int j = i + 1; j < scanners.size(); j++ ) {
                Scanner scanner1 = scanners.get( i );
                Scanner scanner2 = scanners.get( j );
                if ( !processedScanners.contains( scanner1 ) && !processedScanners.contains( scanner2 ) ) {
                    scanner1.reset( 0 );
                    scanner2.reset( 0 );
                    Set<Integer> commonBeaconDistances = scanner1.getCommonBeaconDistances( scanner2 );
                    if ( commonBeaconDistances.size() >= MINIMUM_MATCHING_DISTANCES ) {//12 matching beacons will have 66 matching distances
                        Set<BeaconVector> commonBeaconVectors = getCommonBeaconVectors( scanner1, scanner2 );
                        if ( commonBeaconVectors.size() >= MINIMUM_MATCHING_DISTANCES ) {
                            combineScanners( scanner1, scanner2, commonBeaconVectors );
                            processedScanners.add( scanner2 );
                        }
                    }
                }
            }
        }
        scanners = removeProcessedScanners( scanners, processedScanners );
        return scanners;
    }
    
    private int getMaxManhattanDistance( Set<Beacon> scanners ) {
        int maxManhattanDistance = 0;
        List<Beacon> scannerList = new ArrayList<>( scanners );
        for ( int i = 0; i < scannerList.size(); i++ ) {
            for ( int j = i + 1; j < scannerList.size(); j++ ) {
                maxManhattanDistance = Math.max( maxManhattanDistance, scannerList.get( i ).vectorBetween( scannerList.get( j ) ).getManhattanDistance() );
            }
        }
        return maxManhattanDistance;
    }
    
    private Map<Integer, Scanner> removeProcessedScanners( Map<Integer, Scanner> scanners, List<Scanner> processedScanners ) {
        Map<Integer, Scanner> nextIterationScanners = new HashMap<>();
        int newScannerIndexCount = 0;
        for ( Scanner scanner : scanners.values() ) {
            if ( !processedScanners.contains( scanner ) ) {
                nextIterationScanners.put( newScannerIndexCount++, scanner );
            }
        }
        scanners = nextIterationScanners;
        return scanners;
    }
    
    private void combineScanners( Scanner scanner1, Scanner scanner2, Set<BeaconVector> commonBeaconVectors ) {
        BeaconVector translationVector = null;
        for ( BeaconVector vector : commonBeaconVectors ) {
            Beacon scanner1Beacon = scanner1.vectorToPairMap.get( vector ).beacon1;
            Beacon scanner2Beacon = scanner2.vectorToPairMap.get( vector ).beacon1;
            translationVector = scanner1Beacon.vectorBetween( scanner2Beacon );
            break;
        }
        scanner2.translate( translationVector );
        for ( Beacon beacon : scanner2.getCurrentBeacons() ) {
            if ( !scanner1.currentBeacons.contains( beacon ) ) {
                scanner1.addBeacon( beacon );
            }
        }
        for ( Beacon scanner : scanner2.subScanners ) {
            scanner1.addScanner( scanner );
        }
    }
    
    private Set<BeaconVector> getCommonBeaconVectors( Scanner scanner1, Scanner scanner2 ) {
        Set<BeaconVector> commonBeaconVectors = scanner1.getCommonBeaconVectors( scanner2 );
        while ( commonBeaconVectors.size() < MINIMUM_MATCHING_DISTANCES ) {
            scanner2.rotate();
            commonBeaconVectors = scanner1.getCommonBeaconVectors( scanner2 );
            if ( scanner2.fullyRotated() ) {
                break;
            }
        }
        return commonBeaconVectors;
    }
    
    private Map<Integer, Scanner> getScannerFromInput() {
        Map<Integer, Scanner> inputScanners = new HashMap<>();
        Scanner currentScanner = null;
        for ( String input : inputLines ) {
            if ( input.equals( "" ) ) {
                inputScanners.put( Objects.requireNonNull( currentScanner ).index, currentScanner );
            }
            else if ( input.startsWith( "---" ) ) {
                currentScanner = new Scanner( Integer.parseInt( input.substring( 12, 14 ).trim() ) );
            }
            else {
                currentScanner.addBeacon( input );
            }
        }
        inputScanners.put( currentScanner.index, currentScanner );
        return inputScanners;
    }
    
    class Scanner {
        int index;
        
        int originalIndex;
        
        int currentRotations = 0;
        
        List<Beacon> currentBeacons = new ArrayList<>();
        
        List<List<Beacon>> beaconPermutations = new ArrayList<>();
        
        List<List<Beacon>> subScannerPermutations = new ArrayList<>();
        
        Set<BeaconVector> beaconVectors = new HashSet<>();
        
        Set<Beacon> subScanners = new HashSet<>();
        
        private final Set<Integer> beaconDistances = new HashSet<>();
        
        Map<BeaconVector, BeaconPair> vectorToPairMap = new HashMap<>();
        
        public Scanner( int index ) {
            this.index = index;
            this.originalIndex = index;
            addScanner( new Beacon( 0,0,0 ) );
        }
        
        public void addBeacon( String beaconPosition ) {
            Beacon beacon = new Beacon( Integer.parseInt( beaconPosition.split( "," )[0] ), Integer.parseInt( beaconPosition.split( "," )[1] ), Integer.parseInt( beaconPosition.split( "," )[2] ) );
            addBeacon( beacon );
        }
        
        public void addBeacon( Beacon beacon ) {
            currentBeacons.add( beacon );
            beaconPermutations.add( beacon.getPermutations() );
        }
        
        public void addScanner( Beacon scanner ) {
            subScanners.add( scanner );
            subScannerPermutations.add( scanner.getPermutations() );
        }
        
        public List<Beacon> getCurrentBeacons() {
            return currentBeacons;
        }
        
        public void rotate() {
            reset( ( currentRotations + 1 ) % 24 );
        }
        
        public void reset( int rotations ) {
            currentRotations = rotations;
            currentBeacons.clear();
            for ( List<Beacon> permutation : beaconPermutations ) {
                currentBeacons.add( permutation.get( currentRotations ) );
            }
            beaconDistances.clear();
            beaconVectors.clear();
            vectorToPairMap.clear();
            subScanners.clear();
            for ( List<Beacon> scannerPermutation : subScannerPermutations ) {
                subScanners.add( scannerPermutation.get( currentRotations ) );
            }
        }
        
        private Set<BeaconVector> getBeaconVectors() {
            if ( beaconVectors.isEmpty() ) {
                for ( int i = 0; i < getCurrentBeacons().size(); i++ ) {
                    for ( int j = 0; j < getCurrentBeacons().size(); j++ ) {
                        Beacon beacon1 = getCurrentBeacons().get( i );
                        Beacon beacon2 = getCurrentBeacons().get( j );
                        if ( !beacon1.equals( beacon2 ) ) {
                            BeaconVector vector = beacon1.vectorBetween( beacon2 );
                            beaconVectors.add( vector );
                            vectorToPairMap.put( vector, new BeaconPair( beacon1, beacon2 ) );
                        }
                    }
                }
            }
            return beaconVectors;
        }
        
        private Set<Integer> getBeaconDistances() {
            if ( beaconDistances.isEmpty() ) {
                for ( int i = 0; i < getCurrentBeacons().size(); i++ ) {
                    for ( int j = 0; j < getCurrentBeacons().size(); j++ ) {
                        Beacon beacon1 = getCurrentBeacons().get( i );
                        Beacon beacon2 = getCurrentBeacons().get( j );
                        if ( !beacon1.equals( beacon2 ) ) {
                            BeaconPair bp = new BeaconPair( beacon1, beacon2 );
                            beaconDistances.add( bp.distance );
                            vectorToPairMap.put( bp.vector, new BeaconPair( beacon1, beacon2 ) );
                        }
                    }
                }
            }
            return beaconDistances;
        }
        
        public Set<BeaconVector> getCommonBeaconVectors( Scanner other ) {
            Set<BeaconVector> commonVectors = new HashSet<>( this.getBeaconVectors() );
            commonVectors.retainAll( other.getBeaconVectors() );
            return commonVectors;
        }
        
        public Set<Integer> getCommonBeaconDistances( Scanner other ) {
            Set<Integer> commonDistances = new HashSet<>( getBeaconDistances() );
            commonDistances.retainAll( other.getBeaconDistances() );
            return commonDistances;
        }
        
        public boolean fullyRotated() {
            return currentRotations == 0;
        }
        
        public void translate( BeaconVector translationVector ) {
            for ( Beacon beacon : currentBeacons ) {
                beacon.translate( translationVector );
            }
            for ( Beacon scanner : subScanners ) {
                scanner.translate( translationVector );
            }
//            addScanner( new Beacon( 0, 0, 0 ).translate( translationVector ) );
        }
    }
    
    class BeaconPair {
        Beacon beacon1;
        
        Beacon beacon2;
        
        BeaconVector vector;
        
        int distance;
        
        public BeaconPair( Beacon beacon1, Beacon beacon2 ) {
            this.beacon1 = beacon1;
            this.beacon2 = beacon2;
            distance = beacon1.distanceBetween( beacon2 );
            vector = beacon1.vectorBetween( beacon2 );
        }
        
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            BeaconPair that = (BeaconPair) o;
            return Objects.equals( beacon1, that.beacon1 ) && Objects.equals( beacon2, that.beacon2 );
        }
        
        @Override
        public int hashCode() {
            return Objects.hash( beacon1, beacon2 );
        }
        
        @Override
        public String toString() {
            return "BeaconPair{" + "beacon1=" + beacon1 + ", beacon2=" + beacon2 + ", distance=" + distance + '}';
        }
    }
    
    class BeaconVector {
        int xLength;
        
        int yLength;
        
        int zLength;
        
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            BeaconVector vector3D = (BeaconVector) o;
            return xLength == vector3D.xLength && yLength == vector3D.yLength && zLength == vector3D.zLength;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash( xLength, yLength, zLength );
        }
        
        public BeaconVector( int xLength, int yLength, int zLength ) {
            this.xLength = xLength;
            this.yLength = yLength;
            this.zLength = zLength;
        }
        
        @Override
        public String toString() {
            return "BeaconVector{" + "xLength=" + xLength + ", yLength=" + yLength + ", zLength=" + zLength + '}';
        }
        
        public int getManhattanDistance() {
            return Math.abs( xLength ) + Math.abs( yLength ) + Math.abs( zLength );
        }
    }
    
    class Beacon {
        int x;
        
        int y;
        
        int z;
        
        public Beacon( int x, int y, int z ) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        protected List<Beacon> getPermutations() {
            List<Beacon> permutations = new ArrayList<>();
            permutations.add( new Beacon( x, y, z ) );
            permutations.add( new Beacon( -1 * y, x, z ) );
            permutations.add( new Beacon( -1 * x, -1 * y, z ) );
            permutations.add( new Beacon( y, -1 * x, z ) );
            permutations.add( new Beacon( -1 * z, y, x ) );
            permutations.add( new Beacon( -1 * y, -1 * z, x ) );
            permutations.add( new Beacon( z, -1 * y, x ) );
            permutations.add( new Beacon( y, z, x ) );
            permutations.add( new Beacon( -1 * x, y, -1 * z ) );
            permutations.add( new Beacon( -1 * y, -1 * x, -1 * z ) );
            permutations.add( new Beacon( x, -1 * y, -1 * z ) );
            permutations.add( new Beacon( y, x, -1 * z ) );
            permutations.add( new Beacon( z, y, -1 * x ) );
            permutations.add( new Beacon( -1 * y, z, -1 * x ) );
            permutations.add( new Beacon( -1 * z, -1 * y, -1 * x ) );
            permutations.add( new Beacon( y, -1 * z, -1 * x ) );
            permutations.add( new Beacon( x, -1 * z, y ) );
            permutations.add( new Beacon( z, x, y ) );
            permutations.add( new Beacon( -1 * x, z, y ) );
            permutations.add( new Beacon( -1 * z, -1 * x, y ) );
            permutations.add( new Beacon( x, z, -1 * y ) );
            permutations.add( new Beacon( -1 * z, x, -1 * y ) );
            permutations.add( new Beacon( -1 * x, -1 * z, -1 * y ) );
            permutations.add( new Beacon( z, -1 * x, -1 * y ) );
            
            return permutations;
        }
        
        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            Beacon point3D = (Beacon) o;
            return x == point3D.x && y == point3D.y && z == point3D.z;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash( x, y, z );
        }
        
        @Override
        public String toString() {
            return "Beacon{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
        }
        
        public BeaconVector vectorBetween( Beacon point ) {
            return new BeaconVector( ( this.x - point.x ), ( this.y - point.y ), ( this.z - point.z ) );
        }
        
        public Integer distanceBetween( Beacon point ) {
            return Math.abs( this.x - point.x ) + Math.abs( this.y - point.y ) + Math.abs( this.z - point.z );
        }
        
        public Beacon translate( BeaconVector scannerVector ) {
            this.x += scannerVector.xLength;
            this.y += scannerVector.yLength;
            this.z += scannerVector.zLength;
            return this;
        }
    }
}
