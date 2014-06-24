package algo;

import info.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.ColumnComparatorShort;
import util.MyBitSet;
import util.Text;

public class PValue {

	// for general settings:
	public static int numberOfSampleGraphs = Setting.numberOfSampleGraphs;
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot + "PValue/"
			+ numberOfSampleGraphs + "/";
	public static int seed = Setting.seed;

	// for individual settings:
	// public static int numberOfSampleGraphs = 5000;
	// public static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	// public static String outputPath = "Example/Output/" +
	// "PValue/"+numberOfSampleGraphs+"/";
	// public static int seed = 3306;

	public static String pValue_GL_TXT = outputPath + "PValue_GL.txt";
	public static String pValue_LL_TXT = outputPath + "PValue_LL.txt";

	/**
	 * Read the pValues from coocc Matrix into a pValue Matrix and clear the
	 * lowerleft of the coocc at the same time.
	 * 
	 * @param coocc
	 * @param pValues
	 * @param adjM
	 */
	public static void pSecAddTopRightClear(int[][] coocc, short[][] pValue) {
		int length = coocc.length;
		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {

				if (coocc[i][j] <= coocc[j][i]) {
					pValue[i][j]++;
				}
				coocc[j][i] = 0;
			}

		}

	}

	public static ArrayList<short[]> calculate() {

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		MyBitSet[] adjMSec = bG.toSecBS();

		int[][] edges = bG.generateEdges();

		ArrayList<short[]> measure = new ArrayList<short[]>();

		int length = bG.numberOfPrimaryIds;

		int[][] coocc = new int[length][length];

		short[][] pValue = new short[length][length];

		// read the original Co-occurrence

		CooccFkt.readCooccSecAddTopRight(adjMSec, coocc);

		// the first long swap walks

		Random generator_edge = new Random(seed);

//		System.out.println("The first long swap...");

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjMSec, generator_edge);

		// set the normal length of swap walks

		int lengthOfWalks = (int) (bG.numberOfSamples * Math
				.log(bG.numberOfSamples));

		for (int i = 0; i < numberOfSampleGraphs; i++) {

			long t1 = System.currentTimeMillis();

			CooccFkt.swap(lengthOfWalks, edges, adjMSec, generator_edge);

			CooccFkt.readCooccSecAddLowerLeft(adjMSec, coocc);

			pSecAddTopRightClear(coocc, pValue);

			long t2 = System.currentTimeMillis();

			long t3 = t2 - t1;

//			System.out.println("Round " + i + " takes " + (t3 / 1000)
//					+ " seconds...");

		}

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {

				measure.add(new short[] { (short) i, (short) j, pValue[i][j] });

			}

		}

		return measure;

	}

	/**
	 * You can set the number of the random graphs and set the criterion for
	 * p-value to improve the calculate speed.
	 * 
	 * @param numberOfSamples
	 * @param pValueCriterion
	 *            reserve the p-value which smaller than the Criterion. the
	 *            value is between (0,1]. notice: <0 means all value will be
	 *            considered.
	 * @return
	 */
	public static ArrayList<short[]> calculate(int numberOfSamples,
			double pValueCriterion) {

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		MyBitSet[] adjMSec = bG.toSecBS();

		int[][] edges = bG.generateEdges();

		ArrayList<short[]> measure = new ArrayList<short[]>();

		int length = bG.numberOfPrimaryIds;

		int[][] coocc = new int[length][length];

		short[][] pValue = new short[length][length];

		// read the original Co-occurrence

		CooccFkt.readCooccSecAddTopRight(adjMSec, coocc);

		// the first long swap walks

		Random generator_edge = new Random(seed);

//		System.out.println("The first long swap...");

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjMSec, generator_edge);

		// set the normal length of swap walks

		int lengthOfWalks = (int) (bG.numberOfSamples * Math
				.log(bG.numberOfSamples));

		for (int i = 0; i < numberOfSamples; i++) {

			long t1 = System.currentTimeMillis();

			CooccFkt.swap(lengthOfWalks, edges, adjMSec, generator_edge);

			CooccFkt.readCooccSecAddLowerLeft(adjMSec, coocc);

			pSecAddTopRightClear(coocc, pValue);

			long t2 = System.currentTimeMillis();

			long t3 = t2 - t1;

			System.out.println("Round " + i + " takes " + (t3 / 1000)
					+ " seconds...");

		}

		double pValueCriterionScaled = pValueCriterion * numberOfSamples;

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {

				if (pValue[i][j] < pValueCriterionScaled) {

					measure.add(new short[] { (short) i, (short) j,
							pValue[i][j] });

				}

			}

		}

		return measure;

	}

	public static void run() {

		File file = new File(outputPath);

		if (!file.exists()) {

			file.mkdirs();

		}

		ArrayList<short[]> measures = calculate();

		Text.writeListShortForPValue(measures, numberOfSampleGraphs,
				pValue_GL_TXT, "PValue", "global list", "", true);

		Text.writeLocalListShortForPValue(measures, numberOfSampleGraphs,
				pValue_LL_TXT, "PValue", "loal list", "");

	}

	public static void run(int numberOfSampleGraphs, double pValueCriterion) {

		String outputPathLocal = outputPath.substring(0,
				outputPath.lastIndexOf("P"))
				+ "/PValue/" + numberOfSampleGraphs + "/";
		File file = new File(outputPathLocal);

		if (!file.exists()) {

			file.mkdirs();

		}

		String pValue_GL_TXT = outputPathLocal + "PValue_GL.txt";

		String pValue_LL_TXT = outputPathLocal + "PValue_LL.txt";

		ArrayList<short[]> measures = calculate(numberOfSampleGraphs,
				pValueCriterion);

		Text.writeListShortForPValue(measures, numberOfSampleGraphs,
				pValue_GL_TXT, "PValue", "global list", "numberOfSamples = "
						+ numberOfSampleGraphs, true);

		Text.writeLocalListShortForPValue(measures, numberOfSampleGraphs,
				pValue_LL_TXT, "PValue", "loal list", "");

	}

	public static void main(String[] args) {

		// run();
		run(numberOfSampleGraphs, 1.0);
	}

}
