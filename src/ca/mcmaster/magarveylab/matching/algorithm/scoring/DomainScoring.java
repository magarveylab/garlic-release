package ca.mcmaster.magarveylab.matching.algorithm.scoring;

import java.util.Arrays;
import java.util.List;

import ca.mcmaster.magarveylab.enums.domains.DomainType;
import ca.mcmaster.magarveylab.enums.domains.RegulatorDomains;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.GeneticDomain;

public class DomainScoring {
	/**
	 * NOTE this method removes matches from both lists
	 * @param queryDomains
	 * @param subjectDomains
	 * @param couples
	 * @param coexpressionBonus
	 * @return
	 */
	public static double coExpressedMatches(List<GeneticDomain> queryDomains,
			List<GeneticDomain> subjectDomains, DomainType[][] couples, double coexpressionBonus, double genomeMultiplier) {
		double score = 0;
		
		for(DomainType[] couple : couples){
			GeneticDomain possibleQueryMatch = null;
			GeneticDomain possibleQueryMatch2 = null;
			for(GeneticDomain domain : queryDomains){
				if(Arrays.asList(couple).contains(domain.getDomainType())){
					if(possibleQueryMatch != null && !domain.getDomainType().equals(possibleQueryMatch.getDomainType())){
						possibleQueryMatch2 = domain;
						break;
					}else{
						possibleQueryMatch = domain;
					}
				}
			}
			if(possibleQueryMatch2 == null){ //No couple match in the query
				continue;
			}
			GeneticDomain possibleSubjectMatch = null;
			GeneticDomain possibleSubjectMatch2 = null;
			for(GeneticDomain domain : queryDomains){
				if(Arrays.asList(couple).contains(domain.getDomainType())){
					if(possibleSubjectMatch != null && !domain.getDomainType().equals(possibleSubjectMatch.getDomainType())){
						possibleSubjectMatch2 = domain;
						break;
					}else{
						possibleSubjectMatch = domain;
					}
				}
			}
			if(possibleSubjectMatch2 != null){ //Match in both subject and query for the couple
				if(possibleQueryMatch.inCluster() && possibleQueryMatch2.inCluster() && possibleSubjectMatch.inCluster() && possibleSubjectMatch2.inCluster()){
					score += coexpressionBonus;
				}else{
					score += coexpressionBonus * genomeMultiplier;
				}
				queryDomains.remove(possibleQueryMatch);
				queryDomains.remove(possibleQueryMatch2);
				subjectDomains.remove(possibleSubjectMatch);
				subjectDomains.remove(possibleSubjectMatch2);
				score += coexpressionBonus;
			}
		}
		return score;
	}

	/**
	 * NOTE this method removes matches from both lists
	 * @param queryDomains
	 * @param subjectDomains
	 * @param partials
	 * @param matchScore
	 * @param regulatorCommon 
	 * @return
	 */
	public static double partialMatches(List<GeneticDomain> queryDomains,
			List<GeneticDomain> subjectDomains, DomainType[][] partials, DomainType[] regulatorCommon, double matchScore, double genomeMultiplier, double commonMultiplier) {
		double score = 0;		
		for(int i = 0; queryDomains.size() > i; i++){
			GeneticDomain queryDomain = queryDomains.get(i);
			boolean hit = false;
			for(DomainType[] similarDomains : partials){
				if(Arrays.asList(similarDomains).contains(queryDomain.getDomainType())){
					for(GeneticDomain subjectDomain : subjectDomains){
						if(Arrays.asList(similarDomains).contains(subjectDomain.getDomainType())){
							double scoreToAdd = matchScore;
							if(Arrays.asList(regulatorCommon).contains(queryDomain.getDomainType()) || Arrays.asList(regulatorCommon).contains(subjectDomain.getDomainType())){ //Check one is a common
								scoreToAdd = scoreToAdd * commonMultiplier;
							}
							if(!queryDomain.inCluster() && !subjectDomain.inCluster()){
								scoreToAdd = scoreToAdd * genomeMultiplier;
							}
							score += scoreToAdd;
							subjectDomains.remove(subjectDomain);
							queryDomains.remove(queryDomain); 
							i--;
							hit = true;
							break; //don't keep looking for more matches for this query domain
						}
					}
				}
			}
			if(hit){ //Go to next domain in the query
				continue;
			}
		}
		return score;
	}

	/**
	 * NOTE this method removes matches from both lists
	 * @param queryDomains
	 * @param subjectDomains
	 * @param matchScore
	 * @return
	 */
	public static double exactMatchs(List<GeneticDomain> queryDomains, List<GeneticDomain> subjectDomains, DomainType[] commonDomains, double matchScore, double genomeMultiplier, double commonMultiplier) {
		double score = 0;
		for(int i = 0; queryDomains.size() > i; i++){
			GeneticDomain queryDomain = queryDomains.get(i);
			for(GeneticDomain subjectDomain : subjectDomains){
				if(queryDomain.getDomainType().equals(subjectDomain.getDomainType())){
					double scoreToAdd = matchScore;
					if(Arrays.asList(commonDomains).contains(queryDomain.getDomainType()) || Arrays.asList(commonDomains).contains(subjectDomain.getDomainType())){ //Check one is a common
						scoreToAdd = scoreToAdd * commonMultiplier;
					}
					if(!queryDomain.inCluster() && !subjectDomain.inCluster()){
						scoreToAdd = scoreToAdd * genomeMultiplier;
					}
					score += scoreToAdd;
					queryDomains.remove(queryDomain); i--;
					subjectDomains.remove(subjectDomain);
					break;
				}
			}
		}
		return score;
	}
}
