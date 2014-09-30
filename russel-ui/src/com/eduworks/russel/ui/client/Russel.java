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

package com.eduworks.russel.ui.client;

import com.eduworks.gwt.client.component.AppEntry;
import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.russel.ui.client.pagebuilder.HtmlTemplates;
import com.eduworks.russel.ui.client.pagebuilder.screen.LoginScreen;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;

/**
 * Russel
 * Extends Constants
 * Implements EntryPoint and ValueChangeHandler
 * 
 * @author Eduworks Corporation
 */
public class Russel extends AppEntry
{
	private static String detailId = null;

	/**
	 * getDetailId  returns the value of private static detailId
	 * @return string
	 */
	public static String getDetailId() {
		String temp = detailId;
		detailId = null;
		return temp;
	}
	
		
	/**
	 *  onModuleLoad Initializes handlers, data, and history for the RUSSEL module.
	 *  This module reads in the contents of "../js/installation.settings" to configure the RUSSEl instance.
	 */
	@Override
	public void onModuleLoad()
	{
		ESBApi.esbURL = CommunicationHub.rootURL + "l/levr/api/custom/";
		detailId = Window.Location.getParameter("id");
		defaultScreen = new LoginScreen();
		dispatcher = new ScreenDispatch();
		templates = GWT.create(HtmlTemplates.class);
	}
}