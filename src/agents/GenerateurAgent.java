package agents;
import jade.core.Agent;

public class GenerateurAgent extends Agent
{
	protected void setup(){
		addBehaviour(new GenerateBehaviour(this));
		
	}

}
