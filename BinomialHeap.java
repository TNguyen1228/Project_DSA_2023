package InternalStructure;

public class BinomialHeap {
	private static int EMPTY_HEAP_SIZE = 0;
	private static int EMPTY_MIN_VALUE = -1;
	private Node min;
	private Node first;
	private Node last;
	private int size;
	private int treesNum;

	public BinomialHeap() {
		this(null);
	}

	private BinomialHeap(Node node) {
		this.min = node;
		this.first = node;
		this.last = node;

		// set size and trees count
		if (node != null) {
			this.size = node.getSize();
			treesNum = 1;
		} else {
			this.size = EMPTY_HEAP_SIZE;
			treesNum = 0;
		}
	}

	private void reset() {
		min = null;
		first = null;
		last = null;
		size = EMPTY_HEAP_SIZE;
		treesNum = 0;
	}

	public boolean empty() {
		return this.size == EMPTY_HEAP_SIZE;
	}

	/** fix lại 1 chút cho phù hợp */

	public void insert(int value) {
		Node newNode = new Node(value);
		insert(newNode);
	}

	/**
	 * Thêm vào 1 task có đủ thông tin gồm tên task và deadline
	 */
	public void insert(Node node) {
		BinomialHeap heapToMeld;
		if (empty()) { // first element
			this.first = node;
			this.last = node;
			this.min = node;
			this.size = 1;

		} else { // use meld to add element to heap
			heapToMeld = new BinomialHeap(node);
			this.meld(heapToMeld);
		}
	}

	public void deleteMin() {
		if (empty()) {
			return; // nothing to do
		}

		// collect all min's children to new heap
		BinomialHeap h = new BinomialHeap();
		Node p = this.min.getLeftmostChild();
		if (p != null) {
			while (p != null) {
				h.addTree(p);
				p = p.getNext();
			}
		}

		// remove min from trees list
		if (this.min.getPrev() != null) {
			this.min.getPrev().setNext(this.min.getNext());
		}
		if (this.min.getNext() != null) {
			this.min.getNext().setPrev(this.min.getPrev());
		}
		if (this.min == this.first) {
			this.first = this.min.getNext();
		}
		if (this.min == this.last) {
			this.last = this.min.getPrev();
		}

		this.meld(h);
	}

	public int findMin() {
		if (empty()) {
			return EMPTY_MIN_VALUE; // nothing to find
		} else {
			return this.min.getValue();
		}
	}

	public void meld(BinomialHeap heap2) {
		if (heap2 == null) {
			return; // nothing to meld
		}

		// add heap2's trees to the trees list
		BinomialHeap h = BinomialHeap.merge(this, heap2);
		if (h.empty()) {
			// this.empty(), so do nothing

		} else {
			Node prevX = null;
			Node x = h.getFirst();
			Node nextX = x.getNext();

			// successively link nodes of same ranks
			while (nextX != null) {
				// different ranks, no need to link
				if (x.getRank() != nextX.getRank()
						|| (nextX.getNext() != null && nextX.getNext().getRank() == x.getRank())) {
					prevX = x;
					x = nextX;
				} else {
					// find the larger node to be linked under the other node
					if (x.getValue() <= nextX.getValue()) {
						x.setNext(nextX.getNext());
						if (nextX.getNext() != null) {
							nextX.getNext().setPrev(x);
						}
						x.linkWith(nextX);
					} else {
						if (prevX == null) {
							h.setFirst(nextX);
						} else {
							prevX.setNext(nextX);
							if (nextX != null) {
								nextX.setPrev(prevX);
							}
						}
						nextX.linkWith(x);
						x = nextX;
					}
				}

				nextX = x.getNext();
			}

			// copy the created heap trees to the trees list
			this.first = h.first;
		}

		// fix size, trees count, min node, last
		this.fixProperties();
	}

	private void fixProperties() {
		Node p = this.first;
		Node last = p;
		this.size = 0;
		this.treesNum = 0;
		this.min = null;

		while (p != null) {
			this.size += p.getSize();
			this.treesNum++;
			if (this.min == null) {
				this.min = p;
			} else if (p.getValue() < this.min.getValue()) {
				this.min = p;
			}

			last = p;
			p = p.getNext();
		}

		// fix last pointer
		this.setLast(last);

		// fix list ends pointers
		if (this.getLast() != null) {
			this.getLast().setNext(null);
		}
		if (this.getFirst() != null) {
			this.getFirst().setPrev(null);
		}
	}

