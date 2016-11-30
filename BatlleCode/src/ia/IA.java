package ia;

import java.util.ArrayList;


import battleCode.Bot;




public class IA {
	private ArrayList<Schema> schemas;
	private ArrayList<Schema> schemasTrois;
	private ArrayList<Schema> schemasDeux;
	private ArrayList<Schema> schemasUn;
	private ArrayList<Schema> schemasZero;
	private int pdvCritique = 4;  // Niveau critique à éviter // 


	public IA(){
		this.schemas = new ArrayList<Schema>();
		this.schemasTrois = new ArrayList<Schema>();
		this.schemasDeux = new ArrayList<Schema>();
		this.schemasUn = new ArrayList<Schema>();
		this.schemasZero = new ArrayList<Schema>();

	}





	public boolean devinerCover(Bot miage,Bot adve,ArrayList<String> coupsMiagic,ArrayList<String> coupsAdv,String dernierCoup){

		boolean res = false;  


		return res; 
	}

	public boolean devinerShoot(Bot miage,Bot adve,ArrayList<String> coupsMiagic,ArrayList<String> coupsAdv,String dernierCoup){

		boolean res = false;  


		return res; 
	}

	public boolean devinerAIM(Bot miage,Bot adve,ArrayList<String> coupsMiagic,ArrayList<String> coupsAdv,String dernierCoup){

		boolean res = false;  


		return res; 
	}

	public boolean devinerReload(Bot miage,Bot adve,ArrayList<String> coupsMiagic,ArrayList<String> coupsAdv,String dernierCoup){

		boolean res = false;  


		return res; 
	}


	public String devinerFuturCoup(Bot miage,Bot adve,ArrayList<String> coupsMiagic,ArrayList<String> coupsAdv,String dernierCoup,int nbCoupRestant)
	{
		String c = "NA"; 
		String avDernierCoupM = "NA";
		String avDernierCoupA = "NA"; 
		this.schemas = new ArrayList<Schema>();
		this.schemasZero = new ArrayList<Schema>();
		this.schemasUn = new ArrayList<Schema>();
		this.schemasDeux = new ArrayList<Schema>();
		this.schemasTrois = new ArrayList<Schema>();
		if(!dernierCoup.equals("NA"))
		{
			coupsAdv.add(dernierCoup); 
		}

		if(coupsAdv.size() > 2){
			Schema cible = new Schema(dernierCoup, coupsAdv.get(coupsAdv.size() - 2), coupsMiagic.get(coupsMiagic.size() - 1));
			//this.schemas.add(cible);
			int i ;
			for(i = 1; i <= coupsAdv.size() - 2; i++){
				if(coupsAdv.get(i).equals(dernierCoup)){
					Schema s = new Schema(coupsAdv.get(i),coupsAdv.get(i-1),coupsMiagic.get(i));
					s.setCoupSuivant(coupsAdv.get(i+1));
					this.schemas.add(s);
				}
			}
			for(i = 0 ; i <= this.schemas.size() - 1 ; i++){
				Schema leSchema = this.schemas.get(i);
				leSchema.comparer(cible);
				switch(this.schemas.get(i).getPuissance()){
				case 3:
					this.schemasTrois.add(leSchema);
					break;
				case 2:
					this.schemasDeux.add(leSchema);
					break;
				case 1: 
					this.schemasUn.add(leSchema);
					break;
				case 0:
					this.schemasZero.add(leSchema);
					break;
				}

			}
			if(this.schemasTrois.size()>0){
				c = Schema.comparerValeurCoupsSuivant(this.schemasTrois, adve);
			}  
			if(this.schemasDeux.size()>0&&c=="NA"){
				c = Schema.comparerValeurCoupsSuivant(this.schemasDeux, adve);
			}  
			if(this.schemasUn.size()>0&&c=="NA"){
				c = Schema.comparerValeurCoupsSuivant(this.schemasUn, adve);
			}  
			if(this.schemasZero.size()>0&&c=="NA"){
				c = Schema.comparerValeurCoupsSuivant(this.schemasZero, adve);
			} 

		}

		// SI DEFAUT // 

		if(c == "NA"){
			if(adve.getNbBombe()>0&&dernierCoup.equals("BOMB")){
				c = "BOMB"; 
			}
			else if(adve.getNbBullet()>0)
			{	
				if( dernierCoup.equals("AIM")){
					c = "SHOOT"; 
				}

				if( dernierCoup.equals("RELOAD")){
					c = "AIM"; 
				}
			}
			else if(adve.getNbBullet()==0 ){
				if(coupsMiagic.size()>0){
					if(!coupsMiagic.get(coupsMiagic.size()-1).equals("AIM")){
						c= "RELOAD";
					}
					else if(adve.getNbBouclier()>0){
						c= "COVER";
					}
				}
				else{
					c= "RELOAD";
				}	
			}
		}
//Fin du defaut//
		System.out.println("\n FUTUR COUP JOUER PAR ADV :" + c);

		String dernierCoupMiage = "";
		if(coupsMiagic.size()>0){
			dernierCoupMiage =coupsMiagic.get(coupsMiagic.size()-1);
		}
		else{
			dernierCoupMiage = "NA";
		}

		
		if(coupsMiagic.size()>1){
			avDernierCoupM = coupsMiagic.get(coupsMiagic.size()-2); 
		}
		if(coupsAdv.size()>1){
			avDernierCoupA = coupsAdv.get(coupsMiagic.size()-2); 
		}
		
		c = deciderAction(avDernierCoupA,dernierCoup,c,avDernierCoupM,dernierCoupMiage,miage,adve);

		System.out.println("ON VA JOUER : " + c);

		return  c; 
	}



