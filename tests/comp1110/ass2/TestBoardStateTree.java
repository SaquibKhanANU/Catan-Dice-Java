package comp1110.ass2;

import comp1110.ass2.CatanStructure.BoardStateTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
public class TestBoardStateTree {

    // Create an action
    public BoardStateTree makeBoardStateTree(String board_state){
        return new BoardStateTree(board_state);
    }

    @ Test
    public void testConstructor(){
        BoardStateTree tree= new BoardStateTree("RI,S3,R1");
        Assertions.assertEquals("RI,S3,R1", tree.board_state, "Expected a board_state of RI, S3, R1 but got a board_state of " + tree.board_state);
        BoardStateTree game_tree = new BoardStateTree("RI,R0,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,R11,R12,R13,R14,R15,S3,S4,S5,S7,S9,S11,C7,C12,C20,C30,J1,J2,J3,J4,J5,J6,K1,K2,K3,K4,K5,K6");
        Assertions.assertEquals(false, game_tree.canRemove("RI"));
        Assertions.assertEquals(true, game_tree.canRemove("C30"));
        Assertions.assertEquals(true, game_tree.canRemove("C20"));
        Assertions.assertEquals(false, game_tree.canRemove("C7"));
        Assertions.assertEquals(false, game_tree.canRemove("K5"));
        Assertions.assertEquals(false, game_tree.canRemove("R15"));
        Assertions.assertEquals(true, game_tree.canRemove("S11"));
        Assertions.assertEquals(false, game_tree.canRemove("R14"));
    }
}
