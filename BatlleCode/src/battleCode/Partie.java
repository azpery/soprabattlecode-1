package battleCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import ia.IA;


public class Partie {

	private int lvlmin = 1; 
	private int lvlmax = 6;
	
	private int coutRestant = 0; 
	
	private String idPartie; 
	private String idEquipe; 
   
	private String statut;
	
	private Bot botMiagic;  
	private Bot botAdverse;

	private IA ia; 
	private ArrayList<String> coupsAdv; 
	private ArrayList<String> coupsMia;
	
	String nomEquipeServeur = "Miagic Bot";
	
	public Partie(String idEquipe){
		
		this.idEquipe = idEquipe; 
	    
		botMiagic = new Bot(idEquipe, 0, 0, 0, false); 
		botAdverse = new Bot(idEquipe, 0, 0, 0, false); 
		
		coupsAdv = new ArrayList<String>(); 
		coupsMia = new ArrayList<String>(); 
		
		ia = new IA();
	
	}
	
	
	/*
	 * @author
	 * @description : Initialise une partie : soit un versus ou un practice 
	 * @pre :
	 * @post :
	 */
    public int initPartie() throws IOException{
    	
    	int i = -1 ;
    	while (i==-1)
    	{
    		System.out.println("Practice (0) ou Versus (1)");
    		Scanner sc = new Scanner(System.in);
    		i =  sc.nextInt();
    	}
    	
    	if(i == 0){
    		int lvl = -1; 
    		System.out.println(idEquipe);
    		
    		while(lvl<this.lvlmin||lvl>this.lvlmax){
    		System.out.println("Choix lvl compris entre " + lvlmin + ":" + lvlmax);
    		Scanner sc = new Scanner(System.in); 
    		lvl= sc.nextInt();
    		}
    		idPartie =  get("http://ec2-52-31-147-77.eu-west-1.compute.amazonaws.com/battle-ws/duel/practice/new/"+lvl+"/"+idEquipe);   	
    		System.out.println("id partie : " + idPartie);
    	}
    	
    	if(i == 1){
    		// On rejoind un versus // 
    	}
    
    	
    	return i; 
    	
    }	
	
public static String get(String url) throws IOException{
    	
    	String res=""; 
    	   
    	URL oracle = null;
		try {
			oracle = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
         BufferedReader in = new BufferedReader(
           new InputStreamReader(oracle.openStream()));

           String inputLine;
           while ((inputLine = in.readLine()) != null)
              res = inputLine; 
           in.close();
    	
    	return res; 
    }
    
    

   
    
    public String Jouer() throws InterruptedException, IOException{
    	
        statut="CANTPLAY";
    	String resPartie=""; 
    	
    	while(statut.equals("CANTPLAY"))
    	{
    	
    	System.out.println("WHILE CANTPLAY Statut partie = " + statut); 	
    	
    	Thread.sleep(100); 
  
    	majStatut(); 
	
        System.out.println("WHILE CANTPLAY : Statut partie = " + statut);
  
	
    	}
    	
    	
    	if(statut.equals("CANPLAY")){
    		
    		// Prise de la décision // 
    		String c = ia.devinerFuturCoup(this.botMiagic,this.botAdverse,coupsMia,coupsAdv,getDernierCoup(),coutRestant);
    		System.out.println("CANPLAY coup : on joue" + c);
    		// On attend // 
    		Thread.sleep(2500);
    		Thread.sleep(100);
    	
    		
    		try {
				System.out.println(getBoard());
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
			
    		
			// Prise de décision // 
			coup(c);
			coupsMia.add(c);
			
			
    		
			System.out.println("Coup joué :" +c);
			
    	}
        
        	
    	return statut; 
    } // Fin jouer // 
    
 // RECUPERATION DONNES DE JEUX  + TRAITEMENTS  INFORMATIONS 
    
   // Parseur //  
	public String[] Parser() throws IOException{
		
		String datas = getBoard();
		String delims = "[;]";
		String[] tokens = datas.split(delims);
		
		return tokens; 
	}
	
	// Traitement Partie practice // 
    /*
     * @pre Appeler majStatut() avant // 
     * @warning ATTENTION CETTE METHODE EST A MODIFIER SI LE FORMAT CHANGE DANS LE PRACTICE // 
     * @description : On suppose que le premier robot à jouer est le notre // 
     */
    public void majRobots() throws IOException, InterruptedException{
    	
    	String[] tokens; 
    	tokens= Parser();
    	
    	System.out.println("\n \n Joueur : " + tokens[0]);
    	Thread.sleep(1000);
    	
    	if(tokens[0].equals(nomEquipeServeur))
    	{
    		
        System.out.println("\n \n Dans Joueur nous sommes J1 \n\n\n");
    	// MAJ VALEURS DE NOTRE BOT  // 
    	botMiagic.setNumJoeur("Joueur 1");
    	
    	botMiagic.setNbVies(tokens[1]);
    	botMiagic.setNbBombe(tokens[2]);
    	botMiagic.setNbBullet(tokens[3]);
    	
    	botMiagic.setNbBouclier(tokens[4]);
    	
    	// MAJ VALEURS DE l'ADVERSAIRE PRACTICE // 
    	botAdverse.setNumJoeur("Joueur 2");
    	
    	botAdverse.setNbVies(tokens[6]);
    	botAdverse.setNbBombe(tokens[7]);
    	botAdverse.setNbBullet(tokens[8]);
    	
    	botAdverse.setNbBouclier(tokens[9]);
    	
    	this.coutRestant = Integer.parseInt(tokens[10]);
    	}
    	
    	else if(tokens[5].equals(nomEquipeServeur))
    	{
    		
    		
            System.out.println("\n \n Dans Joueur nous sommes J2 \n\n\n");	
    		
        	// MAJ VALEURS DE l'ADVERSAIRE PRACTICE // 
            botAdverse.setNumJoeur("Joueur 1");
        	
            botAdverse.setNbVies(tokens[1]);
            botAdverse.setNbBombe(tokens[2]);
            botAdverse.setNbBullet(tokens[3]);
        	
            botAdverse.setNbBouclier(tokens[4]);
        	
        	// MAJ VALEURS DE l'ADVERSAIRE PRACTICE // 
        	botMiagic.setNumJoeur("Joueur 2");
        	
        	botMiagic.setNbVies(tokens[6]);
        	botMiagic.setNbBombe(tokens[7]);
        	botMiagic.setNbBullet(tokens[8]);
        	
        	botMiagic.setNbBouclier(tokens[9]);
        	
        	this.coutRestant = Integer.parseInt(tokens[10]);
    		
    	}
    }
    
    
    public  void majStatut() throws IOException{
    	statut =  get("http://ec2-52-31-147-77.eu-west-1.compute.amazonaws.com/battle-ws/duel/game/status/"+idPartie+"/"+idEquipe);
    }
    
    public  String getBoard() throws IOException{
    	System.out.println("Dans get board"); 
    	return get("http://ec2-52-31-147-77.eu-west-1.compute.amazonaws.com/battle-ws/duel/game/board/"+idPartie+"?format=String)");
    }
    
    
    public String getDernierCoup() throws IOException{
    
    	String s=  get("http://ec2-52-31-147-77.eu-west-1.compute.amazonaws.com/battle-ws/duel/game/getlastmove/"+idPartie+"/"+idEquipe);
    	System.out.println("\n\n\n\n\n DERNIER COUP JOUE " + s);
    	return s; 
    }
    
    public String coup(String c) throws IOException{
    	
    	
    	String coup = c;
    	
    	System.out.println(get("http://ec2-52-31-147-77.eu-west-1.compute.amazonaws.com/battle-ws/duel/game/play/"+idPartie+"/"+idEquipe+"/"+coup));
    	
	    return coup;

    }
    
	
    
    /* Requests */ 
    
    
    
    
    
    
    
    
	
	/* Getters and Setters */ 
	
	public void setId(String id) {
		this.idPartie = id;
	}
	public String getId() {
		return idPartie;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getStatut() {
		return statut;
	} 
	
	/* Affichage */ 
	

	public String toString(){
		
		
		
		String s = "\n---------------------------------------------\n";
		s+= "\n" + this.botMiagic; 
		s+= "\n" +  this.botAdverse; 
		s += "\n---------------------------------------------\n";
		
		return s; 
	}
	
}
