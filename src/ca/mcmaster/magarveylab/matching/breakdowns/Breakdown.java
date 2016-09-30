package ca.mcmaster.magarveylab.matching.breakdowns;

import ca.mcmaster.magarveylab.matching.chem.ChemicalAbstraction;

public class Breakdown {
	
	protected ChemicalAbstraction ca = new ChemicalAbstraction();
	protected String name = null;
	protected String fileName = null;
	protected String id = null;
	
	public Breakdown(){} // so can't instantiate just a breakdown outside of Grape and Prism
	
	//setters
	public void setChemicalAbstraction(ChemicalAbstraction ca){
		this.ca = ca;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setID(String id) {
		this.id  = id;
	}
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	//getters
	public ChemicalAbstraction getChemicalAbstraction(){
		return ca;
	}
	public String getName(){
		return name;
	}
	public String getID(){
		return id;
	}
	public String getFileName(){
		return fileName;
	}

}
