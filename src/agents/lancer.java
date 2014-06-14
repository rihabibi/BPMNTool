package agents;

import jade.core.ProfileImpl;
import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class lancer 
{
	public static String SECONDARY_FILE = "doc/SECONDPROP";
	public lancer()
	{
		Runtime rt = Runtime.instance();
		Profile p = null;
		try
		{
			p = new ProfileImpl(SECONDARY_FILE);
			ContainerController cc = rt.createAgentContainer(p);
			AgentController ac1 = cc.createNewAgent("Ecrit", "agents.EcritAgent",null);
			AgentController ac2 = cc.createNewAgent("Interpreteur", "agents.InterpreteurAgent",null);
			AgentController ac3 = cc.createNewAgent("Generateur", "agents.GenerateurAgent",null);
			AgentController ac4 = cc.createNewAgent("Vue", "agents.ViewAgent",null);
			AgentController ac5 = cc.createNewAgent("Graphe", "agents.GrapheAgent",null);
			AgentController ac6 = cc.createNewAgent("Optimisateur", "agents.OptimisateurAgent",null);
			ac5.start();
			ac6.start();
			Thread.sleep(1000);
			ac1.start();
			ac2.start();
			ac3.start();
			ac4.start();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("erreur Second Boot");
		}
	}
}
