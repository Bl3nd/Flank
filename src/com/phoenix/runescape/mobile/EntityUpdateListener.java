package com.phoenix.runescape.mobile;

/**
 * @author Dylan Vicchiarelli
 *
 * Handles the secular updating of entities in the virtual world.
 * 
 * @param <T> The entity that is being updated.
 */
public abstract class EntityUpdateListener<T extends Entity> {
	
	/**
	 * Performs the updating procedure for an entity.
	 * 
	 * @param entity The entity being updated.
	 */
	public abstract void update(T entity);
}