package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

import model.Action;
import model.End;
import model.JoinGateway;
import model.Pool;
import model.SplitGateway;
import model.Start;
import model.Task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateBehaviour extends CyclicBehaviour {

	public GenerateBehaviour(GenerateurAgent a) {
		super(a);
	}

	@Override
	public void action() {
		ACLMessage message = myAgent.receive(/*
											 * MessageTemplate.MatchPerformative(
											 * ACLMessage.REQUEST)
											 */);
		if (message != null) {
			// Deserialisation du message
			String Json = message.getContent();
			HashMap<String, String> map = toMap(Json);

			// extraction des valeurs
			String request = map.get("action");
			String object = map.get("object");
			String objectClassName = map.get("objectClassName");
			String args = map.get("args");

			switch (request) {
			case "create":
				Action addaction = new Action();
				switch (object) {
				case "start event":
					GenerateStartEvent(request, args, addaction);
					sendAction(addaction);
					break;

				case "end event ":
					GenerateEndEvent(request, args, addaction);
					sendAction(addaction);
					break;

				case "activity":
					GenerateActivity(request, args, addaction);
					sendAction(addaction);
					break;

				case "pool":
					GeneratePool(request, args, addaction);
					sendAction(addaction);
					break;

				case "parallel gateway":
					GeneratePGateway(request, args, addaction);
					sendAction(addaction);
					break;

				case "exclusive gateway":
					GenerateEGateway(request, args, addaction);
					sendAction(addaction);
					break;

				}
				break;

			case "remove":
				Action removeaction = new Action();// A noter que les fct
													// removes sont en fait
													// removeWithId() et non
													// removeWithLabel()
				// factoriser ttes les methodes remove en removeItemWithID()
				// removeItemWithLabel()
				switch (object) {
				case "start event":
					RemoveStartEvent(request, args, removeaction);
					sendAction(removeaction);
					break;

				case "end event":
					RemoveEndEvent(request, args, removeaction);
					sendAction(removeaction);
					break;

				case "activity":
					RemoveActivity(request, args, removeaction);
					sendAction(removeaction);
					break;

				case "pool":
					RemovePool(request, args, removeaction);
					sendAction(removeaction);
					break;

				case "parallel gateway":
					RemovePGateway(request, args, removeaction);
					sendAction(removeaction);
					break;

				case "exclusive gateway":
					RemoveEGateway(request, args, removeaction);
					sendAction(removeaction);
					break;
				}
				break;

			case "connect":
				Action connectaction = new Action();
				String[] param = args.split(",");
				connectaction.setType(request);
				for (int i = 0; i < param.length; i++) {
					connectaction.addId(param[i]);
				}
				sendAction(connectaction);
				break;

			}// f switch(request)
		}// f if(message != null)

	}// f fonction

	private void RemoveEndEvent(String type, String args, Action act) {
		act.setType(type);
		act.addId(args);
	}

	private void RemoveStartEvent(String type, String args, Action act) {
		act.setType(type);
		act.addId(args);
	}

	private void RemoveEGateway(String type, String args, Action act) {
		act.setType(type);
		act.addId(args);
	}

	private void RemovePGateway(String type, String args, Action act) {
		act.setType(type);
		act.addId(args);
	}

	private void RemovePool(String type, String args, Action act) {
		act.setType(type);
		act.addId(args);
	}

	private void RemoveActivity(String type, String args, Action act) {
		act.setType(type);
		act.addId(args);
	}

	public void GenerateStartEvent(String type, String args, Action act) {
		Start start = new Start(args);
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
		JoinGateway gate = new JoinGateway();
		act.setType(type);
		act.addObject(gate);

	}

	private void GeneratePGateway(String type, String args, Action act) {
		SplitGateway gate = new SplitGateway();
		act.setType(type);
		act.addObject(gate);

	}

	private void GeneratePool(String type, String args, Action act) {
		String[] param = args.split(",");
		Pool p = new Pool(param[0], Integer.parseInt(param[1].trim()),
				Integer.parseInt(param[2].trim()), Integer.parseInt(param[3]
						.trim()));
		act.setType(type);
		// act.addObject(p);
	}

	private void sendAction(Action action) {
		String content = toJSON(action);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("Graphe", AID.ISLOCALNAME));
		msg.setContent(content);
		myAgent.send(msg);
	}

	public HashMap<String, String> toMap(String json) {
		HashMap<String, String> map = new HashMap<String, String>();
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
			json = m.writeValueAsString(act);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
