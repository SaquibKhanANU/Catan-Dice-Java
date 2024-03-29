package comp1110.ass2.CatanEnum;

// Author: Saquib Khan and John Larkin
public enum StructureType {
    /**
     * The following structure types are for the 6 structures when they are built, not built or used.
     * @Author Saquib Khan and John Larkin
     */
    CITY, SETTLEMENT, ROAD, KNIGHT, YBUILT_CITY, TBUILT_SETTLEMENT, DBUILT_ROAD, JOKER, USED;

    /**
     * Used for testing converts char to a structure
     * @param c the char representing the structure
     * @return the char converted to a structure
     */
    public static StructureType fromChar(char c) {
        return switch (c) {
            case 'S' -> SETTLEMENT;
            case 'R' -> ROAD;
            case 'K' -> KNIGHT;
            case 'C' -> CITY;
            case 'T' -> TBUILT_SETTLEMENT; // Based on last letter
            case 'D' -> DBUILT_ROAD;
            case 'J' -> JOKER;
            case 'Y' -> YBUILT_CITY;
            default -> null;
        };
    }
    //FOR Test
    public char toChar() {
        return this.toString().charAt(0);
    }
}
