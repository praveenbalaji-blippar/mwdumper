/*
 * DumperWindow.java
 *
 * Created on January 6, 2006, 2:57 PM
 *
 * Implementation on top of the NetBeans-generated code in DumperWindowForm.
 * I hate editing generated code!
 */

package org.mediawiki.dumper.gui;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.mediawiki.importer.DumpWriter;

/**
 *
 * @author brion
 */
public class DumperWindow extends DumperWindowForm {
	protected DumperGui backend;
	
	private static final int STOPPED = 0;
	private static final int RUNNING = 1;
	private int mode = STOPPED;
	
	/** Creates a new instance of DumperWindow */
	public DumperWindow(DumperGui backend) {
		this.backend = backend;
	}
	
	public DumpWriter getProgressWriter(DumpWriter sink, int interval) {
		return new GraphicalProgressFilter(sink, interval, progressLabel);
	}
	
	public void start() {
		// disable the other fields...
		setFieldsEnabled(false);
		startButton.setText("Cancel");
		mode = RUNNING;
	}
	
	public void stop() {
		startButton.setText("Start import");
		setFieldsEnabled(true);
		mode = STOPPED;
	}
	
	void setFieldsEnabled(boolean val) {
		final boolean _val = val;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Component[] widgets = new Component[] {
					fileText,
					browseButton,

					serverText,
					portText,
					userText,
					passwordText,
					connectButton,

					schema14Radio,
					schema15Radio,
					prefixText };
				for (int i = 0; i < widgets.length; i++) {
					widgets[i].setEnabled(_val);
				}
			}
		});
	}
	
	void connectionSucceeded() {
		setProgress("Connected to server!");
	}
	
	void connectionFailed() {
		
	}
	
	/**
	 * Set the progress bar text asynchronously, eg from a background thread
	 */
	public void setProgress(String text) {
		final String _text = text;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressLabel.setText(_text);
			}
		});
	}
	
	/* -- event handlers -- */
	
	protected void onBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {
		/*
		 * Note: I'm using the AWT FileDialog because JFileChooser is a piece
		 * of total crap that doesn't make any attempt to fit in with platform
		 * standards.
		 */
		FileDialog chooser = new FileDialog(this, "Select dump file");
		chooser.setVisible(true);
		String selection = chooser.getFile();
		if (selection != null) {
			try {
				fileText.setText(new File(selection).getCanonicalPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	protected void onConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {
		backend.connect(serverText.getText(),
				portText.getText(),
				userText.getText(),
				passwordText.getText());
	}
	
	protected void onStartButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (mode == RUNNING) {
			backend.abort();
		} else {
			try {
				backend.startImport(fileText.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	protected void onQuitItemActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}                                        
	
}
