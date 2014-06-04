package model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SentenceModel {
	// Le nom de l'activité (create, remove, etc.)
	String action;
	// le nom de l'objet (end event, etc.
	String object;
	// La classe de l'objet (bpmn.model.object.HorizontalPool)
	String objectClassName;
	// L'argument : une chaîne quelconque
	ArrayList<String> args = new ArrayList<String>();

	public SentenceModel() {
		action = "";
		object = "";
		// args =;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String act) {
		this.action = act;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<String> args) {
		this.args = args;
	}

	public void addObjectElement(String element) {
		String s = object.length() == 0 ? element : " " + element;
		object = object + s;
	}

	public void addArgsElement(String element) {
		String s = args.size() == 0 ? element : " " + element;
		args.add(s);
	}

	public String getObjectClassName() {
		return objectClassName;
	}

	public void setObjectClassName(String objectClassName) {
		this.objectClassName = objectClassName;
	}

	// Utile pour contrôler
	@Override
	public String toString() {
		return action + " " + object + " " + args + " " + objectClassName;
	}

	// sérialisation objet pour l'échange de message entre agents
	public String toJSON() {
		StringWriter sw = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(sw, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = sw.toString();
		return s;
	}

}
