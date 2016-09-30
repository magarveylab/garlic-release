package ca.mcmaster.magarveylab.matching.inOut.out;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;

import ca.mcmaster.magarveylab.matching.algorithm.alignment.AlignmentObject;
import ca.mcmaster.magarveylab.matching.algorithm.alignment.EditTranscript;
import ca.mcmaster.magarveylab.matching.breakdowns.Breakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.GrapeBreakdown;
import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction.Presence;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNode;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.AminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.GeneticDomain;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.MultipleAminoAcidNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.PKPieceNode;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.StarterNode;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarGeneEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;

public class JSONout {
	private static ObjectMapper mapper = new ObjectMapper();

	public static ObjectNode toJSON(Breakdown queryBreakdown,
			Breakdown subjectBreakdown, AlignmentObject alignment, boolean fullJSON)
			throws IOException {
		
		ObjectNode node = mapper.createObjectNode();
		// cast these to the proper class
		if (queryBreakdown instanceof GrapeBreakdown) {
			GrapeBreakdown qBreakdown = (GrapeBreakdown) queryBreakdown;
			node.put("query", getNameDetails(qBreakdown.getName(), qBreakdown.getID(), queryBreakdown.getClass(), qBreakdown.getSmiles()));
		} else {
			PrismBreakdown qBreakdown = (PrismBreakdown) queryBreakdown;
			node.put("query", getNameDetails(qBreakdown));
		}
		
		if (subjectBreakdown instanceof GrapeBreakdown) {
			GrapeBreakdown sBreakdown = (GrapeBreakdown) subjectBreakdown;
			node.put("subject", getNameDetails(sBreakdown.getName(), sBreakdown.getID(), subjectBreakdown.getClass(), sBreakdown.getSmiles()));
		} else {
			PrismBreakdown sBreakdown = (PrismBreakdown) subjectBreakdown;
			node.put("subject", getNameDetails(sBreakdown));
		}
		
		if(fullJSON){
			ObjectNode queryCAObjNode = mapper.convertValue(queryBreakdown.getChemicalAbstraction(), ObjectNode.class);
			ObjectNode subjectCAObjNode = mapper.convertValue(subjectBreakdown.getChemicalAbstraction(), ObjectNode.class);
			
			node.put("query_abstraction", formatObjectNode(queryCAObjNode));
			node.put("subject_abstraction", formatObjectNode(subjectCAObjNode));
			
			if (alignment.toString().contains("One or both entries is empty")) {
				node.put("alignment", NullNode.getInstance());
			} else {
				node.put("alignment", makeAlignmentNode(queryBreakdown, subjectBreakdown, alignment, alignment.getEditTranscript()));
			}
		}
		
		node.put("scores", makeScoreNode(alignment));
		return node;
	}
	
	public static String groupJSON(List<ObjectNode> nodes) {
		ObjectNode garlicResults = mapper.createObjectNode();
		garlicResults.put("garlic_results", mapper.convertValue(nodes, ArrayNode.class));
		return garlicResults.toString();
	}
	
	private static ObjectNode getNameDetails(PrismBreakdown pBreakdown) throws IOException {
		ObjectNode details = mapper.createObjectNode();
		//ObjectNode organism = mapper.createObjectNode();
		if(pBreakdown.getName() != null){
			String[] queryDetails = pBreakdown.getName().split("!!");
			
			// not putting taxonomy information for now
			if (queryDetails.length > 1) {
				for (String s : queryDetails) {
					String[] subDetails = s.split(":");
					subDetails[0] = subDetails[0].replaceAll("([a-z])([A-Z])", "$1_$2");
					if (subDetails[0].toLowerCase().trim().equals("cluster")) {
						details.put("name", subDetails[1].trim());
					} else if (subDetails[0].toLowerCase().trim().equals("organism")) {
						//String[] taxonomy = subDetails[1].split("_");
						//organism.put("genus", taxonomy[0]);
						//organism.put("species", taxonomy[1]);
					} else if (subDetails[0].toLowerCase().trim().equals("strain")) {
					//	organism.put("strain", subDetails[1]);
					} else {
						details.put(subDetails[0].trim().toLowerCase(), subDetails[1].trim());
					}
				}
				//details.put("organism", organism);
			}
		}else {
			details.put("name", pBreakdown.getName());
		}
		details.put("file_name", pBreakdown.getFileName());
		details.put("id", pBreakdown.getID());
		details.put("cluster_start", pBreakdown.getClusterStart());
		details.put("cluster_end", pBreakdown.getClusterEnd());
		
		details.put("type", "PRISM");
		
		return details;
	}
	
