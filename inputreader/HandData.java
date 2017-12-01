package inputreader;

public class HandData{
	
	private double palm_Position_X;
	
	private double palm_Position_Y;
	
	private double palm_Position_Z;
	
	private int valid;
	
	private double thumb;
		
	private double spread;
	
	public HandData(double palm_Position_X, double palm_Position_Y, double palm_Position_Z, int valid, double thumb, double spread) {
		this.palm_Position_X = palm_Position_X;
		this.palm_Position_Y = palm_Position_Y;
		this.palm_Position_Z = palm_Position_Z;
		this.valid = valid;
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

	public int getValid() {
		return valid;
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