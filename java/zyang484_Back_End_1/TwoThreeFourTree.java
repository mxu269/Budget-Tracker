// --== CS400 File Header Information ==--
// Name: Zhantao Yang
// Email: zyang484@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: this Tree implementation is partially inspired RBTree implementation provided in previous activity,
// notice that this tree is a tree that can be used in places other than project,
// most methods/fields are set to public INTENTIONALLY, so it can be easily accessed and extended outside if needed.

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 * this is a two three four three implementation
 *
 * @param <T> a generic type that is used to store as data in tree node
 */
public class TwoThreeFourTree<T extends Comparable<T>> {
	/**
	 * an inner node class
	 *
	 * @param <T> a generic type that is used to store as data in tree node
	 */
	protected static class Node<T extends Comparable<T>> {
		public T firstData;
		public T secondData;
		public T thirdData; // three values stored in node

		public Node<T> firstChild;
		public Node<T> secondChild;
		public Node<T> thirdChild;
		public Node<T> fourthChild;
		public Node<T> parent;

		// not mandatory fields, only for convenience
		public int position; // which child is this node, 0 if is root, 1-4 accordingly
		public int numChild; // number of child this node possess
		public int numData; // number of data currently stored

		/**
		 * a default constructor that allows to create a node with a single data
		 *
		 * @param data to store in node
		 */
		public Node(T data) {
			this.firstData = data;
			this.numChild = 0;
			this.numData = 1;
			this.position = 0;
			this.parent = null;
			this.secondData = null;
			this.thirdData = null;
			this.firstChild = null;
			this.secondChild = null;
			this.thirdChild = null;
			this.fourthChild = null;
		}

		/**
		 * Get the child at index.
		 *
		 * @param index of the child that needs to be returned
		 * @return child node at index
		 */
		public Node<T> getChild(int index) {
			switch (index) {
				case 1:
					return firstChild;
				case 2:
					return secondChild;
				case 3:
					return thirdChild;
				case 4:
					return fourthChild;
				default:
					return null; // not expected to be used in any case for number other than 1-4
			}
		}

		/**
		 * Get the data at index.
		 *
		 * @param index of the child that needs to be returned
		 * @return data at index in the node
		 */
		public T getData(int index) {
			switch (index) {
				case 1:
					return firstData;
				case 2:
					return secondData;
				case 3:
					return thirdData;
				default:
					return null; // not expected to be used in any case for number other than 1-4
			}
		}

		/**
		 * a simple toString for easier debug and checking
		 *
		 * @return the string representation of the node
		 */
		@Override
		public String toString() {
			return "[" + firstData + ", " + secondData + ", " + thirdData + "]";
		}

		/**
		 * add data to the node at given position
		 *
		 * @param data to add
		 * @throws NoSuchElementException   if the data is null
		 * @throws IllegalArgumentException if data already exist
		 */
		public void add(T data) {
			if (data == null) throw new NoSuchElementException("data cannot be null");
			if (this.numData == 3) throw new NoSuchElementException("already full");
			if (contains(data)) throw new IllegalArgumentException("data already exist");
			this.thirdData = data;
			// insert to the end, then sort, originally it must be null since this node is expected to be not full
			this.numData++;
			sort();
		}

		/**
		 * helper that checks if node contains certain data
		 *
		 * @param data to check if exist
		 * @return true if exist, false otherwise
		 */
		public boolean contains(T data) {
			if (data == null) return false;
			boolean result;
			try {
				result = data.compareTo(this.firstData) == 0 || data.compareTo(this.secondData) == 0 ||
						data.compareTo(this.thirdData) == 0;
			} catch (NullPointerException n) { // throws this only when it meets null data in node, which means no match
				return false;
			}
			return result;
		}

