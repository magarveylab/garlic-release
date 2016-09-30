package ca.mcmaster.magarveylab.matching.chem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.Set;

import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown.Frame;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.MultipleAminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.StarterNode;
import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalScaffold;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalSubtype;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalType;
import ca.mcmaster.magarveylab.matching.enums.Monomers.CStarterEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.ChemicalNodeOrigin;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarGeneEnum;
import ca.mcmaster.magarveylab.matching.enums.Type2Cyclases.Type2CyclaseGene;


/**
 * An abstraction representing the chemical components of a natural product. This abstraction consists of unordered
 * chemical node strings and other fields, such as the presence of tailoring events.
 * This abstraction is intended to represent the information extracted from chemical structures (e.g. GRAPE) or from
 * biosynthetic gene clusters (e.g. PRISM).
 * @author gmchen cDejong
 */
public class ChemicalAbstraction { //TODO make superclass and extension classes for PRISM and GRAPE input, put all Genes (except for GRAPE's permuted genes) into same list (ie make interface for gene enums 
	private List<ChemicalNodeString> nodeStrings = new ArrayList<ChemicalNodeString>();
	private List<PKSubstrateEnum> pkSubstrateInsertion = new ArrayList<PKSubstrateEnum>();
	private List<NonSpecificTailoringEnum> nonSpecificTailoringReactions;
	private List<ExactPossibleSugar> sugars = new ArrayList<ExactPossibleSugar>(); //GRAPE
	private List<HashMap<SugarGeneEnum, Integer>> sugarGenes = new ArrayList<HashMap<SugarGeneEnum, Integer>>(); //BOTH (for now)
	private ChemicalType chemicalType = ChemicalType.UNKNOWN;
	private ChemicalSubtype chemicalSubtype = ChemicalSubtype.NONE;
	private ChemicalScaffold chemicalScaffold = null; //GRAPE
	private List<Type2CyclaseGene> type2CyclaseGenes = new ArrayList<Type2CyclaseGene>(); //PRISM
	private boolean allowSameNSTE = false;
	private boolean addedInsertions = false;
	private List<AcylAdenylatingSubstrates> acylAdenylatingSubstrates = new ArrayList<AcylAdenylatingSubstrates>();
	private Presence fattyAcid = Presence.FALSE;
	private List<CStarterEnum> fattyAcids = new ArrayList<CStarterEnum>(); 
	
	public enum Presence {
		TRUE, POSSIBLE, FALSE
	}
	
	public ChemicalAbstraction(){
		nonSpecificTailoringReactions = new ArrayList<NonSpecificTailoringEnum>();
	}
	public ChemicalAbstraction(boolean allowSameNSTE) {
		this.allowSameNSTE = allowSameNSTE;
		nodeStrings = new ArrayList<ChemicalNodeString>();
		nonSpecificTailoringReactions = new ArrayList<NonSpecificTailoringEnum>();
	}
	
	public ChemicalAbstraction(ArrayList<ChemicalNodeString> nodeStrings) {
		this.setNodeStrings(nodeStrings);
	}
	
	public ChemicalAbstraction(ArrayList<ChemicalNodeString> nodeStrings, boolean allowSameNSTE) {
		this.allowSameNSTE = allowSameNSTE;
		this.setNodeStrings(nodeStrings);
	}
	/**
	 * @return the nodeStrings
	 */
	public List<ChemicalNodeString> getNodeStrings() {
		if(pkSubstrateInsertion.size() > 0 && addedInsertions == false){
			addInsertions();
		}
		return nodeStrings;
	}
	/**
	 * @param nodeStrings the nodeStrings to set
	 */
	public void setNodeStrings(ArrayList<ChemicalNodeString> nodeStrings) {
		this.nodeStrings = nodeStrings;
	}
	
	/**
	 * Add a new node string
	 * @param nodeString
	 */
	public void addNodeString(ChemicalNodeString nodeString) {
		nodeStrings.add(nodeString);
	}
	
	public void addPKSubstrateInsertion(PKSubstrateEnum substrate){
		pkSubstrateInsertion.add(substrate);
	}
	
	public void addType2CyclaseGene(Type2CyclaseGene gene){
		type2CyclaseGenes.add(gene);
	}
	
	public List<Type2CyclaseGene> getType2CyclaseGenes(){
		return type2CyclaseGenes;
	}
	
