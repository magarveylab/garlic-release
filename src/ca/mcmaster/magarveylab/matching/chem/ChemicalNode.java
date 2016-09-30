package ca.mcmaster.magarveylab.matching.chem;

import java.util.HashSet;
import java.util.Set;

import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.ChemicalNodeOrigin;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public abstract class ChemicalNode {
	
	private ChemicalNodeOrigin origin;
	private Integer moduleNumber = null;
	private Integer orfNumber = null;
	private Set<SiteSpecificTailoringEnum> siteSpecificTailoring;
	
	public ChemicalNode() {
		siteSpecificTailoring = new HashSet<SiteSpecificTailoringEnum>();
	}

	/**
	 * @return the origin
	 */
	public ChemicalNodeOrigin getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(ChemicalNodeOrigin origin) {
		this.origin = origin;
	}

	/**
	 * @return the siteSpecificTailoring
	 */
	public Set<SiteSpecificTailoringEnum> getSiteSpecificTailoring() {
		return siteSpecificTailoring;
	}

	/**
	 * @param siteSpecificTailoring the siteSpecificTailoring to add
	 */
	public void addSiteSpecificTailoring(SiteSpecificTailoringEnum siteSpecificTailoring) {
		this.siteSpecificTailoring.add(siteSpecificTailoring);
	}
	
	public void setModuleNumber(int number){
		moduleNumber = number;
	}
	
	public Integer getModuleNumber(){
		return moduleNumber;
	}
	public void setOrfNumber(int number){
		orfNumber = number;
	}
	
	public Integer getOrfNumber(){
		return orfNumber;
	}

	public boolean isAAnode() {
		if(this instanceof AminoAcidNode){
			return true;
		}
		return false;
	}

	public boolean isPKNode() {
		if(this instanceof PKPieceNode){
			return true;
		}
		return false;
	}
}
