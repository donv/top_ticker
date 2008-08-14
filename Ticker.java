package no.datek;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class Ticker implements Runnable {
	private String message;
	private Display display;
	private Shell shell;
	private Image image;
	private Color white;
	private Color black;

	public Ticker() {
		this("JRuby Ticker  -  ");
	}

	public Ticker(String startMessage) {
		this.message = startMessage;
		new Thread(this).start();
	}

	public void run() {
		this.display = Display.getDefault();
		this.shell = new Shell(this.display);

		Tray tray = this.display.getSystemTray();
		if (tray != null) {
			final TrayItem trayItem = new TrayItem(tray, SWT.NONE);
			this.image = new Image(this.display, 240, 32);
			trayItem.setImage(this.image);
			trayItem.setToolTipText("JRuby Ticker");
			trayItem.setVisible(true);

			final Menu menu = new Menu(this.shell, SWT.POP_UP);
			trayItem.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
			menuItem.setText("Exit");
			menuItem.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					System.exit(0);
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					System.exit(0);
				}
			});
			this.display.syncExec(new Runnable() {
				public void run() {
					white = display.getSystemColor(SWT.COLOR_WHITE);
					black = display.getSystemColor(SWT.COLOR_BLACK);
				}
			});

			new Thread(new Runnable() {
				public void run() {
					GC gc = new GC(image);
					try {
						for (;;) {
							for (char c : message.toCharArray()) {
								int char_width = gc.getCharWidth(c);
								Image oldImage = new Image(display, image.getImageData());
								for (int i = 1; i <= char_width; i++) {
								    long drawStart = System.currentTimeMillis();
									gc.drawImage(oldImage, -i, 0);
									gc.setForeground(white);
									gc.drawLine(image.getBounds().width - i, 0,
											image.getBounds().width - i, image
													.getBounds().height - 1);
									display.syncExec(new Runnable() {
										public void run() {
											trayItem.setImage(image);
										}
									});
									long drawStop = System.currentTimeMillis();
									long delay = 10 - drawStop + drawStart;
									if (delay > 0) {
									    Thread.sleep(delay);
									}
								}
								oldImage.dispose();
								gc.setForeground(black);
								gc.drawString(String.valueOf(c), image
										.getBounds().width
										- char_width, 5);
								display.syncExec(new Runnable() {
									public void run() {
										trayItem.setImage(image);
									}
								});
							}
						}
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
						gc.dispose();
					}
				}
			}).start();

			while (!this.shell.isDisposed()) {
				if (!this.display.readAndDispatch()) {
					this.display.sleep();
				}
			}
			this.image.dispose();
			this.display.dispose();
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