		/**
		 * a delete method that delete the data from node, return false if not exist
		 *
		 * @param data to delete from node
		 * @throws NoSuchElementException   when data passed in is null
		 * @throws IllegalArgumentException if data not found in node
		 */
		public void delete(T data) {
			if (data == null) throw new NoSuchElementException("null data not accepted"); // data cannot be null
			try {
				if (data.compareTo(this.firstData) == 0) {
					this.firstData = this.secondData;
					this.secondData = this.thirdData;
					this.thirdData = null;
					this.numData--;
				} else if (data.compareTo(this.secondData) == 0) {
					this.secondData = this.thirdData;
					this.thirdData = null;
					this.numData--;
				} else if (data.compareTo(this.thirdData) == 0) {
					this.thirdData = null;
					this.numData--;
				} else { // if data not found in this node
					throw new IllegalArgumentException("no data found in this tree");
				}
			} catch (NullPointerException n) {
				// this happens when data compares to a null data in node, which means no match
				throw new IllegalArgumentException("no data found in this tree");
			}
		}

		/**
		 * helper that sorts the data in node
		 */
		private void sort() {
//			if ((this.numData == 3 &&
//					this.firstData.compareTo(this.secondData) < 0) && this.secondData.compareTo(this.thirdData) < 0 ||
//					(this.numData == 1 && this.firstData != null) ||
//					(this.numData == 2 && this.thirdData == null && this.firstData.compareTo(this.secondData) < 0))
//				return;
			// originally planning to pre-check if sorting is necessary, but it turns out that sorting is NOT necessary
			// pre-check will slow down the insertion by approx. 500ms for 8 million insertions
			this.numData = 0;
			for (int i = 1; i <= 3; i++) {
				if (this.getData(i) != null) numData++;
			}

			if (this.secondData != null && (this.firstData == null || this.firstData.compareTo(this.secondData) > 0)) {
				T temp = this.secondData;
				this.secondData = this.firstData;
				this.firstData = temp;
			}
			if (this.thirdData != null && (this.firstData == null || this.firstData.compareTo(this.thirdData) > 0)) {
				T temp = this.thirdData;
				this.thirdData = this.firstData;
				this.firstData = temp;
			}
			if (this.thirdData != null && (this.secondData == null || this.secondData.compareTo(this.thirdData) > 0)) {
				T temp = this.thirdData;
				this.thirdData = this.secondData;
				this.secondData = temp;
			}
		}

		/**
		 * helper that sorts the data in node
		 */
		private void sortChild() {
			LinkedList<Node<T>> linkedList = new LinkedList<>();
			boolean check = false;
			for (int i = 1; i <= 4; i++) {
				if (getChild(i) != null) linkedList.add(getChild(i));
			}
			this.numChild = linkedList.size();
			if (numChild != numData + 1 && numChild != 0) System.out.println("! " + this.toString());
			if (this.numChild == 0) return; // if there is no child
			for (int i = 1; i <= 4; i++) {
				if (this.numChild == 4 || (i > this.numChild && getChild(i) != null)) {
					// pre-check if sort is necessary
					check = true;
					break;
				}
			}
			if (!check) return; // continue checking if this boolean is true
			linkedList.sort(Comparator.comparing(o -> o.firstData));
			this.firstChild = linkedList.getFirst();

			if (numChild >= 2) this.secondChild = linkedList.get(1);
			else this.secondChild = null;
			if (numChild >= 3) this.thirdChild = linkedList.get(2);
			else this.thirdChild = null;
			if (numChild == 4) this.fourthChild = linkedList.getLast();
			else this.fourthChild = null;
			this.firstChild.position = 1;
			if (this.secondChild != null) this.secondChild.position = 2;
			if (this.thirdChild != null) this.thirdChild.position = 3;
			if (this.fourthChild != null) this.fourthChild.position = 4;
		}

		/**
		 * this method can be useful when key-value pairs or classes with self-defined compareTo is used,
		 * if there is no key, the exact value will returned
		 *
		 * @param data the data to search for
		 * @return the data found, null if no data found
		 */
		public T get(T data) {
			for (int i = 1; i <= 3; i++) {
				if (getData(i).compareTo(data) == 0) return getData(i);
			}
			return null; // data not found
		}
	}

