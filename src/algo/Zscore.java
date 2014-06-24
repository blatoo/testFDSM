package algo;

import info.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.ColumnComparator;
import util.MyBitSet;
import util.Text;

public class Zscore {

	// for general settings:
	public static int numberOfSampleGraphs = Setting.numberOfSampleGraphs;
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot + "ZScore/"
			+ numberOfSampleGraphs + "/";
	public static int seed = Setting.seed;

	// for individual settings:
	// static int numberOfSampleGraphs = 5000;
	// static String inputFile =
	// "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
	// static String outputPath = "Example/Output/" +
	// "ZScore/"+numberOfSampleGraphs+"/";
	// public static int seed = 3306;

	
	
	// output file name for the levFDSM result:global list
	public static String zScore_GL_TXT = outputPath + "ZScore_GL.txt";
	public static String zScore_LL_TXT = outputPath + "ZScore_LL.txt";

	public static ArrayList<double[]> calculate() {

		// measaures has 3 fields, [(double)i][(double)j][(double)zscore]
		ArrayList<double[]> measures = new ArrayList<double[]>();
		ArrayList<double[]> levFDSM_Means = new ArrayList<double[]>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		int length = bG.numberOfPrimaryIds;

		MyBitSet adjM[] = bG.toSecBS();
		int[][] coocc = new int[bG.numberOfPrimaryIds][bG.numberOfPrimaryIds];
		// read original co-occurrence
		CooccFkt.readCooccSecAddTopRight(adjM, coocc);

		// The first long swap walks
		int[][] edges = bG.generateEdges();
		Random generator_edge = new Random(seed);

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjM, generator_edge);

		// set the normal length of swap walks
		int lengthOfWalks = (int) (bG.numberOfSamples * Math
				.log(bG.numberOfSamples));

