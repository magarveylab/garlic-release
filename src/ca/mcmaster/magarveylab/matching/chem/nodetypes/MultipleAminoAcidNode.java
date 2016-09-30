package ca.mcmaster.magarveylab.matching.chem.nodetypes;

import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;

/**
 * A representation of a node representing a connected chemical fragment which may be a product of zero or more amino acids
 * @author Gregory M Chen
 *
 */
public class MultipleAminoAcidNode extends ChemicalNode {

	private int maxNumAminoAcids;
	
	public MultipleAminoAcidNode(int maxNumAminoAcids) {
		super();
		this.setMaxNumAminoAcids(maxNumAminoAcids);
	}

	/**
	 * @return the maxNumAminoAcids
	 */
	public int getMaxNumAminoAcids() {
		return maxNumAminoAcids;
	}

	/**
	 * @param maxNumAminoAcids the maxNumAminoAcids to set
	 */
	public void setMaxNumAminoAcids(int maxNumAminoAcids) {
		this.maxNumAminoAcids = maxNumAminoAcids;
	}
	
	@Override public String toString() {
		String out = "Possible_Multiple_Amino_Acid: " + maxNumAminoAcids;
		return out;
	}
}
