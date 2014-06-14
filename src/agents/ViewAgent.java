package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.beans.PropertyChangeSupport;
import java.io.IOException;

import model.WorkFlow;
import view.View;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ViewAgent extends GuiAgent {
	public static int TEXT_SEND = 1;
	public static int WORKFLOW_SEND = 2;
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	private View view;
	public WorkFlow graph;

	@Override
	public void setup() {
		super.setup();
		view = new View(this);
		changes.addPropertyChangeListener(view);
		addBehaviour(new GraphBehaviour());
		addBehaviour(new InterpretorBehaviour());
	}

	@Override
	protected void onGuiEvent(GuiEvent e) {
		if (e.getType() == TEXT_SEND) {
			String message_content = e.getParameter(0).toString();
			System.out.println("Receive : " + message_content);
			if (message_content.contains("\n")) {
				System.out.println("para");
			}
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(new AID("Ecrit", AID.ISLOCALNAME));
			message.setContent(message_content);
			send(message);
		} else if (e.getType() == WORKFLOW_SEND) {
			System.out.println("open !!!");
			ObjectMapper mapper = new ObjectMapper();
			try {
				WorkFlow wf = (WorkFlow) e.getParameter(0);
				System.out.println("workflow cast !!!");
				ACLMessage message = new ACLMessage(ACLMessage.INFORM);
				String s = mapper.writeValueAsString(wf);
				message.setContent(s);
				message.addReceiver(new AID("Graphe", AID.ISLOCALNAME));
				send(message);
				System.out.println(message.getContent().toString());
				System.out.println("send modify graph to GraphAgent");
			} catch (JsonProcessingException e2) {
				e2.printStackTrace();
			}
		}
	}

	private class GraphBehaviour extends CyclicBehaviour {
		@Override
		public void action() {
			ACLMessage message = myAgent.receive(MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST));
			if (message != null) {
				System.out.println("reception graph");
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				try {
					graph = mapper.readValue(message.getContent(),
							WorkFlow.class);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("graph recu");
				view.refresh(graph);

			}
		}
	}

	private class InterpretorBehaviour extends CyclicBehaviour {
		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.INFORM);
			ACLMessage m = receive(mt);
			if (m != null) {
				System.out.println("reception interpretor");
				changes.firePropertyChange("message", null, m.getContent());
			}
		}
	}

}
