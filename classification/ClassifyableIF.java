package classification;

import java.util.List;

import inputreader.HandData;

/**
 * Interface for the classificators. Implementing classes allow for the setting of session data and for classification
 * @author Ben Fürnrohr
 */
public interface ClassifyableIF {

	/**
	 * Starts the classification of the currently set session data
	 * @return {@link ClassificationResult} representing the result of the classification
	 */
	public ClassificationResult classify();
	
	/**
	 * Sets the data of the session to be classified
	 * @param sessionData the data of the session to be classified
	 */
	public void setSessionData(List<HandData> sessionData);
}
