package com.phoenix.runescape;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents a coordinate position on the global palate.
 */
public final class Coordinate {

	/**
	 * The X position of the coordinate.
	 */
	private transient int x;

	/**
	 * The Y position of the coordinate.
	 */
	private transient int y;

	/**
	 * The height plane of the coordinate.
	 */
	private transient int plane;

	/**
	 * The default class constructor.
	 * 
	 * @param x The X position of the coordinate.
	 * 
	 * @param y The Y position of the coordinate.
	 * 
	 * @param plane the height plane of the coordinate.
	 */
	public Coordinate(int x, int y, int plane) {
		
		this.x = x;
		
		this.y = y;
		
		this.plane = plane;
	}

	/**
	 * Returns the X position of the coordinate.
	 * 
	 * @return The returned position.
	 */
	public final int getX() {

		return x;
	}

	/**
	 * Returns the Y position of the coordinate.
	 * 
	 * @return The returned position.
	 */
	public final int getY() {

		return y;
	}

	/**
	 * Returns the plane of the coordinate.
	 * 
	 * @return The returned plane.
	 */
	public final int getPlane() {

		return plane;
	}

	/**
	 * Modifies value of the coordinate position.
	 * 
	 * @param other The new modification.
	 */
	public final void setPosition(Coordinate other) {
		
		this.x = other.getX();
		
		this.y = other.getY();
		
		this.plane = other.getPlane();
	}

	/**
	 * Performs a check to see if the specified coordinate is equal to
	 * the instanced one.
	 * 
	 * @param other The other coordinate to be checked.
	 * 
	 * @return The result of the operation.
	 */
	public final boolean coordinatesEqual(Coordinate other) {

		return other.getX() == this.getX() && other.getY() == this.getY() && other.getPlane() == this.getPlane();
	}

	/**
	 * Returns the regional X position.
	 * 
	 * @return The returned position.
	 */
	public final int getRegionalX() {

		return (getX() >> 3) - 6;
	}

	/**
	 * Returns the regional Y position.
	 * 
	 * @return The returned position.
	 */
	public final int getRegionalY() {

		return (getY() >> 3) - 6;
	}

	/**
	 * Returns the local X position.
	 * 
	 * @param position The position base.
	 * 
	 * @return The result.
	 */
	public final int getLocalX(Coordinate position) {

		return getX() - 8 * position.getRegionalX();
	}

	/**
	 * Returns the local Y position.
	 * 
	 * @param position The position base.
	 * 
	 * @return The result.
	 */
	public final int getLocalY(Coordinate position) {

		return getY() - 8 * position.getRegionalY();
	}

	/**
	 * Adds an additional set of values onto the current position.
	 * 
	 * @param x The X coordinate value.
	 * 
	 * @param y The Y coordinate value.
	 * 
	 * @param plane The height plane value.
	 */
	public final void setPositionAdditional(int x, int y, int plane) {
		
		this.x += x;
		
		this.y += y;
		
		this.plane += plane;
	}

	/**
	 * Checks if a specified position is within the range of another.
	 * 
	 * @param position The position for comparison.
	 * 
	 * @param radius The radius of the check.
	 * 
	 * @return The result of the comparison.
	 */
	public final boolean isWithinDistance(Coordinate position, int distance) {

		if (this.getPlane() != position.getPlane()) {

			return false;
		}

		return Math.abs(position.getX() - this.getX()) <= distance && Math.abs(position.getY() - this.getY()) <= distance;
	}
}