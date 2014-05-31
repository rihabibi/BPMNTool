package agents;
import model.Interpretor;
import model.SentenceModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class InterpreteurAgent extends Agent 
{
	private Interpretor interprete = new Interpretor();

	protected void setup()
	{
		addBehaviour(new InterpreteBehaviour());
	}
	
	private class InterpreteBehaviour extends Behaviour
	{
		private static final long serialVersionUID = 1L;

		public void action()
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage m= receive(mt); 
			if(m!=null)
			{
				SentenceModel sm = interprete.analyzeSentence(m.getContent());
				ACLMessage message= new ACLMessage(ACLMessage.REQUEST);
				message.addReceiver(new AID("Generateur",AID.ISLOCALNAME));
				message.setContent(sm.toJSON());
				send(message);
				System.out.println(sm.toJSON());
			}
			else 
			{
				block();
			}
		}

		public boolean done()
		{
			return false;
		}
	}
	
}