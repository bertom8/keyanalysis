package org.keyanalysis.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.keyanalysis.Services.Constants;
import org.keyanalysis.Services.ProcessService;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public class Charts {
	private final CssLayout chartsLayout = new CssLayout();
	private KeyanalysisUI ui;

	/**
	 * Inicializló metódus ami beállítja a chart layoutot a UI-on
	 * 
	 * @param u
	 */
	public void getCharts(final KeyanalysisUI u) {
		this.ui = u;
		this.ui.getRoot().getContent().removeAllComponents();
		this.chartsLayout.removeAllComponents();
		this.chartsLayout.setId("chartsLayout");
		this.addPanels();
		this.ui.getRoot().getContent().addComponent(this.chartsLayout);
	}

	/**
	 * Megjeleníti a hisztogrammok számára és az entrópiáknak/ütközésszámoknak a
	 * paneleket UI-on
	 */
	private void addPanels() {
		// text/AES arány
		final Panel textPerAES = new Panel(Constants.textPerAESPanel);
		textPerAES.setStyleName("chartPanel");
		textPerAES.setWidth(Constants.fiftyPerCent);
		textPerAES.setHeight("100%");
		this.createPanelContent(textPerAES, "textPerAES");
		this.chartsLayout.addComponent(textPerAES, Constants.textPerAESPanel_Number);

		// text/SHA arány
		final Panel textPerSHA = new Panel(Constants.textPerSHAPanel);
		textPerSHA.setStyleName("chartPanel");
		textPerSHA.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textPerSHA, "textPerSHA");
		this.chartsLayout.addComponent(textPerSHA, Constants.textPerSHAPanel_Number);

		// text/Skein arány
		final Panel textPerSkein = new Panel(Constants.textPerSkeinPanel);
		textPerSkein.setStyleName("chartPanel");
		textPerSkein.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textPerSkein, "textPerSkein");
		this.chartsLayout.addComponent(textPerSkein, Constants.textPerSkeinPanel_Number);

		// aes-ekben lévõ 1/0 arány
		final Panel AES10 = new Panel(Constants.AES10Panel);
		AES10.setStyleName("chartPanel");
		AES10.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(AES10, "aes10");
		this.chartsLayout.addComponent(AES10, Constants.AES10Panel_Number);

		// kulcsokban lévõ 1/0 arány
		final Panel key10 = new Panel(Constants.key10Panel);
		key10.setStyleName("chartPanel");
		key10.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(key10, "key10");
		this.chartsLayout.addComponent(key10, Constants.key10Panel_Number);

		// text és AES hammingtávolsága / text hossz
		final Panel textAESHamming = new Panel(Constants.textAESHammingPanel);
		textAESHamming.setStyleName("chartPanel");
		textAESHamming.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textAESHamming, "textAESHamming");
		this.chartsLayout.addComponent(textAESHamming, Constants.textAESHammingPanel_Number);

		// text és SHA hammingtávolsága / text hossz
		final Panel textSHAHamming = new Panel(Constants.textSHAHammingPanel);
		textSHAHamming.setStyleName("chartPanel");
		textSHAHamming.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textSHAHamming, "textSHAHamming");
		this.chartsLayout.addComponent(textSHAHamming, Constants.textSHAHammingPanel_Number);

		final Panel textSkeinHamming = new Panel(Constants.textSkeinHammingPanel);
		textSkeinHamming.setStyleName("chartPanel");
		textSkeinHamming.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textSkeinHamming, "textSkeinHamming");
		this.chartsLayout.addComponent(textSkeinHamming, Constants.textSkeinHammingPanel_Number);

		// texthosszok
		final Panel textLenghts = new Panel(Constants.textLengthPanel);
		textLenghts.setStyleName("chartPanel");
		textLenghts.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textLenghts, "textLenghts");
		this.chartsLayout.addComponent(textLenghts, Constants.textLengthPanel_Number);

		// Min entrópia és Entrópia számítások
		final Panel entropies = new Panel(Constants.entropyPanel);
		entropies.setWidth("100%");
		this.chartsLayout.addComponent(entropies, Constants.entropy_Number);
		final Panel collisions = new Panel(Constants.collisionsPanel);
		collisions.setWidth("100%");
		this.chartsLayout.addComponent(collisions, Constants.collisions_Number);

		final Panel serpent10 = new Panel(Constants.serpentPanel);
		serpent10.setStyleName("chartPanel");
		serpent10.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(serpent10, "serpent10");
		this.chartsLayout.addComponent(serpent10, Constants.serpent10Panel_Number);

		final Panel textPerserpent = new Panel(Constants.textPerSerpentPanel);
		textPerserpent.setStyleName("chartPanel");
		textPerserpent.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textPerserpent, "textPerserpent");
		this.chartsLayout.addComponent(textPerserpent, Constants.textPerserpentPanel_Number);

		final Panel textSerpentHamming = new Panel(Constants.textSerpentHammingPanel);
		textSerpentHamming.setStyleName("chartPanel");
		textSerpentHamming.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textSerpentHamming, "textSerpentHamming");
		this.chartsLayout.addComponent(textSerpentHamming, Constants.textSerpentHammingPanel_Number);

		final Panel twofish10 = new Panel(Constants.twofishPanel);
		twofish10.setStyleName("chartPanel");
		twofish10.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(twofish10, "twofish10");
		this.chartsLayout.addComponent(twofish10, Constants.twofish10Panel_Number);

		final Panel textPerTwofish = new Panel(Constants.textPerTwofishPanel);
		textPerTwofish.setStyleName("chartPanel");
		textPerTwofish.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textPerTwofish, "textPerTwofish");
		this.chartsLayout.addComponent(textPerTwofish, Constants.textPerTwofishPanel_Number);

		final Panel textTwofishHamming = new Panel(Constants.textTwofishHammingPanel);
		textTwofishHamming.setStyleName("chartPanel");
		textTwofishHamming.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textTwofishHamming, "textTwofishHamming");
		this.chartsLayout.addComponent(textTwofishHamming, Constants.textTwofishHammingPanel_Number);

		final Panel blowfish10 = new Panel(Constants.blowfishPanel);
		blowfish10.setStyleName("chartPanel");
		blowfish10.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(blowfish10, "blowfish10");
		this.chartsLayout.addComponent(blowfish10, Constants.blowfish10Panel_Number);

		final Panel textPerblowfish = new Panel(Constants.textPerBlowfishPanel);
		textPerblowfish.setStyleName("chartPanel");
		textPerblowfish.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textPerblowfish, "textPerblowfish");
		this.chartsLayout.addComponent(textPerblowfish, Constants.textPerblowfishPanel_Number);

		final Panel textblowfishHamming = new Panel(Constants.textBlowfishHammingPanel);
		textblowfishHamming.setStyleName("chartPanel");
		textblowfishHamming.setWidth(Constants.fiftyPerCent);
		this.createPanelContent(textblowfishHamming, "textblowfishHamming");
		this.chartsLayout.addComponent(textblowfishHamming, Constants.textblowfishHammingPanel_Number);
	}

	public void setPanelVisible(final int index, final boolean value) {
		this.chartsLayout.getComponent(index).setVisible(value);
	}

	/**
	 * ElkÃ©szÃ­t egy hisztogrammot a megadott adatokkal teli listÃ¡bÃ³l
	 * 
	 * 
	 * @param list
	 */
	public void createHistogram(final List<Double> list, final int index, final String histType) {
		if (list.isEmpty()) {
			new Notification(Constants.histogramError, Type.ERROR_MESSAGE);
			return;
		}

		((TextField) ((HorizontalLayout) ((VerticalLayout) ((Panel) this.chartsLayout.getComponent(index)).getContent())
				.getComponent(1)).getComponent(0)).setValue(String.valueOf(ProcessService.avg(list)));
		((TextField) ((HorizontalLayout) ((VerticalLayout) ((Panel) this.chartsLayout.getComponent(index)).getContent())
				.getComponent(1)).getComponent(1)).setValue(String.valueOf(ProcessService.dispersion(list)));

		if (Constants.HammingHistogramm.equals(histType)) {
			final HammingComplex comp = new HammingComplex(list);
			// LineComplex line = new LineComplex(list,
			// Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			// .getContent()).getComponent(1)).getComponent(0)).getValue()),
			// Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			// .getContent()).getComponent(1)).getComponent(1)).getValue()));

			((CssLayout) ((VerticalLayout) ((Panel) this.chartsLayout.getComponent(index)).getContent())
					.getComponent(0)).addComponent(comp);
			// ((CssLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index)).getContent())
			// .getComponent(0)).addComponent(line);
		} else if (Constants.RateHistogramm.equals(histType)) {
			final RateComplex comp = new RateComplex(list);
			// LineComplex line = new LineComplex(list,
			// Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			// .getContent()).getComponent(1)).getComponent(0)).getValue()),
			// Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			// .getContent()).getComponent(1)).getComponent(1)).getValue()));

			((CssLayout) ((VerticalLayout) ((Panel) this.chartsLayout.getComponent(index)).getContent())
					.getComponent(0)).addComponent(comp);
			// ((CssLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index)).getContent())
			// .getComponent(0)).addComponent(line);
		}

		Collections.sort(list);
		((TextField) ((HorizontalLayout) ((VerticalLayout) ((Panel) this.chartsLayout.getComponent(index)).getContent())
				.getComponent(1)).getComponent(2)).setValue(String.valueOf(list.get(list.size() / 2)));
	}

	public void createHistogram(final List<Integer> list, final int index) {
		if (list.isEmpty()) {
			new Notification(Constants.histogramError, Type.ERROR_MESSAGE);
			return;
		}
		final LengthComplex comp = new LengthComplex(list);
		((Panel) this.chartsLayout.getComponent(index)).setContent(comp);

	}

	public void drawEntropy(final double[] sourceEntropies, final double[] aesEntropies,
			final double[] serpentEntropies, final double[] blowfishEntropies, final double[] twofishEntropies,
			final double[] shaEntropies, final double[] skeinEntropies) {
		final VerticalLayout vl = new VerticalLayout();
		final HorizontalLayout hl = new HorizontalLayout();
		final VerticalLayout shannon = new VerticalLayout();
		shannon.setWidth("100%");
		shannon.setMargin(true);
		final Label shannonEnthropyOfSource = new Label(Constants.SourceEntropy + sourceEntropies[0], ContentMode.HTML);
		final Label shannonEnthropyOfAES = new Label(Constants.AesEntropy + aesEntropies[0], ContentMode.HTML);
		final Label shannonEnthropyOfSerpent = new Label(Constants.SerpentEntropy + serpentEntropies[0],
				ContentMode.HTML);
		final Label shannonEnthropyOfBlowfish = new Label(Constants.BlowfishEntropy + blowfishEntropies[0],
				ContentMode.HTML);
		final Label shannonEnthropyOfTwofish = new Label(Constants.TwofishEntropy + twofishEntropies[0],
				ContentMode.HTML);
		final Label shannonEnthropyOfSHA = new Label(Constants.ShaEntropy + shaEntropies[0], ContentMode.HTML);
		final Label shannonEnthropyOfSkein = new Label(Constants.SkeinEntropy + skeinEntropies[0] + "<br/><br/>",
				ContentMode.HTML);
		shannon.addComponents(shannonEnthropyOfSource, shannonEnthropyOfAES, shannonEnthropyOfSerpent,
				shannonEnthropyOfBlowfish, shannonEnthropyOfTwofish, shannonEnthropyOfSHA, shannonEnthropyOfSkein);
		final VerticalLayout min = new VerticalLayout();
		min.setWidth("100%");
		min.setMargin(true);
		final Label minEnthropyOfSource = new Label(Constants.SourceMinEntropy + sourceEntropies[1], ContentMode.HTML);
		final Label minEnthropyOfAES = new Label(Constants.AesMinEntropy + aesEntropies[1], ContentMode.HTML);
		final Label minEnthropyOfSerpent = new Label(Constants.SerpentMinEntropy + serpentEntropies[1],
				ContentMode.HTML);
		final Label minEnthropyOfBlowfish = new Label(Constants.BlowfishMinEntropy + blowfishEntropies[1],
				ContentMode.HTML);
		final Label minEnthropyOfTwofish = new Label(Constants.TwofishMinEntropy + twofishEntropies[1],
				ContentMode.HTML);
		final Label minEnthropyOfSHA = new Label(Constants.ShaMinEntropy + shaEntropies[1], ContentMode.HTML);
		final Label minEnthropyOfSkein = new Label(Constants.SkeinMinEntropy + skeinEntropies[1], ContentMode.HTML);
		min.addComponents(minEnthropyOfSource, minEnthropyOfAES, minEnthropyOfSerpent, minEnthropyOfBlowfish,
				minEnthropyOfTwofish, minEnthropyOfSHA, minEnthropyOfSkein);
		hl.addComponents(shannon, min);
		vl.addComponent(hl);

		((Panel) this.chartsLayout.getComponent(Constants.entropy_Number)).setContent(vl);
	}

	public void drawCollisions(final HashMap<String, Integer> colls) {
		final VerticalLayout vl = new VerticalLayout();
		final HorizontalLayout hl = new HorizontalLayout();
		final VerticalLayout sha = new VerticalLayout();
		sha.setWidth("100%");
		sha.setMargin(true);
		sha.addComponent(new Label("SHA ütközésszám 256 bit esetén: " + colls.get("SHA64")));
		sha.addComponent(new Label("SHA ütközésszám 128 bit esetén: " + colls.get("SHA32")));
		sha.addComponent(new Label("SHA ütközésszám 64 bit esetén: " + colls.get("SHA16")));
		final VerticalLayout skein = new VerticalLayout();
		skein.setWidth("100%");
		skein.setMargin(true);
		skein.addComponent(new Label("Skein ütközésszám 256 bit esetén: " + colls.get("SKEIN64")));
		skein.addComponent(new Label("Skein ütközésszám 128 bit esetén: " + colls.get("SKEIN32")));
		skein.addComponent(new Label("Skein ütközésszám 64 bit esetén: " + colls.get("SKEIN16")));
		final VerticalLayout aes = new VerticalLayout();
		aes.setWidth("100%");
		aes.setMargin(true);
		aes.addComponent(new Label("AES ütközésszám 256 bit esetén: " + colls.get("AES")));
		hl.addComponents(sha, skein);
		vl.addComponent(hl);

		((Panel) this.chartsLayout.getComponent(Constants.collisions_Number)).setContent(vl);
	}

	private void createPanelContent(final Panel p, final String tagName) {
		final VerticalLayout vsp = new VerticalLayout();
		vsp.setSizeFull();

		final CssLayout chart = new CssLayout();
		chart.setSizeFull();
		chart.setId(tagName);
		vsp.addComponent(chart);

		final HorizontalLayout datas = new HorizontalLayout();
		final TextField atlag = new TextField(Constants.atlag);
		atlag.setEnabled(false);
		datas.addComponent(atlag);
		final TextField szoras = new TextField(Constants.szoras);
		szoras.setEnabled(false);
		datas.addComponent(szoras);
		final TextField median = new TextField(Constants.median);
		median.setEnabled(false);
		datas.addComponent(median);

		vsp.addComponent(datas);
		p.setContent(vsp);
	}

	public CssLayout getChartsLayout() {
		return this.chartsLayout;
	}
}
