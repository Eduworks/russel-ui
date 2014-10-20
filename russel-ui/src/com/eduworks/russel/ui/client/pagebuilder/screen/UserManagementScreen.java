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

import java.util.Iterator;
import java.util.Vector;

import com.eduworks.gwt.client.model.StatusRecord;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.russel.ui.client.handler.StatusWindowHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * UtilityScreen class
 * Extends ScreenTemplate
 * Defines methods and handlers for the Utility screen.
 * 
 * @author Eduworks Corporation
 */
public class UserManagementScreen extends Screen {
	
	private static String CREATE = "create";
	private static String PERMISSION = "permission";
	private static String RESET = "reset";
	private String selectedUsername = null;
	private String selectedIdPrefix = null;
	
	/**
	 * lostFocus In place to handle any processing requirements required when this screen loses focus.
	 * Called by ScreenDispatch for all RUSSEL screens.
	 */
	public void lostFocus() {
		
	}
	
	/**
	 * display Renders the Utility screen using appropriate templates and sets up handlers
	 */
	public void display() {
		PageAssembler.ready(new HTML(templates().getUserManagementPanel().getText()));	
		PageAssembler.buildContents();
		
		DOM.getElementById("r-menuCollections").getParentElement().removeClassName("active");
		DOM.getElementById("r-menuProjects").getParentElement().removeClassName("active");
		DOM.getElementById("r-menuWorkspace").getParentElement().removeClassName("active");
		
		ESBApi.getUserListing(new ESBCallback<ESBPacket>() {
			@Override
			public void onSuccess(ESBPacket esbPacket) {
				populateUserList(esbPacket.getObject("obj").getObject("users"));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
		PageAssembler.attachHandler("userCreate", 
									Event.ONCLICK, 
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											openPopup(CREATE);
											((TextBox)PageAssembler.elementToWidget("newUsername", PageAssembler.TEXT)).setText("");
											((PasswordTextBox)PageAssembler.elementToWidget("newPassword", PageAssembler.PASSWORD)).setText("");
											((PasswordTextBox)PageAssembler.elementToWidget("newPasswordConfirmation", PageAssembler.PASSWORD)).setText("");
											
											PageAssembler.attachHandler("modalNewUserCreate",
																	 	Event.ONCLICK,
																	 	new EventCallback() {
																			@Override
																			public void onEvent(Event event) {
																				String username = ((TextBox)PageAssembler.elementToWidget("newUsername", PageAssembler.TEXT)).getText().trim();
																				if (matchingPasswords("newPassword", "newPasswordConfirmation")&&username!="") { 
																					String password = ((PasswordTextBox)PageAssembler.elementToWidget("newPassword", PageAssembler.PASSWORD)).getText(); 
																					createUser(username, password);
																				}
																			}
																		});
											
											PageAssembler.attachHandler("modalNewUserCancel",
																	 	Event.ONCLICK,
																	 	new EventCallback() {
																			@Override
																			public void onEvent(Event event) {
																				PageAssembler.closePopup("createUserModal");
																			}
																		});
										}
									});
		

		PageAssembler.attachHandler("userPasswordReset", 
									Event.ONCLICK, 
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											if (selectedIdPrefix!=null) {
												openPopup(RESET);
												((PasswordTextBox)PageAssembler.elementToWidget("resetPassword", PageAssembler.PASSWORD)).setText("");
												((PasswordTextBox)PageAssembler.elementToWidget("resetPasswordConfirmation", PageAssembler.PASSWORD)).setText("");
												
												PageAssembler.attachHandler("modalResetPasswordAccept",
																		 	Event.ONCLICK,
																		 	new EventCallback() {
																				@Override
																				public void onEvent(Event event) {
																					if (matchingPasswords("resetPassword", "resetPasswordConfirmation")) {
																						String password = ((PasswordTextBox)PageAssembler.elementToWidget("resetPassword", PageAssembler.PASSWORD)).getText(); 
																						resetPassword(selectedUsername, password);
																					}
																				}
																			});
							
												PageAssembler.attachHandler("modalResetPasswordCancel",
																		 	Event.ONCLICK,
																		 	new EventCallback() {
																				@Override
																				public void onEvent(Event event) {
																					PageAssembler.closePopup("resetPasswordModal");
																				}
																			});
											}
										}
									});
		

