package agents;

import java.io.IOException;
import java.util.HashMap;

import model.Action;
import model.ObjectBPMN;
import model.Pool;
import model.WorkFlow;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GrapheAgent extends Agent {
WorkFlow wf;
	protected void setup(){
		addBehaviour(new GraphBehaviour());
		
	}
	
	private class CreationGraph extends OneShotBehaviour
	{
		public void action() {
			wf=new WorkFlow(800,600);			
		}
		
	}
	
	private class GraphBehaviour extends CyclicBehaviour
	{
		public void action() {
			Boolean maj=false;
			ACLMessage message = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
			if (message != null)
			{
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				//récupération d'une action
				try {
					// act est l'action envoyées
					Action act = mapper.readValue(message.getContent(), Action.class);
					
					switch(act.getType())
					{
					case "create":
						for(int i=0;i<act.getObjects().size();i++) wf.addObject(act.getObjects().get(i));
						maj=true;
						break;
					case "remove":
						for(int i=0;i<act.getId().size();i++) wf.retirer_objet(Integer.parseInt(act.getId().get(i)));
						maj=true;
						break;				
					case "connect":
						wf.linker(Integer.parseInt(act.getId().get(0)), Integer.parseInt(act.getId().get(1)));
						maj=true;
						break;
					case "unconnect":
						wf.unlinker(Integer.parseInt(act.getId().get(0)), Integer.parseInt(act.getId().get(1)));
						maj=true;
						break;
					case "get_label":
						// si on demande au graph de renvoyer un objet grace à son label
						ObjectBPMN o=wf.get_objet(act.getId().get(0));
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
						Pool o2=wf.get_pool(act.getId().get(0));
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
					
					
					// envois d'un message à l'optimisateur pour faire une mise a jour si besoin 
					if(maj)
					{
						WorkFlow wf = mapper.readValue(message.getContent(), WorkFlow.class);
						wf.optimise();
						
						
						// envois du résultat vers un agent d'affichage
						ObjectMapper mapper2 = new ObjectMapper();
						try {
							ACLMessage message_reply = new ACLMessage(ACLMessage.REQUEST);
							
							String s = mapper2.writeValueAsString(wf);
							message_reply.setContent(s);
							message_reply.addReceiver(new AID("Optimisateur",AID.ISLOCALNAME));
							myAgent.send(message_reply);
							
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	
	
	
	
	
	
	public HashMap<String,String> toMap(String json) 
	{
		HashMap<String,String> map = new HashMap<String,String>();
		ObjectMapper mapper = new ObjectMapper();
		try 
		{	// Convert JSON string to Map
			map = mapper.readValue(json,HashMap.class);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return map;
	}
	
	public String toJSON (Action act) 
	{
		String json = new String();
		ObjectMapper m = new ObjectMapper();
		try 
		{
			json= m.writeValueAsString(act);
		} 
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
		}
		return json;
	}
}
