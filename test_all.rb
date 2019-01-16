PHASE_1 = true
PHASE_3 = true
TIMEOUT = 20 # seconds

if PHASE_1
  (1..20).each do |i|
    # 20s each
    log = `gtimeout #{TIMEOUT} java -Xms1024m -Xmx1024m -jar Group26.jar phase3/graphs_phase1/graph#{i.to_s.rjust(2, ?0)}.txt`

    cn = log[/CHROMATIC NUMBER = \d+/]
    lbs = log.scan /NEW BEST LOWER BOUND = \d+/
    ubs = log.scan /NEW BEST UPPER BOUND = \d+/
    maxLb = lbs.map {|b| b.slice! 'NEW BEST LOWER BOUND = '; b} .map(&:to_i).max
    minUb = ubs.map {|b| b.slice! 'NEW BEST UPPER BOUND = '; b} .map(&:to_i).min

    print "GRAPH #{i} (phase 1):  "
    if cn
      cn.slice! 'CHROMATIC NUMBER = '
      puts "chromatic number: #{cn}"
    else
      puts "range: #{maxLb} - #{minUb}"
    end
  end
end

if PHASE_3
  puts "\n" if PHASE_1
  
  (1..20).each do |i|
    # 20s each
    log = `gtimeout #{TIMEOUT} java -Xms1024m -Xmx1024m -jar Group26.jar phase3/graphs/graph#{i.to_s.rjust(2, ?0)}.txt`

    cn = log[/CHROMATIC NUMBER = \d+/]
    lbs = log.scan /NEW BEST LOWER BOUND = \d+/
    ubs = log.scan /NEW BEST UPPER BOUND = \d+/
    maxLb = lbs.map {|b| b.slice! 'NEW BEST LOWER BOUND = '; b} .map(&:to_i).max
    minUb = ubs.map {|b| b.slice! 'NEW BEST UPPER BOUND = '; b} .map(&:to_i).min

    print "GRAPH #{i} (phase 1):  "
    if cn
      cn.slice! 'CHROMATIC NUMBER = '
      puts "chromatic number: #{cn}"
    else
      puts "range: #{maxLb} - #{minUb}"
    end
  end
end