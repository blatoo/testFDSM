package info;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.StringTokenizer;

import com.google.common.base.Objects.ToStringHelper;

import util.MyBitSet;

public class DataExtract {

	public static String dataSet_Original = "/home/ygu/Ying/fdsmData/NetflixOrigData/data3user.txt";

	public static String outputRoot = Setting.outputRoot;

	public static String dataSet_ComputerId = outputRoot
			+ "data3user_ComputerId.txt";

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

				br.readLine();

			}

			bw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**
	 * Extract InputData for BipartiteGraph from original dataset.
	 * 
	 * @param inputOriginalDataSet
	 *            "data3user.txt"
	 * @param from
	 *            computerSecondaryId(computerUserId), >= 0, inclusive
	 * @param to
	 *            compterSecondaryId(computerUserId), exclusive
	 * @param outputFile
	 * @param Ratings
	 */
	public static void selectMode1(String inputOriginalDataSet, int from,
			int to, String outputFile, int... Ratings) {

	}

	public static void main(String[] args) {

		// selectMode1(1, 10, 3,4,5);

		OriginalId_to_ComputerId(dataSet_Original, dataSet_ComputerId);
		

	}

}
