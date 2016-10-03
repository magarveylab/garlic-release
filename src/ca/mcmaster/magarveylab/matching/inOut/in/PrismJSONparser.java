package ca.mcmaster.magarveylab.matching.inOut.in;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import ca.mcmaster.magarveylab.enums.domains.BetaLactamDomains;
import ca.mcmaster.magarveylab.enums.domains.DomainType;
import ca.mcmaster.magarveylab.enums.domains.PrerequisiteDomains;
import ca.mcmaster.magarveylab.enums.domains.RegulatorDomains;
import ca.mcmaster.magarveylab.enums.domains.ResistanceDomains;
import ca.mcmaster.magarveylab.enums.domains.RibosomalDomains;
import ca.mcmaster.magarveylab.matching.breakdowns.PrismBreakdown;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction.Presence;
import ca.mcmaster.magarveylab.matching.chem.ChemicalNodeString;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.GeneticDomain;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.RibosomalOrf;
import ca.mcmaster.magarveylab.matching.enums.AcylAdenylatingSubstrates;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalType;
import ca.mcmaster.magarveylab.matching.enums.Monomers;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.ChemicalSubtype;
import ca.mcmaster.magarveylab.matching.enums.ChemicalClassifications.Enedyine;
import ca.mcmaster.magarveylab.matching.enums.Monomers.AminoAcidEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.CStarterEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.ChemicalNodeOrigin;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.NonSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKOxidationStateEnum;
import ca.mcmaster.magarveylab.matching.enums.Monomers.PKSubstrateEnum;
import ca.mcmaster.magarveylab.matching.enums.Tailorings.SiteSpecificTailoringEnum;
import ca.mcmaster.magarveylab.matching.enums.Sugars.SugarGeneEnum;
import ca.mcmaster.magarveylab.matching.enums.Type2Cyclases.Type2CyclaseGene;
import ca.mcmaster.magarveylab.matching.inOut.Utility;
import ca.mcmaster.magarveylab.matching.inOut.modules.AAmodule;
import ca.mcmaster.magarveylab.matching.inOut.modules.CStarterModule;
import ca.mcmaster.magarveylab.matching.inOut.modules.GTRmodule;
import ca.mcmaster.magarveylab.matching.inOut.modules.PKmodule;

/**
 * This class loads standard PRISM output JSON into GARLIC objects
 * @author dejongc
 *
 */
@SuppressWarnings("unchecked")
public class PrismJSONparser {
	
	private boolean getAllAdenylation;
	
	public PrismJSONparser(boolean getAllAdenylation){
		this.getAllAdenylation = getAllAdenylation;
	}
	
	public List<PrismBreakdown> readPrismDirectoryJSON(File prismDirectory) throws JsonParseException, JsonMappingException, IOException{
		List<PrismBreakdown> prismBreakdowns = new ArrayList<PrismBreakdown>();
		for (final File prismFile : prismDirectory.listFiles()) {
//			prismBreakdowns.addAll(loadPrism(prismFile));
			try{
				prismBreakdowns.addAll(loadPrism(prismFile));
			}catch(Exception e){
				System.err.println("Error on file: " + prismFile.getName());
				//e.printStackTrace();
			}
		}
		return prismBreakdowns;
	}
	
	public List<PrismBreakdown> loadPrism(File file) throws JsonParseException, JsonMappingException, IOException{		
		Scanner scanner = new Scanner(file);
		String json = scanner.useDelimiter("\\Z").next();
		scanner.close();		
		return loadPrism(json);
	}
	
