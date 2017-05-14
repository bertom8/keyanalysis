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
		this.getRpcProxy(LengthComplexTypesRpc.class).sendComplexTypes(this.list);
	}

	public LengthComplex(final List<Integer> list) {
		this.list = new ArrayList<>(list);
		this.sendComplexTypes();
	}

	public LengthComplex() {
	}

	public List<Integer> getList() {
		return this.list;
	}

	public void setList(final List<Integer> list) {
		this.list = list;
	}
}
