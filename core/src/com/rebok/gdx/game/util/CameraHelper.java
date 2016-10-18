package com.rebok.gdx.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rebok.gdx.game.objects.AbstractGameObject;

/**
 * Settings and key functions for operation of the camera
 * @author Justin
 *
 */
/**
 * Settings and key functions for operation of the camera
 * @author Justin
 *
 */
public class CameraHelper {
	private static final String TAG = CameraHelper.class.getName(); //libGDX tag
	private final float MAX_ZOOM_IN = 0.25f; //Maximum zoom in value
	private final float MAX_ZOOM_OUT = 10.0f; //Maximum zoom out value
	private Vector2 position; //Current camera position
	private float zoom; //Current zoom
	private AbstractGameObject target; //Current target
	private final float FOLLOW_SPEED = 4.0f; //follow speed
	
	public CameraHelper() {
		position = new Vector2();
		zoom = 1.0f;
	}
	
	/**
	 * Update the camera's position
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		if (!hasTarget()) return;
	    position.x = target.position.x + target.origin.x;
	    position.y = target.position.y + target.origin.y;
	    position.lerp(target.position, FOLLOW_SPEED * deltaTime);
	    // Prevent camera from moving down too far
	    position.y = Math.max(-1f, position.y);
	}
	
	/**
	 * Set camera position
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
	    this.position.set(x, y);
	}
	
	/**
	 * Get current camera position
	 * @return
	 */
	public Vector2 getPosition() { return position; }
	
	/**
	 * Zoom in by a specified amount
	 * @param amount
	 */
	public void addZoom(float amount) { setZoom(zoom + amount); }
	
	/**
	 * Set the zoom to a specific value
	 * @param zoom
	 */
	public void setZoom(float zoom) {
	    this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	
	/**
	 * Get the current zoom amount
	 * @return
	 */
	public float getZoom() { return zoom; }
	
	/**
	 * Set the camera's target
	 * @param target
	 */
	public void setTarget(AbstractGameObject target) { this.target = target; }
	
	/**
	 * Get the camera's current target
	 * @return
	 */
	public AbstractGameObject getTarget() { return target; }
	
	/**
	 * Does the camera have a current target
	 * @return
	 */
	public boolean hasTarget() { return target != null; }
	
	/**
	 * Ask if a certain target is the current target
	 * @param target
	 * @return
	 */
	public boolean hasTarget(AbstractGameObject target) {
	    return hasTarget() && this.target.equals(target);
	}
	
	/**
	 * Called at the beginning of each frame render to update camera attributes
	 * @param camera
	 */
	public void applyTo(OrthographicCamera camera) {
	    camera.position.x = position.x;
	    camera.position.y = position.y;
	    camera.zoom = zoom;
	    camera.update();
	}
}
