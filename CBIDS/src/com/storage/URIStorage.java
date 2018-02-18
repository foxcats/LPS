package com.storage;

import java.util.Hashtable;

import com.data.Entity;

public class URIStorage {
	
	private Hashtable<String, Entity> uriStorage;
	
	public URIStorage()
	{
		this.uriStorage=new Hashtable<String,Entity>();
	}
	

	public Entity getEntity(String uri)
	{
		return this.uriStorage.get(uri);
	}
	
	public void putEntity(String uri,Entity entity)
	{
		this.uriStorage.put(uri, entity);
	}


	public Hashtable<String, Entity> getUriStorage() {
		return uriStorage;
	}


	public void setUriStorage(Hashtable<String, Entity> uriStorage) {
		this.uriStorage = uriStorage;
	}
	
	
}
