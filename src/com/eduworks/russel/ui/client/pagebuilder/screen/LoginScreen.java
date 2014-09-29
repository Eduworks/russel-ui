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

import java.util.Vector;

import com.eduworks.gwt.client.model.ZipRecord;
import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.gwt.client.util.Logger;
import com.eduworks.russel.ui.client.Constants;
import com.eduworks.russel.ui.client.Russel;
import com.eduworks.russel.ui.client.handler.StatusWindowHandler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * LoginScreen class
 * Extends ScreenTemplate
 * Defines methods and handlers for the Login screen.
 * 
 * @author Eduworks Corporation
 */
public class LoginScreen extends Screen {
	private final String LOGIN_BAD_LOGIN = "Login name or password is not valid.";
	private final String SERVER_UNAVAILABLE = "The server is unavailable."; 

	/**
	 * lostFocus In place to handle any processing requirements required when this screen loses focus.
	 * Called by ScreenDispatch for all RUSSEL screens.
	 */
	public void lostFocus() {
 	}
	
	/**
	 * loginListener Processes the username and password entered on the login screen.
	 */
	protected EventCallback loginListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			if (event.getTypeInt() == Event.ONCLICK || event.getKeyCode() == KeyCodes.KEY_ENTER) {
				final String loginName = ((TextBox)PageAssembler.elementToWidget("loginName", PageAssembler.TEXT)).getText();
				if (loginName.equalsIgnoreCase("guest")) {
					final Element oldErrorDialog = (Element)Document.get().getElementById("errorDialog");
					if (oldErrorDialog != null) oldErrorDialog.removeFromParent();
					final HTML errorDialog = new HTML(templates().getErrorWidget().getText());
					RootPanel.get("errorContainer").add(errorDialog);
					enableLogin0(true);
					((Label)PageAssembler.elementToWidget("errorMessage", PageAssembler.LABEL)).setText(LOGIN_BAD_LOGIN);
					PageAssembler.attachHandler("errorClose", Event.ONMOUSEUP, new EventCallback() {
																					@Override
																					public void onEvent(Event event) {
																							errorDialog.removeFromParent();
																					}
																				});
				}
				else {
					enableLogin0(false);
					ESBApi.login(loginName,
									  ((PasswordTextBox)PageAssembler.elementToWidget("loginPassword", PageAssembler.PASSWORD)).getText(),
									  new ESBCallback<ESBPacket>() {
										@Override
										public void onSuccess(ESBPacket result) {
											if (result.getPayloadString()==null)
												onFailure(new Throwable(LOGIN_BAD_LOGIN));
											else
											{
												ESBApi.sessionId = result.getPayloadString();
												ESBApi.username = loginName;
												PageAssembler.setTemplate(templates().getHeader().getText(),
																	templates().getFooter().getText(),
																		  "contentPane");
												prepTemplateHooks0();
												Constants.loginCheck.scheduleRepeating(1800000);
												StatusWindowHandler.initialize();
												String tempDetailId = Russel.getDetailId();
												if (tempDetailId == null)
													dispatcher().loadHomeScreen();
												else
													dispatcher().loadDetailScreen(tempDetailId);
											}
										}
			
										@Override
										public void onFailure(Throwable caught) {
											final Element oldErrorDialog = (Element)Document.get().getElementById("errorDialog");
											if (oldErrorDialog != null) oldErrorDialog.removeFromParent();

											final HTML errorDialog = new HTML(templates().getErrorWidget().getText());
											RootPanel.get("errorContainer").add(errorDialog);
											enableLogin0(true);
											if (caught.getMessage().indexOf("502")!=-1||caught.getMessage().indexOf("503")!=-1||caught.getMessage().indexOf("System Error")!=-1||
												caught.getMessage().indexOf("404")!=-1)
												((Label)PageAssembler.elementToWidget("errorMessage", PageAssembler.LABEL)).setText(SERVER_UNAVAILABLE);
											else
												((Label)PageAssembler.elementToWidget("errorMessage", PageAssembler.LABEL)).setText(LOGIN_BAD_LOGIN);
											PageAssembler.attachHandler("errorClose", Event.ONMOUSEUP, new EventCallback() {
																											@Override
																											public void onEvent(Event event) {
																													errorDialog.removeFromParent();
																											}
																										});
										}
									  }
					);
				}

			}
		}
	};

	/**
	 * enableLogin Sets the state of the login form.
	 * @param s Enables the login form if set to true, disables it if set to false.
	 */
	private void enableLogin0(boolean s) {
		((TextBox)PageAssembler.elementToWidget("loginName", PageAssembler.TEXT)).setEnabled(s);
		((PasswordTextBox)PageAssembler.elementToWidget("loginPassword",  PageAssembler.PASSWORD)).setEnabled(s);
		((Anchor)PageAssembler.elementToWidget("loginButton",  PageAssembler.A)).setEnabled(s);
	}

	/**
	 * prepTemplateHooks Performs setup for LoginScreen template event handlers  
	 */
	private void prepTemplateHooks0() {
		PageAssembler.attachHandler("r-menuLogout", Event.ONCLICK, new EventCallback() {
																       @Override
																       public void onEvent(Event event) {
																    	   ESBApi.logout(new ESBCallback<ESBPacket>() {
																								@Override
																								public void onSuccess(ESBPacket result) {
																									Constants.loginCheck.cancel();
																									Constants.dispatcher.clearHistory();
																									dispatcher().loadLoginScreen();
																								}
																								
																								@Override
																								public void onFailure(Throwable caught) {
																									Constants.loginCheck.cancel();
																									Constants.dispatcher.clearHistory();
																									dispatcher().loadLoginScreen();
																								}
																							  });
																		}
																	});
		PageAssembler.attachHandler("r-menuWorkspace", Event.ONCLICK, new EventCallback() {
																		@Override
																		public void onEvent(Event event) {
																			dispatcher().loadHomeScreen();
																		}
																	 });
		
		PageAssembler.attachHandler("r-menuCollections", Event.ONCLICK, new EventCallback() {
																		@Override
																		public void onEvent(Event event) {
																			dispatcher().loadFeatureScreen(FeatureScreen.COLLECTIONS_TYPE);
																		}
																	 });

		PageAssembler.attachHandler("r-menuProjects", Event.ONCLICK, new EventCallback() {
																		@Override
																		public void onEvent(Event event) {
																			dispatcher().loadFeatureScreen(FeatureScreen.PROJECTS_TYPE);
																		}
																	 });
	}

	/**
	 * display Renders the Login screen.
	 */
	public void display()
	{
		PageAssembler.setTemplate("", "", "contentPane");
		PageAssembler.ready(new HTML(templates().getLoginWidget().getText()));
		PageAssembler.buildContents();
		
		// Reset the ESB URL now that everything should have loaded...
		ESBApi.esbURL = CommunicationHub.esbURL;
		
		PageAssembler.attachHandler("loginButton", Event.ONCLICK, loginListener);
		PageAssembler.attachHandler("loginPassword", Event.ONKEYUP, loginListener);
//		PageAssembler.attachHandler("ForgotButton", Event.ONCLICK, Russel.nonFunctional);
//		PageAssembler.attachHandler("RequestButton", Event.ONCLICK, Russel.nonFunctional);
//		PageAssembler.attachHandler("rememberMe", Event.ONCLICK, Russel.nonFunctional);
		
		((TextBox)PageAssembler.elementToWidget("loginName", PageAssembler.TEXT)).setFocus(true);
	}
	
}	

