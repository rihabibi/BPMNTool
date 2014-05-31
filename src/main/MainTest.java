package main;

import model.Interpretor;
import model.SentenceModel;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MainTest {
	public static Logger logger = Logger.getLogger("com.hp");
	// phrase ok
	public static String SENTENCE_1 = "ok, create   a horizontal pool called SalesRep";
	// phrase ok
	public static String SENTENCE_2 = "ok, remove   a vertical pool called SalesRep";
	// pas d'activit� pas d'arguments ; on r�cup�re l'objet
	public static String SENTENCE_3 = "ok, create link between Enter Order and Store Order ";
	// objet incomplet ; on r�cup�re l'argument
	public static String SENTENCE_4 = "ok, connect object 1 and object 2";
	// pas de s�parateur ; on r�cup�re l'activit� et l'objet
	public static String SENTENCE_5 = "ok, create   a horizontal pool SalesRep";
	// l'objet n'est pas dans la base mais chacun des mots y est
	public static String SENTENCE_6 = "ok, create   a horizontal event called SalesRep";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		logger.setLevel(Level.INFO);
		Interpretor interpretor = new Interpretor();
		System.out.println("Main Index: create = " + interpretor.search("create"));
		System.out.println("Main Index: title = " + interpretor.search("title"));
		System.out.println("Main Index: inside = " + interpretor.search("inside"));
		System.out.println("Main Index: horizontal = " + interpretor.search("horizontal"));
		System.out.println("Main Index: horizontal pool = " + interpretor.search("horizontal pool"));
		System.out.println("Separator: called = " + interpretor.isSeparator("called"));
		System.out.println("Separator: title = " + interpretor.isSeparator("title"));
		System.out.println("Separator: too = " + interpretor.isSeparator("too"));
		System.out.println("Search: connect = " + interpretor.search("connection"));
		System.out.println("Separator: not = " + interpretor.isSeparator("not"));
		System.out.println("Separator: number = " + interpretor.isSeparator("number"));
		System.out.println("Object: start index = " + interpretor.isObject("start index"));
		System.out.println("Object: horizontal = " + interpretor.isObject("horizontal"));
		System.out.println("Object: pool = " + interpretor.isObject("pool"));
		System.out.println("Object: remove = " + interpretor.isObject("remove"));
		System.out.println("Activity: remove = " + interpretor.isAction("remove"));
		SentenceModel m1 = interpretor.analyzeSentence(SENTENCE_1);
		System.out.println(m1.toJSON());
		SentenceModel m2 = interpretor.analyzeSentence(SENTENCE_2);
		System.out.println(m2.toJSON());
		SentenceModel m3 = interpretor.analyzeSentence(SENTENCE_4);
		System.out.println(m3.toJSON());
		SentenceModel m4 = interpretor.analyzeSentence(SENTENCE_4);
		System.out.println(m4.toJSON());
		SentenceModel m5 = interpretor.analyzeSentence(SENTENCE_5);
		System.out.println(m5.toJSON());
		SentenceModel m6 = interpretor.analyzeSentence(SENTENCE_6);
		System.out.println(m6.toJSON());
	}

}
