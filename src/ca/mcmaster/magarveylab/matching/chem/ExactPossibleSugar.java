package ca.mcmaster.magarveylab.matching.chem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarEnum;

public class ExactPossibleSugar {
	Set<SugarEnum> possibleSugars = new HashSet<SugarEnum>();
	
	public ExactPossibleSugar(){
		
	}
	
	public ExactPossibleSugar(Set<SugarEnum> possibleSugars){
		this.possibleSugars = possibleSugars;
	}
	
	public void add(SugarEnum possibleSugar){
		possibleSugars.add(possibleSugar);
	}
	
	public Set<SugarEnum> getAll(){
		return possibleSugars;
	}
}
