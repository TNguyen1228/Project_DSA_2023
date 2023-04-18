

public class BinomialHeap {
	// Each BinomialHeap object has a head, which is the root of the binomial tree
	// with the minimum value in the heap.
	private BinomialTree head;
	private int size;

	// The constructor initializes the head to null.
	public BinomialHeap() {
		head = null;
		size = 0;
	}
	

	public boolean isEmpty() {
		return head == null;
	}

	public int getSize() {
		return size;
	}

	public void makeEmpty() {
		head = null;
		size = 0;
	}

	// The insert() method inserts a new value into the heap by creating a new
	// binomial tree and merging it with the existing trees in the heap.
	public void insert(double value) {

		if (value > 0) {
			BinomialTree temp = new BinomialTree(value);
			if (head == null) {
				head = temp;
				size = 1;
			} else {
				union(temp);
				size++;
			}
		}
	}

	// The union() method merges two binomial heaps by merging their root binomial
	// trees into a single heap and setting the other heap's head to null.
	public void union(BinomialTree other) {
		merge(other);

		BinomialTree prevTemp = null, temp = head, nextTemp = head.getSibling();

		while (nextTemp != null) {

			if ((temp.getDegree() != nextTemp.getDegree())
					|| ((nextTemp.getSibling() != null) 
							&& (nextTemp.getSibling().getDegree() == temp.getDegree()))) {
				prevTemp = temp;
				temp = nextTemp;
			}

			else {

				if (temp.getValue() <= nextTemp.getValue()) {
					temp.setSibling(nextTemp.getSibling());
					nextTemp.setParent(temp);
					nextTemp.setSibling(temp.getChild());
					temp.addChild(nextTemp);
					temp.degree++;
				}

				else {

					if (prevTemp == null) {
						head = nextTemp;
					}

					else {
						prevTemp.setSibling(nextTemp);
					}

					temp.setParent(nextTemp);
					temp.setSibling(nextTemp.getChild());
					nextTemp.addChild(temp);
					nextTemp.degree++;
					temp = nextTemp;
				}
			}
			nextTemp = temp.getSibling();
		}
	}

	// The extractMinimum() method removes the minimum value from the heap by first
	// finding the binomial tree that contains the minimum value, then removing that
	// tree from the heap and merging its child trees with the remaining trees in
	// the heap.
	public double extractMinimum() {
		if (head == null) {
			throw new IllegalStateException("Heap is empty");
		}

		BinomialTree temp = head, prevTemp = null;
		BinomialTree minNode = head.findMinNode();

		while (temp.getValue() != minNode.getValue()) {
			prevTemp = temp;
			temp = temp.getSibling();
		}

		if (prevTemp == null) {
			head = temp.getSibling();
		} else {
			prevTemp.setSibling(temp.getSibling());
		}

		temp = temp.getChild();
		BinomialTree fakeNode = temp;

		while (temp != null) {
			temp.setParent(null);
			temp = temp.getSibling();
		}

		if ((head == null) && (fakeNode == null)) {
			size = 0;
		} else {
			if ((head == null) && (fakeNode != null)) {
				head = fakeNode.reverse(null);
				size = head.getSize();
			} else {
				if ((head != null) && (fakeNode == null)) {
					size = head.getSize();
				} else {
					union(fakeNode.reverse(null));
					size = head.getSize();
				}
			}
		}

		return minNode.getValue();
	}

	public double findMinimum()
	{
	    return head.findMinNode().getValue();
	}

	public void displayHeap() {
		System.out.print("\nHeap : ");
		displayHeap(head, 10);
		
	}

	private void displayHeap(BinomialTree r, int space) {
			if (r==null)
				return;
			space+=5;
			displayHeap(r.getSibling(), space);
			System.out.println("\n");
			for (int i = 5; i < space; i++)
				System.out.print(" ");
			System.out.print(r.getValue() + "\n");
			displayHeap(r.getChild(), space);
		
	}
	
	// The merge() method merges two binomial trees of the same degree by adding the
	// smaller one as a child of the larger one.
	private void merge(BinomialTree binHeap) {
		BinomialTree temp1 = head, temp2 = binHeap;

		while ((temp1 != null) && (temp2 != null)) {

			if (temp1.getDegree() == temp2.getDegree()) {

				BinomialTree tmp = temp2;
				temp2 = temp2.getSibling();
				tmp.setSibling(temp1.getSibling());
				temp1.setSibling(tmp);
				temp1 = tmp.getSibling();
			}

			else {

				if (temp1.getDegree() < temp2.getDegree()) {

					if ((temp1.getSibling() == null) || (temp1.getSibling().getDegree() > temp2.getDegree())) {
						BinomialTree tmp = temp2;
						temp2 = temp2.getSibling();
						tmp.setSibling(temp1.getSibling());
						;
						temp1.setSibling(tmp);
						temp1 = tmp.getSibling();
					}

					else {
						temp1 = temp1.getSibling();
					}
				}

				else {
					BinomialTree tmp = temp1;
					temp1 = temp2;
					temp2 = temp2.getSibling();
					temp1.setSibling(tmp);

					if (tmp == head) {
						head = temp1;
					}

					else {
					}
				}
			}
		}

		if (temp1 == null) {
			temp1 = head;

			while (temp1.getSibling() != null) {
				temp1 = temp1.getSibling();
			}
			temp1.setSibling(temp2);
		}
	}
	public void delete(int value)
    {
  
        if ((head != null)
            && (head.findANodeWithKey(value) != null)) {
            decreaseKeyValue(value, findMinimum() - 1);
            extractMinimum();
        }
    }
  
    // Method 8
    // To decrease key with a given value */
    public void decreaseKeyValue(double old_value,
                                 double d)
    {
        BinomialTree temp
            = head.findANodeWithKey(old_value);
        if (temp == null)
            return;
        temp.setValue(d);
        BinomialTree tempParent = temp.getParent();
  
        while ((tempParent != null)
               && (temp.getValue() < tempParent.getValue())) {
            double z = temp.getValue();
            temp.setValue(tempParent.getValue()); 
            tempParent.setValue(z);
  
            temp = tempParent;
            tempParent = tempParent.getParent();
        }
    }
    

	public static void main(String[] args) {
		// Make object of BinomialHeap
		BinomialHeap binHeap = new BinomialHeap();

		// Inserting in the binomial heap
		// Custom input integer values
		binHeap.insert(12);
		binHeap.insert(8);
		binHeap.insert(5);
		binHeap.insert(15);
		binHeap.insert(7);
		binHeap.insert(2);
		binHeap.insert(9);
		// Size of binomial heap
		System.out.println("Size of the binomial heap is " + binHeap.getSize());

		binHeap.displayHeap();
//		binHeap.decreaseKeyValue(12, 4);
//		binHeap.displayHeap();
//		binHeap.delete(9);
//		binHeap.insert(1);
//		binHeap.displayHeap();

//		System.out.println(binHeap.extractMinimum());


	}
}
