package ca.mcmaster.magarveylab.matching.chem.nodetypes;

import java.util.ArrayList;
import java.util.List;
import ca.mcmaster.magarveylab.enums.domains.DomainType;


/**
 * @author chris
 * Gets information about a ribosomal cluster's ORF, this information will be useful for matching.
 */
public class RibosomalOrf {
	String sequence;
	List<GeneticDomain> domains = new ArrayList<GeneticDomain>();
	
	public RibosomalOrf(String sequence){
		this.sequence = sequence;
	}
	public void addDomain(DomainType domain, int start, int end){
		
		domains.add(new GeneticDomain(domain, true, start, end));
	}
	
	public String getSequence(){
		return sequence;
	}
	
	public List<GeneticDomain> getDomains(){
		return domains;
	}
}
