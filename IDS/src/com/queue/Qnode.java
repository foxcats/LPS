package com.queue;

import com.data.Uri;

public class Qnode {
	
	private Uri data;
	private Qnode nextNode;
	
	public Qnode(Uri data)
	{
		this.data=data;
		this.setNextNode(null);
	}

	public Qnode getNextNode()
	{
		return nextNode;
	}
	
	public Uri getData()
	{
		return this.data;
	}
	
	public void setNextNode(Qnode nextNode)
	{
		this.nextNode=nextNode;
	}
}
