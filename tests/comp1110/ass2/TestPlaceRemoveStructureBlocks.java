package comp1110.ass2;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanStructure.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class TestPlaceRemoveStructureBlocks {
    private static final String[] STATES = new String[]{
            "RRRRRRRRRRRRRRRRRRRR",
            "RRRCCCSSSRRSSCCSRRSS",
            "RRRCCCSCSRRSSCCSRRSC",
    };

    private static final String[] EXPECTED_STATES = new String[]{
            "RRRRDRRRRRRRRRRRRRRR", // Placing on empty board
            "RRRCCCSSTRRSSCCSRRSS", // Replacing empty road with a built road
            "RRRCCCSYSRRSSCCSRRSC", // replace a different structure with another should fail
    };

    private static final Structure[] STRUCTURES = new Structure[]{
            new Structure("R", new int[]{4,0}),
            new Structure("S", new int[]{3,1}),
            new Structure("C", new int[]{2,1}),
    };

    private static final ArrayList<StructureType> c = new ArrayList<>();

    private void makeArrayList() {
        c.add(StructureType.ROAD);
    }

    @Test
    public void testPlacement() {
        makeArrayList();
        for (int i = 0; i < STRUCTURES.length; i++) {
            CatanBoard catanBoard = new CatanBoard(STATES[i]);
            CatanBoard expected = new CatanBoard(EXPECTED_STATES[i]);
            String boardString = catanBoard.boardToString();
            catanBoard.placeStructureBlock(STRUCTURES[i]);
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 5; x++) {
                    BuildableStructure actual = catanBoard.getBuildableStructure(x, y);
                    BuildableStructure exp = expected.getBuildableStructure(x, y);
                    assertEquals(exp.getStructureType(), actual.getStructureType(), "Given board state\n" + boardString + "\n" +
                            "And structure placement\n" + "\n" +
                            "Expected resulting board state\n" + expected.boardToString() + ",\n" +
                            "but instead got\n" + catanBoard.boardToString() + "!");
                }
            }
        }
    }

    @Test
    public void testRemoveIceBlock() {
        testPlacement();
        for (int i = 0; i < STRUCTURES.length; i++) {
            CatanBoard catanBoard = new CatanBoard(STATES[i]);
            CatanBoard expected = new CatanBoard(STATES[i]);
            catanBoard.placeStructureBlock(STRUCTURES[i]);
            catanBoard.removeStructureBlock(STRUCTURES[i]);
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 5; x++) {
                    BuildableStructure exp = expected.getBuildableStructure(x, y);
                    BuildableStructure actual = catanBoard.getBuildableStructure(x, y);
                    assertEquals(exp.getStructureType(), actual.getStructureType(), "For board state\n" +
                            expected.boardToString() + ",\nexpected placing and removing an structure block to not" +
                            "modify the board, but instead got board state\n" +
                            catanBoard.boardToString());
                }
            }
        }
    }
}
