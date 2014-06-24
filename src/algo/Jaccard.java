package algo;

import info.Setting;

import java.io.File;
import java.util.ArrayList;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.MyBitSet;
import util.Text;

public class Jaccard {

	// for general settings:
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot + "Jaccard/";

	// for indivial settings:
	// public static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	// public static String outputPath = "Example/Output/" + "Jaccard/";

	public static String Jaccard_GL_TXT = outputPath + "Jaccard_GL.txt";
	public static String Jaccard_LL_TXT = outputPath + "Jaccard_LL.txt";

	public static ArrayList<double[]> calculate() {

		ArrayList<double[]> measure = new ArrayList<double[]>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		MyBitSet[] adjMPrimary = bG.toPrimBS();

		int length = bG.numberOfPrimaryIds;

		int[][] coocc = new int[length][length];

		CooccFkt.readCooccPrimaryAddTopRight(adjMPrimary, coocc);

		int[] degrees = bG.readPrimaryDegree();

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {

				if (coocc[i][j] > 0) {
					double denominator = degrees[i] + degrees[j] - coocc[i][j];

					double jaccard = (double) coocc[i][j] / denominator;

					measure.add(new double[] { i, j, jaccard });

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

		ArrayList<double[]> measures = calculate();

		Text.writeListDouble(measures, Jaccard_GL_TXT, "Jaccard",
				"global list", "", true);

		Text.writeLocalListDouble(measures, Jaccard_LL_TXT, "Jaccard",
				"global list", "");
	}

	public static void main(String[] args) {

		run();

	}
}
