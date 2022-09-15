package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class TestIsBoardStateWellFormed {

    private String errorPrefix(String board_state) {
        return "CatanDice.isBoardStateWellFormed(" + board_state + ")";
    }

    private void test(String in, boolean expected) {
        String errorPrefix = errorPrefix(in);
        boolean out = CatanDice.isBoardStateWellFormed(in);
        assertEquals(expected, out, errorPrefix);
    }

    @Test
    public void trivialTrue() {
        test("", true); // an empty string is valid
        test("R2", true); // a single structure of each type
        test("S4", true); // a single structure of each type
        test("C12", true); // a single structure of each type
        test("J1", true); // a single structure of each type
        test("K5", true); // a single structure of each type
        test("R1,S4,R4,C7", true); // several structures
    }

    @Test
    public void trivialFalse() {
        test("A2", false); // invalid type letter
        test("JK2", false); // multiple type letters
        test("3", false); // no type letter
        test("K", false); // no number
        test("2R", false); // type letter in wrong place
        test("R-3", false); // negative number
        test("R 3", false); // spaces in string
        test("R3 C7 J3", false); // wrong separator
        test("R3,C7;J3", false); // wrong separator
        test("R17", false); // index out of range
        test("C1", false); // index out of range
        test("K0", false); // index out of range
        test("K9", false); // index out of range
    }

    public static void main(String[] args) {
	TestIsBoardStateWellFormed tests = new TestIsBoardStateWellFormed();
	System.out.println("testing...");
	tests.trivialTrue();
	tests.trivialFalse();
	System.out.println("all done!");
    }
}
