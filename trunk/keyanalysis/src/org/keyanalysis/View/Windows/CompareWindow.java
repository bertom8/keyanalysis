package org.keyanalysis.View.Windows;

import java.util.List;

import org.keyanalysis.Services.Constants;
import org.keyanalysis.Services.Tests;
import org.keyanalysis.View.CompareComplex;
import org.keyanalysis.View.KeyanalysisUI;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CompareWindow extends Window {
	private static final long serialVersionUID = 5018570437034992488L;
	int szamlalo = 0;

	public CompareWindow() {
		super();
		this.setWindowMode(WindowMode.NORMAL);
		this.setResponsive(true);
		this.setCaption(Constants.compare);
		this.center();
		final CssLayout layout = new CssLayout();
		final VerticalLayout vl = new VerticalLayout();
		final VerticalLayout aranyok = new VerticalLayout();
		aranyok.setEnabled(false);
		aranyok.setVisible(false);
		final VerticalLayout hammings = new VerticalLayout();
		hammings.setEnabled(false);
		hammings.setVisible(false);

		final OptionGroup ops = new OptionGroup();
		ops.setMultiSelect(false);
		ops.addItems("Arany", "Hamming");
		ops.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				if ("Arany".equals(event.getProperty().getValue())) {
					hammings.setEnabled(false);
					hammings.setVisible(false);
					aranyok.setEnabled(true);
					aranyok.setVisible(true);
				} else {
					hammings.setEnabled(true);
					hammings.setVisible(true);
					aranyok.setEnabled(false);
					aranyok.setVisible(false);
				}
			}
		});

		final OptionGroup rates = new OptionGroup();
		rates.setMultiSelect(true);
		rates.addItems("textPerAES", "textPerSHA", "textPerSkein", "AES10", "key10", "serpent10", "textPerserpent",
				"twofish10", "textPerTwofish", "blowfish10", "textPerblowfish");
		rates.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (event.getProperty().getValue().toString().split(",").length >= 2) {
					final Panel charts = new Panel("Hisztogrammok");
					vl.removeAllComponents();
					vl.addComponent(charts);
					layout.addComponent(vl);
					CompareWindow.this.drawCompare(event.getProperty().getValue().toString().split(","));
				}
			}
		});
		aranyok.addComponent(rates);

		final OptionGroup ham = new OptionGroup();
		ham.setMultiSelect(true);
		ham.addItems("textAESHamming", "textSHAHamming", "textSkeinHamming", "textSerpentHamming", "textTwofishHamming",
				"textblowfishHamming");
		ham.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (event.getProperty().getValue().toString().split(",").length >= 2) {
					final Panel charts = new Panel("Hisztogrammok");
					vl.removeAllComponents();
					vl.addComponent(charts);
					layout.addComponent(vl);
					CompareWindow.this.drawCompare(event.getProperty().getValue().toString().split(","));
				}
			}
		});
		hammings.addComponent(ham);

		vl.addComponents(ops, aranyok, hammings);
		layout.addComponent(vl);

		this.setContent(layout);
	}

	private void drawCompare(final Object arrays) {
		final String first = ((String[]) arrays)[0].substring(1);
		final String second = ((String[]) arrays)[1].substring(1, ((String[]) arrays)[1].length() - 1);
		final String filePath = ((KeyanalysisUI) UI.getCurrent()).getProcess().getFilePath();

		List<Double> list = null;
		List<Double> list2 = null;

		switch (first) {
		case "textPerAES":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					true);
			break;
		case "textPerSHA":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha,
					true);
			break;
		case "textPerSkein":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein,
					true);
			break;
		case "AES10":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					false);
			break;
		case "key10":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.key,
					false);
			break;
		case "textAESHamming":
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes);
			break;
		case "textSHAHamming":
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha);
			break;
		case "textSkeinHamming":
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein);
			break;
		case "serpent10":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					false);
			break;
		case "textPerserpent":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					true);
			break;
		case "twofish10":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					false);
			break;
		case "textPerTwofish":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					true);
			break;
		case "blowfish10":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					false);
			break;
		case "textPerblowfish":
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					true);
			break;
		case "textSerpentHamming":
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent);
			break;
		case "textTwofishHamming":
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish);
			break;
		case "textblowfishHamming":
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish);
			break;
		default:
			break;
		}

		switch (second) {
		case "textPerAES":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					true);
			break;
		case "textPerSHA":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha,
					true);
			break;
		case "textPerSkein":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein,
					true);
			break;
		case "AES10":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					false);
			break;
		case "key10":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.key,
					false);
			break;
		case "textAESHamming":
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes);
			break;
		case "textSHAHamming":
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha);
			break;
		case "textSkeinHamming":
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein);
			break;
		case "serpent10":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					false);
			break;
		case "textPerserpent":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					true);
			break;
		case "twofish10":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					false);
			break;
		case "textPerTwofish":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					true);
			break;
		case "blowfish10":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					false);
			break;
		case "textPerblowfish":
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					true);
			break;
		case "textSerpentHamming":
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent);
			break;
		case "textTwofishHamming":
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish);
			break;
		case "textblowfishHamming":
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish);
			break;
		default:
			break;
		}

		final CompareComplex comp = new CompareComplex(list, list2);
		((Panel) ((VerticalLayout) ((CssLayout) ((CompareWindow) ((KeyanalysisUI) UI.getCurrent()).getWindows()
				.iterator().next()).getContent()).getComponent(0)).getComponent(0)).setContent(comp);
		// ((Panel)((Panel)((VerticalLayout)((CssLayout)((KeyanalysisUI)UI.getCurrent()).getWindows().iterator().next().getContent()).getComponent(0)).getComponent(0)).getContent()).setContent(comp);
	}
}