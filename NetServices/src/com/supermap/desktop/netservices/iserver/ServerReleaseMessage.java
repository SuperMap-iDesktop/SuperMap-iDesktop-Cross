package com.supermap.desktop.netservices.iserver;

import com.supermap.desktop.Application;

class ServerReleaseMessage {
	 static String ZippingData;
     static String ZipDataCurrent;
     static String ZipDataCompleted;
     static String ZipDataFailed;
     static String PreUploading;
     static String Uploading;
     static String UploadCurrent;
     static String UploadCompleted;
     static String Releasing;
     static String ReleaseCompleted;
     static String UploadSpeedUnit;
     static String Unknown;

     static void outputMessage(String message)
     {
         Application.getActiveApplication().getOutput().output(message);
     }

     static
     {
		// ZippingData = Properties.NetServicesResources.String_ZippingData;
		// ZipDataCurrent = Properties.NetServicesResources.String_ZipDataInfo;
		// ZipDataCompleted = Properties.NetServicesResources.String_ZipCompleted;
		// ZipDataFailed = Properties.NetServicesResources.String_ZipDataFailed;
		// PreUploading = Properties.NetServicesResources.String_PreUploading;
		// Uploading = Properties.NetServicesResources.String_Uploading;
		// UploadCurrent = Properties.NetServicesResources.String_UploadingInfo;
		// //UploadCurrent = "已上传 [{0}/{1}]";
		// UploadCompleted = Properties.NetServicesResources.String_UploadCompleted;
		// Releasing = Properties.NetServicesResources.String_Releasing;
		// ReleaseCompleted = Properties.NetServicesResources.String_ReleaseCompleted;
		// UploadSpeedUnit = Properties.NetServicesResources.String_UploadSpeedUnit;
		// Unknown = Properties.NetServicesResources.String_Unknown;
     }
}
