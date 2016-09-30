package ca.mcmaster.magarveylab.matching.inOut.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GTRmodule {
	private Map<String, Double> genes = new HashMap<String, Double>();
	
	public GTRmodule(){
		
	}
	
	public void addGene(String gene, Double score){
		genes.put(gene, score);
	}
	
	public boolean isHexose(){
		String bestGene = null;
		Double bestScore = Double.NEGATIVE_INFINITY;
		for(Entry<String, Double> entry : genes.entrySet()){
			if(bestScore < entry.getValue()){
				bestGene = entry.getKey();
				bestScore = entry.getValue();
			}
		}
		if(bestGene.toLowerCase().contains("glucose") || bestGene.toLowerCase().contains("n-acetylglucosamine") || bestGene.toLowerCase().contains("mannose") || bestGene.toLowerCase().contains("gulose")) return true;
		return false;
	}	
}
