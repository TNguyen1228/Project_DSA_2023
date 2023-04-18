
public class BinomialTree {
	int degree;
	private double value;
	private BinomialTree parent;
	private BinomialTree sibling;
	private BinomialTree child;

	public BinomialTree(double value) {
		this.degree = (0);
		this.value = value;
		this.setParent(null);
		this.setSibling(null);
		this.child = null;
	}

	// Method 4
	// To get the size
	public int getSize() {
		return (1 + ((child == null) ? 0 : child.getSize()) + ((sibling == null) ? 0 : sibling.getSize()));
	}

	public double getValue() {
		return value;
	}

	public BinomialTree getParent() {
		return parent;
	}

	public BinomialTree getSibling() {
		return sibling;
	}

	public BinomialTree getChild() {
		return child;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setChild(BinomialTree child) {
		this.child = child;
	}

	public void setSibling(BinomialTree sibling) {
		this.sibling = sibling;
	}

	public void setParent(BinomialTree parent) {
		this.parent = parent;
	}

	public void addChild(BinomialTree child) {
		child.setParent(this);
		child.setSibling(this.child);
		this.child = child;
		this.degree = (this.getDegree() + 1);
	}

	public BinomialTree merge(BinomialTree tree) {
		if (this.value > tree.value) {
			tree.addChild(this);
			return tree;
		} else {
			this.addChild(tree);
			return this;
		}
	}

	public BinomialTree reverse(BinomialTree sibl) {
		BinomialTree ret;
		if (sibling != null)
			ret = sibling.reverse(this);
		else
			ret = this;
		sibling = sibl;
		return ret;
	}

	// Method 2
	// To find minimum node
	public BinomialTree findMinNode() {

		// this keyword refers to current instance itself
		BinomialTree x = this, y = this;
		double min = x.value;

		while (x != null) {
			if (x.value < min) {
				y = x;
				min = x.value;
			}

			x = x.sibling;
		}

		return y;
	}

	// Method 3
	// To find node with key value
	public BinomialTree findANodeWithKey(double old_value) {

		BinomialTree temp = this, node = null;

		while (temp != null) {
			if (temp.value == old_value) {
				node = temp;
				break;
			}

			if (temp.child == null)
				temp = temp.sibling;

			else {
				node = temp.child.findANodeWithKey(old_value);
				if (node == null)
					temp = temp.sibling;
				else
					break;
			}
		}

		return node;
	}
}
