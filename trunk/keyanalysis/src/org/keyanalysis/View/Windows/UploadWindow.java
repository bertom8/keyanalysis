package org.keyanalysis.View.Windows;

import java.io.File;

import org.keyanalysis.Services.Constants;
import org.keyanalysis.Services.FileUploaderService;
import org.keyanalysis.Services.ProcessService;
import org.keyanalysis.Services.DB.LogService;
import org.keyanalysis.View.KeyanalysisUI;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;

public class UploadWindow extends Window {
	private static final long serialVersionUID = -6573314797754683428L;
	private TextField format = new TextField();
	private TextField delimiter = new TextField();
	private CheckBox hasHeader;
	private TextField key = null;
	private double HammingDist = 0;

	public UploadWindow(String title) {
		super(title);
		this.setClosable(true);
		this.setResizable(false);
		this.setWindowMode(WindowMode.NORMAL);
		this.setModal(true);
		this.setResponsive(true);
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		this.setContent(layout);
		
		FileUploaderService uploaderService = new FileUploaderService(this);
		Upload up = new Upload();
		up.setId("uploadForm");
		up.setButtonCaption(Constants.upload);
		up.setResponsive(true);
		up.setReceiver(uploaderService);
		up.addStartedListener(uploaderService);
		up.addProgressListener(uploaderService);
		up.addSucceededListener(uploaderService);
		up.addFailedListener(uploaderService);
		up.setSizeFull();
		
		layout.addComponent(up);
		
		hasHeader = new CheckBox(Constants.hasTag, false);
		hasHeader.setVisible(false);
		hasHeader.addValueChangeListener(e -> { 
			if ((boolean)e.getProperty().getValue() == true) {
				format.setValue(""); format.setVisible(true); format.setEnabled(true);
			}else { 
				format.setValue(""); format.setVisible(false); format.setEnabled(false);
			} });
		layout.addComponent(hasHeader);
		format.setVisible(false);
		layout.addComponent(format);
		delimiter.setVisible(false);
		delimiter.setCaption(Constants.delimiter);
		//delimiter.setInputPrompt("");
		layout.addComponent(delimiter);
		
		
		key = new TextField();
		key.setValue(null);
		key.setNullRepresentation("");
		key.setNullSettingAllowed(true);
		key.addValidator(new StringLengthValidator(Constants.notCorrectKey, 32, 32, true));
		key.setId("keyInput");
		key.setImmediate(true);
		key.setMaxLength(Constants.keyMaxLength);
		key.setWidth("95%");
		key.setInputPrompt(Constants.keyFieldPrompt);
		layout.addComponent(key);
		
		layout.addComponent(new Label("<hr/>", ContentMode.HTML));
		TextField hamming = new TextField(Constants.wantedHamming);
		layout.addComponent(hamming);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setId("uploadControllerButtons");
		hl.setMargin(true);
		
		Button done = new Button(Constants.done, new ClickListener() {
			private static final long serialVersionUID = -9118926051039751583L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (uploaderService.isDone()) {
					if ( hamming.getValue() != null && !("".equals(hamming.getValue())) ) {
						if (!hamming.getValue().matches("[0][\\.|\\,]\\d+")) {
							new Notification("Error", "Give a correct Hamming value!", Type.ERROR_MESSAGE).show(Page.getCurrent());
							return;
						}
								
						if ((Double.parseDouble(hamming.getValue()) > 0 && Double.parseDouble(hamming.getValue()) < 1))
							HammingDist = Double.parseDouble(hamming.getValue());
					}	
					
					ProcessService ps = new ProcessService(uploaderService.getFilename(), getWindow(), uploaderService.getFilePath());
					((KeyanalysisUI)UI.getCurrent()).setProcess(ps);
					((KeyanalysisUI)UI.getCurrent()).getProcess().run();
					LogService.AddLogEntry("Successed upload", null, "Item");
					/*if (((KeyanalysisUI)UI.getCurrent()).getLista().isEmpty()) {
						((KeyanalysisUI)UI.getCurrent()).getChooseButton().addClickListener(new ClickListener() {
							private static final long serialVersionUID = -5380548977298350848L;
	
							@Override
							public void buttonClick(ClickEvent event) {
								makeChartsVisibility(ps);
							}
						});
						
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( " " ));
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( "AES" ));
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( "Serpent" ));
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( "Blowfish" ));
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( "Twofish" ));
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( "SHA" ));
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( "Skein" ));
						((KeyanalysisUI)UI.getCurrent()).getLista().addItem(new String( "Entropies/Collisions" ));
						
						((KeyanalysisUI)UI.getCurrent()).getLista().setValue(new String( " " ));	
					}*/		
					
					close();
				}
			}
		});
		done.setStyleName("uploadButton");
		
