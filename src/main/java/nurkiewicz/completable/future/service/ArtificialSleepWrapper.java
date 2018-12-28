package nurkiewicz.completable.future.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Document;

import nurkiewicz.completable.future.common.IStackOverflowClient;

/**
 * 
 * @author Administrador
 *
 */
public class ArtificialSleepWrapper implements IStackOverflowClient {

	private final IStackOverflowClient target;

	private static final Random RDN = new Random();

	public ArtificialSleepWrapper(IStackOverflowClient target) {
		// TODO Auto-generated constructor stub
		this.target = target;
	}

	/**
	 * @param tag
	 * @return
	 */
	@Override
	public String mostOfRecentQuestionAboutTopic(String tag) {
		// TODO Auto-generated method stub
		final long start = System.currentTimeMillis();
		final String result = this.target.mostOfRecentQuestionAboutTopic(tag);
		artificialSleep(1000 - (System.currentTimeMillis() - start));
		return result;
	}

	@Override
	public Document mostOfRecentQuestionsAboutTopic(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	protected static void artificialSleep(long expected) {
		try {
			TimeUnit.MILLISECONDS.sleep((long) (expected + RDN.nextGaussian() * expected / 2));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
