package au.edu.anu.Aussic.models.avl;

/**
 * @author: u7581818, Oscar Wei
 */

import au.edu.anu.Aussic.models.entity.Song;

/**
 * The following interface defines required methods of any Tree.
 * Note that this is simplified for this lab (no delete).
 *
 * @param <T> the generic type this Tree uses. It extends comparable
 *            which allows us to order two of the same type.
 */
public abstract class Tree<T> {
    /**
     * Here we store our class fields.
     */
    public String key;
    public  T value;
    // element stored in this node of the tree.
    public Tree<T> leftNode;    // less than the node.
    public Tree<T> rightNode;   // greater than the node.

    /**
     * Constructor to allow for empty trees
     */
    public Tree() {
        value = null;
        key = null;
    }

    /**
     * Constructor for creating a new child node.
     * Note that the left and right nodes must be set by the subclass.
     *
     * @param value to set as this node's value.
     */
    public Tree(String key, T value) {
        // Ensure input is not null.
        if (value == null)
            throw new IllegalArgumentException("Input cannot be null");

        this.value = value;
        this.key = key;
    }

    /**
     * Constructor for creating a new node.
     * Note that this is what allows our implementation to be immutable.
     *
     * @param value     to set as this node's value.
     * @param leftNode  left child of current node.
     * @param rightNode right child of current node.
     */
    public Tree(String key, T value, Tree<T> leftNode, Tree<T> rightNode) {
        // Ensure inputs are not null.
        if (value == null || leftNode == null || rightNode == null)
            throw new IllegalArgumentException("Inputs cannot be null");
        this.key = key;
        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public abstract T min();                     // Finds the minimum.

    public abstract T max();                     // Finds the maximum.

    public abstract Tree<T> find(String key);     // Finds the element and returns the node.

    public abstract Tree<T> insertById(Song song);   // Inserts the element and returns a new instance of itself with the new node.
    public abstract Tree<T> insertByName(Song song);
    public abstract Tree<T> insertByArtistName(Song song);
    public abstract Tree<T> insertByReleaseDate(Song song);
    public abstract Tree<T> insertByGenre(String genre, Song song);

    public abstract Tree<T> deleteById(Song song);

    public abstract Tree<T> deleteByName(Song song);

    public abstract Tree<T> deleteByArtistName(Song song);

    public abstract Tree<T> deleteByReleaseDate(Song song);

    public abstract Tree<T> deleteByGenre(String genre, Song song);




    //public abstract Tree<T> delete(T element);   // Deletes the element and returns the node deleted.
/////////////////////////////////////////////////////
    /**
     * Height of current node.
     * @return The maximum height of either children.
     */
    public int getHeight() {
        // Check whether leftNode or rightNode are EmptyTree
        int leftNodeHeight = leftNode instanceof EmptyTree ? 0 : 1 + leftNode.getHeight();
        int rightNodeHeight = rightNode instanceof EmptyTree ? 0 : 1 + rightNode.getHeight();
        return Math.max(leftNodeHeight, rightNodeHeight);
    }

    @Override
    public String toString() {
        return "{"
                +"key=" + key+
                "value=" + value +
                ", leftNode=" + leftNode +
                ", rightNode=" + rightNode +
                '}';
    }


}
