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

package com.eduworks.russel.ui.client.epss;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.vectomatic.file.Blob;

import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.russel.ui.client.Constants;
import com.eduworks.russel.ui.client.model.RUSSELFileRecord;
import com.eduworks.russel.ui.client.model.ProjectRecord;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;

/**
 * EPSSPackBuilder
 * Defines globals, methods for exporting EPSS projects
 * 
 * @author Eduworks Corporation
 */
public class EPSSPackBuilder extends Constants {
	private Vector<String> mediaList = new Vector<String>();
	private ProjectRecord pfm;
	private String missingFiles;
	private Vector<String> filenameAndPath = new Vector<String>();
	//public JavaScriptObject zipWriter;
	//public Blob scormZip;
	
	/**
	 * EPSSPackBuilder Loads an EPSS project file into the constructor.
	 * @param pfm 
	 */
	public EPSSPackBuilder (ProjectRecord pfm) {
		this.pfm = pfm;
		missingFiles = "";
		buildFileList();
	}
	
	/**
	 * buildFileList Creates a list containing the files required for SCORM export.
	 */
	private void buildFileList() {
		filenameAndPath.add("adlcp_v1p3.xsd");
		filenameAndPath.add("adlnav_v1p3.xsd");
		filenameAndPath.add("adlseq_v1p3.xsd");
		filenameAndPath.add("/common/anyElement.xsd");
		filenameAndPath.add("/common/datatypes.xsd");
		filenameAndPath.add("/common/elementNames.xsd");
		filenameAndPath.add("/common/elementTypes.xsd");
		filenameAndPath.add("/common/rootElement.xsd");
		filenameAndPath.add("/common/vocabTypes.xsd");
		filenameAndPath.add("/common/vocabValues.xsd");
		filenameAndPath.add("datatypes.dtd");
		filenameAndPath.add("/extend/custom.xml");
		filenameAndPath.add("/extend/strict.xml");
		filenameAndPath.add("imsmanifest.xml");
		filenameAndPath.add("imsss_v1p0.xsd");
		filenameAndPath.add("imsss_v1p0auxresource.xsd");
		filenameAndPath.add("imsss_v1p0control.xsd");
		filenameAndPath.add("imsss_v1p0delivery.xsd");
		filenameAndPath.add("imsss_v1p0limit.xsd");
		filenameAndPath.add("imsss_v1p0objective.xsd");
		filenameAndPath.add("imsss_v1p0random.xsd");
		filenameAndPath.add("imsss_v1p0rollup.xsd");
		filenameAndPath.add("imsss_v1p0seqrule.xsd");
		filenameAndPath.add("imsss_v1p0util.xsd");
		filenameAndPath.add("initPage.html");
		filenameAndPath.add("lom.xsd");
		filenameAndPath.add("lomCustom.xsd");
		filenameAndPath.add("lomLoose.xsd");
		filenameAndPath.add("lomStrict.xsd");
		filenameAndPath.add("/unique/loose.xsd");
		filenameAndPath.add("/unique/strict.xsd");
		filenameAndPath.add("/vocab/adlmd_vocabv1p0.xsd");
		filenameAndPath.add("/vocab/custom.xsd");
		filenameAndPath.add("/vocab/loose.xsd");
		filenameAndPath.add("/vocab/strict.xsd");
		filenameAndPath.add("xml.xsd");
		filenameAndPath.add("XMLScheme.dtd");
	}

	/**
	 * buildPackIE Used to export the project as a SCORM package when in IE.
	 * @return AlfrescoPacket
	 */
	public ESBPacket buildPackIE() {
		JSONArray storage = new JSONArray();
		
		buildMediaList();
		
		for (int filenameIndex=0;filenameIndex<filenameAndPath.size();filenameIndex++) {
			ESBPacket zipPack = new ESBPacket();
			zipPack.put("filename", filenameAndPath.get(filenameIndex));
			zipPack.put("data", filenameToContents(filenameAndPath.get(filenameIndex)));
			storage.set(filenameIndex, zipPack);
		}
		
		int mediaIndex = storage.size();
		for (int filenameIndex=0;filenameIndex<mediaList.size();filenameIndex++) {
			ESBPacket zipPack = new ESBPacket();
			String[] mediaPair = mediaList.get(filenameIndex).split(","); 
			zipPack.put("id", mediaPair[0]);
			zipPack.put("location", "/media/");
			storage.set(mediaIndex, zipPack);
			mediaIndex++;
		}
		
		ESBPacket projectPack = new ESBPacket();
		projectPack.put("projectName", pfm.projectTitle.replaceAll(" ", "_") + "Package.zip");
		projectPack.put("projectZipName", pfm.projectTitle.replaceAll(" ", "_") + ".zip");
		projectPack.put("projectNodeId", pfm.getGuid());
		
		
		ESBPacket outgoing = new ESBPacket();
		outgoing.put("mediaToZip", storage);
		outgoing.put("projectToZip", projectPack);
		
		return outgoing;
	}
	
	/**
	 * missingFile Maintains a list of files used in the project but not found in the RUSSEL repository. 
	 * The list is displayed to the user when the export is created. It is used when an EPSS project file is 
	 * imported and nodes used in the design can't be located in the current repository.
	 * @param filename
	 */
	private void missingFile(String filename) {
		missingFiles = missingFiles + "  - " + filename + "<br/>";     
	}
	
