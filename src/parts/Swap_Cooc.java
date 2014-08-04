package parts;

import java.util.Random;

import info.Setting;
import structure.BipartiteGraph;
import structure.CooccFkt;
import util.MyBitSet;

public class Swap_Cooc {

	public static void main(String[] args) {

		String inputFile = Setting.outputRoot+"Netflix_Dataset_Good_10k_converted";

		BipartiteGraph bG = new BipartiteGraph(inputFile);
		
		Random rnd = new Random(Setting.seed);

		int numberOfSwaps = (int) (Math.log(bG.numberOfEdges)*bG.numberOfEdges);

		MyBitSet[] adjMSec = bG.toSecBS();
		
		int[][] coocc = new int[bG.numberOfPrimaryIds][bG.numberOfPrimaryIds];

		long swapTime = 0;
		long coocTime = 0;
		int[][] edges = bG.generateEdges(); 

		for(int i=0; i<50; i++){
			
			System.out.println("Round "+i+"...");
			System.out.println("Swap steps is "+numberOfSwaps);

			long t1 = System.currentTimeMillis();
			CooccFkt.swap(numberOfSwaps, edges, adjMSec, rnd);
			long t2 = System.currentTimeMillis();
			swapTime += t2-t1;
			
			
			System.out.println("Read cooccurrence...");
			long t3 = System.currentTimeMillis();
			CooccFkt.readCooccSecAddTopRight(adjMSec, coocc);
			long t4 = System.currentTimeMillis();
			coocTime += t4-t3;
		}
		System.out.println("Swap takes "+(double) swapTime/50000.0+ " seconds");
		System.out.println("Read Cooccurrence takes "+(double) coocTime/50000.0+ " seconds");

		
		
		
		
		
	}

}
