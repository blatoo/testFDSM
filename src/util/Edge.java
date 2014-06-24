package util;

public class Edge {
	
	int node1 = 0;
	int node2 = 0;
	
	public Edge(int node1, int node2){
		this.node1 = node1;
		this.node2 = node2;
		
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + node1;
		result = prime * result + node2;
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (node1 != other.node1)
			return false;
		if (node2 != other.node2)
			return false;
		return true;
	}

}
