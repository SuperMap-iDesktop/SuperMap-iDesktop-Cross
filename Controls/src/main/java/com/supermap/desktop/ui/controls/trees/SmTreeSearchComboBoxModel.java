package com.supermap.desktop.ui.controls.trees;

import com.supermap.desktop.ui.controls.comboBox.SearchBoxModel;
import com.supermap.desktop.ui.controls.comboBox.SearchItemValueGetter;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.tree.TreeNode;
import java.util.Locale;

/**
 * @author XiaJT
 */
public class SmTreeSearchComboBoxModel<T extends TreeNode> extends SearchBoxModel<T> {

	private SmTreeSearchComboBox<T> comboBox;

	public SmTreeSearchComboBoxModel(SmTreeSearchComboBox<T> comboBox, SearchItemValueGetter searchItemValueGetter) {
		super(comboBox, searchItemValueGetter);
		this.comboBox = comboBox;
	}

	@Override
	public void updateModel(String inputString) {
		search(inputString);
	}

	public void search(String keyWord) {
		searchData.clear();
		if (StringUtilities.isNullOrEmpty(keyWord)) {
			return;
		}
		String country = Locale.getDefault().getCountry();
		switch (country) {
			case "CN":
				searchInChinese(keyWord);
				break;
			default:
				searchDirect(keyWord);
		}
		if (this.comboBox.isPopupVisible()) {
			this.comboBox.hidePopup();
		}
		if (getSize() > 0) {
			this.comboBox.showPopup();
		}
	}

	private void searchInChinese(String keyWord) {
		searchTree(keyWord, !StringUtilities.isAllChinese(keyWord));
	}

	private void searchDirect(String keyWord) {
		searchTree(keyWord, false);
	}

	private void searchTree(String keyWord, boolean isUsePingYin) {
		TreeNode root = (TreeNode) comboBox.getTree().getModel().getRoot();
		if (isUsePingYin) {
			keyWord = StringUtilities.convertToPingYin(keyWord);
		}
		searchNodeForChinese(keyWord, root, isUsePingYin);
	}


	private void searchNodeForChinese(String keyWord, TreeNode root, boolean isUsePingYin) {
		if (root == null) {
			return;
		}

		for (int i = 0; i < root.getChildCount(); i++) {
			TreeNode childAt = root.getChildAt(i);
			String treeSearchText = ((SearchItemValueGetter) comboBox.getTree()).getSearchString(childAt);
			if (isUsePingYin) {
				treeSearchText = StringUtilities.convertToPingYin(treeSearchText);
			}
			if (StringUtilities.isContain(treeSearchText, keyWord)) {
				addSearchResult((T) childAt);
			}
			if (!childAt.isLeaf()) {
				searchNodeForChinese(keyWord, childAt, isUsePingYin);
			}
		}
	}

}
