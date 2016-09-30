package ca.mcmaster.magarveylab.matching.inOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.Sugars;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AdenylatedOther;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.CStarterEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarAminoTransferEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarCMTEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarGeneEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarNMTEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarOMTEnum;
import ca.mcmaster.magarveylab.matching.enums.Type2Cyclases.Type2CyclaseGene;

public class Utility {
	
	public static Boolean isPK(String substrate){
		boolean isPK = false;
		if(substrate != null){
			for(PKSubstrateEnum check : PKSubstrateEnum.values()){
				if(substrate.equals(check.toString())){
					isPK = true;
				}
			}
			if(substrate.equals("unknown polyketide substrate")){
				isPK = true;
			}
		}
		
		return isPK;
	}
	public static PKSubstrateEnum getPKsubstrate(String substrate){
		PKSubstrateEnum pk = null;
		for(PKSubstrateEnum check : PKSubstrateEnum.values()){
			if(substrate.equals(check.toString())){
				pk = check;
			}
		}
		return pk;
	}
	public static Boolean isAA(String substrate){
		boolean isAA = false;
		if(substrate != null){
			for(AminoAcidEnum check : AminoAcidEnum.values()){
				if(substrate.equals(check.getAbbreviation())){
					isAA = true;
				}
			}
			if(substrate.equals("unknown amino acid substrate")){
				isAA = true;
			}
		}
		return isAA;
	}
	public static AminoAcidEnum getAAsubstrate(String substrate){
		AminoAcidEnum aa = null;
		if(substrate != null){
			substrate = substrate.replace("&Beta;-", "b");
			substrate = substrate.replace("&Alpha;-", "a");
			for(AminoAcidEnum check : AminoAcidEnum.values()){
				if(substrate.contains(check.toString()) || substrate.equals(check.getAbbreviation())){
					aa = check;
				}
			}	
		}
		return aa;
	}
	
	public static SiteSpecificTailoringEnum getSiteSpecificTailoringEnum(String mod){
		SiteSpecificTailoringEnum sste = null;
		if(mod != null){
			for(SiteSpecificTailoringEnum single : SiteSpecificTailoringEnum.values()){
				if(mod.contains(single.toString()) || mod.matches(single.getAbbreviation())){
					sste = single;
				}
			}
		}
		return sste;
	}
	
	public static ArrayList<SiteSpecificTailoringEnum> getSiteSpecificTailoringEnums(ArrayList<String> mods) {
		ArrayList<SiteSpecificTailoringEnum> sstes = new ArrayList<SiteSpecificTailoringEnum>();
		if(mods != null){
			for(String mod : mods){
				for(SiteSpecificTailoringEnum single : SiteSpecificTailoringEnum.values()){
					if(mod.contains(single.toString()) || mod.matches(single.getAbbreviation())){
						sstes.add(single);
					}
				}
			}
		}
		return sstes;
	}
	public static ArrayList<PKOxidationStateEnum> getPolyketideOxidationStates(ArrayList<String> pkOxi) {
		ArrayList<PKOxidationStateEnum> pkOxiEnums = new ArrayList<PKOxidationStateEnum>();
		if(pkOxi != null){
			for(String oxi : pkOxi){
				for(PKOxidationStateEnum single : PKOxidationStateEnum.values()){
					if(oxi.equals(single.toString())){
						pkOxiEnums.add(single);
					}
				}
			}	
		}
		
		return pkOxiEnums;
	}
	
	public static PKOxidationStateEnum getPolyketideOxidationStates(String pkOxi) {
		PKOxidationStateEnum pkOxiEnum = null;
		for(PKOxidationStateEnum single : PKOxidationStateEnum.values()){
			if(pkOxi.equals(single.toString())){
				pkOxiEnum = single;
			}
		}
		return pkOxiEnum;
	}
	
