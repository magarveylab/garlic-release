package ca.mcmaster.magarveylab.matching.chem.nodetypes;

import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public class AminoAcidNode extends ChemicalNode {
	private AminoAcidEnum aminoAcidType;

	/**
	 * @return the aminoAcidType
	 */
	public AminoAcidEnum getAminoAcidType() {
		return aminoAcidType;
	}

	/**
	 * @param aminoAcidType the aminoAcidType to set
	 */
	public void setAminoAcidType(AminoAcidEnum aminoAcidType) {
		this.aminoAcidType = aminoAcidType;
	}
	
	@Override public String toString() {
		String out = "";
		if(aminoAcidType != null) {
			out += aminoAcidType.getAbbreviation();
		}
		else {
			out += "Unk";
		}
		for(SiteSpecificTailoringEnum siteSpecificTailoring : this.getSiteSpecificTailoring()) {
			out += "-" + siteSpecificTailoring.getAbbreviation();
		}
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AminoAcidNode)) {
			return false;
		}
		AminoAcidNode otherNode = (AminoAcidNode) obj;
		if(this.aminoAcidType != otherNode.aminoAcidType) {
			return false;
		}
		if(!this.getSiteSpecificTailoring().containsAll(otherNode.getSiteSpecificTailoring())) {
			return false;
		}
		if(!otherNode.getSiteSpecificTailoring().containsAll(this.getSiteSpecificTailoring())) {
			return false;
		}
		return true;
	}
}
