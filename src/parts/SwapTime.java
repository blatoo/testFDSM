package parts;

import java.util.Random;

import info.Setting;
import structure.BipartiteGraph;
import structure.CooccFkt;
import util.MyBitSet;

public class SwapTime {
	
	public static void run(int numberOfSwaps){
		
		BipartiteGraph bG = new BipartiteGraph(Setting.inputFile);
		
		int length = bG.numberOfPrimaryIds;
		
		int[][] coocc = new int[length][length];
		
		MyBitSet[] adjMSec = bG.toSecBS();
		
		int[][] edges = bG.generateEdges();
		
		
		Random generator_edges = new Random(Setting.seed);

		long t1 = System.currentTimeMillis();
		
		CooccFkt.swap(numberOfSwaps, edges, adjMSec, generator_edges);
		
		long t2 = System.currentTimeMillis();
		
		long t3 = t2-t1;
		
		System.out.println("Swap "+numberOfSwaps+" steps takes "+(double)t3/1000+" seconds");
		
		
	}
	


	public static void main(String[] args) {

		BipartiteGraph bG = new BipartiteGraph();
		
		System.out.println(bG.numberOfEdges);
		
		int numberOfSwaps = (int)Math.log(bG.numberOfEdges)*bG.numberOfSamples;
		
		System.out.println("Swap steps is "+numberOfSwaps);
		
	}

}
