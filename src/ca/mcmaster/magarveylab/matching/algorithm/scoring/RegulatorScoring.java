package ca.mcmaster.magarveylab.matching.algorithm.scoring;

import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.magarveylab.enums.domains.RegulatorDomains;
import ca.mcmaster.magarveylab.matching.chem.nodetypes.GeneticDomain;

public class RegulatorScoring {
	
	//These are coexpressed, if both clusters contain a full set give a bonus
	private static RegulatorDomains[][] regulatorCouples = {
		    {RegulatorDomains.AfsQ1, RegulatorDomains.AfsQ2},
			{RegulatorDomains.CutR, RegulatorDomains.CutS},
			{RegulatorDomains.DraK, RegulatorDomains.DraR},
			{RegulatorDomains.EcrA1, RegulatorDomains.EcrA2},
			{RegulatorDomains.EcrE1, RegulatorDomains.EcrE2},
			{RegulatorDomains.RapA1, RegulatorDomains.RapA2},
			{RegulatorDomains.StrR, RegulatorDomains.AdpA},
			{RegulatorDomains.AbrA1, RegulatorDomains.AbrA2},
			{RegulatorDomains.CheA, RegulatorDomains.CheB},
			{RegulatorDomains.CheA, RegulatorDomains.CheV},
			{RegulatorDomains.CheA, RegulatorDomains.CheW},
			{RegulatorDomains.DraR, RegulatorDomains.DraK},
			{RegulatorDomains.FrzE, RegulatorDomains.FrzZ},
			{RegulatorDomains.GlnK, RegulatorDomains.GlnL},
			{RegulatorDomains.LytTR, RegulatorDomains.LytS},
			{RegulatorDomains.NarL, RegulatorDomains.NarX},
			{RegulatorDomains.NarL, RegulatorDomains.NarQ},
			{RegulatorDomains.NarP, RegulatorDomains.NarX},
			{RegulatorDomains.NarP, RegulatorDomains.NarQ},
			{RegulatorDomains.NtrB, RegulatorDomains.NtrC}
			};
	
