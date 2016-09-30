package ca.mcmaster.magarveylab.matching.breakdowns;

import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.enums.Monomers.KnownOther;

public class GrapeBreakdown extends Breakdown{

	private String smiles = null;
	private List<KnownOther> knownOthers = new ArrayList<KnownOther>();
	
	public GrapeBreakdown(ChemicalAbstraction ca, String smiles, String name) {
		this.ca = ca;
		this.name = name;
		this.smiles = smiles;
	}
	
	public GrapeBreakdown(){
		
	}
	public void setSmiles(String smiles){
		this.smiles = smiles;
	}
	public void addKnownOther(KnownOther knownOther) {
		knownOthers.add(knownOther);		
	}
	
	public String getSmiles(){
		return smiles;
	}
	public List<KnownOther> getKnownOthers(){
		return knownOthers;
	}
}
