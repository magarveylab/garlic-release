package ca.mcmaster.magarveylab.matching.enums;

public class Monomers {

	/**
	 * Amino acid enums
	 * @author gmchen
	 */
	public enum AminoAcidEnum {
		Amino_Epoxi_Oxodecanoic_Acid(new String[]{"Aeo"}),
		Beta_Alanine(new String[]{"bAla", "&beta;-Ala"}),
		Alanine(new String[]{"Ala", "A"}),
		BetaMethylPhenylalanine(new String[]{"MePhe"}),
		Phenylalanine(new String[]{"Phe", "F"}),
		BetaPhenylalanine(new String[]{"bPhe", "&beta;-Phe"}),
		Beta_HydroxyPhenylalanine(new String[]{"OHPhe"}),
		HydroxyLeucine(new String[]{"OHLeu"}),
		Leucine(new String[]{"Leu", "L"}),
		Isoleucine(new String[]{"Ile", "I"}),
		Glycine(new String[]{"Gly", "G"}),
		Hydroxyphenylglycine(new String[]{"Hpg"}),
		DiHydroxyphenylglycine(new String[]{"Dhpg"}),
		Hydroxyasparagine(new String[]{"OHAsn"}),
		Asparagine(new String[]{"Asn", "N"}),
		Aspartic_Acid(new String[]{"Asp", "D"}),
		Hydroxyaspartic_Acid(new String[]{"OHAsp"}),
		Beta_Methyl_Aspartic_Acid(new String[]{"MeAsp"}),
		Benzoic_Acid(new String[]{"Bz"}), //not amino
		Capreomycidine(new String[]{"Cap"}),
		Citrulline(new String[]{"Cit"}),
		Cysteine(new String[]{"Cys", "C"}),
		Glutamine(new String[]{"Gln", "Q"}),
		Glutamic_Acid(new String[]{"Glu", "E"}),
		Histidine(new String[]{"His", "H"}),
		Methionine(new String[]{"Met", "M"}),
		Norvaline(new String[]{"Nva"}),
		Isovaline(new String[]{"Iva"}),
		Valine(new String[]{"Val", "V"}),
		Beta_hydroxy_Valine(new String[]{"OHVal"}),
		HydroxyOrnithine(new String[]{"OHOrn"}),
		HydroxyAcetylOrnithine(new String[]{"HAOrn"}),
		Ornithine(new String[]{"Orn"}),
		Tryptophan(new String[]{"Trp", "W"}),
		Threonine(new String[]{"Thr", "T"}),
		Butenyl_Methyl_Threonine(new String[]{"Bmt"}),
		Arginine(new String[]{"Arg", "R"}),
		MethylProline(new String[]{"MePro"}),
		Proline(new String[]{"Pro", "P"}),
		Serine(new String[]{"Ser", "S"}),
		Tyrosine(new String[]{"Tyr", "Y"}),
		HydroxyTyrosine(new String[]{"OHTyr"}),
		Lysine(new String[]{"Lys", "K"}),
		Beta_Lysine(new String[]{"bLys", "&beta;-Lys"}),
		Adipic_Acid(new String[]{"Aad"}),
		Lactate(new String[]{"Lac"}),
		Lactic_Acid(new String[]{"Lac"}),
		Propionyl(new String[]{"Hap"}),
		Hydroxyisovalerate(new String[]{"Hiv"}),//not amino
		Kynurenine(new String[]{"Kyn"}),
		Aminobutyric_Acid(new String[]{"Abu"}),
		Dehydro_Aminobutyric_Acid(new String[]{"Dhab"}),
		Aminoisobutyric(new String[]{"Abu", "Aib"}),
		Coronamic(new String[]{"Cma"}),
		Diaminopropinate(new String[]{"Dap", "Dpr"}),
		Enduracididine(new String[]{"End"}),
		Hydroxy_3_methylpentanoic_Acid(new String[]{"Hmp"}),
		Valeric_Acid(new String[]{"Vaa"}),
		Diaminobutyric_Acid(new String[]{"Dab"}),
		Pipecolic_Acid(new String[]{"Pip"}),
		MethylGlutamate(new String[]{"MeGlu"}),
		Pyruvate(new String[]{"Pyr"}),
		Alpha_keto_isocaproate(new String[]{"akIL"}), //not amino
		Alpha_keto_isovalerate(new String[]{"akIV"}), //not amino
		Oxopentoate(new String[]{"Oxo"}), //not amino acid
		Epoxi_OXODECANOIC_ACID(new String[]{"Aeo"}), //not amino
		OH_quinaldic_acid(new String[]{"OHQA"}),
		piperazic_acid(new String[]{"Piz", "Ppa", "PpA"}),
		quinoxaline_carboxylic_acid(new String[]{"QxCA"}),
		_3_hydroxy_anthranilic_acid(new String[]{"3HA", "Haa"}),
		//not really a thing? but in prism
		DOCK(new String[]{"Dock"}),
		SERORCYS(new String[]{"SerCysA"}),
		;
		
