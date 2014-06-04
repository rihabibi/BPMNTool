package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EcritAgent extends Agent {

	@Override
	protected void setup() {
		addBehaviour(new EcritBehaviour());
	}

	private class EcritBehaviour extends Behaviour {
		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage m = receive(mt);
			if (m != null) {
				System.out.println("message received");
				String message = m.getContent();
				String[] phrases = message.split("[.]");
				int i;
				for (i = 0; i < phrases.length; i++) {
					// System.out.println(phrases[i]);
					ACLMessage mess = new ACLMessage(ACLMessage.REQUEST);
					mess.addReceiver(new AID("Interpreteur", AID.ISLOCALNAME));
					mess.setContent(phrases[i]);
					send(mess);
				}
			} else {
				block();
			}
		}

		@Override
		public boolean done() {
			return false;
		}
	}
}
