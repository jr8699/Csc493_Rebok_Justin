package com.rebok.gdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class for our game objects
 * @author Justin
 *
 */
public abstract class AbstractGameObject {
    public Vector2 position; //Postition of the object
    public Vector2 dimension; //Dimension of the object
    public Vector2 origin; //Origin of the object
    public Vector2 scale; //Object's scale
    public float rotation; //Objects rotation
    
    //Constructor
    public AbstractGameObject () {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
    }
    
    /**
     * Update method for objects, called by our world controller
     * @param deltaTime
     */
    public void update (float deltaTime) {
    }
    
    /**
     * Render method for objects, called by our world renderer
     * @param batch
     */
    public abstract void render (SpriteBatch batch);
}
