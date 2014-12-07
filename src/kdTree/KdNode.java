package kdTree;

import nearestNeighbor.Node;

public class KdNode {
	private KdNode left;
	private KdNode right;
	private Node val;
	private double split_direction;
	
	public KdNode(Node n, KdNode left, KdNode right, double split) {
		this.val = n ;
		this.left = left;
		this.right = right;
		this.split_direction = split;
	}
}
