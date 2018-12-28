package nurkiewicz.completable.future.common;

import org.jsoup.nodes.Document;

/**
 * 
 * @author Administrador
 *
 */
public interface IStackOverflowClient {

	/**
	 * 
	 * @param tag
	 * @return
	 */
	String mostOfRecentQuestionAboutTopic(String tag);
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	Document mostOfRecentQuestionsAboutTopic(String tag);
}
