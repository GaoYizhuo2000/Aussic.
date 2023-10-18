package au.edu.anu.Aussic.models.avl;

/**
 * @author: u7581818, Oscar Wei
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;

public class AVLTree<T> extends BinarySearchTree<T> {
    public AVLTree() {
        super();
    }

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
        Tree<T> newParent = this.leftNode;
        Tree<T> newLeftOfCurrent = newParent.rightNode;
        newParent.rightNode = this;
        this.leftNode = newLeftOfCurrent;
        return (AVLTree<T>) newParent;
    }

    public AVLTree<T> leftRotate() {
        Tree<T> newParent = this.rightNode;
        Tree<T> newRightOfCurrent = newParent.leftNode;
        newParent.leftNode = this;
        this.rightNode = newRightOfCurrent;
        return (AVLTree<T>) newParent;

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

        if (leftNode instanceof EmptyAVL) {
            leftNode = null;
        }
        if (rightNode instanceof EmptyAVL) {
            rightNode = null;
        }

        AVLTree<T> newTree = null;
        if (song.getId().compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, rightNode.deleteById(song));
        } else if (song.getId().compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, leftNode.deleteById(song), rightNode);
        } else {
            //The song's ID is equal to current node's key
            //Node to be deleted is found
            if (leftNode instanceof EmptyAVL && rightNode instanceof EmptyAVL) { //Leaf node
                return new EmptyAVL<>();
            } else if (leftNode instanceof EmptyAVL) {  // Right child only
                //return (AVLTree<T>) rightNode;
                return new AVLTree<>(rightNode.key, rightNode.value, rightNode.leftNode, rightNode.leftNode);
            } else if (rightNode instanceof EmptyAVL) { //Left child only
                //return (AVLTree<T>) leftNode;
                return new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
            } else { //Two children
                AVLTree<T> newLeft = new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                T predecessorT = newLeft.findMaxNode();
                Song predecessor = null;
                if (predecessorT instanceof List<?>) {
                    List<Song> songList = (List<Song>) predecessorT;
                    System.out.println("Size of songList: " + songList.size());
                    if (!songList.isEmpty()) {
                        predecessor = songList.get(0);
                        System.out.println("Retrieved song: " + predecessor);
                    } else {
                        System.out.println("songList is empty");
                    }
                } else {
                    System.out.println("predecessorT is not an instance of List: " + predecessorT);
                }
                if (predecessor == null) {
                    throw new IllegalStateException("Predecessor song was not found.");
                }
                if (newLeft.find(predecessor.getId()) != null) {
                    System.out.println("Predecessor exists before deletion");
                } else {
                    System.out.println("Predecessor does not exist before deletion");
                }
                newLeft = (AVLTree<T>) newLeft.deleteById(predecessor);
                newTree = new AVLTree<T>(predecessor.getId(), predecessorT, newLeft, rightNode);
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
            newTree = new AVLTree<>(key, value, leftNode, rightNode.deleteByName(song));
        } else if (songName.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, leftNode.deleteByName(song), rightNode);
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
                        AVLTree<T> newLeft = new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                        T predecessorT = newLeft.findMaxNode();
                        Song predecessor = null;
                        if (predecessorT instanceof List<?>) {
                            List<Song> predecessorList = (List<Song>) predecessorT;
                            if (!predecessorList.isEmpty()) {
                                predecessor = predecessorList.get(0);
                            }
                        }
                        if (predecessor == null) {
                            throw new IllegalStateException("Predecessor song was not found.");
                        }
                        newLeft = (AVLTree<T>) newLeft.deleteByName(predecessor);
                        List<Song> newPredecessorList = new ArrayList<>();
                        newPredecessorList.add(predecessor);
                        return new AVLTree<T>(predecessor.getSongName(), (T) newPredecessorList, newLeft, rightNode);
                        //return new AVLTree<T>(predecessor.getSongName(), predecessorT, newLeft, rightNode);
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

        AVLTree<T> newTree = null;
        if (artistName.compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, rightNode.deleteByArtistName(song));
        } else if (artistName.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, leftNode.deleteByArtistName(song), rightNode);
        } else { // Artist name matches the current node's key
            List<Song> songList = (List<Song>) value;
            songList.removeIf(s -> s.getArtistName().equals(artistName));
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
                    AVLTree<T> newLeft = new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                    T predecessorT = newLeft.findMaxNode();
                    Song predecessor = null;
                    if (predecessorT instanceof List<?>) {
                        List<Song> predecessorList = (List<Song>) predecessorT;
                        if (!predecessorList.isEmpty()) {
                            predecessor = predecessorList.get(0);
                        }
                    }
                    if (predecessor == null) {
                        throw new IllegalStateException("Predecessor song was not found.");
                    }
                    newLeft = (AVLTree<T>) newLeft.deleteByArtistName(predecessor);
                    List<Song> newPredecessorList = new ArrayList<>();
                    newPredecessorList.add(predecessor);
                    return new AVLTree<T>(predecessor.getArtistName(), (T) newPredecessorList, newLeft, rightNode);
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

        AVLTree<T> newTree = null;
        if (releaseDate.compareTo(key) > 0 && rightNode != null) {
            newTree = new AVLTree<>(key, value, leftNode, rightNode.deleteByReleaseDate(song));
        } else if (releaseDate.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, leftNode.deleteByReleaseDate(song), rightNode);
        } else { // Release date matches the current node's key
            List<Song> songList = (List<Song>) value;
            songList.removeIf(s -> s.getReleaseDate().equals(releaseDate));
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
                        AVLTree<T> newLeft = new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                        T predecessorT = newLeft.findMaxNode();
                        Song predecessor = null;
                        if (predecessorT instanceof List<?>) {
                            List<Song> predecessorList = (List<Song>) predecessorT;
                            if (!predecessorList.isEmpty()) {
                                predecessor = predecessorList.get(0);
                            }
                        }
                        if (predecessor == null) {
                            throw new IllegalStateException("Predecessor song was not found.");
                        }
                        newLeft = (AVLTree<T>) newLeft.deleteByReleaseDate(predecessor);
                        List<Song> newPredecessorList = new ArrayList<>();
                        newPredecessorList.add(predecessor);
                        return new AVLTree<T>(predecessor.getReleaseDate(), (T) newPredecessorList, newLeft, rightNode);
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
            newTree = new AVLTree<>(key, value, leftNode, rightNode.deleteByGenre(genre, song));
        } else if (genre.compareTo(key) < 0 && leftNode != null) {
            newTree = new AVLTree<>(key, value, leftNode.deleteByGenre(genre, song), rightNode);
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
                    // Both children are present, need to find predecessor or successor to replace
                    AVLTree<T> newLeft = new AVLTree<>(leftNode.key, leftNode.value, leftNode.leftNode, leftNode.rightNode);
                    T predecessorT = newLeft.findMaxNode();
                    Song predecessor = null;
                    if (predecessorT instanceof List<?>) {
                        List<Song> predecessorList = (List<Song>) predecessorT;
                        if (!predecessorList.isEmpty()) {
                            predecessor = predecessorList.get(0);
                        }
                    }
                    if (predecessor == null) {
                        throw new IllegalStateException("Predecessor song was not found.");
                    }
                    newLeft = (AVLTree<T>) newLeft.deleteByGenre(genre, predecessor);
                    List<Song> newPredecessorList = new ArrayList<>();
                    newPredecessorList.add(predecessor);
                    return new AVLTree<T>(genre, (T) newPredecessorList, newLeft, rightNode);
                }
            }
        }

        return balanceTree(newTree);
    }


    /**
     *
     * @param node
     * @return
     */
    private AVLTree<T> balanceTree(AVLTree<T> node) {
        if (node.getBalanceFactor() < -1) {  // Right heavy
            // Check if we need a double rotation (right-left)
            if (((AVLTree<T>)node.rightNode).getBalanceFactor() > 0) {
                AVLTree<T> rightNode = (AVLTree<T>) node.rightNode;
                node.rightNode = rightNode.leftRotate();
            }
            node = node.leftRotate();
        } else if (node.getBalanceFactor() > 1) {  // Left heavy
            // Check if we need a double rotation (left-right)
            if (((AVLTree<T>)node.leftNode).getBalanceFactor() < 0) {
                AVLTree<T> leftNode = (AVLTree<T>) node.leftNode;
                node.leftNode = leftNode.rightRotate();
            }
            node = node.rightRotate();
        }
        return node;
    }



    /**
         * Helper for deleteById()
         * To find the
         * Finds the rightmost (largest) element in the left subtree
         *
         * @return the rightmost element
         * @author
         */
        private T findMaxNode() {
            if (this.rightNode == null || (this.rightNode.key == null && this.rightNode.value == null)) {
                return this.value;
            } else {
                return new AVLTree<T>(this.rightNode.key, this.rightNode.value, this.rightNode.leftNode, this.rightNode.rightNode).findMaxNode();
            }
        }


        private class EmptyAVL<T> extends EmptyTree<T> {

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

        }
    }

