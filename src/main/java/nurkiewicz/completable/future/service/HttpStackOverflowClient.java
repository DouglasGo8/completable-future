package nurkiewicz.completable.future.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

import nurkiewicz.completable.future.common.IStackOverflowClient;

/**
 * 
 * @author Administrador
 *
 */
public class HttpStackOverflowClient implements IStackOverflowClient {

	private static final Logger loG = LoggerFactory.getLogger(HttpStackOverflowClient.class);
	
	@Override
	public String mostOfRecentQuestionAboutTopic(String tag) {
		// TODO Auto-generated method stub
		return this.fetchTitleOnline(tag);
	}

	
	/**
	 * @param tag
	 * @return
	 */
	@Override
	public Document mostOfRecentQuestionsAboutTopic(String tag) {

		// TODO Auto-generated method stub
		try {
			loG.debug("Read to start research about...{}", tag);
			return Jsoup.connect("http://stackoverflow.com/questions/tagged/" + tag).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw Throwables.propagate(e);
		}
	}
	
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	private String fetchTitleOnline(String tag) {
		return mostOfRecentQuestionsAboutTopic(tag).select("a.question-hyperlink").text();
	}

}
