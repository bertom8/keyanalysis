package org.keyanalysis.View;

import java.util.List;

import com.vaadin.shared.communication.ClientRpc;

/**
 * 
 * @author Bereczki Tam�s
 */
public interface ComplexTypesRpc extends ClientRpc {
	public void sendComplexTypes(List<Double> list);
}
