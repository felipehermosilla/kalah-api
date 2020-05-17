package com.fhermosilla.kalah.game.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fhermosilla.kalah.game.enums.GameStatusEnum;
import com.fhermosilla.kalah.game.enums.TurnValueEnum;

/**
 *
 * @author felipehermosilla
 * This object represent the game table
 */

@Entity
@Table(name = "game")
public class Game implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonInclude()
	@Transient
	private List<Bucket> buckets;

	@Enumerated(EnumType.STRING)
	@Column(name = "turn")
	private TurnValueEnum turn;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private GameStatusEnum status;

	@Column(name = "winner")
	private String winner;

	@Column(name = "current_index")
	private Integer currentIndex;

	public Game() {
		this.buckets = new ArrayList<>();
		this.turn = TurnValueEnum.FIRST_PLAYER;
		this.currentIndex = 1;
		this.status = GameStatusEnum.IN_PROGRESS;
	}

	public Game(List<Bucket> buckets) {
		this();
		this.buckets = buckets;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GameStatusEnum getStatus() {
		return status;
	}

	public void setStatus(GameStatusEnum status) {
		this.status = status;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public List<Bucket> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<Bucket> buckets) {
		this.buckets = buckets;
	}

	public TurnValueEnum getTurn() {
		return turn;
	}

	public void setTurn(TurnValueEnum turn) {
		this.turn = turn;
	}

	public Integer getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex;
	}

}
