package ca.mcmaster.magarveylab.matching.inOut.in;

import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.inOut.Utility;

/**
 * @author cDejong
 * Simple class for parsing strings of input
 * Deals with both scaffolds and tailorings
 * Does not deal with sugars, specific tailors, fatty acids, or acyl adenylation units
 */
public class TextIn {
	
	/**
	 * @param scaffold the scaffold of amino acids and polyketides see parseScaffold for formatting information
	 * @param tailorings the tailorings of a breakdown, see parseTailorings for formatting information
	 * @return a Breakdown object usable by GARLIC
	 * @throws BreakdownParsingException catches all errors
	 */
	public static PrismBreakdown readText(String scaffold, String tailorings) throws BreakdownParsingException{
		
		if(scaffold.isEmpty()){
			throw new BreakdownParsingException("No scaffold information");
		}
		
		ChemicalAbstraction ca = new ChemicalAbstraction();
		PrismBreakdown breakdown = new PrismBreakdown();
		try{
			//Parse the scaffold
			if(!scaffold.isEmpty()){
				parseScaffold(scaffold, ca);
			}
			
			//add tailoring
			if(!tailorings.isEmpty()){
				parseTailorings(tailorings, ca);
			}
			
			breakdown.setChemicalAbstraction(ca);
		}catch(Exception e){
			throw new BreakdownParsingException("Could not parse string" + e.getLocalizedMessage());
		}
		
		if(breakdown.getChemicalAbstraction().getNodeStrings().size() < 1){
			throw new BreakdownParsingException("No scaffold information could be parsed from input");
		}
		
		return breakdown;
	}

	/**
	 * @param tailorings all tailorings that match the abbreviation of NonSpecificTailoringEnum, split by space
	 * Example: "Cl, ST, GTr"
	 * @param chemicalAbstraction the parsed tailoring data gets added to this object
	 * @throws BreakdownParsingException
	 */
	private static void parseTailorings(String tailorings, ChemicalAbstraction chemicalAbstraction) throws BreakdownParsingException {
		for(String single : tailorings.split(" ")){
			if(single.isEmpty()){
				continue;
			}
			try{
				NonSpecificTailoringEnum nste = Utility.getNonSpecificTailoringEnum(single);
				if(nste != null){
					chemicalAbstraction.addNonspecificTailoringEnum(nste);
				}
			}catch(Exception e){
				throw new BreakdownParsingException(", bad tailoring piece: " + single);
			}
		}
	}

	/**
	 * @param scaffold all scaffold Polyketide or amino acid pieces. 
	 * Amino acids match abbreviation of AminoAcidEnum,
	 * Polyketide matche abbreviation for PKSubstrateEnum.
	 * Space separated, different orfs are split by "|" 
	 * oxidation states match the abbreviation of PKOxidationStateEnum.
	 * Polyketide substrates and Oxidation states are split by "-"
	 * Example: "Ala Gly | MeM-H"
	 * @param ca
	 * @throws BreakdownParsingException
	 */
	private static void parseScaffold(String scaffold, ChemicalAbstraction ca) throws BreakdownParsingException {
		for(String splitPiece : scaffold.split("\\|")){
			ChemicalNodeString cns = new ChemicalNodeString();
			for(String single : splitPiece.split(" ")){
				if(single.isEmpty()){
					continue;
				}
				try{
					if(single.contains("-")){ //PK 
						String[] pkSplit = single.split("-");
						PKPieceNode pkp = new PKPieceNode();
						PKSubstrateEnum substrate = Utility.getPKsubstrate(pkSplit[0]);
						if(substrate == null){
							throw new Exception();
						}
						pkp.setSubstrate(substrate);
						PKOxidationStateEnum oxi = Utility.getPolyketideOxidationStates(pkSplit[1]);
						if(oxi == null){
							throw new Exception();
						}
						pkp.setPossibleOxidationStates(oxi);
						cns.addNode(pkp);
					}else{ //AA
						AminoAcidNode aan = new AminoAcidNode();
						AminoAcidEnum aa = Utility.getAAsubstrate(single);
						if(aa == null){
							throw new Exception();
						}
						aan.setAminoAcidType(aa);
						cns.addNode(aan);
					}
				}catch(Exception e){
					throw new BreakdownParsingException(", bad scaffold piece: " + single);
				}
			}
			if(cns.getChemicalNodes().size() > 0){
				ca.addNodeString(cns);
			}
		}
	}
}
