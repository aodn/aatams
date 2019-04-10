#!/bin/bash
echo `date`
# cd /home/xhoenner/QC/; ## On Nectar VM
cd /Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/ ## On local machine

echo @@@@@@@@ Download all detections and transmitters @@@@@@@@
	Rscript DownloadDetections.R;

echo @@@@@@@@ Run QC tests @@@@@@@@ 
	Rscript QC.R;

echo @@@@@@@@ Summarise QC results @@@@@@@@
	Rscript TechnicalValidation/Summarise_QCOutput.R;
	Rscript TechnicalValidation/Plot_QCOutput.R;
