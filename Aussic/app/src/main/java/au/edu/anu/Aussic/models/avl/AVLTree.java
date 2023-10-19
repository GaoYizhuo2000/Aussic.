package au.edu.anu.Aussic.models.avl;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.entity.Song;

/**
 * @author: u7581818, Oscar Wei
 * @author: u7552399, Yizhuo Gao
 */

public class AVLTree<T> extends BinarySearchTree<T> {
    public AVLTree() {
        super();
    }

    private int height;

    public AVLTree(String key, T value) {
        super(key, value);
        // Set left and right children to be of EmptyAVL as opposed to EmptyBST.
        this.leftNode = new EmptyAVL<>();
        this.rightNode = new EmptyAVL<>();
    }

    public AVLTree(String key, T value, Tree<T> leftNode, Tree<T> rightNode) {
        super(key, value, leftNode, rightNode);
    }

    /**
     * @return balance factor of the current node.
     */
    public int getBalanceFactor() {
        return leftNode.getHeight() - rightNode.getHeight();
    }

    public int getHeight() {
        // Check whether leftNode or rightNode are EmptyTree
        int leftNodeHeight = leftNode instanceof EmptyTree ? 0 : 1 + leftNode.getHeight();
        int rightNodeHeight = rightNode instanceof EmptyTree ? 0 : 1 + rightNode.getHeight();
        height = Math.max(leftNodeHeight, rightNodeHeight);
        return height;
    }


