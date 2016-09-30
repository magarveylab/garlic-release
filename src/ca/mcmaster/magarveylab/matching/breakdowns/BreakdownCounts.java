package ca.mcmaster.magarveylab.matching.breakdowns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ca.mcmaster.magarveylab.enums.domains.BetaLactamDomains;
import ca.mcmaster.magarveylab.enums.domains.DomainType;
import ca.mcmaster.magarveylab.enums.domains.RegulatorDomains;
import ca.mcmaster.magarveylab.enums.domains.ResistanceDomains;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.GeneticDomain;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.KnownOther;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public class BreakdownCounts {
	private Map<AcylAdenylatingSubstrates, Integer> acyls;
	private Map<AminoAcidEnum, Integer> aas;
	private Map<PKSubstrateEnum, Integer> pks;
	private Map<NonSpecificTailoringEnum, Integer> tailors; //doesn't include quite all "specific" tailors
	private Map<KnownOther, Integer> otherPieces;
	private Map<RegulatorDomains, Integer> regulators;
	private Map<ResistanceDomains, Integer> resistors;
	private Map<DomainType, Integer> otherDomains;
	
	//TODO: add sugar and sugar genes, also add polyketide (substrate), and 'number of'
	public BreakdownCounts(){
		initiate();		
	}
	
	public BreakdownCounts(Breakdown breakdown){
		initiate();
		count(breakdown);
	}
	
	public BreakdownCounts(List<Breakdown> breakdowns){
		initiate();
		for(Breakdown breakdown : breakdowns){
			count(breakdown);
		}
	}
	
	private void initiate(){
		acyls = new HashMap<AcylAdenylatingSubstrates, Integer>();
		for(AcylAdenylatingSubstrates acyl : AcylAdenylatingSubstrates.values()){
			acyls.put(acyl, 0);
		}		
		aas = new HashMap<AminoAcidEnum, Integer>();
		for(AminoAcidEnum aa : AminoAcidEnum.values()){
			aas.put(aa, 0);
		}
		pks = new HashMap<PKSubstrateEnum, Integer>();
		for(PKSubstrateEnum aa : PKSubstrateEnum.values()){
			pks.put(aa, 0);
		}
		tailors = new HashMap<NonSpecificTailoringEnum, Integer>();
		for(NonSpecificTailoringEnum nste : NonSpecificTailoringEnum.values()){
			tailors.put(nste, 0);
		}
		otherPieces = new HashMap<KnownOther, Integer>();
		for(KnownOther other : KnownOther.values()){
			otherPieces.put(other, 0);
		}
		regulators = new HashMap<RegulatorDomains, Integer>();
		for(RegulatorDomains regulator : RegulatorDomains.values()){
			regulators.put(regulator, 0);
		}
		resistors = new HashMap<ResistanceDomains, Integer>();
		for(ResistanceDomains resistor : ResistanceDomains.values()){
			resistors.put(resistor, 0);
		}
		otherDomains = new HashMap<DomainType, Integer>();
		for(DomainType domain : BetaLactamDomains.values()){
			otherDomains.put(domain, 0);
		}
	}
	
	public void addBreakdown(Breakdown breakdown){
		count(breakdown);
	}
	
	public void addBreakdowns(List<Breakdown> breakdowns){
		for(Breakdown breakdown : breakdowns){
			count(breakdown);
		}
	}

	private void count(Breakdown breakdown) {
		for(AcylAdenylatingSubstrates acyl : breakdown.getChemicalAbstraction().getAcylAdenylatingSubstrates()){
			acyls.put(acyl, acyls.get(acyl) + 1);
		}
		for(ChemicalNodeString cns : breakdown.getChemicalAbstraction().getNodeStrings()){
			for(ChemicalNode cn : cns.getChemicalNodes()){
				if(cn.isAAnode()){
					AminoAcidNode aan = (AminoAcidNode)cn;
					if(aan.getAminoAcidType() != null){
						aas.put(aan.getAminoAcidType(), aas.get(aan.getAminoAcidType()) + 1);
					}
				}else if(cn.isPKNode()){
					PKPieceNode pkn = (PKPieceNode)cn;
					if(pkn.getSubstrate() != null){
						pks.put(pkn.getSubstrate(), pks.get(pkn.getSubstrate()) + 1);
					}
				}
				//Add all the site specific tailors to the list
				for(SiteSpecificTailoringEnum ste : cn.getSiteSpecificTailoring()){
					if(ste.equals(SiteSpecificTailoringEnum.C_METHYLTRANSFERASE)){
						tailors.put(NonSpecificTailoringEnum.C_METHYLTRANSFERASE, tailors.get(NonSpecificTailoringEnum.C_METHYLTRANSFERASE) + 1);
					}else if(ste.equals(SiteSpecificTailoringEnum.N_METHYLTRANSFERASE)){
						tailors.put(NonSpecificTailoringEnum.N_METHYLTRANSFERASE, tailors.get(NonSpecificTailoringEnum.N_METHYLTRANSFERASE) + 1);
					}else if(ste.equals(SiteSpecificTailoringEnum.O_METHYLTRANSFERASE)){
						tailors.put(NonSpecificTailoringEnum.O_METHYLTRANSFERASE, tailors.get(NonSpecificTailoringEnum.O_METHYLTRANSFERASE) + 1);
					}else if(ste.equals(SiteSpecificTailoringEnum.OXAZOLE) || ste.equals(SiteSpecificTailoringEnum.CYCLIZATION)){
						tailors.put(NonSpecificTailoringEnum.CYCLIZATION, tailors.get(NonSpecificTailoringEnum.CYCLIZATION) + 1);
					}else if(ste.equals(SiteSpecificTailoringEnum.THIAZOLE)){
						tailors.put(NonSpecificTailoringEnum.THIAZOLE, tailors.get(NonSpecificTailoringEnum.THIAZOLE) + 1);
					}
				}
			}
		}
		for(NonSpecificTailoringEnum nste : breakdown.getChemicalAbstraction().getNonSpecificTailorings()){
			tailors.put(nste, tailors.get(nste) + 1);
		}
		
		//prism specific data resistors and regulators
		if(breakdown instanceof PrismBreakdown){
			PrismBreakdown prismBreakdown = (PrismBreakdown) breakdown;
			for(GeneticDomain resistor : prismBreakdown.getResistors()){
				resistors.put((ResistanceDomains)resistor.getDomainType(), resistors.get(resistor.getDomainType()) + 1);
			}
			for(GeneticDomain regulator : prismBreakdown.getRegulators()){
				regulators.put((RegulatorDomains)regulator.getDomainType(), regulators.get(regulator.getDomainType()) + 1);
			}
			for(DomainType other : prismBreakdown.getOtherDomains()){
				otherDomains.put(other, otherDomains.get(other) + 1);
			}
		}
		//grape specific data others
		else if(breakdown instanceof GrapeBreakdown){
			GrapeBreakdown grapeBreakdown = (GrapeBreakdown) breakdown;
			for(KnownOther other : grapeBreakdown.getKnownOthers()){
				otherPieces.put(other, otherPieces.get(other) + 1);
			}
		}		
	}
	
	//checkers
	public boolean contains(AcylAdenylatingSubstrates acyl){
		if(acyls.get(acyl) > 0){
			return true;
		}
		return false;
	}
	public boolean contains(AminoAcidEnum amino){
		if(aas.get(amino) > 0){
			return true;
		}
		return false;
	}
	public boolean contains(PKSubstrateEnum pk){
		if(pks.get(pk) > 0){
			return true;
		}
		return false;
	}
	public boolean contains(NonSpecificTailoringEnum nste){
		if(tailors.get(nste) > 0){
			return true;
		}
		return false;
	}
	public boolean contains(KnownOther other){
		if(otherPieces.get(other) > 0){
			return true;
		}
		return false;
	}
	public boolean contains(RegulatorDomains regulator){
		if(regulators.get(regulator) > 0){
			return true;
		}
		return false;
	}
	public boolean contains(ResistanceDomains resistor){
		if(resistors.get(resistor) > 0){
			return true;
		}
		return false;
	}
	public boolean contains(DomainType other){
		if(otherDomains.get(other) > 0){
			return true;
		}
		return false;
	}
	
	//piece getters
	public int count(AcylAdenylatingSubstrates acyl){
		return acyls.get(acyl);
	}
	public int count(AminoAcidEnum amino){
		return aas.get(amino);
	}
	public int count(PKSubstrateEnum pk){
		return pks.get(pk);
	}
	public int count(NonSpecificTailoringEnum nste){
		return tailors.get(nste);
	}
	public int count(KnownOther other){
		return otherPieces.get(other);
	}
	public int count(RegulatorDomains regulator){
		return regulators.get(regulator);
	}
	public int count(ResistanceDomains resistor){
		return resistors.get(resistor);
	}
	public int count(DomainType other){
		return otherDomains.get(other);
	}
	
	//full getters
	public Map<AcylAdenylatingSubstrates, Integer> getAcyls(){
		return acyls;
	}
	
	public Map<AminoAcidEnum, Integer> getAdenylation(){
		return aas;
	}
	public Map<PKSubstrateEnum, Integer> getPKSubstrates(){
		return pks;
	}
	
	public Map<NonSpecificTailoringEnum, Integer> getTailors(){
		return tailors;
	}
	
	public Map<KnownOther, Integer> getOthers(){
		return otherPieces;
	}
	
	public Map<RegulatorDomains, Integer> getRegulators(){
		return regulators;
	}
	
	public Map<ResistanceDomains, Integer> getResistors(){
		return resistors;
	}
	
	//toString override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("AcylAdenylating:\n");
		for(Entry<AcylAdenylatingSubstrates, Integer> acyl : acyls.entrySet()){
			if(acyl.getValue() > 0){
				sb.append(acyl.getKey().abbreviation() + "\t" + acyl.getValue() + "\n");
			}
		}
		sb.append("AminoAcids:\n");
		for(Entry<AminoAcidEnum, Integer> aa : aas.entrySet()){
			if(aa.getValue() > 0){
				sb.append(aa.getKey().getAbbreviation() + "\t" + aa.getValue() + "\n");
			}
		}
		sb.append("Polyketide:\n");
		for(Entry<PKSubstrateEnum, Integer> pk : pks.entrySet()){
			if(pk.getValue() > 0){
				sb.append(pk.getKey() + "\t" + pk.getValue() + "\n");
			}
		}
		sb.append("Non specific tailorings:\n");
		for(Entry<NonSpecificTailoringEnum, Integer> nstes : tailors.entrySet()){
			if(nstes.getValue() > 0){
				sb.append(nstes.getKey().getAbbreviation() + "\t" + nstes.getValue() + "\n");
			}
		}
		sb.append("Other pieces:\n");
		for(Entry<KnownOther, Integer> other : otherPieces.entrySet()){
			if(other.getValue() > 0){
				sb.append(other.getKey().getAbbreviation() + "\t" + other.getValue() + "\n");
			}
		}
		sb.append("Regulators:\n");
		for(Entry<RegulatorDomains, Integer> regulator : regulators.entrySet()){
			if(regulator.getValue() > 0){
				sb.append(regulator.getKey().abbreviation() + "\t" + regulator.getValue() + "\n");
			}
		}
		sb.append("Resistors:\n");
		for(Entry<ResistanceDomains, Integer> resistor : resistors.entrySet()){
			if(resistor.getValue() > 0){
				sb.append(resistor.getKey().abbreviation() + "\t" + resistor.getValue() + "\n");
			}
		}
		sb.append("Other domain types:\n");
		for(Entry<DomainType, Integer> other : otherDomains.entrySet()){
			if(other.getValue() > 0){
				sb.append(other.getKey().abbreviation() + "\t" + other.getValue() + "\n");
			}
		}
		return sb.toString();
	}
}