	public Node<T> root; // set to public for easier access in this project

	/**
	 * this method inserts a new node to 2-3-4-Tree
	 *
	 * @param data to insert
	 * @return true if insertion is successful, false if data exist
	 * @throws NoSuchElementException if data is null
	 */
	public boolean insert(T data) {
		try {
			if (data == null) throw new NoSuchElementException("null data is not accepted");
			if (this.root == null) this.root = new Node<>(data); // if the tree is empty
			else insertHelper(this.root, data);
		} catch (IllegalArgumentException i) { // expected when data already exist
			return false;
		}
		return true;
	}

	/**
	 * this is a helper method that helps to insert new node to 2-3-4-Tree
	 *
	 * @param current the current node
	 * @param newData the data of new node to insert
	 * @throws IllegalArgumentException if data already exist
	 */
	private void insertHelper(Node<T> current, T newData) {
		if (current.contains(newData)) throw new IllegalArgumentException("data already exist");
		if (this.root.numChild == 0 && this.root.numData != 3) this.root.add(newData);
		else if (current.numChild != 0) {
			if ((current.numChild > 1 && newData.compareTo(current.getData(1)) < 0))
				insertHelper(current.getChild(1), newData);
			else if (current.numChild > 2 && newData.compareTo(current.getData(2)) < 0
					|| (current.numChild == 2 && newData.compareTo(current.getData(1)) > 0))
				insertHelper(current.getChild(2), newData);
			else if (current.numChild > 3 && newData.compareTo(current.getData(3)) < 0
					|| (current.numChild == 3 && newData.compareTo(current.getData(2)) > 0))
				insertHelper(current.getChild(3), newData);
			else if (current.numChild == 4 && newData.compareTo(current.getData(3)) > 0)
				insertHelper(current.getChild(4), newData);
		} else {
			// when it reaches leaf
			if (current.numData != 3) { // add directly if there is space left
				current.add(newData);
			} else { // split if full
				insertHelper(split(current), newData); // keep recurring at the provided node
			}
		}
	}

	/**
	 * check for existence of a data in the tree
	 *
	 * @param data to check existence
	 * @return true if contains, false otherwise
	 */
	public boolean contains(T data) {
		return searchHelper(this.root, data) != null;
	}

	/**
	 * this method searches for the given data
	 *
	 * @param node to search at
	 * @param data to search for
	 * @return the data has the same key with the given data, null if not found any
	 */
	private T searchHelper(Node<T> node, T data) {
		if (node == null) return null;
		if (node.numChild == 0 && !node.contains(data)) return null;
		if (node.contains(data)) return node.get(data);
//		System.out.println("node " +node);
		if ((node.numChild > 1 && data.compareTo(node.getData(1)) < 0))
			return searchHelper(node.getChild(1), data);
		else if (node.numChild > 2 && data.compareTo(node.getData(2)) < 0
				|| (node.numChild == 2 && data.compareTo(node.getData(1)) > 0))
			return searchHelper(node.getChild(2), data);
		else if (node.numChild > 3 && data.compareTo(node.getData(3)) < 0
				|| (node.numChild == 3 && data.compareTo(node.getData(2)) > 0))
			return searchHelper(node.getChild(3), data);
		else if (node.numChild == 4 && data.compareTo(node.getData(3)) > 0)
			return searchHelper(node.getChild(4), data);
		return null;
	}

	/**
	 * search for the data that compareTo the given data = 0
	 *
	 * @param data with the same key of data that searches for
	 * @return the data has the same key with the given data, null if not found any
	 */
	public T search(T data) {
		return searchHelper(this.root, data);
	}

