package version2;

import java.util.Arrays;
import java.util.Random;

import version2.BNH.HeapNode;

public class test {

	private static boolean contains(int[] ar, int x) {
		for (int y : ar) {
			if (y == x) {
				return true;
			}
		}
		return false;
	}

	private static int[] generateRandomSet(int count) {
		int[] ar = new int[count];
		int x;
		Random rnd = new Random();

		for (int i = 0; i < count; i++) {
			x = rnd.nextInt(count * 23) + 1;

			while (contains(ar, x)) {
				x = rnd.nextInt(count * 23) + 1;
			}

			ar[i] = x;
			;
		}

		return ar;
	}

	public static void main(String[] args) {
		int[] sizes = { 3,4,6 };
		int[] array;
		BNH h = new BNH();
//		h.insert(1);
//		h.insert(2);
//		h.insert(3);
//		h.insert(4);
//		h.insert(5);
//		System.out.println(h.size());
//		h.visualize();
		for (int size : sizes) {
			array = generateRandomSet(size);
			h.arrayToHeap(array);

			System.out.format("(i) After %d insert()s:\n", size);
			System.out.format("%d trees: %s\n", h.treesSize().length, Arrays.toString(h.treesSize()));
			System.out.println(h.toString());
			h.visualize();
			h.insert(10);
			System.out.println(h.toString());
			h.visualize();
			// Decrease the value of a node in the binomial heap
	        HeapNode nodeToDecrease = h.findNodeWithValue(10); // Find the node with value 10
	        h.decreaseValue(nodeToDecrease, 5); // Decrease the value to 5
			h.visualize();
			
			h.deleteMin();
			System.out.println("(ii) After 1 deleteMin():");
			System.out.format("%d trees: %s\n", h.treesSize().length, Arrays.toString(h.treesSize()));
			System.out.println(h.toString());
			h.visualize();
			System.out.println();
			
		}
		
	}
}