package au.edu.anu.Aussic.models.avl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.edu.anu.Aussic.models.entity.Song;

public class AVLTree<T> extends BinarySearchTree<T> {
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
            AVLTree<T> t = (AVLTree<T>) rightNode.insertById(song);
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, t);
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
            AVLTree<T> t = (AVLTree<T>) rightNode.insertByName(song);
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, t);
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
            AVLTree<T> t = (AVLTree<T>) rightNode.insertByArtistName(song);
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, t);
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
            Tree<T> tree = rightNode.insertByReleaseDate(song);

            // Type tree cannot be directly casted into AVLtree
            AVLTree<T> t = (AVLTree<T>) tree;
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, t);
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
    public AVLTree<T> deleteById(Song song) {
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
            AVLTree<T> t = (AVLTree<T>) rightNode.deleteById(song);
            newTree = new AVLTree<>(key, value, leftNode, t != null ? t : new EmptyAVL<>());
        } else if (song.getId().compareTo(key) < 0 && leftNode != null) {
            AVLTree<T> t = (AVLTree<T>) leftNode.deleteById(song);
            newTree = new AVLTree<>(key, value, t != null ? t : new EmptyAVL<>(), rightNode);
        } else {
            //The song's ID is equal to current node's key
            //Node to be deleted is found
            if (leftNode == null && rightNode == null) { //Leaf node
                return null;
            } else if (leftNode == null) {  // Right child only
                return (AVLTree<T>) rightNode;
            } else if (rightNode == null) { //Left child only
                return (AVLTree<T>) leftNode;
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
                newLeft = newLeft.deleteById(predecessor);
                newTree = new AVLTree<T>(predecessor.getId(), predecessorT, (newLeft != null ? newLeft : new EmptyAVL<>()), rightNode);
            }
        }

        //Balance tree
        return balanceTree(newTree);
    }

    public AVLTree<T> deleteByName(String songName, String songId) {
        if (songName == null || songId == null)
            throw new IllegalArgumentException("Deletion arguments cannot be null");

        if (leftNode instanceof EmptyAVL) {
            leftNode = null;
        }
        if (rightNode instanceof EmptyAVL) {
            rightNode = null;
        }

        AVLTree<T> newTree = null;
        if (songName.compareTo(key) > 0 && rightNode != null) {
            AVLTree<T> t = (AVLTree<T>) rightNode.deleteByName(songName, songId);
            newTree = new AVLTree<>(key, value, leftNode, t != null ? t : new EmptyAVL<>());
        } else if (songName.compareTo(key) < 0 && leftNode != null) {
            AVLTree<T> t = (AVLTree<T>) leftNode.deleteByName(songName, songId);
            newTree = new AVLTree<>(key, value, t != null ? t : new EmptyAVL<>(), rightNode);
        } else { // Song name matches the current node's key
            List<Song> songList = (List<Song>) value;
            if (songList.size() == 1 && songList.get(0).getId().equals(songId)) {
                // If it's the only song in the node, delete the entire node
                if (leftNode == null && rightNode == null) { // Leaf node
                    return null;
                } else if (leftNode == null) {  // Right child only
                    return (AVLTree<T>) rightNode;
                } else if (rightNode == null) { // Left child only
                    return (AVLTree<T>) leftNode;
                } else { // Node with two children
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
                    newLeft = newLeft.deleteById(predecessor);
                    newTree = new AVLTree<T>(predecessor.getSongName(), predecessorT, (newLeft != null ? newLeft : new EmptyAVL<>()), rightNode);
                }
            } else {
                // If there are multiple songs in the node, just remove the song with matching ID
                songList.removeIf(song -> song.getId().equals(songId));
            }
        }
        // Balance the tree
        assert newTree != null;
        return balanceTree(newTree);
    }


        private AVLTree<T> balanceTree (AVLTree <T> newTree) {
            if (newTree.getBalanceFactor() < -1) {
                newTree = newTree.leftRotate();
            } else if (newTree.getBalanceFactor() < -1) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            } else if (newTree.getBalanceFactor() > 1) {
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor() > 1) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
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
                return new BinarySearchTree<T>(song.getReleaseDate(), (T) songList);
            }

            @Override
            public Tree<T> insertByGenre(String genre, Song song) {
                List<Song> songList = new ArrayList<>();
                songList.add(song);// The creation of a new Tree, hence, return tree.
                return new BinarySearchTree<T>(genre, (T) songList);
            }

            @Override
            public Tree<T> deleteById(Song song) {
                return null;
            }

            @Override
            public Tree<T> deleteByName(String songName, String songId){
                return null;
            }

        }
    }

