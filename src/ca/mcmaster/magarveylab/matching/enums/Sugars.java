package ca.mcmaster.magarveylab.matching.enums;

public class Sugars {
	
	public enum SugarEnum {
		L_ACULOSE("L-aculose", "CC1O[C@H](C=CC1=O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum.OXIDOREDUCTASE, SugarGeneEnum.DEOXY_GTR }),
		L_CINERULOSE_A("L-cinerulose A", "CC1O[C@H](CCC1=O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum.DEOXY_GTR }),
				L_RHODINOSE("L-rhodinose", "C[C@@H]1O[C@@H](CC[C@@H]1O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE , SugarGeneEnum.DEOXY_GTR}),
		REDNOSE("Rednose", "CC1OC(C(N)=CC1=O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE , SugarGeneEnum.DEOXY_GTR}),
		L_CINERULOSE_B("L-cinerulose B", "CC1O[C@H]([C@@H](O)CC1=O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, 
				SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE , SugarGeneEnum.DEOXY_GTR}),
		O_METHYL_L_AMICETOSE("O-methyl-L-amicetose", "COC1CC[C@@H](O[C@H]1C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, 
				SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_4_O_METHYL_L_RHODINOSE("4-O-methyl-L-rhodinose", "CO[C@H]1CC[C@@H](O[C@H]1C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, 
				SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		L_DAUNOSAMINE("L-daunosamine", "C[C@@H]1OC(C[C@@H]([C@@H]1O)N)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum._3_AMINOTRANSFERASE , SugarGeneEnum.DEOXY_GTR}), 
		L_RISTOSAMINE("L-ristosamine", "CC1OC(CC(C1O)N)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE,  //achiral
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum._4_AMINOTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		D_DIGITOXOSE("D-digitoxose", "CC1OC(CC(C1O)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, //achiral
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE , SugarGeneEnum.DEOXY_GTR}),
		L_DIGITOXOSE("L-digitoxose", "CC1OC(CC(C1O)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE,  //achiral
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE , SugarGeneEnum.DEOXY_GTR}),
		_2_DEOXY_L_FUCOSE("2-deoxy-L-fucose", "CC1OC(CC(C1O)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE , SugarGeneEnum.DEOXY_GTR}),//achiral
		D_OLIVOSE("D-olivose", "CC1O[C@@H](C[C@H]([C@@H]1O)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE , SugarGeneEnum.DEOXY_GTR}),
		D_OLIOSE("D-oliose", "CC1O[C@H](C[C@H]([C@H]1O)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE , SugarGeneEnum.DEOXY_GTR}),
		_4_OXO_L_VANCOSAMINE("4-oxo-L-vancosamine", "C[C@@H]1OC(C[C@](N)(C1=O)C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum._4_AMINOTRANSFERASE, SugarGeneEnum.C_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		D_FOROSAMINE("D-forosamine", "CC1OC(CC[C@@H]1N(C)C)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_AMINOTRANSFERASE, SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),//achiral
		L_ACTINOSAMINE("L-actinosamine", "COC1C(OC(CC1N)O)C", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE , SugarGeneEnum.DEOXY_GTR}),//achiral
		L_VANCOSAMINE("L-vancosamine", "OC1O[C@H]([C@@H](O)[C@](C1)(N)C)C", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum._3_AMINOTRANSFERASE, SugarGeneEnum.C_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		L_VICENISAMINE("L-vicenisamine", "CN[C@@H]1C(CC(OC1C)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_AMINOTRANSFERASE, 
				SugarGeneEnum.N_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		D_CHALCOSE("D-chalcose", "CO[C@H]1C[C@H](OC([C@@H]1O)O)C", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_AMINOTRANSFERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE, SugarGeneEnum.OXIDATIVE_DEAMINASE , SugarGeneEnum.DEOXY_GTR}),
		D_MYCAROSE("D-mycarose", "CC1OC(C[C@](O)([C@H]1O)C)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum.C_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		L_OLEANDROSE("L-oleandrose", "CO[C@H]1C[C@H](O[C@H]([C@@H]1O)C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		OLIVOMOSE("Olivomose", "COC1C(CC(OC1C)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),//achiral
		D_MYCOSAMINE("D-mycosamine", "C[C@H]1O[C@H]([C@H]([C@H]([C@@H]1O)N)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._3_4_ISOMERASE, SugarGeneEnum._3_AMINOTRANSFERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_4_DEOXY_4_THIO_D_DIGITOXOSE("4-deoxy-4-thio-D-digitoxose ", "CC1O[C@H](C[C@H](C1S)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE,
				SugarGeneEnum.EPIMERASE, SugarGeneEnum.THIOSUGAR_SYNTHASE , SugarGeneEnum.DEOXY_GTR}),
		D_FUCOFURANOSE("D-fucofuranose", "C[C@H]([C@H]1O[C@H]([C@H](C1O)O)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE , SugarGeneEnum.DEOXY_GTR}),
		D_FUCOSE("D-fucose", "CC1OC([C@@H]([C@H]([C@H]1O)O)O)O", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum._4_KETOREDUCTASE , SugarGeneEnum.DEOXY_GTR}),		
		L_RHAMNOSE("L-rhamnose", "C[C@@H]1O[C@@H]([C@@H]([C@@H]([C@H]1O)O)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE , SugarGeneEnum.DEOXY_GTR}),
		_4_N_ETHYL_4_AMINO_3_O_METHOXY_2_4_5_TRIDEOXYPENTOSE("4-N-ethyl-4-amino-3-O-methoxy-2,4,5-trideoxypentose", 
				"CCN[C@H]1CO[C@H](C[C@H]1OC)O", new SugarGeneEnum[] { SugarGeneEnum.UDP_DECARBOXYLASE, SugarGeneEnum.UDP_DEHYDROGENASE,
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_AMINOTRANSFERASE, 
				SugarGeneEnum.N_ETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		D_3_N_METHYL_4_O_METHYL_L_RISTOSAMINE("D-3-N-methyl-4-O-methyl-L-ristosamine", "CN[C@H]1CC(OC([C@@H]1OC)C)O", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum._4_AMINOTRANSFERASE,
				SugarGeneEnum.N_METHYLTRANSFERASE, SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		N_N_DIMETHYL_L_PYRROLOSAMINE("N,N-dimethyl-L-pyrrolosamine", "CC1OC(CC(C1N(C)C)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, 
				SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum._4_AMINOTRANSFERASE,
				SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),		//achiral
		D_DESOSAMINE("D-desosamine", "C[C@@H]1C[C@H](N(C)C)[C@@H](O)[C@H](O)O1", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._3_4_DEHYDRATASE, SugarGeneEnum._3_AMINOTRANSFERASE, 
				SugarGeneEnum.N_N_DIMETHYLTRANSFERASE, SugarGeneEnum.OXIDATIVE_DEAMINASE , SugarGeneEnum.DEOXY_GTR}),
		L_MEGOSAMINE("L-megosamine", "C[C@@H]1OC(C[C@@H](N(C)C)[C@H]1O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum._3_AMINOTRANSFERASE, SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		NOGALAMINE("Nogalamine", "IC1O[C@@H](C)[C@H](O)[C@@H](N(C)C)[C@@H]1O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum._3_AMINOTRANSFERASE, SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}), 
				//same as megosamine!
		L_RHODOSAMINE("L-rhodosamine", "CC1O[C@H](CC(N(C)C)[C@H]1O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum._3_AMINOTRANSFERASE, SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}), // same as above two!		
		D_ANGOLOSAMINE("D-angolosamine", "C[C@@H]1OC(CC(N(C)C)[C@H]1O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum._3_AMINOTRANSFERASE, SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		KEDAROSAMINE("Kedarosamine", "IC1O[C@@H](C)[C@@H](N(C)C)[C@@H](O)C1", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_AMINOTRANSFERASE, SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		L_NOVIOSE("L-noviose", "CC1(C)[C@H](O)[C@@H](O)[C@@H](O)C(O)O1", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum.C_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		L_CLADINOSE("L-cladinose", "C[C@H]1[C@H](O)[C@](C)(OC)CC(O)O1", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum.C_METHYLTRANSFERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_2_N_METHYL_D_FUCOSAMINE("2'-N-methyl-D-fucosamine", "CN[C@H]1C(O[C@@H]([C@@H]([C@@H]1O)O)C)O", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.N_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		D_DIGITALOSE("D-digitalose", "CO[C@H]1[C@H]([C@H](O[C@H]([C@@H]1O)O)C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_3_O_METHYL_RHAMNOSE("3-O-methyl-rhamnose", "COC1[C@H]([C@H](OC([C@H]1O)O)C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_2_O_METHYL_RHAMNOSE("2-O-methyl-rhamnose", "COC1[C@@H](OC([C@@H](C1O)O)C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}), //same
		_6_DEOXY_3_C_METHYL_L_MANNOSE("6-deoxy-3-C-methyl-L-mannose", "C[C@@H]1OC([C@@H]([C@](O)([C@H]1O)C)O)O", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum.C_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_4_6_DIDEOXY_4_HYDROXYLAMINO_D_GLUCOSE("4,6-dideoxy-4-hydroxylamino-D-glucose", "CC1OC(C(C(C1NO)O)O)O", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_AMINOTRANSFERASE , SugarGeneEnum.DEOXY_GTR}), //achiral
//				GLUCOSE("Glucose", "", new SugarGeneEnumFunctions[] { , SugarGeneEnum.DEOXY_GTR}), //XXX TODO Keep?
		_3_N_N_DIMETHYL_L_EREMOSAMINE("3-N,N-dimethyl-L-eremosamine", "OC1C[C@](C)(N(C)C)[C@@H](O)[C@H](C)O1", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum.C_METHYLTRANSFERASE, 
				SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		CHROMOSE("Chromose (4-O-acetyl-beta-D-oliose)", "CC1OC(CC(C1OC(C)=O)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE,
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.ACETYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}), //achiral
		_4_O_CARBAMOYL_D_OLIVOSE("4-O-carbamoyl-D-olivose", "CC1OC(CC(C1OC(N)=O)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE,
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.CARBAMOYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		D_RAVIDOSAMINE("D-ravidosamine", "C[C@H]1O[C@@H]([C@@H]([C@@H](N(C)C)[C@H]1O)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._3_4_ISOMERASE, SugarGeneEnum._3_AMINOTRANSFERASE,
				SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		MYCAMINOSE("3-N,N-dimethyl-D-mycosamine", "C[C@H]1O[C@H]([C@@H]([C@@H](N(C)C)[C@@H]1O)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._3_4_ISOMERASE, SugarGeneEnum._3_AMINOTRANSFERASE,
				SugarGeneEnum.N_N_DIMETHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}), //same
		_2_3_O_DIMETHYL_L_RHAMNOSE("2,3-O-dimethyl-L-rhamnose", "COC1[C@@H](OC([C@@H](C1OC)O)C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE,
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_2_4_O_DIMETHYL_L_RHAMNOSE("2,4-O-dimethyl-L-rhamnose", "CO[C@H]1C(O[C@H](C(C1O)OC)O)C", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE,
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_3_4_O_DIMETHYL_L_RHAMNOSE("3,4-O-dimethyl-L-rhamnose", "CO[C@H]1C(O[C@H](C(C1OC)O)O)C", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE,
				SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_2_THIOGLUCOSE("2-thioglucose", "OC1[C@H](S)[C@@H](O)[C@H](O)[C@@H](CO)O1", new SugarGeneEnum[] { 
				SugarGeneEnum.THIOSUGAR_SYNTHASE , SugarGeneEnum.DEOXY_GTR}),
		OLIVOMYCOSE("Olivomycose", "C[C@@H]1O[C@H](C[C@](O)([C@H]1OC(C)=O)C)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum.C_METHYLTRANSFERASE, 
				SugarGeneEnum.ACETYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_4_N_N_DIMETHYLAMINO_4_DEOXY_5_C_METHYL_L_RHAMNOSE("4-N,N-dimethylamino-4-deoxy-5-C-methyl-l-rhamnose", 
				"CN(C1C(C(C(OC1(C)C)O)O)O)C", new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum._4_AMINOTRANSFERASE, SugarGeneEnum.C_METHYLTRANSFERASE, 
				SugarGeneEnum.N_N_DIMETHYLTRANSFERASE, SugarGeneEnum.ACETYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),//achiral
		_2_3_4_TRI_O_METHYLRHAMNOSE("2,3,4-tri-O-methylrhamnose", "CO[C@@H]1[C@H](O[C@@H]([C@H]([C@H]1OC)OC)O)C", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, 
				SugarGeneEnum.EPIMERASE, SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_4_O_ACETYL_L_ARCANOSE("4-O-acetyl-L-arcanose", "OC1C[C@@](C)(OC)[C@H](OC(C)=O)[C@H](C)O1", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, SugarGeneEnum.C_METHYLTRANSFERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE, SugarGeneEnum.ACETYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_3_N_ACETYL_D_RAVIDOSAMINE("3-N-acetyl-D-ravidosamine", "C[C@H]1O[C@@H]([C@@H]([C@@H](N(C(C)=O)C)[C@H]1OC(C)=O)O)O", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._3_4_ISOMERASE, 
				SugarGeneEnum._3_AMINOTRANSFERASE,SugarGeneEnum.N_N_DIMETHYLTRANSFERASE, 
				SugarGeneEnum.ACETYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_3_O_CARBAMOYL_L_NOVIOSE("3-O-carbamoyl-L-noviose", "CC1([C@@H]([C@H]([C@H](C(O1)O)O)OC(N)=O)O)C", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.C_METHYLTRANSFERASE, 
				SugarGeneEnum.CARBAMOYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		L_NOGALOSE("L-nogalose", "COC1C(OC(C(C1(OC)C)OC)O)C", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE,
				SugarGeneEnum.C_METHYLTRANSFERASE, SugarGeneEnum.O_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),//achiral
		_4_O_ACETYL_D_RAVIDOSAMINE("4-O-acetyl-D-ravidosamine", "C[C@H]1O[C@@H]([C@@H]([C@@H](N(C)C)[C@H]1OC(C)=O)O)O", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._3_4_ISOMERASE, 
				SugarGeneEnum._3_AMINOTRANSFERASE,SugarGeneEnum.N_N_DIMETHYLTRANSFERASE, 
				SugarGeneEnum.ACETYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_3_O_CARBAMOYL_4_O_METHYL_L_NOVIOSE("3-O-carbamoyl-4-O-methyl-L-noviose", "CC1(C)[C@H](OC)[C@@H](OC(N)=O)[C@@H](O)C(O)O1", 
				new SugarGeneEnum[] { SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._2_3_DEHYDRATASE, 
				SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.C_METHYLTRANSFERASE, 
				SugarGeneEnum.O_METHYLTRANSFERASE, SugarGeneEnum.CARBAMOYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_3_N_ACETYL_4_O_ACETYL_D_RAVIDOSAMINE("3-N-acetyl-4-O-acetyl-D-ravidosamine", 
				"C[C@H]1O[C@@H]([C@@H]([C@@H](N(C(C)=O)C)[C@H]1OC(C)=O)O)O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._3_4_ISOMERASE, SugarGeneEnum._3_AMINOTRANSFERASE, 
				SugarGeneEnum.N_N_DIMETHYLTRANSFERASE, SugarGeneEnum.ACETYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_3_5_METHYL_2_PYRROLYLCARBONYL_4_O_METHYL_L_NOVIOSE("3-(5'-methyl-2'-pyrrolylcarbonyl-)4-O-methyl-L-noviose", 
				"CO[C@@H]1[C@@H](C([C@@H](OC1(C)C)O)O)OC(C2=CC=C(N2)C)=O", new SugarGeneEnum[] { 
				SugarGeneEnum._4_6_DEHYDRATASE, SugarGeneEnum._4_KETOREDUCTASE, SugarGeneEnum.EPIMERASE, 
				SugarGeneEnum.C_METHYLTRANSFERASE, SugarGeneEnum.O_METHYLTRANSFERASE, 
				SugarGeneEnum.PYRROLYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		MADUROSE("Madurose", "O[C@@H]1[C@H](O)[C@@](O)(C)[C@H](N)CO1", new SugarGeneEnum[] { SugarGeneEnum.UDP_DECARBOXYLASE,
				SugarGeneEnum.UDP_DEHYDROGENASE, SugarGeneEnum._4_AMINOTRANSFERASE, SugarGeneEnum.C_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		_4_N_METHYL_4_AMINO_3_O_METHOXY_2_4_5_TRIDEOXYPENTOSE("4-N-methyl-4-amino-3-O-methoxy-2,4,5-trideoxypentose", 
				"IO[C@@H]1C[C@H](OC)[C@@H](NC)CO1", new SugarGeneEnum[] { SugarGeneEnum.UDP_DECARBOXYLASE, SugarGeneEnum.UDP_DEHYDROGENASE,
				SugarGeneEnum._2_3_DEHYDRATASE, SugarGeneEnum._3_KETOREDUCTASE, SugarGeneEnum._4_AMINOTRANSFERASE, 
				SugarGeneEnum.N_METHYLTRANSFERASE , SugarGeneEnum.DEOXY_GTR}),
		GLUCOSE("Glucose", "OC[C@H]1OC([C@@H]([C@H]([C@@H]1O)O)O)O", new SugarGeneEnum[] {SugarGeneEnum.HEXOSE_GTR}),
		GLCNAC("N-acetylglucosamine", "O=C(C)N[C@@H]1[C@H]([C@@H]([C@@H](CO)OC1O)O)O", new SugarGeneEnum[] {SugarGeneEnum.HEXOSE_GTR}),
		MANNOSE("Mannose", "OC(O1)[C@@H](O)[C@@H](O)[C@H](O)[C@H]1CO", new SugarGeneEnum[] {SugarGeneEnum.HEXOSE_GTR}),
		GULOSE("Gulose", "O[C@H](C(O)O1)[C@@H](O)[C@H](O)[C@@H]1CO", new SugarGeneEnum[] {SugarGeneEnum.HEXOSE_GTR}),
		DEOXY("DeoxySugar", "", new SugarGeneEnum[] {SugarGeneEnum.DEOXY_GTR});
					
		private final String name;
		private final String smiles;
		private SugarGeneEnum[] genes;
		
		private SugarEnum(final String name, final String smiles, final SugarGeneEnum[] genes) {
			this.name = name;
			this.smiles = smiles;
			this.genes = genes;
		}
		
		/**
		 * Get the HTML-friendly name of the sugar. 
		 */
		public String fullName() {
			return name;
		}
		
		/**
		 * Get the SMILES of a sugar.
		 * @return		sugar SMILES, with attachment site labelled with iodine 
		 */
		public String smiles() {
			return smiles;
		}
		
		/**
		 * Get the sugar biosynthesis gene types required to biosynthesize a particular sugar. 
		 * @return		biosynthetic gene functionalities required for biosynthesis 
		 */
		public SugarGeneEnum[] genes() {
			return genes;
		}
	}
	
	public enum SugarGeneEnum {
		_4_6_DEHYDRATASE(		"4_6_dehydratase.hmm", "4,6-dehydratase", "4,6DH", 400.0d),
		_2_3_DEHYDRATASE(		"2_3_dehydratase.hmm", "2,3-dehydratase", "2,3DH", 100.0d),
		_3_4_DEHYDRATASE(		"3_4_dehydratase.hmm", "3,4-dehydratase", "3,4DH", 600.0d),
		_3_KETOREDUCTASE(		"3_ketoreductase.hmm", "3-ketoreductase", "3KR", 200.0d),
		_4_KETOREDUCTASE(		"4_ketoreductase.hmm", "4-ketoreductase", "4KR", 190.0d),
		EPIMERASE(				"epimerase.hmm", "Epimerase", "E", 160.0d),
		_3_4_ISOMERASE(			"3_4_ketoisomerase.hmm", "3,4-isomerase", "3,4IM", 150.0d),
		_3_AMINOTRANSFERASE(	"3_aminotransferase.hmm", "3-aminotransferase", "3-AmT", 451.0d),
		_4_AMINOTRANSFERASE(	"4_aminotransferase.hmm", "4-aminotransferase", "4-AmT", 350.0d),
		C_METHYLTRANSFERASE(	"sugar_c_methyltransferase.hmm", "C-methyltransferase", "CMT", 400.0d),
		N_METHYLTRANSFERASE(	"sugar_n_methyltransferase.hmm", "N-methyltransferase", "NMT", 180.0d),
		N_N_DIMETHYLTRANSFERASE("n_n_dimethyltransferase.hmm", "N,N-dimethyltransferase", "N,N-MT", 200.0d),
		O_METHYLTRANSFERASE(	"sugar_o_methyltransferase.hmm", "O-methyltransferase", "OMT", 147.0d),
		N_ETHYLTRANSFERASE(		"n_ethyltransferase.hmm", "N-ethyltransferase", "N-ET", 500.0d),
		ACETYLTRANSFERASE(		"acetyltransferase.hmm", "Acetyltransferase", "AcT", 160.0d),
		CARBAMOYLTRANSFERASE(	"carbamoyltransferase.hmm", "Carbamoyltransferase", "CarbT", 845.0d),
		PYRROLYLTRANSFERASE(	"pyrrolyltransferase.hmm", "Pyrrolyltransferase", "PyT", 350.0d),
		OXIDOREDUCTASE(			"oxidoreductase.hmm", "Oxidoreductase", "Ox", 500.0d),
		OXIDATIVE_DEAMINASE(	"oxidative_deaminase.hmm", "Oxidative deaminase", "OxDA", 500.0d),
		THIOSUGAR_SYNTHASE(		"thiosugar_synthase.hmm", "Thiosugar synthase", "ThiS", 300.0d),
		UDP_DECARBOXYLASE(		"decarboxylase_epimerase.hmm", "UDP-sugar decarboxylase", "UDP-DC", 400.0),
		UDP_DEHYDROGENASE(		"pentose_dehydrogenase.hmm", "UDP-sugar dehydrogenase", "UDP-DH", 400.0),
		HEXOSE_GTR(					"", "Hexose", "Hx", -1),
		DEOXY_GTR(					"", "Deoxy", "Dx", -1);
		
		private final String hmm;
		private final String name;
		private final String abbreviation;
		private final double cutoff;
		
		private SugarGeneEnum(final String hmm, final String name, final String abbreviation, final double cutoff) {
			this.hmm = hmm;
			this.name = name;
			this.abbreviation = abbreviation;
			this.cutoff = cutoff;
		}
		
		/**
		 * Replace the default enum toString method with formatted, output-friendly text describing this sugar gene. 
		 */
		public String fullName() {
			return name;
		}
		
		/**
		 * Get the abbreviation of this gene, for biosynthetic assembly graphs. 
		 * @return		abbreviation of this gene  
		 */
		public String abbreviation() {
			return abbreviation;
		}
			
		/**
		 * Get the name of the hidden Markov model corresponding to this gene. 
		 * @return		abbreviation of this gene type 
		 */
		public String hmm() {
			return hmm;
		}
		
		/**
		 * Get the bitscore cutoff for this hidden Markov model. 
		 * @return		appropriate hidden Markov model cutoff 
		 */
		public double cutoff() {
			return cutoff;
		}
	}
	
	public enum SugarOMTEnum {
		// From Angela's list, Apr 21, 2015
		AveBVII,
		CalS11,
		ChaM,
		ChmCI,
		ChmE,
		ChmF,
		CloP,
		CmmMIII,
		EryG,
		GerMIII,
		MycE,
		MycF,
		NanM,
		OleY,
		PlaM1,
		SnogL,
		SnogM,
		SnogY,
		SpnH,
		SpnI,
		SpnK,
		StaMB,
		TylE,
		TylF,
		busH,
		busI,
		busK,
		calS11,
		spnH,
		spnI,
		spnK;
	}
	
	public enum SugarNMTEnum {
		// From Angela's list, Apr 21, 2015
		// N-methyltransferase
		NscC5,
		RavNMT,
		SnogA,
		SnogX,
		StaMA,
		Strop_2195,
		Strop_2214,
		VinG,
		// N,N-dimethyltransferase
		DesVI,
		HedH,
		Med15,
		MegDIII,
		SpnS,
		TylM1;
	}
	
	public enum SugarCMTEnum {
		// From Angela's list, Apr 21, 2015
		AviG2,
		CmmC,
		DvaC,
		MtmC,
		NovU,
		SnogG2,
		TylC3;
	}
	
	public enum SugarAminoTransferEnum {
		// From Angela's list, Apr 21, 2015
		AknZ,
		AmphDII,
		CanA,
		ChmCIV,
		DesV,
		DvaB,
		NscC3,
		NysDII,
		PimC,
		RavAMT,
		StaI,
		Strop_2221,
		TylB,
		VcaB,
		VinF;
	}
	
}
