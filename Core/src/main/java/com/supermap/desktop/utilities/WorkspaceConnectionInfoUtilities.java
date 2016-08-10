package com.supermap.desktop.utilities;

import com.supermap.data.Enum;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.data.WorkspaceVersion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author XiaJT
 */
public class WorkspaceConnectionInfoUtilities {
	private WorkspaceConnectionInfoUtilities() {

	}

	public static Element toXml(WorkspaceConnectionInfo workspaceConnectionInfo, Document document) {

		Element rootNode = document.createElement("WorkspaceConnectionInfo");

		Element server = document.createElement("Server");
		server.appendChild(document.createTextNode(workspaceConnectionInfo.getServer()));
		rootNode.appendChild(server);

		Element type = document.createElement("Type");
		type.appendChild(document.createTextNode(String.valueOf(workspaceConnectionInfo.getType().value())));
		rootNode.appendChild(type);

		Element name = document.createElement("Name");
		name.appendChild(document.createTextNode(workspaceConnectionInfo.getName()));
		rootNode.appendChild(name);

		Element database = document.createElement("Database");
		database.appendChild(document.createTextNode(workspaceConnectionInfo.getDatabase()));
		rootNode.appendChild(database);

		Element User = document.createElement("User");
		User.appendChild(document.createTextNode(workspaceConnectionInfo.getUser()));
		rootNode.appendChild(User);

		Element version = document.createElement("Version");
		version.appendChild(document.createTextNode(String.valueOf(workspaceConnectionInfo.getVersion().value())));
		rootNode.appendChild(version);
		return rootNode;

	}

	public static WorkspaceConnectionInfo fromXml(Node workspaceConnectionInfoNode) {
		Node serverNode = XmlUtilities.getChildElementNodeByName(workspaceConnectionInfoNode, "Server");
		String server = serverNode.getChildNodes().item(0).getNodeValue();

		WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo(server);

		Node typeNode = XmlUtilities.getChildElementNodeByName(workspaceConnectionInfoNode, "Type");
		if (typeNode.getChildNodes() != null && typeNode.getChildNodes().item(0) != null) {
			workspaceConnectionInfo.setType((WorkspaceType) Enum.parse(WorkspaceType.class, Integer.valueOf(typeNode.getChildNodes().item(0).getNodeValue())));
		}

		Node nameNode = XmlUtilities.getChildElementNodeByName(workspaceConnectionInfoNode, "Name");
		if (nameNode.getChildNodes() != null && nameNode.getChildNodes().item(0) != null) {
			workspaceConnectionInfo.setName(nameNode.getChildNodes().item(0).getNodeValue());
		}

		Node databaseNode = XmlUtilities.getChildElementNodeByName(workspaceConnectionInfoNode, "Database");
		if (databaseNode.getChildNodes() != null && databaseNode.getChildNodes().item(0) != null) {
			workspaceConnectionInfo.setDatabase(databaseNode.getChildNodes().item(0).getNodeValue());
		}

		Node versionNode = XmlUtilities.getChildElementNodeByName(workspaceConnectionInfoNode, "Version");
		if (versionNode.getChildNodes() != null && versionNode.getChildNodes().item(0) != null) {
			workspaceConnectionInfo.setVersion((WorkspaceVersion) Enum.parse(WorkspaceVersion.class, Integer.valueOf(versionNode.getChildNodes().item(0).getNodeValue())));
		}

		return workspaceConnectionInfo;
	}
}
