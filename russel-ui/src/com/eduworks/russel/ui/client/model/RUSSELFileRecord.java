package com.eduworks.russel.ui.client.model;

import com.eduworks.gwt.client.model.FLRRecord;
import com.eduworks.gwt.client.model.FileRecord;
import com.eduworks.gwt.client.net.packet.ESBPacket;

public class RUSSELFileRecord extends FileRecord {
	public static final String NOTES = "notes_t";
	public static final String STRATEGY = "epssStrategy_t";
	public static final String USAGE_DELIMITER = "|";
	public static final String USAGE_STRATEGY_DELIMITER = "^";
	public static final String USAGE_COUNT_DELIMITER = "#";
    public static final String FLR_DOC_ID = "flrDocId_t";
    public static final String FLR_PARADATA_ID = "flrParadataId_t";

	private String flrDocId = "";
	private String flrParadataId = "";
	private String notes = "";
	private String strategy = "";

	public RUSSELFileRecord () {
		
	}
	
	public RUSSELFileRecord (ESBPacket esbPacket) {
		parseESBPacket(esbPacket);
	}
	
	@Override 
	public void parseESBPacket(ESBPacket metaDataPack) {
		ESBPacket esbPacket;
		if (metaDataPack.containsKey("obj"))
			esbPacket = new ESBPacket(metaDataPack.get("obj").isObject());
		else
			esbPacket = metaDataPack;
		super.parseESBPacket(esbPacket);
		if (esbPacket.containsKey(NOTES))
			notes = esbPacket.getString(NOTES);
		if (esbPacket.containsKey(STRATEGY))
			strategy = esbPacket.getString(STRATEGY);
		if (esbPacket.containsKey(FLR_DOC_ID))
			flrDocId = esbPacket.getString(FLR_DOC_ID);
		if (esbPacket.containsKey(FLR_PARADATA_ID))
			flrParadataId = esbPacket.getString(FLR_PARADATA_ID);
	}
	
	@Override
	public String getFieldList() {
		return super.getFieldList() + " " + NOTES + " " + STRATEGY + " " + FLR_DOC_ID + " " + FLR_PARADATA_ID;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	@Override
	public String toString() {
		ESBPacket esbPacket = super.toObject();
		esbPacket.put(NOTES, notes);
		esbPacket.put(STRATEGY, strategy);
		esbPacket.put(FLR_DOC_ID, flrDocId);
		esbPacket.put(FLR_PARADATA_ID, flrParadataId);
		return esbPacket.toString();
	}
	
	@Override
	public ESBPacket toObject() {
		ESBPacket esbPacket = super.toObject();
		esbPacket.put(NOTES, notes);
		esbPacket.put(STRATEGY, strategy);
		esbPacket.put(FLR_DOC_ID, flrDocId);
		esbPacket.put(FLR_PARADATA_ID, flrParadataId);
		return esbPacket;
	}

	public String getFlrDocId() {
		return flrDocId;
	}

	public void setFlrDocId(String flrDocId) {
		this.flrDocId = flrDocId;
	}
	
	public FLRRecord toFLRRecord() {
		FLRRecord record = new FLRRecord(this.toObject());
		return record;
	}
	
	public String getFlrParadataId() {
		return flrParadataId;
	}

	public void setFlrParadataId(String flrParadataId) {
		this.flrParadataId = flrParadataId;
	}
}
