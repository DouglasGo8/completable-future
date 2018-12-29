package nurkiewicz.completate.future;

import static java.lang.System.out;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurkiewicz.completable.future.domain.Question;
import nurkiewicz.completable.future.util.AbstractFuture;

/**
 * 
 * @author Administrador
 *
 */
public class EntryPoint extends AbstractFuture {

	/**
	 * 
	 */
	private static final String tag = "java";
	/**
	 * 
	 */
	private Logger loG = LoggerFactory.getLogger(EntryPoint.class);

	@Test
	@Ignore
	public void step_01_Introduction() {

		final String result = super.svcStackoverflow.mostOfRecentQuestionAboutTopic(tag);
		loG.debug("Most recent Java question is: {}", result); // blocks

		loG.debug("Got it...");
	}

	@Test
	@Ignore
	public void step_01_ExecutorService() {
		try {

			final Callable<String> task = () -> super.svcStackoverflow.mostOfRecentQuestionAboutTopic(tag);
			final Future<String> javaQuestionAbout = super.executor.submit(task);

			final String javaQuestionResult = javaQuestionAbout.get(); // blocks

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

			final int fortyTwo = answer.get(); // blocks

			out.println(fortyTwo);

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void step_02_supplyAsync() {

		try {

			loG.debug("Found: '{}'", super.questions(tag).get()); // Future.get() equivalent blocks

			// loG.debug("Sometimes first");

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void step_02_supplyAsyncWithCustomExecutor() {
		try {
			
			loG.debug("Found: '{}'", super.questions(tag).get()); // blocks
			// loG.debug("Sometimes get first");

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void step_03_oldSchool() {

		final Document doc = super.svcStackoverflow.mostOfRecentQuestionsAboutTopic(tag);
		
		loG.debug("Hi there");

		final Element element = doc.select("a.question-hyperlink").get(0);
		final String title = element.text();
		final int length = title.length();

		loG.debug("Length: {}", length);

	}

	@Test
	@Ignore
	public void step_03_callbacksCallbacksEverywhere() {

		super.questions(tag).thenAccept(doc -> loG.debug("Downloaded: {}", doc)); // non-blocking

		this.sleepMe(5);
	}

	@Test
	@Ignore
	public void step_03_thenApply() {

		super.questionsByDoc(tag)
			.thenApply((Document doc) -> doc.select("a.question-hyperlink").get(0)) // works like Map in Stream
			.thenApply(Element::text) // Works like Map in Stream
			.thenApply(String::length) // Works like Map in Stream
			.thenAccept(out::println); // Consumer
		
		this.sleepMe(5);
	} 
	
	@Test
	@Ignore
	public void step_04_thenApplyIsWrong() {
		
		super.questionsByDoc(tag)
			.thenApply(doc -> findMostInterestingQuestion(doc));
		
		
		this.sleepMe(5);
	}
	
	@Test
	@Ignore
	public void step_04_thenAcceptIsPoor() {
		super.questionsByDoc(tag).thenAccept(doc -> {
			this.findMostInterestingQuestion(doc).thenAccept(question -> {
				this.googleAnswer(question).thenAccept(answer  -> {
					this.postAnswer(answer ).thenAccept(status -> {
						if (status == 200) {
							loG.debug("OK");
						} else {
							loG.error("Wrong status code {}", status);
						}
					});
				});
			});
		});
		
		this.sleepMe(5);
	}

	@Test
	@Ignore
	public void step_04_thenCompose() {
		
		super.questionsByDoc(tag)
			.thenCompose(doc -> this.findMostInterestingQuestion(doc))
			.thenCompose(question -> this.googleAnswer(question))
			.thenCompose(answer -> this.postAnswer(answer))
			.thenAccept(status -> {
				if (status == 200) {
					loG.debug("Ok");
				} else {
					loG.error("Wrong status code {}", status);
				}
			});
		
		this.sleepMe(6);
		
	}
	
	@Test
	@Ignore
	public void step_04_chainedComposeShort() {
		super.questionsByDoc(tag)
			.thenCompose(this::findMostInterestingQuestion)
			.thenCompose(this::googleAnswer)
			.thenCompose(this::postAnswer)
			.thenAccept(status -> {
				if (status == 200) {
					loG.debug("Ok");
				} else {
					loG.error("Wrong status code {}", status);
				}
			});
	
		this.sleepMe(10);
	}
	
	@Test
	@Ignore
	public void step_05_thenCombide() {
		
		final CompletableFuture<String> java = super.questions("java");
		final CompletableFuture<String> python = super.questions("python");
		
		final CompletableFuture<Integer> both = java.thenCombine(python, (t1, t2) -> t1.length() + t2.length());
		
		both.thenAccept(length -> loG.debug("Total Length: {}", length));
		
		this.sleepMe(10);
	}
	
	@Test
	@Ignore
	public void step_05_either() {
		
		final CompletableFuture<String> java = super.questions("java");
		final CompletableFuture<String> python = super.questions("python");

		final CompletableFuture<String> both = java.applyToEither(python, title -> title.toUpperCase());
		
		both.thenAccept(title -> loG.debug("First: {}", title));
		
		
		this.sleepMe(10);
	}
	
	@Test
	@Ignore
	public void step_06_allOf() {
		
		final CompletableFuture<String> java = super.questions("java");
		final CompletableFuture<String> python = super.questions("python");
		final CompletableFuture<String> groovy = super.questions("groovy");
		final CompletableFuture<String> clojure = super.questions("clojure");

		final CompletableFuture<Void> allCompleted = 
			CompletableFuture.allOf(java, python, groovy, clojure);
		
		allCompleted.thenRun(() -> { // Non-blocking
			try {
				loG.debug("Loaded: {}", java.get());
				loG.debug("Loaded: {}", python.get());
				loG.debug("Loaded: {}", groovy.get());
				loG.debug("Loaded: {}", clojure.get());
			} catch (InterruptedException | ExecutionException e) {
				loG.error("", e);
			}
		});		
		
		this.sleepMe(10);
		
	}
	
	@Test
	@Ignore
	public void step_06_anyOf() {
		
		final CompletableFuture<String> java = super.questions("java");
		final CompletableFuture<String> python = super.questions("python");
		final CompletableFuture<String> groovy = super.questions("groovy");
		final CompletableFuture<String> clojure = super.questions("clojure");

		final CompletableFuture<Object> anyOf = 
			CompletableFuture.anyOf(java, python, groovy, clojure);
		
		anyOf.thenAccept(result -> loG.debug("First: {}", result));
		
		this.sleepMe(10);
		
	}
	
	
	@Test 
	@Ignore
	public void step_07_AsyncCallback () throws Exception {
		
		final CompletableFuture<String> java = CompletableFuture
				.<String>supplyAsync(() -> 
					super.svcStackoverflow.mostOfRecentQuestionAboutTopic("java"), 
					super.poolAlpha);
		
		final CompletableFuture<String> scala = CompletableFuture
				.<String>supplyAsync(() -> 
					super.svcStackoverflow.mostOfRecentQuestionAboutTopic("scala"), 
					super.poolBeta);
		
		final CompletableFuture<String> first = java.applyToEither(scala, question -> {
			this.loG.debug("First: {}", question);
			return question.toUpperCase();
		});
		
		first.thenAccept(q-> loG.debug("Sync: {}", q));
		first.thenAcceptAsync(q-> loG.debug("Async: {}", q));
		first.thenAcceptAsync(q-> loG.debug("Async (pool): {}", q), super.poolGamma);
		
		first.get(); // block
	
	}
	
	
	@Test
	@Ignore
	public void step_08_exceptionsShortCircuitFuture() throws Exception {
		
		super.questions("php")
			.thenApply(r-> {
				this.loG.debug("Success!");
				return r;
			}).get();
		
		
	}
	
	
	@Test
	@Ignore
	public void step_08_handleExceptions() throws Exception {

		CompletableFuture<String> questions = super.questions("php")
				.handle((result, throwable) -> {
					if (throwable != null) {
						return "No PHP today due to: " + throwable;
					} else {
						return result.toUpperCase();
					}
				});
		
		loG.debug("Handled: {}", questions.get());
	}
	
	
	@Test
	@Ignore
	public void step_08_shouldHandleExceptionally() throws Exception {

		CompletableFuture<String> questions = super.questions("php")
				.exceptionally(throwable -> "Sorry, try again later!");
		
		loG.debug("Handled: {}", questions.get());
	}
	
	
	
	
	private CompletableFuture<Question> findMostInterestingQuestion(Document document) {
		return CompletableFuture.completedFuture(new Question());
	}
	
	
	private CompletableFuture<String> googleAnswer(Question q) {
		return CompletableFuture.completedFuture("42");
	}
	
	private CompletableFuture<Integer> postAnswer(String answer) {
		return CompletableFuture.completedFuture(200);
	}
	
	/**
	 * 
	 * @param duration
	 */

	private void sleepMe(int duration) {
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
