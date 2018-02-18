package com.stack;

import java.util.ArrayList;

import com.data.Entity;

public class EntityStack implements Stack {

	public ArrayList<Entity> entityList;
	
	
	
	public EntityStack() {
		this.entityList = new ArrayList<Entity>();
	}

	@Override
	public void push(Entity e) {
		// TODO Auto-generated method stub
		
		entityList.add(e);
		
	}

	@Override
	public Entity pop() {
		// TODO Auto-generated method stub
		
		Entity entity=entityList.get(entityList.size()-1);
		if(entity==null)
			return null;
		
		entityList.remove(entityList.size()-1);
		return  entity;
	}

}
