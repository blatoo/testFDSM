package util;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import netflix.Jaccard;

public class TextCorrect {

	/**
	 * correct the line with the given number.
	 * 
	 * @param lineNumber
	 *            nature number beginning with 1
	 * @param Content
	 * @param inputFile
	 * @param outputFile
	 */
	public static void line(int lineNumber, String Content, String inputFile,
			String outputFile) {
		if (lineNumber <= 0) {
			System.err.println("The line number must be greater than 1");
			System.exit(-1);
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			int cnt = 1;
			for (int i = 1; i < lineNumber; i++) {

				bw.write(line + System.lineSeparator());

				line = br.readLine();
			}

			bw.write(Content + System.lineSeparator());
			System.out.println(Content + System.lineSeparator());
			line = br.readLine();

			while (line != null) {
				bw.write(line + System.lineSeparator());

				line = br.readLine();

			}

			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);

		}

	}

	public static void main(String[] args) {
//		line(5, "aaa", "testText", "testText2.txt");
		String outputFile = Jaccard.outputPath+"Jaccard_GL_new.txt";
//		line(1, "#measure = Jaccard, type = local list,", Jaccard.Jaccard_LL_TXT,outputFile);
		Text.textReader2(Jaccard.Jaccard_LL_TXT, 0, 10);
		System.out.println();
		Text.textReader2(outputFile, 0, 10);

	}

}
