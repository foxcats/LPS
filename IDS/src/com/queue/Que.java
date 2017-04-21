package com.queue;

import com.data.Uri;

public class Que {
	
	public static int qSize;
	private Qnode head;
	private Qnode tail;
	public Que()
	{
		this.head=null;
		this.tail=null;
		qSize=0;
	}

	public boolean isEmpty()
	{
		
		return(tail==null);
	}
	
	
	public void enQueue(Uri data)
	{
		Qnode newNode=new Qnode(data);
		
		
		if(isEmpty())
		{
			this.head=newNode;
			this.tail=newNode;
		}
		else
		{
			this.tail.setNextNode(newNode);
			this.tail=newNode;
		}
		this.qSize++;
	}
	
	
	public Qnode deQueue()
	{
		Qnode deQueNode;
		if(isEmpty())
		{
			return null;
		}
		else if(head==tail)
		{
			deQueNode=this.head;
			head=null;
			tail=null;
			this.qSize--;
			return deQueNode;
		}
		else
		{
			deQueNode=this.head;
			this.head=this.head.getNextNode();
			this.qSize--;
			return deQueNode;
		}
		
		
	}
}
