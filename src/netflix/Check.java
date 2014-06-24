package netflix;

import info.DataSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.RandomAccess;
import java.util.StringTokenizer;

import util.Remove;

public class Check {

	public static String movieTitleTXT = info.DataSource.Pfad_doc
			+ "movie_titles.txt";

	public static String outputPath = info.DataSource.outputPath + "netflix/";

	public static void readGL_Top(String levFDSM_GL_TXT, String movieTitleTXT, int top,
			String outputFile) {

		ArrayList<String[]> movieTitles = new ArrayList<String[]>();

		movieTitles = readMovieTitles(movieTitleTXT);

		try {

			BufferedReader br = new BufferedReader(new FileReader(
					levFDSM_GL_TXT));

			String line = br.readLine();

			HashMap<String, String> hm = util.Text.readLineInfos(line);
			
			int numberOflines = Integer.parseInt(hm.get("length"));

			if (Math.abs(top) > numberOflines) {
				System.err
						.println("You can't input a number which bigger than the number of the total cooccurences.");
				System.exit(-1);
			}

			int begin = 0;

			if (top < 0) {
				begin = numberOflines + top;
			}

			for (int i = 0; i < begin; i++) {

				br.readLine();

			}

			File file = new File(outputPath);

			if (!file.exists()) {
				file.mkdir();
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

			bw.write("#Format: PrimaryId1, year1, title1, PrimaryId2, year2, title2, measure");
			bw.write("#Top = " + top);

			top = Math.abs(top);


			for (int i = 0; i < top; i++) {

				line = br.readLine();

				StringTokenizer st = new StringTokenizer(line, ",");

				int[] cooccInfo = new int[] { Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()) };

				String details = cooccInfo[0] + ","
						+ movieTitles.get(cooccInfo[0])[0] + ","
						+ movieTitles.get(cooccInfo[0])[1] + "," + cooccInfo[1]
						+ "," + movieTitles.get(cooccInfo[1])[0] + ","
						+ movieTitles.get(cooccInfo[1])[1] + "," + cooccInfo[2];

				bw.write(details + System.lineSeparator());
				System.out.println(details);

			}


			bw.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**
	 * With ComputerId (PrimaryIds)
	 */
	public static ArrayList<String[]> readMovieTitles(String movieTitleTXT) {

		ArrayList<String[]> movieTitles = new ArrayList<String[]>();

		File file = new File(movieTitleTXT);

		if (!file.exists()) {
			System.err
					.println("movieTitleTXT doesn't exist! Please give the right path!");
			System.exit(-1);
		}

		try {

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				st.nextToken();
				String[] infos = new String[] { st.nextToken(), st.nextToken() };

				movieTitles.add(infos);

				line = br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return movieTitles;

	}
	
	public static void readLL(int[] primaryIds, String movieTitleTXT, String outputFile){
		ArrayList<String[]> movieTitles = readMovieTitles(movieTitleTXT);
		
		
		
		
		
	}

	public static void test() {

	}

	public static void main(String[] args) {

//		String outputFile = outputPath + "levFDSM_GL_Top.txt";
//		readGL_Top(algo.LevFDSM.levFDSM_GL_TXT, movieTitleTXT, 100, outputFile);
		readGL_Top(Jaccard.outputPath+"Jaccard_GL_MinDegree2.txt", movieTitleTXT, 100, Jaccard.outputPath+"Jaccard_GL_MinDegree2Top100.txt");
		
		

	}

}