	/**
	 * make a pre-emptive split
	 *
	 * @param current the node wants to be split
	 * @return the node to restart the insertion after splitting
	 */
	private Node<T> split(Node<T> current) {
		if (current == this.root && this.root.numData == 3) {
			Node<T> tempRoot = this.root;
			this.root = new Node<>(this.root.getData(2));
			this.root.firstChild = new Node<>(tempRoot.getData(1));
			this.root.secondChild = new Node<>(tempRoot.getData(3));

			// change references
			this.root.numChild = 2;
			this.root.firstChild.parent = this.root;
			this.root.firstChild.position = 1;
			this.root.secondChild.parent = this.root;
			this.root.secondChild.position = 2;

			this.root.getChild(1).firstChild = tempRoot.getChild(1);
			this.root.getChild(1).secondChild = tempRoot.getChild(2);
			this.root.getChild(2).firstChild = tempRoot.getChild(3);
			this.root.getChild(2).secondChild = tempRoot.getChild(4);
			this.root.firstChild.numChild = 0;
			this.root.secondChild.numChild = 0;
			// notice that at this point the tempRoot can only have either no child or 4 child

			if (tempRoot.numChild == 4) { // change reference if the original root has child
				this.root.getChild(1).firstChild.parent = this.root.getChild(1);
				this.root.getChild(1).secondChild.parent = this.root.getChild(1);
				this.root.getChild(1).firstChild.position = 1;
				this.root.getChild(1).secondChild.position = 2;
				this.root.getChild(2).firstChild.parent = this.root.getChild(2);
				this.root.getChild(2).secondChild.parent = this.root.getChild(2);
				this.root.getChild(2).firstChild.position = 1;
				this.root.getChild(2).secondChild.position = 2;
				this.root.firstChild.numChild = 2;
				this.root.secondChild.numChild = 2;
			}
			return this.root;
		} else {
			if (current.parent.numData == 3) split(current.parent);
			current.parent.add(current.getData(2));
			current.parent.fourthChild = new Node<>(current.getData(3));
			current.parent.numChild++;
			current.parent.fourthChild.parent = current.parent;
			current.numData = 1;
			current.thirdData = null;
			current.secondData = null; // clear the data after movement
			current.parent.fourthChild.position = 4;
			if (current.numChild == 4) {
				current.parent.fourthChild.firstChild = current.thirdChild;
				current.parent.fourthChild.secondChild = current.fourthChild;
				current.parent.fourthChild.firstChild.parent = current.parent.fourthChild;
				current.parent.fourthChild.secondChild.parent = current.parent.fourthChild;
				current.parent.fourthChild.firstChild.position = 1;
				current.parent.fourthChild.secondChild.position = 2;
				current.parent.fourthChild.numChild = 2;
				current.numChild = 2;
				current.thirdChild = null;
				current.fourthChild = null;  // clear the child after splitting
			}
			current.parent.sortChild();
			return current.parent;
		}
	}

	// deletion starts here

	/**
	 * a remove method that removes data from the tree
	 *
	 * @param data to remove
	 * @return true if deletion is successful, false if data does not exist
	 * @throws NoSuchElementException if data is null
	 */
	public boolean remove(T data) {
		try {
			if (data == null) throw new NoSuchElementException("null data is not accepted");
			if (this.root == null) return false;
			else if (this.root.contains(data) && this.root.numChild == 0 && this.root.numData == 1) this.root = null;
			else if (this.root.numChild == 2 && this.root.firstChild.numData == 1
					&& this.root.secondChild.numData == 1) {
				merge(this.root.firstChild, this.root.secondChild);
				removeHelper(this.root, data, null);
			} else removeHelper(this.root, data, null);
			return true;
		} catch (IllegalArgumentException n) {
			n.printStackTrace();
			return false; // expected when data not found
		}
	}

