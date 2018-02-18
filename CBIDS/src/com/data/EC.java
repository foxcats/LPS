package com.data;

public class EC {
	
	/**
	 * Entity Identical Confidence Class
	 */
	
	private int count;
	private double ec;
	
	public EC(int count,double ec)
	{
		this.count=count;
		this.ec=ec;
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getEc() {
		return ec;
	}

	public void setEc(double ec) {
		this.ec = ec;
	}

	
	
}