		private final String[] abbreviation;
		private AminoAcidEnum(final String[] abbreviation) {
			this.abbreviation = abbreviation;
		}
		/**
		 * Get the abbreviation associated with this domain.
		 * @return	the abbreviation 
		 */
		public String getAbbreviation() {
			return abbreviation[0];
		}
		public String[] getAbbreviations() {
			return abbreviation;
		}
	}
	
	public static AminoAcidEnum getAminoAcidEnumFromAbbreviation(String abbreviation) {
		for(AminoAcidEnum currentEnum : AminoAcidEnum.values()) {
			for(int i = 0;  currentEnum.getAbbreviations().length > i; i++){
				if(abbreviation.equals(currentEnum.getAbbreviations()[i])) {
					return(currentEnum);
				}
			}
		}
		System.err.println("Warning: amino acid domain abbreviation " + abbreviation + " does not exist!.");
		return(null);
	}
	
	/**
	 * An enum storing the source of origin of a chemical node
	 */
	public enum ChemicalNodeOrigin {
		GRAPE_AMINO_ACID,
		GRAPE_PK,
		PRISM_ADENYLATION,
		PRISM_ADENYLATION_KETOREDUCTASE,
		PRISM_ADENYLATION_NON_MODULE,
		PRISM_STARTER_ADENYLATION,
		PRISM_TRANS_ADENYLATION,
		PRISM_TRANS_ADENYLATION_INSERTION,
		PRISM_ACYLTRANSFERASE,
		PRISM_TRANS_AT,
		PRISM_TRANS_AT_INSERTION,
		PRISM_C_STARTER;
	}
	
	/**
	 * Enums for polyketides
	 */
	public enum PKOxidationStateEnum {
		KETONE("K"),
		HYDROXYL("H"),
		DOUBLEBOND("="),
		SINGLEBOND("-");
		private final String abbreviation;
		private PKOxidationStateEnum(final String abbreviation) {
			this.abbreviation = abbreviation;
		}
		
		public String getAbbreviation() {
			return(this.abbreviation);
		}
		@Override
		public String toString() {
			return this.abbreviation;
		}
		public static PKOxidationStateEnum getPKOxidationStateEnumFromName(String name){
			PKOxidationStateEnum oxiEnum = null;
			
			for(PKOxidationStateEnum oxidationState : PKOxidationStateEnum.values()) {
				  if(name.equals(oxidationState.name())) {
					  oxiEnum = oxidationState;
					  break;
				  }
			  }
			return oxiEnum;
		}
	}
	
	public enum PKSubstrateEnum {
		MBu,
		Bz,
		EtM,
		IBu,
		Mal,
		MeM,
		OMeMal,
		Pr;
		
		public static PKSubstrateEnum getPKSubstrateEnumFromName(String name){
			PKSubstrateEnum substrateEnum = null;
			
			  for(PKSubstrateEnum pkSubstrate : PKSubstrateEnum.values()) {
				  if(name.equals(pkSubstrate.toString())) {
					 substrateEnum = pkSubstrate;
					 break;
				  }
			  }
			return substrateEnum;
		}
	}
	
	public interface AdenylatedOther {
		public String getAbbreviation();
		public String name();
		
	}
	
	public enum CStarterEnum implements AdenylatedOther{ //currently the abbreviation is redundant
		DptA	("DptA"),
		LptA	("LptA"),
		CdaPS1	("CdaPS1"),
		SrfA	("SrfA"),
		LchAA	("LchAA"),
		XltA	("XltA"),
		ViscA	("ViscA"),
		WLIP	("WLIP"),
		MassA	("MassA"),
		PsoA	("PsoA"),
		SyrE	("SyrE"),
		ArfA	("ArfA"),
		TaaA	("TaaA"),
		SypA	("SypA"),
		OfaA	("OfaA"),
		SyfA	("SyfA"),
		LpmA	("LpmA"),
		HasV	("HasV"),
		PmxE	("PmxE"),
		NosA	("NosA"),
		LpiB	("LpiB"),
		McnA	("McnA"),
		PstA	("PstA"),
		FenC	("FenC"),
		GlbF	("GlbF"),
		EndA	("EndA"),
		AryB	("AryB"),
		antE	("antE"),
		act2_C1_start		("act2_C1_start"),
		bacil2_C1_start		("bacil2_C1_start"),
		cdaps1_C1_start		("cdaps1_C1_start"),
		fengy3_C1_start		("fengy3_C1_start"),
		liche1_C1_start		("liche1_C1_start"),
		prist1_C1_start		("prist1_C1_start"),
		surfa4_C1_start		("surfa4_C1_start"),
		syrin1_C1_start		("syrin1_C1_start"),
		tioR_C1_start		("tioR_C1_start"),
		Thalassospiramide	("Thalassospiramide"),
		Skyllamycin			("Skyllamycin"),
		Echinomycin			("Echinomycin"),
		WS9326				("WS9326");
		
