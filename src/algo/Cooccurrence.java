package algo;

import info.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.ColumnComparator;
import util.MyBitSet;
import util.Text;

public class Cooccurrence {

	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot + "Cooccurence/";

	// for indivial settings
	// public static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	// public static String outputPath = "Example/Output/" + "Cooccurence/";

	public static String Coocc_GL_TXT = outputPath + "Coocc_GL.txt";
	public static String Coocc_LL_TXT = outputPath + "Coocc_LL.txt";

	public static ArrayList<int[]> calculate() {
		ArrayList<int[]> globalList = new ArrayList<>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		int length = bG.numberOfPrimaryIds;

		int[][] coocc = new int[length][length];

		MyBitSet[] adjMPrimary = bG.toPrimBS();

		CooccFkt.readCooccPrimaryAddTopRight(adjMPrimary, coocc);

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (coocc[i][j] > 0) {

					globalList.add(new int[] { i, j, coocc[i][j] });

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

		Collections.sort(measures, new ColumnComparator(-3, 1, 2));

		Text.writeList(measures, Coocc_GL_TXT, "Consine", "global list", "",
				true);

		Text.writeLocalList(measures, Coocc_LL_TXT, "Cooccurrence",
				"local list", "");

	}

	public static void main(String[] args) {

		run();

	}

}
