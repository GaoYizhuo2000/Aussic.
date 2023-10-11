package au.edu.anu.Aussic.models.avl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {
    public AVLTree(T value) {
        super(value);
        // Set left and right children to be of EmptyAVL as opposed to EmptyBST.
        this.leftNode = new EmptyAVL<>();
        this.rightNode = new EmptyAVL<>();
    }

    public AVLTree(T value, Tree<T> leftNode, Tree<T> rightNode) {
        super(value, leftNode, rightNode);
    }

    /**
     * @return balance factor of the current node.
     */
    public int getBalanceFactor() {
        return leftNode.getHeight() - rightNode.getHeight();
    }

    @Override
    public AVLTree<T> insert(T element) {
        // Ensure input is not null.
        if (element == null)
            throw new IllegalArgumentException("Input cannot be null");

        //Create a tree which copies everything about the tree to make sure the original tree immutable
        AVLTree<T> copyTree = new AVLTree<>(this.value, this.leftNode, this.rightNode);

        //Insert unduplicated element
        if (element.compareTo(value) > 0) {
            // insert on right subtree when greater than this.value
            copyTree.rightNode = copyTree.rightNode.insert(element);
        } else if (element.compareTo(value) < 0) {
            // insert on left subtree when less than this.value
            copyTree.leftNode = copyTree.leftNode.insert(element);
        } else {
            return copyTree; // Duplicated element is not allowed, which is different with BST
        }

        //Balance the tree to make sure it is a AVL tree
        copyTree = balanceTree(copyTree);

        //return this; // Change to return something different
        return copyTree;
    }



    private AVLTree<T> balanceTree(AVLTree<T> tree) {

        int balanceFactor = tree.getBalanceFactor();

        if (balanceFactor < -1) {
            // Right subtree is heavier
            return balanceRightHeavy(tree);
        } else if (balanceFactor > 1) {
            // Left subtree is heavier
            return balanceLeftHeavy(tree);
        }

        return tree; // Already balanced
    }



    private AVLTree<T> balanceRightHeavy(AVLTree<T> tree) {
        AVLTree<T> rightNode = (AVLTree<T>) tree.rightNode;

        //Right left rotate
        if(tree.rightNode.rightNode.getHeight() < tree.rightNode.leftNode.getHeight()){
            tree.rightNode = rightNode.rightRotate();
            return tree.leftRotate();
        }

        //Right heavy, left rotate anyway
        return tree.leftRotate();
    }

    private AVLTree<T> balanceLeftHeavy(AVLTree<T> tree) {
        AVLTree<T> leftNode = (AVLTree<T>) tree.leftNode;

        //Left right rotate
        if (tree.leftNode.leftNode.getHeight() < tree.leftNode.rightNode.getHeight()){
            tree.leftNode = leftNode.leftRotate();
            return tree.rightRotate();
        }

        //Left heavy, right rotate anyway
        return tree.rightRotate();
    }

    /**
     * Conducts a left rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> leftRotate() {
        Tree<T> newParent = this.rightNode; // newParent's value equals to B's value.
        Tree<T> newRightOfCurrent = newParent.leftNode; // Set newROC's value is null now.
        // COMPLETED
        newParent.leftNode = this; //A becomes the left node of newParent(B)
        newParent.leftNode.rightNode = newRightOfCurrent; // A's right node is newROC(null)
        return (AVLTree<T>) newParent;
        //return null; // Change to return something different
    }

    /**
     * Conducts a right rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> rightRotate() {
        Tree<T> newParent = this.leftNode;
        Tree<T> newLeftOfCurrent = newParent.rightNode;
        newParent.rightNode = this;
        newParent.rightNode.leftNode = newLeftOfCurrent;
        return (AVLTree<T>) newParent;

    }

    /**
     * Delete the input element
     * @param element The element to be deleted
     * @return
     */
    public AVLTree<T> delete(T element) {
        if (element == null || find(element) == null)
            throw new IllegalArgumentException("Invalid input! Cannot find such element in the tree.");
        AVLTree<T> copyTree; //Make sure its immutability
        copyTree = deleteNode(this, element);
        return copyTree;
    }

    private AVLTree<T> deleteNode(AVLTree<T> treeNode, T element) {
        if (treeNode == null) {
            return null;
        }

        // Avoid the cast. Handle cases where leftNode or rightNode might be instances of EmptyAVL.
        if (treeNode.leftNode instanceof EmptyAVL) {
            treeNode.leftNode = null;
        }
        if (treeNode.rightNode instanceof EmptyAVL) {
            treeNode.rightNode = null;
        }

        AVLTree<T> newTreeNode = null;

        if (element.compareTo(treeNode.value) < 0) {
            AVLTree<T> copyLeftNode = deleteNode((AVLTree<T>) treeNode.leftNode, element);
            newTreeNode = new AVLTree<>(treeNode.value, copyLeftNode != null ? copyLeftNode : new EmptyAVL<>(), treeNode.rightNode);
        } else if (element.compareTo(treeNode.value) > 0) {
            AVLTree<T> copyRightNode = deleteNode((AVLTree<T>) treeNode.rightNode, element);
            newTreeNode = new AVLTree<>(treeNode.value, treeNode.leftNode, copyRightNode != null ? copyRightNode : new EmptyAVL<>());
        } else {
            // Node with the element to be deleted found
            if (treeNode.leftNode == null && treeNode.rightNode == null) {
                // No children / Leaf node
                return null;
            } else if (treeNode.leftNode == null) {
                // Right child only
                return (AVLTree<T>) treeNode.rightNode;
            } else if (treeNode.rightNode == null) {
                // Left child only
                return (AVLTree<T>) treeNode.leftNode;
            } else {
                // Two children
                AVLTree<T> newRight = (AVLTree<T>) treeNode.rightNode;
                T successor = newRight.findMinInRight();
                newRight = deleteNode(newRight, successor);
                newTreeNode = new AVLTree<>(successor, treeNode.leftNode, newRight != null ? newRight : new EmptyAVL<>());
            }
        }

        return balanceTree(newTreeNode);
    }




    /**
     * Helper for deleteNode()
     * To find the
     * Finds the leftmost (smallest) element in the right subtree
     *
     * @return the leftmost element
     * @author
     */
    public T findMinInRight() {
        if (this.leftNode == null || this.leftNode.value == null) {
            return this.value;
        } else {
            return new AVLTree<T>(this.leftNode.value, this.leftNode.leftNode, this.leftNode.rightNode).findMinInRight();
        }
    }

    // 中序遍历方法
    public List<T> inorderTraversal() {
        List<T> result = new ArrayList<>();
        inorderTraversal(this, result);
        return result;
    }

    private void inorderTraversal(AVLTree<T> node, List<T> result) {
        if (node == null || node.value == null) {
            return;
        }

        // 遍历左子树
        if (node.leftNode instanceof AVLTree) {
            node.inorderTraversal((AVLTree<T>) node.leftNode, result);
        }

        // 访问当前节点
        result.add(node.value);

        // 遍历右子树
        if (node.rightNode instanceof AVLTree) {
            node.inorderTraversal((AVLTree<T>) node.rightNode, result);
        }
    }


    public Set<T> searchAll(T element) {
        Set<T> resultSet = new HashSet<>();
        searchAll(this, element, resultSet);
        return resultSet;
    }

    private void searchAll(AVLTree<T> node, T element, Set<T> resultSet) {
        if (node == null || node.value == null) {
            return;
        }

        int comparison = element.compareTo(node.value);
        if (comparison == 0) {
            resultSet.add(node.value);
        }

        if (node.leftNode instanceof AVLTree) {
            searchAll((AVLTree<T>) node.leftNode, element, resultSet);
        }

        if (node.rightNode instanceof AVLTree) {
            searchAll((AVLTree<T>) node.rightNode, element, resultSet);
        }
    }


    /**
     * Note that this is not within a file of its own... WHY?
     * The answer is: this is just a design decision. 'insert' here will return something specific
     * to the parent class inheriting Tree from BinarySearchTree. In this case an AVL tree.
     */
    public static class EmptyAVL<T extends Comparable<T>> extends EmptyTree<T> {
        @Override
        public Tree<T> insert(T element) {
            // The creation of a new Tree, hence, return tree.
            return new AVLTree<T>(element);
        }


        @Override
        public Tree<T> delete(T element) {
            return new AVLTree<T>(element);
        }
    }
}
