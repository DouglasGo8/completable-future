package nurkiewicz.completable.future.service;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
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
public class FallbackStubClient implements IStackOverflowClient {

	private final IStackOverflowClient target;
	private static final Logger loG = LoggerFactory.getLogger(FallbackStubClient.class);

	public FallbackStubClient(IStackOverflowClient target) {
		// TODO Auto-generated constructor stub
		this.target = target;
	}

	@Override
	public String mostOfRecentQuestionAboutTopic(String tag) {
		// TODO Auto-generated method stub
		try {
			return this.target.mostOfRecentQuestionAboutTopic(tag);
		} catch (Exception e) {
			loG.warn("Problem retrieving tag {}", tag, e);
			//
			switch (tag.toLowerCase()) {
			case "java":
				return "How to generate xml report with maven dependency?";
			case "scala":
				return "Update a timestamp SettingKey in an sbt 0.12 task";
			case "groovy":
				return "Reusing Grils variables inside Config.groovy";
			case "clojure":
				return "Merge two comma delimited string in Cloujure";
			default:
				throw e;

			}
		}
	}

	@Override
	public Document mostOfRecentQuestionsAboutTopic(String tag) {
		// TODO Auto-generated method stub
		try {
			return this.target.mostOfRecentQuestionsAboutTopic(tag);
		} catch (Exception e) {
			loG.warn("Problem retrieving recent question {}", tag, e);
			return loadStubHtmlFromDisk(tag);
		}
	}

	private Document loadStubHtmlFromDisk(String tag) {
		try {
			final URL resource = getClass().getResource("/" + tag + "-questions.html");
			final String html = IOUtils.toString(resource);
			return Jsoup.parse(html);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

}
