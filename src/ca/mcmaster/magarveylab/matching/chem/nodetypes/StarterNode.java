package ca.mcmaster.magarveylab.matching.chem.nodetypes;

import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.CStarterEnum;

public class StarterNode extends ChemicalNode{
	
	private CStarterEnum cse = null;
	
	
	public StarterNode(){
		super();
	}
	public void setStarter(CStarterEnum cse){
		this.cse = cse;
	}
	
	public String toString(){
		String out = cse.toString();
		return out;
	}

}
