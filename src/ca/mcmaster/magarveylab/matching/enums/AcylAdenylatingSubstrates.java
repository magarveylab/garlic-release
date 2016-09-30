package ca.mcmaster.magarveylab.matching.enums;

/**
 * Substrates of a distinct clade of acyl-adenylating enzymes revealed by phylogenetic analysis. 
 * @author skinnider
 *
 */
public enum AcylAdenylatingSubstrates{
	
	// fatty acids
	MYRISTATE("myristate.hmm", "Myristate", "C<sub>14</sub>", "CCCCCCCCCCCCCC(I)=O"),
	LONG_CHAIN_FATTY_ACID("long_chain_fatty_acid.hmm", "Long-chain fatty acid", "C<sub>10</sub>", "O=C(I)CCCCCCCCC"), 
	SHORT_CHAIN_FATTY_ACID("short_chain_fatty_acid.hmm", "Short-chain fatty acid", "C<sub>6</sub>", "O=C(I)CCCCC"),
	_3_AMINONON_5_ENOIC_ACID("3-aminonon-5-enoic_acid.hmm", "3-aminonon-5-enoic acid", "C<sub>9</sub>", "CCC/C=C/CC(N)CC(I)=O"),

	// aromatic starters
	_2_3_DIHYDROXYBENZOIC_ACID("2_3_dihydroxybenzoic_acid.hmm", "2,3-dihydroxybenzoic acid", 
			"2,3DHB", "O=C(C1=C(O)C(O)=CC=C1)I"),
	SALICYLIC_ACID("salicylic_acid.hmm", "Salicylic acid", "Sal", "IC(C1=C(O)C=CC=C1)=O"),
	CINNAMIC_ACID("cinnamic_acid.hmm", "Cinnamic acid", "Cinn", "IC(/C=C/C1=CC=CC=C1)=O"),
	_3_AMINO_5_HYDROXYBENZOIC_ACID("AHBA.hmm", "3-amino-5-hydroxybenzoic acid", "AHBA", "O=C(I)C1=CC(N)=CC(O)=C1"),
	_3_FORMAMIDO_5_HYDROXYBENZOIC_ACID("FHBA.hmm", "3-formamido-5-hydroxybenzoic acid", "FHBA", 
			"IC(C1=C(O)C(NC=O)=CC=C1)=O"),
	_3_HYDROXYPICOLINIC_ACID("3-HPA.hmm", "3-hydroxypicolinic acid", "3-HPA", "IC(C1=C(O)C=CC=N1)=O"),
	_3_HYDROXYQUINALDIC_ACID("3-HQA.hmm", "3-hydroxyquinaldic acid", "3-HQA", "IC(C1=NC2=CC=CC=C2C=C1O)=O"),
	QUINOXALINE("quinoxaline.hmm", "Quinoxaline-2-carboxylic acid", "QX", "IC(C1=NC2=CC=CC=C2N=C1)=O"),
	PHENYLACETATE("phenylacetate.hmm", "Phenylacetate", "PAA", "O=C(I)CC1=CC=CC=C1"),
	PARA_HYDROXYBENZOIC_ACID("PHBA.hmm", "Para-hydroxybenzoic acid", "PHBA", "O=C(C1=CC=C(C=C1)O)I"),
	PARA_AMINOBENZOIC_ACID("PABA.hmm", "Para-aminobenzoic acid", "PABA", "O=C(C1=CC=C(C=C1)N)I"),
	
	// small starters
	BETA_AMINOALANINAMIDE("beta_aminoalaninamide.hmm", "&beta;-aminoalaninamide", "&beta;-Aln", "ICC(C(N)=O)N"),
	CYCLOHEXANECARBOXYLATE("CHC.hmm", "Cyclohexanecarboxylic acid", "CHC", "IC(C1CCCCC1)=O"),
	DIHYDROXYCYCLOHEXANECARBOXYLIC_ACID("DHCHC.hmm", "Dihydroxycyclohexanecarboxylic acid", 
			"DHCHC", "OC1C(CCC(C1)C(I)=O)O"),
	ALKYLMALONYL_COA("alkylmalonyl_CoA.hmm", "Alkylmalonyl-CoA", "CoL", "IC(CF)=O"),
	_3_HYDROXYBUTANOIC_ACID("3-hydroxybutanoic_acid.hmm", "3-hydroxybutanoic acid", "OHBu", "IC(CC(O)C)=O"),
	AMINOLEVULINIC_ACID("aminolevulinic_acid.hmm", "5-aminolevulinic acid", "5-ALA", "IC1=C(O)CCC1=O"),
	
	// alpha keto/alpha hydroxy acids
	PYRUVATE("pyruvate.hmm", "Pyruvate", "Pyr", "IC(C(C)OF)=O"),
	ALPHA_KETOISOVALERATE("alpha-ketoisovalerate.hmm", "&alpha;-ketoisovalerate", "&alpha;kV", "IC(C(C(C)C)OF)=O"),
	ALPHA_KETOISOCAPROATE("alpha-ketoisocaproate.hmm", "&alpha;-ketoisocaproate", "&alpha;kL", "IC(C(CC(C)C)OF)=O"),
	_3_METHYL_2_OXOPENTANOIC_ACID("3-methyl-2-oxopentanoate.hmm", "3-methyl-2-oxopentanoate", "&alpha;kI", "IC(C(OF)C(CC)C)=O"),
	PHENYLPYRUVATE("phenylpyruvate.hmm", "Phenylpyruvate", "&alpha;kF", "IC(C(OF)CC1=CC=CC=C1)=O"),
	
	//Different structures used for GRAPE
	LACTATE("", "lactate", "lac", "CC(C(O)=O)O"),
	VALERAIC_ACID("", "Valeraic acid", "VLA", "CCCCC(O)=O"),
	_3_HYDROXYLPATANOLIC_ACID("", "3-hydroxylpatanolic acid", "3-HLPA", "OC(CC(O)CC)=O"),
	BENZOIC_ACID("", "Benzoic acid", "BZA", "OC1=CC=CC=C1C(O)=O"),
	HYDROXY_3_METHYL_PENTANOIC_ACID("", "Hydroxy 3-methyl pentanoic acid", "HMP", "OC(C(O)C(CC)C)=O"),
	;
	
	private final String hmm;
	private final String name;
	private final String abbreviation;
	private final String smiles;
	
	private AcylAdenylatingSubstrates(final String hmm, final String name, final String abbreviation, 
			final String smiles) {
		this.hmm = hmm;
		this.smiles = smiles;
		this.name = name;
		this.abbreviation = abbreviation;
	}

	public String hmm() { 
		return hmm; 
	}
	
	public String fullName() { 
		return name; 
	}
	
	public String abbreviation() { 
		return abbreviation; 
	}
	
	public String smiles() { 
		return smiles; 
	}
	
	public static AcylAdenylatingSubstrates getAcylAdenylatingSubstrateFromString(String name){
		for(AcylAdenylatingSubstrates other : AcylAdenylatingSubstrates.values()){
			if(name.equals(other.abbreviation()) || name.equals(other.fullName()) || name.equals(other.name())){
				return other;
			}
		}
		System.err.println("Warning: acyl adenylating domain " + name + " does not exist!.");
		return null;
	}
}
