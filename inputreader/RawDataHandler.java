package inputreader;

import java.util.ArrayList;
import java.util.List;

public class RawDataHandler{

	private List<HandData> dataFromCompleteRun;
	private List<Double> thumbSpreadData;
	private List<Double> handSpreadData;
		
	public void setDataFromCompleteRun(List<HandData> data) {
		this.dataFromCompleteRun = data;
		List<Double> thumbDataList = new ArrayList<Double>();
		for(HandData hd: dataFromCompleteRun) {
			thumbDataList.add(hd.getThumb());
		}
		this.thumbSpreadData = thumbDataList;
		
		List<Double> handDataList = new ArrayList<Double>();
		for(HandData hd: dataFromCompleteRun) {
			handDataList.add(hd.getSpread());
		}
		this.handSpreadData = handDataList;
	}
	
	public double calculateThumbSpreadAvg() {		
		double[] maxima = this.find3Maxima(this.thumbSpreadData);
		return (maxima[0]+maxima[1]+maxima[2])/3;		
	}
	
	public double calculateHandSpreadAvg() {		
		double[] maxima = this.find3Maxima(this.handSpreadData);
		return (maxima[0]+maxima[1]+maxima[2])/3;
	}
	
	public double calculateThumbRestAvg() {
		return this.avgFromData(this.thumbSpreadData);
	}
	
	public double calculateHandRestAvg() {
		return this.avgFromData(this.handSpreadData);
	}
	
	private double avgFromData(List<Double> data) {
		double sum = 0;
		for (Double d : data) {
			sum = sum + d;
		}
		return sum/data.size();
	}

	private double[] find3Maxima(List<Double> dataList) {
		double highest = 0;
		double second = 0;
		double third = 0;
		double current = 0.0;
		boolean rising = true;
		
		//find a maximum
		for(Double dataPoint : dataList) {		
			if (rising) {
				if (dataPoint < current) {
					//local Maximum found! Check for global
					rising = false;
					if (dataPoint > highest) {
						third = second;
						second = highest;
						highest = dataPoint;
					}
					else if (dataPoint > second) {
						third = second;
						second = dataPoint;
					}
					else if (dataPoint > third) {
						third = dataPoint;
					}
				}			
			}
			else {
				//value falling
				if (dataPoint > current) {
					//local Minimum found
					rising = true;
				}			
			}
			current = dataPoint;
		}
		double[] retArray = {highest, second, third};		
		return retArray;
	}

	public CalibrationDataset generateCalibrationDataset() {
		return new CalibrationDataset(this.calculateThumbRestAvg(), this.calculateHandRestAvg(), this.calculateThumbSpreadAvg(), this.calculateHandSpreadAvg());		
	}
	
}