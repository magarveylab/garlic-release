package ca.mcmaster.magarveylab.matching.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChemicalClassifications {

	public enum ChemicalType {
		NRP, NRP_PK_HYBRID, PK, AMINOGLYCOSIDE, UNKNOWN, RIBOSOMAL;
		
		public static ChemicalType getChemicalType(String ctString){
			if(ctString == null) return ChemicalType.UNKNOWN;
			ChemicalType chemicalType = ChemicalType.UNKNOWN;
			for(ChemicalType ct : ChemicalType.values()){
				if(ctString.equals(ct.name())){
					chemicalType = ct;
					break;
				}
			}
			return chemicalType;
		}
	}
	
	public enum ChemicalSubtype{
		TYPE_1_PK, MACROLIDE, STANDARD, NON_TYPE_2_AROMATIC, TYPE_2, TERPENE, FUNGAL, ENEDYINE, NONE;

		public static ChemicalSubtype getChemicalSubtype(String cstString) {
			if(cstString == null) return null;
			ChemicalSubtype chemicalSubtype = null;
			for(ChemicalSubtype cst : ChemicalSubtype.values()){
				if(cstString.equals(cst.name())){
					chemicalSubtype = cst;
					break;
				}
			}
			return chemicalSubtype;
		}
	}
	
	public interface ChemicalScaffold{
		String name();
		
	}
	
	/**
	 * Creates and returns a list of all the chemical scaffolds
	 * @return
	 */
	public static List<ChemicalScaffold> getChemicalScaffolds(){
		List<ChemicalScaffold> chemicalScaffolds = new ArrayList<ChemicalScaffold>();
		chemicalScaffolds.addAll(Arrays.asList(AromaticFungal.values()));
		chemicalScaffolds.addAll(Arrays.asList(AromaticNotType2.values()));
		chemicalScaffolds.addAll(Arrays.asList(Type2Cyclases.Type2Scaffold.values()));
		chemicalScaffolds.addAll(Arrays.asList(Terpenes.values()));
		chemicalScaffolds.addAll(Arrays.asList(Enedyine.values()));
		return chemicalScaffolds;
	}
	
	public static ChemicalScaffold getChemicalScaffold(String csString){
		if(csString == null) return null;
		List<ChemicalScaffold> chemicalScaffolds = getChemicalScaffolds();
		ChemicalScaffold chemicalScaffold = null;
		for(ChemicalScaffold cs : chemicalScaffolds){
			if(csString.toUpperCase().equals(cs.name().toUpperCase())){
				chemicalScaffold = cs;
				break;
			}
		}
		return chemicalScaffold;
	}
	
	public enum AromaticFungal implements ChemicalScaffold {
		Annularin("CCCC1CC(CC(=O)O1)OC"),
		Fungal1("C1CCC2CC3OCCCC3CC2C1"),
		Fungal2("C1CCC2CC3CCCCC3CC2C1"),
		Fungal5("C1CCC2C(C1)OC1CCCCC21"),
		Fungal6("CC1(C)COC2C1CC1CCCC3CCCC2C13"),
		Fungal7("CC1(C)COC2C1CC1CCOC3CCCC2C13"),
		Fungal8("CC1(C)COC2C1CC1COCC3CCCC2C13"),
		Fungal4("C1CCC2C(C1)COC1CCCCC21"),
		Lagopodin("CC1(C)CC(O)CC1C1=CC(=O)C=CC1=O"),
		Fungal3("C1CCC2CC3OC4CCCCC4CC3CC2C1"),
		Xanthone2("O=C1C2CCCCC2OC2C3C(CCC12)OC1OCCC31"),
		Xanthone("O=C1C2CCCCC2OC2CCCCC12"),
		Fuscin("CC1(C)CCC2C(CCC3COCCC23)O1"),
		Fuscinarin("CC1(C)CCC2C3COCC3CCC2O1"),
		Aporpinone("CC#C\\C=C1/OC(=O)C=C1"),
		Cyclopaidic_acid("OCC1C2C(O)OC(=O)C2C(O)CC1O"),
		Phenalenone1("CC(C)CCOC1CCC2CCCC3CCCC1C23"),
		Phenalenone4("CC(C)CCOC1CCC2CCOC3CCCC1C23"),
		Phenalenone5("CC(C)CCOC1CCC2COCC3CCCC1C23"),
		Phenalenone2("OC1CCC2CCCC3CCCC1C23"),
		Phenalenone3("OC1CCC2CCOC3CCCC1C23"),
		Phenalenone6("OC1CCC2COCC3CCCC1C23");
		
		private final String smiles;
		private AromaticFungal(final String smiles) {
			this.smiles = smiles;
		}
	}
	
	public enum AromaticNotType2 implements ChemicalScaffold {
		Rebeccamycin_agly("OC1NC(O)C2C1C1C(NC3C1CCCC3)C1C2C2C(N1)CCCC2"),
		Teradecomycin("CC1OCC2C1OC1CC3CCCCC3C2C1"),
		Bisindole_malleam("OC1NC(O)C(C2CNC3C2CCCC3)C1C1CNC2C1CCCC2"),
		Violacei("OC1NC(C\\C1C1/C(O)NC2C1CCCC2)C1CNC2C1CCCC2"),
		Bis_indole_quinon("N1CC(C2C1CCCC2)C1CCC(CC1)C1CNC2C1CCCC2"),
		Pre_violacei("OC1NC(CC1C1CNC2C1CCCC2)C1CNC2C1CCCC2"),
		Pulvinic_acid2("OC(O)C(C1OC(O)C(C1O)C1CCCCC1)C1CCCCC1"),
		Flavone("OC1CC(OC2CCCCC12)C1CCCCC1"),
		Pulvinic_acid1("OC1C(C(O)O\\C1C/C1CCCCC1)C1CCCCC1"),
		Bis_aryl_quinon("C1CCC(CC1)C1CCC(CC1)C1CCCCC1"),
		Quinolon("CCCCCC1C(O)C(O)C2C(N1)CCCC2"),
		Coumanin("NC1C(O)C2CCC(O)CC2OC1O"),
		Phenazin("C1CC2NC3CCCCC3NC2CC1"),
		Phenothazin("N1C2CCCCC2SC2C1CCCC2"),
		Phenoxazin("N1C2CCCCC2OC2C1CCCC2"),
		Carbolin("C1CC2C(CN1)NC1C2CCCC1"),
		Pyoluteorin_es1("OC(C1CCCN1)C1CCCCC1"),
		Pyoluteorin_es2("C(C1CCCN1)C1CCCCC1"),
		Chromophyrrolic_a("OC(O)C1CCC(N1)C(O)O"),
		Pyrrolintrin_fan("N1CCC(C1)C1CCCCC1"),
		Indolotryptolin("OC1C(O)C(O)NC1O"),
		Maleimides1("OC1NC(O)CC1"),
		Maleimides2("OC1NCCC1");
		
		private final String smiles;
		private AromaticNotType2(final String smiles) {
			this.smiles = smiles;
		}
	}
	
	public enum Terpenes implements ChemicalScaffold {
		Mutilin("CCC1(C)CC[C@@H]2CCC[C@]3(CCC[C@@H]23)CC1"),
		Hopane("C1CC2CCC3C(CCC4C5CCCCC5CCC34)C2C1"),
		Sesquiterpene("C1CC2CCC3C(CCC4CCCCC34)C2C1"),
		Melledonal("CC1(C)CC2CCC3CCC3C2C1"),
		Abietadien("CC(C)C1CCC2C(CCC3C(C)(C)CCCC23C)C1"),
		Cyclocitrin("C1CC2CCC3C(CCC4CCCCC3C4)C2C1"),
		Cyathane("CC(C)C1CCC2CCC3CCCCCC3C12"),
		Pimaradiene("CC1(C)CCCC2(C)C3CCC(C)(CC3CCC12)CC"),
		Albaflavone("CC1(C)CC2CCCC22CCC1C2"),
		Acremostrictin("CC1CC2CC3(C)OC(O)C(C3C)C2C1"),
		Aszonapyrone("CC1(C)CCCC2(C)C1CCC1(C)C(CC3CCCOC3)CCCC21"),
		Carotenoid(""); //No smiles associated

		private final String smiles;
		private Terpenes(final String smiles) {
			this.smiles = smiles;
		}
	}
	
	public enum Enedyine implements ChemicalScaffold {
		NineMember("C1CC#CCCC#CC1"),
		TenMember("C1CCC#C\\C=C/C#CC1");

		private final String smiles;
		private Enedyine(final String smiles) {
			this.smiles = smiles;
		}
	}
}