	//These are similar domains, they receive partial match
	private static RegulatorDomains[][] regulatorSimilars = {
			{RegulatorDomains.OzmU, RegulatorDomains.NarL},
			{RegulatorDomains.SCO0204, RegulatorDomains.SCO3818},
			{RegulatorDomains.PapR6, RegulatorDomains.VmsT},
			{RegulatorDomains.RubS, RegulatorDomains.CmtI},
			{RegulatorDomains.OmpR, RegulatorDomains.AdpA},
			{RegulatorDomains.CutR, RegulatorDomains.RapA1},
			{RegulatorDomains.Sky44, RegulatorDomains.CyC_C},
			{RegulatorDomains.Spo0A, RegulatorDomains.CheV},
			{RegulatorDomains.GlnL, RegulatorDomains.YcbB},
			{RegulatorDomains.TylS, RegulatorDomains.MphR},
			{RegulatorDomains.DesT, RegulatorDomains.FabR},
			{RegulatorDomains.MmyR, RegulatorDomains.PlaR2},
			{RegulatorDomains.AbrC1, RegulatorDomains.AbrC2},
			{RegulatorDomains.MSMEG_6564, RegulatorDomains.SCO0332},
			{RegulatorDomains.NalC, RegulatorDomains.TvrR},
			{RegulatorDomains.BafG, RegulatorDomains.KanG},
			{RegulatorDomains.FrrA, RegulatorDomains.FadRT},
			{RegulatorDomains.CasR, RegulatorDomains.RmrR},
			{RegulatorDomains.RedD, RegulatorDomains.TylR},
			{RegulatorDomains.Aur1B, RegulatorDomains.PgaY},
			{RegulatorDomains.HnoR, RegulatorDomains.Asm29},
			{RegulatorDomains.CdaR, RegulatorDomains.GlnK},
			{RegulatorDomains.SanR1, RegulatorDomains.AbsA1},
			{RegulatorDomains.ArpR, RegulatorDomains.MepR},
			{RegulatorDomains.EmhR, RegulatorDomains.PmeR},
			{RegulatorDomains.SrpR, RegulatorDomains.TtgW},
			{RegulatorDomains.FrzZ, RegulatorDomains.HrtR},
			{RegulatorDomains.BtrR1, RegulatorDomains.MexZ},
			{RegulatorDomains.CifR, RegulatorDomains.SocA3},
			{RegulatorDomains.NanR2, RegulatorDomains.VieB},
			{RegulatorDomains.TarA, RegulatorDomains.Lct13},
			{RegulatorDomains.FarA, RegulatorDomains.ScbR},
			{RegulatorDomains.ArpA, RegulatorDomains.BarA},
			{RegulatorDomains.AvaR1, RegulatorDomains.SpbR},
			{RegulatorDomains.Tyl_type_Regulator, RegulatorDomains.TylP},
			{RegulatorDomains.Aur1R, RegulatorDomains.AvaR2},
			{RegulatorDomains.SrrC, RegulatorDomains.BarZ},
			{RegulatorDomains.KijA8, RegulatorDomains.KijR},
			{RegulatorDomains.KijC5, RegulatorDomains.TR},
			{RegulatorDomains.CalR1, RegulatorDomains.EsmT4},
			{RegulatorDomains.MonRII, RegulatorDomains.SchR3},
			{RegulatorDomains.TamK, RegulatorDomains.TrdK},
			{RegulatorDomains.AmeR, RegulatorDomains.RmiR},
			{RegulatorDomains.LuxR, RegulatorDomains.OpaR},
			{RegulatorDomains.NarQ, RegulatorDomains.NarX},
			{RegulatorDomains.AcuRs, RegulatorDomains.AcuR},
			{RegulatorDomains.DhaR, RegulatorDomains.NtrC},
			{RegulatorDomains.RifQ, RegulatorDomains.VarR},
			{RegulatorDomains.CheA, RegulatorDomains.FrzE},
			{RegulatorDomains.CheY, RegulatorDomains.ActR},
			{RegulatorDomains.PapR2, RegulatorDomains.PhaD},
			{RegulatorDomains.AcmP, RegulatorDomains.AcmU},
			{RegulatorDomains.CcaR, RegulatorDomains.EnvZ},
			{RegulatorDomains.Fad35R, RegulatorDomains.SACE_7040},
			{RegulatorDomains.ChryX5, RegulatorDomains.AmtR},
			{RegulatorDomains.AlpV, RegulatorDomains.BrtA},
			{RegulatorDomains.AtuR, RegulatorDomains.KstR2},
			{RegulatorDomains.KirRII, RegulatorDomains.LfrR},
			{RegulatorDomains.AfsA, RegulatorDomains.ScbA},
			{RegulatorDomains.BecM, RegulatorDomains.MlaM},
			{RegulatorDomains.PhlH, RegulatorDomains.PltZ},
			{RegulatorDomains.BspR, RegulatorDomains.SchA4},
			{RegulatorDomains.AsuR5, RegulatorDomains.LytTR},
			{RegulatorDomains.QdoR, RegulatorDomains.LmrA},
			{RegulatorDomains.SaqK, RegulatorDomains.UrdK},
			{RegulatorDomains.CmeR, RegulatorDomains.QacR},
			{RegulatorDomains.AmiP, RegulatorDomains.PksA},
			{RegulatorDomains.LuxT, RegulatorDomains.PsbI},
			{RegulatorDomains.NasR, RegulatorDomains.TylQ},
			{RegulatorDomains.RsbU, RegulatorDomains.CymR},
			{RegulatorDomains.CmmR, RegulatorDomains.CmtR},
			{RegulatorDomains.FscRII, RegulatorDomains.FscRIV},
			{RegulatorDomains.AveR, RegulatorDomains.FscRIII},
			{RegulatorDomains.PhoP, RegulatorDomains.DarR},
			{RegulatorDomains.Cpp1, RegulatorDomains.LytS},
			{RegulatorDomains.Cvm7P, RegulatorDomains.LysR},
			{RegulatorDomains.MedORF28, RegulatorDomains.FadRP},
			{RegulatorDomains.KinR, RegulatorDomains.XdhR},
			{RegulatorDomains.MtmR, RegulatorDomains.PrrB},
			{RegulatorDomains.CutS, RegulatorDomains.AfsQ2},
			{RegulatorDomains.PieR, RegulatorDomains.PleC},
			{RegulatorDomains.ThnU, RegulatorDomains.VieS},
			{RegulatorDomains.EcrA1, RegulatorDomains.EcrE1},
			{RegulatorDomains.ChlF2, RegulatorDomains.SCO0203},
			{RegulatorDomains.Tmn21, RegulatorDomains.Tsn22},
			{RegulatorDomains.Pyr27, RegulatorDomains.RemQ},
			{RegulatorDomains.CphR, RegulatorDomains.YesM},
			{RegulatorDomains.RsbW, RegulatorDomains.CamR},
			{RegulatorDomains.BdcR, RegulatorDomains.CsrA},
			{RegulatorDomains.RsbV, RegulatorDomains.CgmR},
			{RegulatorDomains.Rkl, RegulatorDomains.RegE},
			{RegulatorDomains.AlpU, RegulatorDomains.VieA},
			{RegulatorDomains.AdeN, RegulatorDomains.PigZ},
			{RegulatorDomains.CheW, RegulatorDomains.YesN},
			{RegulatorDomains.AknO, RegulatorDomains.StrR},
			{RegulatorDomains.PolR, RegulatorDomains.BldD},
			{RegulatorDomains.PimR, RegulatorDomains.Crp},
			{RegulatorDomains.EcrA2, RegulatorDomains.EcrE2}
			};
	
