package com.example.administrator.myapplication.fileupload;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.myapplication.R;


public class FileUploadActivity extends ActionBarActivity {
	//private Bitmap bitmapToFaceDet;

	private static RequestQueue mSingleQueue;
	private static String TAG = "test";

/*	private TextView mShowResponse;*/

	//private ProgressDialog mDialog;
	private static final int CMD_FACE_DET_RETURN=2;
	private static final int CMD_FACE_DET=1;
	private  HandlerThread mHandlerThread=new HandlerThread("mHandlerThread");
	private Handler threadHandler;
	Socket socket;

	private OutputStream socketOutputStream;
	private Handler mainHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case CMD_FACE_DET_RETURN:
					//showFaceDetResult(msg.obj==null?null:(FaceDetResult)msg.obj);
					break;
			}
		}
	};
	private Uri imgFileUri0;
	private Uri imgFileUri1;
	private ImageView iv0;
	private ImageView iv1;
	private RadioGroup radioGroupSelectImg;
	private RadioButton radioButtonSelectImg0;
	private RadioButton radioButtonSelectImg1;
	private Button button_sel_pic1;
	private Button button_sel_pic2;
	private Button button_face_det;
	Button button_compute_sim;
	private  static final int  REQUEST_CODE_SEL_PIC1=1;
	private  static final int  REQUEST_CODE_SEL_PIC2=2;
	private ContentLoadingProgressBar contentLoadingProgress;

	private void showFaceDetResult(int index,Uri imgFileUri,FaceDetResult faceDetResult){
		Bitmap bitmapToFaceDet=index==0?
				ImageResizer.decodeSampledBitmapFromImageFileUri(this,imgFileUri,iv0.getWidth(),iv0.getHeight()):
				ImageResizer.decodeSampledBitmapFromImageFileUri(this,imgFileUri,iv1.getWidth(),iv1.getHeight())
				;
		//bitmapToFaceDet
		Canvas canvas = new Canvas(bitmapToFaceDet);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		//canvas.drawLine(0, 0, bitmapToFaceDet.getWidth(), bitmapToFaceDet.getHeight(), paint);
		if(faceDetResult.getFace_num()>0){
			List<Face_Rect> faceRectList = faceDetResult.getFace_rectList();
			for (Face_Rect faceRect : faceRectList) {
				canvas.drawRect((float) faceRect.getLeft(),
						(float)faceRect.getTop(),
						(float)faceRect.getLeft()+faceRect.getWidth(),
						(float)faceRect.getTop()+faceRect.getHeight(),paint);
			}
			//canvas.drawBitmap(bitmapToFaceDet,0, 0, paint);
		}
		if(index==0){
			iv0.setImageBitmap(bitmapToFaceDet);
		}else if(index==1){
			iv1.setImageBitmap(bitmapToFaceDet);
		}


	}
	//2，选择图片，返回所选照片uri信息
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {//操作成功
			Uri imgFileUri = intent.getData();//获得所选照片的信息
			if(requestCode==REQUEST_CODE_SEL_PIC1){
				imgFileUri0=imgFileUri;
				showSelectedUriImage(imgFileUri0,iv0);
			}
			else if(requestCode==REQUEST_CODE_SEL_PIC2){
				imgFileUri0=imgFileUri;
				showSelectedUriImage(imgFileUri1,iv1);
			}

		}
	}
	private void showSelectedUriImage(@NonNull Uri imageFileUri,@NonNull ImageView iv)  {
		/*Display currentDisplay = getWindowManager().getDefaultDisplay();
		int dw = currentDisplay.getWidth();
		int dh = currentDisplay.getHeight();
		try
		{
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getContentResolver().
					openInputStream(imageFileUri), null,  bmpFactoryOptions);
			int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)dh);
			int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)dw);
			if (heightRatio > 1 && widthRatio > 1)
			{
				bmpFactoryOptions.inSampleSize =  heightRatio > widthRatio ? heightRatio:widthRatio;
			}
			bmpFactoryOptions.inJustDecodeBounds = false;
			bitmapToFaceDet = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);
			ivFace.setImageBitmap(bitmapToFaceDet);
		}
		catch (FileNotFoundException e)
		{
			Log.v("ERROR", e.toString());

		}*/
		Bitmap bitmap=ImageResizer.decodeSampledBitmapFromImageFileUri(this,imageFileUri,iv.getWidth(),iv.getHeight());
		iv.setImageBitmap(bitmap);



	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandlerThread.quit();
	}
	protected void connectServerWithTCPSocket() {

		try {// 创建一个Socket对象，并指定服务端的IP及端口号
			if(socket==null){
				socket = new Socket(Constant.SocketHost,Constant.SocketPort );
			}

			if(socketOutputStream==null){
				if(socket!=null&&!socket.isClosed()){
					socketOutputStream = socket.getOutputStream();
				}
			}



			/*// 创建一个InputStream用户读取要发送的文件。
			InputStream inputStream = new FileInputStream("e://a.txt");
			// 获取Socket的OutputStream对象用于发送数据。
			OutputStream outputStream = socket.getOutputStream();
			// 创建一个byte类型的buffer字节数组，用于存放读取的本地文件
			*/

			/** 或创建一个报文，使用BufferedWriter写入,看你的需求 **/
//     String socketData = "[;fjks;]";
//     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//         socket.getOutputStream()));
//     writer.write(socketData.replace("\n", " ") + "\n");
//     writer.flush();
			/************************************************/
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*private FaceDetResult faceDet() throws IOException {
		connectServerWithTCPSocket();
		if(socketOutputStream!=null){
			String img_base64=Base64Helper.bitmapToBase64(bitmapToFaceDet);
			String filemsg="{\"ID\":\"123456789\",\"ServiceType\":1,\"img\":\"+img_base64+\",\"img_type\":\"jpg\"}";
			byte buffer[] = filemsg.getBytes();
			socketOutputStream.write(buffer);
			socketOutputStream.flush();
			String faceDetResultStr= new String(readInputStream(socket.getInputStream()));
			return  fromFaceDetResultStr(faceDetResultStr);
		}
		return null;
	}*/
	private FaceDetResult fromFaceDetResultStr(String faceDetResultStr){
		JSONTokener jsonTokener=new JSONTokener(faceDetResultStr);
		try {
			JSONObject jsonObject=(JSONObject) jsonTokener.nextValue();
			String ID = jsonObject.getString("ID");
			int face_num = jsonObject.getInt("face_num");
			JSONArray face_rectArray = jsonObject.getJSONArray("face_rect");
			List<Face_Rect> face_rectList=new ArrayList<Face_Rect>();
			for (int i=0;i<face_rectArray.length();i++) {
				JSONObject jo= (JSONObject) face_rectArray.get(i);

				Face_Rect face_rect = new Face_Rect(jo.getInt("left"), jo.getInt("top"),  jo.getInt("width") , jo.getInt("height") );
				face_rectList.add(face_rect);
			}

			int errorcode =jsonObject.getInt("errorcode");
			return  new FaceDetResult(ID,face_num,face_rectList,errorcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  null;
	}

	public static byte[] readInputStream(InputStream ins) {
		if (ins == null) {
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(ins);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[128];
			int n = -1;
			while ((n = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bos.toByteArray();
	}




	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fileupload);
		contentLoadingProgress=(ContentLoadingProgressBar)findViewById(R.id.contentLoadingProgress);


		iv0=(ImageView)findViewById(R.id.iv0);
		iv1=(ImageView)findViewById(R.id.iv1);
		//bitmapToFaceDet= BitmapFactory.decodeResource(getResources(),R.mipmap.bg) ;
		radioGroupSelectImg=(RadioGroup)findViewById(R.id.radioGroupSelectImg);
		radioButtonSelectImg0=(RadioButton)findViewById(R.id.radioButtonSelectImg0);
		radioButtonSelectImg1=(RadioButton)findViewById(R.id.radioButtonSelectImg1);
		//mHandlerThread.start();


		Looper looper = mHandlerThread.getLooper();
		threadHandler = new Handler(looper){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what){
					case  CMD_FACE_DET:
						//try {
							//FaceDetResult result=faceDet();
							/*Message msgToMainThread=Message.obtain(mainHandler,CMD_FACE_DET_RETURN,result);
							msgToMainThread.sendToTarget();*/
						/*} catch (IOException e) {
							e.printStackTrace();
						}*/
						break;
				}



			}
		};


		/*mShowResponse = (TextView) findViewById(R.id.id_show_response) ;
		mImageView = (ImageView) findViewById(R.id.id_show_img) ;
		mDialog = new ProgressDialog(this) ;
		mDialog.setCanceledOnTouchOutside(false);*/
//		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());
		mSingleQueue = Volley.newRequestQueue(getApplicationContext());
		button_compute_sim = (Button) findViewById(R.id.button_compute_sim);
		button_compute_sim.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doComputeSim(v);
			}
		});
		button_sel_pic1= (Button) findViewById(R.id.button_sel_pic1);
		button_sel_pic1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,

				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//启动照片Gallery
				startActivityForResult(intent, REQUEST_CODE_SEL_PIC1);


			}
		});
		button_sel_pic2= (Button) findViewById(R.id.button_sel_pic2);
		button_sel_pic2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//启动照片Gallery
				startActivityForResult(intent, REQUEST_CODE_SEL_PIC2);

			}
		});


		button_face_det= (Button) findViewById(R.id.button_face_det);
		button_face_det.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Message msg=Message.obtain(threadHandler);
				msg.what=CMD_FACE_DET;
				msg.sendToTarget();*/
				try {
					if(radioGroupSelectImg.getCheckedRadioButtonId()==R.id.radioButtonSelectImg0){
						doFaceDet(0,imgFileUri0);
					}else if(radioGroupSelectImg.getCheckedRadioButtonId()==R.id.radioButtonSelectImg1){
						doFaceDet(1,imgFileUri1);
					}

				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	public void uploadImg(View view){
	/*	mDialog.setMessage("图片上传中...");
		mDialog.show();*/
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.bg) ;
		UploadApi.uploadImg(
				this,
				bitmap,Constant.ComputeSimUrl,
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				response = response.substring(response.indexOf("img src="));
				response = response.substring(8,response.indexOf("/>")) ;
				Log.v("zgy","===========onResponse========="+response) ;
			/*	mShowResponse.setText("图片地址:\n"+response);
				mDialog.dismiss();
				Toast.makeText(FileUploadActivity.this,"上传成功",Toast.LENGTH_SHORT).show();*/
			}
		},new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Log.v("zgy","===========VolleyError========="+volleyError) ;
						/*mShowResponse.setText("ErrorResponse\n"+error.getMessage());
						Toast.makeText(FileUploadActivity.this,"上传失败",Toast.LENGTH_SHORT).show() ;
						mDialog.dismiss();*/
					}
				}) ;
	}
	//计算相似度
	private void doComputeSim(final View view){
		if(imgFileUri0==null){
			showSnackbar("请选择图片1",view);
			return;

		}
		if(imgFileUri1==null){
			showSnackbar("请选择图片2",view);
			return;
		}

		//boolean bok = ping();
		//String path = "/mnt/sdcard/0/test.jpg";
		String url = Constant.ComputeSimUrl; //换成自己的测试url地址
		Map<String, String> params = new HashMap<String, String>();
		/*params.put("id", "19");
		params.put("type", "shop");*/
		/*File f1 = new File(path);
		File f2 = new File(path);*/
	/*	Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.mipmap.bg) ;
		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.mipmap.bg);*/

		/*if(!f1.exists()){
			Toast.makeText(getApplicationContext(), "图片不存在，测试无效", Toast.LENGTH_SHORT).show();
			return;
		}*/
		//List<File> f = new ArrayList<File>();
		List<Bitmap> bitmaps=new ArrayList<Bitmap>();
		Bitmap bitmap1=ImageResizer.decodeSampledBitmapFromImageFileUri(this,imgFileUri0,iv0.getWidth(),iv0.getHeight());
		Bitmap bitmap2=ImageResizer.decodeSampledBitmapFromImageFileUri(this,imgFileUri0,iv1.getWidth(),iv1.getHeight());
		bitmaps.add(bitmap1);
		bitmaps.add(bitmap2);
		MultipartRequest request = new MultipartRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				contentLoadingProgress.hide();
				//Toast.makeText(getApplicationContext(), "uploadSuccess,response = " + response, Toast.LENGTH_SHORT).show();

				if(response!=null){
					JSONTokener jsonTokener=new JSONTokener(response);
					try {
						JSONObject jsonObject=(JSONObject) jsonTokener.nextValue();
						boolean matching = jsonObject.getBoolean("matching");
						showSnackbar("比对结果:相似度"+(matching?"达标":"未达标"),view);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Log.i("YanZi", "success,response = " + response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				contentLoadingProgress.hide();
				Toast.makeText(getApplicationContext(), "uploadError,response = " + error.getMessage(), Toast.LENGTH_SHORT).show();
				Log.i("YanZi", "error,response = " + error.getMessage());				
			}
		}, "imgs", params,bitmaps); //注意这个key必须是f_file[],后面的[]不能少
		mSingleQueue.add(request);
		contentLoadingProgress.show();
	}
	private static final boolean ping() {

		String result = null;

		try {

			//String ip = "192.168.1.101";// 除非百度挂了，否则用这个应该没问题~
			String ip = "10.0.2.2";// 除非百度挂了，否则用这个应该没问题~
			//String ip = "www.baidu.com";// 除非百度挂了，否则用这个应该没问题~

			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping3次


// 读取ping的内容，可不加。

			InputStream input = p.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(input));

			StringBuffer stringBuffer = new StringBuffer();

			String content = "";

			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			Log.i("TTT", "result content : " + stringBuffer.toString());

// PING的状态

			int status = p.waitFor();

			if (status == 0) {

				result = "successful~";

				return true;

			} else {

				result = "failed~ cannot reach the IP address";

			}

		} catch (IOException e) {

			result = "failed~ IOException";

		} catch (InterruptedException e) {

			result = "failed~ InterruptedException";

		} finally {

			Log.i("TTT", "result = " + result);

		}

		return false;

	}

	private void doFaceDet(final int index, final Uri imgFileUri) throws JSONException {
		String url = Constant.FaceDetUrl;
		JSONObject jsonRequest=new JSONObject();
		String img_base64=Base64Helper.bitmapToBase64(this,imgFileUri);
		jsonRequest.put("id","123456789");
		jsonRequest.put("serviceType",1);
		jsonRequest.put("img",img_base64);
		jsonRequest.put("img_type","jpg");
		/*private String id;
		private int serviceType;
		private String img;
		private String img_type;*/
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST,
				url,
				jsonRequest,
				new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				showSnackbar("人脸检测返回",button_face_det);
				contentLoadingProgress.hide();
				showFaceDetResult(index,imgFileUri,fromFaceDetResultJsonObject(jsonObject));
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				contentLoadingProgress.hide();
				showSnackbar(volleyError.getMessage(),button_face_det);

			}
		});
		mSingleQueue.add(jsonObjectRequest);
		contentLoadingProgress.show();



	}
	private FaceDetResult fromFaceDetResultJsonObject(JSONObject jsonObject){

		try {
			String id = jsonObject.getString("id");
			int face_num = jsonObject.getInt("face_num");
			JSONArray face_rectArray = jsonObject.getJSONArray("face_rect");
			List<Face_Rect> face_rectList=new ArrayList<Face_Rect>();
			for (int i=0;i<face_rectArray.length();i++) {
				JSONObject jo= (JSONObject) face_rectArray.get(i);

				Face_Rect face_rect = new Face_Rect(jo.getInt("left"), jo.getInt("top"),  jo.getInt("width") , jo.getInt("height") );
				face_rectList.add(face_rect);
			}

			int errorcode =jsonObject.getInt("errorcode");
			return  new FaceDetResult(id,face_num,face_rectList,errorcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  null;
	}
	private  void showSnackbar(@NonNull CharSequence text , View view){
		final Snackbar snackbar= Snackbar.make(
				view,
				text,
				Snackbar.LENGTH_SHORT);

		snackbar.setAction("隐藏",
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						snackbar.dismiss();
					}
				});
		snackbar.show();
	}

}
