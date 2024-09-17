package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TimerDisplay extends Text {
	private Timeline scheduler;
	private long startTime;
	private long millisBetweenUpdates;
	private boolean running = false;

	public TimerDisplay(long _millisBetweenUpdates) {
		millisBetweenUpdates = _millisBetweenUpdates;

		scheduler = new Timeline();
		scheduler.getKeyFrames().add(new KeyFrame(Duration.millis(millisBetweenUpdates), ev -> {
			if (running) {
				updateDisplay(System.currentTimeMillis() - startTime);
			}
		}));
		scheduler.setCycleCount(Animation.INDEFINITE);

		updateDisplay(0);
	}

	public void start() {
		startTime = System.currentTimeMillis();
		running = true;

		scheduler.playFromStart();
	}

	public void stop() {
		updateDisplay(System.currentTimeMillis() - startTime);
		running = false;

		scheduler.stop();
	}

	private void updateDisplay(long passedMillis) {
		long minutes = (passedMillis / 1000) / 60;
		long seconds = (passedMillis / 1000) % 60;
		long millis = (passedMillis % 1000);

		this.setText(minutes + ":" + String.format("%02d", seconds) + "." + String.format("%03d", millis));
	}
}