	/**
	 * Adds a SugarGeneEnum to the HashMap of sugarGenes, will increase the count if it is there. 
	 * THIS METHOD IS MADE FOR PRISM RESULTS
	 * @param sugar
	 */
	public void addSugarGene(SugarGeneEnum sugar){
		if(sugarGenes.size() == 0){
			HashMap<SugarGeneEnum, Integer> toAdd = new HashMap<SugarGeneEnum, Integer>();
			toAdd.put(sugar, 1);
			sugarGenes.add(toAdd);
		}
		if(sugarGenes.get(0).containsKey(sugar)){
			sugarGenes.get(0).put(sugar, sugarGenes.get(0).get(sugar) + 1);
		}else{
			sugarGenes.get(0).put(sugar, 1);
		}
	}
	
	public List<HashMap<SugarGeneEnum, Integer>> getSugarGenes(){
		if(sugarGenes.size() < 1 && sugars.size() > 0){
			getSugarGenePermutations();
		}
		return sugarGenes;
	}
	
	public List<NonSpecificTailoringEnum> getNonSpecificTailorings() {
		return nonSpecificTailoringReactions;
	}
	
	public void addNonspecificTailoringEnum(NonSpecificTailoringEnum tailoringEnum) {
		// Do no)t permit duplicates
		for(NonSpecificTailoringEnum n : nonSpecificTailoringReactions) {
			if(!allowSameNSTE){
				if(n.equals(tailoringEnum)) {
					return;
				}	
			}			
		}
		nonSpecificTailoringReactions.add(tailoringEnum);
	}
	
	public void addExactPossibleSugars(ExactPossibleSugar exactPossibleSugars){
		sugars.add(exactPossibleSugars);
	}
	
	/**
	 * Get the number of chemical nodes present in this chemical abstraction
	 * @return The number of nodes
	 */
	public int getNumNodes() {
		int numNodes = 0;
		for(ChemicalNodeString s : nodeStrings) {
			numNodes += s.getChemicalNodes().size();
		}
		return numNodes;
	}
	
	public List<ExactPossibleSugar> getSugars(){
		return sugars;
	}
	
	private void addInsertions(){
		if(getNumNodes() < 1 || pkSubstrateInsertion.size() < 1) return;
		
		PKSubstrateEnum atInsert = pkSubstrateInsertion.get(0); // for now only consider the first  possible substrate
		
		for(ChemicalNodeString ns : nodeStrings){
			for(ChemicalNode cn : ns.getChemicalNodes()){
				if(cn.getOrigin().equals(ChemicalNodeOrigin.PRISM_TRANS_AT_INSERTION)){
					PKPieceNode pkp = (PKPieceNode)cn;
					pkp.setSubstrate(atInsert);				
				}
			}
		}
		addedInsertions = true;
	}
	
	@Override public String toString() {
		if(pkSubstrateInsertion.size() > 0 && addedInsertions == false){
			addInsertions();
		}
		String out = "Chemical Type: " + chemicalType.name() + "\nChemical subtype: "  + chemicalSubtype.name();
		if(chemicalScaffold != null) out +=  "\nChemical scaffold: " + chemicalScaffold.name();
		out += "\n";
		for(int i = 0; i < nodeStrings.size(); i++) {
			ChemicalNodeString nodeString = nodeStrings.get(i);
			for(int j = 0; j < nodeString.getChemicalNodes().size(); j++) {
				ChemicalNode node = nodeString.getChemicalNodes().get(j);
				if(node.getClass() == AminoAcidNode.class) {
					AminoAcidNode aminoAcidNode = (AminoAcidNode) node;
					if(aminoAcidNode.getAminoAcidType() != null) {
						out += aminoAcidNode.getAminoAcidType().getAbbreviation();
					}
					else {
						out += "Unk";
					}
					for(SiteSpecificTailoringEnum siteSpecificTailoring : aminoAcidNode.getSiteSpecificTailoring()) {
						out += "-" + siteSpecificTailoring.getAbbreviation();
					}
					out += "\t";
				}
				else if(node.getClass() == PKPieceNode.class) {
					PKPieceNode pkNode = (PKPieceNode) node;
					out += pkNode.getSubstrate() + "-" + pkNode.getPossibleOxidationStates();
					for(SiteSpecificTailoringEnum siteSpecificTailoring : pkNode.getSiteSpecificTailoring()) {
						out += "-" + siteSpecificTailoring.getAbbreviation();
					}
					out += "\t";
				}
				else if(node.getClass() == StarterNode.class) {
					out += node.toString() + "\t";
				}
				else if(node.getClass() == MultipleAminoAcidNode.class) {
					MultipleAminoAcidNode multipleNode = (MultipleAminoAcidNode) node;
					out += "Multiple amino acid: " + multipleNode.getMaxNumAminoAcids();
					out += "\t";
				}
			}
			if(i < nodeStrings.size() - 1) 
				out += "\n";
		}
		
		if(sugars.size() > 0){
			out += "\nPossible Sugars:    ";
			for(ExactPossibleSugar possibleSugars : sugars){
				for(SugarEnum single : possibleSugars.getAll()){
					out += (single.fullName());
					out += "\t";
				}
				out += "|| ";
			}
		}
		if(sugarGenes.size() > 0){
			out += "\nSugar genes:   ";
			for(Map<SugarGeneEnum, Integer> permutation : sugarGenes){
				for(Entry<SugarGeneEnum, Integer> gene : permutation.entrySet()){
					out += gene.getKey().fullName() + ": " + gene.getValue().toString() + " -- ";
				}
			}
		}
		
		if(type2CyclaseGenes.size() > 0){
			out += "\nType 2 Genes:     ";
			for(Type2CyclaseGene genes : type2CyclaseGenes){
				out += genes.name() + " -- ";
			}
		}
		
		if(acylAdenylatingSubstrates.size () > 0){
			out += "\nAcyl adenylating:     ";
			for(AcylAdenylatingSubstrates starter : acylAdenylatingSubstrates){
				out += starter.name() + " -- ";
			}
		}
		
		if(nonSpecificTailoringReactions.size() > 0) {
			out += "\nNon specific tailors:     ";
			for(NonSpecificTailoringEnum n : nonSpecificTailoringReactions) {
				out += n + " -- ";
			}
		}
		
		return(out);
	}
	
