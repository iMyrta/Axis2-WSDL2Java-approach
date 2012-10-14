package com.itcuties.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.itcuties.model.HighScore;
import com.itcuties.serivces.AddHighScoreDocument;
import com.itcuties.serivces.GetHighScoresDocument;
import com.itcuties.serivces.GetHighScoresForNicknameDocument;
import com.itcuties.serivces.GetHighScoresForNicknameResponseDocument;
import com.itcuties.serivces.GetHighScoresForNicknameResponseDocument.GetHighScoresForNicknameResponse;
import com.itcuties.serivces.GetHighScoresResponseDocument;
import com.itcuties.serivces.GetHighScoresResponseDocument.GetHighScoresResponse;
import com.itcuties.serivces.GetHighScoresSizeDocument;
import com.itcuties.serivces.GetHighScoresSizeResponseDocument;
import com.itcuties.serivces.GetHighScoresSizeResponseDocument.GetHighScoresSizeResponse;


public class MyHighScoreServiceSkeleton implements
		HighScoreServiceSkeletonInterface {

	/*
	 * Warning!
	 * There are two HighScore classes
	 * 1) com.itcuties.model.HighScore 
	 * 	  this is model to store data on application side
	 * 2) com.itcuties.serivces.xsd.HighScore used to serialize/deserialize
	 *    data to/from WebService response/request
	 */
	
	// A list to hold highScores
	private static List<HighScore> highScores = new ArrayList<HighScore>();

	static {
		highScores.add(new HighScore("person1", 100));
		highScores.add(new HighScore("person2", 90));
		highScores.add(new HighScore("person3", 80));
		highScores.add(new HighScore("person4", 70));
		highScores.add(new HighScore("person1", 60));
		highScores.add(new HighScore("person2", 50));
		highScores.add(new HighScore("person7", 45));
		highScores.add(new HighScore("person8", 40));
		highScores.add(new HighScore("person1", 35));
		highScores.add(new HighScore("person10", 30));
	}


	public void addHighScore(AddHighScoreDocument addHighScore) {
		//get HighScore object from request
		com.itcuties.serivces.xsd.HighScore tmp = addHighScore.getAddHighScore().getScore();
	
		//create HighScore object to save on application side
		HighScore hsToAdd = new HighScore(tmp.getNickname(), tmp.getScore());
			
		synchronized (HighScoreServiceSkeleton.class) {
			//index at which the new high score is to be inserted
			int addAtPosition = -1;
			
			//iterate through highScores list
			for (int i = 0; i < highScores.size(); i++) {
				HighScore hs = highScores.get(i);
				//compare highScores
				if (hsToAdd.getScore() >= hs.getScore()) {
					//add before
					addAtPosition = i;
					break;
				} 
			}			
			
			if (addAtPosition != -1) {			
				//there are worse scores, add before them
				highScores.add(addAtPosition, hsToAdd);
			} else {
				//very low score, add at the end
				highScores.add(hsToAdd);
			}
		}
	}

	public GetHighScoresResponseDocument getHighScores(GetHighScoresDocument getHighScores) {

		Collection<com.itcuties.serivces.xsd.HighScore> result = new ArrayList<com.itcuties.serivces.xsd.HighScore>();

		synchronized (HighScoreServiceSkeleton.class) {
			//iterate through highScores list
			for (HighScore hs : highScores) {

				//create HighScore object 
				com.itcuties.serivces.xsd.HighScore tmp = com.itcuties.serivces.xsd.HighScore.Factory.newInstance();
				
				//set nickname and score on HighScore object
				tmp.setNickname(hs.getNickname());				
				tmp.setScore(hs.getScore());
				
				//add HighScore object to collection
				result.add(tmp);
			}
		}

		//create empty responseDocument
		GetHighScoresResponseDocument responseDoc = GetHighScoresResponseDocument.Factory
				.newInstance();
		
		//create empty response
		GetHighScoresResponse response = GetHighScoresResponse.Factory
				.newInstance();

		//set return value for response
		response.setReturnArray(result
				.toArray(new com.itcuties.serivces.xsd.HighScore[result.size()]));

		//add/set response to response document		
		responseDoc.setGetHighScoresResponse(response);
		
		return responseDoc;
	}

	public GetHighScoresForNicknameResponseDocument getHighScoresForNickname(GetHighScoresForNicknameDocument getHighScoresForNickname) {

		//get parameter from request
		String nickname = getHighScoresForNickname
				.getGetHighScoresForNickname().getNickname();
		
		Collection<com.itcuties.serivces.xsd.HighScore> result = new ArrayList<com.itcuties.serivces.xsd.HighScore>();

		if (nickname != null && !nickname.isEmpty()) {
			synchronized (HighScoreServiceSkeleton.class) {
				//iterate through highScores list in order to find results achieved by specified user
				for (HighScore hs : highScores) {
					if (hs.getNickname().equalsIgnoreCase(nickname)) {
					
						//create HighScore object 
						com.itcuties.serivces.xsd.HighScore tmp = com.itcuties.serivces.xsd.HighScore.Factory
								.newInstance();
						
						//set nickname and score on HighScore object
						tmp.setNickname(hs.getNickname());
						tmp.setScore(hs.getScore());
						
						//add HighScore object to collection
						result.add(tmp);
					}
				}
			}
		}

		//create empty responseDocument
		GetHighScoresForNicknameResponseDocument responseDoc = GetHighScoresForNicknameResponseDocument.Factory.newInstance();
		
		//create empty response
		GetHighScoresForNicknameResponse response = GetHighScoresForNicknameResponse.Factory.newInstance();

		//set return value for response
		response.setReturnArray(result.toArray(new com.itcuties.serivces.xsd.HighScore[result.size()]));

		//add/set response to response document		
		responseDoc.setGetHighScoresForNicknameResponse(response);
		
		return responseDoc;
	}

	public GetHighScoresSizeResponseDocument getHighScoresSize(GetHighScoresSizeDocument getHighScoresSize) {

		//create empty responseDocument
		GetHighScoresSizeResponseDocument responseDoc = GetHighScoresSizeResponseDocument.Factory
				.newInstance();
		
		//create empty response
		GetHighScoresSizeResponse response = GetHighScoresSizeResponse.Factory
				.newInstance();

		//set return value for response
		synchronized (HighScoreServiceSkeleton.class) {
			response.setReturn(highScores.size());
		}

		//add/set response to response document
		responseDoc.setGetHighScoresSizeResponse(response);
		
		return responseDoc;
	}

}
