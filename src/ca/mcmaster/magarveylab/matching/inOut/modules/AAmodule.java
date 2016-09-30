package ca.mcmaster.magarveylab.matching.inOut.modules;

import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.ChemicalNodeOrigin;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public class AAmodule {
	public boolean empty = true;
	private Integer moduleNumber = null;
	private Integer orfNumber = null;
	private List<AminoAcidEnum> aaes = new ArrayList<AminoAcidEnum>();
	private List<Double> aaesScores = new ArrayList<Double>();
	private List<SiteSpecificTailoringEnum> sstes = new ArrayList<SiteSpecificTailoringEnum>(); //Add the modifications here
	private ChemicalNodeOrigin cno = null;
	
	public AAmodule(){
	}
	public void setOrigin(ChemicalNodeOrigin cno){
		this.cno  = cno;
		empty = false;
	}
	
	public void setModuleNumber(int number){
		moduleNumber = number;
	}
	public void setOrfNumber(int number){
		orfNumber = number;
	}
	
	public void addAA(AminoAcidEnum aae, Double score){
		aaes.add(aae);
		aaesScores.add(score);
		empty = false;
	}
	public void addSiteSpecificTailoring(SiteSpecificTailoringEnum sste){
		sstes.add(sste);
		empty = false;
	}
	public AminoAcidNode getNode(){//make and return AA node here
		AminoAcidNode aan = new AminoAcidNode();
		if(aaes.size() > 0){
			aan.setAminoAcidType(getBestAA());
		}
		aan.setOrigin(cno);
		for(SiteSpecificTailoringEnum sste : sstes){
			aan.addSiteSpecificTailoring(sste);
		}
		if(moduleNumber != null){
			aan.setModuleNumber(moduleNumber);
		}
		if(orfNumber != null){
			aan.setOrfNumber(orfNumber);
		}
		return(aan);
	}
	public ChemicalNodeOrigin getOrigin(){
		return cno;
	}

	private AminoAcidEnum getBestAA(){
		int bestScore = bestScore(aaesScores);
		return(aaes.get(bestScore));
		
	}

	private int bestScore(List<Double> scores) {
		int i = 0;
		Double best = 0.0;
		int bestIndex = -1;
		for (Double d : scores){
			if (d > best){
				best = d;
				bestIndex = i;
			}
			i ++;
		}
		
		return bestIndex;
	}
	public String toString(){
		String output = "";
		if(empty){
			return "Empty";
		}else{
			if(cno != null){
				output = output + cno.toString() + "\n";
			}
			if(aaes != null){
				for(AminoAcidEnum aae : aaes){
					output = output + aae.toString() + " ";
				}
				output = output + "\n";
			}
			if(aaesScores != null){
				for(Double score : aaesScores){
					output = output + score + " ";
				}
				output = output + "\n";
			}
			if(sstes != null){
				for(SiteSpecificTailoringEnum sste : sstes){
					output = output + sste + " ";
				}
			}
		}		
		return output;		
	}
}
