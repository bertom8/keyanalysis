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
	private CssLayout chartsLayout = new CssLayout();
	private KeyanalysisUI ui;

	/**
	 * Inicializló metódus ami beállítja a chart layoutot a UI-on
	 * 
	 * @param u
	 */
	public void getCharts(KeyanalysisUI u) {
		ui = u;
		ui.getRoot().getContent().removeAllComponents();
		chartsLayout.removeAllComponents();
		chartsLayout.setId("chartsLayout");
		addPanels();
		ui.getRoot().getContent().addComponent(chartsLayout);
	}

	/**
	 * Megjeleníti a hisztogrammok számára és az entrópiáknak/ütközésszámoknak a paneleket UI-on
	 */
	private void addPanels() {
		// text/AES arány
		Panel textPerAES = new Panel(Constants.textPerAESPanel);
		textPerAES.setStyleName("chartPanel");
		textPerAES.setWidth(Constants.fiftyPerCent);
		textPerAES.setHeight("100%");
		createPanelContent(textPerAES, "textPerAES");
		chartsLayout.addComponent(textPerAES, Constants.textPerAESPanel_Number);

		// text/SHA arány
		Panel textPerSHA = new Panel(Constants.textPerSHAPanel);
		textPerSHA.setStyleName("chartPanel");
		textPerSHA.setWidth(Constants.fiftyPerCent);
		createPanelContent(textPerSHA, "textPerSHA");
		chartsLayout.addComponent(textPerSHA, Constants.textPerSHAPanel_Number);

		// text/Skein arány
		Panel textPerSkein = new Panel(Constants.textPerSkeinPanel);
		textPerSkein.setStyleName("chartPanel");
		textPerSkein.setWidth(Constants.fiftyPerCent);
		createPanelContent(textPerSkein, "textPerSkein");
		chartsLayout.addComponent(textPerSkein, Constants.textPerSkeinPanel_Number);

		// aes-ekben lévõ 1/0 arány
		Panel AES10 = new Panel(Constants.AES10Panel);
		AES10.setStyleName("chartPanel");
		AES10.setWidth(Constants.fiftyPerCent);
		createPanelContent(AES10, "aes10");
		chartsLayout.addComponent(AES10, Constants.AES10Panel_Number);

		// kulcsokban lévõ 1/0 arány
		Panel key10 = new Panel(Constants.key10Panel);
		key10.setStyleName("chartPanel");
		key10.setWidth(Constants.fiftyPerCent);
		createPanelContent(key10, "key10");
		chartsLayout.addComponent(key10, Constants.key10Panel_Number);

		// text és AES hammingtávolsága / text hossz
		Panel textAESHamming = new Panel(Constants.textAESHammingPanel);
		textAESHamming.setStyleName("chartPanel");
		textAESHamming.setWidth(Constants.fiftyPerCent);
		createPanelContent(textAESHamming, "textAESHamming");
		chartsLayout.addComponent(textAESHamming, Constants.textAESHammingPanel_Number);

		// text és SHA hammingtávolsága / text hossz
		Panel textSHAHamming = new Panel(Constants.textSHAHammingPanel);
		textSHAHamming.setStyleName("chartPanel");
		textSHAHamming.setWidth(Constants.fiftyPerCent);
		createPanelContent(textSHAHamming, "textSHAHamming");
		chartsLayout.addComponent(textSHAHamming, Constants.textSHAHammingPanel_Number);
		
		Panel textSkeinHamming = new Panel(Constants.textSkeinHammingPanel);
		textSkeinHamming.setStyleName("chartPanel");
		textSkeinHamming.setWidth(Constants.fiftyPerCent);
		createPanelContent(textSkeinHamming, "textSkeinHamming");
		chartsLayout.addComponent(textSkeinHamming, Constants.textSkeinHammingPanel_Number);

		// texthosszok
		Panel textLenghts = new Panel(Constants.textLengthPanel);
		textLenghts.setStyleName("chartPanel");
		textLenghts.setWidth(Constants.fiftyPerCent);
		createPanelContent(textLenghts, "textLenghts");
		chartsLayout.addComponent(textLenghts, Constants.textLengthPanel_Number);

		// Min entrópia és Entrópia számítások

		Panel entropies = new Panel(Constants.entropyPanel);
		entropies.setWidth("100%");
		chartsLayout.addComponent(entropies, Constants.entropy_Number);
		
		Panel collisions = new Panel(Constants.collisionsPanel);
		collisions.setWidth("100%");
		chartsLayout.addComponent(collisions, Constants.collisions_Number);
		
		Panel serpent10 = new Panel(Constants.serpentPanel);
		serpent10.setStyleName("chartPanel");
		serpent10.setWidth(Constants.fiftyPerCent);
		createPanelContent(serpent10, "serpent10");
		chartsLayout.addComponent(serpent10, Constants.serpent10Panel_Number);
		
		Panel textPerserpent = new Panel(Constants.textPerSerpentPanel);
		textPerserpent.setStyleName("chartPanel");
		textPerserpent.setWidth(Constants.fiftyPerCent);
		createPanelContent(textPerserpent, "textPerserpent");
		chartsLayout.addComponent(textPerserpent, Constants.textPerserpentPanel_Number);
		
		Panel textSerpentHamming = new Panel(Constants.textSerpentHammingPanel);
		textSerpentHamming.setStyleName("chartPanel");
		textSerpentHamming.setWidth(Constants.fiftyPerCent);
		createPanelContent(textSerpentHamming, "textSerpentHamming");
		chartsLayout.addComponent(textSerpentHamming, Constants.textSerpentHammingPanel_Number);
		
		Panel twofish10 = new Panel(Constants.twofishPanel);
		twofish10.setStyleName("chartPanel");
		twofish10.setWidth(Constants.fiftyPerCent);
		createPanelContent(twofish10, "twofish10");
		chartsLayout.addComponent(twofish10, Constants.twofish10Panel_Number);
		
		Panel textPerTwofish = new Panel(Constants.textPerTwofishPanel);
		textPerTwofish.setStyleName("chartPanel");
		textPerTwofish.setWidth(Constants.fiftyPerCent);
		createPanelContent(textPerTwofish, "textPerTwofish");
		chartsLayout.addComponent(textPerTwofish, Constants.textPerTwofishPanel_Number);
		
		Panel textTwofishHamming = new Panel(Constants.textTwofishHammingPanel);
		textTwofishHamming.setStyleName("chartPanel");
		textTwofishHamming.setWidth(Constants.fiftyPerCent);
		createPanelContent(textTwofishHamming, "textTwofishHamming");
		chartsLayout.addComponent(textTwofishHamming, Constants.textTwofishHammingPanel_Number);
		
		Panel blowfish10 = new Panel(Constants.blowfishPanel);
		blowfish10.setStyleName("chartPanel");
		blowfish10.setWidth(Constants.fiftyPerCent);
		createPanelContent(blowfish10, "blowfish10");
		chartsLayout.addComponent(blowfish10, Constants.blowfish10Panel_Number);
		
		Panel textPerblowfish = new Panel(Constants.textPerBlowfishPanel);
		textPerblowfish.setStyleName("chartPanel");
		textPerblowfish.setWidth(Constants.fiftyPerCent);
		createPanelContent(textPerblowfish, "textPerblowfish");
		chartsLayout.addComponent(textPerblowfish, Constants.textPerblowfishPanel_Number);
		
		Panel textblowfishHamming = new Panel(Constants.textBlowfishHammingPanel);
		textblowfishHamming.setStyleName("chartPanel");
		textblowfishHamming.setWidth(Constants.fiftyPerCent);
		createPanelContent(textblowfishHamming, "textblowfishHamming");
		chartsLayout.addComponent(textblowfishHamming, Constants.textblowfishHammingPanel_Number);

	}
	
	public void setPanelVisible(int index, boolean value) {
		chartsLayout.getComponent(index).setVisible(value);
	}

	/**
	 * ElkÃ©szÃ­t egy hisztogrammot a megadott adatokkal teli listÃ¡bÃ³l
	 * 
	 * 
	 * @param list
	 */
	public void createHistogram(List<Double> list, int index, String histType) {
		if (list.isEmpty()) {
			new Notification(Constants.histogramError, Type.ERROR_MESSAGE);
			return;
		}
		
		((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
				.getContent()).getComponent(1)).getComponent(0)).setValue(String.valueOf(ProcessService.avg(list)));
		((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
				.getContent()).getComponent(1)).getComponent(1)).setValue(String.valueOf(ProcessService.dispersion(list)));
		
		if (Constants.HammingHistogramm.equals(histType)) {
			HammingComplex comp = new HammingComplex(list);
			//LineComplex line = new LineComplex(list,  Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			//	.getContent()).getComponent(1)).getComponent(0)).getValue()), Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			//			.getContent()).getComponent(1)).getComponent(1)).getValue()));
			
			((CssLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index)).getContent())
					.getComponent(0)).addComponent(comp);
			//((CssLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index)).getContent())
			//		.getComponent(0)).addComponent(line);
		} else if (Constants.RateHistogramm.equals(histType)) {
			RateComplex comp = new RateComplex(list);
			//LineComplex line = new LineComplex(list,  Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			//		.getContent()).getComponent(1)).getComponent(0)).getValue()), Double.parseDouble(((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
			//				.getContent()).getComponent(1)).getComponent(1)).getValue()));
			
			((CssLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index)).getContent())
					.getComponent(0)).addComponent(comp);
			//((CssLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index)).getContent())
			//		.getComponent(0)).addComponent(line);
		}
		
		Collections.sort(list);
		((TextField)((HorizontalLayout)((VerticalLayout)((Panel)chartsLayout.getComponent(index))
				.getContent()).getComponent(1)).getComponent(2)).setValue(String.valueOf(list.get(list.size()/2)));
	}

	public void createHistogram(List<Integer> list, int index) {
		if (list.isEmpty()) {
			new Notification(Constants.histogramError, Type.ERROR_MESSAGE);
			return;
		}
		LengthComplex comp = new LengthComplex(list);
		((Panel) chartsLayout.getComponent(index)).setContent(comp);

	}

	public void drawEntropy(double[] sourceEntropies, double[] aesEntropies, double[] serpentEntropies,
			double[] blowfishEntropies, double[] twofishEntropies, double[] shaEntropies, double[] skeinEntropies) {
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		VerticalLayout shannon = new VerticalLayout();
		shannon.setWidth("100%");
		shannon.setMargin(true);
		Label shannonEnthropyOfSource = new Label(Constants.SourceEntropy + sourceEntropies[0], ContentMode.HTML);
		Label shannonEnthropyOfAES = new Label(Constants.AesEntropy + aesEntropies[0], ContentMode.HTML);
		Label shannonEnthropyOfSerpent = new Label(Constants.SerpentEntropy + serpentEntropies[0], ContentMode.HTML);
		Label shannonEnthropyOfBlowfish = new Label(Constants.BlowfishEntropy + blowfishEntropies[0], ContentMode.HTML);
		Label shannonEnthropyOfTwofish = new Label(Constants.TwofishEntropy + twofishEntropies[0], ContentMode.HTML);
		Label shannonEnthropyOfSHA = new Label(Constants.ShaEntropy + shaEntropies[0], ContentMode.HTML);
		Label shannonEnthropyOfSkein = new Label(Constants.SkeinEntropy + skeinEntropies[0] + "<br/><br/>", ContentMode.HTML);
		shannon.addComponents(shannonEnthropyOfSource, shannonEnthropyOfAES, shannonEnthropyOfSerpent,
				shannonEnthropyOfBlowfish, shannonEnthropyOfTwofish, shannonEnthropyOfSHA, shannonEnthropyOfSkein);
		VerticalLayout min = new VerticalLayout();
		min.setWidth("100%");
		min.setMargin(true);
		Label minEnthropyOfSource = new Label(Constants.SourceMinEntropy + sourceEntropies[1], ContentMode.HTML);
		Label minEnthropyOfAES = new Label(Constants.AesMinEntropy + aesEntropies[1], ContentMode.HTML); 
		Label minEnthropyOfSerpent = new Label(Constants.SerpentMinEntropy + serpentEntropies[1], ContentMode.HTML); 
		Label minEnthropyOfBlowfish = new Label(Constants.BlowfishMinEntropy + blowfishEntropies[1], ContentMode.HTML); 
		Label minEnthropyOfTwofish = new Label(Constants.TwofishMinEntropy + twofishEntropies[1], ContentMode.HTML); 
		Label minEnthropyOfSHA = new Label(Constants.ShaMinEntropy + shaEntropies[1], ContentMode.HTML);
		Label minEnthropyOfSkein = new Label(Constants.SkeinMinEntropy + skeinEntropies[1], ContentMode.HTML);
		min.addComponents(minEnthropyOfSource, minEnthropyOfAES, minEnthropyOfSerpent, minEnthropyOfBlowfish,
				minEnthropyOfTwofish, minEnthropyOfSHA, minEnthropyOfSkein);
		hl.addComponents(shannon, min);		
		vl.addComponent(hl);

		((Panel) chartsLayout.getComponent(Constants.entropy_Number)).setContent(vl);
	}
	
	public void drawCollisions(HashMap<String, Integer> colls) {
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		VerticalLayout sha = new VerticalLayout();
		sha.setWidth("100%");
		sha.setMargin(true);
		sha.addComponent(new Label("SHA ütközésszám 256 bit esetén: " + colls.get("SHA64")) );
		sha.addComponent(new Label("SHA ütközésszám 128 bit esetén: " + colls.get("SHA32")) );
		sha.addComponent(new Label("SHA ütközésszám 64 bit esetén: " + colls.get("SHA16")) ); 
		VerticalLayout skein = new VerticalLayout();
		skein.setWidth("100%");
		skein.setMargin(true);
		skein.addComponent(new Label("Skein ütközésszám 256 bit esetén: " + colls.get("SKEIN64")) );
		skein.addComponent(new Label("Skein ütközésszám 128 bit esetén: " + colls.get("SKEIN32")) );
		skein.addComponent(new Label("Skein ütközésszám 64 bit esetén: " + colls.get("SKEIN16")) ); 
		VerticalLayout aes = new VerticalLayout();
		aes.setWidth("100%");
		aes.setMargin(true);
		aes.addComponent(new Label("AES ütközésszám 256 bit esetén: " + colls.get("AES")));
		hl.addComponents(sha, skein);
		vl.addComponent(hl);
		
		((Panel) chartsLayout.getComponent(Constants.collisions_Number)).setContent(vl);
	}
	
	private void createPanelContent(Panel p, String tagName) {
		VerticalLayout vsp = new VerticalLayout();
		vsp.setSizeFull();
		
		CssLayout chart = new CssLayout();
		chart.setSizeFull();
		chart.setId(tagName);
		vsp.addComponent(chart);
		
		HorizontalLayout datas = new HorizontalLayout();
		TextField atlag = new TextField(Constants.atlag);
		atlag.setEnabled(false);
		datas.addComponent(atlag);		
		TextField szoras = new TextField(Constants.szoras);
		szoras.setEnabled(false);
		datas.addComponent(szoras);
		TextField median = new TextField(Constants.median);
		median.setEnabled(false);
		datas.addComponent(median);
		
		vsp.addComponent(datas);
		p.setContent(vsp);
	}

	public CssLayout getChartsLayout() {
		return chartsLayout;
	}
}
