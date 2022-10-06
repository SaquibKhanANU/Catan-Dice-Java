package comp1110.ass2.CatanStructure;

import java.util.ArrayList;

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

    public static void main(String[] args) {
        GameTree path = new GameTree();
        path.node = "RI";
        path.add("RI", null, "R0");
        path.add("R0", "R1", "R2");
        path.add("R1", null, "C7");
        path.add("R2", null, "S4");


        ArrayList<Object> res = new ArrayList<>();
        System.out.println(path.fold(res));

        GameTree simple = new GameTree();
        simple.node = "RI";
        simple.left = new GameTree();
        simple.left.node = "R0";
        ArrayList<Object> res1 = new ArrayList<>();
        System.out.println(path.findPath("RI", res1));
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

    // Given a GameTree a Object node, l and r extend the current GameTree by adding l and r to the
    // branches of the tree where the node is located. If the node is not in the tree then it does nothing
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

}


