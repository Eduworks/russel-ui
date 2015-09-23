package com.eduworks.russel.ui.client;

import java.util.Vector;

import com.eduworks.russel.ui.client.handler.TileHandler;
import com.eduworks.russel.ui.client.model.ProjectRecord;
import com.eduworks.russel.ui.client.model.RUSSELFileRecord;
import com.eduworks.russel.ui.client.pagebuilder.screen.DetailScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.EPSSScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.EditScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.FeatureScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.GroupScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.HomeScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.LoginScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.PermissionScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.SearchScreen;
import com.eduworks.russel.ui.client.pagebuilder.screen.UserScreen;


public class ScreenDispatch extends com.eduworks.gwt.client.pagebuilder.screen.ScreenDispatch
{

	public void loadLoginScreen() {
		LoginScreen s = new LoginScreen();
		loadScreen(s, true);
	}
	
	public void loadHomeScreen() {
		HomeScreen s = new HomeScreen();
		loadScreen(s, true);
	}
	
	public void loadDetailScreen(String id) {
		DetailScreen s = new DetailScreen(id);
		loadScreen(s, true);
	} 
	
	public void loadDetailScreen(RUSSELFileRecord r, boolean isFull) {
		DetailScreen s = new DetailScreen(r, isFull);
		loadScreen(s, true);
	} 
	
	public void loadDetailScreen(RUSSELFileRecord r, TileHandler sth) {
		DetailScreen s = new DetailScreen(r, sth);
		loadScreen(s, true);
	} 
	
	public void loadSearchScreen() {
		SearchScreen s = new SearchScreen();
		loadScreen(s, true);
	}
	
	public void loadSearchScreen(String searchType) {
		SearchScreen s = new SearchScreen(searchType);
		loadScreen(s, true);
	}   
	
	public void loadSearchScreen(String searchType, String setting) {
		SearchScreen s = new SearchScreen(searchType, setting);
		loadScreen(s, true);
	}
	
	public void loadEditScreen() {
		EditScreen s = new EditScreen();
		loadScreen(s, true);
	}
	
	public void loadEditScreen(Vector<RUSSELFileRecord> pendingEdits) {
		EditScreen s = new EditScreen(pendingEdits);
		loadScreen(s, true);
	}
	
	public void loadEPSSScreen(ProjectRecord incomingProject) {
		EPSSScreen s = new EPSSScreen(incomingProject);
		loadScreen(s, true);
	}
	
	public void loadFeatureScreen(String featureType) {
		FeatureScreen s = new FeatureScreen(featureType);
		loadScreen(s, true);
	}
	
	public void loadPermissionsScreen(String type, String source) {
		PermissionScreen s = new PermissionScreen(type, source);
		loadScreen(s, true);
	}
	
	public void loadGroupScreen() {
		GroupScreen s = new GroupScreen();
		loadScreen(s, true);
	}
	
	public void loadUserScreen() {
		UserScreen s = new UserScreen();
		loadScreen(s, true);
	}
	
	
}