	private static ObjectNode getNameDetails(String nameString, String id, Class<?> queryClass, String smiles) throws IOException {
		ObjectNode details = mapper.createObjectNode();
		//ObjectNode organism = mapper.createObjectNode();
		String[] queryDetails = nameString.split("!!");
		
		// not putting taxonomy information for now
		if (queryDetails.length > 1) {
			for (String s : queryDetails) {
				String[] subDetails = s.split(":");
				subDetails[0] = subDetails[0].replaceAll("([a-z])([A-Z])", "$1_$2");
				if (subDetails[0].toLowerCase().trim().equals("cluster")) {
					details.put("name", subDetails[1].trim());
				} else if (subDetails[0].toLowerCase().trim().equals("organism")) {
					//String[] taxonomy = subDetails[1].split("_");
					//organism.put("genus", taxonomy[0]);
					//organism.put("species", taxonomy[1]);
				} else if (subDetails[0].toLowerCase().trim().equals("strain")) {
				//	organism.put("strain", subDetails[1]);
				} else {
					details.put(subDetails[0].trim().toLowerCase(), subDetails[1].trim());
				}
			}
			//details.put("organism", organism);
		} else {
			details.put("name", nameString);
		}
		details.put("id", id);
		details.put("smiles", smiles);
		details.put("type", "GRAPE");
		
		return details;
	}
	
