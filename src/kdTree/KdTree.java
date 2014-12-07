package kdTree;

public class KdTree {
    private KdNode left;
    private KdNode right;
    private KdNode root;
    
    public KdTree(KdNode l, KdNode r) {
    	this.root = null;
        this.left = l;
        this.right = r;
    }
    
    public void addToTree(KdNode toAdd) {
    	if (this.root == null) {// just add
    		this.root = toAdd;
    		return;
    	}		
    	
    	
    }
}