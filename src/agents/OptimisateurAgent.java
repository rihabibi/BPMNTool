package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

import model.WorkFlow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OptimisateurAgent extends Agent {
	@Override
	protected void setup() {
		addBehaviour(new OptimisateurBehaviour());
	}

	private class OptimisateurBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = myAgent.receive(MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST));
			if (message != null) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				// récupération du workflow non opti
				try {
					WorkFlow wf = mapper.readValue(message.getContent(),
							WorkFlow.class);
					wf.optimise();

					// envois du résultat vers un agent d'affichage
					ObjectMapper mapper2 = new ObjectMapper();
					try {
						ACLMessage message_reply = new ACLMessage(
								ACLMessage.INFORM);

						String s = mapper2.writeValueAsString(wf);
						message_reply.setContent(s);
						message_reply.addReceiver(new AID("Vue",
								AID.ISLOCALNAME));
						myAgent.send(message_reply);

					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}
}