	public List<PrismBreakdown> loadPrism(String json) throws JsonParseException, JsonMappingException, IOException{		
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = mapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});		
		List<PrismBreakdown> pBreakdown = readMap(map);
		return pBreakdown;
	}
	
	private Integer version = null;
	private List<PrismBreakdown> readMap(Map<String, Object> map) {
			
		Map<String, Object> prismResults = (Map<String, Object>)map.get("prism_results");
		
		//get the version
		Map<String, Object> config = (Map<String, Object>)prismResults.get("configuration");
		String versionString = (String)config.get("version");
		version = Integer.parseInt(versionString.replace(".", "").replaceAll("-.*$", ""));
		
		//get the input data
		Map<String, Object> input = (Map<String, Object>)prismResults.get("input");
		String description = (String)input.get("description");
		String fileName = (String)input.get("filename");
		List<Map<String, Object>> regulatoryGenes = new ArrayList<Map<String, Object>>();
		
		//Check for which type of data structure it is (not consistant, needs to be fixed in prism
		List<Object> regulatoryGenesPrelim = (ArrayList<Object>)prismResults.get("regulatory_genes");
		if(regulatoryGenesPrelim.size() > 0){
			if(regulatoryGenesPrelim.get(0) instanceof ArrayList){
				regulatoryGenes = (ArrayList<Map<String, Object>>)regulatoryGenesPrelim.get(0);
			}else{
				regulatoryGenes = (ArrayList<Map<String, Object>>)prismResults.get("regulatory_genes");
			}
		}
		
		List<Map<String, Object>> resistanceGenes = new ArrayList<Map<String, Object>>();
		
		//Check for which type of data structure it is (not consistant, needs to be fixed in prism
		List<Object> resistanceGenesPrelim = (ArrayList<Object>)prismResults.get("resistance_genes");
		if(resistanceGenesPrelim != null){
			if(resistanceGenesPrelim.size() > 0){
				if(resistanceGenesPrelim.get(0) instanceof ArrayList){
					resistanceGenes = (ArrayList<Map<String, Object>>)resistanceGenesPrelim.get(0);
				}else{
					resistanceGenes = (ArrayList<Map<String, Object>>)prismResults.get("resistance_genes");
				}
			}
		}
		List<GeneticDomain> genomeResistanceGenes = getGenomeResistanceDomains(resistanceGenes);
		List<GeneticDomain> genomeRegulatoryGenes = getGenomeRegulatoryDomains(regulatoryGenes); 
		
		String id;
		if(input.get("id") instanceof Integer){
			id = Integer.toString((Integer)input.get("id"));
		}else{
			id = (String)input.get("id");
		}
		
		fileName = new File(fileName).getName();
		String accession = (String)input.get("accession");
		
		//get the cluster data
		List<Map<String, Object>> clusters = (ArrayList<Map<String, Object>>)prismResults.get("clusters");
		List<PrismBreakdown> pBreakdowns = new ArrayList<PrismBreakdown>();
		int clusterNumber = 1;
		for(Map<String, Object> cluster : clusters){
			PrismBreakdown pBreakdown = readCluster(cluster);
			pBreakdown.setName(description + "(" + clusterNumber + ")");
			//pBreakdown.setName(fileName + "(" + clusterNumber + ")");
			pBreakdown.setFrame((String)cluster.get("frame"));
			pBreakdown.setFileName(fileName);
			pBreakdown.setClusterNumber(clusterNumber);
			for(GeneticDomain domain : genomeRegulatoryGenes){
				pBreakdown.addRegulator(domain);
			}
			for(GeneticDomain domain: genomeResistanceGenes){
				pBreakdown.addResistor(domain);
			}
			if(accession != null) pBreakdown.setAccession(accession);
			if(id != null) pBreakdown.setID(id);
			if(input.get("length") != null){ //temporary for old prism data
				int sequenceLength = (int)input.get("length");
				int clusterStart = (int)cluster.get("start");
				int clusterFromEnd = sequenceLength - (int)cluster.get("end");
				pBreakdown.setClusterStart(clusterStart);
				pBreakdown.setClusterEnd((int)cluster.get("end"));
				if(clusterStart >= 20000 && clusterFromEnd >= 20000) pBreakdown.setComplete(true);
			}
			pBreakdown.setColinear();
			pBreakdowns.add(pBreakdown);
			clusterNumber ++;
		}
		return pBreakdowns;
	}

	//TODO remove these class properties and turn them into method objects that get passed
	private PrismBreakdown prismBreakdown;
	private ChemicalAbstraction chemicalAbstraction;
	private Integer currentOrfNumber = 0;
	
	private PrismBreakdown readCluster(Map<String, Object> clusterMap){
		prismBreakdown = new PrismBreakdown();
		chemicalAbstraction = prismBreakdown.getChemicalAbstraction();
		
		if(version < 125){
			ClusterFamily clusterFamily = ClusterFamily.getClusterFamily((String)clusterMap.get("family"));
			if(clusterFamily == ClusterFamily.ENEDIYNE){
				chemicalAbstraction.setChemicalSubtype(ChemicalSubtype.ENEDYINE);
				String enediyneType = (String)clusterMap.get("type");
				if(enediyneType.equals("_9_MEMBERED_RING")){
					chemicalAbstraction.setChemicalScaffold(Enedyine.NineMember);
				}else if(enediyneType.equals("_10_MEMBERED_RING")){
					chemicalAbstraction.setChemicalScaffold(Enedyine.TenMember);
				}
			}else if(clusterFamily == ClusterFamily.TYPE_II_POLYKETIDE){
				chemicalAbstraction.setChemicalSubtype(ChemicalSubtype.TYPE_2);
			}
			ClusterType clusterType = ClusterType.getClusterType((String)clusterMap.get("type"));
			if(clusterType == ClusterType.NRPS){
				chemicalAbstraction.setChemicalType(ChemicalType.NRP);
			}else if(clusterType == ClusterType.PKS){
				chemicalAbstraction.setChemicalType(ChemicalType.PK);
			}else if(clusterType == ClusterType.HYBRID){
				chemicalAbstraction.setChemicalType(ChemicalType.NRP_PK_HYBRID);
			}else if(clusterType == ClusterType.RIBOSOMAL){
				chemicalAbstraction.setChemicalType(ChemicalType.RIBOSOMAL);
			}
		}else{
			List<String> clusterFamilyStrings = (ArrayList<String>)clusterMap.get("family");
			for(String clusterFamilyString : clusterFamilyStrings){
				ClusterFamily clusterFamily = ClusterFamily.getClusterFamily(clusterFamilyString);
				if(clusterFamily == ClusterFamily.ENEDIYNE){
					chemicalAbstraction.setChemicalSubtype(ChemicalSubtype.ENEDYINE);
					String enediyneType = (String)clusterMap.get("type");
					if(enediyneType.equals("_9_MEMBERED_RING")){
						chemicalAbstraction.setChemicalScaffold(Enedyine.NineMember);
					}else if(enediyneType.equals("_10_MEMBERED_RING")){
						chemicalAbstraction.setChemicalScaffold(Enedyine.TenMember);
					}
					break;
				}else if(clusterFamily == ClusterFamily.TYPE_II_POLYKETIDE){
					chemicalAbstraction.setChemicalSubtype(ChemicalSubtype.TYPE_2);
					break;
				}
				else if(clusterFamily == ClusterFamily.RIBOSOMAL){
					chemicalAbstraction.setChemicalType(ChemicalType.RIBOSOMAL);
				}
			}
			List<ClusterType> clusterType = ClusterType.getClusterType((ArrayList<String>)clusterMap.get("type"));
			if(clusterType.contains(ClusterType.NRPS) && clusterType.size() == 1){
				chemicalAbstraction.setChemicalType(ChemicalType.NRP);
			}else if(clusterType.contains(ClusterType.PKS) && clusterType.size() == 1){
				chemicalAbstraction.setChemicalType(ChemicalType.PK);
			}else if(clusterType.contains(ClusterType.NRPS) && clusterType.contains(ClusterType.PKS)){
				chemicalAbstraction.setChemicalType(ChemicalType.NRP_PK_HYBRID);
			}
		}
		
		List<Map<String, Object>> orfs = (ArrayList<Map<String, Object>>)clusterMap.get("orfs");
		for(Map<String, Object> orf : orfs){
			currentOrfNumber ++;
			parseOrfInfo(orf);
		}
		
		return prismBreakdown;
	}
	
	private AAmodule aaMod;
	private PKmodule pkMod;
	private CStarterModule cMod;
	private ChemicalNodeString nodeString;
	private int currentModuleNumber;

	private void parseOrfInfo(Map<String, Object> orf) {
		String type = (String) orf.get("type");
		if(type != null && (type.equals("ribosomal") || type.equals("prerequisite"))){ //both are part of ribosomal clusters
			//skip
		}else{
			List<Map<String, Object>> domains = (ArrayList<Map<String, Object>>)orf.get("domains");
			if(domains == null) return; // Return if there are no domains on this orf (ie no useful information)
			currentModuleNumber = 1;
			aaMod = new AAmodule();
			pkMod = new PKmodule();
			cMod = new CStarterModule();
			nodeString = new ChemicalNodeString();
			for(Map<String, Object> domain : domains){
				//Boolean orphaned = (Boolean)domain.get("orphaned");
				//if(orphaned != null && orphaned) continue; //Ignore orphaned domains
				Boolean onModule = (Boolean)domain.get("on_module");
				if(onModule != null && onModule){
					int moduleNumber = (Integer)domain.get("module_number");
					parseModuleInfo(moduleNumber, domain);
				}else{ //Not a module
					nonModuleInfo(domain);
				}
			}
			if(!pkMod.empty){
				if(pkMod.getOrigin() == ChemicalNodeOrigin.PRISM_TRANS_AT){
					chemicalAbstraction.addPKSubstrateInsertion(pkMod.getNode().getSubstrate());
				}else{
					pkMod.setModuleNumber(currentModuleNumber);
					pkMod.setOrfNumber(currentOrfNumber);
					nodeString.addNode(pkMod.getNode());
				}
			}else if(!aaMod.empty){
				if(aaMod.getNode().getAminoAcidType() == null || !aaMod.getNode().getAminoAcidType().equals(AminoAcidEnum.DOCK)){//TMP FIX BECAUSE DOCK ISNT A REAL MODULE (THANKS MIKE)
					aaMod.setOrfNumber(currentOrfNumber);
					aaMod.setModuleNumber(currentModuleNumber);
					nodeString.addNode(aaMod.getNode());
				}
			}else if(!cMod.empty){
				nodeString.addNode(cMod.getNode());
			}
			if(nodeString.getChemicalNodes().size() > 0) chemicalAbstraction.addNodeString(nodeString);
		}
	}

	private void parseModuleInfo(int moduleNumber, Map<String, Object> domain){
		if(currentModuleNumber != moduleNumber){
			if(!pkMod.empty){
				pkMod.setModuleNumber(currentModuleNumber);
				pkMod.setOrfNumber(currentOrfNumber);
				if(pkMod.getOrigin() == ChemicalNodeOrigin.PRISM_TRANS_AT){
					chemicalAbstraction.addPKSubstrateInsertion(pkMod.getNode().getSubstrate());
				}else{
					nodeString.addNode(pkMod.getNode());
				}
			}else if(!aaMod.empty){
				aaMod.setModuleNumber(currentModuleNumber);
				aaMod.setOrfNumber(currentOrfNumber);
				if(aaMod.getNode().getAminoAcidType() == null || !aaMod.getNode().getAminoAcidType().equals(AminoAcidEnum.DOCK)){//TMP FIX BECAUSE DOCK ISNT A REAL MODULE (THANKS MIKE)
					nodeString.addNode(aaMod.getNode());
				}
			}else if(!cMod.empty){
				nodeString.addNode(cMod.getNode());
			}
			aaMod = new AAmodule();
			pkMod = new PKmodule();
			cMod = new CStarterModule();
			currentModuleNumber = moduleNumber;
		}
		String moduleType = (String)domain.get("module_type");
		List<Map<String, Object>> substrates = (ArrayList<Map<String, Object>>)domain.get("substrates");

		if(moduleType.contains("ACYLTRANSFERASE") || moduleType.contains("TRANS_AT_INSERTION")){
			if(moduleType.equals("ACYLTRANSFERASE")){
				pkMod.setOrigin(ChemicalNodeOrigin.PRISM_ACYLTRANSFERASE);
			}else if(moduleType.equals("TRANS_AT_INSERTION")){
				pkMod.setOrigin(ChemicalNodeOrigin.PRISM_TRANS_AT_INSERTION);
			}
			//Get oxi state
			String name = (String)domain.get("name");
			if(name.equals("ER")) { // "Enolreductase"
				pkMod.addPossibleOxidationState(PKOxidationStateEnum.SINGLEBOND);
			}else if(name.equals("DH")) { // "Dehydroxylase"
				pkMod.addPossibleOxidationState(PKOxidationStateEnum.DOUBLEBOND);
			}else if(name.equals("KR")) { // "Ketoreductase"
				pkMod.addPossibleOxidationState(PKOxidationStateEnum.HYDROXYL);
			}else if(name.equals("KS")) { // "Ketosynthase"
				pkMod.addPossibleOxidationState(PKOxidationStateEnum.KETONE);
			}
			
			if(substrates != null){
				String substrate = (String)substrates.get(0).get("name");
				//Get substrate
				if(Utility.isPK(substrate)){
					PKSubstrateEnum pke = Utility.getPKsubstrate(substrate);
					pkMod.addPossibleSubstrate(pke, 100.0);
				}
			}
			
			//ADD THIOLESTERASE
			if(domain.equals("TE")){
				pkMod.addSiteSpecificTailoring(SiteSpecificTailoringEnum.THIOLESTERASE);
			}
			
			//site specific tailorings
			SiteSpecificTailoringEnum sstEnum = Utility.getSiteSpecificTailoringEnum((String)domain.get("name"));
			if(sstEnum != null) pkMod.addSiteSpecificTailoring(sstEnum);
			
		}else if(moduleType.equals("TRANS_AT")){
			pkMod.setOrigin(ChemicalNodeOrigin.PRISM_TRANS_AT);
			if(substrates != null){
				String substrate = (String)substrates.get(0).get("name");
				if(Utility.isPK(substrate)){
					PKSubstrateEnum pke = Utility.getPKsubstrate(substrate);
					pkMod.addPossibleSubstrate(pke, 100.0);
				}
			}
		}else if(moduleType.contains("ADENYLATION")){
			if(aaMod.getOrigin() == null){
				if(moduleType.equals("STARTER_ADENYLATION)")) {
					aaMod.setOrigin(ChemicalNodeOrigin.PRISM_STARTER_ADENYLATION);
				}else if(moduleType.equals("ADENYLATION_KETOREDUCTASE")) {
					aaMod.setOrigin(ChemicalNodeOrigin.PRISM_ADENYLATION_KETOREDUCTASE);
				}else if(moduleType.equals("TRANS_ADENYLATION_INSERTION")){
					return; //TEMPORARY UNTIL TRANS GETS FIXED  // ADD THESE IN FOR prism on prism
					//aaMod.setOrigin(ChemicalNodeOrigin.PRISM_ADENYLATION);
				}else if(moduleType.equals("TRANS_ADENYLATION")){
					aaMod.setOrigin(ChemicalNodeOrigin.PRISM_TRANS_ADENYLATION);
				}else {
					aaMod.setOrigin(ChemicalNodeOrigin.PRISM_ADENYLATION);
				}
			}
			String fullName = (String)domain.get("full_name");
			if(fullName != null){
				if(fullName.equals("CONDENSATION")){
					List<Map<String, Object>> sources = (ArrayList<Map<String, Object>>)domain.get("sources");
					if(sources != null){
						String source = (String)sources.get(0).get("name");
						CStarterEnum cStarter = CStarterEnum.getCStarterMatch(source);
						if(cStarter != null){
							chemicalAbstraction.setFattyAcid(Presence.TRUE);
							chemicalAbstraction.addFattyAcid(cStarter);
						}
					}
				}
			}
			if(substrates != null){
				String substrate = (String)substrates.get(0).get("name");
				AminoAcidEnum aae = Monomers.getAminoAcidEnumFromAbbreviation(substrate);
				if(aae != null) aaMod.addAA(aae, 100.0);
			}
			List<Map<String, Object>> sources = (ArrayList<Map<String, Object>>)domain.get("sources");
			if(sources != null && sources.size() > 0){
				String source = (String)sources.get(0).get("name");
				if(source.endsWith("_cyc")) aaMod.addSiteSpecificTailoring(SiteSpecificTailoringEnum.CYCLIZATION);
			}
			SiteSpecificTailoringEnum sstEnum = Utility.getSiteSpecificTailoringEnum((String)domain.get("name"));
			if(sstEnum != null) aaMod.addSiteSpecificTailoring(sstEnum);
		}else if(moduleType.equals("FATTY_ACID")){
			chemicalAbstraction.setFattyAcid(Presence.TRUE); // General true or false does not take which type into consideration right now
		}else if(moduleType.equals("ACYL_ADENYLATE") && substrates != null){
				String substrate = (String)substrates.get(0).get("name");
				AcylAdenylatingSubstrates starter = Utility.getAcylAdenylatingSubstrate(substrate);
				if(starter != null) chemicalAbstraction.addStarter(starter);
		}
	}

	private void nonModuleInfo(Map<String, Object> domain){
		String domainName = (String)domain.get("name");
		List<Map<String, Object>> substrates = (ArrayList<Map<String, Object>>)domain.get("substrates");
		String substrate = null;
		if(substrates != null) substrate = (String)substrates.get(0).get("name");
		
		if(getAllAdenylation && substrate != null){
			if(domainName.equals("A")){
				AminoAcidEnum aae = Monomers.getAminoAcidEnumFromAbbreviation(substrate);
				if(aae != null) {
					aaMod.addAA(aae, 100.0);
					aaMod.setOrigin(ChemicalNodeOrigin.PRISM_ADENYLATION_NON_MODULE);
					nodeString.addNode(aaMod.getNode());
					aaMod = new AAmodule();
				}
			}
		}
		
		boolean parsed = false;
		String domainFamily = (String)domain.get("family");
		if(domainFamily != null){
			if(domainFamily.equals("REGULATOR")){
				String name = (String)domain.get("name");
				RegulatorDomains regulator = RegulatorDomains.getRegulatorDomainFromAbbreviation(name);
				if(regulator != null){
					prismBreakdown.addRegulator(regulator, true);
				}
				parsed = true;
			}else if(domainFamily.equals("RESISTANCE")){
				String name = (String)domain.get("name");
				ResistanceDomains resistor = ResistanceDomains.getResistanceDomainFromAbbreviation(name);
				if(resistor != null){
					prismBreakdown.addResistor(resistor, true);
				}
				parsed = true;
			}
		}
		if(!parsed){
			if(domainName.equals("GTr")){
				GTRmodule gtrMod = new GTRmodule();
				List<Map<String, Object>> sources = (ArrayList<Map<String, Object>>)domain.get("sources");
				if(sources != null){
					String source = (String)sources.get(0).get("name");
					gtrMod.addGene(source, 100.0);
					if(gtrMod.isHexose()) chemicalAbstraction.addSugarGene(SugarGeneEnum.HEXOSE_GTR);
				}
				parsed = true;
			}else if(domainName.equals("Cyc")){
				String domainFullName = (String)domain.get("full_name");
				Type2CyclaseGene cyclaseGene = Utility.getCyclaseGene(domainFullName);
				if(cyclaseGene != null) {
					chemicalAbstraction.addType2CyclaseGene(cyclaseGene);
					parsed = true;
				}
			}
		}
		String domainFullName = (String)domain.get("full_name");
		if(!parsed && domainName.equals("Cyc")){
			Type2CyclaseGene cyclaseGene = Utility.getCyclaseGene(domainFullName);
			if(cyclaseGene != null) {
				chemicalAbstraction.addType2CyclaseGene(cyclaseGene);
				parsed = true;
			}
		}			
		if(!parsed && !domainFullName.equals("CONDENSATION")){
			SugarGeneEnum sge = Utility.getSugarGene(domainName);
			
			if(sge != null){
				chemicalAbstraction.addSugarGene(sge);
				parsed = true;
			}
		}
		if(!parsed){
			if(substrate != null){
				if(substrate.endsWith("_cyc")){
					chemicalAbstraction.addNonspecificTailoringEnum(NonSpecificTailoringEnum.CYCLIZATION);
					parsed = true;
				}
			}
			NonSpecificTailoringEnum nste = Utility.getNonSpecificTailoringEnum(domainName);
			if(nste != null){
				chemicalAbstraction.addNonspecificTailoringEnum(nste);
				parsed = true;
			}else{
				nste = Utility.getNonSpecificTailoringEnumSource(substrate); //This is looking for exact match from this string, for FAAL starts
				if(nste != null){
					chemicalAbstraction.addNonspecificTailoringEnum(nste);
					parsed = true;
				}
			}
		}
		if(!parsed){
			DomainType domainType = BetaLactamDomains.getPossibleBetaLactamDomainsFromAbbreviation(domainName);
			if(domainType != null){
				if(domainType.equals(BetaLactamDomains.Sulfazecin_Thioesterase)){
					double score;
					try{
						score = (Double) domain.get("score");
					}catch(ClassCastException e){
						int tmp = (Integer) domain.get("score");
						score = (double) tmp;
					}
					if(score > 60){
						prismBreakdown.addOtherDomain(domainType);
					}
				}else{
					prismBreakdown.addOtherDomain(domainType);
				}
			}
		}
	}
	
	private List<GeneticDomain> getGenomeRegulatoryDomains(List<Map<String, Object>> regulatoryDomainsMap) {
		List<GeneticDomain> genomeRegulatoryDomains = new ArrayList<GeneticDomain>();
		if(regulatoryDomainsMap != null){
			for(Map<String, Object> domainMap : regulatoryDomainsMap){
				String name = (String)domainMap.get("name");
				DomainType regulatorDomain = RegulatorDomains.getRegulatorDomainFromAbbreviation(name);
				if(regulatorDomain != null){
					genomeRegulatoryDomains.add(new GeneticDomain(regulatorDomain, false));
				}
			}
		}
			
		return genomeRegulatoryDomains;
	}
	
	private List<GeneticDomain> getGenomeResistanceDomains(List<Map<String, Object>> resistanceDomainsMap) {
		List<GeneticDomain> genomeResistanceDomains = new ArrayList<GeneticDomain>();
		if(resistanceDomainsMap != null){
			for(Map<String, Object> domainMap : resistanceDomainsMap){
				String name = (String)domainMap.get("name");
				DomainType regulatorDomain = ResistanceDomains.getResistanceDomainFromAbbreviation(name);
				if(regulatorDomain != null){
					genomeResistanceDomains.add(new GeneticDomain(regulatorDomain, false));
				}
			}
		}
			
		return genomeResistanceDomains;
	}
	
	
	private enum ClusterFamily {
		RIBOSOMAL,
		THIOTEMPLATED, 
		BETA_LACTAM,
		ENEDIYNE,
		TYPE_II_POLYKETIDE;
		
		private static ClusterFamily getClusterFamily(String string) {
			ClusterFamily cf = null;
			for(ClusterFamily match : ClusterFamily.values()){
				if(string.equals(match.toString())){
					cf = match;
				}
			}
			return cf;
		}
	}
	private enum ClusterType {
		NRPS,
		PKS,
		HYBRID,
		RIBOSOMAL,
		;
		
		private static ClusterType getClusterType(String string) {
			ClusterType ct = null;
			for(ClusterType match : ClusterType.values()){
				if(string.equals(match.toString())){
					ct = match;
				}
			}
			return ct;
		}
		
		private static List<ClusterType> getClusterType(List<String> strings) {
			List<ClusterType> ct = new ArrayList<ClusterType>();
			for(ClusterType match : ClusterType.values()){
				for(String string : strings){
					if(string.equals(match.toString())){
						ct.add(match);
					}
				}
			}
			return ct;
		}
	}
}
