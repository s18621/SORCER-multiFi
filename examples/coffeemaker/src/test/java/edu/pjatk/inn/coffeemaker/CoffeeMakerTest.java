package edu.pjatk.inn.coffeemaker;

import edu.pjatk.inn.coffeemaker.impl.CoffeeMaker;
import edu.pjatk.inn.coffeemaker.impl.Inventory;
import edu.pjatk.inn.coffeemaker.impl.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.service.ContextException;
import sorcer.service.Routine;

import java.util.Arrays;

import static org.junit.Assert.*;
import static sorcer.eo.operator.*;
import static sorcer.so.operator.eval;
import static sorcer.so.operator.exec;

/**
 * @author Mike Sobolewski
 */
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/coffeemaker")
public class CoffeeMakerTest {
	private final static Logger logger = LoggerFactory.getLogger(CoffeeMakerTest.class);

	private CoffeeMaker coffeeMaker;
	private Inventory inventory;
	private Recipe espresso, mocha, macchiato, americano;

	@Before
	public void setUp() throws ContextException {
		coffeeMaker = new CoffeeMaker();
		inventory = coffeeMaker.checkInventory();

		espresso = new Recipe();
		espresso.setName("espresso");
		espresso.setPrice(50);
		espresso.setAmtCoffee(6);
		espresso.setAmtMilk(1);
		espresso.setAmtSugar(1);
		espresso.setAmtChocolate(0);

		mocha = new Recipe();
		mocha.setName("mocha");
		mocha.setPrice(100);
		mocha.setAmtCoffee(8);
		mocha.setAmtMilk(1);
		mocha.setAmtSugar(1);
		mocha.setAmtChocolate(2);

		macchiato = new Recipe();
		macchiato.setName("macchiato");
		macchiato.setPrice(40);
		macchiato.setAmtCoffee(7);
		macchiato.setAmtMilk(1);
		macchiato.setAmtSugar(2);
		macchiato.setAmtChocolate(0);

		americano = new Recipe();
		americano.setName("americano");
		americano.setPrice(40);
		americano.setAmtCoffee(7);
		americano.setAmtMilk(1);
		americano.setAmtSugar(2);
		americano.setAmtChocolate(0);
	}

	@Test
	public void testAddRecipe() {
		assertTrue(coffeeMaker.addRecipe(espresso));
	}

	@Test
	public void testContextCofee() throws ContextException {
		assertTrue(espresso.getAmtCoffee() == 6);
	}

	@Test
	public void testContextMilk() throws ContextException {
		assertTrue(espresso.getAmtMilk() == 1);
	}

	@Test
	public void addRecepie() throws Exception {
		coffeeMaker.addRecipe(mocha);
		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
	}

	@Test
	public void addContextRecepie() throws Exception {
		coffeeMaker.addRecipe(Recipe.getContext(mocha));
		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
	}

	@Test
	public void addServiceRecepie() throws Exception {
		Routine cmt = task(sig("addRecipe", coffeeMaker),
						context(types(Recipe.class), args(espresso),
							result("recipe/added")));

		logger.info("isAdded: " + exec(cmt));
		assertEquals(coffeeMaker.getRecipeForName("espresso").getName(), "espresso");
	}

	@Test
	public void addRecipes() throws Exception {
		coffeeMaker.addRecipe(mocha);
		coffeeMaker.addRecipe(macchiato);
		coffeeMaker.addRecipe(americano);
		coffeeMaker.addRecipe(espresso);

		assertEquals(coffeeMaker.getRecipeForName("mocha").getName(), "mocha");
		assertEquals(coffeeMaker.getRecipeForName("macchiato").getName(), "macchiato");
		assertEquals(coffeeMaker.getRecipeForName("americano").getName(), "americano");
		assertNull(coffeeMaker.getRecipeForName("espresso"));
	}

	@Test
	public void makeCoffee() throws Exception {
		coffeeMaker.addRecipe(espresso);

		Inventory inventory_new = coffeeMaker.checkInventory();

		int chocolate = inventory.getChocolate();
		int sugar = inventory.getSugar();
		int coffee = inventory.getCoffee();
		int milk = inventory.getMilk();

		assertEquals(coffeeMaker.makeCoffee(espresso, 200), 150);

		inventory_new = coffeeMaker.checkInventory();

		assertEquals(inventory_new.getChocolate(), chocolate - espresso.getAmtChocolate());
		assertEquals(inventory_new.getSugar(), sugar - espresso.getAmtSugar());
		assertEquals(inventory_new.getCoffee(), coffee - espresso.getAmtCoffee());
		assertEquals(inventory_new.getMilk(), milk - espresso.getAmtMilk());
	}


	@Test
	public void deleteRecipe () {
		coffeeMaker.addRecipe(espresso);
		assertTrue(coffeeMaker.deleteRecipe(espresso));
		assertFalse(Arrays.asList(coffeeMaker.getRecipes()).contains(espresso));
	}

	@Test
	public void deleteRecipes () {
		coffeeMaker.deleteRecipes();
		for (int i = 0; i < coffeeMaker.getRecipes().length; i++) assertEquals(coffeeMaker.getRecipes()[i], new Recipe());
	}

	@Test
	public void editRecipe () {
		coffeeMaker.addRecipe(mocha);
		assertTrue(Arrays.asList(coffeeMaker.getRecipes()).contains(mocha));
		coffeeMaker.editRecipe(mocha, espresso);
		assertFalse(Arrays.asList(coffeeMaker.getRecipes()).contains(mocha));
		assertTrue(Arrays.asList(coffeeMaker.getRecipes()).contains(espresso));
	}

	@Test
	public void addInventory () {
		assertTrue(coffeeMaker.addInventory( 1, 1, 1, 1));
		assertFalse(coffeeMaker.addInventory( -1, 0, 0, 0));
		assertFalse(coffeeMaker.addInventory( 0, -1, 0, 0));
		assertFalse(coffeeMaker.addInventory( 0, 0, -1, 0));
		assertFalse(coffeeMaker.addInventory( 0, 0, 0, -1));

		Inventory inventory_new = coffeeMaker.checkInventory();

		assertEquals(inventory_new.getChocolate(), 16);
		assertEquals(inventory_new.getCoffee(), 16);
		assertEquals(inventory_new.getMilk(), 16);
		assertEquals(inventory_new.getSugar(), 16);
	}

}
