package info;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

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
	 * rewrite the original Netflix data set with computerIds.(computer_MovieId
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

		String outputFile = outputRoot + File.separator + "selectModel1_"
				+ "from" + from + "_to" + to + "_rating";

		MyBitSet ratingsMBS = new MyBitSet();

		for (int i = 0; i < ratings.length; i++) {
			ratingsMBS.set(ratings[i]);
			outputFile.concat(String.valueOf(ratings[i]));

			outputFile = outputFile + String.valueOf(ratings[i]);
		}

		outputFile = outputFile + ".txt";

		TIntObjectHashMap<MyBitSet> user_MoviesList = new TIntObjectHashMap<MyBitSet>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					dataSet_ComputerId));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

			String line = br.readLine();

			HashMap<String, String> infoHM = Text.readLineInfos(line);

			line = br.readLine();

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

				line = br.readLine();

			}

			int[] userList = user_MoviesList.keys();

			Arrays.sort(userList);

			int numberOfUsers = userList.length;

			bw.write("#numberOfSamplesAll = " + (to - from)
					+ ", numberOfSamples = " + numberOfUsers + ", from = "
					+ from + ", to = " + to + ", " + "numberOfPrimaryIds = "
					+ Integer.parseInt(infoHM.get("numberOfPrimaryIds"))
					+ ", sumOfCardinarity = " + sumOfCardinality
					+ System.lineSeparator());

			bw.write("#WorkSpaceSecondaryId:ComputerSecondaryId:Cardinality:PrimaryIds1, PrimaryIds2, ... "
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

	/**
	 * select the users according the given arrays with computer_userIds
	 * @param userList
	 * @param ratings
	 */
	public static void selectModel2(int[] userList, int... ratings) {

		// check valid
		Arrays.sort(userList);

		if (userList[0] < 0 || userList[userList.length - 1] > 480188) {
			System.err.println("UserId should be between 0 and 480188.");
			System.exit(-1);
		}

		Arrays.sort(ratings);

		if (ratings[0] < 1 || ratings[ratings.length - 1] > 5) {
			System.err.println("Ratings should be between 0 and 5. ");
			System.exit(-1);
		}

		// construct outputFile
		String rats = null;
		for (int i = 0; i < ratings.length; i++) {
			rats += String.valueOf(ratings[i]);

		}

		String outputFile = outputRoot + File.separator + "selectModel2_rating"
				+ rats + ".txt";

		// construct user_MoviesList
		TIntObjectHashMap<MyBitSet> user_MoviesList = new TIntObjectHashMap<MyBitSet>();

		MyBitSet userListMBS = new MyBitSet();

		for (int i = 0; i < userList.length; i++) {

			userListMBS.set(userList[i]);

		}

		int sumOfCardinality = 0;

		HashMap<String, String> infoHM = new HashMap<String, String>();

		try {

			BufferedReader br = new BufferedReader(new FileReader(
					dataSet_ComputerId));

			String line = br.readLine();

			infoHM = Text.readLineInfos(line);

			line = br.readLine();

			MyBitSet ratingMBS = new MyBitSet();
			for (int i = 0; i < ratings.length; i++) {
				ratingMBS.set(ratings[i]);
			}

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, ",");
				int movieId = Integer.parseInt(st.nextToken());
				int userId = Integer.parseInt(st.nextToken());
				int rating = Integer.parseInt(st.nextToken());

				if (userId < userList[0]) {
					line = br.readLine();
					continue;
				}

				if (userId > userList[userList.length - 1]) {
					break;
				}

				if (userListMBS.get(userId) == false) {
					line = br.readLine();
					continue;
				}

				if (ratingMBS.get(rating) == false) {
					line = br.readLine();
					continue;
				}

				if (user_MoviesList.containsKey(userId) == true) {
					user_MoviesList.get(userId).set(movieId);
					sumOfCardinality++;

				} else {
					MyBitSet list = new MyBitSet();
					list.set(movieId);
					user_MoviesList.put(userId, list);
					sumOfCardinality++;
				}

				line = br.readLine();
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		int[] computerUserIds = user_MoviesList.keys();
		Arrays.sort(computerUserIds);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

			bw.write("#numberOfSamplesAll = " + userList.length
					+ ", numberOfSamples = " + computerUserIds.length
					+ ", numberOfPrimaryIds = "
					+ Integer.parseInt(infoHM.get("numberOfPrimaryIds"))
					+ ", sumOfCardinarity = " + sumOfCardinality);

			bw.newLine();
			bw.write("#WorkSpaceSecondaryId:ComputerSecondaryId:Cardinality:PrimaryIds1, PrimaryIds2, ... ");
			bw.newLine();

			for (int i = 0; i < computerUserIds.length; i++) {

				int[] moviesList = user_MoviesList.get(computerUserIds[i])
						.toArray();
				bw.write(i + ":" + computerUserIds[i] + ":" + moviesList.length
						+ ":");

				for (int j = 0; j < moviesList.length; j++) {
					bw.write(moviesList[j] + ",");
				}
				bw.newLine();
			}

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public static void selectModel2(int[] userList, String outputFileName,
			int... ratings) {

		// check valid
		Arrays.sort(userList);

		if (userList[0] < 0 || userList[userList.length - 1] > 480188) {
			System.err.println("UserId should be between 0 and 480188.");
			System.exit(-1);
		}

		Arrays.sort(ratings);

		if (ratings[0] < 1 || ratings[ratings.length - 1] > 5) {
			System.err.println("Ratings should be between 0 and 5. ");
			System.exit(-1);
		}

		// // construct outputFile
		// String rats = null;
		// for (int i = 0; i < ratings.length; i++) {
		// rats += String.valueOf(ratings[i]);
		//
		// }

		String outputFile = outputRoot + File.separator + outputFileName;

		// construct user_MoviesList
		TIntObjectHashMap<MyBitSet> user_MoviesList = new TIntObjectHashMap<MyBitSet>();

		MyBitSet userListMBS = new MyBitSet();

		for (int i = 0; i < userList.length; i++) {

			userListMBS.set(userList[i]);

		}

		int sumOfCardinality = 0;

		HashMap<String, String> infoHM = new HashMap<String, String>();

		try {

			BufferedReader br = new BufferedReader(new FileReader(
					dataSet_ComputerId));

			String line = br.readLine();

			infoHM = Text.readLineInfos(line);

			line = br.readLine();

			MyBitSet ratingMBS = new MyBitSet();
			for (int i = 0; i < ratings.length; i++) {
				ratingMBS.set(ratings[i]);
			}

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, ",");
				int movieId = Integer.parseInt(st.nextToken());
				int userId = Integer.parseInt(st.nextToken());
				int rating = Integer.parseInt(st.nextToken());

				if (userId < userList[0]) {
					line = br.readLine();
					continue;
				}

				if (userId > userList[userList.length - 1]) {
					break;
				}

				if (userListMBS.get(userId) == false) {
					line = br.readLine();
					continue;
				}

				if (ratingMBS.get(rating) == false) {
					line = br.readLine();
					continue;
				}

				if (user_MoviesList.containsKey(userId) == true) {
					user_MoviesList.get(userId).set(movieId);
					sumOfCardinality++;

				} else {
					MyBitSet list = new MyBitSet();
					list.set(movieId);
					user_MoviesList.put(userId, list);
					sumOfCardinality++;
				}

				line = br.readLine();
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		int[] computerUserIds = user_MoviesList.keys();
		Arrays.sort(computerUserIds);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

			bw.write("#numberOfSamplesAll = " + userList.length
					+ ", numberOfSamples = " + computerUserIds.length
					+ ", numberOfPrimaryIds = "
					+ Integer.parseInt(infoHM.get("numberOfPrimaryIds"))
					+ ", sumOfCardinarity = " + sumOfCardinality);

			bw.newLine();
			bw.write("#WorkSpaceSecondaryId:ComputerSecondaryId:Cardinality:PrimaryIds1, PrimaryIds2, ... ");
			bw.newLine();

			for (int i = 0; i < computerUserIds.length; i++) {

				int[] moviesList = user_MoviesList.get(computerUserIds[i])
						.toArray();
				bw.write(i + ":" + computerUserIds[i] + ":" + moviesList.length
						+ ":");

				for (int j = 0; j < moviesList.length; j++) {
					bw.write(moviesList[j] + ",");
				}
				bw.newLine();
			}

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public static void selectModel2(String userListTXT, String outputFileName,
			int... ratings) {

		MyBitSet userListMBS = new MyBitSet();

		try {
			BufferedReader br = new BufferedReader(new FileReader(userListTXT));
			String line = br.readLine();

			while (line != null) {
				int userId = Integer.parseInt(line);

				userListMBS.set(userId);

				line = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		int[] userList = userListMBS.toArray();

		if (userList[0] < 0 || userList[userList.length - 1] > 480188) {
			System.err.println("UserId should be between 0 and 480188.");
			System.exit(-1);
		}

		Arrays.sort(ratings);

		if (ratings[0] < 1 || ratings[ratings.length - 1] > 5) {
			System.err.println("Ratings should be between 0 and 5. ");
			System.exit(-1);
		}

		String outputFile = outputRoot + File.separator + outputFileName;

		// construct user_MoviesList
		TIntObjectHashMap<MyBitSet> user_MoviesList = new TIntObjectHashMap<MyBitSet>();

		for (int i = 0; i < userList.length; i++) {

			userListMBS.set(userList[i]);

		}

		int sumOfCardinality = 0;

		HashMap<String, String> infoHM = new HashMap<String, String>();

		try {

			BufferedReader br = new BufferedReader(new FileReader(
					dataSet_ComputerId));

			String line = br.readLine();

			infoHM = Text.readLineInfos(line);

			line = br.readLine();

			MyBitSet ratingMBS = new MyBitSet();
			for (int i = 0; i < ratings.length; i++) {
				ratingMBS.set(ratings[i]);
			}

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, ",");
				int movieId = Integer.parseInt(st.nextToken());
				int userId = Integer.parseInt(st.nextToken());
				int rating = Integer.parseInt(st.nextToken());

				if (userId < userList[0]) {
					line = br.readLine();
					continue;
				}

				if (userId > userList[userList.length - 1]) {
					break;
				}

				if (userListMBS.get(userId) == false) {
					line = br.readLine();
					continue;
				}

				if (ratingMBS.get(rating) == false) {
					line = br.readLine();
					continue;
				}

				if (user_MoviesList.containsKey(userId) == true) {
					user_MoviesList.get(userId).set(movieId);
					sumOfCardinality++;

				} else {
					MyBitSet list = new MyBitSet();
					list.set(movieId);
					user_MoviesList.put(userId, list);
					sumOfCardinality++;
				}

				line = br.readLine();
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		int[] computerUserIds = user_MoviesList.keys();
		Arrays.sort(computerUserIds);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

			bw.write("#numberOfSamplesAll = " + userList.length
					+ ", numberOfSamples = " + computerUserIds.length
					+ ", numberOfPrimaryIds = "
					+ Integer.parseInt(infoHM.get("numberOfPrimaryIds"))
					+ ", sumOfCardinarity = " + sumOfCardinality);

			bw.newLine();
			bw.write("#WorkSpaceSecondaryId:ComputerSecondaryId:Cardinality:PrimaryIds1, PrimaryIds2, ... ");
			bw.newLine();

			for (int i = 0; i < computerUserIds.length; i++) {

				int[] moviesList = user_MoviesList.get(computerUserIds[i])
						.toArray();
				bw.write(i + ":" + computerUserIds[i] + ":" + moviesList.length
						+ ":");

				for (int j = 0; j < moviesList.length; j++) {
					bw.write(moviesList[j] + ",");
				}
				bw.newLine();
			}

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}
	
	public static void test(){
		
		try {
			
			String outputFile = outputRoot+File.separator+"userList_ComputerId.txt";
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			
			for(int i=0; i<10000;i++){
				bw.write(i+System.lineSeparator());
				
			}
			
			
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			
		}
		
	}

	public static void main(String[] args) {

		// OriginalId_to_ComputerId(dataSet_Original, dataSet_ComputerId);

		// selectMode1(dataSet_ComputerId, 0, 10000, 4, 5);


//		selectModel2(userList, 4, 5);
		
		String userListTXT = outputRoot+File.separator+"userList_ComputerId.txt";
		
		
		
		
		
		
		

	}

}
