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

import com.google.common.base.Objects.ToStringHelper;

import util.MyBitSet;
import util.Text;

public class DataExtract {

	// Settings for general
	public static String dataSet_Original = "/home/ygu/Ying/fdsmData/NetflixOrigData/data3user.txt";

	public static String outputRoot = Setting.outputRoot;

	// you don't need to change it.
	public static String infoTXT = outputRoot + File.separator + "info.txt";

	public static String primaryIndexTXT = outputRoot + File.separator
			+ "PrimaryIndex.txt";

	public static String secondaryIndexTXt = outputRoot + File.separator
			+ "SecondaryIndex.txt";

	public static String dataSet_ComputerId = outputRoot + File.separator
			+ "data3user_ComputerId.txt";

	/**
	 * rewrite the original netflix data set with computerIds.(computer_MovieId
	 * and computer_UserId start from 0)
	 * 
	 * @param dataSet_Original
	 * @param dataSet_ComputerId
	 */
	public static void OriginalId_to_ComputerId(String dataSet_Original,
			String dataSet_ComputerId) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					dataSet_Original));
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					dataSet_ComputerId));

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
	 * Extract InputData for BipartiteGraph from original Netflix data set.
	 * 
	 * @param inputOriginalDataSet
	 *            "data3user.txt"
	 * @param from
	 *            computerSecondaryId(computerUserId), >= 0, inclusive
	 * @param to
	 *            compterSecondaryId(computerUserId), < 480,189, exclusive
	 * @param outputFile
	 * @param Ratings
	 */
	public static void selectMode1(String dataSet_ComputerId, int from, int to,
			 int... ratings) {
		
		String outputFile = outputRoot+File.separator+"selectModel1_"+"from"+from+"_to"+to+"_rating";

		MyBitSet ratingsMBS = new MyBitSet();

		for (int i = 0; i < ratings.length; i++) {
			ratingsMBS.set(ratings[i]);
			outputFile.concat(String.valueOf(ratings[i]));
			
			outputFile = outputFile+String.valueOf(ratings[i]);
		}
		
		outputFile = outputFile+".txt";

		TIntObjectHashMap<MyBitSet> user_MoviesList = new TIntObjectHashMap<MyBitSet>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					dataSet_ComputerId));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

			String line = br.readLine();

			int sumOfCardinality = 0;
			

			while (line != null) {
				
				
				StringTokenizer st = new StringTokenizer(line, ",");

				int movieId = Integer.parseInt(st.nextToken());
				int userId = Integer.parseInt(st.nextToken());
				int rating = Integer.parseInt(st.nextToken());

				if (userId < from) {
					line = br.readLine();
					continue;
					
				} else if (userId >= to) {
					break;
				}

				if (ratingsMBS.get(rating) == false) {
					line = br.readLine();
					continue;
				}

				if (!user_MoviesList.containsKey(userId)) {

					user_MoviesList.put(userId, new MyBitSet());
					user_MoviesList.get(userId).set(movieId);

					sumOfCardinality++;

				} else {
					user_MoviesList.get(userId).set(movieId);
					sumOfCardinality++;
				}
				
				System.out.println(line);

				line = br.readLine();
				
				
			}
			
			System.out.println("Huhu...");

			HashMap<String, String> info = Text.readInfoTXT(infoTXT);

			int[] userList = user_MoviesList.keys();

			Arrays.sort(userList);

			int numberOfUsers = userList.length;

			bw.write("#numberOfSamples = " + numberOfUsers + ", from = " + from
					+ ", to = " + to + ", " + "numberOfPrimaryIds = "
					+ Integer.parseInt(info.get("numberOfPrimaryIds"))
					+ ", sumOfCardinarity = " + sumOfCardinality
					+ System.lineSeparator());

			bw.write("#WorkSpaceSecondaryId:ComputerSecondaryId:Cardinality:PrimaryIds"
					+ System.lineSeparator());

			for (int i = 0; i < numberOfUsers; i++) {

				int[] moviesList = user_MoviesList.get(userList[i]).toArray();

				bw.write(i + ":" + userList[i] + ":" + moviesList.length + ":");

				for (int j = 0; j < moviesList.length; j++) {
					bw.write(moviesList[j] + ",");

				}
				bw.write(System.lineSeparator());

			}

			bw.close();

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public static void main(String[] args) {


		// OriginalId_to_ComputerId(dataSet_Original, dataSet_ComputerId);
		
		selectMode1(dataSet_ComputerId, 0, 20000, 3,4,5);
	}

}