	public static NonSpecificTailoringEnum getNonSpecificTailoringEnum(String other) {
		NonSpecificTailoringEnum nste = null;
		if(other != null){
			for(NonSpecificTailoringEnum single : NonSpecificTailoringEnum.values()){
				if(other.matches(single.toString()) || other.matches(single.getAbbreviation())){
					nste = single;
				}
			}
			if(other.contains("SUGAR")){
				nste = NonSpecificTailoringEnum.GLYCOSYLTRANSFERASE;
			}
//			if(nste == null){
//				nste = getSugarEnzyme(other);
//			}
		}
		return nste;
	}
	
	public static NonSpecificTailoringEnum getNonSpecificTailoringEnumSource(String other) { //Temporary method to get tailors from 'Source'
		NonSpecificTailoringEnum nste = null;
		if(other != null){
			for(NonSpecificTailoringEnum single : NonSpecificTailoringEnum.values()){
				if(other.matches(single.getAbbreviation())){
					nste = single;
					continue;
				}
			}
		}
		return nste;
	}
	
	public static AcylAdenylatingSubstrates getAcylAdenylatingSubstrate(String other){
		AcylAdenylatingSubstrates match = null;
		for(AcylAdenylatingSubstrates substrate : AcylAdenylatingSubstrates.values()){
			if(other.equals(substrate.abbreviation())) match = substrate;
		}
		return match;
	}
	
	public static AdenylatedOther getAdenylatedGroup(String other) {
		List<AdenylatedOther> adenylatedOthers = new ArrayList<AdenylatedOther>(); //Not the fastest, how to make this a class list?

		adenylatedOthers.addAll(Arrays.asList(CStarterEnum.values()));
		AdenylatedOther ad = null;
		if(other != null){
			for(AdenylatedOther single : adenylatedOthers){
				if(other.matches(single.getAbbreviation())){
					ad = single;
				}
			}
		}
		return ad;
	}
	public static NonSpecificTailoringEnum getSugarEnzyme(String other){

		if(other != null){
			for(SugarAminoTransferEnum single : Sugars.SugarAminoTransferEnum.values()){
				if(other.equals(single.toString())){
					return NonSpecificTailoringEnum.SUGAR_AMINOTRANSFER;
				}
			}
			for(SugarNMTEnum single : Sugars.SugarNMTEnum.values()){
				if(other.equals(single.toString())){
					return NonSpecificTailoringEnum.SUGAR_NMETHYLTRANSFER;
				}
			}
			for(SugarCMTEnum single : Sugars.SugarCMTEnum.values()){
				if(other.equals(single.toString())){
					return NonSpecificTailoringEnum.SUGAR_CMETHYLTRANSFER;
				}
			}
			for(SugarOMTEnum single : Sugars.SugarOMTEnum.values()){
				if(other.equals(single.toString())){
					return NonSpecificTailoringEnum.SUGAR_OMETHYLTRNASFER;
				}
			}
		}
		return null;
	}
	
	public static SugarEnum getSugar(String single) {
		SugarEnum match = null;
		for(SugarEnum toMatch : SugarEnum.values()){
			if(toMatch.fullName().equals(single)){
				match = toMatch;
				break;
			}
		}
		return match;
	}
	
	public static SugarGeneEnum getSugarGene(String single){
		SugarGeneEnum match = null;
		for(SugarGeneEnum other : SugarGeneEnum.values()){
			if(other.abbreviation().equals(single)){
				match = other;
				break;
			}
		}
		return match;
	}
	public static Type2CyclaseGene getCyclaseGene(String domainFullName) {
		Type2CyclaseGene cyclaseGene = null;
		for(Type2CyclaseGene otherCyclaseGene : Type2CyclaseGene.values()){
			if(otherCyclaseGene.name().equals(domainFullName)){
				cyclaseGene = otherCyclaseGene;
				break;
			}
		}
		return cyclaseGene;
	}
	
	public static Boolean isHexoseGene(String single){
		single = single.toLowerCase();
		if (single.contains("glucose") || single.contains("mannose") || single.contains("gulose") || single.contains("glcnac")){
			return true;
		}
		
		return false;
	}
}
