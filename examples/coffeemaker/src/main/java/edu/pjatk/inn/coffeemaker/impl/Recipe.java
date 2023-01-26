package edu.pjatk.inn.coffeemaker.impl;

import sorcer.core.context.ServiceContext;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Class representing a recipe for a coffee drink.
 * Contains information about the name, price, and ingredients (coffee, milk, sugar, chocolate) of the recipe.
 * Implements Serializable interface to allow for storage and retrieval of recipe objects.
 * @author   Sarah and Mike
 */
public class Recipe implements Serializable {
    private String name;
    private int price;
    private int amtCoffee;
    private int amtMilk;
    private int amtSugar;
    private int amtChocolate;

	/**
	 * Create new Recipe object
	 * Initializes all fields to empty or zero values.
	 */
	public Recipe() {
		/** Name of the recipe */
    	this.name = "";
		/** Price of beverage */
    	this.price = 0;
		/** Amount of coffee in the recipe */
    	this.amtCoffee = 0;
		/** Amount of milk in the recipe */
    	this.amtMilk = 0;
		/** Amount of sugar in the recipe */
    	this.amtSugar = 0;
		/** Amount of chocolate in the recipe */
    	this.amtChocolate = 0;
    }
    
    /**
	 * Getter method for the amount of chocolate in the recipe.
	 * @return amtChocolate (the amount of chocolate in the recipe)
	 */
    public int getAmtChocolate() {
		return amtChocolate;
	}

    /**
	 * Setter method for the amount of chocolate in the recipe.
	 * AmtChocolate default value is 0
	 * Sets amtChocolate, if provided value is integer and greater equal 0.
	 *
	 * @param amtChocolate   The amtChocolate to setValue.
	 */
    public void setAmtChocolate(int amtChocolate) {
		if (amtChocolate >= 0) {
			this.amtChocolate = amtChocolate;
		} 
	}
    /**
	 * Getter method for the amount of coffee in the recipe.
	 * @return amtCoffee (the amount of coffee in the recipe)
	 */
    public int getAmtCoffee() {
		return amtCoffee;
	}

	/**
	 * Setter method for the amount of coffee in the recipe.
	 * AmtCoffee default value is 0
	 * Sets amtCoffee, if provided value is integer and greater equal 0.
	 *
	 * @param amtCoffee   The amtChocolate to setValue.
	 */
    public void setAmtCoffee(int amtCoffee) {
		if (amtCoffee >= 0) {
			this.amtCoffee = amtCoffee;
		} 
	}
	/**
	 * Getter method for the amount of milk in the recipe.
	 * @return amtMilk (the amount of milk in the recipe)
	 */
    public int getAmtMilk() {
		return amtMilk;
	}
	/**
	 * Setter method for the amount of milk in the recipe.
	 * AmtMilk default value is 0
	 * Sets amtMilk, if provided value is integer and greater equal 0.
	 *
	 * @param amtMilk   The amtChocolate to setValue.
	 */
    public void setAmtMilk(int amtMilk) {
		if (amtMilk >= 0) {
			this.amtMilk = amtMilk;
		} 
	}
    /**
	 * Getter method for the amount of sugar in the recipe.
	 * @return  amtSugar (the amount of sugar in the recipe)
	 */
    public int getAmtSugar() {
		return amtSugar;
	}
	/**
	 * Setter method for the amount of sugar in the recipe.
	 * AmtSugar default value is 0
	 * Sets amtSugar, if provided value is integer and greater equal 0.
	 *
	 * @param amtSugar   The amtChocolate to setValue.
	 */
    public void setAmtSugar(int amtSugar) {
		if (amtSugar >= 0) {
			this.amtSugar = amtSugar;
		} 
	}
    /**
	 * Getter method for name of recipe.
	 * @return   Returns the key.
	 */
    public String getName() {
		return name;
	}
    /**
	 * Setter method for name of recipe.
	 * Name default value is empty
	 * Sets name, if provided value is string and is not NULL.
	 *
	 * @param name   The key to setValue.
	 */
    public void setName(String name) {
    	if(name != null) {
    		this.name = name;
    	}
	}
    /**
	 * Getter method for beverage price.
	 * @return   Returns the price.
	 */
    public int getPrice() {
		return price;
	}
    /**
	 * Setter method for beverage price.
	 * Price default value is 0
	 * Sets price, if provided value is integer and greater equal 0.
	 *
	 * @param price   The price to setValue.
	 */
    public void setPrice(int price) {
		if (price >= 0) {
			this.price = price;
		} 
	}

	/**
	 * Compares recipe name with given other recipe
	 *
	 * @param r (Recipe to compare to)
	 * @return boolean result for comparison
	 */
    public boolean equals(Recipe r) {
        if((this.name).equals(r.getName())) {
            return true;
        }
        return false;
    }

	/**
	 * Get a string name of the Recipe.
	 *
	 * @return Returns name of Recipe
	 */
    public String toString() {
    	return name;
    }

	/**
	 * Create new Recipe object based on Context object
	 * Passes all values of Context to it
	 *
	 * @param context (Context object to take values from to create new Recipe)
	 * @return new Recipe object which contains all Context values
	 * @throws ContextException thrown if Context failing to process correctly due to incorrect context paths, values, or associations.
	 */
	static public Recipe getRecipe(Context context) throws ContextException {
		Recipe r = new Recipe();
		try {
			r.name = (String)context.getValue("key");
			r.price = (int)context.getValue("price");
			r.amtCoffee = (int)context.getValue("amtCoffee");
			r.amtMilk = (int)context.getValue("amtMilk");
			r.amtSugar = (int)context.getValue("amtSugar");
			r.amtChocolate = (int)context.getValue("amtChocolate");
		} catch (RemoteException e) {
			throw new ContextException(e);
		}
		return r;
	}

	/** Create new ServiceContext object based on Recipe object
	 *  Passes all values of recipe to it
	 *
	 * @param recipe (Recipe object to take values from)
	 * @return new ServiceContext object which contains all Recipe values
	 * @throws ContextException thrown if Context failing to process correctly due to incorrect context paths, values, or associations.
	 */
	static public Context getContext(Recipe recipe) throws ContextException {
		Context cxt = new ServiceContext();
		cxt.putValue("key", recipe.getName());
		cxt.putValue("price", recipe.getPrice());
		cxt.putValue("amtCoffee", recipe.getAmtCoffee());
		cxt.putValue("amtMilk", recipe.getAmtMilk());
		cxt.putValue("amtSugar", recipe.getAmtSugar());
		cxt.putValue("amtChocolate", recipe.getAmtChocolate());
		return cxt;
	}


}