	public static BinomialHeap merge(BinomialHeap heap1, BinomialHeap heap2) {
		BinomialHeap h = new BinomialHeap();

		Node p1 = heap1.getFirst();
		Node p2 = heap2.getFirst();

		// merge-sort two linked lists of binomial trees
		while (p1 != null && p2 != null) {
			if (p1.getRank() <= p2.getRank()) {
				h.addTree(p1);
				p1 = p1.getNext();
			} else {
				h.addTree(p2);
				p2 = p2.getNext();
			}
		}

		// concatenate the remainders
		while (p1 != null) {
			h.addTree(p1);
			p1 = p1.getNext();
		}
		while (p2 != null) {
			h.addTree(p2);
			p2 = p2.getNext();
		}

		return h;
	}

	private void addTree(Node tree) {
		if (empty()) {
			this.first = tree;
			this.last = tree;
		} else {
			this.last.setNext(tree);
			tree.setPrev(this.last);
			this.last = tree;
		}

		this.size += tree.getSize();
	}
	 /**
     * Lấy ra tên và deadline của task có deadline bé nhất, sau đó xóa task đấy ra khỏi danh sách
     */
	public String printOutPriorityTask() {
		Node node = this.min;
		deleteMin();
		String s = node.getName() + " <-- " + node.getDay();
		return s;
	}

	public Node getFirst() {
		return first;
	}

	public void setFirst(Node first) {
		this.first = first;
	}

	public Node getLast() {
		return last;
	}

	public void setLast(Node last) {
		this.last = last;
	}

	public int size() {
		return this.size;
	}

	public int[] treesSize() {
		// collect trees sizes
		int[] treesSize = new int[this.treesNum];
		Node current = first;
		int i = 0;
		while (current != null) {
			treesSize[i++] = current.getSize();
			current = current.getNext();
		}

		return treesSize;
	}

	public void arrayToHeap(int[] array) {
		this.reset();
		for (int i : array) {
			this.insert(i);
		}
	}

	public boolean isValid() {
		if (empty()) {
			return true; // an empty tree is a valid tree
		}

		// validate size
		int actualTreesCount = 0;
		Node p = this.first;
		while (p != null) {
			actualTreesCount++;
			p = p.getNext();
		}

		if (actualTreesCount != this.treesNum) {
			return false; // invalid trees count
		}

		// validate binomial trees
		int[] actualRanks = new int[actualTreesCount];
		int actualMin = EMPTY_MIN_VALUE;
		ValidatedInfo validInfo;
		int actualSize = 0;
		p = this.first;
		int i = 0;

		while (p != null) {
			validInfo = p.validate();
			if (validInfo == null) {
				return false; // invalid binomial minimal tree
			}

			// collect ranks list, total size
			actualRanks[i++] = validInfo.rank;
			actualSize += validInfo.size;

			// collect min value
			if (actualMin == EMPTY_MIN_VALUE || p.getValue() < actualMin) {
				actualMin = p.getValue();
			}
			p = p.getNext();
		}

		// validate heap size
		if (actualSize != this.size) {
			return false; // invalid 'size' value
		}

		// validate minimal value
		if (actualMin != this.min.getValue()) {
			return false; // invalid 'min' value
		}

		// validate uniqueness of ranks
		int j;
		for (i = 0; i < actualTreesCount; i++) {
			for (j = i + 1; j < actualTreesCount; j++) {
				if (actualRanks[i] == actualRanks[j]) {
					return false; // ranks not unique
				}
			}
		}

		// all's good
		return true;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();

		Node tree = this.getFirst();
		while (tree != null) {
			if (tree == this.min) {
				str.append('*'); // mark min tree
			}
			str.append(tree.toString());
			str.append(", ");
			tree = tree.getNext();
		}

		return String.format("{ %s }", str);
	}

	static class ValidatedInfo {
		public int size;
		public int rank;

		public ValidatedInfo(int size, int rank) {
			this.size = size;
			this.rank = rank;
		}
	}

	public String visualize() {
		if (empty()) {
			System.out.println("Empty tree");
		}

		StringBuilder visualization = new StringBuilder();

		Node pointer = this.first;
		while (pointer != null) {
			visualizeNode(pointer, 0, 10, visualization);
			visualization.append('\n');
			pointer = pointer.getNext();
		}
		return visualization.toString();
	}

	public void visualizeNode(Node node, int space, int count, StringBuilder visualization) {
		if (node == null) {
			return;
		}
		space += count;
		visualizeNode(node.getLeftmostChild(), space, count, visualization);
		visualization.append('\n');
		for (int i = count; i < space; i++)
			visualization.append(" ");
		visualization.append(node.getValue());
		Node x = node.getLeftmostChild();
		if (x != null) {
			for (int i = 1; i <= node.getRank() - 1; i++) {
				visualizeNode(x.getNext(), space, count, visualization);
				x = x.getNext();
			}
		}
	}
}