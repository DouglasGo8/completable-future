package nurkiewicz.completable.future.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurkiewicz.completable.future.common.IStackOverflowClient;

/**
 * 
 * @author Administrador
 *
 */
public class InjectErrorsWrapper implements IStackOverflowClient {

	private final Set<String> blackList;
	private final IStackOverflowClient target;

	private Logger loG = LoggerFactory.getLogger(InjectErrorsWrapper.class);

	public InjectErrorsWrapper(IStackOverflowClient target, String... blackList) {
		// TODO Auto-generated constructor stub
		this.target = target;
		this.blackList = new HashSet<String>(Arrays.asList(blackList));
	}

	@Override
	public String mostOfRecentQuestionAboutTopic(String tag) {
		// TODO Auto-generated method stub
		this.throwsIfBlackListed(tag);
		return target.mostOfRecentQuestionAboutTopic(tag);
	}

	@Override
	public Document mostOfRecentQuestionsAboutTopic(String tag) {
		// TODO Auto-generated method stub
		this.throwsIfBlackListed(tag);
		return this.target.mostOfRecentQuestionsAboutTopic(tag);
	}

	/**
	 * 
	 * @param tag
	 */
	private void throwsIfBlackListed(String tag) {
		if (this.blackList.contains(tag)) {
			ArtificialSleepWrapper.artificialSleep(400l);
			loG.warn("About to throw artificial exception due to: {}", tag);
			throw new IllegalArgumentException("Got fail -> Unsupported " + tag);
		}
	}
}
