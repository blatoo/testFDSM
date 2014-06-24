package util;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Sort arrays reverse according the given column order, the number of columns is not
 * constained.
 * 
 * @author Ying
 * 
 */
public class ComparatorArrayReverseNCol implements Comparator<int[]> {

	int[] rang;
	int length;

	/**
	 * 
	 * @param cols
	 *            the Reverse Sort is after the column order, the numbers are native number.
	 */
	public ComparatorArrayReverseNCol(int... cols) {
		this.rang = cols;
		this.length = cols.length;
	}

	@Override
	public int compare(int[] a, int[] b) {

		for (int i = 0; i < length; i++) {

			if (a[rang[i] - 1] > b[rang[i] - 1]) {
				return -1;
			} else if (a[rang[i] - 1] < b[rang[i] - 1]) {
				return 1;
			} else if (a[rang[i] - 1] == b[rang[i] - 1]) {
				continue;
			}

		}

		return 0;

	}

	public static void main(String[] args) {

		int[][] arr = new int[][] { { 1, 2, 3 }, { 10, 12, 12 }, { 7, 8, 9 },
				{ 10, 11, 12 } };

		Arrays.sort(arr, new ComparatorArrayReverseNCol(1, 2));

		util.Functions.arrayPrintTwoDimensions(arr);
	}

}
