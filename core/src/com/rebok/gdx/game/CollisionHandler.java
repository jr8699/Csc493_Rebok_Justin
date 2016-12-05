package com.rebok.gdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.ObjectMap;
import com.rebok.gdx.game.objects.AbstractGameObject;
import com.rebok.gdx.game.objects.GoldCoin;
import com.rebok.gdx.game.objects.Rock;
import com.rebok.gdx.game.objects.WaterPlayer;
import com.rebok.gdx.game.util.AudioManager;

/**
 * Implement Box2D physics. Watches for collisions
 * @author Justin
 * @author Dr.Girard
 */
public class CollisionHandler implements ContactListener {
    private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners; //our list of objects w/ box2d physics
    private WorldController world; //the world

    //Constructor
    public CollisionHandler(WorldController w)
    {
    	world = w;
        listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
    }

    /**
     * Add an object to the listener list
     * @param categoryA
     * @param categoryB
     * @param listener
     */
    public void addListener(short categoryA, short categoryB, ContactListener listener)
    {
        addListenerInternal(categoryA, categoryB, listener);
        addListenerInternal(categoryB, categoryA, listener);
    }

    /**
     * Whenever 2 bodies have collided, handle this event
     */
    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.beginContact(contact);
        }
    }

    /**
     * Whenever contact has been found to have ended
     */
    @Override
    public void endContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        processContact(contact);

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.endContact(contact);
        }
    }

    /**
     * Interaction between contact and manifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.preSolve(contact, oldManifold);
        }
    }

    /**
     * Calculate forces (impulse)
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.postSolve(contact, impulse);
        }
    }

    /**
     * Add a listener internally
     * @param categoryA
     * @param categoryB
     * @param listener
     */
    private void addListenerInternal(short categoryA, short categoryB, ContactListener listener)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
        if (listenerCollection == null)
        {
            listenerCollection = new ObjectMap<Short, ContactListener>();
            listeners.put(categoryA, listenerCollection);
        }
        listenerCollection.put(categoryB, listener);
    }

    /**
     * Grab a listener from the listener collection
     * @param categoryA
     * @param categoryB
     * @return
     */
    private ContactListener getListener(short categoryA, short categoryB)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
        if (listenerCollection == null)
        {
            return null;
        }
        return listenerCollection.get(categoryB);
    }

    /**
     * Process interactions between two fixtures
     * @param contact
     */
    private void processContact(Contact contact)
    {
    	Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        AbstractGameObject objA = (AbstractGameObject)fixtureA.getBody().getUserData();
        AbstractGameObject objB = (AbstractGameObject)fixtureB.getBody().getUserData();

        if (objA instanceof WaterPlayer)
        {
        	processPlayerContact(fixtureA, fixtureB);
        }
        else if (objB instanceof WaterPlayer)
        {
        	processPlayerContact(fixtureB, fixtureA);
        }
    }

    /**
     * Process interactions between a player and objects
     * @param playerFixture
     * @param objFixture
     */
    private void processPlayerContact(Fixture playerFixture, Fixture objFixture)
    {
    	if (objFixture.getBody().getUserData() instanceof Rock)
    	{
    		System.out.println("Collision");
    		WaterPlayer player = (WaterPlayer)playerFixture.getBody().getUserData();
    	    playerFixture.getBody().setLinearVelocity(player.velocity);
    	}
    	else if (objFixture.getBody().getUserData() instanceof GoldCoin)
    	{
    		// Remove the block update the player's score by 1.
    		world.score++;
    		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
    		AudioManager.instance.play(Assets.instance.sounds.jump);
    		AudioManager.instance.play(Assets.instance.sounds.liveLost);

    		Body block = objFixture.getBody();
    		((GoldCoin)block.getUserData()).toRemove = true; //for removal
    		world.flagForRemoval(block);
    	}
    }
}
