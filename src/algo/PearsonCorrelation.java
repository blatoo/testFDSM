package algo;

import info.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.ColumnComparator;
import util.MyBitSet;
import util.Text;

public class PearsonCorrelation {

	// for general settings:
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot
			+ "PearsonCorrelation/";

	// for indivial settings:

	// public static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	//
	// public static String outputPath = "Example/Output/" +
	// "PearsonCorrelation/";

	public static String PearsonCorrelation_GL_TXT = outputPath
			+ "PearsonCorrelation_GL.txt";

	public static String PearsonCorrelation_LL_TXT = outputPath
			+ "PearsonCorrelation_LL.txt";

	public static ArrayList<int[]> calculate() {

		ArrayList<int[]> globalList = new ArrayList<int[]>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		int length = bG.numberOfPrimaryIds;

		int n_R = bG.numberOfSamples;

		int[][] coocc = new int[length][length];

		MyBitSet[] adjMPrimary = bG.toPrimBS();

		CooccFkt.readCooccPrimaryAddTopRight(adjMPrimary, coocc);

		double pearsonCorrelation;

		// calculate the Standard Deviation and Average of the nodes

		/**
		 * sd(i) = sqrt([(n_R - d(i))*[(0 - d(i)/n)^2] + d(i)[(1 -
		 * d(i)/n)^2]]/n_R) = sqrt(d(i)/n_R - d(i)/n_R^2)
		 **/

		double[] avg = new double[length]; // average degree of the node
		double[] sd = new double[length]; // standard deviation of the node

		for (int i = 0; i < length; i++) {

			if (adjMPrimary[i].cardinality() == 0) {
				avg[i] = 0;
				sd[i] = 0;
				continue;
			}
			avg[i] = (double) adjMPrimary[i].cardinality() / (double) n_R;
			sd[i] = Math.sqrt(avg[i] - avg[i] * avg[i]);

		}

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (coocc[i][j] > 0) {

					double covariance = (double) coocc[i][j] / (double) n_R
							- avg[i] * avg[j];

					pearsonCorrelation = covariance / (sd[i] * sd[j]);

					globalList.add(new int[] { i, j,
							(int) Math.rint(pearsonCorrelation * 1000) });

				}

			}

		}

		return globalList;

	}

	public static void run() {

		File file = new File(outputPath);

		if (!file.exists()) {
			file.mkdirs();
		}

		ArrayList<int[]> measures = calculate();

		Text.writeList(measures, PearsonCorrelation_GL_TXT,
				"Pearson Correlation", "global list", "", true);

		Text.writeLocalList(measures, PearsonCorrelation_LL_TXT,
				"Pearson Correlation", "local list", "");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		run();

	}

}
