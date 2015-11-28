
##### Design Notes

* use scaldi composition to inject classes specified in ultronrc.conf sourced from external jars


##### ETL Design patterns

* for variables emitted in the tasks, those variables must be defined in the code before so that Typesafe Config doesnt remove them from the config since they are undefined.