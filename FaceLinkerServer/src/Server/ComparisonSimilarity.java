package Server;

import Luxand.*; 
import Luxand.FSDK.FSDK_FaceTemplate;
import Luxand.FSDK.FSDK_FaceTemplate.ByReference;
import Luxand.FSDK.*; 
import Luxand.FSDKCam.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.jna.Structure;

public class ComparisonSimilarity {
	private static final String[] ERRORCODE = 
	{
		"FSDKE_OK",
		"FSDKE_FAILED",
		"FSDKE_NOT_ACTIVATED",
		"FSDKE_OUT_OF_MEMORY",
		"FSDKE_INVALID_ARGUMENT",
		"FSDKE_IO_ERROR",
		"FSDKE_IMAGE_TOO_SMALL",
		"FSDKE_FACE_NOT_FOUND",
		"FSDKE_INSUFFICIENT_BUFFER_SIZE",	
		"FSDKE_UNSUPPORTED_IMAGE_EXTENSION",
		"FSDKE_CANNOT_OPEN_FILE",
		"FSDKE_CANNOT_CREATE_FILE",
		"FSDKE_BAD_FILE_FORMAT",
		"FSDKE_FILE_NOT_FOUND",
		"FSDKE_CONNECTION_CLOSED",
		"FSDKE_CONNECTION_FAILED",
		"FSDKE_IP_INIT_FAILED",
		"FSDKE_NEED_SERVER_ACTIVATION",
		"FSDKE_ID_NOT_FOUND",
		"FSDKE_ATTRIBUTE_NOT_DETECTED",
		"FSDKE_INSUFFICIENT_TRACKER_MEMORY_LIMIT",
		"FSDKE_UNKNOWN_ATTRIBUTE",
		"FSDKE_UNSUPPORTED_FILE_VERSION",
		"FSDKE_SYNTAX_ERROR",
		"FSDKE_PARAMETER_NOT_FOUND",
		"FSDKE_INVALID_TEMPLATE",
		"FSDKE_UNSUPPORTED_TEMPLATE_VERSION"
	};
	public static final String IMG_DIR = "/home/saya/Project/FLImages/profile";
	public static final String KEY = Key.KEY;
	
	// get similar images
	public static void getSimilarImage(ArrayList<ImageSimilarity> imageArr, String sample){
		HImage imgOrg = new HImage();
		HImage imgCmp = new HImage();
		TFacePosition.ByReference fpOrg = new TFacePosition.ByReference();
		TFacePosition.ByReference fpCmp = new TFacePosition.ByReference();
		FSDK_FaceTemplate.ByReference ftOrg = new FSDK_FaceTemplate.ByReference();
		FSDK_FaceTemplate.ByReference ftCmp = new FSDK_FaceTemplate.ByReference();

		int result;
		float[] similarity = new float[1];
		String pair = null;
		int nFile;
		String[] pics = null;
		String temp = null;
		
		int user_id = Integer.parseInt(sample.substring(0, sample.indexOf('_') - 1));
		
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter("BadPicsPair.txt"));
			
			pics = getFileNames();
			
			initFaceSDK();
			
			FSDK.SetFaceDetectionParameters(false, false, 500);
			
			for(int i = 0 ; i < pics.length ; ++i){
				if(pics[i].compareTo(IMG_DIR + "/" + Integer.toString(user_id) + "_profile.jpg") == 0){
					temp = pics[0];
					pics[0] = pics[i];
					pics[i] = temp;
				}
			}
			
