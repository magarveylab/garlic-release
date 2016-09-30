package ca.mcmaster.magarveylab.matching.chem.nodetypes;

import ca.mcmaster.magarveylab.enums.domains.DomainType;

/**
 * Class to store extra information about a domain, whether it is in a cluster or not and it's location in an orf
 * @author chris
 *
 */
public class GeneticDomain {
	private DomainType domain;
	private boolean inCluster;
	private Integer start;
	private Integer stop;
	
	public GeneticDomain(DomainType domain, boolean inCluster, int start, int stop){
		this.domain = domain;
		this.inCluster = inCluster;
		this.start = start;
		this.stop = stop;
	}
	
	public GeneticDomain(DomainType domain, boolean inCluster) {
		this.domain = domain;
		this.inCluster = inCluster;
		start = null;
		stop = null;
	}

	public DomainType getDomainType(){
		return domain;
	}
	
	/**
	 * @return boolean whether this domain is in the cluster or not
	 */
	public boolean inCluster(){
		return inCluster;
	}
	
	public Integer getStart(){
		return start;
	}
	
	public Integer getStop(){
		return stop;
	}
	
	@Override
	public String toString(){
		StringBuilder out = new StringBuilder();
		out.append(domain + " ");
		out.append("In cluster: " + inCluster + "\n");
		out.append("Location on orf: " + start + "-" + stop);
		return out.toString();
	}
}
