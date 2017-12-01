package inputreader;

/**
 * Class to hold all the information extracted from a set of data that was meant for calibration
 * This includes values like the angle that is supposed to represent a resting/spread hand/thumb
 * @author Ben Fürnrohr
 */
public class CalibrationDataset{
	
	/** Degrees indicating a resting Thumb **/
	double avgThumbRest;
	
	/** Degrees indicating a resting Hand **/
	double avgHandRest;
	
	/** Degrees indicating a spreading of the Thumb **/
	double avgThumbSpread;
	
	/** Degrees indicating a spreading of the Thumb **/
	double avgHandSpread;

	/**
	 * Constructor
	 * @param avgThumbRest average degree of a resting thumb
	 * @param avgHandRest average degree of a resting hand
	 * @param avgThumbSpread average degree of a spread thumb
	 * @param avgHandSpread average degree of a spread hand
	 */
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