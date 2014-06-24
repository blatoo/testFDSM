package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sort arrays with comparable type reversely according the order of given
 * columns, the number of columns is not constained.
 * 
 * @author Ying
 * 
 */
public class ColumnReverseComparator<T extends Number & Comparable<T>>
		implements Comparator<T[]> {

	int[] rang;
	int length;

	/**
	 * 
	 * @param cols
	 *            the Reverse Sort is after the column order, the numbers are
	 *            native number.
	 */
	public ColumnReverseComparator(int... cols) {
		this.rang = cols;
		this.length = cols.length;
	}

	@Override
	public int compare(T[] a, T[] b) {

		for (int i = 0; i < length; i++) {

			int value = -a[rang[i] - 1].compareTo(b[rang[i] - 1]);

			if (value != 0) {
				return value;
			}

		}

		return 0;

	}
	
	public static void main(String[] args) {

		Integer[][] arr = new Integer[][] { { 1, 2, 3 }, { 10, 12, 12 },
				{ 7, 8, 9 }, { 10, 11, 12 } };

		Arrays.sort(arr, new ColumnReverseComparator<Integer>(1, 2));
		

	}

}
