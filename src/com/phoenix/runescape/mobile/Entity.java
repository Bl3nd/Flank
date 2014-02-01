package com.phoenix.runescape.mobile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.Coordinate;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents a primitive RuneScape style entity.
 */
public abstract class Entity {
	
	/**
	 * The numerical index of the entity.
	 */
	private transient int index;
	
	/**
	 * The movement directions of the entity.
	 */
	private int walkingDirection = -1, runningDirection = -1;
	
	/**
	 * Handles the movement procedures of an entity.
	 */
	private final Movement movement = new Movement(this);
	
	/**
	 * The entity updating procedure.
	 * 
	 * @return The super type.
	 */
	public abstract EntityUpdateListener<? extends Entity> getUpdateListener();
	
	/**
	 * Handles the removal of an entity from the virtual world.
	 */
	public abstract void remove();
	
	/**
	 * The position of the entity on the coordinate plane.
	 */
	private Coordinate position = new Coordinate(Constants.STARTING_X, Constants.STARTING_Y, 0), lastPosition = new Coordinate(0, 0, 0);
	
	/**
	 * The temporary attributes held by the entity.
	 */
	private final Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * The update flags held by the entity.
	 */
	private final List<String> updateFlags = new LinkedList<String>();

	/**
	 * Returns the numerical index of the entity.
	 * 
	 * @return The returned index.
	 */
	public int getIndex() {
		
		return index;
	}

	/**
	 * Modifies the numerical index of the entity.
	 * 
	 * @param index The new modification.
	 */
	public final void setIndex(int index) {
		
		this.index = index;
	}

	/**
	 * Returns the position of the entity on the coordinate plane.
	 * 
	 * @return The returned position.
	 */
	public final Coordinate getPosition() {
		
		return position;
	}

	/**
	 * Modifies the position of the entity on the coordinate plane.
	 * 
	 * @return The new modification.
	 */
	public final void setPosition(Coordinate position) {
		
		this.position = position;
	}
	
	/**
	 * Puts a new attribute into an entities collection
	 * 
	 * @param attribute The attribute to be added.
	 */
	public final void putAttribute(String attribute) {
		
		attributes.put(attribute, this);
	}
	
	/**
	 * Removes an attribute from an entities collection.
	 * 
	 * @param attribute The attribute to be removed.
	 */
	public final void removeAttribute(String attribute) {
		
		attributes.remove(attribute);
	}
	
	/**
	 * If an entity has a specific attribute.
	 * 
	 * @param attribute The attribute of the entity.
	 * 
	 * @return The result of the operation.
	 */
	public final boolean hasAttribute(String attribute) {
		
		return attributes.containsKey(attribute);
	}
	
	/**
	 * Returns the value of an attribute in the entity's collection in
	 * Boolean primitive.
	 * 
	 * @param attribute The attribute of interest.
	 * 
	 * @return The value of the attribute.
	 */
	public final boolean getAttributeAsBoolean(String attribute) {
		
		return attributes.get(attribute).equals(true);
	}
	
	/**
	 * Adds a new attribute to the entity's attribute collection.
	 * 
	 * @param attribute The attribute to be added.
	 * 
	 * @param modifier The modifier of the attribute.
	 */
	public final void putAttribute(String attribute, Object modifier) {
		
		attributes.put(attribute, modifier);
	}
	
	/**
	 * Returns the value of an attribute in the entity's collection in
	 * Integer primitive.
	 * 
	 * @param attribute The attribute of interest.
	 * 
	 * @return The value of the attribute.
	 */
	public final int getAttributeAsInteger(String attribute) {
		
		return (int) attributes.get(attribute);
	}
	
	/**
	 * Returns the value of an attribute in the entity's collection in
	 * Long primitive.
	 * 
	 * @param attribute The attribute of interest.
	 * 
	 * @return The value of the attribute.
	 */
	public final long getAttributeAsLong(String attribute) {
		
		return (long) attributes.get(attribute);
	}

	/**
	 * Returns the last position of the entity.
	 * 
	 * @return The returned position.
	 */
	public final Coordinate getLastPosition() {
		
		return lastPosition;
	}

	/**
	 * Modifies the last position of the entity.
	 * 
	 * @param lastPosition The new modification.
	 */
	public final void setLastPosition(Coordinate lastPosition) {
		
		this.lastPosition = lastPosition;
	}

	/**
	 * Returns the update flags possessed by the entity.
	 * 
	 * @return The returned update flags.
	 */
	public final List<String> getUpdateFlags() {
		
		return updateFlags;
	}

	/**
	 * Returns the walking direction of the entity.
	 * 
	 * @return The returned walking direction.
	 */
	public final int getWalkingDirection() {
		
		return walkingDirection;
	}

	/**
	 * Modifies the walking direction of the entity.
	 * 
	 * @param walkingDirection The new modification.
	 */
	public final void setWalkingDirection(int walkingDirection) {
		
		this.walkingDirection = walkingDirection;
	}

	/**
	 * Returns the running direction of the entity.
	 * 
	 * @return The returned running direction.
	 */
	public final int getRunningDirection() {
		
		return runningDirection;
	}

	/**
	 * Modifies the running direction of the entity.
	 * 
	 * @param runningDirection The new modification.
	 */
	public final void setRunningDirection(int runningDirection) {
		
		this.runningDirection = runningDirection;
	}
	
	/**
	 * Checks if an update is required.
	 * 
	 * @return The result of the check.
	 */
	public final boolean isUpdateRequired() {
		
		return !getUpdateFlags().isEmpty();
	}

	/**
	 * Returns the instance for entity movement control.
	 * 
	 * @return The returned instance.
	 */
	public final Movement getMovement() {
		
		return movement;
	}
	
	/**
	 * Performs an animation.
	 * 
	 * @param identity The identity of the animation.
	 * 
	 * @param delay The delay of the animation.
	 */
	public final void performAnimation(int identity, int delay) {
		attributes.put("animation", identity);
		attributes.put("animation-delay", delay);
		updateFlags.add("animation");
	}
	
	/**
	 * Executes a graphic.
	 * 
	 * @param identity The identity of the graphic.
	 * 
	 * @param delay The delay of the graphic.
	 * 
	 * @param height The height of the graphic (100 = body - 200 = head).
	 */
	public final void executeGraphic(int identity, int delay, int height) {
		attributes.put("graphic", identity);
		attributes.put("graphic-delay", delay + (65536 * height));
		updateFlags.add("graphics");
	}
}