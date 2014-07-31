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

	public static String outputRoot = Setting.outputRoot;

	// you don't need to change it.
	public static String infoTXT = outputRoot + "info.txt";

	public static String primaryIndexTXT = outputRoot + "PrimaryIndex.txt";

	public static String secondaryIndexTXt = outputRoot + "SecondaryIndex.txt";

	public static String dataSet_ComputerId = outputRoot
			+ "data3user_ComputerId.txt";

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

		String outputFile = outputRoot + "selectModel1_" + "from" + from
				+ "_to" + to + "_rating";

		MyBitSet ratingsMBS = new MyBitSet();

		for (int i = 0; i < ratings.length; i++) {
			ratingsMBS.set(ratings[i]);
			outputFile.concat(String.valueOf(ratings[i]));

			outputFile = outputFile + String.valueOf(ratings[i]);
		}

		outputFile = outputFile + ".txt";
		
		System.out.println("Output File is: "+outputFile);

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
	 * select the users after the given arrays with computer_userIds
	 * 
	 * @param userList
	 *            an array of computer_UserIds
	 * @param ratings
	 *            the subset of [1,2,3,4,5], for example: 4,5 means choose the
	 *            record which rating is 4 or 5
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
		
		System.out.println("Output File is: "+outputFile);

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

	/**
	 * select the users after the given arrays with computer_userIds
	 * 
	 * @param userList
	 *            an array of computer_UserIds
	 * @param outputFileName
	 *            the name of the output file, you don't need to give the path
	 * @param ratings
	 *            the subset of [1,2,3,4,5], for example: 4,5 means choose the
	 *            record which rating is 4 or 5
	 */
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

		String outputFile = outputRoot + outputFileName;

		System.out.println("Output File is: "+outputFile);
		
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

	/**
	 * select the users after the given arrays with computer_userIds in a TXT file, the TXT file is a list which Computer_UserIds, each line contain one Computer_UserId
	 * 
	 * @param userListTXT
	 * @param outputFileName
	 * @param ratings
	 */
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

		String outputFile = outputRoot + outputFileName;
		
		System.out.println("Output File is: "+outputFile);

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


	public static void main(String[] args) {

		// selectMode1(dataSet_ComputerId, 0, 10000, 4, 5);

		// selectModel2(userList, 4, 5);

		String userListTXT = outputRoot + File.separator
				+ "userList_ComputerId.txt";

		selectMode1(dataSet_ComputerId, 0, 20000, 4, 5);

	}

}
