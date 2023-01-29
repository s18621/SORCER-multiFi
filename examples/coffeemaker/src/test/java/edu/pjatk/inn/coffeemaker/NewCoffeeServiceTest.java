package edu.pjatk.inn.coffeemaker;

import edu.pjatk.inn.coffeemaker.impl.CoffeeMaker;
import edu.pjatk.inn.coffeemaker.impl.DeliveryImpl;
import edu.pjatk.inn.coffeemaker.impl.Recipe;
import edu.pjatk.inn.requestor.CoffeemakerConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.core.provider.rendezvous.ServiceJobber;
import sorcer.service.*;

import static edu.pjatk.inn.coffeemaker.impl.Recipe.getRecipe;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sorcer.co.operator.*;
import static sorcer.ent.operator.*;
import static sorcer.eo.operator.args;
import static sorcer.eo.operator.pipe;
import static sorcer.eo.operator.result;
import static sorcer.eo.operator.*;
import static sorcer.mo.operator.add;
import static sorcer.mo.operator.model;
import static sorcer.mo.operator.result;
import static sorcer.mo.operator.value;
import static sorcer.mo.operator.*;
import static sorcer.so.operator.*;

/**
 * @author Mike Sobolewski
 */
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/coffeemaker")
public class NewCoffeeServiceTest {
	private final static Logger logger = LoggerFactory.getLogger(NewCoffeeServiceTest.class);

	private Context espresso, mocha, macchiato, americano;
	private Recipe recipe;

	@Before
	public void setUp() throws ContextException {
		recipe = new Recipe();
		recipe.setName("espresso");
		recipe.setPrice(50);
		recipe.setAmtCoffee(6);
		recipe.setAmtMilk(1);
		recipe.setAmtSugar(1);
		recipe.setAmtChocolate(0);

		espresso = context(ent("key", "espresso"), ent("price", 50),
			ent("amtCoffee", 6), ent("amtMilk", 0),
			ent("amtSugar", 1), ent("amtChocolate", 0));

		mocha  = context(ent("key", "mocha"), ent("price", 100),
			ent("amtCoffee", 8), ent("amtMilk", 1),
			ent("amtSugar", 1), ent("amtChocolate", 2));

		macchiato  = context(ent("key", "macchiato"), ent("price", 40),
			ent("amtCoffee", 7), ent("amtMilk", 1),
			ent("amtSugar", 2), ent("amtChocolate", 0));

		americano  = context(ent("key", "americano"), ent("price", 40),
			ent("amtCoffee", 4), ent("amtMilk", 0),
			ent("amtSugar", 1), ent("amtChocolate", 0));
	}

	@After
	public void cleanUp() throws Exception {
		Routine cmt =
			task(sig("deleteRecipes", CoffeeMaking.class),
				context(types(), args()));

		cmt = exert(cmt);
		logger.info("deleted recipes context: " + context(cmt));
	}


	@Test
	public void getCoffeeRemoteBlock() throws Exception {

		Task addRecipe = task("addrecipe", sig("addRecipe", CoffeeDBService.class), context(
				inVal("key", "chocolate"),
				inVal("price", 420),
				inVal("amtCoffee", 0),
				inVal("amtMilk", 0),
				inVal("amtSugar", 1),
				inVal("amtChocolate", 10),
			outPaths("key")));

		Task getRecipe = task("getrecipe", sig("getRecipe", Delivery.class), context(
			inVal("recipe/key", "chocolate"),
			outPaths("recipe/key")));

		Block createNewRecipe = block(context(inVal("key"), val("recipe/key")), addRecipe, getRecipe);

		Context out = context(exert(createNewRecipe));

		logger.info("out: " + out);
		assertEquals(value(out, "key"), "chocolate");
		assertEquals(value(out, "recipe/key"), "chocolate");

	}

	@Test
	public void coffeemakerConsumerAsService() throws Exception {

		Consumer req = consumer(CoffeemakerConsumer.class, "netlet");


		Context cxt = (Context) exec(req);

		logger.info("out context: " + cxt);
		assertEquals("chocolate", value(cxt, "job/addRecipe/recipe/key"));
	}
}

