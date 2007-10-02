package com.tek42.perforce.parse;

import java.io.*;
import java.util.*;

import com.tek42.perforce.model.*;
import com.tek42.perforce.PerforceException;

/**
 *
 * @author mwille
 *
 */
public class WorkspaceBuilder extends AbstractFormBuilder<Workspace> {
	/* (non-Javadoc)
	 * @see com.tek42.perforce.parse.Builder#build(java.lang.StringBuilder)
	 */
	public Workspace buildForm(Map<String, String> fields) throws PerforceException {
		Workspace workspace = new Workspace();
		workspace.setName(getField("Client", fields));
		workspace.setOwner(getField("Owner", fields));
		workspace.setHost(getField("Host", fields));
		workspace.setRoot(getField("Root", fields));
		workspace.setOptions(getField("Options", fields));
		workspace.setSubmitOptions(getField("SubmitOptions", fields));
		workspace.setLineEnd(getField("LineEnd", fields));
		workspace.setAltRoots(getField("AltRoots", fields));
		workspace.setDescription(getField("Description", fields));
		workspace.setUpdate(getField("Update", fields));
		workspace.setAccess(getField("Access", fields));
		
		for(String line : getField("View", fields).split("\\n")) {
			workspace.addView(line);
		}
		
		return workspace;
	}

	/* (non-Javadoc)
	 * @see com.tek42.perforce.parse.Builder#getBuildCmd(java.lang.String)
	 */
	public String[] getBuildCmd(String id) {
		return new String[] { "p4", "workspace", "-o", id };
	}

	/* (non-Javadoc)
	 * @see com.tek42.perforce.parse.Builder#getSaveCmd()
	 */
	public String[] getSaveCmd() {
		return new String[] { "p4", "-s", "client", "-i" };
	}

	/* (non-Javadoc)
	 * @see com.tek42.perforce.parse.Builder#save(java.lang.Object)
	 */
	public void save(Workspace workspace, Writer out) throws PerforceException {
		try {
			out.write("Client: " + workspace.getName() + "\n");
			if(!workspace.getOwner().equals(""))
				out.write("Owner: " + workspace.getOwner() + "\n");
			if(!workspace.getHost().equals(""))
				out.write("Host: " + workspace.getHost() + "\n");
			out.write("Description: " + workspace.getDescription() + "\n");
			out.write("Root: " + workspace.getRoot() + "\n");
			
			if(!workspace.getAltRoots().equals(""))
				out.write("AltRoots: " + workspace.getAltRoots() + "\n");
			
			out.write("Options: " + workspace.getOptions() + "\n");
			
			if(!workspace.getSubmitOptions().equals(""))
				out.write("SubmitOptions: " + workspace.getSubmitOptions() + "\n");
			
			out.write("LineEnd: " + workspace.getLineEnd() + "\n");
			out.write("View:\n");
			out.write(" " + workspace.getViewsAsString() + "\n");
			
		} catch(IOException e) {
			throw new PerforceException("Failed to save workspace", e);
		}
	}

}