package ca.mcmaster.magarveylab.matching.algorithm.alignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction.Presence;
import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarGeneEnum;
import ca.mcmaster.magarveylab.matching.enums.Type2Cyclases.Type2CyclaseGene;

/**
 * Define an object that stores the output of an alignment object.
 * Contains two fields: the score, and a string representation of the alignment.
 * @author gmchen
 *
 */
public class AlignmentObject implements Comparable {
	private Double score = null;
	private Double rawScore = null;
	private Double relativeScore = null; //relative to self score such that score/selfScoreQuery score/selfScoreSubject and picks which is lower
	private Double selfScore = null;
	private Double sugarScore = null;
	private Double regulatorScore = null;
	private int lcsLength;
	private EditTranscript editTranscript;
	private String alignmentString;
	private Breakdown grapeBreakdown;
	private Breakdown prismBreakdown;
	private List<ChemicalNodeString> grapeChemicalNodes;
	private List<NonSpecificTailoringEnum> grapeNonspecificTailorings;
	private List<ChemicalNodeString> prismChemicalNodes;
	private List<NonSpecificTailoringEnum> prismNonspecificTailorings;
	
	public AlignmentObject(double score,
			double rawScore,
			double sugarScore,
			double regulatorScore,
			EditTranscript editTranscript, String alignmentString,
			Breakdown grapeBreakdown,
			Breakdown prismBreakdown
			) {
		this.score = score;
		this.rawScore = rawScore;
		this.sugarScore = sugarScore;
		this.regulatorScore = regulatorScore;
		this.editTranscript = editTranscript;
		this.alignmentString = alignmentString;
		this.grapeBreakdown = grapeBreakdown;
		this.prismBreakdown = prismBreakdown;
		this.setGrapeChemicalNodes(grapeBreakdown.getChemicalAbstraction().getNodeStrings());
		this.setGrapeNonspecificTailorings(grapeBreakdown.getChemicalAbstraction().getNonSpecificTailorings());
		this.setPrismChemicalNodes(prismBreakdown.getChemicalAbstraction().getNodeStrings());
		this.setPrismNonspecificTailorings(prismBreakdown.getChemicalAbstraction().getNonSpecificTailorings());
	}
	//TODO: CHANGE FROM GRAPE -> QUERY, CHANGE FROM PRISM -> SUBJECT
	public AlignmentObject(double score,
			double rawScore,
			double sugarScore,
			double regulatorScore,
			EditTranscript editTranscript, String alignmentString,
			Breakdown grapeBreakdown,
			Breakdown prismBreakdown,
			List<ChemicalNodeString> grapeChemicalNodes,
			List<ChemicalNodeString> prismChemicalNodes
			) {
		this.score = score;
		this.rawScore = rawScore;
		this.sugarScore = sugarScore;
		this.regulatorScore = regulatorScore;
		this.editTranscript = editTranscript;
		this.alignmentString = alignmentString;
		this.grapeBreakdown = grapeBreakdown;
		this.prismBreakdown = prismBreakdown;
		this.setGrapeChemicalNodes(grapeChemicalNodes);
		this.setGrapeNonspecificTailorings(grapeBreakdown.getChemicalAbstraction().getNonSpecificTailorings());
		this.setPrismChemicalNodes(prismChemicalNodes);
		this.setPrismNonspecificTailorings(prismBreakdown.getChemicalAbstraction().getNonSpecificTailorings());
	}
	
	@Override public String toString() {
		return alignmentString;
	}
	
	public Double getScore() {
		return score;
	}

	@Override
	public int compareTo(Object obj) {
		AlignmentObject otherAlignmentObject = (AlignmentObject) obj;
		return relativeScore.compareTo(otherAlignmentObject.getRelativeScore());
	}
	
	public Breakdown getGrapeBreakdown(){
		return grapeBreakdown;
	}
	
	public void setGrapeBreakdown(Breakdown breadkdown){
		this.grapeBreakdown = breadkdown;
	}
	
