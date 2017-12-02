package inputreader;

/**
 * Class to hold raw data about the positioning of a hand. 
 * An object of this class represents a single point of measurement or one line in the corresponding csv-file
 * @author Ben Fürnrohr
 */
public class HandData{
	
	/** position of the hand-x*/
	private double palm_Position_X;
	
	/** position of the hand-<*/
	private double palm_Position_Y;
	
	/** position of the hand-z*/
	private double palm_Position_Z;
	
	/** spread-angle of the thumb */
	private double thumb;
		
	/** spread-angle of the hand */
	private double spread;
	
	public HandData(double palm_Position_X, double palm_Position_Y, double palm_Position_Z, double thumb, double spread) {
		this.palm_Position_X = palm_Position_X;
		this.palm_Position_Y = palm_Position_Y;
		this.palm_Position_Z = palm_Position_Z;
		this.thumb = thumb;
		this.spread = spread;
	}
	

	public double getPalm_Position_X() {
		return palm_Position_X;
	}

	public double getPalm_Position_Y() {
		return palm_Position_Y;
	}

	public double getPalm_Position_Z() {
		return palm_Position_Z;
	}

	public double getThumb() {
		return thumb;
	}

	public double getSpread() {
		return spread;
	}	
	
	public String toString() {
		return "X: "+ this.palm_Position_X + " Y: "+ this.palm_Position_Y + " Z: "+ this.palm_Position_Z;
	}
	
}