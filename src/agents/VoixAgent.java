package agents;

import voice.Voice;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class VoixAgent extends Agent 
{
	Voice voicemanager;
	protected void setup() 
	{
		voicemanager =new Voice();
		addBehaviour(new VoiceBehaviour());
	}
	
	private class VoiceBehaviour extends CyclicBehaviour 
	{
		@Override
		public void action() 
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage m = receive(mt);
			if (m != null)
			{
				if(m.getContent().equals("ON"))
				{
					System.out.println("Voice ON (received by agent)");
					voicemanager.startRec();
				}
				else if (m.getContent().equals("OFF")) 
				{
					System.out.println("Voice OFF (received by agent)");
					String result =voicemanager.stopRec();
					System.out.println(result);
					
					ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
					message.addReceiver(new AID("Ecrit", AID.ISLOCALNAME));
					message.setContent(result);
					send(message);
					
					ACLMessage message2 = new ACLMessage(ACLMessage.INFORM);
					message2.addReceiver(new AID("Vue", AID.ISLOCALNAME));
					message2.setContent(result);
					send(message2);
				}
			}
		}
	}
}
