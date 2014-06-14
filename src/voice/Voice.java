package voice;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import voice.SpeechToText;
import voice.JavaSoundRecorder;

public class Voice {

	private static final long serialVersionUID = -3644114956301374006L;
	private boolean on = false;
	private JavaSoundRecorder rec;
	// todo: ajouter clé
	private String credentials = "AIzaSyAuRBaso4NCxJjevkb09mjj6TrDrKhoapM";

	public Voice() 
	{	
		// a commenter si pas réseau UTC
		System.setProperty("https.proxyHost", "proxyweb.utc.fr");
		System.setProperty("https.proxyPort", "3128");
		rec = new JavaSoundRecorder();
	}
	
	public void startRec()
	{
		new Thread() {
			public void run() {
				rec.start();
			}
		}.start();
	}
	
	public String stopRec()
	{
		new Thread() {
			public void run() {
				rec.finish();
			}
		}.start();
		return SpeechToText.execute("File.wav", credentials);
	}

}
