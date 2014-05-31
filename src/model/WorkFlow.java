package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;


public class WorkFlow {
	private ArrayList<Pool> Pools=new ArrayList<Pool>();
	private IdGenerator iter=new IdGenerator();
	int h=0; // hauteur de la fenetre
	int l=0; // largeur de la fenetre
	int nb_obj=0;
	int ecart_H=30;
	int ecart_L=30;
	
	public WorkFlow(int li,int hi)
	{
		h=hi;
		l=li;
		
	}
	
	// ajoute une Pool
	public void addPool(Pool p)
	{
		Pools.add(p);
	}
	
	// ajoute une nouvelle pool avec un label
	public void addNewPool(String s)
	{
		Pool p=new Pool(s,0,0, l);
		addPool(p);
		System.out.println(l);
	}
	
	// Permet d'ajouter un objet dans une pool /!\ il faut obligatoirement passer par cette m�thode
	public void addObject(int pool,ObjectBPMN o)
	{
		o.setId(iter.get_id());
		Pools.get(pool).AddObject(o);
		nb_obj++;
	}
	
	//retourne un objet dont l'id est pass� en param�tres
	public ObjectBPMN get_objet(int id)
	{
		for(int i=0;i<Pools.size();i++)
		{
			Pool p=Pools.get(i);
			for(int j=0;j<p.getObjects().size();j++)
			{
				if(p.getObjects().get(j).getId()==id) return p.getObjects().get(j);
			}
		}
		return null;
	}
	
	
	//retourne un objet dont la position et la ligne sont pass�s en parametre
	public ObjectBPMN get_objet(int ligne,int col)
	{
		int min=get_min_pos();
		for(int i=0;i<Pools.size();i++)
		{
			Pool p=Pools.get(i);
			for(int j=0;j<p.getObjects().size();j++)
			{
				if(p.getObjects().get(j).getLigne()==ligne && p.getObjects().get(j).getColone()==col+min) return p.getObjects().get(j);
			}
		}
		return null;
	}
	
	//retourne une selon l'objet dont l'id est pass� en param�tres
		public int get_pool_objet(int id)
		{
			for(int i=0;i<Pools.size();i++)
			{
				Pool p=Pools.get(i);
				for(int j=0;j<p.getObjects().size();j++)
				{
					if(p.getObjects().get(j).getId()==id) return i;
				}
			}
			return 0;
		}
	
	
		//retourne une pool selon son numero
		public Pool get_pool(int id)
		{
			return Pools.get(id);
		}
		
		//retourne une pool selon son label
		public Pool get_pool(String lbl)
		{
			for(int i=0;i<Pools.size();i++) if(Pools.get(i).getLabel()==lbl) Pools.get(i);
			return null;
		}
	
		public void retirer_objet(int id)// retire un objet selon son id
		{
			Pool p=get_pool(get_pool_objet(id));
			ObjectBPMN o=get_objet(id);
			ArrayList<ObjectBPMN> lst=o.getLinks_arrivant();
			for(int i=0;i<lst.size();i++)
			{
				unlinker(id,lst.get(i).getId());
			}
			
			ArrayList<ObjectBPMN> lst2=o.getLinks_partant();
			for(int i=0;i<lst2.size();i++)
			{
				unlinker(id,lst2.get(i).getId());
			}
			p.delete_obj(id);
			
			
		}
		
	// cr�� un lien entre deux objets 
	// source : id1 target : id2
	public void linker(int id1,int id2)
	{
		ObjectBPMN o1=get_objet(id1);
		ObjectBPMN o2=get_objet(id2);
		
		if(o1!=null && o2!=null && o1.linkable_partant() && o2.linkable_arrivant())
		{
			o1.linker_partant(o2);
			o2.linker_arrivant(o1);
		}
		else
		{
		// error
			System.out.println("Problem de linkage");
		}
	}
	
	// retire un lien entre deux objets
	// source : id1 target id2
	public void unlinker(int id1,int id2)
	{
		ObjectBPMN o1=get_objet(id1);
		ObjectBPMN o2=get_objet(id2);
		o1.unlink(o2);
		o2.unlink(o1);
	}
	
	// affichage
	
	public void affiche(Graphics g)
	{
		g.setColor(Color.black);
		for(int i=0;i<Pools.size();i++)
		{
			Pools.get(i).affiche(g);
			ArrayList<ObjectBPMN> Objects=Pools.get(i).getObjects();
			for(int j=0;j<Objects.size();j++)
			{
				Objects.get(j).affiche_link(g);
			}
			for(int j=0;j<Objects.size();j++)
			{
				Objects.get(j).affiche(g);
			}
			
			
		}
		
	}
	
	
	// optimisation de l'emplacement
	// 
	public void optimise()
	{
		
		ArrayList<ArrayList<ObjectBPMN>> Matrice = new ArrayList<ArrayList<ObjectBPMN>>();
		ArrayList<ObjectBPMN> Ligne;
		
		ObjectBPMN o=get_objet(1);
		
		Ligne=new ArrayList<ObjectBPMN>();
		Ligne.add(o);
		Matrice.add(Ligne);
		Matrice=opt(Matrice,o,Matrice.size()-1,0,0);
		
		place(Matrice);
		
		
		System.out.println(Matrice);
		
	}
	
