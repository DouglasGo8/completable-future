package nurkiewicz.completable.future.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import nurkiewicz.completable.future.common.IStackOverflowClient;
import nurkiewicz.completable.future.service.ArtificialSleepWrapper;
import nurkiewicz.completable.future.service.FallbackStubClient;
import nurkiewicz.completable.future.service.HttpStackOverflowClient;
import nurkiewicz.completable.future.service.InjectErrorsWrapper;
import nurkiewicz.completable.future.service.LoggingWrapper;

/**
 * 
 * @author Administrador
 *
 */
public abstract class AbstractFuture {

	/**
	 * 
	 */
	@Rule
	public TestName tName = new TestName();
	/**
	 * 
	 */
	private static final Logger loG = LoggerFactory.getLogger(AbstractFuture.class);

	/**
	 * 
	 */
	protected final ExecutorService executor = Executors.newFixedThreadPool(10, threadFactory("WorkerThread_"));
	protected final ExecutorService poolAlpha = Executors.newFixedThreadPool(10, threadFactory("Alpha"));
	protected final ExecutorService poolBeta = Executors.newFixedThreadPool(10, threadFactory("Beta"));
	protected final ExecutorService poolGamma = Executors.newFixedThreadPool(10, threadFactory("Gamma"));
	/**
	 * 
	 */
	protected final IStackOverflowClient svcStackoverflow = 
			new FallbackStubClient(
				new InjectErrorsWrapper(
					new LoggingWrapper(
						new ArtificialSleepWrapper(
							new HttpStackOverflowClient())), 
					"php"));

	/**
	 * 
	 * @param nameFormat
	 * @return
	 */
	protected ThreadFactory threadFactory(String nameFormat) {
		return new ThreadFactoryBuilder().setNameFormat(nameFormat.concat("-%d")).build();
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	protected CompletableFuture<String> questions(String tag) {
		return CompletableFuture.<String>supplyAsync(() -> this.svcStackoverflow.mostOfRecentQuestionAboutTopic(tag));
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	protected CompletableFuture<Document> questionsByDoc(String tag) {
		return CompletableFuture
				.<Document>supplyAsync(() -> this.svcStackoverflow.mostOfRecentQuestionsAboutTopic(tag));
	}
	/**
	 * 
	 */
	@Before
	public void logTestStart() {
		loG.debug("Starting: {}", tName.getMethodName());
	}
}
