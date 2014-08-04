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
	
	public static void run(int numberOfSwaps, String inputFile){
		
		BipartiteGraph bG = new BipartiteGraph(inputFile);
		
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
		String inputFile = Setting.outputRoot+"Netflix_Dataset_Good_10k_converted";

		BipartiteGraph bG = new BipartiteGraph(inputFile);
		
		System.out.println(bG.numberOfEdges);
		
		int numberOfSwaps = (int) (Math.log(bG.numberOfEdges)*bG.numberOfEdges);
		
		System.out.println("Swap steps is "+numberOfSwaps);
		
		
		run(numberOfSwaps, inputFile);
		
	}

}
