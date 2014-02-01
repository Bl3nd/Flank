package com.phoenix.runescape.object;

import com.phoenix.runescape.Coordinate;

/**
 * @author Dylan Vicchiarelli
 * 
 * Represents an object viewed by members of the virtual world.
 */
public final class GameObject {
	
	/**
	 * The index of the object.
	 */
	private final int index;
	
	/**
	 * The coordinates of the object.
	 */
	private final Coordinate coordinate;
	
	/**
	 * The index type of the object.
	 */
	private final int indexType;
	
	/**
	 * The direction the object is facing.
	 */
	private final int face;
	
	/**
	 * The default class constructor.
	 * 
	 * @param index The index of the object.
	 * 
	 * @param coordinate The coordinates of the object.
	 * 
	 * @param indexType The index type of the object.
	 * 
	 * @param face The direction the object is facing.
	 */
	public GameObject(int index, Coordinate coordinate, int indexType, int face) {
		this.index = index;
		this.coordinate = coordinate;
		this.indexType = indexType;
		this.face = face;
	}

	/**
	 * Returns the direction the object is facing.
	 * 
	 * @return The returned direction.
	 */
	public final int getFace() {
		
		return face;
	}

	/**
	 * Returns the index type of the object.
	 * 
	 * @return The returned index type.
	 */
	public final int getIndexType() {
		
		return indexType;
	}

	/**
	 * Returns the coordinates of the object.
	 * 
	 * @return The returned coordinates.
	 */
	public final Coordinate getCoordinate() {
		
		return coordinate;
	}

	/**
	 * Returns the index of the object.
	 * 
	 * @return The returned index.
	 */
	public final int getIndex() {
		
		return index;
	}
}