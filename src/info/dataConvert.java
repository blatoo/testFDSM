package info;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.StringTokenizer;

import util.MyBitSet;
import util.Text;

public class dataConvert {

	public static String inputFile = "D:/Ying/netflix/testNetflix/vervor/Good_Ratings/Netflix_Dataset_Good";

	public static String outputRoot = Setting.outputRoot;

	public static String infoTXT = outputRoot + "info.txt";

	public static void convert(String inputFile) {

		int pointPos = inputFile.lastIndexOf(".");
		

		StringBuffer fName = new StringBuffer(inputFile.substring(inputFile.lastIndexOf("/")+1));

		if (pointPos < 0) {
			fName = fName.append("_converted");
		} else {
			fName.insert(pointPos, "_converted");
		}

		String outputFile = outputRoot + fName;
		
		System.out.println("OutputFile: "+outputFile);
		
		TIntObjectHashMap<MyBitSet> userId_movieIdList = new TIntObjectHashMap<MyBitSet>();

		int sumOfCardinality = 0;

		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));

			String line = br.readLine();

			while (line != null) {
				StringTokenizer st = new StringTokenizer(line, " ");

				int movieId = Integer.parseInt(st.nextToken()) - 1;

				int userId = Integer.parseInt(st.nextToken());

				if (userId_movieIdList.contains(userId)) {
					userId_movieIdList.get(userId).set(movieId);

				} else {
					MyBitSet list = new MyBitSet();
					list.set(movieId);
					userId_movieIdList.put(userId, list);
				}

				sumOfCardinality++;
				line = br.readLine();
			}

			br.close();

			int[] userList = userId_movieIdList.keys();

			Arrays.sort(userList);

			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			HashMap<String, String> info = Text.readInfoTXT(infoTXT);

			bw.write("#numberOfSamples = " + userList.length
					+ ", numberOfPrimaryIds = "
					+ Integer.parseInt(info.get("numberOfPrimaryIds"))
					+ ", sumOfCardinarity = " + sumOfCardinality);
			bw.newLine();
			bw.write("#WorkSpaceSecondaryId:ComputerSecondaryId:Cardinality:PrimaryIds1, PrimaryIds2, ... ");
			bw.newLine();

			for (int i = 0; i < userList.length; i++) {
				int[] movieList = userId_movieIdList.get(userList[i]).toArray();
				bw.write(i + ":" + userList[i] + ":" + movieList.length + ":");
				for (int j = 0; j < movieList.length; j++) {
					bw.write(movieList[j] + ",");

				}

				bw.newLine();

			}

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public static void main(String[] args) {
		
		String inputFile = "D:/Ying/netflix/testNetflix/vervor/Good_Ratings/Netflix_Dataset_Good_100k";

		 convert(inputFile);


	}

}
