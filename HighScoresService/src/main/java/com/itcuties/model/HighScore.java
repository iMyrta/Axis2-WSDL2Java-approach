package com.itcuties.model;

public class HighScore {
	// User nickname
	private String nickname;
	// User scores
	private int score;
	
	public HighScore() {
	}
	
	public HighScore(String nickname, int score) {
		this.nickname = nickname;
		this.score = score;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
}