	public Breakdown getPrismBreakdown(){
		return prismBreakdown;
	}
	
	public void setPrismBreakdown(Breakdown breakdown){
		this.prismBreakdown = breakdown;
	}

	public List<ChemicalNodeString> getGrapeChemicalNodes() {
		return grapeChemicalNodes;
	}

	public void setGrapeChemicalNodes(List<ChemicalNodeString> grapeChemicalNodes) {
		this.grapeChemicalNodes = grapeChemicalNodes;
	}

	public List<NonSpecificTailoringEnum> getGrapeNonspecificTailorings() {
		return grapeNonspecificTailorings;
	}

	public void setGrapeNonspecificTailorings(
			List<NonSpecificTailoringEnum> grapeNonspecificTailorings) {
		this.grapeNonspecificTailorings = grapeNonspecificTailorings;
	}

	public List<ChemicalNodeString> getPrismChemicalNodes() {
		return prismChemicalNodes;
	}

	public void setPrismChemicalNodes(List<ChemicalNodeString> prismChemicalNodes) {
		this.prismChemicalNodes = prismChemicalNodes;
	}

	public List<NonSpecificTailoringEnum> getPrismNonspecificTailorings() {
		return prismNonspecificTailorings;
	}

	public void setPrismNonspecificTailorings(
			List<NonSpecificTailoringEnum> prismNonspecificTailorings) {
		this.prismNonspecificTailorings = prismNonspecificTailorings;
	}

	public Double getRawScore() {
		return rawScore;
	}

	public void setRawScore(double rawScore) {
		this.rawScore = rawScore;
	}
	public void setSelfScore(double selfScore){
		this.selfScore = selfScore;
	}
	public Double getSelfScore(){
		return selfScore;
	}
	
	public void setRelativeScore(double relativeScore){
		this.relativeScore = relativeScore;
	}
	
	public Double getRelativeScore(){
		return relativeScore;
	}
	
	public void setSugarScore(double sugarScore) {
		this.sugarScore = sugarScore;
	}
	
	public Double getSugarScore(){
		return sugarScore;
	}
	
	public void setRegulatorScore(double regulatorScore){
		this.regulatorScore = regulatorScore;
	}
	
	public Double getRegulatorScore(){
		return regulatorScore;
	}

	public int getLcsLength() {
		return lcsLength;
	}

	public void setLcsLength(int mcsLength) {
		this.lcsLength = mcsLength;
	}
	
	public EditTranscript getEditTranscript(){
		return editTranscript;
	}
	
	public void setEditTranscript(EditTranscript editTranscript){
		this.editTranscript = editTranscript;
	}
	

