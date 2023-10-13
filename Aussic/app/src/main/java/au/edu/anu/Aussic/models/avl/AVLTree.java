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
            if (newTree.getBalanceFactor()<-1 && newTree.rightNode.rightNode.find(song.getId()) != null) {
                newTree = newTree.leftRotate();
            }else if (newTree.getBalanceFactor()<-1 && newTree.rightNode.leftNode.find(song.getId()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
            // COMPLETE
        } else if (song.getId().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertById(song), rightNode);
            if(newTree.getBalanceFactor()>1 && newTree.leftNode.leftNode.find(song.getId()) != null){
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor()>1 && newTree.leftNode.rightNode.find(song.getId()) != null) {
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
            if (newTree.getBalanceFactor()<-1 && newTree.rightNode.rightNode.find(song.getSongName()) != null) {
                newTree = newTree.leftRotate();
            }else if (newTree.getBalanceFactor()<-1 && newTree.rightNode.leftNode.find(song.getSongName()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (song.getSongName().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByName(song), rightNode);
            if(newTree.getBalanceFactor()>1 && newTree.leftNode.leftNode.find(song.getSongName()) != null){
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor()>1 && newTree.leftNode.rightNode.find(song.getSongName()) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        }else if (song.getSongName().compareTo(key) == 0){
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
            if (newTree.getBalanceFactor()<-1 && newTree.rightNode.rightNode.find(song.getArtistName()) != null) {
                newTree = newTree.leftRotate();
            }else if (newTree.getBalanceFactor()<-1 && newTree.rightNode.leftNode.find(song.getArtistName()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (song.getArtistName().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByArtistName(song), rightNode);
            if(newTree.getBalanceFactor()>1 && newTree.leftNode.leftNode.find(song.getSongName()) != null){
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor()>1 && newTree.leftNode.rightNode.find(song.getArtistName()) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        }else if (song.getArtistName().compareTo(key) == 0){
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
            AVLTree<T> t = (AVLTree<T>) rightNode.insertByReleaseDate(song);
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode, t);
            if (newTree.getBalanceFactor()<-1 && newTree.rightNode.rightNode.find(song.getReleaseDate()) != null) {
                newTree = newTree.leftRotate();
            }else if (newTree.getBalanceFactor()<-1 && newTree.rightNode.leftNode.find(song.getReleaseDate()) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (song.getReleaseDate().compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByReleaseDate(song), rightNode);
            if(newTree.getBalanceFactor()>1 && newTree.leftNode.leftNode.find(song.getReleaseDate()) != null){
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor()>1 && newTree.leftNode.rightNode.find(song.getReleaseDate()) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        }else if (song.getReleaseDate().compareTo(key) == 0){
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
            if (newTree.getBalanceFactor()<-1 && newTree.rightNode.rightNode.find(genre) != null) {
                newTree = newTree.leftRotate();
            }else if (newTree.getBalanceFactor()<-1 && newTree.rightNode.leftNode.find(genre) != null) {
                AVLTree<T> rightNode = (AVLTree<T>) newTree.rightNode;
                newTree.rightNode = rightNode.rightRotate();
                newTree = newTree.leftRotate();
            }

            return newTree;
        } else if (genre.compareTo(key) < 0) {
            AVLTree<T> newTree = new AVLTree<>(key, value, leftNode.insertByGenre(genre, song), rightNode);
            if(newTree.getBalanceFactor()>1 && newTree.leftNode.leftNode.find(genre) != null){
                newTree = newTree.rightRotate();
            } else if (newTree.getBalanceFactor()>1 && newTree.leftNode.rightNode.find(genre) != null) {
                AVLTree<T> leftNode = (AVLTree<T>) newTree.leftNode;
                newTree.leftNode = leftNode.leftRotate();
                newTree = newTree.rightRotate();
            }
            return newTree;
        }else if (genre.compareTo(key) == 0){
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
     * Delete the input element
     * @param element The element to be deleted
     * @return
     */
 //   public AVLTree<T> delete(T element) {
 //       if (element == null || find(element) == null)
 //           throw new IllegalArgumentException("Invalid input! Cannot find such element in the tree.");
 //       AVLTree<T> copyTree; //Make sure its immutability
 //       copyTree = deleteNode(this, element);
 //       return copyTree;
 //   }

  //  private AVLTree<T> deleteNode(AVLTree<T> treeNode, T element) {
  //      if (treeNode == null) {
  //          return null;
  //      }
//
  //      // Avoid the cast. Handle cases where leftNode or rightNode might be instances of EmptyAVL.
  //      if (treeNode.leftNode instanceof EmptyAVL) {
  //          treeNode.leftNode = null;
  //      }
  //      if (treeNode.rightNode instanceof EmptyAVL) {
  //          treeNode.rightNode = null;
  //      }
//
  //      AVLTree<T> newTreeNode = null;
//
  //      if (element.compareTo(treeNode.value) < 0) {
  //          AVLTree<T> copyLeftNode = deleteNode((AVLTree<T>) treeNode.leftNode, element);
  //          newTreeNode = new AVLTree<>(treeNode.value, copyLeftNode != null ? copyLeftNode : new EmptyAVL<>(), treeNode.rightNode);
  //      } else if (element.compareTo(treeNode.value) > 0) {
  //          AVLTree<T> copyRightNode = deleteNode((AVLTree<T>) treeNode.rightNode, element);
  //          newTreeNode = new AVLTree<>(treeNode.value, treeNode.leftNode, copyRightNode != null ? copyRightNode : new EmptyAVL<>());
  //      } else {
  //          // Node with the element to be deleted found
  //          if (treeNode.leftNode == null && treeNode.rightNode == null) {
  //              // No children / Leaf node
  //              return null;
  //          } else if (treeNode.leftNode == null) {
  //              // Right child only
  //              return (AVLTree<T>) treeNode.rightNode;
  //          } else if (treeNode.rightNode == null) {
  //              // Left child only
  //              return (AVLTree<T>) treeNode.leftNode;
  //          } else {
  //              // Two children
  //              AVLTree<T> newRight = (AVLTree<T>) treeNode.rightNode;
  //              T successor = newRight.findMinInRight();
  //              newRight = deleteNode(newRight, successor);
  //              newTreeNode = new AVLTree<>(successor, treeNode.leftNode, newRight != null ? newRight : new EmptyAVL<>());
  //          }
  //      }
  //      return balanceTree(newTreeNode);
  //  }




    /**
     * Helper for deleteNode()
     * To find the
     * Finds the leftmost (smallest) element in the right subtree
     *
     * @return the leftmost element
     * @author
     */
//    public T findMinInRight() {
//        if (this.leftNode == null || this.leftNode.value == null) {
//            return this.value;
//        } else {
//            return new AVLTree<T>(this.leftNode.value, this.leftNode.leftNode, this.leftNode.rightNode).findMinInRight();
//        }
//    }

    // 中序遍历方法
 //   public List<T> inorderTraversal() {
 //       List<T> result = new ArrayList<>();
 //       inorderTraversal(this, result);
 //       return result;
 //   }

 //  private void inorderTraversal(AVLTree<T> node, List<T> result) {
 //      if (node == null || node.value == null) {
 //          return;
 //      }

 //      // 遍历左子树
 //      if (node.leftNode instanceof AVLTree) {
 //          node.inorderTraversal((AVLTree<T>) node.leftNode, result);
 //      }

 //      // 访问当前节点
 //      result.add(node.value);

 //      // 遍历右子树
 //      if (node.rightNode instanceof AVLTree) {
 //          node.inorderTraversal((AVLTree<T>) node.rightNode, result);
 //      }
 //  }


 //   public Set<T> searchAll(T element) {
 //       Set<T> resultSet = new HashSet<>();
 //       searchAll(this, element, resultSet);
 //       return resultSet;
 //   }

 //  private void searchAll(AVLTree<T> node, T element, Set<T> resultSet) {
 //      if (node == null || node.value == null) {
 //          return;
 //      }

 //      int comparison = element.compareTo(node.value);
 //      if (comparison == 0) {
 //          resultSet.add(node.value);
 //      }

 //      if (node.leftNode instanceof AVLTree) {
 //          searchAll((AVLTree<T>) node.leftNode, element, resultSet);
 //      }

 //      if (node.rightNode instanceof AVLTree) {
 //          searchAll((AVLTree<T>) node.rightNode, element, resultSet);
 //      }
 //  }




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
            List<Song> songList = new ArrayList<>()    ;
            songList.add(song);// The creation of a new Tree, hence, return tree.
            return new BinarySearchTree<T>(song.getArtistName(), (T) songList) ;
        }

        @Override
        public Tree<T> insertByReleaseDate(Song song) {
            List<Song> songList = new ArrayList<>()    ;
            songList.add(song);// The creation of a new Tree, hence, return tree.
            return new BinarySearchTree<T>(song.getReleaseDate(), (T) songList) ;
        }

        @Override
        public Tree<T> insertByGenre(String genre, Song song) {
            List<Song> songList = new ArrayList<>();
            songList.add(song);// The creation of a new Tree, hence, return tree.
            return new BinarySearchTree<T>(genre, (T) songList) ;
        }

        @Override
        public Tree<T> delete(T song) {
            //return new AVLTree<T>(element);
            return null;
        }
    }
    }