		for (int i = 0; i < numberOfSampleGraphs; i++) {
			CooccFkt.swap(lengthOfWalks, edges, adjM, generator_edge);

			CooccFkt.readCooccSecAddLowerLeft(adjM, coocc);

		}

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (coocc[i][j] > 0) {

					double mean = (double) coocc[j][i]
							/ (double) numberOfSampleGraphs;
					double levFDSM = (double) coocc[i][j] - mean;
					if (levFDSM > 0) {

						measures.add(new double[] { i, j, 0 });

						levFDSM_Means.add(new double[] { levFDSM, mean });

					}

				}

			}

		}

		double[] standarddiviation = new double[measures.size()];

		int toCalLength = standarddiviation.length;

		CooccFkt.matrixClearLeftDown(coocc);

		bG = new BipartiteGraph(inputFile);

		adjM = null;

		generator_edge = new Random(seed);

		adjM = bG.toSecBS();

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjM, generator_edge);

		for (int i = 0; i < numberOfSampleGraphs; i++) {

			CooccFkt.swap(lengthOfWalks, edges, adjM, generator_edge);
			CooccFkt.readCooccSecAddLowerLeft(adjM, coocc);

			for (int j = 0; j < toCalLength; j++) {

				double[] pos = measures.get(j);
				double mean = levFDSM_Means.get(j)[1];

				standarddiviation[j] += Math.pow(
						(coocc[(int) pos[1]][(int) pos[0]] - mean), 2);

			}

			CooccFkt.matrixClearLeftDown(coocc);

		}

		for (int i = 0; i < toCalLength; i++) {

			standarddiviation[i] = Math.sqrt(standarddiviation[i]
					/ numberOfSampleGraphs);

		}

		for (int i = 0; i < toCalLength; i++) {

			if (standarddiviation[i] == 0) {

				measures.get(i)[2] = -1;
				continue;
			}

			measures.get(i)[2] = levFDSM_Means.get(i)[0] / standarddiviation[i];

		}

		return measures;
	}

	public static ArrayList<double[]> calculate(int numberOfSampleGraphs) {

		// measaures has 3 fields, [(double)i][(double)j][(double)zscore]
		ArrayList<double[]> measures = new ArrayList<double[]>();
		ArrayList<double[]> levFDSM_Means = new ArrayList<double[]>();

		BipartiteGraph bG = new BipartiteGraph(inputFile);

		int length = bG.numberOfPrimaryIds;

		MyBitSet adjM[] = bG.toSecBS();
		int[][] coocc = new int[bG.numberOfPrimaryIds][bG.numberOfPrimaryIds];
		// read original co-occurrence
		CooccFkt.readCooccSecAddTopRight(adjM, coocc);

		// The first long swap walks
		int[][] edges = bG.generateEdges();
		Random generator_edge = new Random(seed);

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjM, generator_edge);

		// set the normal length of swap walks
		int lengthOfWalks = (int) (bG.numberOfSamples * Math
				.log(bG.numberOfSamples));

		for (int i = 0; i < numberOfSampleGraphs; i++) {
			CooccFkt.swap(lengthOfWalks, edges, adjM, generator_edge);

			CooccFkt.readCooccSecAddLowerLeft(adjM, coocc);

		}

		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (coocc[i][j] > 0) {

					double mean = (double) coocc[j][i]
							/ (double) numberOfSampleGraphs;
					double levFDSM = (double) coocc[i][j] - mean;
					if (levFDSM > 0) {

						measures.add(new double[] { i, j, 0 });

						levFDSM_Means.add(new double[] { levFDSM, mean });

					}

				}

			}

		}

		double[] standarddiviation = new double[measures.size()];

		int toCalLength = standarddiviation.length;

		CooccFkt.matrixClearLeftDown(coocc);

		bG = new BipartiteGraph(inputFile);

		adjM = null;

		generator_edge = new Random(seed);

		adjM = bG.toSecBS();

		CooccFkt.swap(4 * bG.numberOfEdges, edges, adjM, generator_edge);

		for (int i = 0; i < numberOfSampleGraphs; i++) {

			CooccFkt.swap(lengthOfWalks, edges, adjM, generator_edge);
			CooccFkt.readCooccSecAddLowerLeft(adjM, coocc);

			for (int j = 0; j < toCalLength; j++) {

				double[] pos = measures.get(j);
				double mean = levFDSM_Means.get(j)[1];

				standarddiviation[j] += Math.pow(
						(coocc[(int) pos[1]][(int) pos[0]] - mean), 2);

			}

			CooccFkt.matrixClearLeftDown(coocc);

		}

		for (int i = 0; i < toCalLength; i++) {

			standarddiviation[i] = Math.sqrt(standarddiviation[i]
					/ numberOfSampleGraphs);

		}

		for (int i = 0; i < toCalLength; i++) {

			if (standarddiviation[i] == 0) {

				measures.get(i)[2] = -1;
				continue;
			}

			measures.get(i)[2] = levFDSM_Means.get(i)[0] / standarddiviation[i];

		}

		return measures;
	}

	public static void run() {

		File file = new File(outputPath);

		if (!file.exists()) {
			file.mkdirs();
		}

		ArrayList<double[]> measures = calculate();

		Text.writeListDouble(measures, zScore_GL_TXT, "Z-Score", "global list",
				"numberOfSamples = " + numberOfSampleGraphs, true);

		Text.writeLocalListDouble(measures, zScore_LL_TXT, "Z-Score",
				"local list", "");

	}

	public static void run(int numberOfSampleGraphs) {

		String outputPathLocal = outputPath.substring(0,
				outputPath.lastIndexOf("Z"))
				+ "/ZScore/" + numberOfSampleGraphs + "/";
		File file = new File(outputPathLocal);

		String zScore_GL_TXT = outputPathLocal + "ZScore_GL.txt";
		String zScore_LL_TXT = outputPathLocal + "zScore_LL.txt";

		if (!file.exists()) {
			file.mkdirs();
		}

		ArrayList<double[]> measures = calculate(numberOfSampleGraphs);

		Text.writeListDouble(measures, zScore_GL_TXT, "Z-Score", "global list",
				"numberOfSampleGraphs = " + numberOfSampleGraphs, true);

		Text.writeLocalListDouble(measures, zScore_LL_TXT, "Z-Score",
				"local list", "numberOfSampleGraphs = " + numberOfSampleGraphs);

	}

	public static void main(String[] args) {

		run();

	}

}