			// load file of user
			result = FSDK.LoadImageFromFile(imgOrg, "/home/saya/Project/FLImages/ideal_type/" + sample);
			if(result != FSDK.FSDKE_OK)
			{
				return;
			}
			result = FSDK.DetectFace(imgOrg, fpOrg);
			if(result != FSDK.FSDKE_OK)
			{
				return;
			}
			result = FSDK.GetFaceTemplateInRegion(imgOrg, fpOrg, ftOrg);
			if(result != FSDK.FSDKE_OK)
			{
				return; 
			}
			for(int i = 1 ; i < pics.length ; ++i)
			{
				// load 1 image
				if(pics[i].contains("_profile.jpg"))
					result = FSDK.LoadImageFromFile(imgCmp, pics[i]);
				else
					continue;
				// find face
				result = FSDK.DetectFace(imgCmp, fpCmp);
				if(result != FSDK.FSDKE_OK)
				{
					if(result != FSDK.FSDKE_OK) writer.write(pics[i]+"\n");

					FSDK.FreeImage(imgCmp);
					break;
				}
			
				FSDK.GetFaceTemplateInRegion(imgCmp, fpCmp, ftCmp);
				
				if(result != FSDK.FSDKE_OK)
				{
					System.out.println("============= break this comparison =============");
					System.out.println(pics[i]+"\n");
					FSDK.FreeImage(imgCmp);
					break;
				}

				FSDK.MatchFaces(ftOrg, ftCmp, similarity);
			
				// save image when similarity is more than 70%
				if(similarity[0] * 100 > 70.0f) imageArr.add(new ImageSimilarity(pics[i],similarity[0]  * 100));
				FSDK.FreeImage(imgCmp);
			}
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void compareWithOneSample(String sample){
		HImage imgOrg = new HImage();
		HImage imgCmp = new HImage();
		TFacePosition.ByReference fpOrg = new TFacePosition.ByReference();
		TFacePosition.ByReference fpCmp = new TFacePosition.ByReference();
		FSDK_FaceTemplate.ByReference ftOrg = new FSDK_FaceTemplate.ByReference();
		FSDK_FaceTemplate.ByReference ftCmp = new FSDK_FaceTemplate.ByReference();
		int result;
		float[] similarity = new float[1];
		float maxSim = 0.0f;
		String pair = null;
		int nFile;
		String[] pics = null;
		String temp = null;
		
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter("BadPicsPair.txt"));
			
			pics = getFileNames();
			
			initFaceSDK();
			
			FSDK.SetFaceDetectionParameters(false, false, 500);
			
			for(int i = 0 ; i < pics.length ; ++i){
				if(pics[i].compareTo(IMG_DIR + sample) == 0){
					temp = pics[0];
					pics[0] = pics[i];
					pics[i] = temp;
				}
			}
			
			result = FSDK.LoadImageFromFile(imgOrg, pics[0]);
			if(result != FSDK.FSDKE_OK)
			{
				System.out.println("============= Input Sample is Bad ============\n");
				return;
			}
			result = FSDK.DetectFace(imgOrg, fpOrg);
			if(result != FSDK.FSDKE_OK)
			{
				System.out.println("============= Input Sample is Bad ============\n");
				return;
			}
			result = FSDK.GetFaceTemplateInRegion(imgOrg, fpOrg, ftOrg);
			if(result != FSDK.FSDKE_OK)
			{
				System.out.println("============= Input Sample is Bad ============\n");
				return;
			}
			for(int i = 1 ; i < pics.length ; ++i)
			{
				// load 1 img
				result = FSDK.LoadImageFromFile(imgCmp, pics[i]);
			
				// find face
				result = FSDK.DetectFace(imgCmp, fpCmp);
				if(result != FSDK.FSDKE_OK)
				{
					System.out.println("============= break this comparison =============\n");
					if(result != FSDK.FSDKE_OK) writer.write(pics[i]+"\n");

					FSDK.FreeImage(imgCmp);
					break;
				}
			
				FSDK.GetFaceTemplateInRegion(imgCmp, fpCmp, ftCmp);
				
				if(result != FSDK.FSDKE_OK)
				{
					System.out.println("============= break this comparison =============");
					System.out.println(pics[i]+"\n");
					FSDK.FreeImage(imgCmp);
					break;
				}

				FSDK.MatchFaces(ftOrg, ftCmp, similarity);
			
				System.out.println("=================== Similarity ==================");
				System.out.println("Similarity is " + similarity[0] * 100 + "%\n");

				if(similarity[0] >= maxSim)
				{
					maxSim = similarity[0];
					
					pair = pics[i];
				}
				FSDK.FreeImage(imgCmp);
			}
			writer.close();
			
			System.out.println("===================== Result ====================");
			System.out.println("Total " + pics.length + " files");
			System.out.println("Max Similarity: " + maxSim * 100 + "%");
			
			System.out.println(pics[0]);
			System.out.println(pair);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
		
	// get all file names 
	public static String[] getFileNames()
	{
		String[] files = null;
		
		File dirFile = new File(IMG_DIR);
		File []fileList = dirFile.listFiles();
		
		files = new String[fileList.length];
		
		for(int i = 0 ; i < fileList.length ; ++i){
			files[i] = fileList[i].getParent() + "/" + fileList[i].getName();
		}
		
		return files;
	}
	
	public static int initFaceSDK()
	{
		int ret = 0;
		ret = FSDK.ActivateLibrary(KEY);

		FSDK.Initialize();

		return ret;
	}
}
