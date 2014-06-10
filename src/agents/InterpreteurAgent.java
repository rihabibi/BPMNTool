package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Interpretor;
import model.SentenceModel;

public class InterpreteurAgent extends Agent 
{
	private Interpretor interprete = new Interpretor();

	@Override
	protected void setup() 
	{
		addBehaviour(new InterpreteBehaviour());
	}

	private class InterpreteBehaviour extends Behaviour 
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void action() 
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage m = receive(mt);
			if (m != null)
			{
				String verif="ERROR";
				SentenceModel sm = interprete.analyzeSentence(m.getContent());
				if (sm!=null)
				{
					ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
					message.addReceiver(new AID("Generateur", AID.ISLOCALNAME));
					message.setContent(sm.toJSON());
					send(message);
					System.out.println(sm.toJSON());
					verif="OK";
				}
				else 
				{

					System.out.println("erreur format de message");
				}
				ACLMessage message = new ACLMessage(ACLMessage.INFORM);
				message.addReceiver(new AID("Vue", AID.ISLOCALNAME));
				message.setContent(verif);
				send(message);
				
			} else 
			{
				block();
			}
		}

		@Override
		public boolean done() 
		{
			return false;
		}
	}

}