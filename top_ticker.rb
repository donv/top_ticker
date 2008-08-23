#!/usr/bin/env jruby-trunk/bin/jruby

require 'java'
require 'rbconfig'

arch = Config::CONFIG['target_cpu']
os = Config::CONFIG['target_os']

$: << File.dirname(__FILE__)
require "swt-#{arch}-#{os}"

#$CLASSPATH << File.dirname(__FILE__) + '/classes/'
#import 'no.datek.Ticker'
require 'ticker'

t = Ticker.new

begin
  IO.popen('top -b') do |io|
    output = ''
    loop do
      output << line = io.readline
      if line.chomp.empty?
        lines = output.scan(/^\s*(\d+) +(\w+?) +(\d+) +(\d+) +(\d+.?) +(\d+.?) +(\d+.?) +(.) +(.+?) +(.+?) +(.+?) +(.+)$/)
        unless lines.empty?
          t.message = (0..2).map {|i| "#{lines[i].last.strip} #{lines[i][8]}%  -  "}.join
        end
        output = ''
      end
    end
  end
rescue
  puts $!.message
  puts $!.backtrace
  exit 1
end

puts "END!"
