package com.eduworks.russel.ui.client.pagebuilder;

import com.eduworks.gwt.client.component.AppSettings;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.eduworks.gwt.client.pagebuilder.modal.ModalDispatch;
import com.eduworks.gwt.client.pagebuilder.overlay.OverlayDispatch;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenTemplate;
import com.eduworks.russel.ui.client.Constants;
import com.eduworks.russel.ui.client.Russel;
import com.eduworks.russel.ui.client.ScreenDispatch;
import com.eduworks.russel.ui.client.net.RusselApi;
import com.eduworks.russel.ui.client.pagebuilder.screen.FeatureScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.HomeScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.LoginScreen;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

public abstract class RusselScreen extends ScreenTemplate {	      
	

	public static final String HOME_SCREEN		= "home";
	public static final String UPLOAD_SCREEN	= "upload";
	public static final String SEARCH_SCREEN	= "search";
	public static final String PROJECTS_SCREEN	= "projects";
	public static final String COLLECTION_SCREEN= "collections";
	public static final String EPSS_SCREEN		= "epss";
	public static final String USERS_SCREEN		= "users";
	public static final String GROUPS_SCREEN	= "groups";
	public static final String DETAILS_SCREEN	= "detail";
	public static String currentScreen			= HOME_SCREEN;
	public static String pendingScreen2Load		= HOME_SCREEN;  // Only used if a guest user must login before proceeding to a screen
	
	public ScreenDispatch getDispatcher() {return (ScreenDispatch) Russel.dispatcher;}
	public HtmlTemplates getTemplates() {return (HtmlTemplates) AppSettings.templates;}		
	public OverlayDispatch getOverlayDispatcher() {return null;}
	public ModalDispatch getModalDispatcher() {return null;}
	
	public void display(){
		PageAssembler.setTemplate(Russel.htmlTemplates.getHeader().getText(), Russel.htmlTemplates.getFooter().getText(), "contentPane");
		
	}
	
	public void configureHeaderLinks() {
		
		// Configure headers and tools for an unauthenticated user
		if (Constants.getSessionId() == null || Constants.getSessionId() == "" || Constants.getSessionId().equals(Constants.DEFAULT_SESSION_ID)) {
			showButton("showUser", true);
			showButton("showLogin", true);
			showButton("showHelp", true);
			showButton("showLogout", false);
			showButton("showFeedback", true);	
			((Label)PageAssembler.elementToWidget("r-menuUserName", PageAssembler.LABEL)).setText(Constants.DEFAULT_USER_NAME);	
			showButton("showWorkspace", true);
			activateButton("showWorkspace", true);
			showButton("showCollections", false);
			activateButton("showCollections", false);
			showButton("showProjects", false);
			activateButton("showProjects", false);
		}
		
		// Configure headers and tools for an authenticated user
		else {
			showButton("showUser", true);
			showButton("showLogin", false);
			showButton("showHelp", true);
			showButton("showLogout", true);
			showButton("showFeedback", true);
			((Label)PageAssembler.elementToWidget("r-menuUserName", PageAssembler.LABEL)).setText(RusselApi.username);	
			showButton("showWorkspace", true);
			activateButton("showWorkspace", true);
			showButton("showCollections", true);
			activateButton("showCollections", false);
			showButton("showProjects", true);
			activateButton("showProjects", false);
		}

		
		PageAssembler.attachHandler("r-menuWorkspace", Event.ONCLICK, new EventCallback() {
			@Override
			public void onEvent(Event event) {
				Russel.screen.loadScreen(new HomeScreen(), true);
			}
		 });
		
		PageAssembler.attachHandler("r-menuProjects", Event.ONCLICK, new EventCallback() {
			@Override
			public void onEvent(Event event) {
				Russel.screen.loadScreen(new FeatureScreen(FeatureScreen.PROJECTS_TYPE), true);
			}
		 });

		PageAssembler.attachHandler("r-menuCollections", Event.ONCLICK, new EventCallback() {
					@Override
					public void onEvent(Event event) {
						Russel.screen.loadScreen(new FeatureScreen(FeatureScreen.COLLECTIONS_TYPE), true);
					}
				 });
		
		PageAssembler.attachHandler("r-menuHelp", Event.ONCLICK, new EventCallback() {
					@Override
					public void onEvent(Event event) {
						String help = PageAssembler.getHelp();
						if (help != null && help != "")
							Window.open(PageAssembler.getHelp(), "_blank", null);
						else 
							Window.alert("Help has not been configured for this installation of RUSSEL.");
					}
				 });
		
		PageAssembler.attachHandler("r-menuLogout", Event.ONCLICK, new EventCallback() {
			       @Override
			       public void onEvent(Event event) {
			    	   RusselApi.logout(new ESBCallback<ESBPacket>() {
											@Override
											public void onSuccess(ESBPacket result) {
												loggingOut();
											}
											
											@Override
											public void onFailure(Throwable caught) {
												loggingOut();
											}
										  });
			       	}
				});
		
		PageAssembler.attachHandler("r-menuLogin", Event.ONCLICK, new EventCallback() {
		       @Override
		       public void onEvent(Event event) {
		    	   pendingScreen2Load = currentScreen;
		    	   Russel.screen.loadScreen(new LoginScreen(), true);
		       	}
			});
		
	}

	public void loggingOut() {
		Constants.loggedInCheck.cancel();
		Constants.invalidateCurrentUser();
		Russel.screen.clearHistory();
		getDispatcher().loadHomeScreen();
	}
	
	public void showButton(String buttonName, boolean shown) {
		if (DOM.getElementById(buttonName) != null) {
			if (shown) {
				DOM.getElementById(buttonName).removeClassName("hidden");
			}
			else {
				DOM.getElementById(buttonName).addClassName("hidden");
			}	
		}
	}
	
	public void activateButton(String buttonName, boolean activate) {
		if (DOM.getElementById(buttonName) != null) {
			if (activate) {
				DOM.getElementById(buttonName).addClassName("active");
			}
			else {
				DOM.getElementById(buttonName).removeClassName("active");
			}	
		}
	}
	
	public void enableButton(String buttonName, boolean enabled) {
		if (DOM.getElementById(buttonName) != null) {
			if (enabled) {
				DOM.getElementById(buttonName).removeAttribute("disabled");
				DOM.getElementById(buttonName).addClassName("secondary");
			}
			else {
				DOM.getElementById(buttonName).setAttribute("disabled", null);
				DOM.getElementById(buttonName).removeClassName("secondary");
			}	
		}
	}

		
}
