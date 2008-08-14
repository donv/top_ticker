package no.datek;

import no.datek.Ticker;

public class TopTicker {
	public static void main(String[] args) throws InterruptedException {
		new Ticker("TopTicker  -  ");

		// IO.popen('top -b') do |io|
		// output = ''
		// loop do
		// output << line = io.readline
		// if line.chomp.empty?
		// lines = output.scan /^\s*(\d+) +(\w+?) +(\d+) +(\d+) +(\d+.?)
		// +(\d+.?)
		// +(\d+.?) +(.) +(.+?) +(.+?) +(.+?) +(.+)$/
		// unless lines.empty?
		// puts "-  #{lines[0].last.strip} #{lines[0][8]}%  "
		// t.message = "#{lines[0].last.strip} #{lines[0][8]}%  -  "
		// end
		// output = ''
		// end
		// end
		// end
		// rescue
		// puts $!.message
		// puts $!.backtrace
		// exit 1
		// end
		Thread.sleep(10000);
		System.out.println("END!");
	}
}