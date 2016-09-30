package ca.mcmaster.magarveylab.matching.chem;

import java.util.ArrayList;
/**
 * A class for storing an ordered list of chemical nodes
 * @author gmchen
 */
public class ChemicalNodeString {
	private ArrayList<ChemicalNode> chemicalNodes;
	private boolean knownStart = true;
	
	/**
	 * Create a new empty chemical node string
	 */
	public ChemicalNodeString() {
		chemicalNodes = new ArrayList<ChemicalNode>();
	}
	
	/**
	 * Create a chemical node string from an ordered list of chemical nodes
	 * @param chemicalNodes
	 */
	public ChemicalNodeString(ArrayList<ChemicalNode> chemicalNodes) {
		this.setChemicalNodes(chemicalNodes);
	}
	/**
	 * @return the chemicalNodes
	 */
	public ArrayList<ChemicalNode> getChemicalNodes() {
		return chemicalNodes;
	}
	/**
	 * @param chemicalNodes the chemicalNodes to set
	 */
	public void setChemicalNodes(ArrayList<ChemicalNode> chemicalNodes) {
		this.chemicalNodes = chemicalNodes;
	}
	
	/**
	 * Add a chemical node to the end of the node string
	 * @param node
	 */
	public void addNode(ChemicalNode node) {
		chemicalNodes.add(node);
	}
	
	public void setKnownStart(Boolean knownStart){
		this.knownStart = knownStart;
	}
	
	public boolean isKnownStart(){
		return knownStart;
	}
	
	@Override public String toString() {
		String out = "";
		for(ChemicalNode c : chemicalNodes) {
			out += c + "\t";
		}
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		ChemicalNodeString otherString = (ChemicalNodeString) obj;
		return this.chemicalNodes.equals(otherString.chemicalNodes);
	}
}
