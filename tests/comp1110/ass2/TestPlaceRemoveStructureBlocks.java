package comp1110.ass2;

import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanStructure.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Timeout(value = 1000, unit = MILLISECONDS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class TestPlaceRemoveStructureBlocks {
    private static final String[] STATES = new String[]{
            "EEEEEEEEEEEEEEEEEEEE",
            "RRRCCCSSSRRSSCCSRRSS",
            "RRRCCCSSSRRSSCCSRRSS",
    };

    private static final String[] EXPECTED_STATES = new String[]{
            "EEEETEEEEEEEEEEEEEEE", // Placing on empty board
            "RRRCCCSSSDRSSCCSRRSS", // Replacing empty road with a built road
            "RRRCCCSYSRRSSCCSRRSS", // replace a different structure with another
    };

    private static final Structure[] ICE_BLOCKS = new Structure[]{
            new CatanSettlement("C", new int[]{4,0}),
            new CatanRoad("C", new int[]{4,1}),
            new CatanCity("C", new int[]{2,1}),
    };

    @Test
    public void testPlacement() {
        for (int i = 0; i < ICE_BLOCKS.length; i++) {
            CatanBoard penguinsPoolParty = new CatanBoard(STATES[i]);
            CatanBoard expected = new CatanBoard(EXPECTED_STATES[i]);
            String boardString = penguinsPoolParty.boardToString();
            penguinsPoolParty.placeStructureBlock(ICE_BLOCKS[i]);
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 5; x++) {
                    BuildableStructure actual = penguinsPoolParty.getBuildableStructure(x, y);
                    BuildableStructure exp = expected.getBuildableStructure(x, y);
                    assertEquals(exp.getStructureType(), actual.getStructureType(), "Given board state\n" + boardString + "\n" +
                            "And ice placement\n" + "\n" +
                            "Expected resulting board state\n" + expected.boardToString() + ",\n" +
                            "but instead got\n" + penguinsPoolParty.boardToString() + "!");
                }
            }
        }
    }

    @Test
    public void testRemoveIceBlock() {
        testPlacement();
        for (int i = 0; i < ICE_BLOCKS.length; i++) {
            CatanBoard penguinsPoolParty = new CatanBoard(STATES[i]);
            CatanBoard expected = new CatanBoard(STATES[i]);
            penguinsPoolParty.placeStructureBlock(ICE_BLOCKS[i]);
            penguinsPoolParty.removeStructureBlock(ICE_BLOCKS[i]);
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 5; x++) {
                    BuildableStructure exp = expected.getBuildableStructure(x, y);
                    BuildableStructure actual = penguinsPoolParty.getBuildableStructure(x, y);
                    assertEquals(exp.getStructureType(), actual.getStructureType(), "For board state\n" +
                            expected.boardToString() + ",\nexpected placing and removing an ice block to not" +
                            "modify the board, but instead got board state\n" +
                            penguinsPoolParty.boardToString());
                }
            }
        }
    }
}