	/**
	 * this is a helper method that helps to delete new node to 2-3-4-Tree
	 *
	 * @param current       the current node
	 * @param data          the data of new node to delete
	 * @param nodeToReplace the internal node that needs to be replaced with leaf
	 * @throws IllegalArgumentException if data does not exist
	 */
	private void removeHelper(Node<T> current, T data, Node<T> nodeToReplace) {
		if (data == null) throw new NoSuchElementException("null data is not accepted");
		if (current.contains(data) && current.numChild != 0 && nodeToReplace == null) { // not at leaf
			// keep recurring till leaf, go for successor
			nodeToReplace = current;
			if ((current.numChild > 1 && data.compareTo(current.getData(1)) == 0)) {
				rotate(current.secondChild, current.firstChild);
				current = merge(current.secondChild, current.firstChild);
			} else if (current.numChild > 2 && data.compareTo(current.getData(2)) == 0
					|| (current.numChild == 2 && data.compareTo(current.getData(1)) > 0)) {
				rotate(current.thirdChild, current.secondChild);
				current = merge(current.thirdChild, current.secondChild);
			} else if (current.numChild > 3 && data.compareTo(current.getData(3)) == 0
					|| (current.numChild == 3 && data.compareTo(current.getData(2)) > 0)) {
				rotate(current.fourthChild, current.thirdChild);
				current = merge(current.fourthChild, current.thirdChild);
			}
			if (!current.contains(data)) removeHelper(current, data, null);
			else
			if ((current.numChild > 1 && data.compareTo(current.getData(1)) == 0)) {
				removeHelper(current.getChild(2), data, nodeToReplace);
			} else if (current.numChild > 2 && data.compareTo(current.getData(2)) == 0
					|| (current.numChild == 2 && data.compareTo(current.getData(1)) > 0)) {
				removeHelper(current.getChild(3), data, nodeToReplace);
			} else if (current.numChild > 3 && data.compareTo(current.getData(3)) == 0
					|| (current.numChild == 3 && data.compareTo(current.getData(2)) > 0)) {
				removeHelper(current.getChild(4), data, nodeToReplace);
			}
		} else if (current == this.root && this.root.numChild == 0) this.root.delete(data);
			// no need of handling exception, let tree.remove() handle
		else if (current.numChild != 0) { // not at leaf
			if ((current.numChild > 1 && data.compareTo(current.getData(1)) < 0)) {
				rotate(current.firstChild, current.secondChild);
				current = merge(current.firstChild, current.secondChild);
			} else if (current.numChild > 2 && data.compareTo(current.getData(2)) < 0
					|| (current.numChild == 2 && data.compareTo(current.getData(1)) > 0)) {
				rotate(current.secondChild, current.firstChild);
				current = merge(current.secondChild, current.firstChild);
			} else if (current.numChild > 3 && data.compareTo(current.getData(3)) < 0
					|| (current.numChild == 3 && data.compareTo(current.getData(2)) > 0)) {
				rotate(current.thirdChild, current.secondChild);
				current = merge(current.thirdChild, current.secondChild);
			} else if (current.numChild == 4 && data.compareTo(current.getData(3)) > 0) {
				rotate(current.fourthChild, current.thirdChild);
				current = merge(current.fourthChild, current.thirdChild);
			}
			if ((current.numChild > 1 && data.compareTo(current.getData(1)) < 0)) {
				removeHelper(current.getChild(1), data, nodeToReplace);
			} else if (current.numChild > 2 && data.compareTo(current.getData(2)) < 0
					|| (current.numChild == 2 && data.compareTo(current.getData(1)) > 0)) {
				removeHelper(current.getChild(2), data, nodeToReplace);
			} else if (current.numChild > 3 && data.compareTo(current.getData(3)) < 0
					|| (current.numChild == 3 && data.compareTo(current.getData(2)) > 0)) {
				removeHelper(current.getChild(3), data, nodeToReplace);
			} else if (current.numChild == 4 && data.compareTo(current.getData(3)) > 0) {
				removeHelper(current.getChild(4), data, nodeToReplace);
			}
		} else {
			if (nodeToReplace != null) { // replace if data to remove is not at leaf
				nodeToReplace.delete(data);
				nodeToReplace.add(current.firstData);
				current.delete(current.firstData);
				current.add(data);
			}
			if (!current.contains(data)) throw new IllegalArgumentException("data not found in tree");
			if (current.numData == 1) {
				Node<T> leftSibling = current.parent.getChild(current.position - 1);
				Node<T> rightSibling = current.parent.getChild(current.position + 1);
				if (leftSibling != null) {
					rotate(current, leftSibling);
					current = merge(current, leftSibling);
				} else { // it is impossible that both left and right siblings are null
					rotate(current, rightSibling);
					current = merge(current, rightSibling);
				}
			} // if current has more than 1 data no special handling expected
			if (current.contains(data))
				current.delete(data);
			else {
				if ((current.numChild > 1 && data.compareTo(current.getData(1)) < 0)) {
					current.firstChild.delete(data);
				} else if (current.numChild > 2 && data.compareTo(current.getData(2)) < 0
						|| (current.numChild == 2 && data.compareTo(current.getData(1)) > 0)) {
					current.secondChild.delete(data);
				} else if (current.numChild > 3 && data.compareTo(current.getData(3)) < 0
						|| (current.numChild == 3 && data.compareTo(current.getData(2)) > 0)) {
					current.thirdChild.delete(data);
				} else if (current.numChild == 4 && data.compareTo(current.getData(3)) > 0) {
					current.fourthChild.delete(data);
				}
			}
		}
	}

