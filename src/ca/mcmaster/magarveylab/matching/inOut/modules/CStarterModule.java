package ca.mcmaster.magarveylab.matching.inOut.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ca.mcmaster.magarveylab.matching.chem.nodetypes.StarterNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AdenylatedOther;
import ca.mcmaster.magarveylab.matching.enums.Monomers.CStarterEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.ChemicalNodeOrigin;

public class CStarterModule {
	private Map<CStarterEnum, Double> cStarters = new HashMap<CStarterEnum, Double>(); 
	public boolean empty = true;
	
	public CStarterModule(){
	}
	
	public void addStarter(AdenylatedOther cse, Double score){
		cStarters.put((CStarterEnum) cse, score);
		empty = false;
	}
	
	public StarterNode getNode(){
		StarterNode sn = new StarterNode();
		sn.setOrigin(ChemicalNodeOrigin.PRISM_C_STARTER);
		sn.setStarter(getBestCStarter());
		return sn;
	}
	
	private CStarterEnum getBestCStarter(){
		CStarterEnum bestCStarter = null;
		Double bestScore = Double.NEGATIVE_INFINITY;
		for(Entry<CStarterEnum, Double> entry : cStarters.entrySet()){
			if(bestScore < entry.getValue()){
				bestCStarter = entry.getKey();
				bestScore = entry.getValue();
			}
		}
		return bestCStarter;
	}	
}