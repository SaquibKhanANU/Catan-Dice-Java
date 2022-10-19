package comp1110.ass2;

import comp1110.ass2.CatanStructure.Action;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
public class TestAction {

    // Create an action
    public Action makeAction(String action_string){
        return new Action(action_string);
    }

    @ Test
    public void testConstructor(){
        Action action = new Action("build J1");
        Assertions.assertEquals(1, action.id, "Expected an id of 1 but got an id of " + action.id);
        Assertions.assertEquals('J', action.structure, "Expected a character of J but got a character of " + action.structure);
        Assertions.assertEquals("build", action.type);
        Action action1 = new Action("trade 3");
        Assertions.assertEquals("trade", action1.type);
        Assertions.assertEquals(3, action1.in);
        Action action2 = new Action("swap 3 2");
        Assertions.assertEquals("swap", action2.type);
        Assertions.assertEquals(2, action2.in);
        Assertions.assertEquals(3, action2.out);
    }
}
