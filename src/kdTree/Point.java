package kdTree;

public class Point extends KdTree.XYZPoint{

	double[] configs; 
	public Point(double[] configs){
		//super(configs[0],configs[1],configs[2]);
		//super();
		this.configs = configs; 
	}
	
	public Point(double x, double y, double z){
		super(x,y,z);
	}
	
	public double[] getConfigs(){
		return this.configs; 
	}
	
	@Override
	public double euclideanDistance(KdTree.XYZPoint point){
		if(point instanceof Point){
			double[] ptConfigs = ((Point) point).getConfigs();
			double sum = 0; 
			for(int i=0; i < configs.length; i++){
				sum += Math.pow((this.configs[i] - ptConfigs[i]), 2);
			}
			return Math.sqrt(sum);
		}else 
			return -1; 
	}
	
	//TODO
	/*
	private static final double euclideanDistance(XYZPoint o1, XYZPoint o2) {
		return Math.sqrt(Math.pow((o1.x - o2.x), 2)
				+ Math.pow((o1.y - o2.y), 2) + Math.pow((o1.z - o2.z), 2));
	};
	*/
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Point))
			return false;

		Point xyzPoint = (Point) obj;
		return compareTo(xyzPoint) == 0;
	}
	
	@Override
	public int compareTo(KdTree.XYZPoint o) {
		if(o instanceof Point){
			double[] ptConfig = ((Point) o).getConfigs();
			for(int i=0; i<ptConfig.length; i++){
				if(this.configs[i] > ptConfig[i]) return 1; 
				if(this.configs[i] < ptConfig[i]) return -1; 
			}
			
			return 0; 
		}
		return -1; 
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for(int i=0; i<configs.length; i++) builder.append(configs[i]).append(", "); 
		builder.append(")");
		return builder.toString();
	}
}