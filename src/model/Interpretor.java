package model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.jena.larq.IndexBuilderString;
import org.apache.jena.larq.IndexLARQ;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class Interpretor 
{
	public static String KB_FILENAME = "doc/kb.n3";
	public static String BPMN_PREFIX = "";
	public static String OBJECT_NAME = "objectName";
	public static String ACTION_NAME = "actionName";
	public static String SEPARATOR_NAME = "sepName";
	public static String SPLIT_CHARACTERS = "[ ,;:.?]";

	Model model;
	IndexLARQ mainIndex;
	IndexLARQ objectIndex;
	IndexLARQ actionIndex;
	IndexLARQ separatorIndex;
	SentenceModel currentSentenceModel;

	public Interpretor()
	{
		init(KB_FILENAME);
	}

	private void init(String fileName) 
	{
		/**
		 * crée le model Jena lit la base de connaissance crée les index
		 */
		model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open(fileName);
		model.read(in, null, "TURTLE");
		// Crée les index
		createIndexes();
	}

	private void createIndexes() 
	{
		String nsbpmn = model.getNsPrefixURI(BPMN_PREFIX);
		Property p1 = model.getProperty(nsbpmn + OBJECT_NAME);
		Property p2 = model.getProperty(nsbpmn + ACTION_NAME);
		Property p3 = model.getProperty(nsbpmn + SEPARATOR_NAME);
		// Index de toutes les chaînes de la base
		mainIndex = getWholeStringIndex();
		// Index des chaînes atteintes par les propriétés p1, p2, p3
		objectIndex = getStringIndex(p1);
		actionIndex = getStringIndex(p2);
		separatorIndex = getStringIndex(p3);
	}

	// retourne l'index basé sur tous les littéraux
	private IndexLARQ getWholeStringIndex() 
	{
		IndexBuilderString larqBuilder = new IndexBuilderString();
		larqBuilder.indexStatements(model.listStatements());
		larqBuilder.closeWriter();
		model.unregister(larqBuilder);
		return larqBuilder.getIndex();
	}

	// Retourne l'index basé sur une propriété
	private IndexLARQ getStringIndex(Property p) 
	{
		IndexBuilderString larqBuilder = new IndexBuilderString(p);
		larqBuilder.indexStatements(model.listStatements());
		larqBuilder.closeWriter();
		model.unregister(larqBuilder);
		return larqBuilder.getIndex();
	}

	// Recherche si une chaîne est dans l'index principal
	public boolean search(String s) 
	{
		return inIndex(mainIndex, s);
	}

	// Est-ce un terme séparator
	public boolean isSeparator(String s) 
	{
		return inIndex(separatorIndex, s);
	}

	// Est-ce un terme d'objet
	public boolean isObject(String s)
	{
		return inIndex(objectIndex, s);
	}

	// Est-ce un terme d'activité
	public boolean isAction(String s) 
	{
		return inIndex(actionIndex, s);
	}

	// Recherche si une chaîne est dans l'index
	private boolean inIndex(IndexLARQ index, String s) 
	{
		NodeIterator iterator = index.searchModelByIndex(s);
		return iterator.hasNext();
	}

	/**
	 * Analyse une chaîne brute
	 * 
	 * @param s
	 *            : la chaîne brute
	 * @return: un SentenceModel contenant toutes les informations extraites de
	 *          l'analyse
	 */
	public SentenceModel analyzeSentence(String s) 
	{
		// Sépare en token
		String[] pieces = s.split(SPLIT_CHARACTERS);
		currentSentenceModel = new SentenceModel();
		ArrayList<String> words = new ArrayList<String>();
		// Ne garde que les tokens non vides
		for (int i = 0; i < pieces.length; i++) 
		{
			if (pieces[i].length() > 0) 
			{
				words.add(pieces[i]);
			}
		}

		boolean found = false;
		int i = 0;
		/**
		 * On recherche l'activité
		 */
		while (!found && (i < words.size())) 
		{
			found = isAction(words.get(i));
			i++;
		}
		if (found) 
		{
			/**
			 * Activité trouvée on cherche la suite de mots décrivant l'objet
			 * jusqu'au séparateur
			 */
			String actionName = words.get(i-1);
			String action = getActionId(actionName);
			currentSentenceModel.setAction(action);
			lookForObject(words, i);
		} 
		else 
		{
			return null;
			/**
			 * L'activité n'est pas trouvée on cherche les éléments objets
			 * à partir du début jusqu'au séparateur
			 */
			//lookForObject(words, 0);
		}
		return currentSentenceModel;
	}
	
	public String getActionId(String actionName)
	{
		String nsrdf = model.getNsPrefixURI(""); 
		Property type = model.getProperty(nsrdf+"actionName"); 
		StmtIterator iterator =  model.listStatements(new SimpleSelector((Resource)null,type,actionName));
		while(iterator.hasNext())
		{	
			String subject = iterator.nextStatement().getSubject().toString();
			String[] parts = subject.split("#");
			if (!parts[1].equals("Separator")){
				System.out.println(parts[1].toLowerCase());
				return parts[1].toLowerCase();
			}
		}
		return null;
	}

	private void lookForObject(ArrayList<String> words, int from) 
	{
		/**
		 * tous les mots appartenant au type objet avant le séparateur sont
		 * ajoutés comme éléments d'objet
		 */
		boolean found = false;
		boolean foundObject = false;
		int i = from;
		while (!found && (i < words.size())) 
		{
			if (isObject(words.get(i))) 
			{
				currentSentenceModel.addObjectElement(words.get(i));
				foundObject = true;
			}
			found = isSeparator(words.get(i));
			i++;
		}
		// Recherche les arguments
		if (found)
			lookForArgs(words, i);
		// Vérifie si l'objet est correct et recherche sa classe dans la kb
		// verifyObject(currentSentenceModel.getObject());
	}

	/**
	 * Prends les mots à la suite du séparateur s'il existe
	 * 
	 * @param words
	 *            : la liste des mots
	 * @param from
	 *            : rang à partir duquel prendre les mots
	 */
	private void lookForArgs(ArrayList<String> words, int from) 
	{
		int i = from;
		while (i < words.size()) 
		{
			if (!isSeparator(words.get(i)) && !words.get(i).equals("to")
					&& !words.get(i).equals("and")
					&& !words.get(i).equals("in")) 
			{
				currentSentenceModel.addArgsElement(words.get(i));
			}
			i++;
		}
	}

	private void verifyObject(String s)
	{
		if (isObject(s))
		{
			lookForObjectClass(s);
		}
	}

	/**
	 * Recherche dans la base la chaîne correspondant à la classe java de
	 * l'objet ne la trouve dans le cas où les mots sont corrects mais pas leur
	 * ensemble (horizontal event)
	 */
	private void lookForObjectClass(String s) 
	{
		String queryResult = runquery(createObjectClassQuery(s));
		currentSentenceModel
				.setObjectClassName(analyseQueryResult(queryResult));
	}

	// Exécute une requête SELECT à partir d'une chaîne
	private String runquery(String query)
	{
		QueryExecution queryExecution = QueryExecutionFactory.create(query,
				model);
		ResultSet r = queryExecution.execSelect();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsCSV(baos, r);
		queryExecution.close();
		return baos.toString();
	}

	/**
	 * Analyse le résultat d'une requête La première ligne contient le nom
	 * des paramètres de la query Les lignes suivantes les valueurs. Ici il n'y
	 * a qu'un paramètre
	 * 
	 * @param queryResult
	 * @return
	 */
	private String analyseQueryResult(String queryResult) 
	{
		Scanner sc = new Scanner(queryResult);
		sc.nextLine();
		String v = "";
		if (sc.hasNextLine()) 
		{
			v = sc.nextLine();
		}
		sc.close();
		return v;
	}

	/**
	 * Crée la requête SPARQL de recherche de la classe JAVA d'un objet
	 * 
	 * @param objectName
	 * @return
	 */
	private String createObjectClassQuery(String objectName) 
	{
		StringBuilder sb = new StringBuilder();
		String nsbpmn = model.getNsPrefixURI(BPMN_PREFIX);
		sb.append("PREFIX : <" + nsbpmn + "> ");
		sb.append("SELECT ?clname ");
		sb.append("	WHERE { ");
		sb.append("  ?object a :Object ; ");
		sb.append("     :objectName \"" + objectName + "\" ; ");
		sb.append("     :className ?clname . }");
		return sb.toString();
	}
}
