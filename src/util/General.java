package util;

import java.io.File;
import java.math.BigDecimal;

import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TIntShortIterator;
import gnu.trove.set.hash.TIntHashSet;

public class General {
	
	public static void creatPath(String outputPath) {
		File file = new File(outputPath);
		boolean createPath = file.mkdirs();

//		System.out.println(createPath);

	}

	public static int THSIntersectSize(TIntHashSet ths1, TIntHashSet ths2) {
		int counter = 0;

		if (ths1.size() <= ths2.size()) {
			TIntIterator it = ths1.iterator();
			for (int i = ths1.size(); i-- > 0;) {
				if (ths2.contains(it.next())) {
					counter++;
				}

			}

		} else {
			TIntIterator it = ths2.iterator();
			for (int i = ths2.size(); i-- > 0;) {
				if (ths1.contains(it.next())) {
					counter++;
				}
			}

		}

		return counter;
	}

	public static double doubleRound(double number, int places) {
		BigDecimal bd = new BigDecimal(number);
		return bd.setScale(places, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	public static void main(String[] args) {

	}

}
