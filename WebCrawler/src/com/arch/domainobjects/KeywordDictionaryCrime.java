package com.arch.domainobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordDictionaryCrime {
	
	//-- To make it effective and intelligent. To consider a news as crime related following checks required:
	//--1. news Should have "killed" AND not "Accident" not "crash"
	//--2 news Should have "Died" AND not "Accident" not "crash"
	//--3 Search for all forms of a keyword. for ex- kill, killed, killing. Or kidnapped, kidnapping, kidnap
	//--4 For intelligent searching need to look for regular expression related search and other things.
	//--5 Words to look for -- human trafficking charge
	//--6 A news should be considered once only as it is one crime incident reported a 
	// number of times as their is some progress in investigation.
	//-- if a new having crime keyword is also having "court, hearing, investigation, allegedly .." then it is most probably
	// reported already in the news and we should skip adding it for calculating severity.
private static final String crimeKeyWordsString = "stolen:5#mugged:5#robbed:6#attacked:5#vandalism:6#theft:5#thief:5#thieves:5#kidnapping:5"
		+ "#kidnapped:5#kidnap:5#illegal:4#disappeared:6#illegal:3#fraud:5#snatch:4#stole:4#steal:4#fake:4#murder:8#attempt to murder:7#"
		+ "hijacked:5#mugged:4#mug:4#assault:4#Burglary:5#Drug:6#scammers:5#scam:5#smuggle:7#fight&arrests:5#fight&arrested:5"
		+ "#fighting&arrests:5#fighting&arrested:5#dead:8#injured:5#detain:4#detained:4"
		+ "#slicing off&court!police!guilty:7#slice off&court!police!guilty:4"
		+ "#attack:5#rape:7#molested:6#ransack:5#vandalised:6#killed:8#kill:8#sedition:5#killing:8";
	
static Map<String, Integer> crimeKeywordsMap = new HashMap<String, Integer>();
static List<String> crimeKeyWordsList = new ArrayList<String>();

	public static void populateCrimeKeywordMapAndList(){
		String crimeKeywordsArray[] = crimeKeyWordsString.split("#");
		for(String crimeKeyword: crimeKeywordsArray){
			String keyValueCrimeArr[] = crimeKeyword.split(":");
			if(null != keyValueCrimeArr && keyValueCrimeArr[0].length() > 1){
				crimeKeyWordsList.add(keyValueCrimeArr[0].toUpperCase());
				crimeKeywordsMap.put(keyValueCrimeArr[0].toUpperCase(), Integer.parseInt(keyValueCrimeArr[1]));
			}
		}
	}

	/**
	 * @return the crimeKeywordsMap
	 */
	public static Map<String, Integer> getCrimeKeywordsMap() {
		return crimeKeywordsMap;
	}

	/**
	 * @param crimeKeywordsMap the crimeKeywordsMap to set
	 */
	public static void setCrimeKeywordsMap(Map<String, Integer> crimeKeywordsMap) {
		KeywordDictionaryCrime.crimeKeywordsMap = crimeKeywordsMap;
	}

	/**
	 * @return the crimeKeyWordsList
	 */
	public static List<String> getCrimeKeyWordsList() {
		return crimeKeyWordsList;
	}

	/**
	 * @param crimeKeyWordsList the crimeKeyWordsList to set
	 */
	public static void setCrimeKeyWordsList(List<String> crimeKeyWordsList) {
		KeywordDictionaryCrime.crimeKeyWordsList = crimeKeyWordsList;
	}

}
