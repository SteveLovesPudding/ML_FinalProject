package kdTree;
import java.util.Comparator;


public class MyComparator<T extends Point> implements Comparator<Point>{
	int axis;
	public MyComparator(int axis) {
		this.axis = axis;
	}
	@Override
	public int compare(Point o1, Point o2) {
		
		double[] first = o1.getConfigs();
		double[] second = o2.getConfigs();
		if (first[axis] < second[axis])
			return -1;
		if (first[axis] > second[axis])
			return 1;
		return 0;
	}
}
