package nurkiewicz.completable.future.service;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurkiewicz.completable.future.common.IStackOverflowClient;

/**
 * 
 * @author Administrador
 *
 */
public class LoggingWrapper implements IStackOverflowClient {

	private final IStackOverflowClient target;
	private static final Logger loG = LoggerFactory.getLogger(LoggingWrapper.class);

	public LoggingWrapper(IStackOverflowClient target) {
		// TODO Auto-generated constructor stub
		this.target = target;
	}

	@Override
	public String mostOfRecentQuestionAboutTopic(String tag) {
		// TODO Auto-generated method stub
		loG.debug("Entering mostOfRecentQuestionAboutTopic({})", tag);
		final String title = this.target.mostOfRecentQuestionAboutTopic(tag);
		loG.debug("Leaving mostOfRecentQuestionAboutTopic({})", tag);
		return title;
	}

	@Override
	public Document mostOfRecentQuestionsAboutTopic(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

}
