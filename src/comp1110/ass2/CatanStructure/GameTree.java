package comp1110.ass2.CatanStructure;

import comp1110.ass2.gui.Game;

import java.util.ArrayList;

// Author: John Larkin
public class GameTree {

    Object node;
    GameTree left; // Left Leaf
    GameTree right; // Right Leaf

    // Create a game tree
    public GameTree(){
        this.node = null;
        this.left = null;
        this.right = null;
    }

    public static GameTree createCatanGameTree(){
        GameTree path = new GameTree();
        path.node = "RI";
        path.add("RI", null, "S3");
        path.add("S3", null, "R0");
        path.add("R0", "R1", "R2");
        path.add("R1", null, "C7");
        path.add("R2", null, "S4");
        path.add("S4", null, "R3");
        path.add("R3", "R4", "R5");
        path.add("R4", null, "C12");
        path.add("R5", null, "S5");
        path.add("S5", null, "R6");
        path.add("R6", null, "R7");
        path.add("R7", null, "S7");
        path.add("S7", "R12", "R8");
        path.add("R8", null, "R9");
        path.add("R9", null, "S9");
        path.add("S9", null, "R10");
        path.add("R10", null, "R11");
        path.add("R11", null, "S11");
        path.add("R12", null, "R13");
        path.add("R13", null, "C20");
        path.add("C20", null, "R14");
        path.add("R14", null, "R15");
        path.add("R15", null, "C30");

        return path;
    }


// A fold on the GameTree into an ArrayList
    // The argument is an empty ArrayList which is modified to
    // display the result of the fold. It is the 'memory' between recursive calls.
    public ArrayList<Object> fold(ArrayList<Object> res){
        if (this.left == null && this.right == null){
            res.add(this.node);}
        else if (this.left == null){
                res.add(this.node);
                this.right.fold(res);
            }else if (this.right == null){
                res.add(this.node);
                this.left.fold(res);
            }
        else {
            res.add(this.node);
            this.left.fold(res);
            this.right.fold(res);
        }
        return res;
    }

    // Check to see if the GameTree contains this object;
    // Assumes the object is not null, as every GameTree contains a null.
    public boolean contains(Object object){
        if (this.node.equals(object)) {
            return true;}
        else if (this.left == null){
            if (this.right != null){
                return this.right.contains(object);}
        }
        else if (this.right == null){
            if (this.left != null){
                return this.left.contains(object);}
        } else {
            return this.left.contains(object) || this.right.contains(object);
        }
        return false;
    }

    /**
     * Given a GameTree a Object node, l and r extend the current GameTree by adding l and r to the
     * branches of the tree where the node is located. If the node is not in the tree then it does nothing.
     * It will overwrite the current tree. If nothing is to be added to that branch then use input null
     * @param node Node to add to.
     * @param l Object to add to left branch.
     * @param r Object to add to right branch.
     */
    public void add(Object node, Object l, Object r){
        if (this.node.equals(node)){
            if (l != null && r != null){
            this.left = new GameTree();
            this.right = new GameTree();
            this.left.node = l;
            this.right.node = r;
            }
            else if (l == null && r != null){
                this.right = new GameTree();
                this.right.node = r;
            }
            else if (l != null && r == null){
                this.left = new GameTree();
                this.left.node = l;
            }
        }
        else if (this.left == null && this.right !=null){
            this.right.add(node, l, r);
        }
        else if (this.right == null && this.left!= null){
            this.left.add(node, l ,r);
        }
        else if (this.left != null && this.right != null){
            this.left.add(node, l ,r);
            this.right.add(node, l ,r);
        }
    }

    /**
     * Add the element to the left branch at a specified node
     * Adds to the branch only if the boolean value of the branch is true
     */
    public void addSelective(Object node, Object l, Object r, boolean left, boolean right){
        if (this.node.equals(node)){
            if (l != null && r != null && left && right){
                this.left = new GameTree();
                this.right = new GameTree();
                this.left.node = l;
                this.right.node = r;
            }
            else if (l == null && r != null && right){
                this.right = new GameTree();
                this.right.node = r;
            }
            else if (l != null && r == null && left){
                this.left = new GameTree();
                this.left.node = l;
            }
        }
        else if (this.left == null && this.right !=null){
            this.right.add(node, l, r);
        }
        else if (this.right == null && this.left!= null){
            this.left.add(node, l ,r);
        }
        else if (this.left != null && this.right != null){
            this.left.add(node, l ,r);
            this.right.add(node, l ,r);
        }
    }


    /**
     * For a GameTree find a path down the gameTree to the object. Assumes no nodes in this GameTree
     * are duplicate and the target object is not null.
     *
     * If this GameTree does not contain the target object then paths will be empty.
     *
     * @param target: The target object of the path
     * @param res: An empty ArrayList of objects
     */

    public ArrayList<Object> findPath(Object target, ArrayList<Object> res){
        if (this.contains(target)){
            res.add(this.node);
            if (this.left != null && this.left.contains(target)){
                this.left.findPath(target, res);
            }
            if (this.right != null && this.right.contains(target)){
                this.right.findPath(target, res);
            }
            if (this.right == null && this.left == null){
                return res;
            }
        }
        return res;
    }

    /**
     * An object can be pruned (removed) iff it is a leaf with no branches in the tree.
     * If this function returns true then the object was removed.
     * @param remove: The object to remove
     * @return true iff the object can be removed, and removes the object.
     */
    public boolean canPrune(Object remove){
        if (this.node.equals(remove)){
            if (this.left == null && this.right == null){
                this.node = null;
                return true;
            }
        }
        else if (this.left == null && this.right!= null){
            return this.right.canPrune(remove);
        }
        else if (this.right == null && this.left!= null){
            return this.left.canPrune(remove);
        }
        else if (this.left == null & this.right == null){return false;}
        else {
            return this.left.canPrune(remove) || this.right.canPrune(remove);
        }
        return false;
    }

    /**
     * Remove a node if the node is null and both branches are null.
     * @param empty: The empty GameTree to add values to. It must have the first node the same as the GameTree.
     *             In this case "RI" is a convenient choice.
     * @return The input GameTree
     */
    public GameTree removeNulls(GameTree empty){
        if (this.node == null && this.left == null && this.right == null){
            // Do nothing
        }
        else if (this.node != null){
            if (this.left == null && this.right == null){
                empty.add(this.node, null, null);
            }
            else if (this.left == null && this.right != null){
                empty.add(this.node, null, this.right.node);
                this.right.removeNulls(empty);
            }
            else if (this.right == null && this.left != null){
                empty.add(this.node, this.left.node, null);
                this.left.removeNulls(empty);
            }
            else if (this.right != null && this.left != null){
                empty.add(this.node, this.left.node, this.right.node);
                this.left.removeNulls(empty);
                this.right.removeNulls(empty);
            }
        }
        return empty;
    }

}


