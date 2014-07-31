package info;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javolution.util.FastBitSet;
import javolution.util.Index;
import util.MyBitSet;
import util.Text;

public class GenerateInputData {
	
//	public static String dataSet_Original = "/home/ygu/Ying/fdsmData/NetflixOrigData/data3user.txt";
	public static String dataSet_Original = "D:/Ying/netflix/NetflixOrigData/data3user.txt";

	public static String outputRoot = Setting.outputRoot;
	
	public static String infoTXT = Setting.outputRoot+"info.txt";
	
	/**
	 * generate the computer indexes for the data, the outputs are two computer
	 * indexes for the primary column and secondary column. There will be three
	 * file as output: 1. info.txt 2. PrimaryIndex.txt 3. SecondaryIndex.txt
	 * 
	 * @param inputData
	 * @param primaryColumn
	 *            native number of the goal Column
	 * @param secondaryColumn
	 *            native number of the Column
	 */
	public static void dataIndex(String inputData, int primaryColumn,
			int secondaryColumn) {
		
		String infoTXT = outputRoot+"info.txt";
		String primaryIndexTXT = outputRoot+"PrimaryIndex.txt";
		String secondaryIndexTXT = outputRoot+"SecondaryIndex.txt";

		File outputPath = new File(outputRoot);

		File file = new File(inputData);

		if (!file.exists()) {

			System.err.println("Sorry, I can't find the input File! :-/ ");
			System.exit(-1);

		}

		if (!outputPath.exists()) {
			outputPath.mkdir();
		}

		FastBitSet fbsPrimaryList = new FastBitSet();
		FastBitSet fbsSecondaryList = new FastBitSet();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line = br.readLine();

			while (line != null) {

				String[] lineInfo = line.split(",");

				fbsPrimaryList.set(Integer
						.parseInt(lineInfo[primaryColumn - 1]));
				fbsSecondaryList.set(Integer
						.parseInt(lineInfo[secondaryColumn - 1]));

				line = br.readLine();

			}

			br.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

		Index[] indPrimary = fbsPrimaryList.toArray(new Index[0]);
		Index[] indSecondary = fbsSecondaryList.toArray(new Index[0]);

		int cnt = 0;

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					primaryIndexTXT));

			bw.write("#numberOfIndexs=" + indPrimary.length
					+ System.lineSeparator());
			bw.write("#<computer_primaryId>, <original_primaryId>"+System.lineSeparator());

			for (Index ind : indPrimary) {

				bw.write(cnt + "," + ind + System.lineSeparator());

				cnt++;
			}

			bw.close();

			cnt = 0;

			bw = new BufferedWriter(new FileWriter(secondaryIndexTXT));

			bw.write("#numberOfIndexs=" + indSecondary.length
					+ System.lineSeparator());
			bw.write("#<computer_secondaryId>, <original_secondaryId>"+System.lineSeparator());


			for (Index ind : indSecondary) {

				bw.write(cnt + "," + ind + System.lineSeparator());

				cnt++;

			}

			bw.close();

			bw = new BufferedWriter(new FileWriter(infoTXT));

			bw.write("#Number of PrimaryIds:" + System.lineSeparator());
			bw.write("numberOfPrimaryIds=" + indPrimary.length
					+ System.lineSeparator());
			bw.write("#Number Of SecondaryIds:" + System.lineSeparator());
			bw.write("numberOfSecondaryIds=" + indSecondary.length
					+ System.lineSeparator());

			bw.close();
		} catch (IOException e) {
			System.exit(-1);

		}

	}


	/**
	 * rewrite the original Netflix data set with computerIds.(computer_MovieId
	 * and computer_UserId start from 0)
	 * 
	 * @param dataSet_Original
	 * @param dataSet_ComputerId
	 */
	public static void originalId_to_computerId(String dataSet_Original) {
		
		String inputFileName = dataSet_Original.substring(dataSet_Original.lastIndexOf("/")+1);
		System.out.println(inputFileName);
		
		inputFileName = inputFileName.replace(".txt", "_ComputerId.txt");
		
		String dataSet_ComputerId = outputRoot+inputFileName;
		
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					dataSet_Original));
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					dataSet_ComputerId));

			HashMap<String, String> infoHM = Text.readInfoTXT(infoTXT);

			int numberOfPrimaryIds = Integer.parseInt(infoHM
					.get("numberOfPrimaryIds"));
			int numberOfSecondaryIds = Integer.parseInt(infoHM
					.get("numberOfSecondaryIds"));

			bw.write("#numberOfPrimaryIds = " + numberOfPrimaryIds
					+ ", numberOfSecondaryIds = " + numberOfSecondaryIds);
			bw.newLine();

			MyBitSet userId_List = new MyBitSet();

			int Computer_UserId = -1;

			String line = br.readLine();

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, ",");
				int movieId = Integer.valueOf(st.nextToken());
				int userId = Integer.valueOf(st.nextToken());
				String rating = st.nextToken();

				int computer_MovieId = movieId - 1;

				if (userId_List.get(userId) == false) {
					userId_List.set(userId);
					Computer_UserId++;
				}

				bw.write(computer_MovieId + "," + Computer_UserId + ","
						+ rating + System.lineSeparator());

				line = br.readLine();

			}

			bw.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}
	
	/**
	 * Read a txt file
	 * @param inputFile 
	 * @param from begin with 1 inclusive
	 * @param to inclusive
	 */
	public static void readData(String inputFile, int from, int to){
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = br.readLine();
			int cnt = 1;
			while(line != null){
				if(cnt < from){
					line = br.readLine();
					continue;
					
				}
				
				if(cnt > to){
					break;
				}
				
				System.out.println(line);
				
				line = br.readLine();
				cnt++;
			}
			
			
			
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
	}
	
	

	public static void main(String[] args) {

		dataIndex(dataSet_Original, 1, 2);
		
		originalId_to_computerId(dataSet_Original);
		
		
		
	}

}
