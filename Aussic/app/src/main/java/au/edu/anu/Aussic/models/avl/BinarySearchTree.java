package au.edu.anu.Aussic.models.avl;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.entity.Song;

/**
 * An AVL tree is actually an extension of a Binary Search Tree
 * with self balancing properties. Hence, our AVL trees will 'extend'
 * this Binary Search tree data structure.
 */
public class BinarySearchTree<T> extends Tree<T> {

    public BinarySearchTree(String key, T value) {
        super(key, value);
        this.leftNode = new EmptyBST<>();
        this.rightNode = new EmptyBST<>();
    }

    public BinarySearchTree(String key, T value, Tree<T> leftNode, Tree<T> rightNode) {
        super(key, value, leftNode, rightNode);
    }

    @Override
    public T min() {
        return (leftNode instanceof EmptyTree) ? value : leftNode.min();
    }

    @Override
    public T max() {
        return (rightNode instanceof EmptyTree) ? value : rightNode.max();
    }

    @Override
    public Tree<T> find(String key) {
        /*
            Left is less, right is greater in this implementation.
            compareTo returns 0 if both elements are equal.
            compareTo returns < 0 if the element is less than the node.
            compareTo returns > 0 if the element is greater than the node.
         */

        // Ensure input is not null.
        if (key == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (key.compareTo(this.key) == 0) {
            return this;
        } else if (key.compareTo(this.key) < 0) {
            return leftNode.find(key);
        } else {
            return rightNode.find(key);
        }
    }

    @Override
    public BinarySearchTree<T> insertById(Song song) {
        // Ensure input is not null.
        if (song == null)
            throw new IllegalArgumentException("Input cannot be null");

        // If the two values are equal, in this implementation we want to insert to the left.
        if (song.getId().compareTo(key) > 0) {
            return new BinarySearchTree<>(key, value, leftNode, rightNode.insertById(song));
        } else {
            return new BinarySearchTree<>(key, value, leftNode.insertById(song), rightNode);
        }
    }

    @Override
    public BinarySearchTree<T> delete(T element) {
        return null;
    }

    /**
     * Note that this is not within a file of its own... WHY?
     * The answer is: this is just a design decision. 'insert' here will return something specific
     * to the parent class inheriting Tree. In this case a BinarySearchTree.
     */
    public static class EmptyBST<T> extends EmptyTree<T> {
        @Override
        public Tree<T> insertById(Song song) {
            List<Song> songList = new ArrayList<>()    ;
            songList.add(song);// The creation of a new Tree, hence, return tree.
            return new BinarySearchTree<T>(song.getId(), (T) songList) ;
        }

        @Override
        public Tree<T> delete(T element) {
            return null;
        }

        // @Override
      // public Tree<T> deleteById(T element) {
      //     return new BinarySearchTree<>(element);
      // }
    }
}
