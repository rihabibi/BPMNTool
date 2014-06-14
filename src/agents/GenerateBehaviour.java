package agents;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import model.Action;
import model.End;
import model.JoinGateway;
import model.SplitGateway;
import model.Start;
import model.Task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GenerateBehaviour extends CyclicBehaviour {

	public GenerateBehaviour(GenerateurAgent a) {
		super(a);
	}

	public void action() {
		ACLMessage message = myAgent.receive(/*MessageTemplate.MatchPerformative(ACLMessage.REQUEST)*/);
		if (message != null) {
			// Deserialisation du message
			String Json = message.getContent();
			HashMap<String, Object> map = toMap(Json);

			// extraction des valeurs
			String request = (String) map.get("action");
			String object = (String) map.get("object");
			//String objectClassName = map.get("objectClassName");
			ArrayList<String> args = (ArrayList<String>) map.get("args");
			if(request==null)JOptionPane.showMessageDialog(null,"La phrase propos� n'est pas valide");
			else 
			switch (request) {
			case "create":
				Action addaction = new Action();
				switch (object) {
				case "startevent":
					GenerateStartEvent(request, args.get(0), addaction);
					sendAction(addaction);
					break;

				case "endevent":
					GenerateEndEvent(request, args.get(0), addaction);
					sendAction(addaction);
					break;

				case "activity":
					GenerateActivity(request, args.get(0), addaction);
					sendAction(addaction);
					break;

				case "horizontalpool":
					GeneratePool("pool",args.get(0), addaction);
					sendAction(addaction);
					break;

				case "parallelgateway":
					GeneratePGateway(request, null, addaction);
					sendAction(addaction);
					break;

				case "exclusivegateway":
					GenerateEGateway(request, null, addaction);
					sendAction(addaction);
					break;

				}
				break;

			case "remove":
				boolean ok=true;
				try {
				      int n = Integer.parseInt(args.get(0));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,"The argument is incorrect");
					ok=false;
				}
				if (ok){
					Action removeaction = new Action();
					if(object.equals("horizontalpool"))
					{
						removeaction.setType("remove_pool");
						String arg =args.get(0);
						removeaction.addId(args.get(0));
						sendAction(removeaction);
					}
					else
					{
						removeaction.setType("remove_item");
						removeaction.addId(args.get(0));
						sendAction(removeaction);
					}	
				}
				break;

			case "connect":
				boolean ok2=true;
				try {
					args.get(0);
				}catch (Exception e) {
					ok2=false;
				}
				try {
					args.get(1);
				}catch (Exception e) {
					ok2=false;
				}
				if (ok2){
					try {
					      int n = Integer.parseInt(args.get(0));
					} catch (NumberFormatException e) {
						ok2=false;
					}
					try {
					      int n = Integer.parseInt(args.get(1));
					} catch (NumberFormatException e) {
						ok2=false;
					}
					if(ok2){
						Action connectaction = new Action();// {"action":"connect","object":"","objectClassName":null,"args":"partde,arrivea1,arrive2"}
						connectaction.addId(args.get(0));
						connectaction.addId(args.get(1));
						connectaction.setType("connect");
						sendAction(connectaction);
						System.out.println("connexion");
					}else {
						JOptionPane.showMessageDialog(null,"The argument is incorrect");
					}
				}else {
					JOptionPane.showMessageDialog(null,"The argument is incorrect");
				}
				break;

			case "rename":
				boolean ok3=true;
				try {
					args.get(0);
				}catch (Exception e) {
					ok3=false;
				}
				try {
					args.get(1);
				}catch (Exception e) {
					ok3=false;
				}
				if (ok3) {
					try {
					      int n = Integer.parseInt(args.get(0));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null,"The argument is incorrect");
						ok3=false;
					}
					if (ok3){
						Action renameaction = new Action();// {"action":"rename","object":"","objectClassName":null,"args":"target,newName"}
						System.out.println(object);
						if(object.equals("horizontalpool"))
						{
							renameaction.setType("rename_pool");
							renameaction.addId(args.get(0));
							System.out.println("label pool a rename "+args.get(1));
							renameaction.addId(args.get(1));
							sendAction(renameaction);
						}
						else
						{
							renameaction.setType("rename_item");
							renameaction.addId(args.get(0));
							renameaction.addId(args.get(1));
							sendAction(renameaction);
						}
					}
				}else {
					JOptionPane.showMessageDialog(null,"The argument is incorrect");
				}
				break;
				
			case "put":
				boolean ok4=true;
				try {
					args.get(0);
				}catch (Exception e) {
					ok4=false;
				}
				try {
					args.get(1);
				}catch (Exception e) {
					ok4=false;
				}
				if (ok4){
					try {
					      int n = Integer.parseInt(args.get(0));
					} catch (NumberFormatException e) {
						ok4=false;
					}
					try {
					      int n = Integer.parseInt(args.get(1));
					} catch (NumberFormatException e) {
						ok4=false;
					}
					if (ok4){
						Action putaction = new Action();
						putaction.setType("put");
						putaction.addId(args.get(0));
						putaction.addId(args.get(1));
						sendAction(putaction);
					}else {
						JOptionPane.showMessageDialog(null,"The argument is incorrect");
					}
				}else{
					JOptionPane.showMessageDialog(null,"The argument is incorrect");
				}
				break;
			}// f switch(request)
		}// f if(message != null)

	}// f fonction

	private void removeItemWithID(String type, String args, Action act) {
		act.setType(type);
		act.addId(args);
	}

	public void GenerateStartEvent(String type, String args, Action act) {
		Start start = new Start(args);
		System.out.println("creation d'un nouveau strat");
		act.setType(type);
		act.addObject(start);
	}

	public void GenerateEndEvent(String type, String args, Action act) {
		End end = new End(args);
		act.setType(type);
		act.addObject(end);
	}

	private void GenerateActivity(String type, String args, Action act) {
		Task task = new Task(args);
		act.setType(type);
		act.addObject(task);
	}

	private void GenerateEGateway(String type, String args, Action act) {
		SplitGateway gate = new SplitGateway();
		act.setType(type);
		act.addObject(gate);

	}

	private void GeneratePGateway(String type, String args, Action act) {
		
		JoinGateway gate = new JoinGateway();
		act.setType(type);
		act.addObject(gate);

	}

	private void GeneratePool(String type, String args, Action act) {
		act.addId(args);// le lbl est mis dans la liste des id qui sert de liste de parametres
		act.setType(type);
	}

	private String getIdfromLabel(String lbl) {
		Action idaction= new Action();
		String id = null;
		idaction.setType("Get Id from Label");
		idaction.addId(lbl);//encore une fois on utilise id comme liste de param
		sendAction(idaction);
		ACLMessage reply = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		if (reply!=null){
			id=reply.getContent();
		}
		return id;
	}

	private void sendAction(Action action) {
		String content = toJSON(action);
		System.out.println("envois de l action");
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("Graphe", AID.ISLOCALNAME));
		msg.setContent(content);
		myAgent.send(msg);
	}

	public HashMap<String, Object> toMap(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		try { // Convert JSON string to Map
			map = mapper.readValue(json, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public String toJSON(Action act) {
		String json = new String();
		ObjectMapper m = new ObjectMapper();
		try {
			json = m.writerWithDefaultPrettyPrinter().writeValueAsString(act);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