	/**
	 * buildMediaList Produces a list of media assets that are contained in the project.
	 */
	private void buildMediaList() {
		Set<String> keys = pfm.projectSectionAssets.getSectionList();
		for (Iterator<String> keyPointer=keys.iterator();keyPointer.hasNext();) {
			String key = keyPointer.next();
			Vector<RUSSELFileRecord> assets = pfm.projectSectionAssets.getSectionAssets(key);
			for (int y=0;y<assets.size();y++)
				mediaList.add(assets.get(y).getGuid() + "," + assets.get(y).getFilename());
		}
	}
	
	/**
	 * filenameToContents Constructs the appropriate SCORM template based on the filename.
	 * @param filename
	 * @return string filedata
	 */
	private String filenameToContents(String filename) {
		String filedata = "";
		
		if (filename.equals("adlcp_v1p3.xsd"))
			filedata = SCORMTemplates.INSTANCE.getAdlcp_v1p3().getText();
		else if (filename.equals("adlnav_v1p3.xsd"))
			filedata = SCORMTemplates.INSTANCE.getAdlnav_v1p3().getText();
		else if (filename.equals("adlseq_v1p3.xsd"))
			filedata = SCORMTemplates.INSTANCE.getAdlseq_v1p3().getText();
		else if (filename.equals("/common/anyElement.xsd"))
			filedata = SCORMTemplates.INSTANCE.getCommonAnyElement().getText();
		else if (filename.equals("/common/datatypes.xsd"))
			filedata = SCORMTemplates.INSTANCE.getCommonDataTypes().getText();
		else if (filename.equals("/common/elementNames.xsd"))
			filedata = SCORMTemplates.INSTANCE.getCommonElementNames().getText();
		else if (filename.equals("/common/elementTypes.xsd"))
			filedata = SCORMTemplates.INSTANCE.getCommonElementTypes().getText();
		else if (filename.equals("/common/rootElement.xsd"))
			filedata = SCORMTemplates.INSTANCE.getCommonRootElement().getText();
		else if (filename.equals("/common/vocabTypes.xsd"))
			filedata = SCORMTemplates.INSTANCE.getCommonVocabTypes().getText();
		else if (filename.equals("/common/vocabValues.xsd"))
			filedata = SCORMTemplates.INSTANCE.getCommonVocabValues().getText();
		else if (filename.equals("datatypes.dtd"))
			filedata = SCORMTemplates.INSTANCE.getDatatypes().getText();
		else if (filename.equals("/extend/custom.xml"))
			filedata = SCORMTemplates.INSTANCE.getExtendCustom().getText();
		else if (filename.equals("/extend/strict.xml"))
			filedata = SCORMTemplates.INSTANCE.getExtendStrict().getText();
		else if (filename.equals("imsmanifest.xml"))
			filedata = SCORMTemplates.INSTANCE.getImsmanifest().getText();
		else if (filename.equals("imsss_v1p0.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0().getText();
		else if (filename.equals("imsss_v1p0auxresource.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0auxresource().getText();
		else if (filename.equals("imsss_v1p0control.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0control().getText();
		else if (filename.equals("imsss_v1p0delivery.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0delivery().getText();
		else if (filename.equals("imsss_v1p0limit.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0limit().getText();
		else if (filename.equals("imsss_v1p0objective.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0objective().getText();
		else if (filename.equals("imsss_v1p0random.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0random().getText();
		else if (filename.equals("imsss_v1p0rollup.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0rollup().getText();
		else if (filename.equals("imsss_v1p0seqrule.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0seqrule().getText();
		else if (filename.equals("imsss_v1p0util.xsd"))
			filedata = SCORMTemplates.INSTANCE.getImsss_v1p0util().getText();
		else if (filename.equals("initPage.html"))
			filedata = SCORMTemplates.INSTANCE.getInitPage().getText();
		else if (filename.equals("lom.xsd"))
			filedata = SCORMTemplates.INSTANCE.getLom().getText();
		else if (filename.equals("lomCustom.xsd"))
			filedata = SCORMTemplates.INSTANCE.getLomCustom().getText();
		else if (filename.equals("lomLoose.xsd"))
			filedata = SCORMTemplates.INSTANCE.getLomLoose().getText();
		else if (filename.equals("lomStrict.xsd"))
			filedata = SCORMTemplates.INSTANCE.getLomStrict().getText();
		else if (filename.equals("/unique/loose.xsd"))
			filedata = SCORMTemplates.INSTANCE.getUniqueLoose().getText();
		else if (filename.equals("/unique/strict.xsd"))
			filedata = SCORMTemplates.INSTANCE.getUniqueStrict().getText();
		else if (filename.equals("/vocab/adlmd_vocabv1p0.xsd"))
			filedata = SCORMTemplates.INSTANCE.getVocabAdlmd_Vocabv1p0().getText();
		else if (filename.equals("/vocab/custom.xsd"))
			filedata = SCORMTemplates.INSTANCE.getVocabCustom().getText();
		else if (filename.equals("/vocab/loose.xsd"))
			filedata = SCORMTemplates.INSTANCE.getVocabLoose().getText();
		else if (filename.equals("/vocab/strict.xsd"))
			filedata = SCORMTemplates.INSTANCE.getVocabLoose().getText();
		else if (filename.equals("xml.xsd"))
			filedata = SCORMTemplates.INSTANCE.getXml().getText();
		else if (filename.equals("XMLScheme.dtd"))
			filedata = SCORMTemplates.INSTANCE.getXMLSchema().getText();
		
		return filedata;
	}
}