	private static ObjectNode makeScoreNode(AlignmentObject alignment) {
		EditTranscript transcript = alignment.getEditTranscript();
		ObjectNode scores = mapper.createObjectNode();
		ArrayNode score = mapper.createArrayNode();
		DecimalFormat df = new DecimalFormat("#0.0000");
		
		if(!alignment.toString().contains("One or both entries is empty")) {
			for(int i = 0; i < transcript.getScores().size(); i++) {
				double individualScore = transcript.getScores().get(i);
				if (i > 0) {
					individualScore -= transcript.getScores().get(i-1);
				}
				score.add(Double.valueOf(df.format(individualScore)));
			}
		}
		
		scores.put("score", score);
		if(alignment.getScore().isInfinite()){
			Double rs = null;
			scores.put("raw_score", rs);
		}else{
			scores.put("raw_score", alignment.getScore());
		}
		if(alignment.getSelfScore().isNaN() || alignment.getSelfScore().isInfinite()){
			Double rs = null;
			scores.put("self_score", rs);
		}else{
			scores.put("self_score", alignment.getSelfScore());
		}
		
		if(alignment.getRelativeScore() != null){
			if(alignment.getRelativeScore().isNaN() || alignment.getRelativeScore().isInfinite()){
				Double rs = null;
				scores.put("relative_score", rs);
			}else{
				scores.put("relative_score", alignment.getRelativeScore());
			}
		}else{
			Double rs = null;
			scores.put("relative_score", rs);
		}
	
		scores.put("sugar_score", alignment.getSugarScore());
		scores.put("regulator_score", alignment.getRegulatorScore());
		
		return scores;
	}

	
	private static ObjectNode getAbstractionDetails(Breakdown breakdown) {
		ChemicalAbstraction abs = breakdown.getChemicalAbstraction();
		ObjectNode details = mapper.createObjectNode();
		details.put("chemical_type", abs.getChemicalType().toString());
		details.put("chemical_sub_type", abs.getChemicalSubtype().toString());
		if (abs.getChemicalScaffold() != null) {
			details.put("chemical_scaffold", abs.getChemicalScaffold().toString());
		} else {
			details.put("chemical_scaffold", NullNode.getInstance());
		}

		details.put("tailoring", mapper.convertValue(abs.getNonSpecificTailorings(), ArrayNode.class)); //Need to change to abbreviation
		details.put("acyl_adenylating", mapper.convertValue(abs.getAcylAdenylatingSubstrates(), ArrayNode.class)); //Need to change to abbreviation
		if(abs.getFattyAcidStatus() != Presence.FALSE){
			if(abs.getFattyAcidStatus() != Presence.TRUE){
				details.put("fatty_acid", "FattyAcid");
			} else {
				details.put("fatty_acid", "FattyAcid(Possible)");
			}
		} else {
			details.put("fatty_acid", NullNode.getInstance());
		}
		
		if (abs.getSugarGenes().size() > 0) {
			ObjectNode sugars = mapper.createObjectNode();
			int numSugars = abs.getSugars().size();
			if (numSugars > 0) {
				sugars.put("num", numSugars);
			} else {
				ObjectNode sugarGenes = mapper.createObjectNode();
				for (Entry<SugarGeneEnum, Integer> gene : abs.getSugarGenes()
						.get(0).entrySet()) {
					sugarGenes.put(gene.getKey().toString(),
							(int) gene.getValue());
				}
				sugars.put("genes", sugarGenes);
			}
			details.put("sugars", sugars);
		} else {
			details.put("sugars", NullNode.getInstance());
		}
	
		
		if (abs.getType2CyclaseGenes().size() > 0) {
			details.put("type_2_cyclase_genes", mapper.convertValue(abs.getType2CyclaseGenes(), ArrayNode.class));
		} else {
			details.put("type_2_cyclase_genes", NullNode.getInstance());
		}
		
		//Prism specific
		if(breakdown instanceof PrismBreakdown){
			PrismBreakdown pb = (PrismBreakdown)breakdown;
			if(pb.getRegulators().size() > 0){
				List<String> regulators = new ArrayList<String>(); 
				for(GeneticDomain regulator : pb.getRegulators()){
					regulators.add(regulator.getDomainType().abbreviation());
				}
				details.put("regulators", mapper.convertValue(regulators, ArrayNode.class));
			}
		}
		
		return details;
	}
	
