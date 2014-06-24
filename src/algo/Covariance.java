package algo;

import info.Setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.soap.Text;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.ColumnComparator;
import util.MyBitSet;

public class Covariance {

	// for general settings:
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot + "Covariance/";

	// for indivial settings:
	// public static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	//
	// public static String outputPath = "Example/Output/"
	// + "Covariance/";

	public static String Covariance_GL_TXT = outputPath + "Covariance_GL.txt";

	public static String Covariance_LL_TXT = outputPath + "Covariance_LL.txt";

	public static ArrayList<int[]> calculate() {

		ArrayList<int[]> globalList = new ArrayList<int[]>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		int length = bG.numberOfPrimaryIds;

		int[][] coocc = new int[length][length];

		MyBitSet[] adjMPrimary = bG.toPrimBS();

		CooccFkt.readCooccPrimaryAddTopRight(adjMPrimary, coocc);

		int covariance;

		int n_R = bG.numberOfSamples;

		// System.out.println("Anzahl der Users = "+n_R);

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (coocc[i][j] > 0) {
					int d_i = adjMPrimary[i].cardinality();
					int d_j = adjMPrimary[j].cardinality();

					// covariance = (int) Math
					// .rint(((double) coocc[i][j] * 1000 - (double) d_i
					// * (double) d_j * 1000 / (double) length)
					// / (double) length);

					covariance = (int) Math
							.rint((double) ((n_R * coocc[i][j] - d_i * d_j) * 1000)
									/ (double) (n_R * n_R));

					globalList.add(new int[] { i, j, covariance });

					// System.out.println("i = "+i+", j = "+j + ": d(i) = "+d_i
					// + ", d(j) = "+ d_j + ", coocc[i][j] = "+
					// coocc[i][j]+": covariance = "+covariance);

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

		util.Text.writeList(measures, Covariance_GL_TXT, "Covariance",
				"global list", "", true);

		util.Text.writeLocalList(measures, Covariance_LL_TXT, "Covariance",
				"local list", "");

	}

	public static void main(String[] args) {
		run();

	}

}
