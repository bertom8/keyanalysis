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
		this.setWidth("200px");
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
		ops.addItems(Messages.getString("CompareWindow.0"), Messages.getString("CompareWindow.1")); //$NON-NLS-1$ //$NON-NLS-2$
		ops.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (Messages.getString("CompareWindow.2").equals(event.getProperty().getValue())) { //$NON-NLS-1$
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
		rates.addItems(Messages.getString("CompareWindow.3"), Messages.getString("CompareWindow.4"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("CompareWindow.5"), Messages.getString("CompareWindow.6"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("CompareWindow.7"), Messages.getString("CompareWindow.8"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("CompareWindow.9"), //$NON-NLS-1$
				Messages.getString("CompareWindow.10"), Messages.getString("CompareWindow.11"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("CompareWindow.12"), Messages.getString("CompareWindow.13")); //$NON-NLS-1$ //$NON-NLS-2$
		rates.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (event.getProperty().getValue().toString()
						.split(Messages.getString("CompareWindow.14")).length >= 2) { //$NON-NLS-1$
					final Panel charts = new Panel(Messages.getString("CompareWindow.15")); //$NON-NLS-1$
					vl.removeAllComponents();
					vl.addComponent(charts);
					layout.addComponent(vl);
					CompareWindow.this.drawCompare(
							event.getProperty().getValue().toString().split(Messages.getString("CompareWindow.16"))); //$NON-NLS-1$
				}
			}
		});
		aranyok.addComponent(rates);

		final OptionGroup ham = new OptionGroup();
		ham.setMultiSelect(true);
		ham.addItems(Messages.getString("CompareWindow.17"), Messages.getString("CompareWindow.18"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("CompareWindow.19"), Messages.getString("CompareWindow.20"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("CompareWindow.21"), //$NON-NLS-1$
				Messages.getString("CompareWindow.22")); //$NON-NLS-1$
		ham.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				if (event.getProperty().getValue().toString()
						.split(Messages.getString("CompareWindow.23")).length >= 2) { //$NON-NLS-1$
					final Panel charts = new Panel(Messages.getString("CompareWindow.24")); //$NON-NLS-1$
					vl.removeAllComponents();
					vl.addComponent(charts);
					layout.addComponent(vl);
					CompareWindow.this.drawCompare(
							event.getProperty().getValue().toString().split(Messages.getString("CompareWindow.25"))); //$NON-NLS-1$
				}
			}
		});
		hammings.addComponent(ham);

		vl.addComponents(ops, aranyok, hammings);
		layout.addComponent(vl);

		this.setContent(layout);
	}

	private void drawCompare(final Object arrays) {
		this.setWidthUndefined();
		final String first = ((String[]) arrays)[0].substring(1);
		final String second = ((String[]) arrays)[1].substring(1, ((String[]) arrays)[1].length() - 1);
		final String filePath = ((KeyanalysisUI) UI.getCurrent()).getProcess().getFilePath();

		List<Double> list = null;
		List<Double> list2 = null;

		switch (first) {
		case "textPerAES": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					true);
			break;
		case "textPerSHA": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha,
					true);
			break;
		case "textPerSkein": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein,
					true);
			break;
		case "AES10": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					false);
			break;
		case "key10": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.key,
					false);
			break;
		case "textAESHamming": //$NON-NLS-1$
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes);
			break;
		case "textSHAHamming": //$NON-NLS-1$
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha);
			break;
		case "textSkeinHamming": //$NON-NLS-1$
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein);
			break;
		case "serpent10": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					false);
			break;
		case "textPerserpent": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					true);
			break;
		case "twofish10": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					false);
			break;
		case "textPerTwofish": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					true);
			break;
		case "blowfish10": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					false);
			break;
		case "textPerblowfish": //$NON-NLS-1$
			list = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					true);
			break;
		case "textSerpentHamming": //$NON-NLS-1$
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent);
			break;
		case "textTwofishHamming": //$NON-NLS-1$
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish);
			break;
		case "textblowfishHamming": //$NON-NLS-1$
			list = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish);
			break;
		default:
			break;
		}

		switch (second) {
		case "textPerAES": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					true);
			break;
		case "textPerSHA": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha,
					true);
			break;
		case "textPerSkein": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein,
					true);
			break;
		case "AES10": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes,
					false);
			break;
		case "key10": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.key,
					false);
			break;
		case "textAESHamming": //$NON-NLS-1$
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.aes);
			break;
		case "textSHAHamming": //$NON-NLS-1$
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.sha);
			break;
		case "textSkeinHamming": //$NON-NLS-1$
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.skein);
			break;
		case "serpent10": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					false);
			break;
		case "textPerserpent": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent,
					true);
			break;
		case "twofish10": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					false);
			break;
		case "textPerTwofish": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish,
					true);
			break;
		case "blowfish10": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					false);
			break;
		case "textPerblowfish": //$NON-NLS-1$
			list2 = Tests.aranyok(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.blowfish,
					true);
			break;
		case "textSerpentHamming": //$NON-NLS-1$
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.serpent);
			break;
		case "textTwofishHamming": //$NON-NLS-1$
			list2 = Tests.binDistance(filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName(),
					filePath + ((KeyanalysisUI) UI.getCurrent()).getProcess().getFile().getName() + Constants.twofish);
			break;
		case "textblowfishHamming": //$NON-NLS-1$
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