	public Integer numAminoAcidModules(){
		Integer numAAmod = 0;
		
		for(ChemicalNodeString ns : nodeStrings){
			for(ChemicalNode node : ns.getChemicalNodes()){
				if(node.isAAnode()){
					numAAmod ++;
				}
			}
		}
		
		return numAAmod;
	}
	
	public Integer numPolyketideModules(){
		Integer numAAmod = 0;
		
		for(ChemicalNodeString ns : nodeStrings){
			for(ChemicalNode node : ns.getChemicalNodes()){
				if(node.isPKNode()){
					numAAmod ++;
				}
			}
		}
		
		return numAAmod;
	}
	
	public void setChemicalType(ChemicalType chemicalType) {
		this.chemicalType = chemicalType;
	}
	
	public ChemicalType getChemicalType() {
		return chemicalType;
	}
	
	public void setChemicalSubtype(ChemicalSubtype chemicalSubtype){
		this.chemicalSubtype = chemicalSubtype;
	}
	
	public ChemicalSubtype getChemicalSubtype(){
		return chemicalSubtype;
	}
	
	public void setChemicalScaffold(ChemicalScaffold chemicalScaffold){
		this.chemicalScaffold = chemicalScaffold;
	}
	
	public ChemicalScaffold getChemicalScaffold(){
		return chemicalScaffold;
	}
	
	public void setFattyAcid(Presence fa){
		fattyAcid = fa;
	}
	
	public Presence getFattyAcidStatus(){
		return fattyAcid;
	}

	/**
	 * Currently only considers 5 sugars maximum (for permutation purposes)
	 */
	private void getSugarGenePermutations(){
		List<Set<SugarEnum>> sugarPossibles = new ArrayList<Set<SugarEnum>>();
		int sugarsToConsider = sugars.size();
		//OPT: Could uncomment for speed, but speed impact minor at best
		//if(sugars.size() > 5) sugarsToConsider = 5;
		for(int i = 0; sugarsToConsider > i; i++){
			ExactPossibleSugar single = sugars.get(i);
			if(single.getAll().size() > 0){
				sugarPossibles.add(single.getAll());
			}
		}
		
		List<ArrayList<SugarEnum>> allCombos = permuteSugars(sugarPossibles);
		List<ArrayList<SugarGeneEnum>> allSugarGenes = genesFromSugars(allCombos);
		sugarGenes = genesToHashMap(allSugarGenes);
	}
	
