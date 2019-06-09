# GenomeMedian

#### GGHPMain 
solve Guided Genome Halving problem

cli options: 

          -o,--ord <arg>      path to ordinary genome B

          -p,--restricted     solve restricted problem, default = false
          -r,--result <arg>   resulted path
          -t,--time <arg>     Time limit for solving problem (in seconds), default
                              60*60*2
          -w,--wgd <arg>      path to duplicated genome A
          -z,--dir <arg>      Solve all problems from dir (use instead o, w, p)

          
 example:
`java  -classpath target/classes:lib/* GGHPMain -o yeasts_tannier/36/ord.gen  -w yeasts_tannier/36/wgd.gen -r resultedPath` 