    @Override
    public AVLTree<T> insertById(Song song) {
        // Ensure input is not null.
        if (song == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (song.getId().compareTo(key) > 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, rightNode.insertById(song));
            if (newTree.getBalanceFactor() < -1 && newTree.rightNode.rightNode.find(song.getId()) != null) {
                newTree = newTree.leftRotate();
            } else if (newTree.getBalanceFactor() < -1 && newTree.rightNode.leftNode.find(song.getId()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
            // COMPLETE
        } else if (song.getId().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertById(song), rightNode);
            if (newTree.getBalanceFactor() > 1 && newTree.leftNode.leftNode.find(song.getId()) != null) {
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor() > 1 && newTree.leftNode.rightNode.find(song.getId()) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        }
        return this;
    }

    @Override
    public AVLTree<T> insertByName(Song song) {
        if (song == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (song.getSongName().compareTo(key) > 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, rightNode.insertByName(song));
            if (newTree.getBalanceFactor() < -1 && newTree.rightNode.rightNode.find(song.getSongName()) != null) {
                newTree = newTree.leftRotate();
            } else if (newTree.getBalanceFactor() < -1 && newTree.rightNode.leftNode.find(song.getSongName()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (song.getSongName().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByName(song), rightNode);
            if (newTree.getBalanceFactor() > 1 && newTree.leftNode.leftNode.find(song.getSongName()) != null) {
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor() > 1 && newTree.leftNode.rightNode.find(song.getSongName()) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        } else if (song.getSongName().compareTo(key) == 0) {
            List<Song> songList = (List<Song>) value;
            songList.add(song);
        }
        return this;
    }

    @Override
    public AVLTree<T> insertByArtistName(Song song) {
        if (song == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (song.getArtistName().compareTo(key) > 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, rightNode.insertByArtistName(song));
            if (newTree.getBalanceFactor() < -1 && newTree.rightNode.rightNode.find(song.getArtistName()) != null) {
                newTree = newTree.leftRotate();
            } else if (newTree.getBalanceFactor() < -1 && newTree.rightNode.leftNode.find(song.getArtistName()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (song.getArtistName().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByArtistName(song), rightNode);
            if (newTree.getBalanceFactor() > 1 && newTree.leftNode.leftNode.find(song.getArtistName()) != null) {
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor() > 1 && newTree.leftNode.rightNode.find(song.getArtistName()) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        } else if (song.getArtistName().compareTo(key) == 0) {
            List<Song> songList = (List<Song>) value;
            songList.add(song);
        }
        return this;
    }

    @Override
    public AVLTree<T> insertByReleaseDate(Song song) {
        if (song == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (song.getReleaseDate().compareTo(key) > 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, rightNode.insertByReleaseDate(song));
            if (newTree.getBalanceFactor() < -1 && newTree.rightNode.rightNode.find(song.getReleaseDate()) != null) {
                newTree = newTree.leftRotate();
            } else if (newTree.getBalanceFactor() < -1 && newTree.rightNode.leftNode.find(song.getReleaseDate()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (song.getReleaseDate().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByReleaseDate(song), rightNode);
            if (newTree.getBalanceFactor() > 1 && newTree.leftNode.leftNode.find(song.getReleaseDate()) != null) {
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor() > 1 && newTree.leftNode.rightNode.find(song.getReleaseDate()) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        } else if (song.getReleaseDate().compareTo(key) == 0) {
            List<Song> songList = (List<Song>) value;
            songList.add(song);
        }
        return this;
    }

    @Override
    public AVLTree<T> insertByGenre(String genre, Song song) {
        if (song == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (genre.compareTo(key) > 0) {
            //  AVLTree<T> t = (AVLTree<T>) rightNode.insertByGenre(genre, song);
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, rightNode.insertByGenre(genre, song));
            if (newTree.getBalanceFactor() < -1 && newTree.rightNode.rightNode.find(genre) != null) {
                newTree = newTree.leftRotate();
            } else if (newTree.getBalanceFactor() < -1 && newTree.rightNode.leftNode.find(genre) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (genre.compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByGenre(genre, song), rightNode);
            if (newTree.getBalanceFactor() > 1 && newTree.leftNode.leftNode.find(genre) != null) {
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor() > 1 && newTree.leftNode.rightNode.find(genre) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        } else if (genre.compareTo(key) == 0) {
            List<Song> songList = (List<Song>) value;
            songList.add(song);
        }
        return this;
    }



    public AVLTree<T> rightRotate() {
        AVLTree<T> newParent = (AVLTree<T>) this.leftNode;
        this.leftNode = newParent.rightNode;
        newParent.rightNode = this;
        this.getHeight();      // Update height of this node
        newParent.getHeight(); // Update height of new parent node
        return newParent;
    }

    public AVLTree<T> leftRotate() {
        AVLTree<T> newParent = (AVLTree<T>) this.rightNode;
        this.rightNode = newParent.leftNode;
        newParent.leftNode = this;
        this.getHeight();      // Update height of this node
        newParent.getHeight(); // Update height of new parent node
        return newParent;
    }


    /**
     * Delete node by id
     *
     * @param song The element to be deleted
     * @return
     */
    public Tree<T> deleteById(Song song) {
        if (song == null)
            throw new IllegalArgumentException("Deletion cannot be null");

        AVLTree<T> newTree = null;
        if (song.getId().compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, balanceTree( rightNode.deleteById(song)));
        } else if (song.getId().compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, balanceTree( leftNode.deleteById(song)), rightNode);
        } else {
            if (leftNode instanceof EmptyAVL && rightNode instanceof EmptyAVL) { //Leaf node
                return new EmptyAVL<>();
            } else if (leftNode instanceof EmptyAVL) {  // Right child only
                return new AVLTree<>(rightNode.key, rightNode.value, rightNode.leftNode, rightNode.rightNode);
            } else if (rightNode instanceof EmptyAVL) { //Left child only
                return new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
            } else { //Two children
                AVLTree predecessorT = leftNode.findMaxNode();
                if(predecessorT.equals(leftNode) ){
                    AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, rightNode);
                    return balanceTree(newLeft);
                }else{
                    AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, predecessorT.leftNode);
                    AVLTree<T> newPredecessor = new AVLTree<>(predecessorT.key, (T)predecessorT.value, newLeft, predecessorT.rightNode);
                    return balanceTree(newPredecessor);
                }
            }
        }
        //Balance tree
        return balanceTree(newTree);
    }


    public Tree<T> deleteByName(Song song) {
        if (song == null || song.getSongName() == null || song.getId() == null)
            throw new IllegalArgumentException("Deletion arguments cannot be null");
        String songName = song.getSongName();
        String songId = song.getId();

        AVLTree<T> newTree = null;
        if (songName.compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, balanceTree(rightNode.deleteByName(song)));
        } else if (songName.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, balanceTree(leftNode.deleteByName(song)), rightNode);
        } else { // Song name matches the current node's key
            List<Song> songList = (List<Song>) value;
            songList.removeIf(s -> s.getId().equals(songId));
            //for(Song s: songList)System.out.println(s);
                if (!songList.isEmpty()) {
                    //return this;
                    return new AVLTree<>(key, value, leftNode, rightNode);
                } else {
                    if (leftNode instanceof EmptyAVL && rightNode instanceof EmptyAVL) {
                        // Leaf node without any songs left

                        return new EmptyAVL<>();
                    } else if (leftNode instanceof EmptyAVL) {
                        // Only right child is present
                        // return (AVLTree<T>) rightNode;
                        return new AVLTree<T>(rightNode.key, rightNode.value, rightNode.leftNode, rightNode.rightNode);
                    } else if (rightNode instanceof EmptyAVL) {
                        // Only left child is present
                        //return (AVLTree<T>) leftNode;
                        return new AVLTree<T>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                    } else {
                        // Both children are present, need to find predecessor or successor to replace

                        AVLTree predecessorT = leftNode.findMaxNode();
                        if(predecessorT.equals(leftNode) ){
                            AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, rightNode);
                            return balanceTree(newLeft);
                        }else{
                            AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, predecessorT.leftNode);
                            AVLTree<T> newPredecessor = new AVLTree<>(predecessorT.key, (T)predecessorT.value, newLeft, predecessorT.rightNode);
                            return balanceTree(newPredecessor);
                        }
                    }
                }
            }

        // Balance the tree
        return balanceTree(newTree);
    }

    public Tree<T> deleteByArtistName(Song song) {
        if (song == null || song.getArtistName() == null)
            throw new IllegalArgumentException("Deletion arguments cannot be null");

        String artistName = song.getArtistName();
        String id = song.getId();

        AVLTree<T> newTree = null;
        if (artistName.compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, balanceTree(rightNode.deleteByArtistName(song)));
        } else if (artistName.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, balanceTree(leftNode.deleteByArtistName(song)), rightNode);
        } else { // Artist name matches the current node's key
            List<Song> songList = (List<Song>) value;
            songList.removeIf(s -> s.getId().equals(id));
            if (!songList.isEmpty()) {
                return new AVLTree<>(key, value, leftNode, rightNode);
            } else {
                if (leftNode instanceof EmptyAVL && rightNode instanceof EmptyAVL) {
                    // Leaf node without any songs left
                    return new EmptyAVL<>();
                } else if (leftNode instanceof EmptyAVL) {
                    // Only right child is present
                    // return (AVLTree<T>) rightNode;
                    return new AVLTree<T>(rightNode.key, rightNode.value, rightNode.leftNode, rightNode.rightNode);
                } else if (rightNode instanceof EmptyAVL) {
                    // Only left child is present
                    //return (AVLTree<T>) leftNode;
                    return new AVLTree<T>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                } else {
                    // Both children are present, need to find predecessor or successor to replace
                    AVLTree predecessorT = leftNode.findMaxNode();
                    if(predecessorT.equals(leftNode) ){
                        AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, rightNode);
                        return balanceTree(newLeft);
                    }else{
                        AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, predecessorT.leftNode);
                        AVLTree<T> newPredecessor = new AVLTree<>(predecessorT.key, (T)predecessorT.value, newLeft, predecessorT.rightNode);
                        return balanceTree(newPredecessor);

                    }
                }
            }
        }

        // Balance the tree
        return balanceTree(newTree);
    }

    public Tree<T> deleteByReleaseDate(Song song) {
        if (song == null || song.getReleaseDate() == null)
            throw new IllegalArgumentException("Deletion arguments cannot be null");

        String releaseDate = song.getReleaseDate();
        String id = song.getId();

        AVLTree<T> newTree = null;
        if (releaseDate.compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, balanceTree(rightNode.deleteByReleaseDate(song)));
        } else if (releaseDate.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, balanceTree(leftNode.deleteByReleaseDate(song)), rightNode);
        } else { // Release date matches the current node's key
            List<Song> songList = (List<Song>) value;
            songList.removeIf(s -> s.getId().equals(id));
            if (!songList.isEmpty()) {
                return new AVLTree<>(key, value, leftNode, rightNode);
            } else {
                    if (leftNode instanceof EmptyAVL && rightNode instanceof EmptyAVL) {
                        // Leaf node without any songs left
                        return new EmptyAVL<>();
                    } else if (leftNode instanceof EmptyAVL) {
                        // Only right child is present
                        // return (AVLTree<T>) rightNode;
                        return new AVLTree<T>(rightNode.key, rightNode.value, rightNode.leftNode, rightNode.rightNode);
                    } else if (rightNode instanceof EmptyAVL) {
                        // Only left child is present
                        //return (AVLTree<T>) leftNode;
                        return new AVLTree<T>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                    } else {
                        // Both children are present, need to find predecessor or successor to replace
                        AVLTree predecessorT = leftNode.findMaxNode();
                        if(predecessorT.equals(leftNode) ){
                            AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, rightNode);
                            return balanceTree(newLeft);
                        }else{
                            AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, predecessorT.leftNode);
                            AVLTree<T> newPredecessor = new AVLTree<>(predecessorT.key, (T)predecessorT.value, newLeft, predecessorT.rightNode);
                            return balanceTree(newPredecessor);

                        }
                    }
                }
            }
        return balanceTree(newTree);
    }

    public Tree<T> deleteByGenre(String genre, Song song) {
        if (song == null || song.getId() == null || genre == null)
            throw new IllegalArgumentException("Deletion arguments cannot be null");

        AVLTree<T> newTree = null;
        if (genre.compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, balanceTree(rightNode.deleteByGenre(genre, song)));
        } else if (genre.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, balanceTree(leftNode.deleteByGenre(genre, song)), rightNode);
        } else { // Genre matches the current node's key
            List<Song> songList = (List<Song>) value;
            songList.removeIf(s -> s.getId().equals(song.getId()));
            if (!songList.isEmpty()) {
                return new AVLTree<>(key, value, leftNode, rightNode);
            } else {
                if (leftNode instanceof EmptyAVL && rightNode instanceof EmptyAVL) {
                    // Leaf node without any songs left
                    return new EmptyAVL<>();
                } else if (leftNode instanceof EmptyAVL) {
                    // Only right child is present
                    // return (AVLTree<T>) rightNode;
                    return new AVLTree<T>(rightNode.key, rightNode.value, rightNode.leftNode, rightNode.rightNode);
                } else if (rightNode instanceof EmptyAVL) {
                    // Only left child is present
                    //return (AVLTree<T>) leftNode;
                    return new AVLTree<T>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                } else {
                    AVLTree predecessorT = leftNode.findMaxNode();
                    if(predecessorT.equals(leftNode) ){
                        AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, rightNode);
                        return balanceTree(newLeft);
                    }else{
                        AVLTree<T> newLeft = (AVLTree<T>) new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, predecessorT.leftNode);
                        AVLTree<T> newPredecessor = new AVLTree<>(predecessorT.key, (T)predecessorT.value, newLeft, predecessorT.rightNode);
                        return balanceTree(newPredecessor);
                    }
                }
            }
        }
        return balanceTree(newTree);
    }

    private Tree<T> balanceTree(Tree<T> tree) {

        int balanceFactor = tree.getBalanceFactor();

        if (balanceFactor < -1) {
            // Right subtree is heavier
            return balanceRightHeavy((AVLTree<T>) tree);
        } else if (balanceFactor > 1) {
            // Left subtree is heavier
            return balanceLeftHeavy((AVLTree<T>) tree);
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
     * Helper for deleteById()
     * To find the
     * Finds the rightmost (largest) element in the left subtree
     *
     * @return the rightmost element
     * @author
     */
        public AVLTree<T> findMaxNode() {
            if (this.rightNode instanceof EmptyAVL ) {
                return this;
            } else {
                return this.rightNode.findMaxNode();
            }
        }

    public class EmptyAVL<T> extends EmptyTree<T> {
            @Override
            public Tree<T> insertById(Song song) {
                List<Song> songList = new ArrayList<>();
                songList.add(song);
                AVLTree<T> newTree = new AVLTree<T>(song.getId(), (T) songList);
                return newTree;
            }

            @Override
            public Tree<T> insertByName(Song song) {
                List<Song> songList = new ArrayList<>();
                songList.add(song);
                AVLTree<T> newTree = new AVLTree<T>(song.getSongName(), (T) songList);
                return newTree;
            }

            @Override
            public Tree<T> insertByArtistName(Song song) {
                List<Song> songList = new ArrayList<>();
                songList.add(song);// The creation of a new Tree, hence, return tree.
                return new AVLTree<T>(song.getArtistName(), (T) songList);
            }

            @Override
            public Tree<T> insertByReleaseDate(Song song) {
                List<Song> songList = new ArrayList<>();
                songList.add(song);// The creation of a new Tree, hence, return tree.
                return new AVLTree<>(song.getReleaseDate(), (T) songList);
            }

            @Override
            public Tree<T> insertByGenre(String genre, Song song) {
                List<Song> songList = new ArrayList<>();
                songList.add(song);// The creation of a new Tree, hence, return tree.
                return new AVLTree<>(genre, (T) songList);
            }

            @Override
            public Tree<T> deleteById(Song song) {
                return this;
            }

            @Override
            public Tree<T> deleteByName(Song song){
                return this;
            }

            @Override
            public Tree<T> deleteByArtistName(Song song) {
                return this;
            }

            @Override
            public Tree<T> deleteByReleaseDate(Song song) {
                return this;
            }

            @Override
            public Tree<T> deleteByGenre(String genre, Song song) {
                return this;
            }

            @Override
            protected AVLTree<T> findMaxNode() {
                return null;
            }

        @Override
        protected int getBalanceFactor() {
            return 0;
        }
    }
    }

