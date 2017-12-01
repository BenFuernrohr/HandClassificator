package inputreader;

public class CalibrationDataset{
	
	/** Degrees indicating a resting Thumb **/
	double avgThumbRest;
	
	/** Degrees indicating a resting Hand **/
	double avgHandRest;
	
	/** Degrees indicating a spreading of the Thumb **/
	double avgThumbSpread;
	
	/** Degrees indicating a spreading of the Thumb **/
	double avgHandSpread;

	public CalibrationDataset(double avgThumbRest, double avgHandRest, double avgThumbSpread, double avgHandSpread) {
		this.avgThumbRest = avgThumbRest;
		this.avgHandRest = avgHandRest;
		this.avgThumbSpread = avgThumbSpread;
		this.avgHandSpread = avgHandSpread;
	}	

	public double getAvgThumbRest() {
		return avgThumbRest;
	}

	public double getAvgHandRest() {
		return avgHandRest;
	}

	public double getAvgThumbSpread() {
		return avgThumbSpread;
	}

	public double getAvgHandSpread() {
		return avgHandSpread;
	}
	
	
	public String toString() {
		return "avgThumbSpread: " + avgThumbSpread + "\navgHandSpread: " + avgHandSpread + "\navgThumbRest: " + avgThumbRest + "\navgHandRest: " + avgHandRest;
	}
}