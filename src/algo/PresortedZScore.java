package algo;

import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.map.hash.TDoubleObjectHashMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import info.Setting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.rmi.CORBA.Util;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileManager.Location;

import util.ColumnComparatorDouble;
import util.Edge;

public class PresortedZScore {
	
	// for general settings:
	public static int numberOfSampleGraphs = Setting.numberOfSampleGraphs;
	public static String inputFile = Setting.inputFile;
	public static String outputPath = Setting.outputRoot + "PresortedZScore/"
			+ numberOfSampleGraphs + "/";
	public static int seed = Setting.seed;

	// for individual settings:

//	public static int numberOfSampleGraphs = 5;
//
//	int seed = 3306;
//
//	public static String inputFile = "Example/Output/selectedEntriesSecondaryId_Model_1.txt";
//
//	public static String outputPath = "Example/Output/" + "PresortedZScore/"
//			+ numberOfSampleGraphs + "/";

	public static String PresortedZScore_GL_TXT = outputPath
			+ "PresortedZScore_GL.txt";

	public static String PresortedZScore_LL_TXT = outputPath
			+ "PresortedZScore_LL.txt";

	public static void checkPValueZScore() {

		String outputRoot = outputPath
				.substring(0, outputPath.lastIndexOf("P"));

		String PValueFile = outputRoot + "/PValue/" + numberOfSampleGraphs
				+ "/PValue_GL.txt";

		String ZScoreFile = outputRoot + "/ZScore/" + numberOfSampleGraphs
				+ "/ZScore_GL.txt";

		File file1 = new File(PValueFile);
		File file2 = new File(ZScoreFile);

		if (!file1.exists()) {

			PValue.run(numberOfSampleGraphs, 1.0);

		}

		if (!file2.exists()) {

			Zscore.run(numberOfSampleGraphs);

		}

	}

	public static ArrayList<int[]> calculate() {
		
		checkPValueZScore();

		File file = new File(outputPath);

		if (!file.exists()) {
			file.mkdirs();
		}

		ArrayList<int[]> measures = new ArrayList<int[]>();

		TDoubleObjectHashMap<ArrayList<double[]>> smallZscoreList = new TDoubleObjectHashMap<ArrayList<double[]>>();

		String outputRoot = outputPath
				.substring(0, outputPath.lastIndexOf("P"));

		String PValueFile = outputRoot + "/PValue/" + numberOfSampleGraphs
				+ "/PValue_GL.txt";

		String ZScoreFile = outputRoot + "/ZScore/" + numberOfSampleGraphs
				+ "/ZScore_GL.txt";

		TObjectDoubleHashMap<Edge> zScores = new TObjectDoubleHashMap<Edge>();

		try {

			BufferedReader br = new BufferedReader(new FileReader(ZScoreFile));
			br.readLine();

			String line = br.readLine();

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, ",");

				zScores.put(
						new Edge(Integer.parseInt(st.nextToken()), Integer
								.parseInt(st.nextToken())), Double
								.parseDouble(st.nextToken()));

				line = br.readLine();
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		try {

			BufferedReader br = new BufferedReader(new FileReader(PValueFile));

			br.readLine();

			String line = br.readLine();

			int m1 = 0;
			int m2 = 0;
			double PValue_Old = -1;

			boolean mPosted = false;
			boolean nPosted = false;

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, ",");

				int n1 = Integer.parseInt(st.nextToken());
				int n2 = Integer.parseInt(st.nextToken());
				double pValue = Double.parseDouble(st.nextToken());

				if (pValue == PValue_Old) {

					if (mPosted == true) {
						double zscore = zScores.get(new Edge(n1, n2));
						int smallZScoreListSize = smallZscoreList.size();
						int pValueListSize = smallZscoreList.get(pValue).size();
						// System.out.println("Pvalue = "+pValue+", smallZScoreList.size() = "+smallZscoreList.size()+", smallZScoreList.get("+pValue+") = "+smallZscoreList.get(pValue).size());
						smallZscoreList.get(pValue).add(
								new double[] { n1, n2, pValue, zscore });
						mPosted = true;
						m1 = n1;
						m2 = n2;
					} else {
						// smallZscoreList.put(pValue, new ArrayList<>());
						ArrayList<double[]> pValueArrayList = new ArrayList<double[]>();
						double zscore_old = zScores.get(new Edge(m1, m2));
						double[] m = new double[] { m1, m2, PValue_Old,
								zscore_old };
						double zscore = zScores.get(new Edge(n1, n2));
						double[] n = new double[] { n1, n2, PValue_Old, zscore };

						pValueArrayList.add(m);
						pValueArrayList.add(n);

						smallZscoreList.put(pValue, pValueArrayList);

						mPosted = true;
						m1 = n1;
						m2 = n2;

					}

				} else {
					mPosted = false;
					PValue_Old = pValue;
					m1 = n1;
					m2 = n2;

				}

				line = br.readLine();

			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(PValueFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					PresortedZScore_GL_TXT));

			String PValueInfoLine = br.readLine();
			String PresortedZScoreInfoLine = PValueInfoLine.replace("PValue", "PresortedZScore");
			
			bw.write(PresortedZScoreInfoLine+System.lineSeparator());
			bw.write("#Format: <PrimaryId1>,<PrimaryId2>,<PValue>,<ZScore>"+System.lineSeparator());
			String line = br.readLine();

			while (line != null) {
				StringTokenizer st = new StringTokenizer(line, ",");

				st.nextToken();
				st.nextToken();
				double pValue = Double.parseDouble(st.nextToken());

				if (smallZscoreList.contains(pValue)) {
					ArrayList<double[]> zscoreList = smallZscoreList
							.get(pValue);
					smallZscoreList.remove(pValue);

					Collections.sort(zscoreList, new ColumnComparatorDouble(-4,
							1, 2));
					int zscoreListSize = zscoreList.size();
					for (int i = 0; i < zscoreListSize; i++) {
						bw.write((int)zscoreList.get(i)[0] + ","
								+ (int)zscoreList.get(i)[1] + ","
								+ zscoreList.get(i)[2] + ","
								+ zscoreList.get(i)[3]);
						bw.newLine();

					}
					
					for(int i=0; i<zscoreListSize-1; i++){
						br.readLine();
					}
					
				}else {
					bw.write(line);
					bw.newLine();
				}
				
				

				line = br.readLine();
			}

			bw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		measures = readPZScore(PresortedZScore_GL_TXT);

		return measures;
	}
	
		
		

	public static ArrayList<int[]> readPZScore(String PresortedZScore_GL_TXT){
		ArrayList<int[]> measure = new ArrayList<int[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(PresortedZScore_GL_TXT));
			String lineInfos = br.readLine();
			HashMap<String, String> info = util.Text.readLineInfos(lineInfos);
			int length = Integer.parseInt(info.get("length"));
			
			br.readLine();
			String line = br.readLine();
			
			int score = length;
			while(line != null){
				StringTokenizer st = new StringTokenizer(line, ",");
				int n1 = Integer.parseInt(st.nextToken());
				int n2 = Integer.parseInt(st.nextToken());
				measure.add(new int[]{n1,n2,score});
				score--;
				line = br.readLine();
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		return measure;
	}
	
	public static void run(){
		
		ArrayList<int[]>measure = calculate();
		
		util.Text.writeLocalList(measure, PresortedZScore_LL_TXT, "PresortedZScore", "local list", "");
		
		
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		run();
		
		
		
		
	}

}
