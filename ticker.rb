class Ticker
  import org.eclipse.swt.SWT
  import org.eclipse.swt.graphics.GC
  import org.eclipse.swt.graphics.Image
  import org.eclipse.swt.widgets.Display
  import org.eclipse.swt.widgets.Menu
  import org.eclipse.swt.widgets.MenuItem
  import org.eclipse.swt.widgets.Shell
  import org.eclipse.swt.widgets.TrayItem
  
  attr_writer :message
  
  def initialize(message = "JRuby Ticker  -  ")
    @message = message
    Thread.new do
      @display = Display.default
      @shell = Shell.new(@display);
      
      tray = @display.getSystemTray();
      if tray
        @trayItem = TrayItem.new(tray, SWT::NONE)
        @image = Image.new(@display, 240, 32)
        @trayItem.setImage(@image)
        @trayItem.setToolTipText("JRuby Ticker");
        @trayItem.setVisible(true)
        
        menu = Menu.new(@shell, SWT::POP_UP);
        @trayItem.addListener(SWT::MenuDetect) {|e| menu.setVisible(true)}
        
        menuItem = MenuItem.new(menu, SWT::PUSH)
        menuItem.setText("Exit")
        menuItem.addSelectionListener {exit}
      else
        raise "System Tray is not supported!"
      end

      @display.syncExec do
        @white = @display.getSystemColor(SWT::COLOR_WHITE)
        @black = @display.getSystemColor(SWT::COLOR_BLACK)
      end
      
      Thread.new do
        begin
          gc = GC.new(@image)
          loop{draw_message(gc)}
        ensure
          gc.dispose
        end
      end
      
      while (!@shell.isDisposed())
        if (!@display.readAndDispatch())
          @display.sleep();
        end
      end
      @image.dispose();
      @display.dispose();
    end
  end

  def draw_message(gc)
    @message.each_byte do |char|
      draw_char(gc, char)
    end
  end

  def draw_char(gc, char)
    char_width = gc.getCharWidth(java.lang.Character.new(char))
    old_image = Image.new(@display, @image.image_data)
    (1..char_width).each do |i|
      scroll(gc, old_image, i)
    end
    old_image.dispose
    gc.setForeground(@black)
    gc.drawString(char.chr, @image.bounds.width - char_width, 5)
    @display.syncExec {@trayItem.setImage(@image)}
  end

  def scroll(gc, old_image, i)
    start_draw = Time.now
    gc.drawImage(old_image, -i, 0)
    gc.setForeground(@white)
    gc.drawLine(@image.bounds.width-i, 0, @image.bounds.width-i, @image.bounds.height-1)
    @display.syncExec{@trayItem.setImage(@image)}
    delay = 0.01 - (Time.now - start_draw)
    sleep delay if delay > 0
  end
  
end
