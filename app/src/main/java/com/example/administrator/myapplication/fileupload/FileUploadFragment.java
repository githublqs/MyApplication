package com.example.administrator.myapplication.fileupload;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.pullableview.PullToRefreshLayout;
import com.example.administrator.myapplication.pullableview.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lqs on 2016/8/23.
 */
public class FileUploadFragment extends Fragment {
    private static final String TAG = "UploadFaceListFragment";
    private static final int REQUEST_CODE_SEL_PIC1 = 1;
    private static final int REQUEST_CODE_SEL_PIC2 = 2;
    private Uri imgFileUri0;
    private Uri imgFileUri1;
    private Uri imgFileUri;
    private OnFragmentInteractionListener mListener;
    private Dialog pDialog;
    private ContentLoadingProgressBar contentLoadingProgress;
    private ImageView iv0;
    private ImageView iv1;
    private RadioGroup radioGroupSelectImg;
    private RadioButton radioButtonSelectImg0;
    private RadioButton radioButtonSelectImg1;
    private RequestQueue mSingleQueue;
    private Button button_compute_sim;
    private Button button_sel_pic1;
    private Button button_sel_pic2;
    private Button button_face_det;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        FileUploadFragment fragment = new FileUploadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fileupload, container, false);

        contentLoadingProgress = (ContentLoadingProgressBar) view.findViewById(R.id.contentLoadingProgress);
        iv0 = (ImageView) view.findViewById(R.id.iv0);
        iv1 = (ImageView) view.findViewById(R.id.iv1);
        radioGroupSelectImg = (RadioGroup) view.findViewById(R.id.radioGroupSelectImg);
        radioButtonSelectImg0 = (RadioButton) view.findViewById(R.id.radioButtonSelectImg0);
        radioButtonSelectImg1 = (RadioButton) view.findViewById(R.id.radioButtonSelectImg1);



        button_compute_sim = (Button)  view.findViewById(R.id.button_compute_sim);
        button_compute_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doComputeSim(v);
            }
        });
        button_sel_pic1 = (Button) view.findViewById(R.id.button_sel_pic1);
        button_sel_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicWhereDialog(REQUEST_CODE_SEL_PIC1);


            }
        });
        button_sel_pic2 = (Button) view.findViewById(R.id.button_sel_pic2);
        button_sel_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicWhereDialog(REQUEST_CODE_SEL_PIC2);
            }
        });


        button_face_det = (Button) view.findViewById(R.id.button_face_det);
        button_face_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (radioGroupSelectImg.getCheckedRadioButtonId() == R.id.radioButtonSelectImg0) {
                        if (imgFileUri0 != null) {
                            doFaceDet(0, imgFileUri0);
                        } else {
                            showSnackbar("图片0不能为空", v);
                            return;
                        }
                    } else if (radioGroupSelectImg.getCheckedRadioButtonId() == R.id.radioButtonSelectImg1) {
                        if (imgFileUri1 != null) {
                            doFaceDet(1, imgFileUri1);
                        } else {
                            showSnackbar("图片1不能为空", v);
                            return;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将 getArguments() 中的取出赋给成员
        mSingleQueue = VolleyController.getInstance(getContext()).getRequestQueue();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }
    private void showPDialog() {
        if (pDialog == null){
            pDialog = CustomProgressDialog.createLoadingDialog(getActivity(),"Loading...");
        }
        //pDialog.setMessage("Loading...");
        pDialog.show();
    }
    private void hidePDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
        pDialog=null;
    }

    private void showFaceDetResult(int index, Uri imgFileUri, FaceDetResult faceDetResult) {
        Bitmap bitmapToFaceDet = index == 0 ?
                ImageResizer.decodeSampledBitmapFromFilePath(UriResolver.getPath(getContext(), imgFileUri), iv0.getWidth(), iv0.getHeight()) :
                ImageResizer.decodeSampledBitmapFromFilePath(UriResolver.getPath(getContext(), imgFileUri), iv1.getWidth(), iv1.getHeight());

        Bitmap bitmapToFaceDetCpy=bitmapToFaceDet.copy(Bitmap.Config.ARGB_8888,true);
        //bitmapToFaceDet
        Canvas canvas = new Canvas(bitmapToFaceDetCpy);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        //canvas.drawLine(0, 0, bitmapToFaceDet.getWidth(), bitmapToFaceDet.getHeight(), paint);
        showSnackbar("检测到"+faceDetResult.getFace_num()+"张人脸", button_face_det);
        if (faceDetResult.getFace_num() > 0) {
            List<Face_Rect> faceRectList = faceDetResult.getFace_rectList();
            for (Face_Rect faceRect : faceRectList) {
                canvas.drawRect((float) faceRect.getLeft(),
                        (float) faceRect.getTop(),
                        (float) faceRect.getLeft() + faceRect.getWidth(),
                        (float) faceRect.getTop() + faceRect.getHeight(), paint);
            }
            //canvas.drawBitmap(bitmapToFaceDet,0, 0, paint);
        }
        if (index == 0) {
            iv0.setImageBitmap(bitmapToFaceDetCpy);
        } else if (index == 1) {
            iv1.setImageBitmap(bitmapToFaceDetCpy);
        }


    }

    //2，选择图片，返回所选照片uri信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {//操作成功
            if (requestCode == REQUEST_CODE_SEL_PIC1) {
                imgFileUri0 = getImageUri(intent);
                showSelectedUriImage(imgFileUri0, iv0);
            } else if (requestCode == REQUEST_CODE_SEL_PIC2) {
                imgFileUri1 = getImageUri(intent);
                showSelectedUriImage(imgFileUri1, iv1);
            }
        }
    }



    private Uri getImageUri(Intent data) {
        Uri uri = null;
        if (data != null && data.getData() != null) {
            uri = data.getData();
        }


// 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
        if (uri == null) {
            if (imgFileUri != null) {
                uri = imgFileUri;
            }
        }
        return uri;

    }


    private void showSelectedUriImage(@NonNull Uri imageFileUri, @NonNull ImageView iv) {

        Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFilePath(UriResolver.getPath(getContext(), imageFileUri), iv.getWidth(), iv.getHeight());
        iv.setImageBitmap(bitmap);
    }
    private FaceDetResult fromFaceDetResultStr(String faceDetResultStr) {
        JSONTokener jsonTokener = new JSONTokener(faceDetResultStr);
        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            String ID = jsonObject.getString("ID");
            int face_num = jsonObject.getInt("face_num");
            JSONArray face_rectArray = jsonObject.getJSONArray("face_rect");
            List<Face_Rect> face_rectList = new ArrayList<Face_Rect>();
            for (int i = 0; i < face_rectArray.length(); i++) {
                JSONObject jo = (JSONObject) face_rectArray.get(i);

                Face_Rect face_rect = new Face_Rect(jo.getInt("left"), jo.getInt("top"), jo.getInt("width"), jo.getInt("height"));
                face_rectList.add(face_rect);
            }

            int errorcode = jsonObject.getInt("errorcode");
            return new FaceDetResult(ID, face_num, face_rectList, errorcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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

    private void showChoosePicWhereDialog(final int requestCode) {
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(getContext())
                .setTitle("选择图片来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == Constant.SELECT_PICTURE) {
                            startActivityForResult(Intent.createChooser(UriResolver.getImageOrCaptureIntent(getContext(),
                                    null, Constant.SELECT_PICTURE),
                                    "选择图片"), requestCode);
                        } else {
                            startActivityForResult(UriResolver.getImageOrCaptureIntent(getContext(),
                                    new UriResolver.ExtraOutputUriCallBack() {
                                        @Override
                                        public void onPutExtra(Uri imageUri) {
                                            imgFileUri = imageUri;
                                        }
                                    }, Constant.SELECT_CAMER), requestCode);
                        }
                    }
                })
                .create().show();
    }

    //计算相似度
    private void doComputeSim(final View view) {
        if (imgFileUri0 == null) {
            showSnackbar("请选择图片1", view);
            return;

        }
        if (imgFileUri1 == null) {
            showSnackbar("请选择图片2", view);
            return;
        }
        String url = Constant.ComputeSimUrl; //换成自己的测试url地址
        Map<String, String> params = new HashMap<String, String>();
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        Bitmap bitmap1 = ImageResizer.decodeSampledBitmapFromFilePath(UriResolver.getPath(getContext(), imgFileUri0), iv0.getWidth(), iv0.getHeight());
        Bitmap bitmap2 = ImageResizer.decodeSampledBitmapFromFilePath(UriResolver.getPath(getContext(), imgFileUri1), iv1.getWidth(), iv1.getHeight());
        bitmaps.add(bitmap1);
        bitmaps.add(bitmap2);
        MultipartRequest request = new MultipartRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoadingProgress();
                //Toast.makeText(getApplicationContext(), "uploadSuccess,response = " + response, Toast.LENGTH_SHORT).show();

                if (response != null) {
                    JSONTokener jsonTokener = new JSONTokener(response);
                    try {
                        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                        String matching = jsonObject.getString("matching");
                        showSnackbar("比对结果:相似度" + ("yes".equals(matching) ? "达标" : "未达标"), view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("YanZi", "success,response = " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSingleQueue.cancelAll("doComputeSim");
                hideLoadingProgress();
                //Toast.makeText(getApplicationContext(), "uploadError,response = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                showSnackbar("网络或服务器错误",button_compute_sim);

            }
        }, "imgs", params, bitmaps); //注意这个key必须是f_file[],后面的[]不能少
        request.setTag("doComputeSim");
        mSingleQueue.add(request);
        showLoadingProgress();
    }


    private void doFaceDet(final int index, final Uri imgFileUri) throws JSONException {
        String url = Constant.FaceDetUrl;
        JSONObject jsonRequest = new JSONObject();
        String img_base64 = Base64Helper.bitmapToBase64(getContext(), imgFileUri);
        jsonRequest.put("id", "123456789");
        jsonRequest.put("serviceType", 1);
        jsonRequest.put("img", img_base64);
        jsonRequest.put("img_type", "jpg");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        hideLoadingProgress();
                        showFaceDetResult(index, imgFileUri, fromFaceDetResultJsonObject(jsonObject));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mSingleQueue.cancelAll("doFaceDet");
                hideLoadingProgress();
                showSnackbar("网络或服务器错误", button_face_det);

            }
        });
        jsonObjectRequest.setTag("doFaceDet");
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//还是会重复提交
        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000,0, 0f));//无法超时了，看来取消最合适
        mSingleQueue.add(jsonObjectRequest);
        hideLoadingProgress();


    }

    private FaceDetResult fromFaceDetResultJsonObject(JSONObject jsonObject) {

        try {
            String id = jsonObject.getString("id");
            int face_num = jsonObject.getInt("face_num");
            JSONArray face_rectArray = jsonObject.getJSONArray("face_rect");
            List<Face_Rect> face_rectList = new ArrayList<Face_Rect>();
            for (int i = 0; i < face_rectArray.length(); i++) {
                JSONObject jo = (JSONObject) face_rectArray.get(i);

                Face_Rect face_rect = new Face_Rect(jo.getInt("left"), jo.getInt("top"), jo.getInt("width"), jo.getInt("height"));
                face_rectList.add(face_rect);
            }

            int errorcode = jsonObject.getInt("errorcode");
            return new FaceDetResult(id, face_num, face_rectList, errorcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

   private void showSnackbar(@NonNull CharSequence text, View view) {
        final Snackbar snackbar = Snackbar.make(
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
    private void showLoadingProgress() {
        contentLoadingProgress.show();
    }
    private void hideLoadingProgress() {
        contentLoadingProgress.hide();
    }

}
