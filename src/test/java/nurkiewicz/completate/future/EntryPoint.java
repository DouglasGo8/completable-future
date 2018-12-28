package nurkiewicz.completate.future;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurkiewicz.completable.future.util.AbstractFuture;
import static java.lang.System.out;
/**
 * 
 * @author Administrador
 *
 */
public class EntryPoint extends AbstractFuture {

	private Logger loG = LoggerFactory.getLogger(EntryPoint.class);

	private static final String tag = "java";

	@Test
	@Ignore
	public void step_01_Introduction() {

		final String result = super.svcStackoverflow.mostOfRecentQuestionAboutTopic(tag);
		loG.debug("Most recent Java question is: {}", result); // block

		loG.debug("Got it...");
	}

	@Test
	@Ignore
	public void step_01_ExecutorService() {
		try {

			final Callable<String> task = () -> super.svcStackoverflow.mostOfRecentQuestionAboutTopic(tag);
			final Future<String> javaQuestionAbout = super.executor.submit(task);

			final String javaQuestionResult = javaQuestionAbout.get(); // block

			loG.debug("Found: '{}'", javaQuestionResult);

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // block

	}

	@Test
	@Ignore
	public void step_02_Creating() {

		try {
			CompletableFuture<Integer> answer = CompletableFuture.<Integer>completedFuture(42);

			final int fortyTwo = answer.get();
			
			out.println(fortyTwo);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void step_02_supllyAsync() {
		
	}
}