	//These domains cluster together phylogenetically. They get a partial match
	private static RegulatorDomains[][] regulatorClades = {
			{RegulatorDomains.PolR, RegulatorDomains.BldD, RegulatorDomains.PimR, RegulatorDomains.Crp, RegulatorDomains.AbrC3, RegulatorDomains.EcrA2, RegulatorDomains.EcrE2, RegulatorDomains.AbrA2, RegulatorDomains.SamR0468, RegulatorDomains.NarL, RegulatorDomains.OzmU, RegulatorDomains.NarP, RegulatorDomains.MnbR, RegulatorDomains.SCO0204, RegulatorDomains.SCO3818, RegulatorDomains.Sco5785, RegulatorDomains.AbsA2, RegulatorDomains.PapR6, RegulatorDomains.VmsT, RegulatorDomains.RubS, RegulatorDomains.CmtI, RegulatorDomains.ClaR, RegulatorDomains.RedZ, RegulatorDomains.OmpR, RegulatorDomains.AdpA, RegulatorDomains.AfsQ1, RegulatorDomains.PhoPs, RegulatorDomains.DraR, RegulatorDomains.JadR, RegulatorDomains.PrrA, RegulatorDomains.CutR, RegulatorDomains.RapA1, RegulatorDomains.Sky44, RegulatorDomains.CyC_C, RegulatorDomains.PleD, RegulatorDomains.Spo0A, RegulatorDomains.CheV, RegulatorDomains.CheB, RegulatorDomains.RpfG, RegulatorDomains.GlnL, RegulatorDomains.YcbB, RegulatorDomains.FrnG, RegulatorDomains.TylS, RegulatorDomains.MphR, RegulatorDomains.DesT, RegulatorDomains.FabR, RegulatorDomains.MmyR, RegulatorDomains.PlaR2},
			{RegulatorDomains.KijA8, RegulatorDomains.KijR, RegulatorDomains.KijC5, RegulatorDomains.TR, RegulatorDomains.CalR1, RegulatorDomains.EsmT4, RegulatorDomains.MonRII, RegulatorDomains.SchR3, RegulatorDomains.NapR3, RegulatorDomains.TcmR, RegulatorDomains.SCO3201, RegulatorDomains.NonG, RegulatorDomains.TamK, RegulatorDomains.TrdK, RegulatorDomains.SlgR1, RegulatorDomains.SsfT2, RegulatorDomains.Asm2, RegulatorDomains.AmeR, RegulatorDomains.RmiR, RegulatorDomains.LuxR, RegulatorDomains.OpaR, RegulatorDomains.SmcR, RegulatorDomains.VtpR, RegulatorDomains.VanT, RegulatorDomains.HapR, RegulatorDomains.LitR, RegulatorDomains.NarQ, RegulatorDomains.NarX, RegulatorDomains.CinR1, RegulatorDomains.RolR, RegulatorDomains.AcuRs, RegulatorDomains.AcuR, RegulatorDomains.DddH, RegulatorDomains.NemR, RegulatorDomains.DhaR, RegulatorDomains.NtrC, RegulatorDomains.ActII_orf4, RegulatorDomains.McbR},
			{RegulatorDomains.AbrC1, RegulatorDomains.AbrC2, RegulatorDomains.PlaR, RegulatorDomains.HemR, RegulatorDomains.MSMEG_6564, RegulatorDomains.SCO0332, RegulatorDomains.RemM, RegulatorDomains.RutR, RegulatorDomains.NalC, RegulatorDomains.TvrR, RegulatorDomains.AbyC, RegulatorDomains.AefR, RegulatorDomains.BafG, RegulatorDomains.KanG, RegulatorDomains.SAV3818, RegulatorDomains.LplR, RegulatorDomains.MerO, RegulatorDomains.FrrA, RegulatorDomains.FadRT, RegulatorDomains.CasR, RegulatorDomains.RmrR, RegulatorDomains.RedD, RegulatorDomains.TylR, RegulatorDomains.Rv3066, RegulatorDomains.Aur1B, RegulatorDomains.PgaY, RegulatorDomains.HnoR, RegulatorDomains.Asm29, RegulatorDomains.EncS, RegulatorDomains.BetI, RegulatorDomains.CdaR, RegulatorDomains.GlnK, RegulatorDomains.NtrB, RegulatorDomains.SanR1, RegulatorDomains.AbsA1, RegulatorDomains.Sco5784, RegulatorDomains.Pyr3},
			{RegulatorDomains.KirRII, RegulatorDomains.LfrR, RegulatorDomains.AfsA, RegulatorDomains.ScbA, RegulatorDomains.BarX, RegulatorDomains.NosP, RegulatorDomains.BecM, RegulatorDomains.MlaM, RegulatorDomains.Strop_2766, RegulatorDomains.PhlH, RegulatorDomains.PltZ, RegulatorDomains.PsrA, RegulatorDomains.BspR, RegulatorDomains.SchA4, RegulatorDomains.NicS, RegulatorDomains.ChlF1, RegulatorDomains.HlyIIR, RegulatorDomains.RefZ, RegulatorDomains.AsuR5, RegulatorDomains.LytTR, RegulatorDomains.IcaR, RegulatorDomains.QdoR, RegulatorDomains.LmrA, RegulatorDomains.SaqK, RegulatorDomains.UrdK, RegulatorDomains.LanK, RegulatorDomains.CmeR, RegulatorDomains.QacR, RegulatorDomains.AmiP, RegulatorDomains.PksA, RegulatorDomains.PapR4, RegulatorDomains.UidR},
			{RegulatorDomains.LuxT, RegulatorDomains.PsbI, RegulatorDomains.NasR, RegulatorDomains.TylQ, RegulatorDomains.RsbU, RegulatorDomains.CymR, RegulatorDomains.CmmR, RegulatorDomains.CmtR, RegulatorDomains.FscRII, RegulatorDomains.FscRIV, RegulatorDomains.AveR, RegulatorDomains.FscRIII, RegulatorDomains.RphD, RegulatorDomains.RapA2, RegulatorDomains.PhoP, RegulatorDomains.DarR, RegulatorDomains.SMU_1349, RegulatorDomains.DhaS, RegulatorDomains.SczA, RegulatorDomains.Cpp1, RegulatorDomains.LytS, RegulatorDomains.RamR, RegulatorDomains.Cvm7P, RegulatorDomains.LysR, RegulatorDomains.ThnI, RegulatorDomains.StgR, RegulatorDomains.PG1181, RegulatorDomains.MedORF28, RegulatorDomains.FadRP, RegulatorDomains.KinR, RegulatorDomains.XdhR},
			{RegulatorDomains.RifQ, RegulatorDomains.VarR, RegulatorDomains.Ecm10, RegulatorDomains.SCAB1401, RegulatorDomains.AcrR, RegulatorDomains.CheA, RegulatorDomains.FrzE, RegulatorDomains.AurD, RegulatorDomains.EthR, RegulatorDomains.CheY, RegulatorDomains.ActR, RegulatorDomains.SchA21, RegulatorDomains.MdoR, RegulatorDomains.NfxB, RegulatorDomains.PapR2, RegulatorDomains.PhaD, RegulatorDomains.AcmP, RegulatorDomains.AcmU, RegulatorDomains.CcaR, RegulatorDomains.EnvZ, RegulatorDomains.Mce3R, RegulatorDomains.Fad35R, RegulatorDomains.SACE_7040, RegulatorDomains.ChryX5, RegulatorDomains.AmtR, RegulatorDomains.AlpV, RegulatorDomains.BrtA, RegulatorDomains.CampR, RegulatorDomains.AtuR, RegulatorDomains.KstR2},
			{RegulatorDomains.RsbW, RegulatorDomains.CamR, RegulatorDomains.Tei8, RegulatorDomains.BdcR, RegulatorDomains.CsrA, RegulatorDomains.RsbV, RegulatorDomains.CgmR, RegulatorDomains.PaaR, RegulatorDomains.SlmA, RegulatorDomains.PimM, RegulatorDomains.Rkl, RegulatorDomains.RegE, RegulatorDomains.PhlF, RegulatorDomains.AlpU, RegulatorDomains.VieA, RegulatorDomains.AdeN, RegulatorDomains.PigZ, RegulatorDomains.YsiA, RegulatorDomains.CheW, RegulatorDomains.YesN, RegulatorDomains.BioQ, RegulatorDomains.SCO0253, RegulatorDomains.AguR, RegulatorDomains.SbtR, RegulatorDomains.AknO, RegulatorDomains.StrR, RegulatorDomains.FasR, RegulatorDomains.KstR, RegulatorDomains.SCO1712},                                                 
			{RegulatorDomains.MtmR, RegulatorDomains.PrrB, RegulatorDomains.CutS, RegulatorDomains.AfsQ2, RegulatorDomains.DraK, RegulatorDomains.PieR, RegulatorDomains.PleC, RegulatorDomains.PhoR, RegulatorDomains.PhoRs, RegulatorDomains.ThnU, RegulatorDomains.VieS, RegulatorDomains.RpfC, RegulatorDomains.EcrA1, RegulatorDomains.EcrE1, RegulatorDomains.AbrA1, RegulatorDomains.ChlF2, RegulatorDomains.SCO0203, RegulatorDomains.Tmn21, RegulatorDomains.Tsn22, RegulatorDomains.Pyr27, RegulatorDomains.RemQ, RegulatorDomains.OvmY, RegulatorDomains.AcnR, RegulatorDomains.CphR, RegulatorDomains.YesM, RegulatorDomains.RphA3, RegulatorDomains.LiuQ},
			{RegulatorDomains.ArpR, RegulatorDomains.MepR, RegulatorDomains.TtgR, RegulatorDomains.EmhR, RegulatorDomains.PmeR, RegulatorDomains.BpeR, RegulatorDomains.AcrRe, RegulatorDomains.MtrR, RegulatorDomains.SrpR, RegulatorDomains.TtgW, RegulatorDomains.EnvR, RegulatorDomains.FrzZ, RegulatorDomains.HrtR, RegulatorDomains.SmeT, RegulatorDomains.NalD, RegulatorDomains.BepR, RegulatorDomains.IfeR, RegulatorDomains.BtrR1, RegulatorDomains.MexZ, RegulatorDomains.CifR, RegulatorDomains.SocA3, RegulatorDomains.ComR, RegulatorDomains.Azi42, RegulatorDomains.NanR2},
			{RegulatorDomains.TarA, RegulatorDomains.Lct13, RegulatorDomains.SrrA, RegulatorDomains.FarA, RegulatorDomains.ScbR, RegulatorDomains.SngR, RegulatorDomains.ArpA, RegulatorDomains.BarA, RegulatorDomains.AvaR1, RegulatorDomains.SpbR, RegulatorDomains.Brp, RegulatorDomains.Tyl_type_Regulator, RegulatorDomains.TylP, RegulatorDomains.KsbA, RegulatorDomains.SscR, RegulatorDomains.PyrO, RegulatorDomains.Aur1R, RegulatorDomains.AvaR2, RegulatorDomains.AlpW, RegulatorDomains.SrrB, RegulatorDomains.Lct14, RegulatorDomains.SrrC, RegulatorDomains.BarZ}
	};
	
