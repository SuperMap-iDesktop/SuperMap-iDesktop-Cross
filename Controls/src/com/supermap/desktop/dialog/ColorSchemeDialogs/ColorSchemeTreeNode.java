package com.supermap.desktop.dialog.ColorSchemeDialogs;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorScheme;
import com.supermap.desktop.core.IteratorEnumerationAdapter;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author XiaJT
 */
public class ColorSchemeTreeNode extends DefaultMutableTreeNode implements Cloneable, TreeNode {

	private List<ColorSchemeTreeNode> children;
	private List<ColorScheme> colorSchemes;
	private ColorSchemeTreeNode parent;
	private String name;
	private String filePath;

	private static final String[] DIRECTORY_NAMES = new String[]{"Grid_DEM", "Grid_HillShade", "Map_ThemeGraduatedSymbol"
			, "Map_ThemeGraph", "Map_ThemeRange", "Map_ThemeUnique", "Other_Gradien", "Other_Random"}; // Typo: Gradien 好像是Gradient,保持一致不修改

	public ColorSchemeTreeNode(ColorSchemeTreeNode parent) {
		this.parent = parent;

	}

	public String getFilePath() {
		if (parent == null) {
			return filePath;
		}
		String filePath = parent.getFilePath();

		String name = this.name;
		if (getParent() != null && getParent().getParent() == null) {
			if (name.equals(CoreProperties.getString("String_Default"))) {
				name = "Default";
			}
			if (name.equals(CoreProperties.getString("String_UserDefine"))) {
				name = "UserDefine";
			}
			if (name.equals(CoreProperties.getString("String_MyFavorites"))) {
				name = "Favorites";
			}
		}
		return filePath + name + System.getProperty("file.separator");
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void addColorScheme(ColorScheme colorScheme) {
		if (colorSchemes == null) {
			colorSchemes = new ArrayList<>();
		}
		colorScheme.setParentNode(this);
//		ArrayList<String> names = new ArrayList<>();
//		for (ColorScheme scheme : colorSchemes) {
//			names.add(scheme.getName());
//		}
//		colorScheme.setName(StringUtilities.getUniqueName(colorScheme.getName(), names));
		colorSchemes.add(colorScheme);
	}

	public List<ColorScheme> getColorSchemes() {
		if (colorSchemes != null) {
			return colorSchemes;
		}
		if (children != null && children.size() > 0) {
			ArrayList<ColorScheme> colorSchemes = new ArrayList<>();
			for (ColorSchemeTreeNode child : children) {
				if (child.getColorSchemes() != null && child.getColorSchemes().size() > 0) {
					colorSchemes.addAll(child.getColorSchemes());
				}
			}
			return colorSchemes;
		}
		return null;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		if (children != null) {
			return children.size();
		}
		return 0;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		if (children == null) {
			return -1;
		}
		return children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return children == null || children.size() == 0;
	}

	@Override
	public Enumeration children() {
		if (children == null) {
			children = new ArrayList<>();
		}
		return new IteratorEnumerationAdapter<>(children.iterator());
	}

	public void addChild(ColorSchemeTreeNode child) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getShowName();
	}

	public void setColorSchemes(List<ColorScheme> colorSchemes) {
		this.colorSchemes = colorSchemes;
	}

	@Override
	public ColorSchemeTreeNode clone() {

		ColorSchemeTreeNode clone = null;
		clone = (ColorSchemeTreeNode) super.clone();
		if (clone == null) {
			return null;
		}
		clone.filePath = this.filePath;
		clone.children = null;
		clone.colorSchemes = null;
		if (children != null && children.size() > 0) {
			for (ColorSchemeTreeNode child : children) {
				ColorSchemeTreeNode node = child.clone();
				node.parent = clone;
				clone.addChild(node);
			}
		}
		clone.setName(this.name);
		if (colorSchemes != null && colorSchemes.size() > 0) {
			for (ColorScheme colorScheme : colorSchemes) {
				ColorScheme colorScheme1 = colorScheme.clone();
				colorScheme1.setColorSchemePath(colorScheme.getColorSchemePath());
				colorScheme1.setParentNode(clone);
				clone.addColorScheme(colorScheme1);
			}
		}
		return clone;
	}

	public String getName() {
		return name;
	}

	public ColorSchemeTreeNode getChild(String name) {
		if (children != null) {
			for (ColorSchemeTreeNode aChildren : children) {
				if (aChildren.getName().equals(name)) {
					return aChildren;
				}
			}
		}
		ColorSchemeTreeNode colorSchemeTreeNode = new ColorSchemeTreeNode(this);
		colorSchemeTreeNode.setName(name);
		this.addChild(colorSchemeTreeNode);
		return colorSchemeTreeNode;
	}

	public void removeChild(ColorSchemeTreeNode lastSelectedPathComponent) {
		if (new File(lastSelectedPathComponent.getFilePath()).exists()) {
			File file = new File(lastSelectedPathComponent.getFilePath());
			for (File file1 : file.listFiles()) {
				file1.delete();
			}
			file.delete();
		}
		children.remove(lastSelectedPathComponent);
	}

	public ColorSchemeTreeNode getChildByName(String name) {
		if (children != null && children.size() > 0) {
			for (ColorSchemeTreeNode child : children) {
				if (child.getName().equals(name) || child.getShowName().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

	public String getShowName() {
		for (String directoryName : DIRECTORY_NAMES) {
			if (directoryName.equals(name)) {
				return ControlsProperties.getString("String_ColorSchemeManager_" + name);
			}
		}
		return name;
	}

	public void save() {
		if (!new File(getFilePath()).exists()) {
			new File(getFilePath()).mkdirs();
		}
		if (colorSchemes != null && colorSchemes.size() > 0) {
			for (ColorScheme colorScheme : colorSchemes) {
				colorScheme.save();
			}
		}
		if (children != null) {
			for (ColorSchemeTreeNode child : children) {
				child.save();
			}
		}
	}
}
