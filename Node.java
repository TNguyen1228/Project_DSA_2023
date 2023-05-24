package InternalStructure;

public class Node {
	/**
	 * Tên của task
	 */
	private String name;

	/**
	 * Deadline của task
	 */
	private String day;

	public String getName() {
		return name;
	}

	public String getDay() {
		return day;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDay(String day) {
		this.day = day;
	}

	private int value;
	private int rank;
	private int size;
	private Node next;
	private Node prev;
	private Node leftmostChild;
	private Node rightmostChild;

	public Node(int value) {
		this.value = value;
		this.size = 1;
		this.rank = 0;
	}

	/**
	 * Nếu có đủ thông tin của tên và deadline của task thì sẽ đặt value bằng ngày
	 * Julian tương ứng của deadline
	 */

	public Node(String name, String day) {
		this.name = name;
		this.day = day;
		if (this.day != null) {
			this.value = calculateJulianDay(day);
		}
		this.size = 1;
		this.rank = 0;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	public Node getLeftmostChild() {
		return leftmostChild;
	}

	public void setLeftmostChild(Node leftmostChild) {
		this.leftmostChild = leftmostChild;
	}

	public Node getRightmostChild() {
		return rightmostChild;
	}

	public void setRightmostChild(Node rightmostChild) {
		this.rightmostChild = rightmostChild;
	}

	public int getRank() {
		return this.rank;
	}

	public int getSize() {
		return this.size;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		if (value >= 0) {
			this.value = value;
		}
	}

	/**
	 * Hàm dùng để tính ra ngày Julian của deadline tương ứng
	 */
	public int calculateJulianDay(String dateString) {
		String[] dateParts = dateString.split("/");
		int day = Integer.parseInt(dateParts[0]);
		int month = Integer.parseInt(dateParts[1]);
		int year = Integer.parseInt(dateParts[2]);

		// Adjust the month and year for the Julian day calculation
		if (month <= 2) {
			month += 12;
			year -= 1;
		}

		// Perform the Julian day calculation
		int a = year / 100;
		int b = a / 4;
		int c = 2 - a + b;
		int e = (int) (365.25 * (year + 4716));
		int f = (int) (30.6001 * (month + 1));

		int julianDay = c + day + e + f - 1524;

		return julianDay;
	}

	public BinomialHeap.ValidatedInfo validate() {
		Node child = this.getLeftmostChild();
		BinomialHeap.ValidatedInfo childResult;
		int size = 1;
		int rank = 0;

		// traversing child nodes, validating them
		while (child != null) {
			if (this.getValue() > child.getValue()) {
				return null; // root's value isn't the minimal value
				// (minimal rule)
			}

			childResult = child.validate();
			if (childResult == null) {
				return null; // child node is invalid
			} else {
				// collect size and rank
				size += childResult.size;
				rank++;
			}

			child = child.getNext();
		}

		// verify size and rank relation (size == 2^rank)
		if (size != Math.pow(2, rank)) {
			return null; // invalid size / rank
		}

		return new BinomialHeap.ValidatedInfo(size, rank);
	}

	public void linkWith(Node other) {
		other.setNext(null);
		other.setPrev(this.rightmostChild);
		if (this.rightmostChild != null) {
			this.rightmostChild.setNext(other);
		} else {
			this.leftmostChild = other;
		}
		this.rightmostChild = other;

		this.rank++;
		this.size += other.size;
	}

	@Override
	public String toString() {
		StringBuffer children = new StringBuffer();

		Node child = this.getLeftmostChild();
		while (child != null) {
			children.append(child.toString());
			child = child.getNext();
		}

		return String.format("[ %d %s ]", this.getValue(), children);
	}

}
