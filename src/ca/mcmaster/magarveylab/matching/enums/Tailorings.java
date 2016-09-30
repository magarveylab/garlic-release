package ca.mcmaster.magarveylab.matching.enums;

public class Tailorings {
	/**
	 * Enum containing tailoring enzymes that PRISM or GRAPE cannot link to a specific monomer.
	 */
	public enum NonSpecificTailoringEnum {
		// Tailoring enzymes
				GLYCOSYLTRANSFERASE("GTr"),
				CHLORINATION("Cl"),
				SULFOTRANSFERASE("ST"), // inactivated!
				TRYPTOPHAN_DIOXYGENASE("TrD"),
				PROLINE_DEHYDROGENASE("DHO"),
				ISOPENICILLIN_N_ACYLTRANSFERASE("IAT"),
				DEACETOXYCEPHALOSPORIN_C_SYNTHASE("DOACS"),
				P450("P450"),
				PRENYLATION("PRN"), 
				//FATTY_ACID("Fat"),
				O_METHYLTRANSFERASE("OMT"),
				N_METHYLTRANSFERASE("NMT"),
				C_METHYLTRANSFERASE("CMT"),
				C_HYDROXYLATION("CH"),
				NITROREDUCTASE("NR"),
				THIAZOLE("THZ"),
				OXAZOLE("OXZ"),
				CYCLIZATION("CYC"),
				SULFUR_BETA_LACTAM("SULFUR_BETA_LACTAM"),
				//Sugar enzymes
				SUGAR_OMETHYLTRNASFER("SugarOMT"),
				SUGAR_NMETHYLTRANSFER("SugarNMT"),
				SUGAR_CMETHYLTRANSFER("SugarCMT"),
				SUGAR_AMINOTRANSFER("SugarAT"),
				//ribosomal tailorings
				LANTITHIONE_LINKAGE("LL"),
				PYRIDINE_CLYLYZATION("PC"),
				DURAMYCIN_LIKE_LINKAGE("DLL"),
				BOTTROMYCIN_LIKE_LINKAGE("BLL"),
				;				
				private final String abbreviation;
				private NonSpecificTailoringEnum(final String abbreviation) {
					this.abbreviation = abbreviation;
				}
				
				/**
				 * Get the abbreviation associated with this domain.
				 * @return	the abbreviation 
				 */
				public String getAbbreviation() {
					return(this.abbreviation);
				}
	}
	
	/**
	 * Enum containing tailoring enzymes specific to a given monomer whose specificity can be identified by both PRISM and GRAPE.
	 */
	public enum SiteSpecificTailoringEnum {
	
		O_METHYLTRANSFERASE("OMT"),
		N_METHYLTRANSFERASE("NMT"),
		C_METHYLTRANSFERASE("CMT"),
		NITROREDUCTASE("NR"),
		OXAZOLE("OXZ"),
		CYCLIZATION("CYC"),
		THIAZOLE("THZ"),
		//CHLORINATION("Cl"),
		THIOLESTERASE("TE"),
		//ribo
		LANTITHIONE_LINKAGE("LL"),
		PYRIDINE_CLYLYZATION("PC"),
		DURAMYCIN_LIKE_LINKAGE("DLL"),
		BOTTROMYCIN_LIKE_LINKAGE("BLL"),
		;
		
		private final String abbreviation;
		private SiteSpecificTailoringEnum(final String abbreviation) {
			this.abbreviation = abbreviation;
		}
		
		/**
		 * Get the abbreviation associated with this domain.
		 * @return	the abbreviation 
		 */
		public String getAbbreviation() {
			return(this.abbreviation);
		}
	}

	public static SiteSpecificTailoringEnum getSiteSpecificTailoringFromAbbreviation(String abbreviation) {
		for(SiteSpecificTailoringEnum currentEnum : SiteSpecificTailoringEnum.values()) {
			if(currentEnum.abbreviation.equals(abbreviation)) {
				return(currentEnum);
			}
			else if(abbreviation.equals("OXZ")) {
				return(SiteSpecificTailoringEnum.OXAZOLE);
			}
			else if(abbreviation.equals("THZ")) {
				return(SiteSpecificTailoringEnum.THIAZOLE);
			}
		}
		return(null);
	}

	public static NonSpecificTailoringEnum getNonSpecificTailoringFromAbbreviation(String abbreviation) {
		for(NonSpecificTailoringEnum currentEnum : NonSpecificTailoringEnum.values()) {
			if(currentEnum.abbreviation.equals(abbreviation)) {
				return(currentEnum);
			}
		}
		return(null);
	}
	
	public static SiteSpecificTailoringEnum getSiteSpecificTailoringFromName(String name) {
		for(SiteSpecificTailoringEnum currentEnum : SiteSpecificTailoringEnum.values()) {
			if(currentEnum.name().equals(name)) {
				return(currentEnum);
			}
		}
		return(null);
	}

	public static NonSpecificTailoringEnum getNonSpecificTailoringFromName(String name) {
		for(NonSpecificTailoringEnum currentEnum : NonSpecificTailoringEnum.values()) {
			if(currentEnum.name().equals(name)) {
				return(currentEnum);
			}
		}
		System.err.println("Warning: Found tailoring value " + name + " that does not match any tailoring domains");
		return(null);
	}
}