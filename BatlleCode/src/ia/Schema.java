package ia;

import java.util.ArrayList;
import java.util.Random;

import battleCode.Bot;

public class Schema {
	private String coup;
	private String coupPrecedent;
	private String coupMiagic;
	private String coupSuivant="NA";
	private int puissance = 0;
	private enum Coups {
		SHOOT, RELOAD, COVER, AIM, NA, BOMB;
	}
	

	public Schema(String coup, String coupPrecedent, String coupMiagic) {
		this.coup = coup;
		this.coupPrecedent = coupPrecedent;
		this.coupMiagic = coupMiagic;
	}
	
	public void comparer(Schema s){
		int vretour = 0;
		if(this.coupPrecedent.equals(s.getCoupPrecedent())){
			vretour = 1;
		}
		if(this.coupMiagic.equals(s.getCoupMiagic())){
			vretour = 2;
		}
		if(this.coupPrecedent.equals(s.getCoupPrecedent())&&this.coupMiagic.equals(s.getCoupMiagic())){
			vretour = 3;
		}
		this.puissance = vretour;
	}
	public static String comparerValeurCoupsSuivant(ArrayList<Schema> s, Bot adve){
		String vretour = "NA";
		int reload = 0;
		int shoot = 0;
		int cover = 0;
		int aim = 0;
		int bomb = 0; 
		Coups valCoup;
		for(int i = 0; i <= s.size() - 1; i++){
			valCoup = Coups.valueOf(s.get(i).getCoupSuivant());
			switch(valCoup){
			case SHOOT:
				if(adve.getNbBullet()>0){
					shoot++;
				}
				break;
			case AIM:
				if(adve.getNbBullet()>0){
					aim++;
				}
				break;
			case COVER:
				if(adve.getNbBouclier()>0){
					cover++;
				}
				break;
			case RELOAD: 
				reload++;
				break;
				case BOMB: 
					if(adve.getNbBombe()>0){
						bomb++;
					}
				break;
			
			}
			
		}
		int max =  Math.max(Math.max(Math.max(shoot,Math.max(bomb,reload)),cover),aim);
		ArrayList<String> maxs=new ArrayList<String>();
		if(shoot==max){maxs.add("SHOOT");}
		if(aim==max){maxs.add("AIM");}
		if(cover==max){maxs.add("COVER");}
		if(bomb==max){maxs.add("BOMB");}
		if(reload==max){maxs.add("RELOAD");}
		if(maxs.size()==1){vretour=maxs.get(0);}
		if(maxs.size()==2|| maxs.size() == 3){
			Random rand = new Random();
			int randomNum = rand.nextInt(1);
			vretour=maxs.get(randomNum);
		}
		if(maxs.size() == 4 || maxs.size() == 5){
			vretour = "NA";
		}
		return vretour;
	}
	
	/*Getter and Setter*/
	public String getCoupSuivant() {
		return coupSuivant;
	}
	public void setCoupSuivant(String coupSuivant) {
		this.coupSuivant = coupSuivant;
	}
	public int getPuissance() {
		return puissance;
	}
	public void setPuissance(int puissance) {
		this.puissance = puissance;
	}
	public String getCoup() {
		return coup;
	}

	public String getCoupPrecedent() {
		return coupPrecedent;
	}

	public String getCoupMiagic() {
		return coupMiagic;
	}

	
	
	
	

}