		PageAssembler.attachHandler("userPermissions", 
									Event.ONCLICK, 
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											if (selectedIdPrefix!=null) {
												openPopup(PERMISSION);
												
												fillPermissions();
												
												PageAssembler.attachHandler("modalPermissionAccept",
																		 	Event.ONCLICK,
																		 	new EventCallback() {
																				@Override
																				public void onEvent(Event event) {
																					toggleUserAdmin(selectedIdPrefix, 
																									selectedUsername, 
																									((SimpleCheckBox)PageAssembler.elementToWidget("adminPermissionToggle", PageAssembler.CHECK_BOX)).getValue());
																				}
																			});
		
												PageAssembler.attachHandler("modalPermissionCancel",
																		 	Event.ONCLICK,
																		 	new EventCallback() {
																				@Override
																				public void onEvent(Event event) {
																					PageAssembler.closePopup("setPermissionsModal");
																				}
																			});
											}
										}

									});
		

		PageAssembler.attachHandler("userDelete", 
									Event.ONCLICK, 
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											if (selectedIdPrefix!=null)
												deleteUser(selectedIdPrefix, selectedUsername);
										}
									});
	}


	private void fillPermissions() {
		ESBApi.getUser(selectedUsername,
					   new ESBCallback<ESBPacket>() {
					   	@Override
					   	public void onFailure(Throwable caught) {
					   		
					   	}
					   	
					   	@Override
					   	public void onSuccess(ESBPacket esbPacket) {
					   		JSONObject user = esbPacket.getObject("obj");
					   		boolean isAdmin = false;
					   		if (user.containsKey("permissions")) {
					   			if (isAdmin(selectedUsername, user.get("permissions").isArray()))
					   				isAdmin = true;
					   		}
					   		((SimpleCheckBox)PageAssembler.elementToWidget("adminPermissionToggle", PageAssembler.CHECK_BOX)).setValue(isAdmin);
					   	}
					   });
	}
	
	private boolean matchingPasswords(String element1, String element2) {
		String password1 = ((PasswordTextBox)PageAssembler.elementToWidget(element1, PageAssembler.PASSWORD)).getText();
		String password2 = ((PasswordTextBox)PageAssembler.elementToWidget(element2, PageAssembler.PASSWORD)).getText();
		return password1.equals(password2);
	}
	
	private void populateUserList(JSONObject users) {
		for (Iterator<String> userCursor = users.keySet().iterator(); userCursor.hasNext();) {
			String username = userCursor.next();
			JSONObject user = (JSONObject) users.get(username);
			makeUserRow(username, user.containsKey("permissions")?user.get("permissions").isArray():null);
		}		
	}
	
	private boolean isAdmin(String username, JSONArray permissions) {
		if (username=="admin")
			return true;
		if (permissions!=null) {
			for (int permissionIndex = 0; permissionIndex < permissions.size(); permissionIndex++) {
				if (permissions.get(permissionIndex).isString().stringValue().equalsIgnoreCase("admin"))
					return true;
			}
		}
		return false;
	}
	
	private void makeUserRow(final String username, JSONArray permissions) {
		Vector<String> ids = PageAssembler.inject("userList", "x", new HTML(templates().getUserManagementPanelWidget().getText()), true);
		final String idPrefix = ids.get(0).substring(0,ids.get(0).indexOf("-"));
		((Label)PageAssembler.elementToWidget(idPrefix + "-username", PageAssembler.LABEL)).setText(username);
		String roleText = "";
		if (isAdmin(username, permissions))
			roleText = "Administrator";
		((Label)PageAssembler.elementToWidget(idPrefix + "-role", PageAssembler.LABEL)).setText(roleText);
		PageAssembler.attachHandler(idPrefix + "-userSelected",
									Event.ONCLICK,
									new EventCallback() {
										@Override
										public void onEvent(Event event) {
											selectedIdPrefix = idPrefix;
											selectedUsername = username;
										}
									});
		
	}
	
	private void openPopup(String type) {
		if (type.equals(RESET)) {
			PageAssembler.openPopup("resetPasswordModal");
		} else if (type.equals(PERMISSION)) {
			PageAssembler.openPopup("setPermissionsModal");
		} else if (type.equals(CREATE)) {
			PageAssembler.openPopup("createUserModal");
		}
	}

	private void toggleUserAdmin(final String idPrefix, final String username, Boolean makeAdmin) {
		final StatusRecord sr = StatusWindowHandler.createMessage(StatusWindowHandler.getSetUserPermissionsBusy(username), StatusRecord.STATUS_BUSY);
		if (makeAdmin) {
			ESBApi.addUserPermissions(username,
									  "admin", 
									  new ESBCallback<ESBPacket>() {
										@Override
										public void onSuccess(ESBPacket esbPacket) {
											if (!Boolean.parseBoolean(esbPacket.getString("obj"))) {
												sr.setMessage(StatusWindowHandler.getSetUserPermissionsError(username));
												sr.setState(StatusRecord.STATUS_ERROR);
												StatusWindowHandler.alterMessage(sr);
											} else {
												((Label)PageAssembler.elementToWidget(idPrefix + "-role", PageAssembler.LABEL)).setText("Administrator");
												sr.setMessage(StatusWindowHandler.getSetUserPermissionsDone(username));
												sr.setState(StatusRecord.STATUS_DONE);
												StatusWindowHandler.alterMessage(sr);
												PageAssembler.closePopup("setPermissionsModal");
											}
										}
										
										@Override
										public void onFailure(Throwable caught) {
											sr.setMessage(StatusWindowHandler.getSetUserPermissionsError(username));
											sr.setState(StatusRecord.STATUS_ERROR);
											StatusWindowHandler.alterMessage(sr);
										}
								       });
		} else {
			ESBApi.removeUserPermissions(username,
										 "admin", 
										 new ESBCallback<ESBPacket>() {
										 	@Override
										 	public void onSuccess(ESBPacket esbPacket) {
										 		if (!Boolean.parseBoolean(esbPacket.getString("obj"))) {
										 			sr.setMessage(StatusWindowHandler.getSetUserPermissionsError(username));
													sr.setState(StatusRecord.STATUS_ERROR);
													StatusWindowHandler.alterMessage(sr);
										 		} else {
										 			((Label)PageAssembler.elementToWidget(idPrefix + "-role", PageAssembler.LABEL)).setText("");
										 			sr.setMessage(StatusWindowHandler.getSetUserPermissionsDone(username));
										 			sr.setState(StatusRecord.STATUS_DONE);
										 			StatusWindowHandler.alterMessage(sr);
										 			PageAssembler.closePopup("setPermissionsModal");
										 		}
										 	}
										 	
										 	@Override
										 	public void onFailure(Throwable caught) {
										 		sr.setMessage(StatusWindowHandler.getSetUserPermissionsError(username));
												sr.setState(StatusRecord.STATUS_ERROR);
												StatusWindowHandler.alterMessage(sr);
										 	}
										 });
		}
	}
	
	private void resetPassword(final String username, String password) {
		final StatusRecord sr = StatusWindowHandler.createMessage(StatusWindowHandler.getResetUserPasswordBusy(username), StatusRecord.STATUS_BUSY);
		ESBApi.resetUserPassword(username, 
								 password, 
								 new ESBCallback<ESBPacket>() {
									@Override
									public void onSuccess(ESBPacket esbPacket) {
										if (!Boolean.parseBoolean(esbPacket.getString("obj"))) {
											sr.setMessage(StatusWindowHandler.getResetUserPasswordError(username));
											sr.setState(StatusRecord.STATUS_ERROR);
											StatusWindowHandler.alterMessage(sr);
										} else {
											sr.setMessage(StatusWindowHandler.getResetUserPasswordDone(username));
											sr.setState(StatusRecord.STATUS_DONE);
											StatusWindowHandler.alterMessage(sr);
											PageAssembler.closePopup("resetPasswordModal");
										}
									}
									
									@Override
									public void onFailure(Throwable caught) {
										sr.setMessage(StatusWindowHandler.getResetUserPasswordError(username));
										sr.setState(StatusRecord.STATUS_ERROR);
										StatusWindowHandler.alterMessage(sr);
									}
								 });
	}
	
	private void createUser(final String username, String password) {
		final StatusRecord sr = StatusWindowHandler.createMessage(StatusWindowHandler.getCreateUserBusy(username), StatusRecord.STATUS_BUSY);
		ESBApi.createUser(username, 
					      password, 
					      new ESBCallback<ESBPacket>() {
						  	@Override
						  	public void onFailure(Throwable caught) {
								sr.setMessage(StatusWindowHandler.getCreateUserError(username));
								sr.setState(StatusRecord.STATUS_ERROR);
								StatusWindowHandler.alterMessage(sr);
						  	}
						  	
						  	@Override
						  	public void onSuccess(ESBPacket esbPacket) {
						  		if (!Boolean.parseBoolean(esbPacket.getString("obj"))) {
						  			sr.setMessage(StatusWindowHandler.getCreateUserError(username));
									sr.setState(StatusRecord.STATUS_ERROR);
									StatusWindowHandler.alterMessage(sr);
						  		} else {
						  			makeUserRow(username, new JSONArray());
						  			sr.setMessage(StatusWindowHandler.getCreateUserDone(username));
									sr.setState(StatusRecord.STATUS_DONE);
									StatusWindowHandler.alterMessage(sr);
									PageAssembler.closePopup("createUserModal");
						  		}
						  	}
						  });
	}
	
	private void deleteUser(final String idPrefix, final String username) {
		final StatusRecord sr = StatusWindowHandler.createMessage(StatusWindowHandler.getDeleteUserBusy(username), StatusRecord.STATUS_BUSY);
		ESBApi.deleteUser(username,
						  new ESBCallback<ESBPacket>() {
						  	@Override
						  	public void onFailure(Throwable caught) {
								sr.setMessage(StatusWindowHandler.getDeleteUserError(username));
								sr.setState(StatusRecord.STATUS_ERROR);
								StatusWindowHandler.alterMessage(sr);
						  	}
						  	
						  	@Override
						  	public void onSuccess(ESBPacket esbPacket) {
						  		if (!Boolean.parseBoolean(esbPacket.getString("obj"))) {
					  				sr.setMessage(StatusWindowHandler.getDeleteUserError(username));
									sr.setState(StatusRecord.STATUS_ERROR);
									StatusWindowHandler.alterMessage(sr);
					  			} else {
					  				PageAssembler.removeElement(idPrefix + "-user");
					  				sr.setMessage(StatusWindowHandler.getDeleteUserDone(username));
									sr.setState(StatusRecord.STATUS_DONE);
									StatusWindowHandler.alterMessage(sr);
									selectedIdPrefix = null;
									selectedUsername = null;
					  			}
						  	}
		                  });
	}
}