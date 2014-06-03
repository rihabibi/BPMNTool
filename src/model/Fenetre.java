package model;


import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
 /*
public class Fenetre extends JFrame {
  public Fenetre(){                
    this.setTitle("BPMNToolGraph");
    this.setSize(800,600);
    this.setLocationRelativeTo(null);               
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    
    WorkFlow wf=new WorkFlow(800,600);
    
    Task sp= new Task("Je suis tristan et je test un truc");
    Task sp2= new Task("Test2");
    Task sp3= new Task("Test3");
    
    Start st= new Start();
    End nd=new End();
    End nd2=new End();
    JoinGateway jn=new JoinGateway();
    SplitGateway spl=new SplitGateway();
 
 
    wf.addNewPool("ok");
  
    wf.addNewPool("ok2");
  
    wf.addObject(0, st);
	wf.addObject(0, sp);
	wf.addObject(0, sp2);
	
	wf.addObject(1, nd);
	wf.addObject(0, jn);
	wf.addObject(0, spl);
	wf.addObject(0, sp3);
	
	Task stt=new Task("Test8");
	Task stt2=new Task("Test9");
	wf.addObject(1, stt);
	wf.addObject(1, stt2);
	Task sp6= new Task("Test10");
	
	wf.addObject(0, sp6);
	
	
	wf.linker(1, 5);
	wf.linker(5, 2);
	wf.linker(8, 5);
	wf.linker(2, 6);
	wf.linker(3, 6);
	
	wf.linker(6, 10);
	wf.linker(3, 4);
	wf.linker(9, 8);
	//wf.linker(7, 10);
	

	wf.addObject(0, nd2);
	wf.linker(10, 7);
	wf.linker(7, 11);
	
	
	Task sta2= new Task("taskTest");
    wf.addObject(1,sta2);
	wf.linker(12, 9);
	
	
	Task sta3= new Task("Entre 13 et 3");
    wf.addObject(0,sta3);
    wf.linker(6, 13);
	wf.linker(13, 3);
	//wf.retirer_objet(10);
	System.out.println("taille ::"+wf.get_objet(7).getLinks_partant().size());
    Panneau pan=new Panneau(wf);
    this.setContentPane(pan);

    wf.optimise();
 
   // System.out.println(stt2.getColone());
    this.setVisible(true);
  }     
}

*/