	public String deciderAction(String avdernierAdv,String dernierAdv,String predAdv,String avdernierMia,String dernierMia,Bot miage,Bot adve){
		String s =""; 
		boolean decisionPrise = false; 

		boolean shootMia 	  = false; 
		boolean coverMia	  = false; 

		boolean coverAdv 	  = false; 
        

		if(miage.getNbBullet()>0){
			shootMia = true; 
		}
		if(miage.getNbBouclier()>0){
			coverMia = true; 
		}
		if(adve.getNbBouclier()>0){
			coverAdv = true; 
		}
		
        
		
		if(predAdv.equals("BOMB")){
			if(shootMia){
			s = "SHOOT";
			decisionPrise = true; 
			}	
		}		
         /// Partie protection bombe // 
		if(avdernierAdv.equals("BOMB")&&!avdernierMia.equals("SHOOT")&&coverMia){
			s = "COVER"; 
			decisionPrise=true; 			
		}
		if(dernierAdv.equals("BOMB")&&!dernierMia.equals("SHOOT")&&miage.getNbBouclier()>2){
			s = "COVER"; 
			decisionPrise=true; 			
		}
		// 
		if(dernierMia.equals("AIM")&&dernierAdv.equals("SHOOT")&&adve.getNbVies()>=this.pdvCritique&&!decisionPrise){
			s = "AIM"; 
			decisionPrise = true; 

		}

		// SI NOTRE ADVERSAIRE VA TIRER // 
		if(predAdv.equals("SHOOT")&&!decisionPrise)
		{

			// SOUS CONTRAITES -> TIRER + CACHER // 

			// ****** COVER ***** // 
			// Pour se cacher -> Contrainte de bouclier // 

			// Pour se cacher il faut absolument un bouclier // 

			if(coverMia){
				// On peut se cacher //	
				// On se pose la question si on doit se cacher // 
				// Manque de vie | Si l'autre IA à visé // 
				if(miage.getNbVies()<=this.pdvCritique){
					s = "COVER";
					decisionPrise=true; 
				}
				else if(dernierAdv.equals("AIM")&&dernierMia.equals("SHOOT")){
					s="COVER"; 
					decisionPrise=true; 
				}
				else if(dernierMia.equals("AIM")&&adve.getNbVies()>=this.pdvCritique&&miage.getNbVies()>this.pdvCritique&&!decisionPrise){
					s="SHOOT"; 
					decisionPrise=true; 
				}
				else{
					s = "COVER"; 
					decisionPrise=true; 
				}

			}	
			// ****** SHOOT ***** // 
			if(!decisionPrise){				// Si pas de décision prise // 
				if(shootMia){  // Si on a des balles // 
					s = "SHOOT"; 
					decisionPrise = true; 
				}
				else{ // Si pas de décision 
					s = "RELOAD"; 
					decisionPrise = true; 
				}		
			}	
		} // FIN DECISION SHOOT // 

		// SI NOTRE ADVERSAIRE VA VISER // 
		else if(predAdv.equals("AIM")&&!decisionPrise)
		{
			if(shootMia) // Si on peut tirer // 
			{
				if(adve.getNbVies()<pdvCritique){ // Si il a peu de point de vie // 
					s = "SHOOT"; 
					decisionPrise = true; 
				}
				else if(adve.getNbVies()>=pdvCritique&&dernierAdv.equals("SHOOT")){
					s = "AIM"; 
					decisionPrise = true; 
				}
				else{
					s = "SHOOT"; 
					decisionPrise = true; 
				}
				if(dernierMia.equals("AIM")&&dernierAdv.equals("SHOOT")&&!decisionPrise){
					s = "SHOOT"; 
					decisionPrise = true; 
				}
			}
			else
			{
				s = "RELOAD"; 
				decisionPrise = true; 
			}
		} // FIN ADVERSAIRE VISER // 

		else if(predAdv.equals("COVER")&&!decisionPrise){

			// Si on peut tirer // 
			if(shootMia){
				if(dernierMia.equals("AIM")&&adve.getNbBouclier()>1){
					s = "AIM"; 
					decisionPrise = true;
				}
				else{   
					if(adve.getNbVies()>pdvCritique&&!dernierMia.equals("AIM")&&miage.getNbVies()>pdvCritique){
						s = "AIM";
						decisionPrise = true; 
					}
					else{
						s = "SHOOT";
						decisionPrise = true; 
					}
				}

			}
			else{   // Sinon // 
				s = "RELOAD"; 
				decisionPrise = true; 
			}
		}
		else if(predAdv.equals("RELOAD")&&!decisionPrise){
			if(shootMia){
				if(dernierMia.equals("AIM")){
					s = "SHOOT"; 
					decisionPrise = true; 
				}
				else if(adve.getNbVies()<=this.pdvCritique){
					s = "SHOOT"; 
					decisionPrise = true; 
				}
				else{
					s = "AIM"; 
					decisionPrise = true; 
				}
			}
			else{   // Sinon // 
				s = "RELOAD";
				decisionPrise = true; 
			}	
		}
		else if(predAdv.equals("NA")){

			if(miage.getNbBombe()>0){
				s = "BOMB";
			}

			else if(shootMia){
				if(miage.getNbBullet()>0&&adve.getNbVies()>=pdvCritique){
					s = "AIM"; 
					decisionPrise = true; 
				}
				else if(miage.getNbBullet()>0){
					s = "SHOOT"; 
					decisionPrise = true; 
				}else{
					s = "RELOAD"; 
					decisionPrise = true;
				}
			}
			else{   // Sinon // 
				s = "RELOAD";
				decisionPrise = true; 
			}	
		}
		return s; 
	}



	private enum Coups {
		SHOOT, RELOAD, COVER, AIM;
	}
}