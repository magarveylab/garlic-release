package ca.mcmaster.magarveylab.matching.chem.nodetypes;

import java.util.ArrayList;

import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public class PKPieceNode extends ChemicalNode {
	private ArrayList<PKOxidationStateEnum> possibleOxidationStates;
	private PKSubstrateEnum substrate;
	// If this should be considered a possible false prediction from GRAPE
	private boolean possibleNonPK;
	
	public PKPieceNode() {
		super();
		possibleOxidationStates = new ArrayList<PKOxidationStateEnum>();
	}
	
	/**
	 * @return the substrate
	 */
	public PKSubstrateEnum getSubstrate() {
		return substrate;
	}
	/**
	 * @param substrate the substrate to set
	 */
	public void setSubstrate(PKSubstrateEnum substrate) {
		this.substrate = substrate;
	}
	/**
	 * @return the possibleOxidationStates
	 */
	public ArrayList<PKOxidationStateEnum> getPossibleOxidationStates() {
		return possibleOxidationStates;
	}
	/**
	 * @param possibleOxidationStates the possibleOxidationStates to set
	 */
	public void setPossibleOxidationStates(ArrayList<PKOxidationStateEnum> possibleOxidationStates) {
		this.possibleOxidationStates = possibleOxidationStates;
	}
	/**
	 * Add a possible oxidation state
	 * @param possibleOxidationState
	 */
	public void addPossibleOxidationState(PKOxidationStateEnum possibleOxidationState) {
		this.possibleOxidationStates.add(possibleOxidationState);
	}
	/**
	 * Set the possble oxidation states to a single element array
	 * @param possibleOxidationStates the possibleOxidationStates to set
	 */
	public void setPossibleOxidationStates(PKOxidationStateEnum possibleOxidationState) {
		ArrayList<PKOxidationStateEnum> possibleOxidationStates = new ArrayList<PKOxidationStateEnum>();
		possibleOxidationStates.add(possibleOxidationState);
		this.possibleOxidationStates = possibleOxidationStates;
	}
	
	@Override public String toString() {
		String out = "";
		String oxidationStateString = "[";
		for(int i = 0; i < possibleOxidationStates.size(); i++) {
			if(i > 0) {
				oxidationStateString += ",";
			}
			oxidationStateString += possibleOxidationStates.get(i).getAbbreviation();
			
		}
		oxidationStateString += "]";
		out += this.getSubstrate() + "" + oxidationStateString;
		for(SiteSpecificTailoringEnum siteSpecificTailoring : this.getSiteSpecificTailoring()) {
			out += "-" + siteSpecificTailoring.getAbbreviation();
		}
		return out;
	}

	public boolean isPossibleNonPK() {
		return possibleNonPK;
	}

	public void setPossibleNonPK(boolean possibleFattyAcid) {
		this.possibleNonPK = possibleFattyAcid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PKPieceNode)) {
			return false;
		}
		PKPieceNode otherNode = (PKPieceNode) obj;
		if(otherNode.substrate != this.substrate) {
			return false;
		}
		if(this.possibleOxidationStates.size() != otherNode.possibleOxidationStates.size()) {
			return false;
		}
		if(!this.possibleOxidationStates.containsAll(otherNode.possibleOxidationStates)) {
			return false;
		}
		//assert (otherNode.possibleOxidationStates.containsAll(this.possibleOxidationStates));
		if (this.getSiteSpecificTailoring().size() != otherNode.getSiteSpecificTailoring().size()) {
			return false;
		}
		if(!this.getSiteSpecificTailoring().containsAll(otherNode.getSiteSpecificTailoring())) {
			return false;
		}
		//assert (otherNode.getSiteSpecificTailoring().containsAll(this.getSiteSpecificTailoring()));
		
		return true;
	}
}
