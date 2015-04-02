///*
//Copyright 2012-2013 Eduworks Corporation
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//*/
package com.eduworks.russel.ui.client.net;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.MultipartPost;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.google.gwt.http.client.URL;

public class RusselApi extends ESBApi {
	public static final String GENERATE_LINK_METADATA = "link";
	public static final String GENERATE_FILE_METADATA = "file";
	public static final String FLR_TYPE = "Learning Registry";
	
	public static String getUserListing(ESBCallback<ESBPacket> callback) {
		  ESBPacket jo = new ESBPacket(); 
		  MultipartPost mp = new MultipartPost();
		  jo.put("sessionId", sessionId);
		  mp.appendMultipartFormData("session", jo);
		  return CommunicationHub.sendMultipartPost(getESBActionURL("userListing"), mp, false, callback);
	}

	public static String deleteUser(String username, ESBCallback<ESBPacket> callback) {
		  ESBPacket jo = new ESBPacket(); 
		  MultipartPost mp = new MultipartPost();
		  jo.put("sessionId", sessionId);
		  jo.put("username", username);
		  mp.appendMultipartFormData("session", jo);
		  return CommunicationHub.sendMultipartPost(getESBActionURL("deleteUser"), mp, false, callback);

	}

	public static String resetUserPassword(String username, String password, ESBCallback<ESBPacket> callback) {
		  ESBPacket jo = new ESBPacket(); 
		  MultipartPost mp = new MultipartPost();
		  jo.put("sessionId", sessionId);
		  jo.put("username", username);
		  jo.put("password", password);
		  mp.appendMultipartFormData("session", jo);
		  return CommunicationHub.sendMultipartPost(getESBActionURL("userPasswordReset"), mp, false, callback);

	}

	public static String removeUserPermissions(String username, String permissionId, ESBCallback<ESBPacket> callback) {
		  ESBPacket jo = new ESBPacket(); 
		  MultipartPost mp = new MultipartPost();
		  jo.put("sessionId", sessionId);
		  jo.put("username", username);
		  jo.put("permissionId", permissionId);
		  mp.appendMultipartFormData("session", jo);
		  return CommunicationHub.sendMultipartPost(getESBActionURL("removeUserPermission"), mp, false, callback);

	}

	public static String addUserPermissions(String username, String permissionId, ESBCallback<ESBPacket> callback) {
		  ESBPacket jo = new ESBPacket(); 
		  MultipartPost mp = new MultipartPost();
		  jo.put("sessionId", sessionId);
		  jo.put("username", username);
		  jo.put("permissionId", permissionId);
		  mp.appendMultipartFormData("session", jo);
		  return CommunicationHub.sendMultipartPost(getESBActionURL("addUserPermission"), mp, false, callback);

	}

	public static String getUser(String selectedUsername, ESBCallback<ESBPacket> callback) {
		  ESBPacket jo = new ESBPacket(); 
		  MultipartPost mp = new MultipartPost();
		  jo.put("username", selectedUsername);
		  jo.put("sessionId", sessionId);
		  mp.appendMultipartFormData("session", jo);
		  return CommunicationHub.sendMultipartPost(getESBActionURL("getUserByUsername"), mp, false, callback);
	}
	
	public static String search(ESBPacket ap, String type, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ap.put("sessionId", sessionId);
		mp.appendMultipartFormData("session", ap);
		if (type!=FLR_TYPE)
			return CommunicationHub.sendMultipartPost(getESBActionURL("solrQuery"), 
										   			  mp, 
									   			  	  false, 	
									   			  	  callback);
		else
			return decalsBasicSearch(ap.getString("sq"), ap.getInteger("rows"), ap.containsKey("cursor")?ap.getInteger("cursor"):0, callback);
	}
	
   /**
    * Attempts to generate a metadata set for the given DECALS application repository resource
    * 
    * @param resourceId The ID of the resource to generate metadata
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGenerateDarFileResourceMetadata(String resourceId, String fileName, String mimeType, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put("sessionId", sessionId);
	  jo.put("mimeType", mimeType);
	  jo.put("fileId", resourceId);
	  jo.put("fileName", fileName);
	  jo.put("type", GENERATE_FILE_METADATA);
      mp.appendMultipartFormData("session", jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGenerateResourceMetadata"),mp,false,callback);
   }
   
   /**
    * Attempts to generate a metadata set for the given DECALS application repository resource
    * 
    * @param resourceId The ID of the resource to generate metadata
    * @param callback The event callback
    * @return Returns JSON result string
    */
   public static String decalsGenerateDarUrlResourceMetadata(String url, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();      
      jo.put("sessionId", sessionId);
      jo.put("type", GENERATE_LINK_METADATA);
      jo.put("url", url);
      mp.appendMultipartFormData("session", jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsGenerateResourceMetadata"),mp,false,callback);
   }
   
   /**
    * Perform a basic search
    * @param searchTerm The search term
    * @param rows The number of rows to return
    * @param page The page to start retrieval
    * @return Returns the query result JSON string
    */
   public static String decalsBasicSearch(String searchTerm, int rows, int page, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put("searchTerm", searchTerm);
      jo.put("itemsPerPage", rows);
      jo.put("page", page);
      mp.appendMultipartFormData("session", jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("decalsBasicSearch"),
                                 mp, 
                                 false, 
                                 callback);
   }
}
