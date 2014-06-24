package netflix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

import structure.BipartiteGraph;
import structure.CooccFkt;
import util.ColumnComparator;
import util.ColumnComparatorDouble;
import util.MyBitSet;
import util.Text;

public class Jaccard {

	public static String inputFile = info.dataReadWrite.selectedEntriesSecondaryId_Model_1TXT;

	public static String outputPath = info.DataSource.outputPath + File.separator
			+ "Jaccard" + File.separator;

	// output file name for the levFDSM result:global list
	public static String Jaccard_TXT = outputPath + "Jaccard.txt";
	public static String Jaccard_GL_TXT = outputPath + "Jaccard_GL.txt";
	public static String Jaccard_LL_TXT = outputPath + "Jaccard_LL.txt";

	public static int seed = 3306;

	public static int[] readDegree(MyBitSet[] adjMPrimary) {
		int[] degrees = new int[adjMPrimary.length];
		int length = degrees.length;
		for (int i = 0; i < length; i++) {
			degrees[i] = adjMPrimary[i].cardinality();
		}

		return degrees;
	}

	public static int[][] calculate() {

		BipartiteGraph bg = new BipartiteGraph();

		ArrayList<int[]> measures = new ArrayList<int[]>();

		int[][] coocc = new int[bg.numberOfPrimaryIds][bg.numberOfPrimaryIds];

		int length = bg.numberOfPrimaryIds;

		MyBitSet[] adjMPrimary = bg.toPrimBS();

		int[] degrees = readDegree(adjMPrimary);

		CooccFkt.readCooccPrimaryAddTopRight(adjMPrimary, coocc);

		for (int i = 0; i < length; i++) {

			for (int j = i + 1; j < length; j++) {

				if (coocc[i][j] > 0) {

					double measureValue = Math
							.round((double) coocc[i][j]
									* 10000.0
									/ ((double) degrees[i]
											+ (double) degrees[j] - (double) coocc[i][j]));
					// double measureValue2 = Math.round((double)coocc[i][j] *
					// 1000
					// / ((double)degrees[i] + (double)degrees[j] -
					// (double)coocc[i][j]));
					//
					// if (measureValue > 0) {
					// measures.add(new int[] { i, j, (int)measureValue });
					// }
					//
					// System.out.println("coocc[" + i + "][" + j + "] = "
					// + coocc[i][j] + "," + "degree[" + i + "] = "
					// + degrees[i] + ", degree[" + j + "] = "
					// + degrees[j] + ", measureValue = " + measureValue);
					coocc[j][i] = (int) measureValue;

				}

			}

		}

		return coocc;
	}

	public static void run() {

		File file = new File(outputPath);

		if (!file.exists()) {
			file.mkdirs();
		}

		int[][] coocc = calculate();
		// System.out.println("measures without sort");
		// for (int i = 0; i < 10; i++) {
		// System.out.println(measures.get(i)[0] + ", " + measures.get(i)[1]
		// + ", " + measures.get(i)[2]);
		// }

		ArrayList<int[]> measures = CooccFkt.positiveMeasureLowerLeft(coocc);

		Collections.sort(measures, new ColumnComparator(-3, 1, 2));

		// System.out.println();
		// System.out.println("measures with sort");
		// for (int i = 0; i < 10; i++) {
		// System.out.println(measures.get(i)[0] + ", " + measures.get(i)[1]
		// + ", " + measures.get(i)[2]);
		// }

		// System.out.println("Write the Jaccard List");
		// Text.writeList(measures, Jaccard_TXT, "Jaccard", "List", "", false);
		//
		// System.out.println("Write the Jaccard Gloable List");
		// Text.writeList(measures, Jaccard_GL_TXT, "Jaccard", "global list",
		// "",
		// true);

		System.out.println("Write the Jaccard Local List");
		Text.writeLocalList(measures, Jaccard_LL_TXT, "Jaccard", "locallist",
				"");

	}

	public static void test() {

		BipartiteGraph bg = new BipartiteGraph();

		MyBitSet[] adjMPrimary = bg.toPrimBS();

		int[] degrees = readDegree(adjMPrimary);

		int[] abc = new int[] { 334, 744, 1754, 3198, 3557, 4910, 5156, 5200,
				5300, 5838, 41, 0, 72 };

		for (int i = 0; i < abc.length; i++) {
			System.out.println("degree(" + abc[i] + ") = " + degrees[abc[i]]);

		}

		System.out.println();

	}

	public static ArrayList<int[]> readGLTXTinList(String GlobalListTXT) {
		ArrayList<int[]> measures = new ArrayList<int[]>();

		try {
			BufferedReader br = new BufferedReader(
					new FileReader(GlobalListTXT));
			String line = br.readLine();
			HashMap<String, String> hm = Text.readLineInfos(line);
			String type = hm.get("type").trim();
			System.out.println(type);
			if (!type.equalsIgnoreCase("global list") && !type.equalsIgnoreCase("List")) {
				if(!type.equalsIgnoreCase("global list")){
					System.err.println("The global list is Wrong");
				}
				if(!type.equalsIgnoreCase("List")){
					System.err.println("The List is Wrong!");
				}
				
				System.exit(-1);
			}

			line = br.readLine();

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, ",");
				int[] value = new int[] { Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()) };
				measures.add(value);
				line = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return measures;
	}
	
	public static void creatLL(){
		ArrayList<int[]> measures = readGLTXTinList(Jaccard_TXT);
		Text.writeLocalList(measures, Jaccard_LL_TXT, "Jaccard", "local list", "");
		
		
	}

	public static void main(String[] args) {
		// run();
//		creatLL();
		 Text.textReader2(Jaccard_LL_TXT, 0, 10);

	}

}
