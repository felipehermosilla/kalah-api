package com.fhermosilla.kalah.game.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author felipehermosilla
 * This object represent the bucket table
 */
@Entity
@Table(name="bucket")
public class Bucket implements  Serializable {

	@Id
	@Column(name = "id") 
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	@Column(name = "bucket_id") 
    private Integer bucketId;

	@Column(name = "piece") 
    private Integer pieces;

	public Bucket() {
	}
	public Bucket(Integer bucketId, Integer pieces) {
		this.bucketId = bucketId;
		this.pieces = pieces;
	}
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBucketId() {
		return bucketId;
	}

	public void setBucketId(Integer bucketId) {
		this.bucketId = bucketId;
	}

	public Integer getPieces() {
		return pieces;
	}

	public void setPieces(Integer pieces) {
		this.pieces = pieces;
	}

	@JsonIgnore
    public Boolean isEmpty (){
        return this.getPieces() == 0;
    }

    public void clearPieces (){
        this.setPieces(0);
    }

    public void place () {
        setPieces(getPieces()+1);
    }

    public void addPieces (Integer pieces){
    	setPieces(getPieces()+pieces);
    }

    @Override
    public String toString() {
        return  "bucketId: "+ bucketId.toString() +
                "pieces:" + pieces.toString() ;
    }
}
