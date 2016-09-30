package ca.mcmaster.magarveylab.matching.breakdowns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.mcmaster.magarveylab.enums.domains.DomainType;
import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.GeneticDomain;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.RibosomalOrf;

public class PrismBreakdown extends Breakdown{
	
	private String accession = "";
	private double clusterNumber = -1;
	private int magarveyID = 0;
	private Boolean complete = false;
	private Frame frame = Frame.NONE;
	private List<GeneticDomain> regulators = new ArrayList<GeneticDomain>();
	private List<GeneticDomain> resistors = new ArrayList<GeneticDomain>();
	private List<DomainType>	otherDomains = new ArrayList<DomainType>(); 
	private Set<RibosomalOrf>	ribosomalOrfs = new HashSet<RibosomalOrf>();
	private Integer clusterStart = null;
	private Integer clusterEnd = null;
	
	public enum Frame {
		POSITIVE, NONE, NEGATIVE;

		public static Frame setFrame(String frame) {
			if(frame.equals("POSITIVE")){
				return Frame.POSITIVE;
			}
			if(frame.equals("NONE")){
				return Frame.NONE;
			}
			if(frame.equals("NEGATIVE")){
				return Frame.NEGATIVE;
			}
			return null;
		}
	}

	public PrismBreakdown(){
	}
	public PrismBreakdown(ChemicalAbstraction ca, String name, String accession, Boolean complete, Frame frame){
		this.ca = ca;
		this.name = name;
		this.accession = accession;
		this.complete = complete;
		this.frame = frame;
	}
	public PrismBreakdown(ChemicalAbstraction ca, String name){
		this.ca = ca;
		this.name = name;
	}
	public void setAccession(String accession){
		this.accession = accession;
	}
	public void setComplete(Boolean complete){
		this.complete = complete;
	}
	public void setFrame(String frame){
		this.frame = Frame.setFrame(frame);
	}
	public void setClusterNumber(double clusterNumber) {
		this.clusterNumber = clusterNumber;		
	}
	/**
	 * Frame is not yet reliable enough to determine for certanty if a cluster is colinear So function is disabled
	 * @return
	 */
	public void setColinear(){
		if(frame != Frame.NONE)	ca.setColinear(frame);
	}
	
	public void addRegulator(DomainType regulator, boolean inCluster){
		GeneticDomain domain = new GeneticDomain(regulator, inCluster);
		regulators.add(domain);
	}
	public void addRegulator(GeneticDomain domain) {
		regulators.add(domain);	
	}
	public void addResistor(DomainType resistor, boolean inCluster){
		GeneticDomain domain = new GeneticDomain(resistor, inCluster);
		resistors.add(domain);
	}
	public void addResistor(GeneticDomain domain) {
		resistors.add(domain);	
	}
	public void addOtherDomain(DomainType domain) {
		otherDomains.add(domain);
	}
	public void setClusterStart(int clusterStart) {
		this.clusterStart = clusterStart;
		
	}
	public void setClusterEnd(int clusterEnd) {
		this.clusterEnd = clusterEnd;
	}	
	public String getAccession(){
		if(accession == null){
			return "";
		}
		return accession;
	}
	public Boolean isComplete(){
		return complete;
	}
	public Frame getFrame(){
		return frame;
	}
	public Double getClusterNumber(){
		return clusterNumber;
	}
	public Integer getClusterStart(){
		return clusterStart;
	}
	public Integer getClusterEnd(){
		return clusterEnd;
	}
	public List<GeneticDomain> getRegulators(){
		return regulators;
	}
	public List<GeneticDomain> getResistors(){
		return resistors;
	}
	public List<DomainType> getOtherDomains(){
		return otherDomains;
	}
	public Set<RibosomalOrf> getRibosomalOrfs() {
		return ribosomalOrfs;
	}
	public void addRibosomalOrf(RibosomalOrf ribosomalOrf) {
		ribosomalOrfs.add(ribosomalOrf);
	}
	public int getMagarveyID() {
		return magarveyID;
	}
	public void setMagarveyID(int magarveyID) {
		this.magarveyID = magarveyID;
	}
}