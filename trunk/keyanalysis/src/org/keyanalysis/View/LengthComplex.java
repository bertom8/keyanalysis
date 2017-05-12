package org.keyanalysis.View;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * 
 * @author Bereczki Tamás
 *
 */
@JavaScript("org_keyanalysis_View_LengthComplex.js")
public class LengthComplex extends AbstractJavaScriptComponent {
	private static final long serialVersionUID = -9061447705933132682L;

	private List<Integer> list;

	public void sendComplexTypes() {
		getRpcProxy(LengthComplexTypesRpc.class).sendComplexTypes(list);
	}

	public LengthComplex(List<Integer> list) {
		this.list = new ArrayList<Integer>(list);
		sendComplexTypes();
	}

	public LengthComplex() {
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}
}
