package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import model.Action;
import model.ObjectBPMN;
import model.Pool;
import model.WorkFlow;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GrapheAgent extends Agent {
	WorkFlow wf = new WorkFlow(790, 500);

	@Override
	protected void setup() {
		addBehaviour(new GraphBehaviour());
		addBehaviour(new startBehaviour());
		addBehaviour(new GetModifyGraph());

	}

	private class startBehaviour extends OneShotBehaviour {

		@Override
		public void action() {

			// envois du graph de départ vers un agent d'affichage
			ObjectMapper mapper2 = new ObjectMapper();
			try {
				ACLMessage message_reply = new ACLMessage(ACLMessage.REQUEST);

				String s = mapper2.writeValueAsString(wf);
				message_reply.setContent(s);
				message_reply.addReceiver(new AID("Vue", AID.ISLOCALNAME));
				System.out.println("message:");
				System.out.println(message_reply.getContent());
				myAgent.send(message_reply);
				System.out.println("send to view");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}

	}

	private class GraphBehaviour extends CyclicBehaviour {
		@Override
		public void action() {
			Boolean maj = false;
			ACLMessage message = myAgent.receive(MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST));
			if (message != null) {
				System.out.println("reception d action");
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				// récupération d'une action
				try {
					// act est l'action envoyées
					Action act = mapper.readValue(message.getContent(),
							Action.class);

					switch (act.getType()) {
					case "pool":
						wf.addNewPool(act.getId().get(0));
						maj = true;
						break;
					case "create":
						wf.addObject(act.getObjects().get(0));
						maj = true;
						break;
					case "remove_item":
						if (Integer.parseInt(act.getId().get(0)) != 1) {
							for (int i = 0; i < act.getId().size(); i++) {
								wf.retirer_objet(Integer.parseInt(act.getId()
										.get(i)));
								maj = true;
							}
						} else {
							JOptionPane.showMessageDialog(null,
									"You can't delete the initial object.");
						}

						break;
					case "remove_pool":
						if (Integer.parseInt(act.getId().get(0)) != 0) {

							wf.retirer_pool(Integer
									.parseInt(act.getId().get(0)));
							maj = true;
						} else {
							JOptionPane.showMessageDialog(null,
									"You can't delete the initial pool.");
						}

						break;

					case "connect":
						wf.linker(Integer.parseInt(act.getId().get(0)),
								Integer.parseInt(act.getId().get(1)));
						maj = true;
						break;
					case "put":
						wf.change_pool(Integer.parseInt(act.getId().get(0)),
								Integer.parseInt(act.getId().get(1)));
						maj = true;
						break;
					case "unconnect":
						wf.unlinker(Integer.parseInt(act.getId().get(0)),
								Integer.parseInt(act.getId().get(1)));
						maj = true;
						break;
					case "rename_item":
						// System.out.println("pool a rename : "+act.getId().get(1));
						wf.get_objet(Integer.parseInt(act.getId().get(0)))
								.setLabel(act.getId().get(1));
						maj = true;
						break;
					case "rename_pool":
						wf.get_pool(Integer.parseInt(act.getId().get(0)))
								.setLabel(act.getId().get(1));
						maj = true;
						break;
					case "get_label":
						// si on demande au graph de renvoyer un objet grace à
						// son label
						ObjectBPMN o = wf.get_objet(act.getId().get(0));
						ObjectMapper mapper2 = new ObjectMapper();
						try {
							ACLMessage message_reply = message.createReply();
							message_reply.setPerformative(ACLMessage.REQUEST);

							String s = mapper2.writeValueAsString(o);
							message_reply.setContent(s);
							myAgent.send(message_reply);

						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						break;
					case "get_pool":
						// même chose mais avec une pool
						Pool o2 = wf.get_pool(act.getId().get(0));
						ObjectMapper mapper3 = new ObjectMapper();
						try {
							ACLMessage message_reply = message.createReply();
							message_reply.setPerformative(ACLMessage.REQUEST);

							String s = mapper3.writeValueAsString(o2);
							message_reply.setContent(s);
							myAgent.send(message_reply);

						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						break;

					}

					// envois d'un message à l'optimisateur pour faire une mise
					// a jour si besoin
					if (maj) {

						// envois du résultat vers un agent d'affichage
						ObjectMapper mapper2 = new ObjectMapper();
						ACLMessage message_reply = new ACLMessage(
								ACLMessage.REQUEST);

						String s = mapper2.writeValueAsString(wf);
						message_reply.setContent(s);
						message_reply.addReceiver(new AID("Optimisateur",
								AID.ISLOCALNAME));
						myAgent.send(message_reply);
						System.out
								.println("Message envoyé a partir du graph vers opti");
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	private class GetModifyGraph extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = myAgent.receive(MessageTemplate
					.MatchPerformative(ACLMessage.INFORM));
			if (message != null) {
				System.out.println("reception graph re�ut");
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				try {
					wf = mapper.readValue(message.getContent(), WorkFlow.class);
					
					// envoit du résultat vers un agent d'affichage
					ObjectMapper mapper2 = new ObjectMapper();
					ACLMessage message_reply = new ACLMessage(
							ACLMessage.REQUEST);

					String s = mapper2.writeValueAsString(wf);
					message_reply.setContent(s);
					message_reply.addReceiver(new AID("Optimisateur",
							AID.ISLOCALNAME));
					myAgent.send(message_reply);
					
					System.out.println("workflow modified !");
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

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

	public String toJSON(WorkFlow act) {
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