	public boolean estdans(ArrayList<ArrayList<ObjectBPMN>> mat,ObjectBPMN o)
	{
		for(int i=0;i<mat.size();i++)
			for(int j=0;j<mat.get(i).size();j++)
				if(mat.get(i).get(j)==o) return true;
		return false;
	}
	
	public int estdansligne(ArrayList<ArrayList<ObjectBPMN>> mat,ObjectBPMN o)
	{
		for(int i=0;i<mat.size();i++)
			for(int j=0;j<mat.get(i).size();j++)
				if(mat.get(i).get(j)==o) return i;
		return -1;
	}
	
	public int estdanscol(ArrayList<ArrayList<ObjectBPMN>> mat,ObjectBPMN o)
	{
		for(int i=0;i<mat.size();i++)
			for(int j=0;j<mat.get(i).size();j++)
				if(mat.get(i).get(j)==o) return j;
		return -1;
	}
	
	public ArrayList<ArrayList<ObjectBPMN>> opt(ArrayList<ArrayList<ObjectBPMN>> mat,ObjectBPMN o,int l,int pos,int col)
	{
		ArrayList<ObjectBPMN> partant= o.getLinks_partant();
		ArrayList<ObjectBPMN> arrivant= o.getLinks_arrivant();
		
		if(o.getId()!=1)
		if(l==-1) 
		{   
			pos=0;
			ArrayList<ObjectBPMN> Ligne =  new ArrayList<ObjectBPMN>();
			Ligne.add(o);
			mat.add(Ligne);
			l=mat.size()-1;
			o.setColone(col);
			o.setLigne(l);
		}
		else
		{
			if(o.getId()==9)System.out.println("ok ligne "+l+ "pos "+ pos);
			mat.get(l).add(pos,o);
			o.setColone(col);
			o.setLigne(l);
		}
		
		boolean trouve=false;
		for(int j=0;j<arrivant.size();j++)
		{
		if(estdans(mat,arrivant.get(j))) trouve=true;
		}
		
		for(int j=0;j<arrivant.size();j++)
		{
			if(!estdans(mat,arrivant.get(j)))	
			{
				if(trouve || get_pool_objet(o.getId())!=get_pool_objet(arrivant.get(j).getId()))mat=opt(mat,arrivant.get(j),-1,0,col);
				else mat=opt(mat,arrivant.get(j),l,pos,col-1);
			}
		}
		
		
		
		for(int j=0;j<partant.size();j++)
		{
			if(!estdans(mat,partant.get(j)))
			if(partant.get(j)!=null && (j==0 && get_pool_objet(o.getId())==get_pool_objet(partant.get(j).getId()))) mat=opt(mat,partant.get(j),l,pos+1,col+1);
			else  mat=opt(mat,partant.get(j),-1,0,col);
				
		}
		return mat;
	}
	
	//renvois la valeur de la colone la plus basse (sachant qu'elle peut etre n�gative) le min est forcement <=0
	public int get_min_pos()
	{
		int min=0;
		for(int i=1;i<=nb_obj;i++) if(get_objet(i)!=null && get_objet(i).getColone()<min) min=get_objet(i).getColone();
		return min;
	}
	
	//renvois la valeur de la colone la plus haute 
		public int get_max_pos()
		{
			int max=0;
			for(int i=1;i<=nb_obj;i++) if(get_objet(i)!=null && get_objet(i).getColone()>max) max=get_objet(i).getColone();
			return max;
		}
	
	public int get_max_taille_col(int col) // renvois la taille max d'une colone
	{
		int max=0;
		for(int i=1;i<=nb_obj;i++) if(get_objet(i)!=null && get_objet(i).getColone()==col && get_objet(i).getL()>max) max=get_objet(i).getL(); // on garde la largeur max
		return max;
	}
	
	public int get_max_taille_ligne(ArrayList<ObjectBPMN> ligne) // retourne la hauteur d'une ligne
	{
		int max=0;
		for(int i=0;i<ligne.size();i++) if(ligne.get(i).getH()>max)max=ligne.get(i).getH();
		return max;
	}
	
	// ces deux variables sont les valeurs d'espacement entre lignes et colones
	protected int espace_h=30;
	protected int espace_l=30;
	