	/**
	 * merge the node with its sibling so it maintains property after deletion
	 *
	 * @param node    the node that contains value that needs to be deleted
	 * @param sibling the sibling that needs to be merged with node
	 * @return the node need to be checked during fixing
	 */
	private Node<T> merge(Node<T> node, Node<T> sibling) {
		if (node == null || sibling == null) return this.root;
		if (node.parent.numChild == 2 && node.parent == this.root && this.root.numData == 1 &&
				this.root.firstChild.numData == 1 && this.root.secondChild.numData == 1) {
			// special this.root merge required
			this.root.add(sibling.firstData);
			this.root.add(node.firstData);
			this.root.firstChild = node.firstChild;
			this.root.secondChild = node.secondChild;
			this.root.thirdChild = sibling.firstChild;
			this.root.fourthChild = sibling.secondChild;
			this.root.numData = 3;
			this.root.numChild = 0; // assume it has no child, change later if it does
			this.root.sort();
			if (node.numChild != 0) { // then this.root has child
				this.root.firstChild.parent = this.root;
				this.root.secondChild.parent = this.root;
				this.root.thirdChild.parent = this.root;
				this.root.fourthChild.parent = this.root;
				this.root.numChild = 4;
				this.root.firstChild.position = 1;
				this.root.secondChild.position = 2;
				this.root.thirdChild.position = 3;
				this.root.fourthChild.position = 4;
				this.root.sortChild();
			}
			return this.root;
		} else if (node.numData == 1 && sibling.numData == 1 && node.parent.numChild > 2) {
			// sibling will be deleted afterward
			if (node.position < sibling.position) { // this is a right sibling
				node.secondData = node.parent.getData(node.position);
				node.parent.delete(node.parent.getData(node.position));
			} else {
				node.secondData = node.parent.getData(node.position - 1);
				node.parent.delete(node.parent.getData(node.position - 1));
			}
			node.thirdData = sibling.firstData; // sibling will be deleted afterward
			node.numData = 3;
			if (sibling.position == 1) node.parent.firstChild = null;
			else if (sibling.position == 2) node.parent.secondChild = null;
			else if (sibling.position == 3) node.parent.thirdChild = null;
			else node.parent.fourthChild = null;
			node.thirdChild = sibling.firstChild;
			node.fourthChild = sibling.secondChild;
			if (sibling.numChild != 0) {
				node.thirdChild.parent = node;
				node.fourthChild.parent = node;
				node.thirdChild.position = 3;
				node.fourthChild.position = 4;
				node.numChild = 4;
			}
			node.parent.numChild--;
			node.parent.sortChild();
			node.parent.sort();
			node.sort();
			node.sortChild();
			return node.parent;
		} else return node.parent;
	}