	/**
	 * Get a string representation of the alignment
	 * @param queryNodeStrings
	 * @param queryAbstraction
	 * @param subjectNodeStrings
	 * @param subjectAbstraction
	 * @param bestEditTranscript
	 * @return A string that contains two lines separated by \n. The first line is the PRISM string, and the second line is the GRAPE string.
	 */
	public static String getAlignmentString(
			List<ChemicalNodeString> queryNodeStrings,
			ChemicalAbstraction queryAbstraction,
			List<ChemicalNodeString> subjectNodeStrings,
			ChemicalAbstraction subjectAbstraction,
			EditTranscript bestEditTranscript) {
		
		String out = "";
		List<ChemicalNode> queryString = new ArrayList<ChemicalNode>();
		List<Integer> queryEndNodeIndices = new ArrayList<Integer>();
		List<Integer> queryStartNodeIndices = new ArrayList<Integer>();
		for(ChemicalNodeString nodeString : queryNodeStrings) {
			for(int i = 0; i < nodeString.getChemicalNodes().size(); i++) {
				queryString.add(nodeString.getChemicalNodes().get(i));
				if(i == 0) {
					queryStartNodeIndices.add(queryString.size()-1);
				}
				if(i == nodeString.getChemicalNodes().size() - 1) {
					queryEndNodeIndices.add(queryString.size()-1);
				}
			}
		}
		
		List<ChemicalNode> subjectString = new ArrayList<ChemicalNode>();
		List<Integer> subjectEndNodeIndices = new ArrayList<Integer>();
		List<Integer> subjectStartNodeIndices = new ArrayList<Integer>();
		for(ChemicalNodeString nodeString : subjectNodeStrings) {
			for(int i = 0; i < nodeString.getChemicalNodes().size(); i++) {
				subjectString.add(nodeString.getChemicalNodes().get(i));
				if(i == 0) {
					subjectStartNodeIndices.add(subjectString.size() - 1);
				}
				if(i == nodeString.getChemicalNodes().size() - 1) {
					subjectEndNodeIndices.add(subjectString.size() - 1);
				}
			}
		}
		
		int length = 12;
		
		String queryText = "";
		String subjectText = "";
		int queryIndex = 0;
		int subjectIndex = 0;
		for(int i = 0; i < bestEditTranscript.getEditOperations().size(); i++) {
			switch(bestEditTranscript.getEditOperations().get(i)) {
			case I:
				if(subjectStartNodeIndices.contains(subjectIndex))	{
					subjectText += "|";
				}
				else {
					subjectText += " ";
				}
				subjectText += getMonospacedString(subjectString.get(subjectIndex).toString(), length-2);
				if(subjectEndNodeIndices.contains(subjectIndex))	{
					subjectText += "|";
				}
				else {
					subjectText += " ";
				}
				subjectIndex++;
				queryText += getMonospacedString("", length);
				break;
			case D:
				if(queryStartNodeIndices.contains(queryIndex))	{
					queryText += "|";
				}
				else {
					queryText += " ";
				}
				queryText += getMonospacedString(queryString.get(queryIndex).toString(), length-2);
				if(queryEndNodeIndices.contains(queryIndex))	{
					queryText += "|";
				}
				else {
					queryText += " ";
				}
				queryIndex++;
				subjectText += getMonospacedString("", length);
				break;
			case M:
				if(queryStartNodeIndices.contains(queryIndex))	{
					queryText += "|";
				}
				else {
					queryText += " ";
				}
				queryText += getMonospacedString(queryString.get(queryIndex).toString(), length-2);
				if(queryEndNodeIndices.contains(queryIndex))	{
					queryText += "|";
				}
				else {
					queryText += " ";
				}
				queryIndex++;
				if(subjectStartNodeIndices.contains(subjectIndex))	{
					subjectText += "|";
				}
				else {
					subjectText += " ";
				}
				subjectText += getMonospacedString(subjectString.get(subjectIndex).toString(), length-2);
				if(subjectEndNodeIndices.contains(subjectIndex))	{
					subjectText += "|";
				}
				else {
					subjectText += " ";
				}
				subjectIndex++;
				if(subjectIndex >= subjectString.size())
					subjectIndex = subjectString.size()-1;
				if(queryIndex >= queryString.size())
					queryIndex = queryString.size()-1;
				break;
			}
		}
		queryText += "Chemical Type: " + queryAbstraction.getChemicalType() + " Chemical Subtype: " + queryAbstraction.getChemicalSubtype() + " ";
		if(queryAbstraction.getChemicalScaffold() != null) queryText += "Chemical Scaffold: " + queryAbstraction.getChemicalScaffold() + " ";
		
		subjectText += "Chemical Type: " + subjectAbstraction.getChemicalType() + " Chemical Subtype: " + subjectAbstraction.getChemicalSubtype() + " ";
		if(subjectAbstraction.getChemicalScaffold() != null) subjectText += "Chemical Scaffold: " + subjectAbstraction.getChemicalScaffold() + " ";
		
		if(queryAbstraction.getNonSpecificTailorings().size() > 0) {
			queryText += "Tailoring: ";
		}
		for(NonSpecificTailoringEnum grapeTailor : queryAbstraction.getNonSpecificTailorings()) {
			queryText += grapeTailor + " ";
		}
		if(subjectAbstraction.getNonSpecificTailorings().size() > 0) {
			subjectText += "Tailoring: ";
		}
		for(NonSpecificTailoringEnum prismTailor : subjectAbstraction.getNonSpecificTailorings()) {
			subjectText += prismTailor + " ";
		}
		if(queryAbstraction.getAcylAdenylatingSubstrates().size() > 0){
			queryText += "Acyl Adenylating: ";
		}
		for(AcylAdenylatingSubstrates grapeStarter : queryAbstraction.getAcylAdenylatingSubstrates()) {
			queryText += grapeStarter.abbreviation() + " ";
		}
		if(subjectAbstraction.getAcylAdenylatingSubstrates().size() > 0){
			subjectText += "Acyl Adenylating: ";
		}
		for(AcylAdenylatingSubstrates subjectStarter : subjectAbstraction.getAcylAdenylatingSubstrates()) {
			subjectText += subjectStarter.abbreviation() + " ";
		}
		
		if(queryAbstraction.getFattyAcidStatus() != Presence.FALSE){
			if(queryAbstraction.getFattyAcidStatus() != Presence.TRUE){
				queryText += "FattyAcid ";
			}else{
				queryText += "FattyAcid(Possible)";
			}
		}
		if(subjectAbstraction.getFattyAcidStatus() != Presence.FALSE){
			if(subjectAbstraction.getFattyAcidStatus() != Presence.TRUE){
				subjectText += "FattyAcid ";
			}else{
				subjectText += "FattyAcid(Possible) ";
			}
		}
		if(queryAbstraction.getSugarGenes().size() > 0){
			if(queryAbstraction.getSugars().size() > 0){
				queryText += "NumSugars: " + queryAbstraction.getSugars().size();
			}else{
				queryText += "SugarGenes: ";
				for(Entry<SugarGeneEnum, Integer> gene: queryAbstraction.getSugarGenes().get(0).entrySet()){
					queryText += gene.getKey() + " (" + gene.getValue() + ") ";
				}
			}
		}
		if(subjectAbstraction.getSugarGenes().size() > 0){
			if(subjectAbstraction.getSugars().size() > 0){
				subjectText += "NumSugars: " + subjectAbstraction.getSugars().size();
			}
			else{
				subjectText += "SugarGenes: ";
				for(Entry<SugarGeneEnum, Integer> gene: subjectAbstraction.getSugarGenes().get(0).entrySet()){
					subjectText += gene.getKey() + " (" + gene.getValue() + ") ";
				}
			}
		}
		
		if(queryAbstraction.getType2CyclaseGenes().size() > 0){
			queryText += "Type 2 Cyclase Genes: ";
			for(Type2CyclaseGene gene: queryAbstraction.getType2CyclaseGenes()){
				queryText += gene.name() + " ";
			}
		}
		
		if(subjectAbstraction.getType2CyclaseGenes().size() > 0){
			subjectText += "Type 2 Cyclase Genes: ";
			for(Type2CyclaseGene gene: subjectAbstraction.getType2CyclaseGenes()){
				subjectText += gene.name() + " ";
			}
		}
			
		out += queryText;
		out += "\n" + subjectText;
		String scoreText = "";
		DecimalFormat df = new DecimalFormat("#0.0000");
		
		for(int i = 0; i < bestEditTranscript.getScores().size(); i++) {
			double individualScore = bestEditTranscript.getScores().get(i);
			if(i>0) {
				individualScore -= bestEditTranscript.getScores().get(i-1);
			}
			scoreText += " " + getMonospacedString(""+df.format(individualScore), length-2) + " ";
		}
		out += "\n" + scoreText;
		return out;
	}
	
	/**
	 * Given a string, return a strung of a given length by truncating (if the original string is longer)
	 * or adding trailing spaces. Always end with a space.
	 * @param s
	 * @param length
	 * @return
	 */
	public static String getMonospacedString(String s, int length) {
		String out = s;
		if(out.length() < length) {
			while(out.length() < length) {
				out += " ";
			}
		}
		if(out.length() > length) {
			out = out.substring(0, length-1) + " ";
		}
		return out;
	}
	
}
