package com.example.administrator.myapplication.fileupload;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.administrator.myapplication.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class UploadFaceListActivity extends AppCompatActivity {
    private static final String TAG=UploadFaceListActivity.class.getSimpleName();
    private ArrayList<UploadFace> uploadFaces;
    private UploadFaceAdapter adapter;
    private ListView uploadFaceListView;
    private Dialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfacelist);
        uploadFaceListView= (ListView) findViewById(R.id.listview_uploadface);
        uploadFaces=new ArrayList<UploadFace>();
        adapter=new UploadFaceAdapter(this,uploadFaces);
        uploadFaceListView.setAdapter(adapter);
        fetchUploadFaces();
        showPDialog();
    }

    private void fetchUploadFaces() {

        JsonArrayRequest req=new JsonArrayRequest(Constant.UploadFaceListUrl, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        hidePDialog();
                    }
                });
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject object=jsonArray.getJSONObject(i);
                        UploadFace uploadFace=new UploadFace();
                        uploadFace.setDate(object.getString("date"));
                        uploadFace.setiMgFileName(object.getString("imgFileiName"));
                        uploadFace.setThumbnailImgFileName(object.getString("thumbnailImgFileName"));
                        uploadFaces.add(uploadFace);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //注意刷新数据
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,"error:"+volleyError.getMessage());
                hidePDialog();
            }
        });
        VolleyController.getInstance(this).addToRequestQueue(req);
    }

    public void hidePDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
        pDialog=null;
    }
    public void showPDialog() {
        if (pDialog == null){
            pDialog = CustomProgressDialog.createLoadingDialog(this,"Loading...");
        }
        //pDialog.setMessage("Loading...");
        pDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


}
