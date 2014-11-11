package sorcer.eol.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sorcer.co.operator.from;
import static sorcer.co.operator.inEnt;
import static sorcer.co.operator.outEnt;
import static sorcer.eo.operator.context;
import static sorcer.eo.operator.cxt;
import static sorcer.eo.operator.exert;
import static sorcer.eo.operator.get;
import static sorcer.eo.operator.result;
import static sorcer.eo.operator.srvFi;
import static sorcer.eo.operator.srvFis;
import static sorcer.eo.operator.sig;
import static sorcer.eo.operator.srv;
import static sorcer.eo.operator.strategy;
import static sorcer.eo.operator.task;
import static sorcer.eo.operator.type;
import static sorcer.eo.operator.value;

import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;

import sorcer.arithmetic.provider.Adder;
import sorcer.arithmetic.provider.Multiplier;
import sorcer.arithmetic.provider.impl.AdderImpl;
import sorcer.arithmetic.provider.impl.MultiplierImpl;
import sorcer.arithmetic.tester.provider.impl.SubtractorImpl;
import sorcer.core.provider.Exerter;
import sorcer.service.Context;
import sorcer.service.ContextException;
import sorcer.service.Exertion;
import sorcer.service.ExertionException;
import sorcer.service.Signature;
import sorcer.service.Signature.Direction;
import sorcer.service.SignatureException;
import sorcer.service.Strategy.Access;
import sorcer.service.Strategy.Monitor;
import sorcer.service.Strategy.Wait;
import sorcer.service.Task;
import sorcer.util.ProviderAccessor;

/**
 * @author Mike Sobolewski
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/eol")
public class NetExertionTasks {
	private final static Logger logger = Logger.getLogger(NetExertionTasks.class.getName());
	
	@Test
	public void exertTask() throws Exception  {

		Task t5 = srv("t5", sig("add", Adder.class),
				cxt("add", inEnt("arg/x1", 20.0), inEnt("arg/x2", 80.0), result("result/y")));

		Exertion out = exert(t5);
		Context cxt = context(out);
		logger.info("out context: " + cxt);
		logger.info("context @ arg/x1: " + value(cxt, "arg/x1"));
		logger.info("context @ arg/x2: " + value(cxt, "arg/x2"));
		logger.info("context @ result/y: " + value(cxt, "result/y"));

		assertEquals(100.0, value(cxt, "result/y"));
	}
	
	
	@Test
	public void valueTask() throws SignatureException, ExertionException, ContextException  {

		Task t5 = srv("t5", sig("add", Adder.class),
				cxt("add", inEnt("arg/x1", 20.0), inEnt("arg/x2", 80.0), result("result/y")));

		Object out = value(t5);
		logger.info("out value: " + out);
		assertEquals(100.0, out);
		
	}

	
	@Test
	public void spaceTask() throws Exception {

		Task t5 = task(
				"t5",
				sig("add", Adder.class),
				context("add", inEnt("arg/x1", 20.0),
						inEnt("arg/x2", 80.0), outEnt("result/y")),
				strategy(Access.PULL, Wait.YES));

		t5 = exert(t5);
		logger.info("t5 context: " + context(t5));
		logger.info("t5 value: " + get(t5, "result/y"));
		assertEquals("Wrong value for 100.0", get(t5, "result/y"), 100.0);
	}

	
	@Test
	public void exerterTest() throws Exception {

		Task f5 = task(
			"f5",
			sig("add", Adder.class),
			context("add", inEnt("arg/x1", 20.0),
					inEnt("arg/x2", 80.0), outEnt("result/y", null)),
			strategy(Monitor.NO, Wait.YES));
	
	Exertion out = null;
//	long start = System.currentTimeMillis();
	Exerter exerter = ProviderAccessor.getExerter();
//	logger.info("got exerter: " + exerter);

	out = exerter.exert(f5);
//	long end = System.currentTimeMillis();
	
//	logger.info("task f5 context: " + context(out));
//	logger.info("task f5 result/y: " + get(context(out), "result/y"));
	assertEquals(get(out, "result/y"), 100.00);
	
	}
	
	
	@Test
	public void batchTask() throws Exception {
		// batch for the composition f1(f2(f3((x1, x2), f4(x1, x2)), f5(x1, x2))
		// shared context with named paths
		Task batch3 = task("batch3",
				type(sig("multiply", MultiplierImpl.class, result("subtract/x1", Direction.IN)), Signature.PRE),
				type(sig("add", AdderImpl.class, result("subtract/x2", Direction.IN)), Signature.PRE),
				sig("subtract", SubtractorImpl.class, result("result/y", from("subtract/x1", "subtract/x2"))),
				context(inEnt("multiply/x1", 10.0), inEnt("multiply/x2", 50.0), 
						inEnt("add/x1", 20.0), inEnt("add/x2", 80.0)));
		
		logger.info("task getSignatures:" + batch3.getFidelity());
				
		batch3 = exert(batch3);
//		//logger.info("task result/y: " + get(batch3, "result/y"));
//		assertEquals("Wrong value for 400.0", get(batch3, "result/y"), 400.0);
	}
	
	
	@Test
	public void suffixedBatchTask() throws Exception {
		// batch for the composition f1(f2(f3((x1, x2), f4(x1, x2)), f5(x1, x2))
		// shared context with prefixed paths
		Task batch3 = task("batch3",
				type(sig("multiply#op1", MultiplierImpl.class, result("op3/x1", Direction.IN)), Signature.PRE),
				type(sig("add#op2", AdderImpl.class, result("op3/x2", Direction.IN)), Signature.PRE),
				sig("subtract", SubtractorImpl.class, result("result/y", from("op3/x1", "op3/x2"))),
				context(inEnt("op1/x1", 10.0), inEnt("op1/x2", 50.0), 
						inEnt("op2/x1", 20.0), inEnt("op2/x2", 80.0)));
		
		batch3 = exert(batch3);
		//logger.info("task result/y: " + get(batch3, "result/y"));
		assertEquals("Wrong value for 400.0", get(batch3, "result/y"), 400.0);
	}
		
	
	@Test
	public void localFiBatchTask() throws Exception {
		
		Task t4 = task("t4", srvFi("object", sig("multiply", MultiplierImpl.class), sig("add", AdderImpl.class)),
				srvFi("net", sig("multiply", Multiplier.class), sig("add", Adder.class)),
				context("shared", inEnt("arg/x1", 10.0), inEnt("arg/x2", 50.0),
						outEnt("result/y", null)));

		t4 = exert(t4);
		logger.info("task cont4text: " + context(t4));
		
		t4 = exert(t4, srvFi("net"));
		logger.info("task cont4text: " + context(t4));
		
	}

	@Test
	public void netLocalFiTask() throws Exception {
		Task task = task("add",
				srvFi("net", sig("add", Adder.class)),
				srvFi("object", sig("add", AdderImpl.class)),
				context(inEnt("arg/x1", 20.0), inEnt("arg/x2", 80.0),
						result("result/y")));
		
		logger.info("sFi: " + srvFi(task));
		logger.info("sFis: " + srvFis(task));

//		task = exert(task, sFi("object"));
//		logger.info("exerted: " + task);
//		assertTrue("Wrong value for 100.0", (Double)get(task) == 100.0);
		
		task = exert(task, srvFi("net"));
		logger.info("exerted: " + task);
		assertTrue("Wrong value for 100.0", (Double)get(task) == 100.0);
	
	}
	
}
	
	