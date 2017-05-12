package org.keyanalysis.View;

import java.util.List;

import com.vaadin.shared.communication.ClientRpc;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public interface LineComplexRpc extends ClientRpc {
	public void sendComplexTypes(List<Double> list, double avg, double sdt);
}
