package info;

public class DataSource {

	// Windows
	
	// public static String Pfad_doc = "D:/Ying/netflix/";
	//
	// public static String outputPath = "D:/Ying/netflix/output/";
	//
	// public static String data3user = Pfad_doc+"data3user.txt";

	
	// Linux

	public static String Pfad_doc = "/home/ygu/Ying/fdsmData/";

	public static String outputPath = "/home/ygu/Ying/fdsmData/Example/Output/";

	public static String data3user = "/home/ygu/Ying/fdsmData/NetflixOrigData/"
			+ "data3user.txt";

	public static int primaryColumn = 1;

	public static int secondaryColumn = 2;

	public static int ratingColumn = 3;

	public static int ratingCriterion = 3;

	public static String lineSeparate = System.lineSeparator();

	public static int selectModel = 1; // 1:select from ... to .., 2:random

	public static int fromInd = 0;

	public static int toInd = 20000;

	public static int numberOfSamples = 20000;

}
