package classification;

import java.util.List;

import inputreader.HandData;

public interface Classifyable {

	public Classification_Result classify();
	
	public void setSessionData(List<HandData> sessionData);
}