		Button cancel = new Button(Constants.cancel, new ClickListener() {
			private static final long serialVersionUID = 1489239772039007250L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(uploaderService.isDone()) {
					if (new File(VaadinServlet.getCurrent().getServletContext().getRealPath("") + uploaderService.getFilename()).delete())
						close();
					else throw new UnsupportedOperationException();
				}
				close();
			}
		});
		cancel.setStyleName("uploadButton");
		
		hl.addComponents(cancel, done);
		layout.addComponent(hl);
	}
	
	/*private void makeChartsVisibility(ProcessService ps) {
		switch ( ((KeyanalysisUI)UI.getCurrent()).getLista().getValue().toString() ) {
		case "AES":
			ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.key10Panel_Number, true);
			
			ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.entropy_Number, false);
			ps.getCharts().setPanelVisible(Constants.collisions_Number, false);
			ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, false);
			break;
		case "Serpent":
			ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.key10Panel_Number, true);
			
			ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.entropy_Number, false);
			ps.getCharts().setPanelVisible(Constants.collisions_Number, false);
			ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, false);
			break;
		case "Blowfish":
			ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.key10Panel_Number, true);
			
			ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.entropy_Number, false);
			ps.getCharts().setPanelVisible(Constants.collisions_Number, false);
			ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, false);
			break;
		case "Twofish":
			ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.key10Panel_Number, true);
			
			ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.entropy_Number, false);
			ps.getCharts().setPanelVisible(Constants.collisions_Number, false);
			ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, false);
			break;
		case "SHA":
			ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, true);
			
			ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.key10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.entropy_Number, false);
			ps.getCharts().setPanelVisible(Constants.collisions_Number, false);
			ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, false);
			break;
		case "Skein":
			ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, true);
			ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, true);
			
			ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.key10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.entropy_Number, false);
			ps.getCharts().setPanelVisible(Constants.collisions_Number, false);
			ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, false);
			break;
		case "Entropies/Collisions":
			ps.getCharts().setPanelVisible(Constants.entropy_Number, true);
			ps.getCharts().setPanelVisible(Constants.collisions_Number, true);
			ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, true);
			
			ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.key10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, false);
			ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, false);
			break;
		default:
			makeAllChartVisible(ps);
		}
	}*/
	
	/*private void makeAllChartVisible(ProcessService ps) {
		ps.getCharts().setPanelVisible(Constants.AES10Panel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textAESHammingPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textPerAESPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.key10Panel_Number, true);
		ps.getCharts().setPanelVisible(Constants.serpent10Panel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textSerpentHammingPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textPerserpentPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.blowfish10Panel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textblowfishHammingPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textPerblowfishPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.twofish10Panel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textTwofishHammingPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textPerTwofishPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textSHAHammingPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textPerSHAPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textSkeinHammingPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.textPerSkeinPanel_Number, true);
		ps.getCharts().setPanelVisible(Constants.entropy_Number, true);
		ps.getCharts().setPanelVisible(Constants.collisions_Number, true);
		ps.getCharts().setPanelVisible(Constants.textLengthPanel_Number, true);
	}*/
	
	public CheckBox getHasHeader() {
		return hasHeader;
	}

	public TextField getDelimiter() {
		return delimiter;
	}
	
	public void setDelimiter(TextField tf) {
		delimiter = tf;
	}

	public TextField getKey() {
		return key;
	}

	public TextField getFormat() {
		return format;
	}
	
	private UploadWindow getWindow() {
		return this;
	}

	public void setFormat(TextField tf) {
		format = tf;
	}

	public double getHammingDist() {
		return HammingDist;
	}
}