	// m�thode pour recuperer tous les objets d'une colones
	public ArrayList<ObjectBPMN> get_objects_col(int col)
	{
		ArrayList<ObjectBPMN> colone = new ArrayList<ObjectBPMN>();
		for(int i=1;i<=nb_obj;i++)
		{
			ObjectBPMN o=get_objet(i);
			if(o!=null && o.getColone()==col) colone.add(o);
		}
		
		return colone;
	}
	
	
	// Fonction modifiant les emplacements suite � l'optimisation
	public void place(ArrayList<ArrayList<ObjectBPMN>> mat)
	{
		int min = get_min_pos();
		int max = get_max_pos();
		ArrayList<Integer> Col_taille= new ArrayList<Integer>(); // permet de stocker la taille d'une colone
		ArrayList<Integer> H_ligne= new ArrayList<Integer>(); // permet de stocker la hauteur d'une ligne
		// calcul de largeur de chaque colone et les stock dans la liste
		for(int i=min;i<=max;i++)
		{
			Col_taille.add(get_max_taille_col(i));
		}
		//System.out.println("ttt "+Col_taille);
		
		for(int i=0;i<mat.size();i++) // on parcour les lignes pour avoir leur hauteurs
		{
			H_ligne.add(get_max_taille_ligne(mat.get(i)));
		}
		//System.out.println("hhh " +H_ligne);
		
		// on calcul la taille des pools
		ArrayList<Integer> taille_pool=new ArrayList<Integer>();
		
		// recup�ration de la liste des pools
		for(int i=0;i<mat.size();i++)
		{
			if(taille_pool.size()-1<get_pool_objet(mat.get(i).get(0).getId())) taille_pool.add(0);
		}
		
		for(int i=0;i<mat.size();i++)
		{
			int pool=get_pool_objet(mat.get(i).get(0).getId());
			taille_pool.set(pool, taille_pool.get(pool)+H_ligne.get(i)+ecart_H);
		}
		
		
		// placement des objets
		int posx=0;
		int posy=ecart_H;
		int poolactu=0;//pool actuelle
		
		for(int pool=0;pool<taille_pool.size();pool++) // parcour des pools
		for(int i=0;i<H_ligne.size();i++)// parcour des lignes
		{
			if(get_pool_objet(mat.get(i).get(0).getId())==pool)
			{
			posx=ecart_L+20;
			
			for(int j=0;j<Col_taille.size();j++)// parcour des colones
			{
				
				ObjectBPMN o=get_objet(i,j);
				if(o!=null)
				{
				if(o.getId()==12)System.out.println("hauteur : "+o.getY());
				o.setX(posx+(Col_taille.get(j)/2)-(o.getL()/2));
				o.setY(posy+(H_ligne.get(i)/2)-(o.getH()/2)-ecart_H/2); // le -ecart_H/2 permet de compenser l'�cart du haut tout en restant centr�
				}
				posx+=Col_taille.get(j)+ecart_L;//on ajoute la largeur de la colone
			}
			posy+=H_ligne.get(i)+ecart_H;// on ajoute la hauteur de la ligne
		
			}
		}
		
		// placement des pools
		posy=0;
		for(int i=0;i<taille_pool.size();i++)
		{
			get_pool(i).setH(taille_pool.get(i));
			System.out.println(taille_pool.get(i));
			get_pool(i).setY(posy);
			posy+=taille_pool.get(i);
		}
		
		
		// choix des priorit�s sur l'afffichage des liens dans une m�me colone
		ArrayList<ObjectBPMN> lst = new ArrayList<ObjectBPMN>();
		for(int k=min;k<=max;k++) {
			lst =get_objects_col(k);
			int prio=0;
			for(int i=0;i<lst.size();i++)
			{
				if(lst.get(i).getPrio()==-1)
				{
					lst.get(i).setPrio(prio);
					ArrayList<ObjectBPMN> olist=lst.get(i).getLinks_partant();
					for(int m=0;m<olist.size();m++)
						if(olist.get(m).getColone()==k)
						{
						olist.get(m).setPrio(prio);
						prio++;
						}					
				}
			}
		}
		
	}

	
	
	// Accesseur
	
	public ArrayList<Pool> getPools() {
		return Pools;
	}

	public void setPools(ArrayList<Pool> pools) {
		Pools = pools;
	}

	public IdGenerator getIter() {
		return iter;
	}

	public void setIter(IdGenerator iter) {
		this.iter = iter;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getNb_obj() {
		return nb_obj;
	}

	public void setNb_obj(int nb_obj) {
		this.nb_obj = nb_obj;
	}

	public int getEcart_H() {
		return ecart_H;
	}

	public void setEcart_H(int ecart_H) {
		this.ecart_H = ecart_H;
	}

	public int getEcart_L() {
		return ecart_L;
	}

	public void setEcart_L(int ecart_L) {
		this.ecart_L = ecart_L;
	}

	public int getEspace_h() {
		return espace_h;
	}

	public void setEspace_h(int espace_h) {
		this.espace_h = espace_h;
	}

	public int getEspace_l() {
		return espace_l;
	}

	public void setEspace_l(int espace_l) {
		this.espace_l = espace_l;
	}
	
	
}
