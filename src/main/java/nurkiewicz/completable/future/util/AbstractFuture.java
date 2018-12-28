package nurkiewicz.completable.future.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
	protected final ExecutorService executor = Executors.newFixedThreadPool(10, threadFactory("custom-thread_"));

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

	@Before
	public void logTestStart() {
		loG.debug("Starting: {}", tName.getMethodName());
	}
}
