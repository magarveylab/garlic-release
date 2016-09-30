package ca.mcmaster.magarveylab.matching.enums;

import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalScaffold;

public class Type2Cyclases {
	
	public enum Type2Scaffold implements ChemicalScaffold {
		FR_9001("CCC(C)C1OC2(OC1(C)C1C=CC3CC(CCC3C21C)C(O)O)C1(OC(C)O)C(O)OC(C)C1O", new Type2CyclaseGene[0][0]),
		Enterocin("OC(C1C2CC3CC(C1C(O)O3)C2C1CCCCO1)C1CCCCC1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{	Type2CyclaseGene.FAVORSKIIASE}}),
		AZ154("C1CCC(CC1)C1CCCC2CC3CC4CCCCC4CC3CC12", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7}, 
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1}, 
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),
		Fredericamycin1("C1C2CC3CCCCC3CC2CC11CC2CC3CCNCC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6a},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_3}}),
		Fredericamycin2("C1C2CC3CCNCC3CC2CC11CCC2CC3CCCCC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6a},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_3}}),
		Fredericamycin3("C1C2CC3CCCCC3CC2CC11CCC2CC3CCNCC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6a},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_3}}),
		Fredericamycin4("C1CCC2CC3CC4(CCC3CC2C1)CCC1CC2CCNCC2CC1C4", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6a},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_3}}),
		Aureolic_acid1("CC(O)C(O)C(O)C(O)C1CCC2CC3CCCCC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_2},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6b}}),		
		Lysolipin("C1CCC2OC3CC4CCC5CC6CCNCC6CC5C4CC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),	
		Rubromycin4("C1C2CC3CCCCC3CC2OC11CC2CC3CCOCC3CC2O1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),	
		Rubromycin3("C1C2CC3CCOCC3CC2OC11CCC2CC3CCCCC3CC2O1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),	
		Rubromycin2("C1CCC2CC3OC4(CCC3CC2C1)CCC1CC2CCOCC2CC1O4", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),	
		Rubromycin1("C1C2CC3CCCCC3CC2OC11CCC2CC3CCOCC3CC2O1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),
		Chartarin("C1CCC2C(C1)CC1COC3CCCC4COC2C1C34", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9, Type2CyclaseGene.CYCLASE_CLADE_10},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_2},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6a}}),
		Resistomycin("C1CC2CCC3CCC4CCCC5CC(C1)C2C3C45", new Type2CyclaseGene[0][0]),
		Erdacimycin("C1C2CCC3CCCCC3C2C2CCC3CCCCC3C12", new Type2CyclaseGene[0][0]),
		Urdamycin("C1CCC2CC3C(CCC4CCCCCC34)CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_4, Type2CyclaseGene.CYCLASE_CLADE_5b}}),
		Pradimicin2("C1CCC2CC3CC4C(CCC5CCCCC45)CC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),
		Pradimicin1("C1CCC2OC3CC4CCC5CCCCC5C4CC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_5a}}),
		Wailupemycin("C1CCC(OC1)C1CCCC2CCCCC12", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_3},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_1}}),
		Angucyclines4("C1CCC2CC3C(CC2C1)NCC1CCCOC31", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_4, Type2CyclaseGene.CYCLASE_CLADE_5b}}),
		Angucyclines3("C1CCC2CC3C(CCC4CCCOC34)CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_4, Type2CyclaseGene.CYCLASE_CLADE_5b}}),
		Angucyclines2("C1CCC2CC3C(CCC4CCCCC34)CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_4, Type2CyclaseGene.CYCLASE_CLADE_5b}}),
		Angucyclines1("C1CCC2CC3C(CC2C1)NCC1CCCCC31", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_4, Type2CyclaseGene.CYCLASE_CLADE_5b}}),
		Benzonaphthopyrrole("C1CCC2C(C1)CCC1C3CCCCC3COC21", new Type2CyclaseGene[0][0]),
		Kinobscurin("C1C2CCCCC2C2CC3CCCCC3CC12", new Type2CyclaseGene[0][0]),
		tetracyclines("C1CCC2CC3CC4CCCCC4CC3CC2C1", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_2},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6b_SUBTYPE_2}}),
		Nogalonate("OC1C2CCCCC2C(O)C2CCCCC12", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_2},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6b_SUBTYPE_1}}),
		Benziosochromanequinone("OC1C2CCCCC2CC2CCOCC12", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_3}}),
		UT_X26("C1CCC2CC34CCCC(CCC3CC2C1)C4", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_7},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_2},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_6b}}),
		AB649("C1C2CCCCC2C2CCC3CCCCC3C12", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9},
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_4, Type2CyclaseGene.CYCLASE_CLADE_5b}}),
		Juglomycin("OC1CCC(O)C2CCCCC12", new Type2CyclaseGene[][]{
				new Type2CyclaseGene[]{ Type2CyclaseGene.CYCLASE_CLADE_10, Type2CyclaseGene.CYCLASE_CLADE_8a, Type2CyclaseGene.CYCLASE_CLADE_8_9}});

		
		
		private final String smiles;
		private final Type2CyclaseGene[][] genes;
		
		private Type2Scaffold(final String smiles, final Type2CyclaseGene[][] genes){
			this.smiles = smiles;
			this.genes = genes;
		}
		
		public String smiles(){
			return smiles;
		}
		
		public Type2CyclaseGene[][] genes(){
			return genes;
		}
		
		public static int count(Type2CyclaseGene[][] genes) {
			int count = 0;
			for(int i = 0; genes.length > i; i++){
				count += genes[i].length;
			}
			return count;
		}
		
	}
	
	public enum Type2CyclaseGene{
				CYCLASE_CLADE_1("Type II polyketide cyclase, clade 1 (tetracenomycins/pentangular polyphenols, 3rd ring)", 
				"cyclase_clade_I.hmm", "Cyc", 207.8d),
		CYCLASE_CLADE_2("Type II polyketide cyclase, clade 2 (anthracyclines/tetracyclines/aureolic acids, 3rd ring)", 
				"cyclase_clade_II.hmm", "Cyc", 208.4d),
		CYCLASE_CLADE_3("Type II polyketide cyclase, clade 3 (benzoisochromanequinones, pyran ring)", 
				"cyclase_clade_III.hmm", "Cyc", 429.2d),
		CYCLASE_CLADE_4("Type II polyketide cyclase, clade 4 (angucyclines, 3rd/4th ring)", "cyclase_clade_IV.hmm", "Cyc", 100.0d),
		CYCLASE_CLADE_5a("Type II polyketide cyclase, clade 5a (pentangular polyphenols, 4th/5th rings)", 
				"cyclase_clade_Va.hmm", "Cyc", 100.0d),
		CYCLASE_CLADE_5b("Type II polyketide cyclase, clade 5b (tetracenomycins, 4th ring)", "cyclase_clade_Vb.hmm", "Cyc", 99.7d),
		CYCLASE_CLADE_6a("Type II polyketide cyclase, clade 6a (anthracyclines, 4th ring)", "cyclase_clade_VIa.hmm", "Cyc", 100.0d),
		CYCLASE_CLADE_6b("Type II polyketide cyclase, clade 6b (heterogeneous clade)", 
				"cyclase_clade_VIb.hmm", "Cyc", 188.1d),
		CYCLASE_CLADE_6b_SUBTYPE_1("Type II polyketide cyclase, clade 6b subtype 1 (anthracyclines, 4th ring)", 
				"", "Cyc", 25.0d),
		CYCLASE_CLADE_6b_SUBTYPE_2("Type II polyketide cyclase, clade 6b subtype 2 (tetracyclines/aureolic acids, 4th ring)", 
				"", "Cyc", 25.0d),
		CYCLASE_CLADE_7("Type II polyketide cyclase, clade 7 (tetracenomycins/pentangular polyphenols, 1st/2nd/3rd rings)", 
				"cyclase_clade_VII.hmm", "Cyc", 103.4d),
		CYCLASE_CLADE_8_9("Type II polyketide cyclase, clades 8/9 (multi-family, 1st/2nd/3rd rings)", 
				"cyclase_clade_VIII_IX.hmm", "Cyc", 162.6d),
		CYCLASE_CLADE_8a("Type II polyketide cyclase, clade 8a (benzoisochromanequinones, 1st/2nd rings)",
				"cyclase_clade_VIIIa.hmm", "Cyc", 100.0), //fix?
		CYCLASE_CLADE_10("Type II polyketide cyclase, clade 10 (angucyclines, 1st/2nd rings)", 
				"cyclase_clade_X.hmm", "Cyc", 150.0d),
		FAVORSKIIASE("Favorskiiase", "favorskiiase.hmm", "EncM", 700.0d);
	
		private final String name;
		private final String hmm;
		private final String abbreviation;
		private final double cutoff;
		
		private Type2CyclaseGene(final String name, final String hmm, final String abbreviation, final double cutoff) {
			this.name = name;
			this.hmm = hmm;
			this.abbreviation = abbreviation;
			this.cutoff = cutoff;
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
	
		public double cutoff() {
			return cutoff;	
		}
	}
}
