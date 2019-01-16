PHASE_1 = true
PHASE_3 = true
TIMEOUT = 20 # seconds

def general title, folder
  puts title
  puts "name       CN |  LB ->  UB"
  
  table = (1..20).map do |i|
    # 20s each
    log = `gtimeout #{TIMEOUT} java -Xms1024m -Xmx1024m -jar Group26.jar phase3/#{folder}/graph#{i.to_s.rjust(2, ?0)}.txt`

    cn = log[/CHROMATIC NUMBER = \d+/]
    lbs = log.scan /NEW BEST LOWER BOUND = \d+/
    ubs = log.scan /NEW BEST UPPER BOUND = \d+/
    maxLb = lbs.map {|b| b.slice! 'NEW BEST LOWER BOUND = '; b} .map(&:to_i).max
    minUb = ubs.map {|b| b.slice! 'NEW BEST UPPER BOUND = '; b} .map(&:to_i).min
    
    if cn
      cn.slice! 'CHROMATIC NUMBER = '
    else
      lo, hi = maxLb, minUb
    end
    
    print "Graph #{i.to_s.rjust 2}: "
    print (cn&.to_s || ' ').rjust(3) + ' | '
    print "#{lo.to_s.rjust 3} -> #{hi.to_s.rjust 3}" if lo
    puts
  end
end

if PHASE_1
  general 'GRAPHS PHASE 1', 'graphs_phase1'
end

if PHASE_3
  puts "\n" if PHASE_1
  
  general 'GRAPHS PHASE 3', 'graphs'
end