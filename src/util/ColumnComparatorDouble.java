package util;

import gnu.trove.set.hash.TIntHashSet;

import java.util.Arrays;
import java.util.Comparator;

/**
 * sort after the column order, the numbers are native number.
 * 
 * @author Ying
 * 
 */
public class ColumnComparatorDouble implements Comparator<double[]> {

	int[] rang;

	int length;

	/**
	 * @param cols
	 *            postive cols means ascending, negative cols means descending,
	 *            0 is not allowed.
	 */
	public ColumnComparatorDouble(int... cols) {

		this.rang = cols;

		this.length = cols.length;
		TIntHashSet ts = new TIntHashSet();

		for (int i : cols) {

			if (i == 0) {
				System.err
						.println("the column number should be nature number, cannot equal 0!");
				System.exit(-1);
			} else {

				// boolean repeat = ts.add(i);

				if (ts.add(i) == false) {
					System.err.println("the columns can't be repeated.");

					System.exit(-1);
				}
				;
			}

		}

	}

	@Override
	public int compare(double[] a, double[] b) {

		for (int i = 0; i < length; i++) {

			if (rang[i] > 0) {

				if (a[rang[i] - 1] > b[rang[i] - 1]) {
					return 1;
				} else if (a[rang[i] - 1] < b[rang[i] - 1]) {
					return -1;
				}

				continue;

			} else if (rang[i] < 0) {


				if (a[-rang[i] - 1] > b[-rang[i] - 1]) {
					return -1;
				} else if (a[-rang[i] - 1] < b[-rang[i] - 1]) {
					return 1;
				}

				continue;
			}

		}

		return 0;
	}

	public static void main(String[] args) {
		double[][] arr = new double[][] { { 1, 2, 12 }, { 10, 12, 12 }, { 7, 8, 12 },
				{ 9, 13, 12 } };

		Arrays.sort(arr, new ColumnComparatorDouble(-3, 1 , 2));

		util.Functions.arrayPrintTwoDimensions(arr);

	}

}
