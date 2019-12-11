# GenomeMedian

#### Run 
solve Guided Genome Halving Problem (GGHP), Genome Aliquoting Problem m=3 (GAP3)

cli options: 

         -g,--genome <arg>   Path to source (main) genome
         -b,--ord <arg>      Path to ordinary genome B (for GGHP only)
         -o,--output <arg>   Resulted path
         -r,--restricted     Flag for using restricted model, default = false
         -t,--time <arg>     Time limit for solving problem (in seconds), default 60*60*2
         -w,--dir <arg>      Solve all problems from dir (use instead g, [b], o) (for GGHP only)
         
 example:
 
`java11 -classpath target/classes:lib/* Run -g data/yeasts_tannier/36/wgd.gen -b data/yeasts_tannier/36/ord.gen -o resultedPath`
 