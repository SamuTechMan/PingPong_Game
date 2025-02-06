package br.audio;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	private Clip clip;

	public AudioPlayer(String path) {
		try {
			// Verifique se o recurso existe
			InputStream inputStream = getClass().getResourceAsStream(path);
			if (inputStream == null) {
				throw new RuntimeException("Arquivo não encontrado: " + path);
			}

			AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream);
			AudioFormat baseFormat = ais.getFormat();

			// Formato decodificado (PCM)
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2, // Frame size = 2 bytes * canais
					baseFormat.getSampleRate(),
					false
			);

			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);

			// Inicie a reprodução
			clip.start();

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao carregar o áudio: " + e.getMessage());
		}
	}

	public void stop() {
		if (clip.isRunning())
			clip.stop();
	}

	public void play() {
		if (clip == null)
			return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}

	public void loop() {
		if (clip != null) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
}

