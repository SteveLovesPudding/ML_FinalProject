package robotArm;

import java.util.Comparator;


public class Position implements Comparable<Position> {

	/**
	 * {x,y,z}
	 */
	public double[] coordinates = new double[3];

	public Position(double[] coordinates) {
		this.coordinates = coordinates;
	}

	public Position(double x, double y, double z) {
		this.coordinates = new double[] { x, y, z };
	}

	public Position() {

	}

	public double[] getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	public String toString() {
		return "" + coordinates[0] + "  " + coordinates[1] + "  "
				+ coordinates[2] + "";
	}
	
    /**
     * Computes the Euclidean distance from this point to the other.
     * 
     * @param value
     *            other point.
     * @return euclidean distance.
     */
    public double euclideanDistance(Position value) {
        return euclideanDistance(value, this);
    }
    
    /**
     * Computes the Euclidean distance from one point to the other.
     * 
     * @param o1
     *            first point.
     * @param o2
     *            second point.
     * @return euclidean distance.
     */
    private static final double euclideanDistance(Position o1, Position o2) {
    
        return Math.sqrt(Math.pow((o1.coordinates[0] - o2.coordinates[0]), 2) + Math.pow((o1.coordinates[1] - o2.coordinates[1]), 2) + Math.pow((o1.coordinates[2] - o2.coordinates[2]), 2));
    };
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Position))
            return false;

        Position xyzPoint = (Position) obj;
        return compareTo(xyzPoint) == 0;
    }
    
    @Override
    public int compareTo(Position o) {
        int xComp = X_COMPARATOR.compare(this, o);
        if (xComp != 0)
            return xComp;
        int yComp = Y_COMPARATOR.compare(this, o);
        if (yComp != 0)
            return yComp;
        int zComp = Z_COMPARATOR.compare(this, o);
        return zComp;
    }
    
    public final static Comparator<Position> X_COMPARATOR = new Comparator<Position>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(Position o1, Position o2) {
            if (o1.coordinates[0] < o2.coordinates[0])
                return -1;
            if (o1.coordinates[0] > o2.coordinates[0])
                return 1;
            return 0;
        }
    };

    public final static Comparator<Position> Y_COMPARATOR = new Comparator<Position>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(Position o1, Position o2) {
            if (o1.coordinates[1] < o2.coordinates[1])
                return -1;
            if (o1.coordinates[1] > o2.coordinates[1])
                return 1;
            return 0;
        }
    };

    public final static Comparator<Position> Z_COMPARATOR = new Comparator<Position>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(Position o1, Position o2) {
            if (o1.coordinates[2] < o2.coordinates[2])
                return -1;
            if (o1.coordinates[2] > o2.coordinates[2])
                return 1;
            return 0;
        }
    };

}
