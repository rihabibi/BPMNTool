package agents;

import java.beans.PropertyChangeSupport;

import view.View;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class ViewAgent extends GuiAgent 
{
	public static int TEXT_SEND = 1;
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	private View view;
	
	public void setup() 
	{
		super.setup();
		view = new View(this);
	}
	
	protected void onGuiEvent(GuiEvent e) 
	{
		if (e.getType() == TEXT_SEND) 
		{
			String message_content = e.getParameter(0).toString();
			System.out.println("Receive : " + message_content);
			if (message_content.contains("\n"))
				System.out.println("para");
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(new AID("Ecrit",AID.ISLOCALNAME));

			message.setContent(message_content);
			send(message);
		}
	}

}
