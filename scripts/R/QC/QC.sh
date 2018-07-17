#!/bin/bash
echo `date`
cd /home/xhoenner/QC/;

echo @@@@@@@@ Download all detections and transmitters @@@@@@@@
	/usr/bin/Rscript DownloadDetections.R;

echo @@@@@@@@ Run QC tests @@@@@@@@ 
	/usr/bin/Rscript QC.R;

echo @@@@@@@@ Summarise QC results @@@@@@@@
	/usr/bin/Rscript TechnicalValidation/Summarise_QCOutput.R;
	/usr/bin/Rscript TechnicalValidation/Plot_QCOutput.R;