		private final String abbreviation;
		private CStarterEnum(final String abbreviation) {
			this.abbreviation = abbreviation;
		}
		
		/**
		 * Get the abbreviation associated with this domain.
		 * @return	the abbreviation 
		 */
		public String getAbbreviation() {
			return(this.abbreviation);
		}
		public static CStarterEnum getCStarterMatch(String single){
			CStarterEnum match = null;
			for(CStarterEnum cStart : CStarterEnum.values()){
				if(single.contains(cStart.name())){
					match = cStart;
					break;
				}
			}
			return match;
		}
	}
	
	public enum KnownOther{
		ChrA("ChrA", "azotobactins chromophore", "C1=C(C(=CC2=C1N4C3C(=C2)NC(N3CCC4C(O)=O)=O)O)O"),
		ChrD("ChrD", "5,6-dihydropyoverdin chromophore", "C1=C(C(=CC2=C1N3C(C(N)C2)NCCC3C(O)=O)O)O"),
		ChrI("ChrI", "isopyoverdin chromophore", "C1=C(C(=CC2=C1N3C(C(=C2)N)NC(CC3)C(O)=O)O)O"),
		ChrP("ChrP", "pyoverdin chromophore", "C1=C(C(=CC2=C1N3C(C(=C2)N)NCCC3C(O)=O)O)O"),
		ChrAct("ChrAct", "actinomycin chromophore", "CC1=C2C(=C(C=C1)C(=O)O)N=C3C(=C(C(=O)C(=C3O2)C)N)C(=O)O"),
		Pyr("Pyr", "pyrrolidone", "C1CC(=O)NC1"),
		Dpy("Dpy", "Dolapyrrolidone", "COC1=CC(=O)NC1CC2=CC=CC=C2"),
		OH_Pyr("OH-Pyr", "hydroxy pyrrolidone", "C1[C@@H](CNC1=O)O"),
		Pya("Pya", "pyruvate", "CC(=O)C(=O)O"),
		dPyr("dPyr", "dehydropyrrolidone", "C1(CC(C(=CC(=O)O)N1)N)=O"),
		NSpd("NSpd", "norspermidine", "C(CN)CNCCCN"),
		Spd("Spd", "spermidine", "C(CCNCCCN)CN"),
		GSpd("GSpd", "guanylspermidine", "C(CCNCCCN=C(N)N)CN"),
		Ist("Ist", "isostatine", "CCC(C)C(C(CC(=O)O)O)N"),
		Nst("Nst", "norstatine", "CC(C)CC(C(C(=O)O)O)N"),
		Sta("Sta", "statine", "CC(C)CC(C(CC(=O)O)O)N"),
		DMOG("DMOG", "DHP-methyloxazolinyl group", "C1(=NC(C(O1)C)C(O)=O)C2=CC=CC(=C2O)O"),
		Choi("Choi", "2-carboxy-6-hydroxyoctahydroindole", "C1C(NC2C1CCC(C2)O)C(=O)O"),
		Ibu("Ibu", "4-amino-2,2-dimethyl-3-oxopentanoic acid", "CC(C(C(C(O)=O)(C)C)=O)N"),
		Hpa("Hpa", "hydroxypicolinic acid", "C1=CC(=C(N=C1)C(=O)O)O"),
		Map("Map", "2-methyl-3-aminopentanoic acid", "C(C(C(C(=O)O)C)N)C"),
		MdCP("MdCP", "N-methyldichloropyrrole-2-carboxylic acid", "C1(=CC(=C([N]1C)Cl)Cl)C(=O)O"),
		Me_AOA("Me-AOA", "methyl-2-aminooctanoic acid", "C(CCC(C(=O)O)N)CCCC"),
		Daz("Daz", "2,6-diamino-7-hydroxyazelaic acid", "C(CC(C(CC(=O)O)O)N)CC(C(=O)O)N"),
		Doe("Doe", "Dolaphenine", "C1=CC=C(C=C1)CC(C2=NC=CS2)N"),
		MCP("MCP", "N-methylchloropyrrole", "CN1C=C(C=C1C(=O)O)Cl"),
		CO("CO", "oxo group", "C=O"),
		DHMDA("DHMDA", "8,10-Dimethyl-9-hydroxy-7-methoxytridecadienoic acid", "C(C(C(CCC)C)O)(C(C=CC=CCC(=O)O)OC)C"),
		Agdha("Agdha", "4-amino-7-guanidino-2,3-dihydroxyheptanoic acid", "C(C(C(C(O)=O)O)O)(CCCNC(=N)N)N"),
		Ahp("Ahp", "3-amino-6-hydroxy-2-piperidone", "C1C(N(C(C(C1)N)=O)C(C(C)O)C(=O)O)O"),
		Dov("Dov", "Dolavaline", "CC(C)C(N(C)C)C(=O)O"),
		Eta("Eta", "Ethanolamine", "C(CO)N"),
		DHPT("DHPT", "dihydroxyphenylthiazol group", "C1(=NC(CS1)C(O)=O)C2=C(C(=CC=C2)O)O"),
		Hpoe("Hpoe", "2-hydroxyphenyl-2-oxo-ethanoic acid", "C1=CC(=CC=C1C(=O)C(=O)O)O"),
		HseL("HseL", "homoserine lactone", "C1COC(=O)C1N"),
		Aca("Aca", "Anticapsin", "C1CC(=O)[C@H]2[C@@H]([C@H]1C[C@@H](C(=O)O)N)O2"),
		NMe_Lan("NMe-Lan", "N-Methyl-Lanthionine", "C(SCC(C(=O)O)N)C(C(O)=O)NO"),
		PTTA("PTTA", "4-propenoyl-2-tyrosylthiazole acid", "C(O)(=O)C=CC1=CSC(=N1)C(N)CC2=CC=C(C=C2)O"),
		Mab("Mab", "2-methyl-3-aminobutanoic acid", "CC(C(C)N)C(=O)O"),
		NMe_Dha("NMe-Dha", "N-Methyl-dehydroalanine", "CNC(=C)C(=O)O"),
		Pda("Pda", "pentanedioic acid", "C(CC(=O)O)CC(=O)O"),
		Pha("Pha", "phenylacetic acid", "C1=CC=C(C=C1)CC(=O)O"),
		PT("PT", "phosphinothricin", "CP(=O)(CCC(C(=O)O)N)O"),
		Put("Put", "putrescine", "C(CCN)CN"),
		COOH_Qui("COOH-Qui", "2-carboxyquinoxaline", "C1=CC=C2C(=C1)N=CC(=N2)C(=O)O"),
		Azd("Azd", "aziridine dicarboxylic acid", "C1(C(N1)C(=O)O)C(=O)O"),
		N_OH_Hta("N-OH-Hta", "N-Hydroxy-histamine", "C1(CCCN1)CCNO"),
		PALOA("PALOA", "propenoyl-alanyloxazole acid", "C(C=CC1=COC(=N1)C(N)C)(=O)O"),
		PAOA("PAOA", "propenoyl-2-aminobutanoyloxazole acid", "C(C=CC1=COC(=N1)C(N)CC)(=O)O"),
		PMST("PMST", "propenoyl-O-methylserinylthiazole acid", "C(C=CC1=CSC(=N1)C(N)COC)(=O)O"),
		DMAH("DMAH", "dialkylmaleic Anhydride", "O=C(C([C@@H](CC(O)=O)O)=C1C)OC1=O"),
		Ac("Ac", "acetate", "CC(O)=O"),
		IV("IV", "isovalerate", "CC(C)CC(O)=O"),
		Deu("Deu", "3’-deoxy-4'-enamine uridine", "O=C(N1)C=CN(C2O/C(CC2O)=C/N)C1=O"),
		Hita("Hita", "6-hydroxy-tetrahydroisoquinoline carboxylic acid", "CC1NC(C(O)=O)CC2=C1C=CC(O)=C2"),
		dHis("dHis", "decarboxy histidine", "ONCCC1=CNC=N1"),
		MeOH("MeOH", "methanol", "CO"),
		Cba("Cba", "carbamic acid", "OC(N)=O"),
		Iva("Iva", "isovaleric acid", "O=C(O)CC(C)C"),
		;
		private final String abbreviation;
		private final String fullName;
		private final String smiles;
		private KnownOther(final String abbreviation, final String fullName, final String smiles) {
			this.abbreviation = abbreviation;
			this.fullName = fullName;
			this.smiles = smiles;
		}
		
		public String getAbbreviation() {
			return(this.abbreviation);
		}
		public String getfullName() {
			return(this.fullName);
		}
		public String getSmiles() {
			return(this.smiles);
		}
		public static KnownOther getKnownOtherMatch(String single){
			for(KnownOther other : KnownOther.values()){
				if(single.contains(other.getAbbreviation())){
					return other;
				}
			}
				System.err.println("Warning: known other abbreviation " + single + " does not exist!.");
			return null;
		}	
	}
}
