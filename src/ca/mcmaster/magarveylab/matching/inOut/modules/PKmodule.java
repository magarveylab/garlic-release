package ca.mcmaster.magarveylab.matching.inOut.modules;

import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.ChemicalNodeOrigin;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public class PKmodule {

	public boolean empty = true;
	private ChemicalNodeOrigin cno = null;
	private Integer moduleNumber = null;
	private Integer orfNumber = null;
	private List<PKOxidationStateEnum> oxis = new ArrayList<PKOxidationStateEnum>();
	private List<PKSubstrateEnum> subs = new ArrayList<PKSubstrateEnum>();
	private List<Double> pksesScores = new ArrayList<Double>();
	private List<SiteSpecificTailoringEnum> sstes = new ArrayList<SiteSpecificTailoringEnum>();
	
	public PKmodule(){
		
	}
	
	public void addPossibleSubstrate(PKSubstrateEnum pkse, Double score){
		subs.add(pkse);
		pksesScores.add(score);
		empty = false;
	}
	public void addPossibleOxidationState(PKOxidationStateEnum oxi){
		oxis.add(oxi);
		empty = false;
	}
	public void setOrigin(ChemicalNodeOrigin cno){
		this.cno = cno;
		empty = false;
	}
	public void setModuleNumber(int number){
		moduleNumber = number;
	}
	public void setOrfNumber(int number){
		orfNumber = number;
	}
	public void addSiteSpecificTailoring(SiteSpecificTailoringEnum sste){
		sstes.add(sste);
		empty = false;
	}

	public PKPieceNode getNode(){ //make the node and return it here
		PKPieceNode pkn = new PKPieceNode();
		PKOxidationStateEnum oxidationState = getOxiState();
		if(oxidationState != null) {
			pkn.setPossibleOxidationStates(oxidationState);
		}
		PKSubstrateEnum pkSubstrate = getSubstrate();
		if(pkSubstrate != null) {
			pkn.setSubstrate(pkSubstrate);
		}
		pkn.setOrigin(cno);
		
		for(SiteSpecificTailoringEnum sste : sstes){
			pkn.addSiteSpecificTailoring(sste);
		}
		if(moduleNumber != null){
			pkn.setModuleNumber(moduleNumber);
		}
		if(orfNumber != null){
			pkn.setOrfNumber(orfNumber);
		}
		
		return(pkn);
	}
	
	public ChemicalNodeOrigin getOrigin(){
		return cno;
	}
	
	private PKSubstrateEnum getSubstrate(){
		//how to pick? for now just picking first
		if(subs.size() > 0){
			return(subs.get(0));
		}else{
			return null;
		}
	}
	private PKOxidationStateEnum getOxiState(){
		
		if(oxis.contains(PKOxidationStateEnum.KETONE)){
			if(oxis.contains(PKOxidationStateEnum.HYDROXYL)){
				if(oxis.contains(PKOxidationStateEnum.DOUBLEBOND)){
					if(oxis.contains(PKOxidationStateEnum.SINGLEBOND)){
						return PKOxidationStateEnum.SINGLEBOND;
					}else{
						return PKOxidationStateEnum.DOUBLEBOND;
					}
				}else{
					return PKOxidationStateEnum.HYDROXYL;
				}
			}else{
				return PKOxidationStateEnum.KETONE;
			}
		}else{
			return null;
		}
	}
	public String toString(){
		String output = "";
		if(empty){
			return "Empty";
		}else{
			if(cno != null){
				output = cno.toString() + "\n";
			}
			if(oxis != null){
				for(PKOxidationStateEnum oxi : oxis){
					output = output + oxi.toString() + " ";
				}
				output = output + "\n";
			}
			if(subs.size() > 0){
				for(PKSubstrateEnum sub : subs){
					output = output + sub.toString() + " ";
				}
				output = output + "\n";
			}
			if(pksesScores != null){
				for(Double score : pksesScores){
					output = output + score + " ";
				}
				output = output + "\n";
			}
			if(sstes != null){
				for(SiteSpecificTailoringEnum sste : sstes){
					output = output + sste + " ";
				}
				output = output + "\n";
			}
		}
		
		return output;
	}
}
