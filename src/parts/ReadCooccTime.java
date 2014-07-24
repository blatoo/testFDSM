package parts;

import java.io.File;

import info.Setting;
import structure.BipartiteGraph;
import structure.CooccFkt;
import util.MyBitSet;

public class ReadCooccTime {

	public static void run() {

		// Define the bipartite graph
		BipartiteGraph bG = new BipartiteGraph();

		int numberOfPrimaryIds = bG.numberOfPrimaryIds;

		// Construct the bipartite graph. Format: Adjacency matrix. SecondaryIds
		// and its friends in the other side of the bipartite graph.(just like
		// user(SecondaryId) and his voted movies(PrimaryIds). )
		MyBitSet[] adjMSec = bG.toSecBS();

		int[][] coocc = new int[numberOfPrimaryIds][numberOfPrimaryIds];
		
		long t1 = System.currentTimeMillis();

		CooccFkt.readCooccSecAddTopRight(adjMSec, coocc);
		
		long t2 = System.currentTimeMillis();
		
		long t3 = t2-t1;
		
		System.out.println("read cooccurence from inputFile uses "+(double)t3/1000+" millionseconds");

		
	}
	
	public static void run(String inputFile) {

		// Define the bipartite graph
		BipartiteGraph bG = new BipartiteGraph(inputFile);

		int numberOfPrimaryIds = bG.numberOfPrimaryIds;

		// Construct the bipartite graph. Format: Adjacency matrix. SecondaryIds
		// and its friends in the other side of the bipartite graph.(just like
		// user(SecondaryId) and his voted movies(PrimaryIds). )
		MyBitSet[] adjMSec = bG.toSecBS();

		int[][] coocc = new int[numberOfPrimaryIds][numberOfPrimaryIds];
		
		long t1 = System.currentTimeMillis();

		CooccFkt.readCooccSecAddTopRight(adjMSec, coocc);
		
		long t2 = System.currentTimeMillis();
		
		long t3 = t2-t1;
		
		System.out.println("read cooccurence from inputFile uses "+(double)t3/1000+" seconds");

		
	}
	

	public static void main(String[] args) {
		run(Setting.outputRoot+File.separator+"Netflix_Dataset_Good_2"
				+ "0k_converted");
//		run(Setting.outputRoot+File.separator+"selectModel1_from0_to20000_rating45.txt");
	}

}
