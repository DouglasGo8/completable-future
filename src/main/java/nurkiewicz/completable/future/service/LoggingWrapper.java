package nurkiewicz.completable.future.service;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

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
		
		loG.debug("Entering mostRecentQuestionsAbout({})", tag);
		final Document document = this.target.mostOfRecentQuestionsAboutTopic(tag);
		
		if (loG.isTraceEnabled()) {
			loG.trace("Leaving mostRecentQuestionsAbout({}): {}", tag, htmlExcerpt(document));
		}
		
		return document;
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	private String htmlExcerpt(Document document) {
		final String outerHtml = document.outerHtml();
		final Iterable<String> lines = Splitter.onPattern("\r?\n").split(outerHtml);
		final Iterable<String> firstLines = Iterables.limit(lines, 4);
		final String excerpt = Joiner.on(' ').join(firstLines);
		final int remainingBytes = Math.max(outerHtml.length() - excerpt.length(), 0);
		return excerpt + " [...and " + remainingBytes + " chars]";
	}

}
