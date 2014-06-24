package algo;

import info.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

import org.apache.commons.math3.util.CombinatoricsUtils;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.ColumnComparatorDouble;
import util.MyBitSet;

public class HypergeometricCoefficient {

	// for general settings:
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot
			+ "HypergeometricCoefficient/";

	// for indivial settings:
	// public static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	// public static String outputPath = "Example/Output/"
	// + "HypergeometricCoefficient/";

	public static String HypergeometricCoefficient_GL_TXT = outputPath
			+ "HypergeometricCoefficient_GL.txt";
	public static String HypergeometricCoefficient_LL_TXT = outputPath
			+ "HypergeometricCoefficient_LL.txt";

	public static ArrayList<double[]> calculate() {
		ArrayList<double[]> globalList = new ArrayList<double[]>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		int length = bG.numberOfPrimaryIds;

		int n_R = bG.numberOfSamples;

		int[][] coocc = new int[length][length];

		MyBitSet[] adjMPrimary = bG.toPrimBS();

		CooccFkt.readCooccPrimaryAddTopRight(adjMPrimary, coocc);

		double hyp;

		int[] degree = new int[length];

		for (int i = 0; i < length; i++) {
			degree[i] = adjMPrimary[i].cardinality();
		}

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {

				if (coocc[i][j] == 0) {
					continue;
				}

				int minDegree = Math.min(degree[i], degree[j]);

				double sum = 0;

				for (int c = coocc[i][j]; c <= minDegree; c++) {

					double t1 = CombinatoricsUtils.binomialCoefficient(
							degree[i], c);
					double t2 = CombinatoricsUtils.binomialCoefficient(n_R
							- degree[i], degree[j] - c);
					double t3 = CombinatoricsUtils.binomialCoefficient(n_R,
							degree[j]);

					double term = t1 * t2 / t3;

					sum += term;

				}

				hyp = -Math.log(sum);

				globalList.add(new double[] { i, j, hyp });

			}

		}

		return globalList;

	}

	public static void run() {

		File file = new File(outputPath);

		if (!file.exists()) {

			file.mkdirs();

		}

		ArrayList<double[]> measures = calculate();

		Collections.sort(measures, new ColumnComparatorDouble(-3, 1, 3));

		util.Text.writeListDouble(measures, HypergeometricCoefficient_GL_TXT,
				"Hypergeometric Coefficient", "global list", "", true);

		util.Text.writeLocalListDouble(measures,
				HypergeometricCoefficient_LL_TXT, "Hypergeometric coefficient",
				"local list", "");

	}

	public static void main(String[] args) {

		run();

	}

}