	public static List<HashMap<SugarGeneEnum, Integer>> genesToHashMap(List<ArrayList<SugarGeneEnum>> allSugarGenes) {
		List<HashMap<SugarGeneEnum, Integer>> sugarHashList = new ArrayList<HashMap<SugarGeneEnum, Integer>>();
		
		for(List<SugarGeneEnum> sugarGenes : allSugarGenes){
			HashMap<SugarGeneEnum, Integer> sugarHash = new HashMap<SugarGeneEnum, Integer>();
			for(SugarGeneEnum sugar : sugarGenes){
				if(sugarHash.containsKey(sugar)){
					sugarHash.put(sugar, sugarHash.get(sugar) + 1);
				}else{
					sugarHash.put(sugar, 1);
				}
			}
			sugarHashList.add(sugarHash);
		}
		return sugarHashList;
	}
	
	public static List<ArrayList<SugarGeneEnum>> genesFromSugars(List<ArrayList<SugarEnum>> allCombos) {
		List<ArrayList<SugarGeneEnum>> allSugarGenes = new ArrayList<ArrayList<SugarGeneEnum>>();
		
		for(List<SugarEnum> combo : allCombos){
			ArrayList<SugarGeneEnum> singleSugarGenes = new ArrayList<SugarGeneEnum>();
			
			for(SugarEnum sug : combo){
				for(SugarGeneEnum gene :sug.genes()){
					singleSugarGenes.add(gene);
				}
			}
			allSugarGenes.add(singleSugarGenes);
		}
		return allSugarGenes;
	}
	
	public static ArrayList<ArrayList<SugarEnum>> permuteSugars(List<Set<SugarEnum>> sugarPossibles) { //Lightly tested
		Set<SugarEnum> part = sugarPossibles.get(0);
		ArrayList<ArrayList<SugarEnum>> set = new ArrayList<ArrayList<SugarEnum>>();
		ArrayList<ArrayList<SugarEnum>> fullSet = new ArrayList<ArrayList<SugarEnum>>();
		if(sugarPossibles.size() > 1)	set = permuteSugars(sugarPossibles.subList(1, sugarPossibles.size()));
		if(set.size() > 0){
			for(ArrayList<SugarEnum> single : set){	
				for(Iterator<SugarEnum> it = part.iterator(); it.hasNext(); ){
					SugarEnum sugar = it.next();
					ArrayList<SugarEnum> toAdd = new ArrayList<SugarEnum>();
					toAdd.addAll(single);
					toAdd.add(sugar);
					fullSet.add(toAdd);
				}
			}
		}else{
			for(Iterator<SugarEnum> it = part.iterator(); it.hasNext(); ){
				ArrayList<SugarEnum> single = new ArrayList<SugarEnum>();
				single.add(it.next());
				fullSet.add(single);
			}
		}
		return fullSet;
	}
	
	public void setColinear(Frame frame) {
		ChemicalNodeString newString = new ChemicalNodeString();
		if(frame == Frame.POSITIVE || frame == Frame.NEGATIVE){
			if (frame == Frame.POSITIVE) {
				for(ChemicalNodeString ns : nodeStrings){
					for(ChemicalNode node : ns.getChemicalNodes()){
						newString.addNode(node);
					}
				}
			} else { //frame = Frame.NEGATIVE
				for(int nsindex=nodeStrings.size()-1;nsindex >= 0;nsindex--){
					ChemicalNodeString ns = nodeStrings.get(nsindex);
					for(ChemicalNode node : ns.getChemicalNodes()){
						newString.addNode(node);
					}
				}
			}
			List<ChemicalNodeString> newStringList = new ArrayList<ChemicalNodeString>();
			newStringList.add(newString);
			nodeStrings = newStringList;
		}
	}
	public void addStarter(AcylAdenylatingSubstrates substrate) {
		acylAdenylatingSubstrates.add(substrate);
	}
	
	public List<AcylAdenylatingSubstrates> getAcylAdenylatingSubstrates(){
		return acylAdenylatingSubstrates;
	}
	
	public boolean hasOnlyKnownStarts(){
		boolean onlyKnownStarts = true;
		for(ChemicalNodeString ns : nodeStrings){
			if(!ns.isKnownStart()) onlyKnownStarts = false;
		}
		return onlyKnownStarts;
	}
	
	public void removeIterativeNodeStrings(){
		for(int i = 0; i < nodeStrings.size(); i++) {
			for(int j = i-1; j >= 0; j--) {
				if(nodeStrings.get(i).equals(nodeStrings.get(j))) {
					if(nodeStrings.get(i).getChemicalNodes().size() > 1){
						nodeStrings.remove(i);
						i--;
						break;	
					}
				}
			}
		}
	}
	public void addFattyAcid(CStarterEnum cStarter) {
		fattyAcids.add(cStarter);		
	}
	public List<CStarterEnum> getFattyAcids(){
		return fattyAcids;
	}
}
