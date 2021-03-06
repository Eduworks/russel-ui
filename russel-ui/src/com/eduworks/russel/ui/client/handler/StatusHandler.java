/*
Copyright 2012-2013 Eduworks Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.eduworks.russel.ui.client.handler;

import java.util.Vector;

import com.eduworks.gwt.client.model.StatusRecord;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

/**
 * StatusWindowHandler
 * Defines globals, methods and handlers for the Status Window.
 * 
 * @author Eduworks Corporation
 */
public class StatusHandler {
	private static final String statusItemTemplate = "<div id=\"x-status\" class=\"alert-box\"><span id=\"x-statusMessage\"></span><a id=\"x-statusClose\" class=\"close finger\">&times;</a></div>";
	private static Vector<StatusRecord> messages = new Vector<StatusRecord>();

	public static final String INVALID_FILENAME = "Invalid filename. Please try again.";
	public static final String INVALID_NAME = "Invalid name. Please try again.";
	public static final String DUPLICATE_NAME = "Can't have a duplicate name please try again";

	/**
	 * initialize Initializes the RUSSEL status window
	 */
	public static void initialize() {
		PageAssembler.attachHandler("statusClear", 
									Event.ONCLICK, 
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											clearMessages();
										}
									});
		clearMessages();
	}
	
	public static String getDeleteGroupDone(String groupname) {
		return "Deleting " + groupname;
	}

	public static String getDeleteGroupError(String groupname) {
		return "Deleting " + groupname + " has failed. Do you have permission to perform this action?";
	}

	public static String getDeleteGroupBusy(String groupname) {
		return "Deleting " + groupname + ". Please wait.";
	}
	
	public static String getCreateGroupDone(String groupname) {
		return "Created " + groupname;
	}

	public static String getCreateGroupError(String groupname) {
		return "Creating " + groupname + " has failed. Group may have already been created or you don't have permission to perform this action.";
	}

	public static String getCreateGroupBusy(String groupname) {
		return "Creating " + groupname + ". Please wait.";
	}
	
	public static String getResetUserPasswordBusy(String username) {
		return "Reseting " + username + " password. Please wait.";
	}
	
	public static String getResetUserPasswordError(String username) {
		return "Reseting " + username + " has failed. Do you have permission to perform this action?";
	}
	
	public static String getResetUserPasswordDone(String username) {
		return "Reseting " + username + " has been completed";
	}
	
	public static String getCreateUserBusy(String username) {
		return "Creating " + username + ". Please wait.";
	}
	
	public static String getCreateUserError(String username) {
		return "Creating " + username + " has failed. User may have already been created or you don't have permission to perform this action.";
	}
	
	public static String getCreateUserDone(String username) {
		return "Created " + username;
	}
	
	public static String getDeleteUserBusy(String username) {
		return "Deleting " + username + ". Please wait.";
	}
	
	public static String getDeleteUserError(String username) {
		return "Deleting " + username + " has failed. Do you have permission to perform this action?";
	}
	
	public static String getDeleteUserDone(String username) {
		return "Deleted " + username;
	}
	
	public static String getSetUserPermissionsBusy(String username) {
		return "Setting " + username + " permissions. Please wait.";
	}
	
	public static String getSetUserPermissionsError(String username) {
		return "Setting " + username + " permissions has failed. Do you have permission to perform this action?";
	}
	
	public static String getSetUserPermissionsDone(String username) {
		return "Set " + username + " permissions.";
	}
	
	/**
	 * getHomeMessageError Builds error message for missing user home folder
	 * @param directory String
	 * @return String
	 */
	public static String getHomeMessageError(String directory) {
		return "This user does not have a folder in <span class=\"fileName\">" + directory+ "</span>. Defaulting to company home for file uploads";
	}
	
	/**
	 * getZipImportMessageError Builds error message for zip entry import error
	 * @param filename String
	 * @return String
	 */
	public static String getZipImportMessageError(String filename) {
		return "Failed to import zip entries in <span class=\"fileName\">" + filename + "</span>";
	}
	
	/**
	 * getZipExportMessageError Builds error message for zip export
	 * @param filename String
	 * @return String
	 */
	public static String getZipExportMessageError(String filename) {
		return "Failed to export <span class=\"fileName\">" + filename + "</span>";
	}
	
	/**
	 * getZipExportMessageDone Builds message for successful export
	 * @param filename String
	 * @return String
	 */
	public static String getZipExportMessageDone(String filename) {
		return "Exported <span class=\"fileName\">" + filename + "</span>";
	}
	
	/**
	 * getSearchMessageError Builds search failure message
	 * @param terms String 
	 * @return String
	 */
	public static String getSearchMessageError(String terms) {
		return "Search failed on <span class=\"fileName\">" + terms + "</span> Please try again.";
	}
	
	/**
	 * getFileMessageBusy Builds busy uploading message
	 * @param filename String
	 * @return String
	 */
	public static String getFileMessageBusy(String filename) {
		return "Uploading <span class=\"fileName\">" +filename+ "</span> ... ";
	}
	
	/**
	 * getFileMessageDone Builds completed upload message
	 * @param filename String
	 * @return String
	 */
	public static String getFileMessageDone(String filename) {
		return "<span class=\"fileName\">" +filename+ "</span> is ready. ";
	}
	
	/**
	 * getFileMessageError Builds upload error message
	 * @param filename String
	 * @return String
	 */
	public static String getFileMessageError(String filename) {
		return "There was a problem loading <span class=\"fileName\">" +filename+ "</span> Please try again. ";
	}
	
	/**
	 * getDeleteMessageError Builds delete failure message
	 * @param filename String
	 * @return String
	 */
	public static String getDeleteMessageError(String filename) {
		return "Failed deleting <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getDeleteMessageDone Builds delete success message
	 * @param filename String
	 * @return String
	 */
	public static String getDeleteMessageDone(String filename) {
		return "Deleted <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getDeleteMessageBusy Builds busy deleting message
	 * @param filename String
	 * @return String
	 */
	public static String getDeleteMessageBusy(String filename) {
		return "Deleting <span class=\"\">" +filename+ "</span>";
	}
	
	/**
	 * getUpdateMetadataMessageError Builds metadata update failure message
	 * @param filename String
	 * @return String
	 */
	public static String getUpdateMetadataMessageError(String filename) {
		return "Failed to update <span class=\"fileName\">" +filename+ "</span> metadata.";
	}
	
	/**
	 * getUpdateMetadataMessageDone Builds metadata update completion message
	 * @param filename String
	 * @return String
	 */
	public static String getUpdateMetadataMessageDone(String filename) {
		return "Updated <span class=\"fileName\">" +filename+ "</span> metadata.";
	}
	
	/**
	 * getUpdateMetadataMessageBusy Builds busy updating metadata message
	 * @param filename String
	 * @return String
	 */
	public static String getUpdateMetadataMessageBusy(String filename) {
		return "Updating <span class=\"fileName\">" +filename+ "</span> metadata.";
	}
	
	/**
	 * getMetadatamessageError Builds metadata retrieval error message
	 * @param filename String
	 * @return String
	 */
	public static String getMetadataMessageError(String filename) {
		return "Failed to get metadata for <span class=\"fileName\">" +filename+ "</span> metadata.";
	}
	
	/**
	 * getProjectLoadMessageError Builds file load error message
	 * @param filename String
	 * @return String
	 */
	public static String getProjectLoadMessageError(String filename) {
		return "Failed to load <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getProjectLoadMessageError Builds generate metadata busy message
	 * @param filename String
	 * @return String
	 */
	public static String getGenerateMetaDataBusy(String filename) {
		return "Generating metadata for <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getProjectLoadMessageError Builds generate metadata done message
	 * @param filename String
	 * @return String
	 */
	public static String getGenerateMetaDataDone(String filename) {
		return "Success: Generating metadata for <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getProjectLoadMessageError Builds generate metadata error message
	 * @param filename String
	 * @return String
	 */
	public static String getGenerateMetaDataError(String filename) {
		return "Error: Generating metadata for <span class=\"fileName\">" +filename+ "</span>";
	}
	
	
	/**
	 * getProjectSaveMessageError Builds project save failure message
	 * @param filename String
	 * @return String
	 */
	public static String getProjectSaveMessageError(String filename) {
		return "Failed to save <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getCommentMessageError Builds message for failure to add a comment to a file
	 * @param filename String
	 * @return String
	 */
	public static String getCommentMessageError(String filename) {
		return "Failed to add a comment to <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getRemoveCommentMessageError Builds message for failure to remove a comment from a file
	 * @param filename String
	 * @return String
	 */
	public static String getRemoveCommentMessageError(String filename) {
		return "Failed to remove comment from <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getRatingPostError Builds error message for failing to add a rating to a file
	 * @param filename String
	 * @return String
	 */
	public static String getRatingPostError(String filename) {
		return "Failed to add a rating to <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getRatingMessageError Builds error message for failing to retrieve the ratings for a file
	 * @param filename String
	 * @return String
	 */
	public static String getRatingMessageError(String filename) {
		return "Failed to get the ratings for <span class=\"fileName\">" +filename+ "</span>";
	}
	
	/**
	 * getRateOwnDdocumentError Builds error message for failing to rate a file that you own
	 * @param filename String
	 * @return String
	 */
	public static String getRateOwnDocumentError(String filename) {
		return "Cannot rate <span class=\"fileName\">" +filename+ "</span> because you own it.";
	}
	
	/**
	 * getFLRMessageBusy Builds posting to FLR busy message
	 * @param filename String
	 * @return String
	 */
	public static String getFLRMessageBusy(String filename) {
		return "Posting <span class=\"fileName\">" +filename+ "</span> to FLR... ";
	}
	
	/**
	 * getFLRMessageDone Builds completed posting to FLR message
	 * @param filename String
	 * @return String
	 */
	public static String getFLRMessageDone(String filename) {
		return "<span class=\"fileName\">" +filename+ "</span> is posted on FLR. ";
	}
	
	/**
	 * getFLRMessageError Builds error message for failing to post to the FLR
	 * @param filename String
	 * @return String
	 */
	public static String getFLRMessageError(String filename) {
		return "There was a problem posting <span class=\"fileName\">" +filename+ "</span> to FLR. Please try again. ";
	}

	/**
	 * getFLRActivityBusy Builds busy message for posting to FLR Activity Stream
	 * @param filename String
	 * @return String
	 */
	public static String getFLRActivityBusy(String filename) {
		return "Posting <span class=\"fileName\">" +filename+ "</span> activity to FLR... ";
	}
	
	/**
	 * getFLRActivityDone Builds message for successful post to FLR Activity Stream
	 * @param filename String
	 * @return String
	 */
	public static String getFLRActivityDone(String filename) {
		return "<span class=\"fileName\">" +filename+ "</span> activity is posted on FLR. ";
	}
	
	/**
	 * getFLRActivityError Builds error message for failed post to FLR Activity Stream
	 * @param filename String
	 * @return String
	 */
	public static String getFLRActivityError(String filename) {
		return "There was a problem posting <span class=\"fileName\">" +filename+ "</span> activity to FLR. Please try again. ";
	}
	
	/**
	 * getFLRActivityError Builds error message for failed post to FLR Activity Stream
	 * @param filename String
	 * @return String
	 */
	public static String getFLRDeleteActivityError(String filename) {
		return "There was a problem deleting <span class=\"fileName\">" +filename+ "</span> activity to FLR. Please try again. ";
	}

	/**
	 * getFLRActivityError Builds error message for failed post to FLR Activity Stream
	 * @param filename String
	 * @return String
	 */
	public static String getFLRDeleteMessageError(String filename) {
		return "There was a problem deleting <span class=\"fileName\">" +filename+ "</span> to FLR. Please try again. ";
	}

	/**
	 * getFLRActivityError Builds error message for failed post to FLR Activity Stream
	 * @param filename String
	 * @return String
	 */
	public static String getFLRDeleteMessageDone(String filename) {
		return "Deleted metadata of <span class=\"fileName\">" +filename+ "</span> from FLR. ";
	}
	
	/**
	 * getFLRActivityError Builds error message for failed post to FLR Activity Stream
	 * @param filename String
	 * @return String
	 */
	public static String getFLRDeleteParadataDone(String filename) {
		return "Deleted activity stream of <span class=\"fileName\">" +filename+ "</span> from FLR. ";
	}

	/**
	 * getFLRHarvestMessageBusy Builds busy message for harvesting the FLR
	 * @param filename
	 * @return
	 */
	public static String getFLRHarvestMessageBusy(String filename) {
		return "Harvesting the <span class=\"fileName\">" +filename+ "</span>... ";
	}
	
	/**
	 * getFLRHarvestDone Builds completion message for harvesting the FLR
	 * @param good String Count of total nodes harvested
	 * @param bad String Count of nodes not harvested due to problem with FLR record
	 * @param ugly String Count of harvested nodes that were not fully parsed (unrecognized format)
	 * @return String
	 */
	public static String getFLRHarvestDone(String good, String bad, String ugly) {
		return "<span class=\"fileName\">FLR Harvest</span> complete.<br/> "+good+" records were duplicated, <br/>"+ugly+" partially duplicated, <br/>and "+bad+" had errors.";
	}
	
	/**
	 * getFLRHarvestError Builds error message for FLR harvest operation
	 * @return String
	 */
	public static String getFLRHarvestError() {
		return "<span class=\"fileName\">FLR Harvest</span> failed. Please try again.";
	}
	
	/**
	 * getFLRDisabledError Builds message for disabled FLR operation
	 * @param op String
	 * @return String
	 */
	public static String getFLRDisabledError(String op) {
		return "<span class=\"fileName\"> FLR "+op+"</span> is currently disabled.";
	}
	
	/**
	 * get3DRQueryMessageBusy Builds busy message for 3DR query
	 * @return String
	 */
	public static String get3DRQueryMessageBusy() {
		return "Querying the <span class=\"fileName\">ADL 3D Repository</span>... ";
	}
	
	/**
	 * get3DRQuerymessageDone Builds completed 3DR query message
	 * @return String
	 */
	public static String get3DRQueryMessageDone() {
		return "<span class=\"fileName\">ADL 3DR</span> query complete.";
	}
	
	/**
	 * get3DRQuerymessageError Builds error message for 3DR query
	 * @return String
	 */
	public static String get3DRQueryMessageError() {
		return "<span class=\"fileName\">ADL 3DR </span> query failed. Please try again.";
	}

	/** 
	 * get3DRQueryMessageEmpty Builds empty search term warning message for 3DR query
	 * @return String
	 */
	public static String get3DRQueryMessageEmpty() {
		return "<span class=\"fileName\">ADL 3DR </span> query is empty. Please provide search term.";
	}
	
	/**
	 * get3DRReviewMessageDone Builds completion message for 3DR review operation on a file
	 * @param op String Name of review operation
	 * @param id String 3DR node id
	 * @return String
	 */
	public static String get3DRReviewMessageDone(String op, String id) {
		return "<span class=\"fileName\">ADL 3DR</span> "+op+" of "+id+" complete.";
	}
	
	/**
	 * get3DRReviewMessageError Builds error message for review operation on a 3DR file
	 * @param op String name of review operation
	 * @param id String 3DR node id
	 * @return String
	 */
	public static String get3DRReviewMessageError(String op, String id) {
		return "<span class=\"fileName\">ADL 3DR</span> "+op+" of "+id+" failed.";
	}
	
	/**
	 * get3DRReviewMessageWarn Builds warning message for review operation on a 3DR file
	 * @return String
	 */
	public static String get3DRReviewMessageWarn() {
		return "<span class=\"fileName\">ADL 3DR</span> requires ratings to be submitted with comments.";
	}

	/**
	 * get3DRDisabledError Builds error message for disabled 3DR operation
	 * @param op String name of 3DR operation
	 * @return String
	 */
	public static String get3DRDisabledError(String op) {
		return "<span class=\"fileName\"> 3DR "+op+"</span> is currently disabled.";
	}
	
	/**
	 * createMessage Creates a message entry in the global status window
	 * @param message String 
	 * @param initialState String
	 * @return StatusRecord
	 */
	public static StatusRecord createMessage(String message, String initialState) {
		StatusRecord msg = new StatusRecord();
		msg.setMessage(message);
		msg.setState(initialState);
		msg.setRendered(false);
		messages.add(msg);
		refreshMessages();
		return msg;
	}
	
	/**
	 * removeMessage Removes a message entry in the global status window
	 * @param message StatusRecord
	 */
	public static void removeMessage(StatusRecord message) {
		DOM.getElementById(message.getGuid() + "-status").removeFromParent();
		messages.remove(message);
		refreshMessages();
	}
	
	/**
	 * refreshMessages Refreshes the messages in the global status window
	 */
	private static void refreshMessages() {
		int messageCount = messages.size();
		for (int messageIndex=0;messageIndex<messageCount;messageIndex++) {
			if (!messages.get(messageIndex).getRendered()) {
				Vector<String> ids = PageAssembler.inject("statusList", "x", new HTML(statusItemTemplate), true);
				String idNumPrefix = ids.get(0).substring(0, ids.get(0).indexOf("-"));
				final StatusRecord message = messages.get(messageIndex);
				message.setGuid(idNumPrefix);
				PageAssembler.merge(idNumPrefix + "-statusMessage", "x", new HTML(message.getMessage()).getElement());
				message.setRendered(true);
				if (message.getState()!="")
					DOM.getElementById(idNumPrefix + "-status").addClassName(message.getState());
				PageAssembler.attachHandler(idNumPrefix + "-statusClose", 
											Event.ONCLICK, 
											new EventCallback() {
												@Override
												public void onEvent(Event event) {
													removeMessage(message);
												}
											});
			}
		}
		if (messageCount>0) {
			DOM.getElementById("statusWindow").removeClassName("hidden");
			DOM.getElementById("count").setInnerText(String.valueOf(messageCount));
			DOM.getElementById("count").removeClassName("hidden");
		} else {
			DOM.getElementById("count").addClassName("hidden");
			DOM.getElementById("statusWindow").addClassName("hidden");
			hideStatusList0(DOM.getElementById("statusList"));
			DOM.getElementById("statusWindow").removeClassName("expand");
		}
		
		boolean hasError = false;
		boolean isBusy = false;
		for (int messageIndex=0;messageIndex<messageCount;messageIndex++)
			if (messages.get(messageIndex).getState()==StatusRecord.ALERT_ERROR)
				hasError = true;
			else if (messages.get(messageIndex).getState()==StatusRecord.ALERT_BUSY)
				isBusy = true;
		DOM.getElementById("icon").removeClassName(StatusRecord.STATUS_BUSY);
		DOM.getElementById("icon").removeClassName(StatusRecord.STATUS_ERROR);
		DOM.getElementById("icon").removeClassName(StatusRecord.STATUS_DONE);
		if (hasError)
			DOM.getElementById("icon").addClassName(StatusRecord.STATUS_ERROR);
		else if (isBusy)
			DOM.getElementById("icon").addClassName(StatusRecord.STATUS_BUSY);
		else
			DOM.getElementById("icon").addClassName(StatusRecord.STATUS_DONE);
	}
	
	/** 
	 * alterMessage Changes the contents of the designated message in the global status window
	 * @param message StatusRecord
	 */
	public static void alterMessage(StatusRecord message) {
		int messageCount = messages.size();
		for (int messageIndex=0;messageIndex<messageCount;messageIndex++) {
			if (messages.get(messageIndex).getGuid()==message.getGuid()) {
				messages.get(messageIndex).setMessage(message.getMessage());
				messages.get(messageIndex).setState(message.getState());
				messages.get(messageIndex).setRendered(false);
				DOM.getElementById(message.getGuid() + "-status").getParentElement().removeFromParent();
			}
		}		
		refreshMessages();
	}
	
	/**
	 * hideStatusList0 Hides the display of the element provided
	 * @param element Element
	 */
	private static native void hideStatusList0(Element element) /*-{
		element.style.display = "none";
	}-*/;
		
	/**
	 * clearMessages Clears all of the messages in the global status window
	 */
	public static void clearMessages() {
		int messageCount = DOM.getElementById("statusList").getChildCount();
		for (int messageIndex=0;messageIndex<messageCount;messageIndex++)
			DOM.getElementById("statusList").removeChild(DOM.getElementById("statusList").getChild(0));
		messages.clear();
		refreshMessages();
	}
}
