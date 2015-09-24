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

package com.eduworks.russel.ui.client.pagebuilder.screen;

import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.gwt.client.util.Date;
import com.eduworks.russel.ui.client.Constants;
import com.eduworks.russel.ui.client.Russel;
import com.eduworks.russel.ui.client.handler.SearchHandler;
import com.eduworks.russel.ui.client.handler.StatusHandler;
import com.eduworks.russel.ui.client.model.FileRecord;
import com.eduworks.russel.ui.client.pagebuilder.RusselScreen;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * HomeScreen class
 * Extends ScreenTemplate
 * Defines methods and handlers for the RUSSEL Home screen.
 * 
 * @author Eduworks Corporation
 */
public class HomeScreen extends RusselScreen {

	private final SearchHandler ash = new SearchHandler(this, true); 

	/**
	 * lostFocus In place to handle any processing requirements required when this screen loses focus.
	 * Called by ScreenDispatch for all RUSSEL screens.
	 */
	@Override
	public void lostFocus() {
		ash.stop();
	}
	
	/**
	 * display Renders the RUSSEL home screen using appropriate templates and sets up handlers
	 */
	@Override
	public void display() {
		currentScreen = HOME_SCREEN;
		super.display();
		
		Constants.loggedInCheck.scheduleRepeating(1800000);
		
		// Set up Home screen tool tiles and recent items panel
		PageAssembler.ready(new HTML(Russel.htmlTemplates.getMenuBar().getText()));
		PageAssembler.ready(new HTML(Russel.htmlTemplates.getObjectPanel().getText()));
		PageAssembler.buildContents();
		StatusHandler.initialize();
		
		PageAssembler.inject("flowContainer", "x", new HTML(Russel.htmlTemplates.getDetailModal().getText()), true);
		PageAssembler.inject("objDetailPanelWidget", "x", new HTML(Russel.htmlTemplates.getDetailPanel().getText()), true);

		configureHeaderLinks();
		configureSearchBar0();		
		configurePageHandlers0();

	}

	private void configurePageHandlers0() {
		
		// First make sure that the appropriate tool tiles are shown based on if the user is authenticated or not
		JsArray<com.google.gwt.user.client.Element> eList = PageAssembler.getElementsByClass(".tile");
		if (Constants.getSessionId() == null || Constants.getSessionId() == "" || Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
			for (int i=0; i< eList.length(); i++) {
				if (eList.get(i).hasClassName("public")) {  // If a tool should be visible to the public, the 'public' class is present
					eList.get(i).removeClassName("hidden");
				}
				else {
					eList.get(i).addClassName("hidden");
				}
			}
		}
		else {
			for (int i=0; i< eList.length(); i++) {
				eList.get(i).removeClassName("hidden");
			}
		}

		PageAssembler.attachHandler("r-uploadContentTile", Event.ONCLICK, new EventCallback() {
					@Override
					public void onEvent(Event event) {
						if (Constants.getSessionId() == null || Constants.getSessionId() == "" || 
								Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
							pendingScreen2Load = UPLOAD_SCREEN;
							getDispatcher().loadLoginScreen();
						}
						else
							getDispatcher().loadEditScreen();
					}
				});
		
		PageAssembler.attachHandler("r-projectsTile", Event.ONCLICK, new EventCallback() {
					@Override
					public void onEvent(Event event) {
						if (Constants.getSessionId() == null || Constants.getSessionId() == "" || 
								Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
							pendingScreen2Load = PROJECTS_SCREEN;
							getDispatcher().loadLoginScreen();
						}
						else
							getDispatcher().loadFeatureScreen(FeatureScreen.PROJECTS_TYPE);
					}
			 });
		
		PageAssembler.attachHandler("r-collectionsTile", Event.ONCLICK, new EventCallback() {
				@Override
				public void onEvent(Event event) {
					if (Constants.getSessionId() == null || Constants.getSessionId() == "" || 
							Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
						pendingScreen2Load = COLLECTION_SCREEN;
						getDispatcher().loadLoginScreen();
					}
					else
						getDispatcher().loadFeatureScreen(FeatureScreen.COLLECTIONS_TYPE);
				}
			 });
		
		PageAssembler.attachHandler("r-manageUsersTile", Event.ONCLICK, new EventCallback() {
				@Override
				public void onEvent(Event event) {
					if (Constants.getSessionId() == null || Constants.getSessionId() == "" || 
							Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
						pendingScreen2Load = USERS_SCREEN;
						getDispatcher().loadLoginScreen();
					}
					else
						getDispatcher().loadUserScreen();
				}
			 });
		
		PageAssembler.attachHandler("r-groupTile", Event.ONCLICK, new EventCallback() {
				@Override
				public void onEvent(Event event) {
					if (Constants.getSessionId() == null || Constants.getSessionId() == "" || 
							Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
						pendingScreen2Load = GROUPS_SCREEN;
						getDispatcher().loadLoginScreen();
					}
					else
						getDispatcher().loadGroupScreen();
				}
			 });
		
		PageAssembler.attachHandler("r-objectEditSelected", Event.ONCLICK, new EventCallback() {
			   	@Override
			   	public void onEvent(Event event) {
					if (Constants.getSessionId() == null || Constants.getSessionId() == "" || 
							Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
						pendingScreen2Load = UPLOAD_SCREEN;
						getDispatcher().loadLoginScreen();
					}
					else
						getDispatcher().loadEditScreen(ash.getSelected());
			   	}
		    });
	}
	
	private void configureSearchBar0() {

		((TextBox)PageAssembler.elementToWidget("r-menuSearchBar", PageAssembler.TEXT)).setFocus(true);
		
		ash.hookAndClear("r-menuSearchBar", "searchObjectPanelScroll", SearchHandler.TYPE_RECENT);
		Date currentDate = new Date();
		Date pastDate = new Date();
		pastDate.setDate(pastDate.getDate()-10);
		ash.query(FileRecord.UPDATED_DATE + ":[" + pastDate.getTime() + " TO " + currentDate.getTime() + "]");
		
		PageAssembler.attachHandler("r-menuSearchBar", Event.ONKEYUP, new EventCallback() {
			@Override
			public void onEvent(Event event) {
				getDispatcher().loadSearchScreen(SearchHandler.TYPE_SEARCH);
			}
		});
	}

}