	private static RegulatorDomains[] regulatorCommon = {
		//RegulatorDomains.PhoP, RegulatorDomains.PhoR
	};
	
	//Scoring scheme for regulators
	private static double match = 1;
	private static double coexpressionBonus = 1;
	private static double similarMultiplier = 0.5;
	private static double regulatorMultiplier = 0.25;
	private static double commonRegulatorMultiplier = 0.25;
	private static double genomeRegulatorMultiplier = 0.25; //if one or both domains are in the genome use this multiplier
	
	/**
	 * @param queryRegulators
	 * @param subjectRegulators
	 * @return score between the regulators
	 */
	public static Double getRegulatoryMatchScore(List<GeneticDomain> queryRegulators, List<GeneticDomain> subjectRegulators){
		double score = 0;

		List<GeneticDomain> queryDomains1 = new ArrayList<GeneticDomain>(); //Made as a list that will remove objects as they get matched from exact and partial
		List<GeneticDomain> queryDomains2 = new ArrayList<GeneticDomain>(); //Made as a list that will remove object as they get matched from coExpression
		for(GeneticDomain queryDomain : queryRegulators){
			if(queryDomain.inCluster()){
				queryDomains1.add(0, queryDomain);
				queryDomains2.add(0, queryDomain);
			}else{
				if(queryDomains1.size() > 0){
					queryDomains1.add(queryDomains1.size() - 1, queryDomain);
					queryDomains2.add(queryDomains1.size() - 1, queryDomain);
				}else{
					queryDomains1.add(queryDomain);
					queryDomains2.add(queryDomain);
				}
			}

		}
		List<GeneticDomain> subjectDomains1 = new ArrayList<GeneticDomain>(); //Made as a list that will remove objects as they get matched from exact and partial
		List<GeneticDomain> subjectDomains2 = new ArrayList<GeneticDomain>(); //Made as a list that will remove object as they get matched from coExpression
		for(GeneticDomain subjectDomain : subjectRegulators){
			if(subjectDomain.inCluster()){//Sort by whether in cluster or not, so cluster regulators get matched first
				subjectDomains1.add(0, subjectDomain);
				subjectDomains2.add(0, subjectDomain);
			}else{
				if(subjectDomains1.size() > 0){
					subjectDomains1.add(subjectDomains1.size() - 1, subjectDomain);
					subjectDomains2.add(subjectDomains1.size() - 1, subjectDomain);
				}else{
					subjectDomains1.add(subjectDomain);
					subjectDomains2.add(subjectDomain);	
				}
			}
		}
		
		if(queryDomains2.size() == 0 || subjectDomains2.size() == 0){
			score = 0;
		}else{
			score += DomainScoring.exactMatchs(queryDomains1, subjectDomains1, regulatorCommon, match, genomeRegulatorMultiplier, commonRegulatorMultiplier);
			score += DomainScoring.partialMatches(queryDomains1, subjectDomains1, regulatorSimilars, regulatorCommon, match * similarMultiplier, genomeRegulatorMultiplier, commonRegulatorMultiplier);
			score += DomainScoring.partialMatches(queryDomains1, subjectDomains1, regulatorClades, regulatorCommon, match * regulatorMultiplier, genomeRegulatorMultiplier, commonRegulatorMultiplier);
			if(queryDomains2.size() > 1 && subjectDomains2.size() > 1){ //only do this if both have more than one domain
				score += DomainScoring.coExpressedMatches(queryDomains2, subjectDomains2, regulatorCouples, coexpressionBonus, genomeRegulatorMultiplier);
			}
		}
		return score;
	}
}
