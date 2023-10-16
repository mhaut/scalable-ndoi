#!/bin/bash
nodes=1
files=("file16GB" "file8GB" "file4GB" "file1GB")
# Run PiG and get function times by PIG Counters (need job ID)
for name in "${files[@]}"
do
  echo "$name"
  for N in {1..5}
  do
     log_name="logs/execution_nodes${nodes}_${name}_${N}.log"
     echo "$log_name"
     # SET file name for code/calculation_emr_3_time.pig SCRIPT
     # Do not forget to change this path 's3://scalable-ndoi-test1/code/calculation.jar' to your Amazon S3 path.
     input_file="s3://scalable-ndoi-test1/input/${name}.csv"
     echo "$input_file"
     time pig -Dpig.udf.profile=true -param input=$input_file -x mapreduce code/calculation_emr_3_time.pig > $log_name
     job_id="`cat $log_name| grep -m 1 -o 'job_[0-9]*_[0-9]*'`"
     echo $job_id
     job_status_log="logs/job_status_nodes${nodes}_${name}_${N}.log"
     echo $job_status_log
     mapred job -status $job_id > $job_status_log
  done
done
