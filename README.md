# GenomeMedian

#### GGHPMain 
solve Guided Genome Halving Problem

cli options: 

          -o,--ord <arg>      Path to ordinary genome B
          -w,--wgd <arg>      Path to duplicated genome A
          -r,--result <arg>   Resulted path
          -t,--time <arg>     Time limit for solving problem (in seconds), default 60*60*2
          -p,--restricted     Flag for using restricted model, default = false
          -z,--dir <arg>      Solve all problems from dir (use instead o, w, p)

          
 example:
 
`java  -classpath target/classes:lib/* GGHPMain -o yeasts_tannier/36/ord.gen  -w yeasts_tannier/36/wgd.gen -r resultedPath` 


#### GAPMain 
solve Genome Aliquoting Problem (m<4)

cli options:
 
        -g,--genome <arg>   Path to source genome
        -r,--result <arg>   Resulted path
        -p,--restricted     Flag for using restricted model, default = false
        -t,--time <arg>     Time limit for solving problem (in seconds), default 60*60*2
        
 example:
 
`java -classpath target/classes:lib/* GAPMain -g yeasts_tannier/36/wgd.gen -r resultedPath -t 60 ` 
