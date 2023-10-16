# Cloud-Based Analysis of Large-Scale Hyperspectral Imagery for Oil Spill Detection
The Code for "Cloud-Based Analysis of Large-Scale Hyperspectral Imagery for Oil Spill Detection". []
```
J. M. Haut, S. Moreno-Álvarez, R. Pastor-Vargas, A. Perez-Garcia and M. E. Paoletti
Cloud-Based Analysis of Large-Scale Hyperspectral Imagery for Oil Spill Detection.
DOI: ,
October 2023.
```

![CLOUDNDOI](./images/CLOUDNDOI.jpg)


### Compile Java project

## Local version
```
cd ./local_version
mvn clean compile assembly:single
mvn clean compile package
```

## Distributed version
```
cd ./distributed_version
mvn clean compile assembly:single
mvn clean compile package
```


### Run code

## Local version
```
#!/bin/bash
java  -Xms14096m -Xms14096m -Xmx15144m -jar archivo.jar

```
## Distributed version
First, you need to upload the compiled file, the Pig script, and the data to Amazon S3. Once this is done and you have confirmed that the paths match in both the Pig script and the execution script. Finally, execute the following code:
```
#!/bin/bash
sh run_pig_script.sh
```