	private static ObjectNode makeAlignmentNode(Breakdown queryBR, Breakdown subjectBR, AlignmentObject obj, EditTranscript editTranscript) {
		
		ArrayList<ChemicalNode> queryString = new ArrayList<ChemicalNode>();
		ArrayList<Integer> queryEndNodeIndices = new ArrayList<Integer>();
		ArrayList<Integer> queryStartNodeIndices = new ArrayList<Integer>();
		for(ChemicalNodeString nodeString : obj.getGrapeChemicalNodes()) {

			for(int i = 0; i < nodeString.getChemicalNodes().size(); i++) {
				queryString.add(nodeString.getChemicalNodes().get(i));
				if(i == 0) {
					queryStartNodeIndices.add(queryString.size()-1);
				}
				if(i == nodeString.getChemicalNodes().size() - 1) {
					queryEndNodeIndices.add(queryString.size() - 1);
				}
			}
		}
		
		ArrayList<ChemicalNode> subjectString = new ArrayList<ChemicalNode>();
		ArrayList<Integer> subjectEndNodeIndices = new ArrayList<Integer>();
		ArrayList<Integer> subjectStartNodeIndices = new ArrayList<Integer>();
		for(ChemicalNodeString nodeString : obj.getPrismChemicalNodes()) {
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
		
		int queryIndex = 0;
		int subjectIndex = 0;
		ArrayNode queryNode = mapper.createArrayNode();
		ArrayNode subjectNode = mapper.createArrayNode();
		ArrayNode internalQueryNode = mapper.createArrayNode();
		ArrayNode internalSubjectNode = mapper.createArrayNode();
		for(int i = 0; i < editTranscript.getEditOperations().size(); i++) {
			switch(editTranscript.getEditOperations().get(i)) {
			case I:
				if(subjectStartNodeIndices.contains(subjectIndex))	{
					if (internalSubjectNode.size() > 0) subjectNode.add(internalSubjectNode);
					internalSubjectNode = mapper.createArrayNode();
				}

				internalSubjectNode.add(getChemicalJson(subjectString.get(subjectIndex)));

				internalQueryNode.add(NullNode.getInstance());

				if(subjectEndNodeIndices.contains(subjectIndex))	{
					subjectNode.add(internalSubjectNode);
					internalSubjectNode = mapper.createArrayNode();
					queryNode.add(internalQueryNode);
					internalQueryNode = mapper.createArrayNode();
				}

				subjectIndex++;
				break;
			case D:
				if(queryStartNodeIndices.contains(queryIndex))	{
					if (internalQueryNode.size() > 0) queryNode.add(internalQueryNode);
					internalQueryNode = mapper.createArrayNode();
				}
			
				internalQueryNode.add(getChemicalJson(queryString.get(queryIndex)));
				internalSubjectNode.add(NullNode.getInstance());

				if(queryEndNodeIndices.contains(queryIndex))	{
					queryNode.add(internalQueryNode);
					internalQueryNode = mapper.createArrayNode();
					subjectNode.add(internalSubjectNode);
					internalSubjectNode = mapper.createArrayNode();
				}
				queryIndex++;

				break;
			case M:
				if(queryStartNodeIndices.contains(queryIndex))	{

					if (internalQueryNode.size() > 0) queryNode.add(internalQueryNode);
					internalQueryNode = mapper.createArrayNode();
				}

				internalQueryNode.add(getChemicalJson(queryString.get(queryIndex)));
				if(queryEndNodeIndices.contains(queryIndex))	{
					queryNode.add(internalQueryNode);
					internalQueryNode = mapper.createArrayNode();
				}

				queryIndex++;
				if(subjectStartNodeIndices.contains(subjectIndex))	{
					if (internalSubjectNode.size() > 0) subjectNode.add(internalSubjectNode);
					internalSubjectNode = mapper.createArrayNode();
				}

				internalSubjectNode.add(getChemicalJson(subjectString.get(subjectIndex)));

				if(subjectEndNodeIndices.contains(subjectIndex))	{
					subjectNode.add(internalSubjectNode);
					internalSubjectNode = mapper.createArrayNode();
				}

				subjectIndex++;
				break;
			default:
				break;
			}
		}


		ObjectNode alignments = mapper.createObjectNode();

		alignments.put("query_aligned", queryNode);
		alignments.put("subject_aliged", subjectNode);
		alignments.put("query_non_spec_tailoring", getAbstractionDetails(queryBR));
		alignments.put("subject_non_spec_tailoring", getAbstractionDetails(subjectBR));
		
		return alignments;
	}
	
	private static ObjectNode getChemicalJson(ChemicalNode node) {
		if (node instanceof AminoAcidNode) {
			return getAAObjNode((AminoAcidNode) node);
		}
		
		if (node instanceof PKPieceNode) {
			return getPKObjNode((PKPieceNode) node);
		}
		
		if (node instanceof StarterNode) {
			return getStarterObjNode((StarterNode) node);
		}
		
		if (node instanceof MultipleAminoAcidNode) {
			return getMultAAObjNode((MultipleAminoAcidNode) node);
		}

		return null;
	}
	
	private static ObjectNode getAAObjNode(AminoAcidNode node) {
		ObjectNode map = mapper.createObjectNode();
		if (node.getAminoAcidType() != null) {
			map.put("substrate", node.getAminoAcidType().getAbbreviation());
		} else {
			map.put("substrate", "Unk");
		}
		
		if (node.getSiteSpecificTailoring().size() > 0) {
			ArrayNode tailoring = mapper.createArrayNode();
			for (SiteSpecificTailoringEnum tailor : node.getSiteSpecificTailoring()) {
				tailoring.add(tailor.getAbbreviation());
			}
			map.put("tailoring", tailoring);
		} else {
			map.put("tailoring", NullNode.getInstance());
		}
		
		if(node.getModuleNumber() != null){
			map.put("module", node.getModuleNumber());
		}
		if(node.getOrfNumber() != null){
			map.put("orf", node.getOrfNumber());
		}
		
		return map;
	}
	
	private static ObjectNode getStarterObjNode(StarterNode node) {
		ObjectNode map = mapper.createObjectNode();
		map.put("substrate", node.toString());
		return map;
	}
	
	private static ObjectNode getPKObjNode(PKPieceNode node) {
		ObjectNode map = mapper.createObjectNode();
		if (node.getSubstrate() != null) {
			map.put("substrate", node.getSubstrate().toString());
		} else {
			map.put("substrate", NullNode.getInstance());
		}
		if (node.getPossibleOxidationStates().size() > 0) {
			ArrayNode oxidationStates = mapper.createArrayNode();
			for (PKOxidationStateEnum state : node.getPossibleOxidationStates()) {
				oxidationStates.add(state.getAbbreviation());
			}
			map.put("oxidation_states", oxidationStates);
		} else {
			map.put("oxidation_states", NullNode.getInstance());
		}
		
		if (node.getSiteSpecificTailoring().size() > 0) {
			ArrayNode tailoring = mapper.createArrayNode();
			for (SiteSpecificTailoringEnum tailor : node.getSiteSpecificTailoring()) {
				tailoring.add(tailor.getAbbreviation());
			}
			map.put("tailoring", tailoring);
		} else {
			map.put("tailoring", NullNode.getInstance());
		}
		if(node.getModuleNumber() != null){
			map.put("module", node.getModuleNumber());
		}
		if(node.getOrfNumber() != null){
			map.put("orf", node.getOrfNumber());
		}
		return map;
	}
	
	private static ObjectNode getMultAAObjNode(MultipleAminoAcidNode node) {
		ObjectNode map = mapper.createObjectNode();
		map.put("possible_mult_aa", String.valueOf(node.getMaxNumAminoAcids()));
		return map;
	}
	
		
	/**
	 * For formatting the ChemicalAbstractions
	 * @param node
	 * @return
	 */
	private static ArrayNode formatArrayNode(ArrayNode node) {
		ArrayNode formattedNode = mapper.createArrayNode();
		for (JsonNode n : node) {
			if (n.isArray()) {
				formattedNode.add(formatArrayNode((ArrayNode) n));
			} else if (n.isObject()) {
				formattedNode.add(formatObjectNode((ObjectNode) n));
			} else {
				formattedNode.add(n);
			}
		}
		
		return formattedNode;
	}
	
	/**
	 * For formatting the ChemicalAbstractions
	 * @param node
	 * @return
	 */
	private static ObjectNode formatObjectNode(ObjectNode node) {
		ObjectNode formattedNode = mapper.createObjectNode();
		Iterator<String> fieldNames = node.getFieldNames();
		
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			if (fieldName.equals("aanode") || fieldName.equals("pknode")) continue;
			JsonNode data = node.get(fieldName);
			fieldName = fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
			if (data.isArray()) {
				formattedNode.put(fieldName, formatArrayNode((ArrayNode) data));
			} else if (data.isObject()) {
				formattedNode.put(fieldName, formatObjectNode((ObjectNode) data));
			} else {
				formattedNode.put(fieldName, data);
			}
		}
		return formattedNode;
	}
}