	/**
	 * rotate the node so that it maintains tree property after deletion
	 *
	 * @param node    the node that contains the value that needs to be deleted
	 * @param sibling the sibling that needs to be rotated to node
	 */
	private void rotate(Node<T> node, Node<T> sibling) {
		if (node == null || sibling == null) return;
		if (node.numData != 1 || sibling.numData <= 1 || node.parent.numData < 1) return;
		if (node.position < sibling.position) { // this is a right sibling, needs left rotation
			node.add(node.parent.getData(node.position)); // rotate data from parent to current node
			node.parent.delete(node.parent.getData(node.position)); // delete the data from parent after rotation
			node.parent.add(sibling.firstData); // rotate from sibling to parent
			sibling.delete(sibling.firstData);

			if (node.numChild != 0) { // if node child is not 0 then sibling child must not be 0,
				// since sibling has >=2 data, it also has >=3 child
				node.fourthChild = sibling.firstChild;
				node.fourthChild.position = 4;
				node.fourthChild.parent = node;
				node.numChild++;
				sibling.firstChild = null;
				sibling.numChild--;
				sibling.sortChild();
				node.sortChild();
			}
		} else { // this is a left sibling, need right rotation
			node.add(node.parent.getData(node.position - 1)); // rotate data from parent to current node
			node.parent.delete(node.parent.getData(node.position - 1)); // delete the data from parent after rotation
			node.parent.add(sibling.getData(sibling.numData)); // rotate from sibling's last data to parent
			sibling.delete(sibling.getData(sibling.numData));

			if (node.numChild != 0) { // if node child is not 0 then sibling child must not be 0,
				// since sibling has >=2 data, it also has >=3 child
				node.fourthChild = sibling.getChild(sibling.numChild); // get the last child of sibling
				node.secondChild.position = 2;
				node.firstChild.position = 1;
				node.fourthChild.position = 4;
				node.fourthChild.parent = node;
				node.numChild++;
				if (sibling.numChild == 3) sibling.thirdChild = null;
				else sibling.fourthChild = null;
				sibling.numChild--;
				node.sortChild();
				sibling.sortChild();
			}
		}
	}

	/**
	 * a String representation of the tree in order
	 *
	 * @return a string representation of the traversed items
	 */
	@Override
	public String toString() {
		if (this.root == null) return "[]";
		else {
			LinkedList<String> stringLinkedList = new LinkedList<>();
			for (T data : inOrderTraverser(root, new LinkedList<>())) {
				stringLinkedList.add(data.toString());
			}
			return stringLinkedList.toString();
		}
	}

	/**
	 * traverse the whole tree in order
	 *
	 * @param node            the node to stat traversing
	 * @param currentDataList a linked list of currently traversed item
	 * @return a string linked list representation of the traversed items
	 */
	private LinkedList<T> inOrderTraverser(Node<T> node, LinkedList<T> currentDataList) {
		if (node != null) {
			if (node.numChild >= 1) inOrderTraverser(node.firstChild, currentDataList);
			if (node.numData >= 1) currentDataList.add(node.getData(1));
			if (node.numChild >= 2) inOrderTraverser(node.secondChild, currentDataList);
			if (node.numData >= 2) currentDataList.add(node.getData(2));
			if (node.numChild >= 3) inOrderTraverser(node.thirdChild, currentDataList);
			if (node.numData >= 3) currentDataList.add(node.getData(3));
			if (node.numChild == 4) inOrderTraverser(node.fourthChild, currentDataList);
		}
		return currentDataList;
	}

	/**
	 * a data linked list representation of the tree in order
	 *
	 * @return a linked list representation of the traversed items
	 */
	public LinkedList<T> toLinkedList() {
		if (this.root == null) return new LinkedList<>();
		else return inOrderTraverser(root, new LinkedList<>());
	}
}
