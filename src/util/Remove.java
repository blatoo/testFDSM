package util;

import gnu.trove.list.array.TIntArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileManager.Location;

import structure.BipartiteGraph;
import netflix.Jaccard;

public class Remove {

	public static MyBitSet lowDegrees(int[] degrees, int minDegree) {
		MyBitSet lowDeg = new MyBitSet();
		for (int i = 0; i < degrees.length; i++) {
			if (degrees[i] < minDegree) {
				lowDeg.set(i);
			}

		}

		return lowDeg;
	}

	public static void rewriteList(String inputFile, String outputFile,
			int minDegree) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			HashMap<String, String> hm = util.Text.readLineInfos(line);
			String type = hm.get("type");

			BipartiteGraph bg = new BipartiteGraph();
			int[] degrees = bg.readPrimaryDegree();
			MyBitSet lowDegrees = lowDegrees(degrees, minDegree);
			if (type.equalsIgnoreCase("global list")
					|| type.equalsIgnoreCase("list")) {
				System.out.println("Write Global List or List");
				bw.write(line + " minDegree = " + minDegree
						+ ", numberOfExclusiveIds = "
						+ lowDegrees.cardinality() + System.lineSeparator());
				// bw.write("#ExclusiveIds = " + lowDegrees.mytoString(":"));
				rewriteGlobal(br, bw, lowDegrees);

			} else if (type.equalsIgnoreCase("local list")) {

				System.out.println("Write local list");

				bw.write(line + " minDegree = " + minDegree
						+ ", numberOfExclusiveIds = "
						+ lowDegrees.cardinality()+System.lineSeparator());
				rewriteLocal(br, bw, lowDegrees);
			}

			bw.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public static void rewriteGlobal(BufferedReader br, BufferedWriter bw,
			MyBitSet lowDegrees) throws IOException {
		String line = br.readLine();
		while (line != null) {
			StringTokenizer st = new StringTokenizer(line, ",");
			int n1 = Integer.parseInt(st.nextToken());
			int n2 = Integer.parseInt(st.nextToken());

			if (lowDegrees.get(n1) || lowDegrees.get(n2)) {
				line = br.readLine();
				continue;

			}
			bw.write(line + System.lineSeparator());
			line = br.readLine();

		}

	}

	public static void rewriteLocal(BufferedReader br, BufferedWriter bw,
			MyBitSet lowDegrees) throws IOException {

		String line = br.readLine();
		while (line != null) {

			if (!line.startsWith("#")) {
				System.err.println("Wrong in line " + line + " and exit!");
				System.exit(-1);
			}

			HashMap<String, String> hm = Text.readLineInfos(line);

			int primaryId = Integer.parseInt(hm.get("primaryId"));
			int numberOfFriends = Integer.parseInt(hm.get("numberOfFriends"));

			if (lowDegrees.get(primaryId)) {
				for (int i = 0; i < numberOfFriends; i++) {

					br.readLine();

				}

			} else {
				
//				bw.write(line+System.lineSeparator());
//
//				for (int i = 0; i < numberOfFriends; i++) {
//					line = br.readLine();
//
//					StringTokenizer st = new StringTokenizer(line, ",");
//					int friend = Integer.parseInt(st.nextToken());
//					if (!lowDegrees.get(friend)) {
//						bw.write(line + System.lineSeparator());
//					}
//
//				}
				
				ArrayList<String> mylines = new ArrayList<String>();
				
				for(int i = 0; i<numberOfFriends; i++){
					String myline = br.readLine();
					StringTokenizer st = new StringTokenizer(myline, ",");
					int friend = Integer.parseInt(st.nextToken());
					if(!lowDegrees.get(friend)){
						mylines.add(myline);
						
					}
				}
				
				int lastIndex = line.lastIndexOf("=");
				
				String infoLines = line.substring(0, lastIndex+1)+mylines.size();
				
				bw.write(infoLines+System.lineSeparator());
				
				for(int i = 0; i<mylines.size(); i++){
					
					bw.write(mylines.get(i)+System.lineSeparator());
					
				}
				
//				line = br.readLine();

			}

			line = br.readLine();

		}

	}

	public static void test() {

	}

	public static void main(String[] args) {
		int minDegree = 2;
//		String GLoutputFile = Jaccard.outputPath + "Jaccard_GL_MinDegree"
//				+ minDegree + ".txt";
		String LLoutputFile = Jaccard.outputPath + "Jaccard_LL_MinDegree"
				+ minDegree + ".txt";

		rewriteList(Jaccard.Jaccard_LL_TXT, LLoutputFile, minDegree);

		 Text.textReader2(LLoutputFile, 0, 10);

	}

}
