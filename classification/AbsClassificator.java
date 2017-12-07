package classification;

import java.util.List;

import inputreader.CalibrationDataset;
import inputreader.HandData;

/**
 * Abstract class from which all classificators shall be derived
 * @author Ben Fürnrohr
 */
public abstract class AbsClassificator implements ClassifyableIF{
	
	/** Dataset from calibration, must be set at least once */
	protected CalibrationDataset calibrationDataset;
	
	/** Data of the session in evaluation */
	protected List<HandData> sessionData;
	
	public AbsClassificator(CalibrationDataset calibrationDataset) {
		this.calibrationDataset = calibrationDataset;
	}

	public void setSessionData(List<HandData> sessionData) {
		this.sessionData = sessionData;
	}
	
	
}